import bwapi.Unit;

import java.util.HashSet;
import java.util.List;

public class VultureEvaluator implements IEvaluator {
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

        List<Unit> unitsInRadius = unit.getUnitsInRadius(unit.getType().sightRange());
        unitsInRadius.removeIf(unit2 -> unit2.getPlayer() == unit.getPlayer());
        int visibleEnemyUnitCount = unitsInRadius.size();

        List<Unit> unitsInWeaponRange = unit.getUnitsInRadius(unit.getType().groundWeapon().maxRange());
        unitsInWeaponRange.removeAll(unit.getUnitsInRadius(unit.getType().groundWeapon().minRange()));
        unitsInWeaponRange.removeIf(unit2 -> unit2.getPlayer() == unit.getPlayer());
        int attackableEnemyUnitCount = unitsInWeaponRange.size();

        // Ist das vielleicht ein bisschen zu explizit?
        double attackableEnemyReward = 0;
        if(attackableEnemyUnitCount == 0)
        {
            attackableEnemyReward = -20;
        }
        else
        {
            attackableEnemyReward = (2 - attackableEnemyUnitCount) * 40;
        }

        double reward = deltaKilledUnitCount * 200 + deltaHP * 15 + deltaDamageDealt + visibleEnemyUnitCount * 2 + attackableEnemyReward; //+ (unit.isAttacking() ? 0.00001 : 0);
        // System.out.println("evaluation reward: " + reward);
        return reward;
    }
}
