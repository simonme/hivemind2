import bwapi.Unit;

public class VultureEvaluator implements IEvaluator {
    private boolean isInitialized = false;

    private int killedUnitCount;
    private int damageDealt;
    private int HP;

    public int evaluate(Unit unit)
    {
        if(isInitialized == false)
        {
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

        return killedUnitCount * 20 + deltaHP * 2 + damageDealt + visibleEnemyUnitCount;
    }
}
