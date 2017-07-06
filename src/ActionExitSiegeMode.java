import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;

/**
 * Created by Jakob on 06.07.2017.
 */
public class ActionExitSiegeMode extends Action {

    public ActionExitSiegeMode() { }

    @Override
    public int ExecuteOn(Unit unit) {
        if(unit.getType() == UnitType.Terran_Siege_Tank_Siege_Mode)
        {
            unit.useTech(TechType.Tank_Siege_Mode);
            return 0;
        }
        return -10;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ActionExitSiegeMode))return false;
        else return true;
    }

    @Override
    public int hashCode() {
        return 32;
    }
}
