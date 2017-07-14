package Condition;

import Serialization.CSVWriter;

import java.util.Objects;
import java.util.Scanner;

/**
 * Created by Jakob on 01.06.2017.
 */
public class WrappingIntervalPredicate extends PredicateBase {

    private double wrappingValue;

    public WrappingIntervalPredicate(double lowerBound, double upperBound, double wrappingValue) {
        this.lowerBound = lowerBound % wrappingValue;
        this.upperBound = upperBound % wrappingValue;
        this.wrappingValue = wrappingValue;
    }

    public WrappingIntervalPredicate(Scanner scanner, double boundary) {
        this.lowerBound = Double.parseDouble(scanner.next());
        this.upperBound = Double.parseDouble(scanner.next());
        this.wrappingValue = boundary;
    }

    @Override
    void serialize(CSVWriter writer) {
        writer.write(this.lowerBound);
        writer.write(this.upperBound);
    }

    @Override
    boolean contains(double d) {
        double value = d % wrappingValue;
        if (lowerBound <= upperBound) {
            return (value >= lowerBound && value <= upperBound);
        } else {
            return (value <= lowerBound || value >= upperBound);
        }
    }

    @Override
    boolean isMoreGeneral(PredicateBase other) {
        if (this.lowerBound <= this.upperBound) {
            return (other.lowerBound >= this.lowerBound && other.upperBound <= this.upperBound);
        } else {
            return (other.lowerBound <= this.lowerBound && other.upperBound >= this.upperBound);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof WrappingIntervalPredicate)) return false;
        WrappingIntervalPredicate other = (WrappingIntervalPredicate) obj;
        return this.lowerBound == other.lowerBound
                && this.upperBound == other.upperBound;
    }

    @Override
    public int hashCode() {
        return 32 * Objects.hashCode(lowerBound) + 22 * Objects.hashCode(upperBound);
    }
}
