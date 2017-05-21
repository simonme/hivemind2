import bwapi.*;
import java.util.HashSet;

public class Vulture {

    private final Mirror bwapi;
    private final HashSet<Unit> enemyUnits;
    final private Unit unit;
    private final AI ai;

    public Vulture(Unit unit, Mirror bwapi, HashSet<Unit> enemyUnits) {
        this.unit = unit;
        this.bwapi = bwapi;
        this.enemyUnits = enemyUnits;
        this.ai = new XCS();
    }

    public void step() {
        /**
         * TODO: XCS
         */
        final Situation sigmaT = new Situation();
        this.ai
        Unit target = getClosestEnemy();
//        move(target);
        this.unit.attack(target);
    }

    private void move(Unit target) {
        unit.move(new Position(target.getPosition().getX(), target.getPosition().getY()), false);
    }

    private void move(Unit target, double percentage) {
        unit.move(new Position(target.getPosition().getX(), target.getPosition().getY()), false);
        unit.getPosition();
    }

    private Unit getClosestEnemy() {
        Unit result = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Unit enemy : enemyUnits) {
            double distance = getDistance(enemy);
            if (distance < minDistance) {
                minDistance = distance;
                result = enemy;
            }
        }

        return result;
    }

    private double getDistance(Unit enemy) {
    	return this.unit.getPosition().getDistance(enemy.getPosition());
    }
}
