package Actions;

import Position.RelativePosition;
import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;

/**
 * Created by Jakob on 24.05.2017.
 */
public class ActionSpiderMines extends Action {
    private RelativePosition target;

    public ActionSpiderMines(RelativePosition target) {
        this.target = target;
        this.requiresTargetUnit = true;
    }

    @Override
    public int ExecuteOn(Unit unit) {
        Unit enemy = getTargetUnit();
        setTargetUnit(null);
        if (unit.getType() == UnitType.Terran_Vulture && enemy != null) {
            bwapi.Position enemyPosition = enemy.getPosition();
            int unitDistance = unit.getDistance(enemyPosition);
            if (unit.canUseTech(TechType.Spider_Mines, target.applyTo(unit.getPosition()))
                    && unitDistance < 200) {
                unit.useTech(TechType.Spider_Mines, target.applyTo(unit.getPosition()));
                return 0;
            }
        }
        return -20;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ActionSpiderMines)) return false;
        ActionSpiderMines other = (ActionSpiderMines) obj;
        return this.target.equals(other.target);
    }

    @Override
    public int hashCode() {
        return this.target.hashCode();
    }
}
