package Actions;

import bwapi.Unit;

/**
 * Created by Jakob on 06.07.2017.
 */
public class ActionIdle extends Action {
    @Override
    public int ExecuteOn(Unit unit) {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ActionIdle)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return 5;
    }
}
