import bwapi.Unit;
import bwapi.UnitType;

import java.util.HashSet;

/**
 * Created by Ferdi on 15.06.2017.
 */
public class MedicEvaluator implements IEvaluator {
    private boolean isInitialized = false;

    private int HP;
    private int energy;

    public double evaluate(Unit unit, HashSet<Unit> alliedUnits) {
        if(!isInitialized) {
            HP = unit.getHitPoints();
            energy = unit.getEnergy();
            isInitialized = true;
            return 0;
        }
        int energyUsed = energy - unit.getEnergy();
        // System.out.println("EnergyUsed: " + energyUsed);
        energy = unit.getEnergy(); //Energy regeneriert sich...
        int deltaDamageDealt = unit.isAttacking() ? unit.getType().groundWeapon().damageAmount() : 0;
        int deltaHP = unit.getHitPoints() - HP;
        HP += deltaHP;
        int distanceToClosestMarine = (int)(getClosestUnitOfType(unit, alliedUnits, UnitType.Terran_Marine));
        int visibleEnemyUnitCount = unit.getUnitsInRadius(unit.getType().sightRange()).size();

        double reward = 50 * energyUsed + deltaHP * 15 + visibleEnemyUnitCount + 2000 / distanceToClosestMarine; //+ (unit.isAttacking() ? 0.00001 : 0);
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
