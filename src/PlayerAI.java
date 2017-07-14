import Actions.*;
import Evaluator.IEvaluator;
import Situation.ISituationFactory;
import Situation.Situation;
import Position.PositionHelper;
import bwapi.*;

import java.util.HashSet;

public class PlayerAI {

    private final Mirror bwapi;
    private final HashSet<Unit> enemyUnits;
    private final HashSet<Unit> alliedUnits;
    private final Unit unit;
    private final AI ai;
    private final IEvaluator evaluator;
    private final ISituationFactory situationFactory;
    private final Boiding boiding;

    private int immediateReward;

    public PlayerAI(Unit unit, Mirror bwapi, HashSet<Unit> enemyUnits, HashSet<Unit> alliedUnits, IEvaluator evaluator, AI ai, ISituationFactory situationFactory) {
        this.unit = unit;
        this.bwapi = bwapi;
        this.enemyUnits = enemyUnits;
        this.evaluator = evaluator;
        this.ai = ai;
        this.alliedUnits = alliedUnits;
        this.situationFactory = situationFactory;
        this.boiding = new Boiding(this.unit, this.enemyUnits);
    }

    public void step() {
        // System.out.println("stepping " + unit.getID() + " " + unit.getType());
        final Unit closestEnemy = getClosestEnemy();
        final Unit lowestHealableAlly = PositionHelper.getLowestHealable(alliedUnits);
        final Situation sigmaT = situationFactory.create(this.unit, closestEnemy, enemyUnits, alliedUnits);
        final Action action = this.ai.step(sigmaT, evaluator.evaluate(this.unit, this.alliedUnits) + immediateReward, unit.getID());
        if(action.isRequiresTargetUnit()) {
            if(action instanceof ActionAttackClosestEnemy
                    || action instanceof ActionAwayFromClosestEnemy
                    || action instanceof ActionSpiderMines
                    || action instanceof ActionTriggerStimPack)
            {
                action.setTargetUnit(closestEnemy);
            }
            if(action instanceof ActionHeal)
            {
                 action.setTargetUnit(lowestHealableAlly);
            }
        }
        if (action.isRequiresBoidingMove()) {
            action.setBoidingMove(boiding.getBoidingMovement());
        }
        immediateReward = action.ExecuteOn(this.unit);
        // System.out.println("stepped " + unit.getID());
    }

    private void move(Unit target) {
        unit.move(new Position(target.getPosition().getX(), target.getPosition().getY()), false);
    }

    private void move(Unit target, double percentage) {
        unit.move(new Position(target.getPosition().getX(), target.getPosition().getY()), false);
        unit.getPosition();
    }

    private Unit getClosestEnemy() {
        Unit result = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Unit enemy : enemyUnits) {
            double distance = getDistance(enemy);
            if (distance < minDistance) {
                minDistance = distance;
                result = enemy;
            }
        }
        return result;
    }

    public Unit getUnit(){
        return unit;
    }

    public AI getAi(){
        return ai;
    }

    private double getDistance(Unit enemy) {
    	return this.unit.getPosition().getDistance(enemy.getPosition());
    }
}
