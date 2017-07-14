package Actions;

import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;

/**
 * Created by Ferdi on 15.06.2017.
 */
public class ActionEnterSiegeMode extends Action {

    public ActionEnterSiegeMode() {
        this.requiresTargetUnit = true;
    }

    @Override
    public int ExecuteOn(Unit unit) {
        Unit enemy = getTargetUnit();
        if(unit.getType() == UnitType.Terran_Siege_Tank_Tank_Mode
                && enemy != null
                && unit.getDistance(enemy) < 350) {
            unit.useTech(TechType.Tank_Siege_Mode);
            return 0;
        }
        return -20;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ActionEnterSiegeMode))return false;
        else return true;
    }

    @Override
    public int hashCode() {
        return 3;
    }
}
