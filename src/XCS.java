import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by Simon on 15.05.2017.
 */
public class XCS implements AI {
    private ArrayList<Action> possibleActions;
    private LinkedHashSet<Classifier> population;
    private XCSConfig xcsConfig;
    private Covering covering;
    private int timestamp;
    private LinkedList<Set<Classifier>> actionSetHistory;
    private Random random;

    public XCS(ArrayList<Action> possibleActions) {
        this.possibleActions = possibleActions;
        this.xcsConfig = new XCSConfig();// TODO not necessary, all XCSConfig parameters are public static final XY
        covering = new Covering();
        population = new LinkedHashSet<>();
        timestamp = 0;
        actionSetHistory = new LinkedList<>();
        this.random = new Random();
    }

    public XCS(ArrayList<Action> possibleActions, Scanner reader) {
        this(possibleActions);
        while(reader.hasNextLine())
        {
            String line = reader.nextLine();
            System.out.println("Reading line: " + line);
            Scanner classifier = new Scanner(line);
            classifier.useDelimiter(Character.toString(CSVWriter.VALUE_DELIMITER));
            population.add(new Classifier(classifier, possibleActions));
        }
    }

    @Override
    public void serialize(CSVWriter writer) {
        for (Classifier classifier:
             population) {
            classifier.serialize(writer, possibleActions);
            writer.newLine();
        }
    }

    @Override
    public Action step(Situation sigmaT, double reward) {
        if(actionSetHistory.size() > 0) { // we could also test if timestamp > 0

            actionSetHistory.forEach(actionSet -> {
                final int actionSetSize = actionSet.stream().mapToInt(Classifier::getNumerosity).sum();
                double accuracySum = 0;
                LinkedHashMap<Classifier, Double> accuracies = new LinkedHashMap<>();
                for (Classifier classifier1 : actionSet) {
                    if (classifier1.getError() < XCSConfig.epsilon0) accuracies.put(classifier1, 1.0);
                    else accuracies.put(classifier1, XCSConfig.alpha * Math.pow(classifier1.getError() / XCSConfig.epsilon0, XCSConfig.nu));
                    accuracySum += accuracies.get(classifier1) * classifier1.getNumerosity();
                }
                for (Classifier classifier : actionSet) {
                    classifier.update(reward, actionSetSize, accuracies.get(classifier), accuracySum);
                }
            });

            // Only using A and A_-1 right now, so discard older action sets
            if(actionSetHistory.size() > 1) {
                actionSetHistory.removeLast();
            }
        }
        timestamp++;
        Set<Classifier> matchSet = generateMatchSet(sigmaT);
        Map<Action, Double> predictionArray = generatePredictionArray(matchSet);
        Action chosenAction = selectAction(predictionArray);
        actionSetHistory.addFirst(generateActionSet(sigmaT, chosenAction, matchSet));
        return chosenAction;
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
            Classifier clc = covering.generateCoveringClassifier(sigmaT, pickRandomActionNotPresentInSet(matchSet), timestamp);
            population.add(clc);
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
        Action randomAction = null;
        while (randomAction == null) {
            int randomIndex = (int) (Math.random() * possibleActions.size());
            if (!set.contains(possibleActions.get(randomIndex)))
                randomAction = possibleActions.get(randomIndex);
        }
        return randomAction;
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
        int sumNumerosityInPopulation = 0;
        double sumFitnessInPopulation = 0;
        for (Classifier cl: population) {
            sumNumerosityInPopulation += cl.getNumerosity();
            sumFitnessInPopulation += cl.getFitness();
        }
        if(sumNumerosityInPopulation < XCSConfig.N) {
            return;
        }

        double averageFitnessInPopulation = sumFitnessInPopulation / sumNumerosityInPopulation;
        double voteSum = 0;
        for (Classifier cl: population) {
            voteSum += deletionVote(cl, averageFitnessInPopulation);
        }
        double choicePoint = Math.random() * voteSum;
        voteSum = 0;
        for (Classifier cl: population) {
            voteSum += deletionVote(cl, averageFitnessInPopulation);
            if(voteSum > choicePoint)
            {
                if(cl.getNumerosity() > 1)
                {
                    cl.setNumerosity(cl.getNumerosity() - 1);
                }
                else
                {
                    population.remove(cl);
                }
                return;
            }
        }
    }

    private double deletionVote(Classifier cl, double averageFitnessInPopulation) {
        double vote = cl.getMeanActionSetSize() * cl.getNumerosity();
        double relativeFitness = cl.getFitness() / cl.getNumerosity();
        if((cl.getExperience() > XCSConfig.thetaDEL) && (relativeFitness > (XCSConfig.delta * averageFitnessInPopulation)))
        {
            vote *= averageFitnessInPopulation / relativeFitness;
        }
        return vote;
    }
}
