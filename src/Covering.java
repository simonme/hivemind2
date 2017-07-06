import Actions.Action;
import Condition.Condition;
import Configuration.XCSConfig;
import Situation.Situation;
import Condition.PredicateFactory;

/**
 * Created by Ferdi on 23.05.2017.
 */
public class Covering {

    public Covering() {
    }

    public Classifier generateCoveringClassifier(Situation sigmaT, Action action, int timestamp){

        Condition condition = new Condition(PredicateFactory.coverSituation(sigmaT));
        return new Classifier(condition,
                action,
                XCSConfig.predictionI,
                XCSConfig.epsilonI,
                XCSConfig.FI,
                0,
                timestamp);
    }

}
