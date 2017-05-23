import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Simon on 15.05.2017.
 */
public class XCS implements AI {
    private LinkedHashSet<Classifier> population;
    private XCSConfig xcsConfig;

    public XCS() {
        this.xcsConfig = new XCSConfig();// TODO not necessary, all XCSConfig parameters are public static final XY

    }

    @Override
    public Action step(Situation sigmaT) {
        // TODO update previous action set, if there is any
        Set<Classifier> matchSet = generateMatchSet(sigmaT);
        Map<Action, Double> predictionArray = generatePredictionArray(matchSet);
        Set<Classifier> actionSet =
        // TODO
        return null;
    }

    private Set<Classifier> generateMatchSet(Situation sigmaT) {
        return null;
    }

    private Map<Action, Double> generatePredictionArray(Set<Classifier> matchSet) {
        return null;
    }

    private Action selectAction(Map<Action, Double> predictionArray) {
        return null;
    }

    private Set<Classifier> generateActionSet(Situation sigmaT) {
        return null;
    }


    @Override
    public Action step(Situation sigmaT) {
        return null;
    }
}
