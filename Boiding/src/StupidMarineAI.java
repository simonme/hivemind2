
import bwapi.*;
import java.util.HashSet;

/**
 * Created by Steffen Wilke on 31.05.2016.
 */
public class StupidMarineAI extends DefaultBWListener implements Runnable {

	private final Mirror bwapi;
	private Game game;

	private Player self;

	private final HashSet<Marine> marines;

	private final HashSet<Unit> enemyUnits;

	private int frame;

	public StupidMarineAI() {
		System.out.println("This is the StupidMarineAI! :)");

		this.bwapi = new Mirror();
		this.marines = new HashSet<Marine>();
		this.enemyUnits = new HashSet<Unit>();
	}

	public static void main(String[] args) {
		new StupidMarineAI().run();
	}

	@Override
	public void onStart() {

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
				this.marines.add(new Marine(unit, this.enemyUnits));
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
                this.marines.add(new Marine(unit, this.enemyUnits));
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
		this.marines.clear();
		this.enemyUnits.clear();
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
