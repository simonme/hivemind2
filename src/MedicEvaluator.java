import bwapi.Unit;

/**
 * Created by Ferdi on 15.06.2017.
 */
public class MedicEvaluator implements IEvaluator {
    private boolean isInitialized = false;

    private int HP;
    private int energy;

    public double evaluate(Unit unit) {
        if(!isInitialized) {
            HP = unit.getHitPoints();
            energy = unit.getEnergy();
            isInitialized = true;
            return 0;
        }
        int energyUsed = energy - unit.getEnergy();
        System.out.println("EnergyUsed: " + energyUsed);
        energy = unit.getEnergy(); //Energy regeneriert sich...
        int deltaDamageDealt = unit.isAttacking() ? unit.getType().groundWeapon().damageAmount() : 0;
        int deltaHP = unit.getHitPoints() - HP;
        HP += deltaHP;

        int visibleEnemyUnitCount = unit.getUnitsInRadius(unit.getType().sightRange()).size();

        double reward = 50 * energyUsed + deltaHP * 15 + visibleEnemyUnitCount; //+ (unit.isAttacking() ? 0.00001 : 0);
        // System.out.println("evaluation reward: " + reward);
        return reward;
    }
}
