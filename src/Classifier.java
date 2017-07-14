import Actions.Action;
import Condition.Condition;
import Condition.PredicateFactory;
import Configuration.XCSConfig;
import Serialization.CSVWriter;
import Situation.Situation;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

/**
 * Created by Simon on 15.05.2017.
 */
public class Classifier {
    public int hashOnEnter = 0;
    private Condition condition;
    private Action action;
    private double prediction;
    private double error;
    private double fitness;
    private double experience;
    private int lastGA = 0;
    private double meanActionSetSize = 1;
    private int numerosity = 1;
    private int timeStamp = 0;

    public Classifier(Condition cond, Action act, double pred, double error, double fitness, double exp, int ts) {
        setCondition(cond);
        setAction(act);
        setPrediction(pred);
        setError(error);
        setFitness(fitness);
        setExperience(exp);
        setTimeStamp(ts);
    }

    public Classifier(Scanner scanner, ArrayList<Action> possibleActions, PredicateFactory predicateFactory) {
        setPrediction(Double.parseDouble(scanner.next()));
        setError(Double.parseDouble(scanner.next()));
        setFitness(Double.parseDouble(scanner.next()));
        setNumerosity(Integer.parseInt(scanner.next()));
        setExperience(Double.parseDouble(scanner.next()));
        setTimeStamp(Integer.parseInt(scanner.next()));
        setLastGA(Integer.parseInt(scanner.next()));
        setAction(possibleActions.get(Integer.parseInt(scanner.next())));
        setCondition(new Condition(scanner, predicateFactory));
    }

    public void serialize(CSVWriter writer, ArrayList<Action> possibleActions) {
        writer.write(getPrediction());
        writer.write(getError());
        writer.write(getFitness());
        writer.write(getNumerosity());
        writer.write(getExperience());
        writer.write(getTimeStamp());
        writer.write(getLastGA());
        writer.write(possibleActions.indexOf(getAction()));
        getCondition().serialize(writer);
    }

    public void update(double reward, int totalActionSetSize, double accuracy, double accuracySum) {
        experience++;
        // update prediction
        if (experience < 1 / XCSConfig.beta) {
            prediction += (reward - prediction) / experience;
        } else {
            prediction += XCSConfig.beta * (reward - prediction);
        }
        // update prediction error
        if (experience < 1 / XCSConfig.beta) {
            error += (Math.abs(reward - prediction) - error) / experience;
        } else {
            error += XCSConfig.beta * (Math.abs(reward - prediction) - error);
        }
        // update action set size estimate
        if (experience < 1 / XCSConfig.beta) {
            meanActionSetSize += (totalActionSetSize - meanActionSetSize) / experience;
        } else {
            meanActionSetSize += XCSConfig.beta * (totalActionSetSize - meanActionSetSize);
        }
        // update fitness
        fitness += XCSConfig.beta * (accuracy * numerosity / accuracySum - fitness);
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


    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Classifier)) return false;
        Classifier other = (Classifier) obj;
        return this.condition.equals(other.condition)
                && this.action.equals(other.action)
                && this.prediction == other.prediction
                && this.error == other.error
                && this.fitness == other.fitness
                && this.experience == other.experience
                && this.lastGA == other.lastGA
                && this.meanActionSetSize == other.meanActionSetSize
                && this.numerosity == other.numerosity
                && this.timeStamp == other.timeStamp
                ;
    }

    @Override
    public int hashCode() {
        // The condition shouldn't change, but somehow the hash code can change if the condition is used, so leave it out
        // Don't change this, or the population.remove function will not work
        return 23 * 17 //+ Objects.hashCode(this.condition)
                + Objects.hashCode(this.action)
                + Objects.hashCode(this.timeStamp)
                ;
    }

    public Classifier copy() {
        return new Classifier(getCondition(), getAction(), getPrediction(), getError(), getFitness(), getExperience(), getTimeStamp());
    }
}