import bwapi.Position;

/**
 * Created by Jakob on 24.05.2017.
 */
public class RelativePosition {
    private double angle;
    private double distance;

    // angle in Degrees
    public RelativePosition(double angle, double distance)
    {
        this.angle = (angle / 360) * 2 * Math.PI;
        this.distance = distance;
    }

    public Position applyTo(Position origin)
    {
        double deltaX = distance * Math.cos(angle);
        double deltaY = distance * Math.sin(angle);

        return new Position(origin.getX() + (int)Math.round(deltaX), origin.getY() + (int)Math.round(deltaY));
    }

    public double getAngle() {
        return angle;
    }

    public double getDistance() {
        return distance;
    }

    public static double computeAngle(Position origin, Position target)
    {
        double deltaX = target.getX() - origin.getX();
        double deltaY = target.getY() - origin.getY();

        return Math.atan2(deltaY, deltaX);
    }
}
