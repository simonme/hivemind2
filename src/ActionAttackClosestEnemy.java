import bwapi.Player;
import bwapi.Unit;

/**
 * Created by Jakob on 25.05.2017.
 */
public class ActionAttackClosestEnemy extends Action {

    private Player self;

    public ActionAttackClosestEnemy(Player self)
    {
        this.requiresTargetUnit = true;
        this.self = self;
    }

    @Override
    public int ExecuteOn(Unit unit)
    {
        Unit enemy = getTargetUnit();
        if(enemy != null && enemy.getPlayer() != this.self)
        {
            unit.attack(enemy);
            setTargetUnit(null);
        }
        else
        {
            System.out.println("AttackClosestEnemy failed (Missing target).");
            return -100;
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ActionAttackMove)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
