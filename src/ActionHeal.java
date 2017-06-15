import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;

/**
 * Created by Ferdi on 15.06.2017.
 */
public class ActionHeal extends Action {

    public ActionHeal() { this.requiresTargetUnit = true; }

    @Override
    public int ExecuteOn(Unit unit) {
        if(unit.getType() == UnitType.Terran_Medic)
        {
            Unit ally = getTargetUnit();
            if(ally != null)
            {
                if(unit.canUseTech(TechType.Healing, ally))
                {
                    unit.useTech(TechType.Healing, ally);
                    return 0;
                }
                setTargetUnit(null);
            }
            else
            {
                System.out.println("Heal failed (Missing target).");
                return -100;
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
        return 2;
    }
}
