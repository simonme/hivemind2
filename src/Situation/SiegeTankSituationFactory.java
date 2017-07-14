package Situation;

import bwapi.Unit;
import bwapi.UnitType;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Jakob on 12.07.2017.
 */
public class SiegeTankSituationFactory extends SituationFactoryBase {
    @Override
    protected ArrayList<Double> createArray(Unit unit, Unit closestEnemy, HashSet<Unit> enemyUnits, HashSet<Unit> alliedUnits) {
        ArrayList<Double> situation = super.createArray(unit, closestEnemy, enemyUnits, alliedUnits);

        // Is Tank sieged? Represented as wrapping interval.
        situation.add(unit.getType() == UnitType.Terran_Siege_Tank_Siege_Mode ? 100.0 : 0.0);

        return situation;
    }
}
