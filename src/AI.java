import Actions.Action;
import Serialization.CSVWriter;
import Situation.Situation;

/**
 * Created by Simon on 15.05.2017.
 */
public interface AI {

    public Action step(Situation sigmaT, double reward, int unitID);

    public void serialize(CSVWriter writer);
}
