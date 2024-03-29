package Condition;

import Configuration.XCSConfig;
import Situation.Situation;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Jakob on 01.06.2017.
 */
public class PredicateFactory {

    private double[] boundaries;

    public PredicateFactory(double[] boundaries) {
        this.boundaries = boundaries;
    }

    public ArrayList<PredicateBase> deserializePredicates(Scanner scanner) {
        ArrayList<PredicateBase> intervalPredicates = new ArrayList<>();
        for (double boundary :
                boundaries) {
            if (boundary == 0.0) {
                intervalPredicates.add(new IntervalPredicate(scanner));
            } else {
                intervalPredicates.add(new WrappingIntervalPredicate(scanner, boundary));
            }
        }
        return intervalPredicates;
    }

    private double spread(double feature, double sign) {
        double rand = Math.random() * XCSConfig.r0;
        double bound;
        if (sign < 0.5)
            bound = feature + rand;
        else
            bound = feature - rand;
        return bound;
    }

    private PredicateBase generateIntervalPredicate(double feature) {
        double sign = Math.random();
        double lower = spread(feature, sign);
        double upper = spread(feature, 1 - sign);
        if (lower > upper) {
            double temp = lower;
            lower = upper;
            upper = temp;
        }
        return new IntervalPredicate(lower, upper);
    }

    private PredicateBase generateWrappingIntervalPredicate(double feature, double wrapValue) {
        double sign = Math.random();
        double lower = spread(feature, sign);
        double upper = spread(feature, 1 - sign);
        return new WrappingIntervalPredicate(lower, upper, wrapValue);
    }

    public ArrayList<PredicateBase> coverSituation(Situation sigmaT) {
        ArrayList<PredicateBase> intervalPredicates = new ArrayList<>();
        for (int index = 0; index < boundaries.length; index++) {
            if (boundaries[index] == 0.0) {
                intervalPredicates.add(generateIntervalPredicate(sigmaT.getFeatures().get(index)));
            } else {
                intervalPredicates.add(generateWrappingIntervalPredicate(sigmaT.getFeatures().get(index), boundaries[index]));
            }
        }
        return intervalPredicates;
    }
}
