import bwapi.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class VultureAI  extends DefaultBWListener implements Runnable {

    private final Mirror bwapi;
    
    private Game game;

    private Player self;

    private Vulture vulture;

    private HashSet<Unit> enemyUnits;

    private HashSet<Unit> alliedUnits;

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
        alliedUnits = new HashSet<Unit>();
        this.game = this.bwapi.getGame();
        this.self = game.self();
        this.frame = 0;

        // complete map information
        this.game.enableFlag(0);
        
        // user input
        this.game.enableFlag(1);
        this.game.setLocalSpeed(10);
        
    }

    private void saveAIToCSV() { // Is saved on every match end
        if (XCSConfig.SHOULD_SAVE_TO_CSV) {
            System.out.println("Saving XCS to CSV");
            try {
                FileWriter fileWriter = new FileWriter("Test.csv");
                PrintWriter printWriter = new PrintWriter(fileWriter);
                CSVWriter csvWriter = new CSVWriter(printWriter);
                this.ai.serialize(csvWriter);
            }
            catch (Exception ex)
            {
                System.out.println("Failed to serialize");
            }
        } else {
            System.out.println("Did not save XCS to CSV, XCSConfig.SHOULD_SAVE_TO_CSV is false!");
        }
    }

    @Override
    public void onFrame() {

        if (frame % 1 == 0) {
            vulture.step();
        }

        if (frame % 1000 == 0) {
            System.out.println("Frame: " + frame);
        }
        frame++;
    }

    private AI getAI() {
        if (this.ai == null && XCSConfig.SHOULD_LOAD_FROM_CSV) {
            ArrayList<Action> possibleActions = new ArrayList<>();
            possibleActions.add(new ActionMove(new RelativePosition(0+0.0, 50)));
            possibleActions.add(new ActionMove(new RelativePosition(90+0.0, 50)));
            possibleActions.add(new ActionMove(new RelativePosition(180+0.0, 50)));
            possibleActions.add(new ActionMove(new RelativePosition(270+0.0, 50)));
            possibleActions.add(new ActionAttackClosestEnemy());
            try
            {
                this.ai = new XCS(possibleActions, new Scanner(new FileReader("Test.csv")));
            }
            catch(Exception ex)
            {
                System.out.println("Failed reading csv: " + ex.getMessage());
            }
        }

        if (this.ai == null) {
            ArrayList<Action> possibleActions = new ArrayList<>();

            possibleActions.add(new ActionMove(new RelativePosition(0+0.0, 50)));
            possibleActions.add(new ActionMove(new RelativePosition(90+0.0, 50)));
            possibleActions.add(new ActionMove(new RelativePosition(180+0.0, 50)));
            possibleActions.add(new ActionMove(new RelativePosition(270+0.0, 50)));
            possibleActions.add(new ActionAttackClosestEnemy());
            /*possibleActions.add(new ActionAttackMove(new RelativePosition(0+0.0, 80)));
            possibleActions.add(new ActionAttackMove(new RelativePosition(90+0.0, 80)));
            possibleActions.add(new ActionAttackMove(new RelativePosition(180+0.0, 80)));
            possibleActions.add(new ActionAttackMove(new RelativePosition(270+0.0, 80)));*/
            /*possibleActions.add(new ActionSpiderMines(new RelativePosition(0+0.0, 8)));
            possibleActions.add(new ActionSpiderMines(new RelativePosition(90+0.0, 8)));
            possibleActions.add(new ActionSpiderMines(new RelativePosition(180+0.0, 8)));
            possibleActions.add(new ActionSpiderMines(new RelativePosition(270+0.0, 8)));*/
            this.ai = new XCS(possibleActions);
        }

        return this.ai;
    }

    @Override
    public void onUnitCreate(Unit unit) {
        //System.out.println("New unit discovered " + unit.getType());
        UnitType type = unit.getType();

        if (type == UnitType.Terran_Vulture) {
            if (unit.getPlayer() == this.self) {
                this.vulture = new Vulture(unit, bwapi, enemyUnits, alliedUnits, new VultureEvaluator(), getAI());
            }
        } else if (type == UnitType.Protoss_Zealot) {
            if (unit.getPlayer() != this.self) {
                enemyUnits.add(unit);
            }
        } else if (unit.getPlayer() != this.self) {
            enemyUnits.add(unit);
        } else if (unit.getPlayer() == this.self) {
            alliedUnits.add(unit);
        }
    }

    @Override
    public void onUnitDiscover(Unit unit) {
        //System.out.println("New unit discovered " + unit.getType());
        UnitType type = unit.getType();

        if (type == UnitType.Protoss_Zealot) {
            if (unit.getPlayer() != this.self) {
                enemyUnits.add(unit);
            }
        }
    }
    
    @Override
    public void onUnitDestroy(Unit unit) {
    	if(this.enemyUnits.contains(unit)){
            this.enemyUnits.remove(unit);
    	} else if (this.alliedUnits.contains(unit)) {
    	    this.alliedUnits.remove(unit);
        }
    }
    

    @Override
    public void onEnd(boolean winner) {
        System.out.println("Game ended, saving XCS.");
        saveAIToCSV();
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
