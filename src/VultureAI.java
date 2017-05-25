import bwapi.*;

import java.util.ArrayList;
import java.util.HashSet;

public class VultureAI  extends DefaultBWListener implements Runnable {

    private final Mirror bwapi;
    
    private Game game;

    private Player self;

    private Vulture vulture;

    private HashSet<Unit> enemyUnits;

    private AI ai = null;

    private int frame;

    public VultureAI() {
        System.out.println("This is the VultureAI! :)");
        this.bwapi = new Mirror();
    }

    public static void main(String[] args) {
        new VultureAI().run();
    }

    @Override
    public void onStart() {
        enemyUnits = new HashSet<Unit>();
        this.game = this.bwapi.getGame();
        this.self = game.self();
        this.frame = 0;

        // complete map information
        this.game.enableFlag(0);
        
        // user input
        this.game.enableFlag(1);
        this.game.setLocalSpeed(10);
        
    }

    @Override
    public void onFrame() {

        if (frame % 8 == 0) {
            vulture.step();
        }

        // save XCS to CSV every 100th frame
        if (!XCSConfig.SHOULD_LOAD_FROM_CSV && (frame % 100 == 0)) {
            System.out.println("Saving XCS to CSV");
            // TODO save to CSV. use a serialize function of XCS
        }

        if (frame % 1000 == 0) {
            System.out.println("Frame: " + frame);
        }
        frame++;
    }

    private AI getAI() {
        if (XCSConfig.SHOULD_LOAD_FROM_CSV) {
            // TODO load XCS from csv. (one fixed path should suffice for the moment)
            // probably use two files, one for the single properties and one for a classifier list
            // use a deserialize function of XCSF
        }

        if (this.ai == null) {
            ArrayList<Action> possibleActions = new ArrayList<>();
            possibleActions.add(new ActionMove(new RelativePosition(0+0.0, 8)));
            possibleActions.add(new ActionMove(new RelativePosition(90+0.0, 8)));
            possibleActions.add(new ActionMove(new RelativePosition(180+0.0, 8)));
            possibleActions.add(new ActionMove(new RelativePosition(270+0.0, 8)));
            possibleActions.add(new ActionAttackMove(new RelativePosition(0+0.0, 8)));
            possibleActions.add(new ActionAttackMove(new RelativePosition(90+0.0, 8)));
            possibleActions.add(new ActionAttackMove(new RelativePosition(180+0.0, 8)));
            possibleActions.add(new ActionAttackMove(new RelativePosition(270+0.0, 8)));
            possibleActions.add(new ActionSpiderMines(new RelativePosition(0+0.0, 8)));
            possibleActions.add(new ActionSpiderMines(new RelativePosition(90+0.0, 8)));
            possibleActions.add(new ActionSpiderMines(new RelativePosition(180+0.0, 8)));
            possibleActions.add(new ActionSpiderMines(new RelativePosition(270+0.0, 8)));
            this.ai = new XCS(possibleActions);
        }

        return this.ai;
    }

    @Override
    public void onUnitCreate(Unit unit) {
        System.out.println("New unit discovered " + unit.getType());
        UnitType type = unit.getType();

        if (type == UnitType.Terran_Vulture) {
            if (unit.getPlayer() == this.self) {
                this.vulture = new Vulture(unit, bwapi, enemyUnits, new VultureEvaluator(), getAI());
            }
        } else if (type == UnitType.Protoss_Zealot) {
            if (unit.getPlayer() != this.self) {
                enemyUnits.add(unit);
            }
        }
    }
    
    @Override
    public void onUnitDestroy(Unit unit) {
    	if(this.enemyUnits.contains(unit)){
            this.enemyUnits.remove(unit);
    	}
    }
    

    @Override
    public void onEnd(boolean winner) {
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
    public void onNukeDetect(Position position) {
    }

    @Override
    public void onUnitEvade(Unit unit) {
    }

    @Override
    public void onUnitShow(Unit unit) {

    }

    @Override
    public void onUnitHide(Unit unit) {
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
