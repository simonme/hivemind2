package Actions;

import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;

/**
 * Created by Ferdi on 15.06.2017.
 */
public class ActionTriggerStimPack extends Action {

    public ActionTriggerStimPack() {
        this.requiresTargetUnit = true;
    }

    @Override
    public int ExecuteOn(Unit unit) {
        Unit enemy = getTargetUnit();
        if(unit.getType() == UnitType.Terran_Marine) {
            if(unit.canUseTech(TechType.Stim_Packs)
                    && enemy != null
                    && unit.getDistance(enemy) < 350) {
                unit.useTech(TechType.Stim_Packs);
                return 0;
            }
        }
        return -30;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ActionTriggerStimPack))return false;
        else return true;
    }

    @Override
    public int hashCode() {
        return 4;
    }
}
