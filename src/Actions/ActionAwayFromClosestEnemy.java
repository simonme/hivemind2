package Actions;

import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;

public class ActionAwayFromClosestEnemy extends Action {

    public ActionAwayFromClosestEnemy() {
        this.requiresTargetUnit = true;
    }

    @Override
    public int ExecuteOn(Unit unit) {
        if (unit.getType() == UnitType.Terran_Siege_Tank_Siege_Mode) {
            return -50;
        }
        Unit enemy = getTargetUnit();
        if (enemy != null) {
            Position enemyPosition = enemy.getPosition();
            Position unitPosition = unit.getPosition();

            Position vector = new Position(enemyPosition.getX() - unitPosition.getX(), enemyPosition.getY() - unitPosition.getY());

            Position invertedVector = new Position(-1 * vector.getX(), -1 * vector.getY());

            Position movementVector = invertedVector.getLength() > 200
                    ? new Position((int) Math.round(vector.getX() / invertedVector.getLength()) * 100, (int) Math.round(vector.getY() / invertedVector.getLength()) * 100)
                    : vector;

            Position targetPosition = new Position(unit.getX() + movementVector.getX(), unit.getY() + movementVector.getY());

            unit.move(targetPosition);

            setTargetUnit(null);

            return 0;
        }
        System.out.println("ActionAwayFromClosestEnemy failed (Missing target).");
        return -100;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ActionAwayFromClosestEnemy)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return 43;
    }
}
