package Situation;

import Position.RelativePosition;

import bwapi.Unit;
import bwapi.Position;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Simon on 15.05.2017.
 */
public class Situation {

    private ArrayList<Double> features;

    /*
    * Situation.Situation with 9 features:
    * own HP, closestEnemy HP, closestEnemy distance, closestEnemy angle,
    * own ground weapon cooldown, enemyUnits size, distance to enemy centre of mass, angle to enemy com
    * allied units size, distance to allied centre of mass, angle to enemy centre of mass
    * */
    public Situation(ArrayList<Double> features) {
        this.features = features;
    }

    public ArrayList<Double> getFeatures() {
        return features;
    }


}
