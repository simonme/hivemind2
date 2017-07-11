import java.util.ArrayList;
import java.util.Random;
/**
 * Created by Jakob on 04.07.2017.
 */

public class Parameters {
    // r_sig
//    public double neighborhoodRange = 0.2;
    private double neighborhoodRange = 20;
    // r_sep
//    public double separationRange = 0.04;
    private double separationRange = 4;
    // r_col
//    public double columnWidth = 0.05;
    private double columnWidth = 5;
    // r_col-sep
//    public double columnSeparationRange = 0.03;
    private double columnSeparationRange = 3;
    // r_lin
//    public double lineHeight = 0.05;
    private double lineHeight = 5;
    // r_lin-sep
//    public double lineSeparationRange = 0.03;
    private double lineSeparationRange = 3;
    // w_r1-a
    private double weightEnemy = 0.0;
    // w_r1-b
    private double weightEnemyVisible = 0.1;
    // w_r2
    private double weightSeparation = 0.5;
    // w_r3
    private double weightColumn = 0.2;
    // w_r4
    private double weightLine = 0.2;
    // Summe der Hitpoints verbuendeter Einheiten bei Spielende bzw. negativ bei Niederlage
    private int fitness = Integer.MIN_VALUE;

    private Random random = new Random();

    private ArrayList<Double> parameterList = new ArrayList<>();

    public Parameters(double neighborhoodRange, double separationRange, double columnWidth,
                      double columnSeparationRange, double lineHeight, double lineSeparationRange,
                      double weightEnemy, double weightEnemyVisible, double weightSeparation, double weightColumn,
                      double weightLine, int fitness) {
        this.neighborhoodRange = neighborhoodRange;
        this.separationRange = separationRange;
        this.columnWidth = columnWidth;
        this.columnSeparationRange = columnSeparationRange;
        this.lineHeight = lineHeight;
        this.lineSeparationRange = lineSeparationRange;
        this.weightEnemy = weightEnemy;
        this.weightEnemyVisible = weightEnemyVisible;
        this.weightSeparation = weightSeparation;
        this.weightColumn = weightColumn;
        this.weightLine = weightLine;
        this.fitness = fitness;
    }

    public Parameters(Parameters p) {
        this.neighborhoodRange = p.getNeighborhoodRange();
        this.separationRange = p.getSeparationRange();
        this.columnWidth = p.getColumnWidth();
        this.columnSeparationRange = p.getColumnSeparationRange();
        this.lineHeight = p.getLineHeight();
        this.lineSeparationRange = p.getLineSeparationRange();
        this.weightEnemy = p.getWeightEnemy();
        this.weightEnemyVisible = p.getWeightEnemyVisible();
        this.weightSeparation = p.getWeightSeparation();
        this.weightColumn = p.getWeightColumn();
        this.weightLine = p.getWeightLine();
        updateParameterList();
        this.fitness = p.getFitness();
    }

    public Parameters() {

    }

    public ArrayList<Double> getParameterList() {
        return parameterList;
    }

    public void updateParameterList() {
        this.parameterList = new ArrayList<Double>();
        this.parameterList.add(neighborhoodRange);
        this.parameterList.add(separationRange);
        this.parameterList.add(columnWidth);
        this.parameterList.add(columnSeparationRange);
        this.parameterList.add(lineHeight);
        this.parameterList.add(lineSeparationRange);
        this.parameterList.add(weightEnemy);
        this.parameterList.add(weightEnemyVisible);
        this.parameterList.add(weightSeparation);
        this.parameterList.add(weightColumn);
        this.parameterList.add(weightLine);
    }

    public void setParameterList(ArrayList<Double> parameterList){
        this.parameterList = parameterList;
        setNeighborhoodRange(parameterList.get(0));
        setSeparationRange(parameterList.get(1));
        setColumnWidth(parameterList.get(2));
        setColumnSeparationRange(parameterList.get(3));
        setLineHeight(parameterList.get(4));
        setLineSeparationRange(parameterList.get(5));
        setWeightEnemy(parameterList.get(6));
        setWeightEnemyVisible(parameterList.get(7));
        setWeightSeparation(parameterList.get(8));
        setWeightColumn(parameterList.get(9));
        setWeightLine(parameterList.get(10));
        updateParameterList();
    }

    public double getNeighborhoodRange() {
        return neighborhoodRange;
    }

    public void setNeighborhoodRange(double neighborhoodRange) {
        if (neighborhoodRange < 0)
            this.neighborhoodRange = 0;
        else if (neighborhoodRange > 1)
            this.neighborhoodRange = 1;
        else
            this.neighborhoodRange = neighborhoodRange;
        updateParameterList();
    }

    public double getSeparationRange() {
        return separationRange;
    }

