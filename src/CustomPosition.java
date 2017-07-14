
public class CustomPosition extends bwapi.Position {
    public CustomPosition(int X, int Y) {
        super(X, Y);
    }

    public CustomPosition add(bwapi.Position other)
    {
        return new CustomPosition(this.getX() + other.getX(), this.getY() + other.getY());
    }

    public static CustomPosition add(bwapi.Position current, bwapi.Position other)
    {
        return ((CustomPosition)current).add(other);
    }

    public CustomPosition sub(bwapi.Position other)
    {
        return new CustomPosition(this.getX() - other.getX(), this.getY() - other.getY());
    }

    public static CustomPosition sub(bwapi.Position current, bwapi.Position other)
    {
        return ((CustomPosition)current).sub(other);
    }

    public CustomPosition div(double scalar)
    {
        return new CustomPosition((int)(this.getX() / scalar), (int)(this.getY() / scalar));
    }

    public CustomPosition mul(double scalar)
    {
        return new CustomPosition((int)(this.getX() * scalar), (int)(this.getY() * scalar));
    }

    public CustomPosition getPerpendicularClockwise()
    {
        return new CustomPosition(-this.getY(), this.getX());
    }

    public CustomPosition getPerpendicularCounterClockwise()
    {
        return new CustomPosition(this.getY(), -this.getX());
    }
}
