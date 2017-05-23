import java.security.InvalidParameterException;

/**
 * Created by Ferdi on 23.05.2017.
 */
public class IntervalPredicate {

    public IntervalPredicate(double lowerBound, double upperBound) throws InvalidParameterException {
        if(lowerBound > upperBound)
        {
            throw new InvalidParameterException("lowerBound has to be less than or equal to upperBound");
        }
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    private double lowerBound;
    private double upperBound;


    public boolean contains(double d){
        return (lowerBound <= d) && (upperBound >= d);
    }

}
