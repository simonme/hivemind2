
import bwapi.*;

import java.util.HashSet;
import java.util.List;

/**
 * Created by Steffen Wilke on 31.05.2016.
 */
public class Marine {

    private final HashSet<Unit> enemyUnits;
    final private Unit unit;

    public Marine(Unit unit, HashSet<Unit> enemyUnits) {
        this.unit = unit;
        this.enemyUnits = enemyUnits;
    }

    public void step() {
        Unit target = getClosestEnemy();
        if (target == null) {
            return;
        }
        if (this.unit.getGroundWeaponCooldown() == 0 && !this.unit.isAttackFrame() && !this.unit.isStartingAttack()
                && !this.unit.isAttacking() && target != null) {
            if (WeaponType.Gauss_Rifle.maxRange() > getDistance(target) - 20.0) {
                this.unit.attack(target);
            }
        } else {
            move(target);
        }
    }

    private void move(Unit target) {
        if (target == null) {
            return;
        }
        Parameters params = new Parameters();
        Position unitPosition = (Position) unit.getPosition();

        List<Unit> closeAllies = getAlliesInRadius(params.separationRange);

        // delta_r2(c) = delta_sep(c)
        Position vecSeparation = computeSeparationVector(unitPosition, closeAllies);

        if(vecSeparation.getLength() > 0)
        {
            this.unit.move(unitPosition.add(vecSeparation.mul(params.weightSeparation)), false);
            return;
        }

        // delta_r1(c)
        Position vecToEnemy = Position.sub(target.getPosition(), unitPosition);


        List<Unit> relevantAllies = getAlliesInRadius(params.neighborhoodRange);

        // delta_r3(c)
        Position vecColumn = computeFormationCohesionVector(unitPosition, vecToEnemy.getPerpendicularClockwise(), relevantAllies, params.columnWidth, params.neighborhoodRange, params.columnSeparationRange);

        // delta_r4(c)
        Position vecLine = computeFormationCohesionVector(unitPosition, vecToEnemy, relevantAllies, params.lineHeight, params.neighborhoodRange, params.lineSeparationRange);

        double weightEnemy = params.weightEnemyVisible;
        Position targetPosition = unitPosition.add(vecToEnemy.mul(weightEnemy)).add(vecColumn.mul(params.weightColumn)).add(vecLine.mul(params.weightLine));
        this.unit.move(targetPosition, false);
    }

    private Position computeSeparationVector(Position unitPosition, List<Unit> closeAllies) {
        Position vecSeparation = new Position(0, 0);
        for (Unit ally :
                closeAllies) {
            vecSeparation = vecSeparation.add(Position.sub(ally.getPosition(), unitPosition));
        }
        return vecSeparation;
    }

    private Position computeFormationCohesionVector(Position unitPosition, Position columnAxis, List<Unit> relevantAllies, double windowWidth, double neighborhoodRange, double separation) {
        int columnCount = (int) Math.floor((neighborhoodRange / 2 - windowWidth / 2) / windowWidth);
        double columnSpacingTotal = neighborhoodRange - (columnCount * 2 + 1) * windowWidth;
        double columnSpacing = columnSpacingTotal / columnCount * 2;
        double columnCenterDistance = windowWidth + columnSpacing;
        SelectionRectangle columnSelector = new SelectionRectangle(unitPosition, columnAxis, 0, windowWidth / 2);

        double max = Double.NEGATIVE_INFINITY;
        Position bestColumnCentroid = unitPosition;
        List<Unit> bestColumnUnits = relevantAllies;
        for (int columnIndex = columnCount; columnIndex >= -columnCount; columnIndex--) {
            columnSelector.setOffsetLength(columnIndex * columnCenterDistance);

            // S_j
            List<Unit> columnUnits = columnSelector.apply(relevantAllies);
            int columnUnitCount = columnUnits.size();
            Position columnCentroid = computeCentroid(columnUnits);

            for (Unit columnUnit :
                    columnUnits) {
                double value = columnUnitCount / unitPosition.add(columnCentroid.sub(columnUnit.getPosition())).getLength();
                if (value > max) {
                    max = value;
                    bestColumnCentroid = columnCentroid;
                    bestColumnUnits = columnUnits;
                }
            }
        }

        // delta_coh(c)
        Position vecCohesion = bestColumnCentroid.sub(unitPosition);

        final Position centroid = bestColumnCentroid;
        bestColumnUnits.removeIf(unit1 -> Position.sub(unit1.getPosition(), centroid).getLength() > separation);

        Position vecSeparation = computeSeparationVector(vecCohesion, bestColumnUnits);
        return vecCohesion.add(vecSeparation);
    }

    private Position computeCentroid(List<Unit> units) {
        Position centroid = new Position(0, 0);
        for (Unit unit_s :
                units) {
            centroid = centroid.add(unit_s.getPosition());
        }
        centroid = centroid.div(1.0d / units.size());
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

    public int getID() {
        return this.unit.getID();
    }
}
