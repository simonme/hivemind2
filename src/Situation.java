import bwapi.Unit;
import bwapi.Position;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Simon on 15.05.2017.
 */
public class Situation {

    private ArrayList<Double> features;

    public Situation(Unit unit, Unit closestEnemy, HashSet<Unit> enemyUnits, HashSet <Unit> alliedUnits) {
        features = new ArrayList<>();
        features.add((double) unit.getHitPoints());
        if (closestEnemy != null) {
            features.add((double) closestEnemy.getHitPoints());
            features.add((double) closestEnemy.getDistance(unit));
            features.add(RelativePosition.computeAngle(unit.getPosition(), closestEnemy.getPosition()) * 360 / (2*Math.PI));
        } else {
            features.add((double) -10000);
            features.add((double) 0);
            features.add((double) -10000);
        }
        features.add((double) unit.getGroundWeaponCooldown());
        System.out.println("Anzahl gegnerischer Einheiten: " + enemyUnits.size());
        System.out.println("Anzahl verbündeter Einheiten: " + alliedUnits.size());
        features.add(calculateDistance(unit.getPosition(), calculateCentreOfMass(enemyUnits))); // Distanz zum Masseschwerpunkt der Gegner
        features.add(calculateDistance(unit.getPosition(), calculateCentreOfMass(alliedUnits))); // Distanz zum Masseschwerpunkt der Verbündeten
        features.add(RelativePosition.computeAngle(unit.getPosition(), calculateCentreOfMass(enemyUnits)) * 360 / (2*Math.PI)); // Relative Position zum Masseschwerpunkt der Gegner
        features.add(RelativePosition.computeAngle(unit.getPosition(), calculateCentreOfMass(alliedUnits)) * 360 / (2*Math.PI)); // Relative Position zum Masseschwerpunkt der Verbündeten
    }

    public ArrayList<Double> getFeatures() {
        return features;
    }

    public Position calculateCentreOfMass(HashSet <Unit> units){
        double x = 0;
        double y = 0;
        if (units.size() > 0) {
            for (Unit unit : units) {
                x += unit.getX();
                y += unit.getY();
            }
            x = x / units.size();
            y = y / units.size();
        }
        return new Position((int) Math.round(x), (int) Math.round(y));
    }

    double calculateDistance(Position pos1, Position pos2){
        return (Math.sqrt( Math.pow((pos1.getX() - pos2.getX()), 2) + Math.pow((pos1.getY() - pos2.getY()), 2) ));
    }

}
