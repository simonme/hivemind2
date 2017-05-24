import bwapi.TechType;
import bwapi.Unit;
import bwapi.UnitType;

/**
 * Created by Jakob on 24.05.2017.
 */
public class ActionSpiderMines extends Action {
    private RelativePosition target;

    public ActionSpiderMines(RelativePosition target)
    {
        this.target = target;
    }

    @Override
    public void ExecuteOn(Unit unit) {
        if(unit.getType() == UnitType.Terran_Vulture)
        {
            if(unit.canUseTech(TechType.Spider_Mines))
            {
                unit.useTech(TechType.Spider_Mines, target.applyTo(unit.getPosition()));
            }
        }
    }
}
