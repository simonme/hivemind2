package Position;

import bwapi.Unit;
import bwapi.UnitType;

import java.util.HashSet;
import java.util.Optional;

/**
 * Created by Jakob on 12.07.2017.
 */
public class PositionHelper {
    public static Unit getLowestHealable(HashSet<Unit> alliedUnits) {
        Optional<Unit> unitOption = alliedUnits.stream().min((current, other) -> {
            double currentVal, otherVal;
            if ((current.getType() == UnitType.Terran_Marine || current.getType() == UnitType.Terran_Medic) && !current.isBeingHealed()) {
                currentVal = current.getHitPoints() / current.getInitialHitPoints();
            } else {
                currentVal = 2;
            }
            if ((other.getType() == UnitType.Terran_Marine || other.getType() == UnitType.Terran_Medic) && !current.isBeingHealed()) {
                otherVal = other.getHitPoints() / other.getInitialHitPoints();
            } else {
                otherVal = 2;
            }
            return Double.compare(currentVal, otherVal);
        });
        if (unitOption.isPresent()) {
            Unit result = unitOption.get();
            if (result.getHitPoints() < result.getInitialHitPoints()) {
                return result;
            }
        }
        return null;
    }
}
