import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * Created by Ferdi on 06.07.2017.
 */
public class GeneticAlgorithm {

    private LinkedHashSet<Parameters> population = new LinkedHashSet();

    private Parameters execute(int reward, Parameters lastUsedParameters){
        //update Fitness
        lastUsedParameters.setFitness(reward);
        //generate and insert Offsprings, remove excessive
            //TODO
        //select Parameters for next game
        Parameters p1 = pickRandomParameters(population);
        Parameters p2 = pickRandomParameters(population);
        Parameters winner = determineBinaryTournamentSelectionWinner(p1, p2);
        //return Parameters to execute
        return winner;
    }

    //TODO
    private Parameters chooseParent(){
        return null;
    }

    private Parameters determineBinaryTournamentSelectionWinner(Parameters pms1, Parameters pms2){
        if (pms1.getFitness() > pms2.getFitness())
            return pms1;
        else return pms2;
    }

    //TODO
    private Parameters generateOffsprings(Parameters parent){
        return null;
    }

    //TODO
    private void crossover(Parameters offspring1 , Parameters offspring2){}


    private Parameters pickRandomParameters(HashSet<Parameters> population){
        int randomIndex = (int)(population.size()*Math.random());
        return (Parameters) population.toArray()[randomIndex];
    }

}
