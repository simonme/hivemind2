
import bwapi.*;

import java.util.HashSet;
import java.util.List;

/**
 * Created by Steffen Wilke on 31.05.2016.
 */
public class Marine{


    private final HashSet<Unit> enemyUnits;
    final private Unit unit;
    private Parameters params;

    public Marine(Unit unit, HashSet<Unit> enemyUnits) {
        this.unit = unit;
        this.enemyUnits = enemyUnits;
    }

    public void step() {
        Unit target = getClosestEnemy();

        if (target == null || this.unit.isAttackFrame()
                || this.unit.isStartingAttack() || this.unit.isAttacking()) {
            return;
        }

        int cd = this.unit.getGroundWeaponCooldown();
        if (cd >= 1) {
            return;
        }

        int maxRange = WeaponType.Gauss_Rifle.maxRange();
        double distance = getDistance(target);

        if (maxRange > distance - 25.0) {
            this.unit.attack(target);
        } else {
            move(target);
        }




//        if (this.unit.getGroundWeaponCooldown() == 0 && !this.unit.isAttackFrame()
//                && !this.unit.isStartingAttack() && !this.unit.isAttacking()) {
//            int maxRange = WeaponType.Gauss_Rifle.maxRange();
//            double distance = getDistance(target);
//            if (maxRange > distance - 25.0) {
//                this.unit.attack(target);
//            } else {
//                move(target);
//            }
//        }
//        else if (!this.unit.isAttackFrame() && !this.unit.isStartingAttack() && !this.unit.isAttacking()) {
//            move(target);
//        }
    }

    private void move(Unit target) {
        if (target == null) {
            return;
        }
        //Parameters params = new Parameters();
        bwapi.Position unitPositionBwapi = unit.getPosition();
        Position unitPosition = new Position(unitPositionBwapi.getX(), unitPositionBwapi.getY());

        List<Unit> closeAllies = getAlliesInRadius((int) Math.ceil(params.getSeparationRange()));

        // delta_r2(c) = delta_sep(c)
        Position vecSeparation = computeSeparationVector(unitPosition, closeAllies);

        if (vecSeparation.getLength() > 0) {
            Position moveVector = unitPosition.add(vecSeparation.mul(-1).mul(params.getWeightSeparation()));
            this.unit.move(moveVector, false);
            return;
        }

        // delta_r1(c)
        Position vecToEnemy = unitPosition.sub(target.getPosition()).mul(-1);


        List<Unit> relevantAllies = getAlliesInRadius((int) Math.ceil(params.getNeighborhoodRange()));

        // delta_r3(c)
        Position vecColumn = computeFormationCohesionVector(unitPosition, vecToEnemy.getPerpendicularClockwise(), relevantAllies, params.getColumnWidth(), params.getNeighborhoodRange(), params.getColumnSeparationRange());

        // delta_r4(c)
        Position vecLine = computeFormationCohesionVector(unitPosition, vecToEnemy, relevantAllies, params.getLineHeight(), params.getNeighborhoodRange(), params.getLineSeparationRange());

        double weightEnemy = params.getWeightEnemyVisible();
        Position targetPosition = unitPosition.add(vecToEnemy.mul(weightEnemy)).add(vecColumn.mul(params.getWeightColumn())).add(vecLine.mul(params.getWeightLine()));
        this.unit.move(new bwapi.Position(targetPosition.getX(), targetPosition.getY()), false);
    }

    private Position computeSeparationVector(Position unitPosition, List<Unit> closeAllies) {
        Position vecSeparation = new Position(0, 0);
        for (Unit ally : closeAllies) {
            vecSeparation = vecSeparation.add(unitPosition.sub(ally.getPosition()).mul(-1));
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

            for (Unit columnUnit : columnUnits) {
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
//        bestColumnUnits.removeIf(unit1 -> CustomPosition.sub(unit1.getPosition(), centroid).getLength() > separation);
        bestColumnUnits.removeIf(unit1 -> centroid.sub(unit1.getPosition()).mul(-1).getLength() > separation);

        Position vecSeparation = computeSeparationVector(unitPosition, bestColumnUnits);
        return vecCohesion.add(vecSeparation);
    }

    private Position computeCentroid(List<Unit> units) {
        Position centroid = new Position(0, 0);
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

    public int getID() {
        return this.unit.getID();
    }

    public Parameters getParams() {
        return params;
    }

    public void setParams(Parameters params) {
        this.params = params;
    }

    public int getHP(){
        return this.unit.getHitPoints();
    }
}
