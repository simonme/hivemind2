import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Function;

/**
 * Created by Simon on 15.05.2017.
 */
public class Condition {

    private ArrayList<IntervalPredicate> intervalPredicates;

    public Condition(ArrayList<IntervalPredicate> predicates) {
        this.intervalPredicates = predicates;
    }

    public Condition(Scanner scanner)
    {
        intervalPredicates = new ArrayList<>();
        while (scanner.hasNext())
        {
            intervalPredicates.add(new IntervalPredicate(scanner));
        }
    }

    public void serialize(CSVWriter writer)
    {
        for (IntervalPredicate predicate :
                intervalPredicates) {
            predicate.serialize(writer);
        }
    }

    public boolean matches(Situation sigmaT) {
        ArrayList<Double>features = sigmaT.getFeatures();
        for (int i = 0; i < features.size(); i++){
            if (!(intervalPredicates.get(i).contains(features.get(i))))
                return false;
        }
        return true;
    }

    public boolean isMoreGeneral(Condition other)
    {
        for (int i = 0; i < intervalPredicates.size(); i++){
            if (!(intervalPredicates.get(i).isMoreGeneral(other.intervalPredicates.get(i))))
                return false;
        }
        return true;
    }

    public void crossover(Condition other, Random random)
    {
        int conditionLength = intervalPredicates.size() * 2;
        double start = random.nextDouble() * conditionLength;
        double end = random.nextDouble() * conditionLength;
        if(start > end)
        {
            double tmp = start;
            start = end;
            end = tmp;
        }
        if(start % 2 >= 1.0)
        {
            int index = (int)Math.floor(start / 2);
            start++;
            intervalPredicates.get(index).crossover(other.intervalPredicates.get(index), true);
        }
        if(end % 2 >= 1.0)
        {
            int index = (int)Math.floor(end / 2);
            end--;
            intervalPredicates.get(index).crossover(other.intervalPredicates.get(index), false);
        }
        start /= 2;
        end /= 2;
        for(int index = (int)Math.ceil(start); index < end; index++)
        {
            IntervalPredicate tmp = intervalPredicates.get(index);
            intervalPredicates.set(index, other.intervalPredicates.get(index));
            other.intervalPredicates.set(index, tmp);
        }
    }

    public void mutate(Random random)
    {
        for (IntervalPredicate predicate :
                intervalPredicates) {
            predicate.mutate(random);
        }
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Condition))return false;
        Condition other = (Condition) obj;
        return this.intervalPredicates.equals(other.intervalPredicates);
    }

    @Override
    public int hashCode() {
        return this.intervalPredicates.hashCode();
    }
}
