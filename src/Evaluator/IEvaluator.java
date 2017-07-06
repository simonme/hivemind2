package Evaluator;

import bwapi.Unit;

import java.util.HashSet;

/**
 * Created by Jakob on 24.05.2017.
 */
public interface IEvaluator
{
    double evaluate(Unit unit, HashSet<Unit> alliedUnits);
}

