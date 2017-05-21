/**
 * Created by Simon on 15.05.2017.
 */
public class Classifier {
    private Condition condition;
    private double error;
    private double fitness;
    private double experience;
    private int lastGA = Integer.MIN_VALUE;
    private double meanActionSetSize = 0;
    private int numerosity = 0;

    public Classifier() {

    }

    public void update(double reward) {

    }

    public boolean matches(Situation sigmaT) {
        return this.condition.matches(sigmaT);
    }
}
