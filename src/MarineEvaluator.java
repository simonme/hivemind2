import bwapi.Unit;

/**
 * Created by Ferdi on 15.06.2017.
 */
public class MarineEvaluator implements IEvaluator {
    private boolean isInitialized = false;

    private int killedUnitCount;
    private int damageDealt;
    private int HP;

    public double evaluate(Unit unit) {
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

        int visibleEnemyUnitCount = unit.getUnitsInRadius(unit.getType().sightRange()).size();

        double reward = deltaKilledUnitCount * 200 + deltaHP * 15 + deltaDamageDealt + visibleEnemyUnitCount; //+ (unit.isAttacking() ? 0.00001 : 0);
        // System.out.println("evaluation reward: " + reward);
        return reward;
    }
}
