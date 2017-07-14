import Actions.Action;
import Condition.PredicateFactory;
import Configuration.SelectionType;
import Configuration.XCSConfig;
import Serialization.CSVWriter;
import Situation.Situation;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Simon on 15.05.2017.
 */
public class XCS implements AI {
    private ArrayList<Action> possibleActions;
    private LinkedHashSet<Classifier> population;
    private XCSConfig xcsConfig;
    private Covering covering;
    private int timestamp;
    private Map<Integer, LinkedList<Set<Classifier>>> actionSetHistory;
    private Random random;

    public XCS(ArrayList<Action> possibleActions, PredicateFactory predicateFactory) {
        this.possibleActions = possibleActions;
        this.xcsConfig = new XCSConfig();// TODO not necessary, all Configuration.XCSConfig parameters are public static final XY
        this.covering = new Covering(predicateFactory);
        population = new LinkedHashSet<>();
        timestamp = 0;
        actionSetHistory = new HashMap<>();
        this.random = new Random();
    }

    public XCS(ArrayList<Action> possibleActions, Scanner reader, PredicateFactory predicateFactory) {
        this(possibleActions, predicateFactory);
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            if (line.split(";").length != 18) {
                break;
            }

            Scanner classifier = new Scanner(line);
            classifier.useDelimiter(Character.toString(CSVWriter.VALUE_DELIMITER));
            Classifier cl = new Classifier(classifier, possibleActions, predicateFactory);
            // Set LastGA to current; maybe save timestamp and LastGA to CSV?
            cl.setLastGA(timestamp);
            population.add(cl);
            cl.hashOnEnter = cl.hashCode();
        }
        System.out.println("Reloaded existing population (" + population.stream().mapToInt(Classifier::getNumerosity).sum() + " classifiers)");
    }

    @Override
    public void serialize(CSVWriter writer) {
        //System.out.println("Population count: " + population.stream().mapToInt(Classifier::getNumerosity).sum());
        for (Classifier classifier :
                population) {
            classifier.serialize(writer, possibleActions);
            writer.newLine();
        }
    }

    @Override
    public Action step(Situation sigmaT, double reward, int unitID) {
        if (actionSetHistory.containsKey(unitID) == false) {
            actionSetHistory.put(unitID, new LinkedList<>());
        }
        if (actionSetHistory.get(unitID).size() > 0) { // we could also test if timestamp > 0

            actionSetHistory.get(unitID).forEach(actionSet -> {
                final int actionSetSize = actionSet.stream().mapToInt(Classifier::getNumerosity).sum();
                double accuracySum = 0;
                LinkedHashMap<Classifier, Double> accuracies = new LinkedHashMap<>();
                for (Classifier classifier1 : actionSet) {
                    if (classifier1.getError() < XCSConfig.epsilon0) accuracies.put(classifier1, 1.0);
                    else
                        accuracies.put(classifier1, XCSConfig.alpha * Math.pow(classifier1.getError() / XCSConfig.epsilon0, XCSConfig.nu));
                    accuracySum += accuracies.get(classifier1) * classifier1.getNumerosity();
                }
                for (Classifier classifier : actionSet) {
                    classifier.update(reward, actionSetSize, accuracies.get(classifier), accuracySum);
                }
                if (XCSConfig.doActionSetSubsumption) {
                    actionSetSubsumption(actionSet);
                }
                if (actionSet.size() > 0) { // TODO: why could this ever be empty?
                    runGA(actionSet);
                }
            });

            // Only using A and A_-1 right now, so discard older action sets
            if (actionSetHistory.get(unitID).size() > 1) {
                actionSetHistory.get(unitID).removeLast();
            }
        }
        timestamp++;
        Set<Classifier> matchSet = generateMatchSet(sigmaT);
        Map<Action, Double> predictionArray = generatePredictionArray(matchSet);
        Action chosenAction = selectAction(predictionArray);
        actionSetHistory.get(unitID).addFirst(generateActionSet(sigmaT, chosenAction, matchSet));
        return chosenAction;
    }

    private void actionSetSubsumption(Set<Classifier> actionSet) {
        Classifier cl = null;
        for (Classifier classifier :
                actionSet) {
            if (couldSubsume(classifier)) {
                if (cl == null || classifier.getCondition().isMoreGeneral(cl.getCondition())) {
                    cl = classifier;
                }
            }
        }
        if (cl != null) {
            for (Classifier classifier :
                    actionSet) {
                if (cl.getCondition().isMoreGeneral(classifier.getCondition())) {
                    cl.setNumerosity(cl.getNumerosity() + classifier.getNumerosity());
                    actionSet.remove(classifier);
                    population.remove(classifier);
                }
            }
        }
    }

    public int numberOfDistinctActions(Set<Classifier> set) {
        ArrayList<Action> distinctActions = new ArrayList<>();
        for (Classifier cl : set) {
            if (!(distinctActions.contains(cl.getAction())))
                distinctActions.add(cl.getAction());
        }
        return distinctActions.size();
    }

    private Set<Classifier> generateMatchSet(Situation sigmaT) {
        LinkedHashSet<Classifier> matchSet = new LinkedHashSet();
        for (Classifier cl : population) {
            if (cl.matches(sigmaT))
                matchSet.add(cl);
        }
        //Covering, wenn zu wenig Aktionen im Match Set
        while (numberOfDistinctActions(matchSet) < XCSConfig.thetaMNA) {
            Action newAction = pickRandomActionNotPresentInSet(matchSet);
            Classifier clc = covering.generateCoveringClassifier(sigmaT, newAction, timestamp);
            population.add(clc);
            clc.hashOnEnter = clc.hashCode();
            deleteFromPopulation();
            matchSet.add(clc);
        }
        return matchSet;
    }

    private Map<Action, Double> generatePredictionArray(Set<Classifier> matchSet) {
        LinkedHashMap<Action, Double> predictionArray = new LinkedHashMap<>();
        LinkedHashMap<Action, Double> fitnessSumArray = new LinkedHashMap<>();
        HashSet<Action> distinctActions = new HashSet<>();

        matchSet.forEach(classifier -> distinctActions.add(classifier.getAction()));

        for (Action a : distinctActions) {
            predictionArray.put(a, 0.0);
            fitnessSumArray.put(a, 0.0);
        }
        for (Classifier cl : matchSet) {
            predictionArray.put(cl.getAction(), predictionArray.get(cl.getAction()) + cl.getFitness() * cl.getPrediction());
            fitnessSumArray.put(cl.getAction(), fitnessSumArray.get(cl.getAction()) + cl.getFitness());
        }

        LinkedHashMap<Action, Double> resultPredictionArray = new LinkedHashMap<>();
        for (Action a : distinctActions) {
            if (fitnessSumArray.get(a) != 0.0)
                resultPredictionArray.put(a, predictionArray.get(a) / fitnessSumArray.get(a));
        }
        return resultPredictionArray;
    }

    private Action pickRandomAction(Set<Action> actions) {
        int index = random.nextInt(actions.size());
        Iterator<Action> iter = actions.iterator();
        for (int i = 0; i < index; i++) {
            iter.next();
        }
        return iter.next();
    }

    private Action pickRandomActionNotPresentInSet(Set<Classifier> set) {
        ArrayList<Action> unusedActions = new ArrayList<>();
        Set<Action> usedActions = set.stream().map(Classifier::getAction).collect(Collectors.toSet());
        for (int i = 0; i < possibleActions.size(); i++) {
            Action action = possibleActions.get(i);
            if (!usedActions.contains(action)) {
                unusedActions.add(action);
            }
        }
        int randomIndex = (int) (Math.random() * unusedActions.size());
        return unusedActions.get(randomIndex);
    }


    private Action selectAction(Map<Action, Double> predictionArray) {
        Action chosenAction = null;
        if (Math.random() > XCSConfig.pExp) {
            for (Action action : predictionArray.keySet()) {
                if ((chosenAction == null) || (predictionArray.get(chosenAction) < predictionArray.get(action))) {
                    chosenAction = action;
                }
            }
        } else {
            chosenAction = pickRandomAction(predictionArray.keySet());
        }
        return chosenAction;
    }

    private Set<Classifier> generateActionSet(Situation sigmaT, Action action, Set<Classifier> matchSet) {
        LinkedHashSet<Classifier> actionSet = new LinkedHashSet();
        for (Classifier cl : matchSet) {
            if (cl.getAction() == action)
                actionSet.add(cl);
        }
        return actionSet;
    }

    private void deleteFromPopulation() {
        int sumNumerosityInPopulation = population.stream().mapToInt(Classifier::getNumerosity).sum();
        double sumFitnessInPopulation = population.stream().mapToInt(Classifier::getNumerosity).sum();
        if (sumNumerosityInPopulation < XCSConfig.N) {
            return;
        }

        double averageFitnessInPopulation = sumFitnessInPopulation / sumNumerosityInPopulation;
        double voteSum = 0;
        for (Classifier cl : population) {
            voteSum += deletionVote(cl, averageFitnessInPopulation);
        }
        double choicePoint = Math.random() * voteSum;
        voteSum = 0;
        for (Classifier cl : population) {
            voteSum += deletionVote(cl, averageFitnessInPopulation);
            if (voteSum > choicePoint) {
                if (cl.getNumerosity() > 1) {
                    cl.setNumerosity(cl.getNumerosity() - 1);
                } else {
                    boolean deleteSuccessful = population.remove(cl);
                    if (!deleteSuccessful) {
                        if (cl.hashOnEnter != cl.hashCode()) {
                            System.out.println("Delete: HashCode changed");
                        }
                    }
                }
                return;
            }
        }
    }

    private double deletionVote(Classifier cl, double averageFitnessInPopulation) {
        double vote = cl.getMeanActionSetSize() * cl.getNumerosity();
        double relativeFitness = cl.getFitness() / cl.getNumerosity();
        if ((cl.getExperience() > XCSConfig.thetaDEL) && (relativeFitness > (XCSConfig.delta * averageFitnessInPopulation))) {
            vote *= averageFitnessInPopulation / relativeFitness;
        }
        return vote;
    }

    private void runGA(Set<Classifier> actionSet) {
        if (timestamp - actionSet.stream().mapToInt(classifier -> classifier.getLastGA() * classifier.getNumerosity()).sum() / actionSet.stream().mapToInt(Classifier::getNumerosity).sum() > XCSConfig.thetaGA) {
            for (Classifier classifier :
                    actionSet) {
                classifier.setLastGA(timestamp);
            }
            Classifier parent1 = selectParent(actionSet);
            Classifier parent2 = selectParent(actionSet);
            Classifier child1 = parent1.copy();
            Classifier child2 = parent2.copy();
            child1.setNumerosity(1);
            child1.setTimeStamp(timestamp);
            child1.setExperience(0);
            child2.setNumerosity(1);
            child2.setTimeStamp(timestamp);
            child2.setExperience(0);

            if (random.nextDouble() < XCSConfig.chi) {
                child1.getCondition().crossover(child2.getCondition(), random);
                double prediction = (parent1.getPrediction() + parent2.getPrediction()) / 2;
                double error = 0.25 * (parent1.getError() + parent2.getError()) / 2;
                double fitness = 0.1 * (parent1.getFitness() + parent2.getFitness()) / 2;

                child1.setPrediction(prediction);
                child1.setError(error);
                child1.setFitness(fitness);
                child2.setPrediction(prediction);
                child2.setError(error);
                child2.setFitness(fitness);
            }

            continueGA(parent1, parent2, child1);
            continueGA(parent1, parent2, child2);
        }
    }

    private void continueGA(Classifier parent1, Classifier parent2, Classifier child) {
        child.getCondition().mutate(random);
        if (random.nextDouble() > XCSConfig.my) {
            Action action;
            do {
                action = possibleActions.get(random.nextInt(possibleActions.size()));
            } while (action == child.getAction());
            child.setAction(action);
        }
        if (XCSConfig.doGASubsumption) {
            if (doesSubsume(parent1, child)) {
                parent1.setNumerosity(parent1.getNumerosity() + 1);
            } else if (doesSubsume(parent2, child)) {
                parent2.setNumerosity(parent2.getNumerosity() + 1);
            } else {
                InsertInPopulation(child);
            }
        } else {
            InsertInPopulation(child);
        }
        deleteFromPopulation();
    }

    private void InsertInPopulation(Classifier child) {
        for (Classifier classifier :
                population) {
            if (classifier.getCondition().equals(child.getCondition()) && classifier.getAction().equals(child.getAction())) {
                classifier.setNumerosity(classifier.getNumerosity() + 1);
                return;
            }
        }
        population.add(child);
        child.hashOnEnter = child.hashCode();
    }

    private boolean doesSubsume(Classifier parent, Classifier child) {
        if (parent.getAction().equals(child.getAction())) {
            if (couldSubsume(parent)) {
                if (parent.getCondition().isMoreGeneral(child.getCondition())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean couldSubsume(Classifier parent) {
        if (parent.getExperience() > XCSConfig.thetaSUB) {
            if (parent.getError() < XCSConfig.epsilon0) {
                return true;
            }
        }
        return false;
    }

    private Classifier selectParent(Set<Classifier> actionSet) {
        Classifier parent = null;
        if (XCSConfig.selectionType == SelectionType.ROULETTE_WHEEL) {
            parent = doRouletteWheelSelection(actionSet);
        } else if (xcsConfig.selectionType == SelectionType.TOURNAMENT) {
            parent = doTournamentSelection(actionSet);
        }
        return parent;
    }


    private Classifier doTournamentSelection(Set<Classifier> actionSet) {
        Classifier parent = null;
        int tournamentSize = (int) Math.round(actionSet.size() * XCSConfig.tournamentSize);
        if (tournamentSize <= 1)
            tournamentSize = 2;
        int maxFitness = Integer.MIN_VALUE;
        ArrayList<Classifier> actionSetList = new ArrayList(actionSet);
        ArrayList<Classifier> selection = new ArrayList();
        while (selection.size() < tournamentSize) {
            selection.add(actionSetList.get((int) (Math.random() * actionSetList.size())));
        }
        for (Classifier cl : selection) {
            if (cl.getFitness() >= maxFitness) {
                parent = cl;
            }
        }
        return parent;
    }

    private Classifier doRouletteWheelSelection(Set<Classifier> actionSet) {
        double fitnessSum = actionSet.stream().mapToDouble(Classifier::getFitness).sum();
        double choicePoint = random.nextDouble() * fitnessSum;
        fitnessSum = 0;
        for (Classifier classifier :
                actionSet) {
            fitnessSum += classifier.getFitness();
            if (fitnessSum > choicePoint) {
                return classifier;
            }
        }
        throw new IndexOutOfBoundsException();
    }

}
