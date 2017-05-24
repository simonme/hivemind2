import bwapi.Unit;

/**
 * Created by Jakob on 23.05.2017.
 */
public class ActionMove extends Action {
    private RelativePosition movement;

    public ActionMove(RelativePosition movement) {
        this.movement = movement;
    }

    @Override
    public void ExecuteOn(Unit unit) {
        unit.move(movement.applyTo(unit.getPosition()));
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof ActionMove))return false;
        ActionMove otherClass = (ActionMove) other;
        return this.movement.equals(otherClass.movement);
    }

    @Override
    public int hashCode() {
        return this.movement.hashCode();
    }
}
