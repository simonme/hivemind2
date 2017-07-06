package Condition;

import Configuration.XCSConfig;
import Serialization.CSVWriter;

import java.util.Random;

/**
 * Created by Jakob on 01.06.2017.
 */
public abstract class PredicateBase {

    protected double lowerBound;
    protected double upperBound;

    abstract void serialize(CSVWriter writer);

    abstract boolean contains(double d);

    boolean isMoreGeneral(PredicateBase other)
    {
        return (this.lowerBound <= other.lowerBound) && (this.upperBound >= other.upperBound);
    }

    void crossover(PredicateBase other, boolean firstHalf)
    {
        if(firstHalf)
        {
            double tmp = this.lowerBound;
            this.lowerBound = other.lowerBound;
            other.lowerBound = tmp;
        }
        else
        {
            double tmp = this.upperBound;
            this.upperBound = other.upperBound;
            other.upperBound = tmp;
        }
    }

    void mutate(Random random)
    {
        double sign1 = random.nextDouble();
        double sign2 = random.nextDouble();
        double rand1 = random.nextDouble() * XCSConfig.m0;
        double rand2 = random.nextDouble() * XCSConfig.m0;
        lowerBound += rand1 * sign1 >= 0.5 ? 1 : -1;
        upperBound += rand2 * sign2 >= 0.5 ? 1 : -1;
    }
}
