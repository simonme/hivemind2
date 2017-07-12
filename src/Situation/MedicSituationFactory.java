package Situation;

import Position.PositionHelper;
import Position.RelativePosition;
import bwapi.Unit;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Jakob on 12.07.2017.
 */
public class MedicSituationFactory extends SituationFactoryBase {
    @Override
    protected ArrayList<Double> createArray(Unit unit, Unit closestEnemy, HashSet<Unit> enemyUnits, HashSet<Unit> alliedUnits) {
        ArrayList<Double> situation = super.createArray(unit, closestEnemy, enemyUnits, alliedUnits);

        Unit unitLowest = PositionHelper.getLowestHealable(alliedUnits);

        if (unitLowest != null) {
            situation.add((double) unitLowest.getDistance(unit));
            situation.add(RelativePosition.computeAngle(unit.getPosition(), unitLowest.getPosition()) * 360 / (2 * Math.PI));
        } else {
            situation.add((double) -10000);
            situation.add((double) -10000);
        }

        return situation;
    }
}
