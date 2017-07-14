import Actions.Action;
import Condition.Condition;
import Condition.PredicateFactory;
import Configuration.XCSConfig;
import Situation.Situation;

/**
 * Created by Ferdi on 23.05.2017.
 */
public class Covering {

    private PredicateFactory predicateFactory;

    public Covering(PredicateFactory predicateFactory) {
        this.predicateFactory = predicateFactory;
    }

    public Classifier generateCoveringClassifier(Situation sigmaT, Action action, int timestamp) {

        Condition condition = new Condition(predicateFactory.coverSituation(sigmaT));
        return new Classifier(condition,
                action,
                XCSConfig.predictionI,
                XCSConfig.epsilonI,
                XCSConfig.FI,
                0,
                timestamp);
    }

}
