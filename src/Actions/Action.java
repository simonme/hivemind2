package Actions;

import bwapi.Unit;

/**
 * Created by Simon on 15.05.2017.
 */
public abstract class Action {
    protected boolean requiresTargetUnit = false;
    protected boolean requiresBoidingMove = false;
    private Unit targetUnit = null;
    private bwapi.Position boidingMove;

    public abstract int ExecuteOn(Unit unit);

    public abstract boolean equals(Object obj);

    public abstract int hashCode();

    public boolean isRequiresTargetUnit() {
        return requiresTargetUnit;
    }

    protected Unit getTargetUnit() {
        return targetUnit;
    }

    public void setTargetUnit(Unit targetUnit) {
        this.targetUnit = targetUnit;
    }

    public boolean isRequiresBoidingMove() {
        return requiresBoidingMove;
    }

    protected bwapi.Position getBoidingMove() {
        return boidingMove;
    }

    public void setBoidingMove(bwapi.Position boidingMove) {
        this.boidingMove = boidingMove;
    }
}

