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
    public boolean equals(Object obj){
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ActionMove))return false;
        ActionMove other = (ActionMove) obj;
        return this.movement.equals(other.movement);
    }

    @Override
    public int hashCode() {
        return this.movement.hashCode();
    }
}
