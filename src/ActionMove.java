import bwapi.Unit;

/**
 * Created by Jakob on 23.05.2017.
 */
public class ActionMove extends Action
{
    private RelativePosition movement;

    public ActionMove(RelativePosition movement)
    {
        this.movement = movement;
    }

    @Override
    public void ExecuteOn(Unit unit) {
        unit.move(movement.applyTo(unit.getPosition()));
    }
}
