import bwapi.Unit;

import java.util.HashSet;

/**
 * Created by Jakob on 24.05.2017.
 */
interface IEvaluator
{
    double evaluate(Unit unit, HashSet<Unit> alliedUnits);
}

