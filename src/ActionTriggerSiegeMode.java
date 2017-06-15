import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;

/**
 * Created by Ferdi on 15.06.2017.
 */
public class ActionTriggerSiegeMode extends Action {

    public ActionTriggerSiegeMode() { }

    @Override
    public int ExecuteOn(Unit unit) {
        if(unit.getType() == UnitType.Terran_Siege_Tank_Tank_Mode || unit.getType() == UnitType.Terran_Siege_Tank_Siege_Mode)
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
        if (!(obj instanceof ActionTriggerSiegeMode))return false;
        else return true;
    }

    @Override
    public int hashCode() {
        return 3;
    }
}
