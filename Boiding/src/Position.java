/**
 * Created by Jakob on 04.07.2017.
 */
public class Position extends bwapi.Position {
    public Position(int X, int Y) {
        super(X, Y);
    }

    public Position add(bwapi.Position other)
    {
        return new Position(this.getX() + other.getX(), this.getY() + other.getY());
    }

    public static Position add(bwapi.Position current, bwapi.Position other)
    {
        return ((Position)current).add(other);
    }

    public Position sub(bwapi.Position other)
    {
        return new Position(this.getX() - other.getX(), this.getY() - other.getY());
    }

    public static Position sub(bwapi.Position current, bwapi.Position other)
    {
        return ((Position)current).sub(other);
    }

    public Position div(double scalar)
    {
        return new Position((int)(this.getX() / scalar), (int)(this.getY() / scalar));
    }

    public Position mul(double scalar)
    {
        return new Position((int)(this.getX() * scalar), (int)(this.getY() * scalar));
    }

    public Position getPerpendicularClockwise()
    {
        return new Position(-this.getY(), this.getX());
    }

    public Position getPerpendicularCounterClockwise()
    {
        return new Position(this.getY(), -this.getX());
    }
}
