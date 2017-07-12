import bwapi.*;

import java.util.HashSet;
import java.util.List;

/**
 * Created by Ferdi on 11.07.2017.
 */
public class MarineAI  extends DefaultBWListener implements Runnable{

    private final Mirror bwapi;
    private Game game;

    private Player self;

    private final HashSet<Marine> marines;

    private final HashSet<Unit> enemyUnits;

    private int frame;

    private GeneticAlgorithm GA;
    private int parameterIndex = 0;
    private Parameters params;
    private int lastGameReward = 0;

    public MarineAI() {
        System.out.println("This is the MarineAI! :)");

        this.GA = new GeneticAlgorithm();
        this.bwapi = new Mirror();
        this.marines = new HashSet<>();
        this.enemyUnits = new HashSet<>();
    }

    public static void main(String[] args) {
        new MarineAI().run();
    }

    public int calculateReward(){
        int reward = 0;
        for (Marine marine : marines){
            reward += marine.getHP();
        }
        if (reward == 0)
            reward = -100;
        return reward;
    }

    @Override
    public void onStart() {
        this.params = GA.execute(lastGameReward, parameterIndex);
        if (parameterIndex >= GA.getPopulationSize())
            parameterIndex = 0;
        for (Marine marine : marines)
            marine.setParams(this.params);
        this.game = this.bwapi.getGame();
        this.self = game.self();
        this.frame = 0;

        // complete map information
        this.game.enableFlag(0);

        for (Unit unit: this.game.enemy().getUnits())
        {
            System.out.println("Added enemy unit!");
            enemyUnits.add(unit);
        }

        this.game.setLocalSpeed(10);
        System.out.println("Started");
    }

    @Override
    public void onFrame() {
        for (Marine m : this.marines) {
            m.step();
        }

        if (frame % 1000 == 0) {
            System.out.println("Frame: " + frame);
        }
        frame++;
    }

    @Override
    public void onUnitCreate(Unit unit) {
        if (unit.getType() == UnitType.Terran_Marine) {
            if (unit.getPlayer().equals(this.self)) {
                System.out.println("Added marine!");
                Marine marine = new Marine(unit, this.enemyUnits);
                marine.setParams(params);
                this.marines.add(marine);
            } else {
                System.out.println("Added enemy marine unit!");
                this.enemyUnits.add(unit);
            }
        }
    }

    @Override
    public void onUnitDiscover(Unit unit) {
        if (unit.getType() == UnitType.Terran_Marine) {
            if (unit.getPlayer().equals(this.self)) {
                System.out.println("Added marine!");
                Marine marine = new Marine(unit, this.enemyUnits);
                marine.setParams(params);
                this.marines.add(marine);
            } else {
                System.out.println("Added enemy marine unit!");
                this.enemyUnits.add(unit);
            }
        }
    }

    @Override
    public void onUnitDestroy(Unit unit) {
        if (unit == null) {
            return;
        }

        Marine rm = null;
        for (Marine marine : marines) {
            if (marine.getID() == unit.getID()) {
                rm = marine;
                break;
            }
        }
        marines.remove(rm);

        Unit rmUnit = null;
        for (Unit u : enemyUnits) {
            if (u.getID() == unit.getID()) {
                rmUnit = u;
                break;
            }
        }

        enemyUnits.remove(rmUnit);
    }

    @Override
    public void onEnd(boolean winner) {
        this.lastGameReward = calculateReward();
        this.marines.clear();
        this.enemyUnits.clear();
        parameterIndex++;
    }

    @Override
    public void onSendText(String text) {
    }

    @Override
    public void onReceiveText(Player player, String text) {
    }

    @Override
    public void onPlayerLeft(Player player) {
    }

    @Override
    public void onNukeDetect(bwapi.Position position) {
    }

    @Override
    public void onUnitEvade(Unit unit) {
    }

    @Override
    public void onUnitShow(Unit unit) {

        if (unit.getType() == UnitType.Terran_Marine && !unit.getPlayer().equals(this.self)) {
            if (!this.enemyUnits.contains(unit)) {
                this.enemyUnits.add(unit);
            }
        }
    }

    @Override
    public void onUnitHide(Unit unit) {
        if (unit.getType() == UnitType.Terran_Marine && !unit.getPlayer().equals(this.self)) {
            if (!this.enemyUnits.contains(unit)) {
                this.enemyUnits.remove(unit);
            }
        }
    }

    @Override
    public void onUnitMorph(Unit unit) {

    }

    @Override
    public void onUnitRenegade(Unit unit) {

    }

    @Override
    public void onSaveGame(String gameName) {
    }

    @Override
    public void onUnitComplete(Unit unit) {
    }

    @Override
    public void onPlayerDropped(Player player) {
    }

    @Override
    public void run() {
        this.bwapi.getModule().setEventListener(this);
        this.bwapi.startGame();
    }
}
