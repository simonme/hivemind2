import bwapi.Unit;
import bwapi.UnitType;

import java.util.HashSet;
import java.util.List;

/**
 * Created by Ferdi on 15.06.2017.
 */
public class MarineEvaluator implements IEvaluator {
    private boolean isInitialized = false;

    private int killedUnitCount;
    private int damageDealt;
    private int HP;

    public double evaluate(Unit unit, HashSet<Unit> alliedUnits) {
        if(!isInitialized) {
            killedUnitCount = unit.getKillCount();
            damageDealt += unit.isAttacking() ? unit.getType().groundWeapon().damageAmount() : 0;
            HP = unit.getHitPoints();
            isInitialized = true;
            return 0;
        }

        int deltaKilledUnitCount = unit.getKillCount() - killedUnitCount;
        killedUnitCount += deltaKilledUnitCount;
        int deltaDamageDealt = unit.isAttacking() ? unit.getType().groundWeapon().damageAmount() : 0;
        damageDealt += deltaDamageDealt;
        int deltaHP = unit.getHitPoints() - HP;
        HP += deltaHP;
        int distanceToClosestMedic = (int)(getClosestUnitOfType(unit, alliedUnits, UnitType.Terran_Medic));
        List<Unit> unitsInRadius = unit.getUnitsInRadius(unit.getType().sightRange());
        unitsInRadius.removeIf(unit2 -> unit2.getPlayer() == unit.getPlayer());
        int visibleEnemyUnitCount = unitsInRadius.size();

        double reward = deltaKilledUnitCount * 200 + deltaHP * 15 + deltaDamageDealt + visibleEnemyUnitCount + 2000 / distanceToClosestMedic; //+ (unit.isAttacking() ? 0.00001 : 0);
        // System.out.println("evaluation reward: " + reward);
        return reward;
    }

    private double getClosestUnitOfType(Unit unit, HashSet<Unit> alliedUnits, UnitType type) {
        double minDistance = Double.POSITIVE_INFINITY;
        for (Unit ally : alliedUnits) {
            if (ally.getType() == type && ally.exists()){
                double distance = getDistance(unit, ally);
                if (distance < minDistance) {
                    minDistance = distance;
                }
            }
        }
        return minDistance;
    }

    private double getDistance(Unit self, Unit unit) {
        return self.getPosition().getDistance(unit.getPosition());
    }
}
