import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;

/**
 * Created by Ferdi on 15.06.2017.
 */
public class ActionTriggerStimPack extends Action {

    public ActionTriggerStimPack() {  }

    @Override
    public int ExecuteOn(Unit unit) {
        if(unit.getType() == UnitType.Terran_Marine)
        {
            if(unit.canUseTech(TechType.Stim_Packs))
            {
                unit.useTech(TechType.Stim_Packs);
                return 0;
            }
        }
        return -10;
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
