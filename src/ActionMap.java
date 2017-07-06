import bwapi.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by Simon on 13.06.2017.
 */
public class ActionMap {

    private Map<PlayerAIType, ArrayList<Action>> actions = Collections.synchronizedMap(new EnumMap<PlayerAIType,  ArrayList<Action>>(PlayerAIType.class));

    public ActionMap() { // TODO: set correct actions per unit type (include their special abilities!)
        ArrayList<Action> marineActions = new ArrayList<>();
        marineActions.add(new ActionIdle());
        marineActions.add(new ActionMove(new RelativePosition(0+0.0, 50)));
        marineActions.add(new ActionMove(new RelativePosition(90+0.0, 50)));
        marineActions.add(new ActionMove(new RelativePosition(180+0.0, 50)));
        marineActions.add(new ActionMove(new RelativePosition(270+0.0, 50)));
        marineActions.add(new ActionAttackClosestEnemy());
        marineActions.add(new ActionTriggerStimPack());
        actions.put(PlayerAIType.MARINE, marineActions);

        ArrayList<Action> medicActions = new ArrayList<>();
        medicActions.add(new ActionIdle());
        medicActions.add(new ActionMove(new RelativePosition(0+0.0, 50)));
        medicActions.add(new ActionMove(new RelativePosition(90+0.0, 50)));
        medicActions.add(new ActionMove(new RelativePosition(180+0.0, 50)));
        medicActions.add(new ActionMove(new RelativePosition(270+0.0, 50)));
        medicActions.add(new ActionAttackClosestEnemy());
        medicActions.add(new ActionHeal());
        actions.put(PlayerAIType.MEDIC, medicActions);

        ArrayList<Action> siegeTankActions = new ArrayList<>();
        siegeTankActions.add(new ActionIdle());
        siegeTankActions.add(new ActionMove(new RelativePosition(0+0.0, 50)));
        siegeTankActions.add(new ActionMove(new RelativePosition(90+0.0, 50)));
        siegeTankActions.add(new ActionMove(new RelativePosition(180+0.0, 50)));
        siegeTankActions.add(new ActionMove(new RelativePosition(270+0.0, 50)));
        siegeTankActions.add(new ActionAttackClosestEnemy());
        siegeTankActions.add(new ActionEnterSiegeMode());
        siegeTankActions.add(new ActionExitSiegeMode());
        actions.put(PlayerAIType.SIEGE_TANK, siegeTankActions);

        ArrayList<Action> vultureActions = new ArrayList<>();
        vultureActions.add(new ActionIdle());
        vultureActions.add(new ActionMove(new RelativePosition(0+0.0, 50)));
        vultureActions.add(new ActionMove(new RelativePosition(90+0.0, 50)));
        vultureActions.add(new ActionMove(new RelativePosition(180+0.0, 50)));
        vultureActions.add(new ActionMove(new RelativePosition(270+0.0, 50)));
        vultureActions.add(new ActionSpiderMines(new RelativePosition(0+0.0, 0)));
        vultureActions.add(new ActionAttackClosestEnemy());
        actions.put(PlayerAIType.VULTURE, vultureActions);
    }

    public ArrayList<Action> getActions(PlayerAIType playerAIType) {
        return this.actions.get(playerAIType);
    }
}
