import bwapi.Unit;

/**
 * Created by Jakob on 24.05.2017.
 */
public class ActionAttackMove extends Action
{
    private RelativePosition movement;

    public ActionAttackMove(RelativePosition movement)
    {
        this.movement = movement;
    }

    @Override
    public void ExecuteOn(Unit unit) {
        unit.attack(movement.applyTo(unit.getPosition()));
    }
}