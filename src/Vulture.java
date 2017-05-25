import bwapi.*;

import java.util.ArrayList;
import java.util.HashSet;

public class Vulture {

    private final Mirror bwapi;
    private final HashSet<Unit> enemyUnits;
    private final Unit unit;
    private final AI ai;
    private final IEvaluator evaluator;

    private int immediateReward;

    public Vulture(Unit unit, Mirror bwapi, HashSet<Unit> enemyUnits, IEvaluator evaluator, AI ai) {
        this.unit = unit;
        this.bwapi = bwapi;
        this.enemyUnits = enemyUnits;
        this.evaluator = evaluator;
        this.ai = ai;
    }

    public void step() {
        Unit closestEnemy = getClosestEnemy();
        final Situation sigmaT = new Situation(this.unit, closestEnemy);
        Action action = this.ai.step(sigmaT, evaluator.evaluate(this.unit) + immediateReward);
        if(action.isRequiresClosestEnemy())
        {
            action.setClosestEnemy(closestEnemy);
        }
        immediateReward = action.ExecuteOn(this.unit);
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
