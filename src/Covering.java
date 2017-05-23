import java.util.ArrayList;

/**
 * Created by Ferdi on 23.05.2017.
 */
public class Covering {

    public Covering() {
    }


    public double calculateBound(double feature, double rand, double sign){
        double bound;
        if (sign < 0.5)
            bound = feature + rand;
        else
            bound = feature - rand;
        if (bound > 1.0)
            bound = 1.0;
        else if (bound < 0.0)
            bound = 0.0;
        return bound;
    }

    public IntervalPredicate generateIntervalPredicate(double feature){
        double sign1 = Math.random();
        double sign2 = Math.random();
        double rand1 = Math.random() * XCSConfig.r0;
        double rand2 = Math.random() * XCSConfig.r0;
        double lower = calculateBound(feature, rand1, sign1);
        double upper = calculateBound(feature, rand2, sign2);
        if (lower > upper){
            double temp = lower;
            lower = upper;
            upper = temp;
        }
        return new IntervalPredicate(lower, upper);
    }

    public int getActualTime(){ //TODO: liefere aktuellen Zeitschritt
        return Integer.MIN_VALUE;
    }

    public Classifier generateCoveringClassifier(Situation sigmaT, Action action){
        ArrayList<IntervalPredicate>intervalPredicates = new ArrayList<IntervalPredicate>();
        for (int i = 0; i < sigmaT.getFeatures().size(); i++){
            intervalPredicates.add(generateIntervalPredicate(sigmaT.getFeatures().get(i)));
        }
        Condition condition = new Condition(intervalPredicates);
        return new Classifier(condition,
                action,
                XCSConfig.predictionI,
                XCSConfig.epsilonI,
                XCSConfig.FI,
                0,
                getActualTime());
    }

}
