package Situation;

import bwapi.Unit;

import java.util.HashSet;

/**
 * Created by mande on 06.07.2017.
 */
public interface ISituationFactory {
    Situation create(Unit unit, Unit closestEnemy, HashSet<Unit> enemyUnits, HashSet<Unit> alliedUnits);
}
