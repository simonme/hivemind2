import bwapi.Unit;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Simon on 15.05.2017.
 */
public class Situation {

    private ArrayList<Double> features;

    public Situation(Unit unit, Unit closestEnemy) {
        features = new ArrayList<>();
        features.add((double) unit.getHitPoints());
        features.add((double) closestEnemy.getHitPoints());
        features.add((double) closestEnemy.getDistance(unit));
        features.add(RelativePosition.computeAngle(unit.getPosition(), closestEnemy.getPosition()));
    }

    public ArrayList<Double> getFeatures() {
        return features;
    }
}
