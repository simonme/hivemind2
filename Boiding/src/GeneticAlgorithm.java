import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Ferdi on 06.07.2017.
 */
public class GeneticAlgorithm {

    private Random random = new Random();
    private double CROSSOVER_PROBABILITY = 0.9;
    private double MUTATION_PROBABILITY = 2/11;
    private double MUTATION_DELTA = 0.15;
    public int POPULATION_SIZE = 20;
    private ArrayList<Parameters> population = new ArrayList();
    private ArrayList<Parameters> nextGeneration = new ArrayList<>();
    private Parameters lastUsedParameters;


    public Parameters execute(int rewardLastGame, int parametersIndex) {
        //update Fitness for last used parameters
        if (parametersIndex != 0)
            lastUsedParameters.setFitness(rewardLastGame);
        //instance next generation and reset index if every Parameters fitness has been determined
        if (parametersIndex == POPULATION_SIZE) {
            nextGeneration = new ArrayList<>();
            generateNextGeneration();
            this.lastUsedParameters = null;
            this.population = this.nextGeneration;
            this.lastUsedParameters = population.get(0);
            return population.get(0);
        }
        this.lastUsedParameters = population.get(parametersIndex);
        return population.get(parametersIndex);
    }

    public void generateNextGeneration(){
        for (int i = 0; i < POPULATION_SIZE / 2; i++){
            generateOffsprings();
        }
    }

    private Parameters determineBinaryTournamentSelectionWinner(Parameters pms1, Parameters pms2){
        if (pms1.getFitness() > pms2.getFitness())
            return pms1;
        else return pms2;
    }

    private void generateOffsprings(){
        Parameters parent1 = pickParent();
        Parameters parent2 = pickParent();
        while (parent1 == parent2)
            parent2 = pickParent();
        Parameters offspring1 = new Parameters(parent1);
        Parameters offspring2 = new Parameters(parent2);
        offspring1.setFitness(Integer.MIN_VALUE);
        offspring2.setFitness(Integer.MIN_VALUE);
        if (random.nextDouble() <= CROSSOVER_PROBABILITY)
            crossover(offspring1, offspring2);
        offspring1.mutate(MUTATION_PROBABILITY, MUTATION_DELTA);
        offspring2.mutate(MUTATION_PROBABILITY, MUTATION_DELTA);
        nextGeneration.add(offspring1);
        nextGeneration.add(offspring2);
    }

    private void crossover(Parameters offspring1 , Parameters offspring2){
        int conditionLength = 11;
        int cross = random.nextInt(9) + 1;
        ArrayList<Double> off1Start = (ArrayList<Double>) offspring1.getParameterList().subList(0, cross);
        ArrayList<Double> off1End = (ArrayList<Double>) offspring1.getParameterList().subList(cross, 10);
        ArrayList<Double> off2Start = (ArrayList<Double>) offspring2.getParameterList().subList(0, cross);
        ArrayList<Double> off2End = (ArrayList<Double>) offspring2.getParameterList().subList(cross, 10);
        off1Start.addAll(off2End);
        off2Start.addAll(off1End);
        offspring1.setParameterList(off1Start);
        offspring2.setParameterList(off2Start);
    }

    public Parameters pickParent(){
        Parameters param1 = pickRandomParameters();
        Parameters param2 = pickRandomParameters();
        return determineBinaryTournamentSelectionWinner(param1, param2);
    }

    private Parameters pickRandomParameters(){
        int randomIndex = random.nextInt(population.size());
        return population.get(randomIndex);
    }

}
