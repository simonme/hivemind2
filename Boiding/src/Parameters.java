/**
 * Created by Jakob on 04.07.2017.
 */
public class Parameters {
    // r_sig
    public double neighborhoodRange = 0.2;
    // r_sep
    public double separationRange = 0.04;
    // r_col
    public double columnWidth = 0.05;
    // r_col-sep
    public double columnSeparationRange = 0.03;
    // r_lin
    public double lineHeight = 0.05;
    // r_lin-sep
    public double lineSeparationRange = 0.03;
    // w_r1-a
    public double weightEnemy = 0.0;
    // w_r1-b
    public double weightEnemyVisible = 0.1;
    // w_r2
    public double weightSeparation = 0.5;
    // w_r3
    public double weightColumn = 0.2;
    // w_r4
    public double weightLine = 0.2;
    // Summe der Hitpoints verbuendeter Einheiten bei Spielende bzw. negativ bei Niederlage
    public int fitness = 10;

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
        this.fitness = p.getFitness();
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
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    //TODO
    public void mutate(double mutationProbability){}
}
