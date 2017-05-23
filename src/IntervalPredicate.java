/**
 * Created by Ferdi on 23.05.2017.
 */
public class IntervalPredicate {

    public IntervalPredicate(double lowerBound, double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    private double lowerBound;
    private double upperBound;


    public boolean contains(double d){
        if ((lowerBound <= d) || (upperBound >= d)){
            return true;
        }
        return false;
    }

}
