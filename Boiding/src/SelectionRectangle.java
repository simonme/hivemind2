import bwapi.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jakob on 05.07.2017.
 */
public class SelectionRectangle {
    private double v1;
    private double v2;
    private double offsetLength;
    private double offsetBase;
    private double offsetRange;

    public SelectionRectangle(Position base, Position offsetDirection, double offsetLength, double offsetRange)
    {
        // Nur so als Verständnishilfe:
        // Das berechnet hier eine inverse Basiswechselmatrix und behält dann nur die erste Zeile.
        // Die andere Basis ist aus der offsetDirection (normalisiert) und einem dazu orthogonalen Vektor aufgebaut.
        // Damit lässt sich die Distanz in Richtung des offsets mit der ersten Zeile der Matrix berechnen

        // Orthogonaler Vektor
        Position offsetOrthogonal = offsetDirection.getPerpendicularClockwise();

        // Normalisieren
        double offsetMagnitude = offsetDirection.getLength();
        double offsetXNormalized = offsetDirection.getX() / offsetMagnitude;
        double offsetYNormalized = offsetDirection.getY() / offsetMagnitude;

        // Determinante der Basiswechselmatrix
        double determinant = offsetXNormalized + offsetOrthogonal.getY() - (offsetYNormalized + offsetOrthogonal.getX());

        // Erste Zeile der inversen Basiswechselmatrix
        v1 = offsetXNormalized * (1 / determinant);
        v2 = offsetOrthogonal.getX() * (1 / determinant);

        // "0-Wert" für Distanzberechnung
        this.offsetBase = getDistanceInOffsetDirection(base);
        this.offsetLength = offsetLength;
        this.offsetRange = offsetRange;
    }

    public void setOffsetLength(double offsetLength)
    {
        this.offsetLength = offsetLength;
    }

    private double getDistanceInOffsetDirection(bwapi.Position position)
    {
        return v1 * position.getX() + v2 * position.getY();
    }

    public List<Unit> apply(List<Unit> units)
    {
        List<Unit> selection = new ArrayList<>(units);

        selection.removeIf(unit -> Math.abs(getDistanceInOffsetDirection(unit.getPosition()) - offsetBase - offsetLength) > offsetRange);

        return selection;
    }
}
