import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * Created by Ferdi on 06.07.2017.
 */
public class GeneticAlgorithm {

    private double CROSSOVER_PROBABILITY = 0.4;
    private double MUTATION_PROBABILITY = 0.1;
    private int POPULATION_SIZE = 20;
    private LinkedHashSet<Parameters> population = new LinkedHashSet();

    private Parameters execute(int reward, Parameters lastUsedParameters){
        //update Fitness
        lastUsedParameters.setFitness(reward);
        //generate and insert Offsprings, remove excessive
        Parameters parent1 = pickRandomParameters(population);
        Parameters parent2 = pickRandomParameters(population);
        generateOffsprings(parent1, parent2);
        //select Parameters for next game
        Parameters p1 = pickRandomParameters(population);
        Parameters p2 = pickRandomParameters(population);
        while(p1 == p2){
            p2 = pickRandomParameters(population);
        }
        Parameters winner = determineBinaryTournamentSelectionWinner(p1, p2);
        //return Parameters to execute
        return winner;
    }

    private void dropWorst(HashSet<Parameters> population){
        Parameters worst = null;
        int minFitness = Integer.MAX_VALUE;
        for (Parameters p : population){
            if (p.getFitness() < minFitness){
                worst = p;
                minFitness = p.getFitness();
            }
        }
        population.remove(worst);
    }

    private Parameters determineBinaryTournamentSelectionWinner(Parameters pms1, Parameters pms2){
        if (pms1.getFitness() > pms2.getFitness())
            return pms1;
        else return pms2;
    }

    private void generateOffsprings(Parameters parent1, Parameters parent2){
        Parameters offspring1 = new Parameters(parent1);
        Parameters offspring2 = new Parameters(parent2);
        int offspringFitness = (int)(offspring1.getFitness() + offspring2.getFitness() / 2);
        offspring1.setFitness(offspringFitness);
        offspring2.setFitness(offspringFitness);
        if (Math.random() <= CROSSOVER_PROBABILITY)
            crossover(offspring1, offspring2);
        offspring1.mutate(MUTATION_PROBABILITY);
        offspring2.mutate(MUTATION_PROBABILITY);
        if(population.size() + 2 > POPULATION_SIZE) {
            dropWorst(population);
            dropWorst(population);
        }
        population.add(offspring1);
        population.add(offspring2);
    }


    //TODO
    private void crossover(Parameters offspring1 , Parameters offspring2){}


    private Parameters pickRandomParameters(HashSet<Parameters> population){
        int randomIndex = (int)(population.size()*Math.random());
        return (Parameters) population.toArray()[randomIndex];
    }

}
