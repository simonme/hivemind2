package Actions;

import Position.RelativePosition;
import bwapi.Unit;
import bwapi.UnitType;


public class ActionBoidingMove extends Action {

    public ActionBoidingMove() {
        this.requiresBoidingMove = true;
    }

    @Override
    public int ExecuteOn(Unit unit)
    {
        if(unit.getType() == UnitType.Terran_Siege_Tank_Siege_Mode) {
            return -50;
        }
        if (getBoidingMove() == null) {
            System.out.println("ActionBoidingMove failed (Missing Boiding Move).");
            return -10;
        }
        unit.move(getBoidingMove());
        setBoidingMove(null);
        return 0;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ActionBoidingMove))return false;
        return true;
    }

    @Override
    public int hashCode() {
        return getBoidingMove() != null ? getBoidingMove().hashCode() : 48;
    }
}
