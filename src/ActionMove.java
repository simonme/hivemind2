import bwapi.Position;
import bwapi.Unit;

/**
 * Created by Jakob on 23.05.2017.
 */
public class ActionMove extends Action
{
    public enum Direction
    {
        North,
        South,
        East,
        West
    }
    private Direction direction;
    private int distance;

    public ActionMove (Direction direction) { this(direction, 1); }

    public ActionMove(Direction direction, int distance)
    {
        this.direction = direction;
        this.distance = distance;
    }

    @Override
    public void ExecuteOn(Unit unit) {
        Position position = unit.getPosition();
        Position target;
        switch (direction)
        {
            case North:
                target = new Position(position.getX(), position.getY() + distance);
                break;
            case South:
                target = new Position(position.getX(), position.getY() - distance);
                break;
            case East:
                target = new Position(position.getX() + distance, position.getY());
                break;
            case West:
                target = new Position(position.getX() - distance, position.getY());
                break;
            default:
                target = position;
                break;
        }
        unit.move(target);
    }
}
