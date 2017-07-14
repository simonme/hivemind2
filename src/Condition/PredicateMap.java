package Condition;

import Configuration.PlayerAIType;
import Configuration.PredicateConfig;

/**
 * Created by Jakob on 06.07.2017.
 */
public class PredicateMap {
    public static PredicateFactory getFactory(PlayerAIType type) {
        if (type == PlayerAIType.MARINE) {
            return new PredicateFactory(PredicateConfig.MARINE_PREDICATES);
        } else if (type == PlayerAIType.MEDIC) {
            return new PredicateFactory(PredicateConfig.MEDIC_PREDICATES);
        } else if (type == PlayerAIType.VULTURE) {
            return new PredicateFactory(PredicateConfig.VULTURE_PREDICATES);
        } else if (type == PlayerAIType.SIEGE_TANK) {
            return new PredicateFactory(PredicateConfig.SIEGE_TANK_PREDICATES);
        }
        return null;
    }
}
