import bwapi.Unit;

public class VultureEvaluator implements IEvaluator {
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

        double reward = - 1 + deltaKilledUnitCount * 200 + deltaHP * 30 + deltaDamageDealt + visibleEnemyUnitCount; //+ (unit.isAttacking() ? 0.00001 : 0);
        if(HP <= 0)
        {
            reward -= 1000;
        }
        //System.out.println("evaluation reward: " + reward);
        return reward;
    }
}
