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
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof ActionAttackMove))return false;
        ActionAttackMove otherClass = (ActionAttackMove) other;
        return this.movement.equals(otherClass.movement);
    }

    @Override
    public int hashCode() {
        return this.movement.hashCode();
    }
}