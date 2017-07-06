package Situation;

import Position.RelativePosition;

import bwapi.Position;
import bwapi.Unit;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Jakob on 06.07.2017.
 */
public class SituationFactoryBase implements ISituationFactory {

    @Override
    public Situation create(Unit unit, Unit closestEnemy, HashSet<Unit> enemyUnits, HashSet<Unit> alliedUnits)
    {
        return new Situation(createArray(unit, closestEnemy, enemyUnits, alliedUnits));
    }

    protected ArrayList<Double> createArray(Unit unit, Unit closestEnemy, HashSet<Unit> enemyUnits, HashSet<Unit> alliedUnits)
    {
        ArrayList<Double> features = new ArrayList<>();
        features.add((double) unit.getHitPoints());
        if (closestEnemy != null) {
            features.add((double) closestEnemy.getHitPoints());
            features.add((double) closestEnemy.getDistance(unit));
            features.add(RelativePosition.computeAngle(unit.getPosition(), closestEnemy.getPosition()) * 360 / (2*Math.PI));
            features.add((double)closestEnemy.getType().groundWeapon().maxRange());
        } else {
            features.add((double) -10000);
            features.add((double) -10000);
            features.add((double) -10000);
        }
        features.add((double) unit.getGroundWeaponCooldown());
        //System.out.println("Anzahl gegnerischer Einheiten: " + enemyUnits.size());
        features.add((double) enemyUnits.size());
        if (enemyUnits.size() > 0) {
            features.add(unit.getPosition().getDistance(calculateCentreOfMass(enemyUnits))); // Distanz zum Masseschwerpunkt der Gegner
            features.add(RelativePosition.computeAngle(unit.getPosition(), calculateCentreOfMass(enemyUnits)) * 360 / (2*Math.PI)); // Relative Position zum Masseschwerpunkt der Gegner
        } else {
            features.add((double) -10000);
            features.add((double) -10000);
        }
        //System.out.println("Anzahl verbündeter Einheiten: " + alliedUnits.size());
        features.add((double) alliedUnits.size());
        features.add(unit.getPosition().getDistance(calculateCentreOfMass(alliedUnits))); // Distanz zum Masseschwerpunkt der Verbündeten
        features.add(RelativePosition.computeAngle(unit.getPosition(), calculateCentreOfMass(alliedUnits)) * 360 / (2*Math.PI)); // Relative Position zum Masseschwerpunkt der Verbündeten

        return features;
    }

    private static Position calculateCentreOfMass(HashSet<Unit> units){
        double x = -10000;
        double y = -10000;
        if (units.size() > 0) {
            x = 0;
            y = 0;
            for (Unit unit : units) {
                x += unit.getX();
                y += unit.getY();
            }
            x = x / units.size();
            y = y / units.size();
        }
        return new Position((int) Math.round(x), (int) Math.round(y));
    }

    private static double calculateDistance(Position pos1, Position pos2){
        return (Math.sqrt( Math.pow((pos1.getX() - pos2.getX()), 2) + Math.pow((pos1.getY() - pos2.getY()), 2) ));
    }
}
