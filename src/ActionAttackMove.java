import bwapi.Unit;

/**
 * Created by Jakob on 24.05.2017.
 */
public class ActionAttackMove extends Action {
    private RelativePosition movement;

    public ActionAttackMove(RelativePosition movement)
    {
        this.movement = movement;
    }

    @Override
    public void ExecuteOn(Unit unit) {
        unit.attack(movement.applyTo(unit.getPosition()));
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ActionAttackMove))return false;
        ActionAttackMove other = (ActionAttackMove) obj;
        return this.movement.equals(other.movement);
    }

    @Override
    public int hashCode() {
        return this.movement.hashCode();
    }
}