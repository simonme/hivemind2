/**
 * Created by Simon on 15.05.2017.
 */
public class Classifier {
    private Condition condition;
    private Action action;
    private double prediction;
    private double error;
    private double fitness;
    private double experience;
    private int lastGA = Integer.MIN_VALUE;
    private double meanActionSetSize = 0;
    private int numerosity = 0;
    private int timeStamp = Integer.MIN_VALUE;

    public Classifier(Condition cond, Action act, double pred, double error, double fitness, double exp, int ts) {
        setCondition(cond);
        setAction(act);
        setPrediction(pred);
        setError(error);
        setFitness(fitness);
        setExperience(exp);
        setTimeStamp(ts);
    }

    public void update(double reward) {

    }

    public boolean matches(Situation sigmaT) {
        return this.condition.matches(sigmaT);
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public double getPrediction() {
        return prediction;
    }

    public void setPrediction(double prediction) {
        this.prediction = prediction;
    }

    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getExperience() {
        return experience;
    }

    public void setExperience(double experience) {
        this.experience = experience;
    }

    public int getLastGA() {
        return lastGA;
    }

    public void setLastGA(int lastGA) {
        this.lastGA = lastGA;
    }

    public double getMeanActionSetSize() {
        return meanActionSetSize;
    }

    public void setMeanActionSetSize(double meanActionSetSize) {
        this.meanActionSetSize = meanActionSetSize;
    }

    public int getNumerosity() {
        return numerosity;
    }

    public void setNumerosity(int numerosity) {
        this.numerosity = numerosity;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }
}