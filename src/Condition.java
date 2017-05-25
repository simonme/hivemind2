import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

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
