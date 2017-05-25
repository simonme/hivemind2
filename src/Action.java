import bwapi.Unit;

/**
 * Created by Simon on 15.05.2017.
 */
public abstract class Action {
    protected boolean requiresClosestEnemy = false;
    private Unit closestEnemy = null;

    public abstract int ExecuteOn(Unit unit);

    public abstract boolean equals(Object obj);

    public abstract int hashCode();

    public boolean isRequiresClosestEnemy() {
        return requiresClosestEnemy;
    }

    protected Unit getClosestEnemy() {
        return closestEnemy;
    }

    public void setClosestEnemy(Unit closestEnemy) {
        this.closestEnemy = closestEnemy;
    }
}

