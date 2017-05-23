import java.util.*;

/**
 * Created by Simon on 15.05.2017.
 */
public class XCS implements AI {
    private ArrayList<Action> possibleActions;
    private LinkedHashSet<Classifier> population;
    private XCSConfig xcsConfig;
    private Covering covering;

    public XCS() {
        this.xcsConfig = new XCSConfig();// TODO not necessary, all XCSConfig parameters are public static final XY
        covering = new Covering();
        population = new LinkedHashSet<>();
        // TODO: Load population from csv
    }

    @Override
    public Action step(Situation sigmaT) {
        // TODO update previous action set, if there is any
        Set<Classifier> matchSet = generateMatchSet(sigmaT);
        Map<Action, Double> predictionArray = generatePredictionArray(matchSet);
        Action chosenAction = selectAction(predictionArray);
        Set<Classifier> actionSet = generateActionSet(sigmaT, chosenAction, matchSet);
        // TODO
        return null;
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
            Classifier clc = covering.generateCoveringClassifier(sigmaT, pickRandomActionNotPresentInSet(matchSet));
            population.add(clc);
            deleteFromPopulation();
            matchSet.add(clc);
        }
        return matchSet;
    }

    private Map<Action, Double> generatePredictionArray(Set<Classifier> matchSet) {
        LinkedHashMap<Action, Double> predictionArray = new LinkedHashMap<>();
        LinkedHashMap<Action, Double> fitnessSumArray = new LinkedHashMap<>();
        for (Action action : possibleActions) {
            predictionArray.put(action, null);
            fitnessSumArray.put(action, 0.0);
        }
        for (Classifier cl : matchSet) {
            predictionArray.put(cl.getAction(), predictionArray.get(cl.getAction()) + cl.getFitness() * cl.getPrediction());
            fitnessSumArray.put(cl.getAction(), fitnessSumArray.get(cl.getAction()) + cl.getFitness());
        }
        for (Action action : possibleActions) {
            if (fitnessSumArray.get(action) != 0.0)
                predictionArray.put(action, predictionArray.get(action) / fitnessSumArray.get(action));
        }
        return predictionArray;
    }

    private Action pickRandomAction() {
        int randomIndex = (int) (Math.random() * possibleActions.size());
        return possibleActions.get(randomIndex);
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
        if (Math.random() > XCSConfig.pExp)
            for (Action action : predictionArray.keySet()) {
                if ((chosenAction == null) || (predictionArray.get(chosenAction) < predictionArray.get(action)))
                    chosenAction = action;
            }
        else {
            while (chosenAction == null) {
                Action possibleAction = pickRandomAction();
                if ((predictionArray.get(possibleActions) != null))
                    chosenAction = possibleAction;
            }
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
