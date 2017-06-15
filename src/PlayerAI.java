import bwapi.*;

import javax.swing.text.html.Option;
import java.util.HashSet;
import java.util.Optional;

public class PlayerAI {

    private final Mirror bwapi;
    private final HashSet<Unit> enemyUnits;
    private final HashSet<Unit> alliedUnits;
    private final Unit unit;
    private final AI ai;
    private final IEvaluator evaluator;

    private int immediateReward;

    public PlayerAI(Unit unit, Mirror bwapi, HashSet<Unit> enemyUnits, HashSet<Unit> alliedUnits, IEvaluator evaluator, AI ai) {
        this.unit = unit;
        this.bwapi = bwapi;
        this.enemyUnits = enemyUnits;
        this.evaluator = evaluator;
        this.ai = ai;
        this.alliedUnits = alliedUnits;
    }

    public void step() {
        System.out.println("stepping " + unit.getID() + " " + unit.getType());
        final Unit closestEnemy = getClosestEnemy();
        final Unit lowestHealableAlly = getLowestHealableAlly();
        final Situation sigmaT = new Situation(this.unit, closestEnemy, enemyUnits, alliedUnits);
        final Action action = this.ai.step(sigmaT, evaluator.evaluate(this.unit) + immediateReward, unit.getID());
        if(action.isRequiresTargetUnit()) {
            if(action instanceof ActionAttackClosestEnemy)
            {
                action.setTargetUnit(closestEnemy);
            }
            if(action instanceof  ActionHeal)
            {
                 action.setTargetUnit(lowestHealableAlly);
            }
        }
        immediateReward = action.ExecuteOn(this.unit);
        System.out.println("stepped " + unit.getID());
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

    private Unit getLowestHealableAlly()
    {
        Optional<Unit> unitOption = alliedUnits.stream().min((current, other) -> {
            double currentVal, otherVal;
            if((current.getType() == UnitType.Terran_Marine || current.getType() == UnitType.Terran_Medic) && !current.isBeingHealed())
            {
                currentVal = current.getHitPoints() / current.getInitialHitPoints();
            }
            else
            {
                currentVal = 2;
            }
            if((other.getType() == UnitType.Terran_Marine || other.getType() == UnitType.Terran_Medic) && !current.isBeingHealed())
            {
                otherVal = other.getHitPoints() / other.getInitialHitPoints();
            }
            else
            {
                otherVal = 2;
            }
            return Double.compare(currentVal, otherVal);
        });
        if(unitOption.isPresent()) {
            Unit result = unitOption.get();
            if(result.getHitPoints() < result.getInitialHitPoints()) {
                return result;
            }
        }
        return null;
    }

    private double getDistance(Unit enemy) {
    	return this.unit.getPosition().getDistance(enemy.getPosition());
    }
}
