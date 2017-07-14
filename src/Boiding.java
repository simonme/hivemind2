
import bwapi.*;

import java.util.HashSet;
import java.util.List;


public class Boiding {
    private final HashSet<Unit> enemyUnits;
    final private Unit unit;    public static final int RANGE_MULTIPLIER = 1000;
    // r_sig
    public double neighborhoodRange = 0.2 * RANGE_MULTIPLIER;
    // r_sep
    public double separationRange = 0.04 * RANGE_MULTIPLIER;
    // r_col
    public double columnWidth = 0.05 * RANGE_MULTIPLIER;
    // r_col-sep
    public double columnSeparationRange = 0.03 * RANGE_MULTIPLIER;
    // r_lin
    public double lineHeight = 0.05 * RANGE_MULTIPLIER;
    // r_lin-sep
    public double lineSeparationRange = 0.03 * RANGE_MULTIPLIER;
    // w_r1-a
    private double weightEnemy = 0.08;
    // w_r1-b
    private double weightEnemyVisible = 0.35;
    // w_r2
    private double weightSeparation = 0.5;
    // w_r3
    private double weightColumn = 0.2;
    // w_r4
    private double weightLine = 0.2;

    public Boiding(Unit unit, HashSet<Unit> enemyUnits) {
        this.unit = unit;
        this.enemyUnits = enemyUnits;
    }

    public bwapi.Position getBoidingMovement() {
        Unit target = getClosestEnemy();

        if (target == null) {
            return null;
        }
        bwapi.Position unitPositionBwapi = unit.getPosition();
        CustomPosition unitCustomPosition = new CustomPosition(unitPositionBwapi.getX(), unitPositionBwapi.getY());

        List<Unit> closeAllies = getAlliesInRadius((int) Math.ceil(separationRange));

        // delta_r2(c) = delta_sep(c)
        CustomPosition vecSeparation = computeSeparationVector(unitCustomPosition, closeAllies);

        if (vecSeparation.getLength() > 0) {
            CustomPosition separationTargetCustomPosition = unitCustomPosition.add(vecSeparation.mul(-1).mul(weightSeparation));
            return new bwapi.Position(separationTargetCustomPosition.getX(), separationTargetCustomPosition.getY());
        }

        // delta_r1(c)
        CustomPosition vecToEnemy = unitCustomPosition.sub(target.getPosition()).mul(-1);


        List<Unit> relevantAllies = getAlliesInRadius((int) Math.ceil(neighborhoodRange));

        // delta_r3(c)
        CustomPosition vecColumn = computeFormationCohesionVector(unitCustomPosition, vecToEnemy.getPerpendicularClockwise(), relevantAllies, columnWidth, neighborhoodRange, columnSeparationRange);

        // delta_r4(c)
        CustomPosition vecLine = computeFormationCohesionVector(unitCustomPosition, vecToEnemy, relevantAllies, lineHeight, neighborhoodRange, lineSeparationRange);

        CustomPosition targetCustomPosition = unitCustomPosition.add(vecToEnemy.mul(weightEnemyVisible)).add(vecColumn.mul(weightColumn)).add(vecLine.mul(weightLine));
        return new bwapi.Position(targetCustomPosition.getX(), targetCustomPosition.getY());
    }

    private CustomPosition computeSeparationVector(CustomPosition unitCustomPosition, List<Unit> closeAllies) {
        CustomPosition vecSeparation = new CustomPosition(0, 0);
        for (Unit ally : closeAllies) {
            vecSeparation = vecSeparation.add(unitCustomPosition.sub(ally.getPosition()).mul(-1));
        }
        return vecSeparation;
    }

    private CustomPosition computeFormationCohesionVector(CustomPosition unitCustomPosition, CustomPosition columnAxis, List<Unit> relevantAllies, double windowWidth, double neighborhoodRange, double separation) {
        int columnCount = (int) Math.floor((neighborhoodRange / 2 - windowWidth / 2) / windowWidth);
        double columnSpacingTotal = neighborhoodRange - (columnCount * 2 + 1) * windowWidth;
        double columnSpacing = columnSpacingTotal / columnCount * 2;
        double columnCenterDistance = windowWidth + columnSpacing;
        SelectionRectangle columnSelector = new SelectionRectangle(unitCustomPosition, columnAxis, 0, windowWidth / 2);

        double max = Double.NEGATIVE_INFINITY;
        CustomPosition bestColumnCentroid = unitCustomPosition;
        List<Unit> bestColumnUnits = relevantAllies;
        for (int columnIndex = columnCount; columnIndex >= -columnCount; columnIndex--) {
            columnSelector.setOffsetLength(columnIndex * columnCenterDistance);

            // S_j
            List<Unit> columnUnits = columnSelector.apply(relevantAllies);
            int columnUnitCount = columnUnits.size();
            CustomPosition columnCentroid = computeCentroid(columnUnits);

            for (Unit columnUnit : columnUnits) {
                double value = columnUnitCount / unitCustomPosition.add(columnCentroid.sub(columnUnit.getPosition())).getLength();
                if (value > max) {
                    max = value;
                    bestColumnCentroid = columnCentroid;
                    bestColumnUnits = columnUnits;
                }
            }
        }

        // delta_coh(c)
        CustomPosition vecCohesion = bestColumnCentroid.sub(unitCustomPosition);

        final CustomPosition centroid = bestColumnCentroid;
//        bestColumnUnits.removeIf(unit1 -> CustomPosition.sub(unit1.getPosition(), centroid).getLength() > separation);
        bestColumnUnits.removeIf(unit1 -> centroid.sub(unit1.getPosition()).mul(-1).getLength() > separation);

        CustomPosition vecSeparation = computeSeparationVector(unitCustomPosition, bestColumnUnits);
        return vecCohesion.add(vecSeparation);
    }

    private CustomPosition computeCentroid(List<Unit> units) {
        CustomPosition centroid = new CustomPosition(0, 0);
        for (Unit unit_s : units) {
            centroid = centroid.add(unit_s.getPosition());
        }
        centroid = centroid.div(units.size());
        return centroid;
    }

    private Unit getClosestEnemy() {
        Unit result = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Unit enemy : this.enemyUnits) {
            double distance = getDistance(enemy);
            if (distance < minDistance) {
                minDistance = distance;
                result = enemy;
            }
        }

        return result;
    }

    private List<Unit> getAlliesInRadius(int radius) {
        List<Unit> unitsInRadius = unit.getUnitsInRadius(radius);
        unitsInRadius.removeIf(unit2 -> unit2.getPlayer() != unit.getPlayer());
        return unitsInRadius;
    }

    private double getDistance(Unit enemy) {
        return this.unit.getPosition().getDistance(enemy.getPosition());
    }
}
