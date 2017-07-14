package Actions;

import bwapi.Unit;

/**
 * Created by Jakob on 25.05.2017.
 */
public class ActionAttackClosestEnemy extends Action {

    public ActionAttackClosestEnemy() {
        this.requiresTargetUnit = true;
        this.duration = 15;
    }

    @Override
    public int ExecuteOn(Unit unit) {
        Unit enemy = getTargetUnit();
        if (enemy == null) {
            System.out.println("AttackClosestEnemy failed (Missing target).");
            return -40;
        }
        bwapi.Position enemyPosition = enemy.getPosition();
        int unitDistance = unit.getDistance(enemyPosition);
        if (unitDistance > 160) {
            System.out.println("AttackClosestEnemy failed (target unit too far away).");
            setTargetUnit(null);
            return -20;
        }
        unit.attack(enemy);
        setTargetUnit(null);
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ActionAttackMove)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return 1;
    }

}
