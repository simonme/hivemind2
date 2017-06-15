import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;

/**
 * Created by Ferdi on 15.06.2017.
 */
public class ActionHeal extends Action {

    public ActionHeal() {  }

    @Override
    public int ExecuteOn(Unit unit) {
        if(unit.getType() == UnitType.Terran_Medic)
        {
            if(unit.canUseTech(TechType.Healing))
            {
                unit.useTech(TechType.Healing, unit);
                return 0;
            }
        }
        return -10;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ActionHeal))return false;
        else return true;
    }

    @Override
    public int hashCode() {
        return this.hashCode();
    }
}
