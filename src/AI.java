import java.io.PrintWriter;

/**
 * Created by Simon on 15.05.2017.
 */
public interface AI {

    public Action step(Situation sigmaT, double reward);
    public void serialize(CSVWriter writer);
}