    public void setSeparationRange(double separationRange) {
        if (separationRange < 0)
            this.separationRange = 0;
        else if (separationRange > 1)
            this.separationRange = 1;
        else
            this.separationRange = separationRange;
        updateParameterList();
    }

    public double getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(double columnWidth) {
        if (columnWidth < 0)
            this.columnWidth = 0;
        else if (columnWidth > 1)
            this.columnWidth = 1;
        else
            this.columnWidth = columnWidth;
        updateParameterList();
    }

    public double getColumnSeparationRange() {
        return columnSeparationRange;
    }

    public void setColumnSeparationRange(double columnSeparationRange) {
        if (columnSeparationRange < 0)
            this.columnSeparationRange = 0;
        else if (columnSeparationRange > 1)
            this.columnSeparationRange = 1;
        else
            this.columnSeparationRange = columnSeparationRange;
        updateParameterList();
    }

    public double getLineHeight() {
        return lineHeight;
    }

    public void setLineHeight(double lineHeight) {
        if (lineHeight < 0)
            this.lineHeight = 0;
        else if (lineHeight > 1)
            this.lineHeight = 1;
        else
            this.lineHeight = lineHeight;
        updateParameterList();
    }

    public double getLineSeparationRange() {
        return lineSeparationRange;
    }

    public void setLineSeparationRange(double lineSeparationRange) {
        if (lineSeparationRange < 0)
            this.lineSeparationRange = 0;
        else if (lineSeparationRange > 1)
            this.lineSeparationRange = 1;
        else
            this.lineSeparationRange = lineSeparationRange;
        updateParameterList();
    }

    public double getWeightEnemy() {
        return weightEnemy;
    }

    public void setWeightEnemy(double weightEnemy) {
        if (weightEnemy < 0)
            this.weightEnemy = 0;
        else if (weightEnemy > 2)
            this.weightEnemy = 2;
        else
            this.weightEnemy = weightEnemy;
        updateParameterList();
    }

    public double getWeightEnemyVisible() {
        return weightEnemyVisible;
    }

    public void setWeightEnemyVisible(double weightEnemyVisible) {
        if (weightEnemyVisible < 0)
            this.weightEnemyVisible = 0;
        else if (weightEnemyVisible > 2)
            this.weightEnemyVisible = 2;
        else
            this.weightEnemyVisible = weightEnemyVisible;
        updateParameterList();
    }

    public double getWeightSeparation() {
        return weightSeparation;
    }

    public void setWeightSeparation(double weightSeparation) {
        if (weightSeparation < 0)
            this.weightSeparation = 0;
        else if (weightSeparation > 2)
            this.weightSeparation = 2;
        else
            this.weightSeparation = weightSeparation;
        updateParameterList();
    }

    public double getWeightColumn() {
        return weightColumn;
    }

    public void setWeightColumn(double weightColumn) {
        if (weightColumn < 0)
            this.weightColumn = 0;
        else if (weightColumn > 2)
            this.weightColumn = 2;
        else
            this.weightColumn = weightColumn;
        updateParameterList();
    }

    public double getWeightLine() {
        return weightLine;
    }

    public void setWeightLine(double weightLine) {
        if (weightLine < 0)
            this.weightLine = 0;
        else if (weightLine > 2)
            this.weightLine = 2;
        else
            this.weightLine = weightLine;
        updateParameterList();
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public double mutateAttribute(double attributeValue, double probability, double delta){
        double newAttributeValue = attributeValue;
        if (random.nextDouble() <= probability){
            double actualDelta = random.nextDouble()*delta;
            if (random.nextDouble() < 0.5)
                newAttributeValue = attributeValue + actualDelta;
            else
                newAttributeValue = attributeValue - actualDelta;
        }
        return newAttributeValue;
    }

    public void mutate(double mutationProbability, double mutationDelta){
        setNeighborhoodRange(mutateAttribute(neighborhoodRange, mutationProbability, mutationDelta));
        setSeparationRange(mutateAttribute(separationRange, mutationProbability, mutationDelta));
        setColumnWidth(mutateAttribute(columnWidth, mutationProbability, mutationDelta));
        setColumnSeparationRange(mutateAttribute(columnSeparationRange, mutationProbability, mutationDelta));
        setLineHeight(mutateAttribute(lineHeight, mutationProbability, mutationDelta));
        setLineSeparationRange(mutateAttribute(lineSeparationRange, mutationProbability, mutationDelta));
        setWeightEnemy(mutateAttribute(weightEnemy, mutationProbability, mutationDelta));
        setWeightEnemyVisible(mutateAttribute(weightEnemyVisible, mutationProbability, mutationDelta));
        setWeightSeparation(mutateAttribute(weightSeparation, mutationProbability, mutationDelta));
        setWeightColumn(mutateAttribute(weightColumn, mutationProbability, mutationDelta));
        setWeightLine(mutateAttribute(weightLine, mutationProbability, mutationDelta));
    }
}
