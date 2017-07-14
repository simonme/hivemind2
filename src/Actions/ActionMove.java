package Actions;

import Position.RelativePosition;
import bwapi.Unit;
import bwapi.UnitType;

/**
 * Created by Jakob on 23.05.2017.
 */
public class ActionMove extends Action {
    private RelativePosition movement;

    public ActionMove(RelativePosition movement) {
        this.movement = movement;
    }

    @Override
    public int ExecuteOn(Unit unit) {
        if (unit.getType() == UnitType.Terran_Siege_Tank_Siege_Mode) {
            return -50;
        }
        unit.move(movement.applyTo(unit.getPosition()));
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ActionMove)) return false;
        ActionMove other = (ActionMove) obj;
        return this.movement.equals(other.movement);
    }

    @Override
    public int hashCode() {
        return this.movement.hashCode();
    }
}
