import bwapi.Unit;

/**
 * Created by Jakob on 25.05.2017.
 */
public class ActionAttackClosestEnemy extends Action {
    public ActionAttackClosestEnemy()
    {
        this.requiresClosestEnemy = true;
    }

    @Override
    public int ExecuteOn(Unit unit)
    {
        Unit enemy = getClosestEnemy();
        if(enemy != null)
        {
            unit.attack(getClosestEnemy());
            setClosestEnemy(null);
        }
        else
        {
            return -10;
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ActionAttackMove))return false;
        return true;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}