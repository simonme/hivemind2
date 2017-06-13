import bwapi.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

public class HiveMind2 extends DefaultBWListener implements Runnable {

    private final Mirror bwapi;

    private static final String CSV_SUFFIX = ".csv";
    
    private Game game;

    private Player self;

    private HashSet<Unit> enemyUnits;

    private HashSet<Unit> alliedUnits;

    private HashSet<PlayerAI> playerAIs;

    private ActionMap actionMap;

    private Map<PlayerAIType, AI> AIs = Collections.synchronizedMap(new EnumMap<PlayerAIType, AI>(PlayerAIType.class));

    private int frame;

    public HiveMind2() {
        System.out.println("This is HiveMind 2.0! :)");
        this.bwapi = new Mirror();
        this.playerAIs = new HashSet<>();
        this.actionMap = new ActionMap();
    }

    public static void main(String[] args) {
        new HiveMind2().run();
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
            this.AIs.keySet().forEach(playerAIType -> {
                String playerAITypeString = playerAIType.toString();
                try {
                    FileWriter fileWriter = new FileWriter(XCSConfig.CSV_PREFIX + playerAITypeString + CSV_SUFFIX);
                    PrintWriter printWriter = new PrintWriter(fileWriter);
                    CSVWriter csvWriter = new CSVWriter(printWriter);
                    AIs.get(playerAIType).serialize(csvWriter);
                }
                catch (Exception ex) {
                    System.out.println("Failed to serialize " + playerAITypeString);
                }
            });
        } else {
            System.out.println("Did not save XCS to CSV, XCSConfig.SHOULD_SAVE_TO_CSV is false");
        }
    }

    @Override
    public void onFrame() {

        if (frame % 10 == 0) {
            playerAIs.forEach(PlayerAI::step);
        }

        if (frame % 1000 == 0) {
            System.out.println("Frame: " + frame);
        }
        frame++;
    }

    private AI getAI(PlayerAIType playerAIType) {
        if (this.AIs.get(playerAIType) == null && XCSConfig.SHOULD_LOAD_FROM_CSV) {
            try {
                this.AIs.put(playerAIType, new XCS(actionMap.getActions(playerAIType), new Scanner(new FileReader(XCSConfig.CSV_PREFIX + playerAIType.toString() + CSV_SUFFIX))));
            }
            catch(Exception ex) {
                System.out.println("Failed reading csv: " + ex.getMessage());
            }
        }

        if (this.AIs.get(playerAIType) == null) {
            this.AIs.put(playerAIType, new XCS(actionMap.getActions(playerAIType)));
        }

        return this.AIs.get(playerAIType);
    }

    private void handleNewUnit(Unit unit) {
        UnitType type = unit.getType();
        if (unit.getPlayer() == this.self) {
            alliedUnits.add(unit);
            if (type == UnitType.Terran_Marine) {
                this.playerAIs.add(new PlayerAI(unit, bwapi, enemyUnits, alliedUnits, /*TODO: marineEvaluator*/ new VultureEvaluator(), getAI(PlayerAIType.MARINE)));
            } else if (type == UnitType.Terran_Medic) {
                this.playerAIs.add(new PlayerAI(unit, bwapi, enemyUnits, alliedUnits, /*TODO: marineMedic*/ new VultureEvaluator(), getAI(PlayerAIType.MEDIC)));
            } else if (type == UnitType.Terran_Siege_Tank_Tank_Mode || type == UnitType.Terran_Siege_Tank_Siege_Mode) {
                this.playerAIs.add(new PlayerAI(unit, bwapi, enemyUnits, alliedUnits, /*TODO: siegeTankEvaluator*/ new VultureEvaluator(), getAI(PlayerAIType.SIEGE_TANK)));
            } else if (type == UnitType.Terran_Vulture) {
                this.playerAIs.add(new PlayerAI(unit, bwapi, enemyUnits, alliedUnits, new VultureEvaluator(), getAI(PlayerAIType.VULTURE)));
            }
        } else if (unit.getPlayer() != this.self) {
            enemyUnits.add(unit);
        }
    }

    @Override
    public void onUnitCreate(Unit unit) {
        handleNewUnit(unit);
    }

    @Override
    public void onUnitDiscover(Unit unit) {
        handleNewUnit(unit);
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
