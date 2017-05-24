import bwapi.*;

import java.util.ArrayList;
import java.util.HashSet;

public class Vulture {

    private final Mirror bwapi;
    private final HashSet<Unit> enemyUnits;
    final private Unit unit;
    private final AI ai;

    public Vulture(Unit unit, Mirror bwapi, HashSet<Unit> enemyUnits) {
        this.unit = unit;
        this.bwapi = bwapi;
        this.enemyUnits = enemyUnits;
        ArrayList<Action> possibleActions = new ArrayList<>();
        possibleActions.add(new ActionMove(new RelativePosition(0, 1)));
        possibleActions.add(new ActionMove(new RelativePosition(90, 1)));
        possibleActions.add(new ActionMove(new RelativePosition(180, 1)));
        possibleActions.add(new ActionMove(new RelativePosition(270, 1)));
        possibleActions.add(new ActionSpiderMines(new RelativePosition(0, 1)));
        possibleActions.add(new ActionSpiderMines(new RelativePosition(90, 1)));
        possibleActions.add(new ActionSpiderMines(new RelativePosition(180, 1)));
        possibleActions.add(new ActionSpiderMines(new RelativePosition(270, 1)));
        this.ai = new XCS(possibleActions);
    }

    public void step() {
        final Situation sigmaT = new Situation();
        //TODO: Compute Reward
        Action action = this.ai.step(sigmaT, 0);
        action.ExecuteOn(this.unit);
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

    private double getDistance(Unit enemy) {
    	return this.unit.getPosition().getDistance(enemy.getPosition());
    }
}
