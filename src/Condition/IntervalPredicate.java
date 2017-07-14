package Condition;

import Serialization.CSVWriter;

import java.security.InvalidParameterException;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Ferdi on 23.05.2017.
 */
public class IntervalPredicate extends PredicateBase {
    IntervalPredicate(double lowerBound, double upperBound) throws InvalidParameterException {
        if (lowerBound > upperBound) {
            throw new InvalidParameterException("lowerBound has to be less than or equal to upperBound");
        }
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    IntervalPredicate(Scanner scanner) {
        this.lowerBound = Double.parseDouble(scanner.next());
        this.upperBound = Double.parseDouble(scanner.next());
    }

    void serialize(CSVWriter writer) {
        writer.write(this.lowerBound);
        writer.write(this.upperBound);
    }


    boolean contains(double d) {
        return (lowerBound <= d) && (upperBound >= d);
    }

    void crossover(IntervalPredicate other, boolean firstHalf) {
        super.crossover(other, firstHalf);
        reorder();
        other.reorder();
    }

    void mutate(Random random) {
        super.mutate(random);
        reorder();
    }

    private void reorder() {
        if (lowerBound > upperBound) {
            double tmp = lowerBound;
            lowerBound = upperBound;
            upperBound = tmp;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof IntervalPredicate)) return false;
        IntervalPredicate other = (IntervalPredicate) obj;
        return this.lowerBound == other.lowerBound
                && this.upperBound == other.upperBound;
    }

    @Override
    public int hashCode() {
        return 31 * Objects.hashCode(lowerBound) + 23 * Objects.hashCode(upperBound);
    }

}
