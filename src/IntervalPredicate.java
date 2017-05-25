import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.util.Objects;
import java.util.Scanner;

/**
 * Created by Ferdi on 23.05.2017.
 */
public class IntervalPredicate {

    private double lowerBound;
    private double upperBound;

    public IntervalPredicate(double lowerBound, double upperBound) throws InvalidParameterException {
        if(lowerBound > upperBound)
        {
            throw new InvalidParameterException("lowerBound has to be less than or equal to upperBound");
        }
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public IntervalPredicate(Scanner scanner)
    {
        this.lowerBound = Double.parseDouble(scanner.next());
        this.upperBound = Double.parseDouble(scanner.next());
    }

    public void serialize(CSVWriter writer)
    {
        writer.write(this.lowerBound);
        writer.write(this.upperBound);
    }


    public boolean contains(double d){
        return (lowerBound <= d) && (upperBound >= d);
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof IntervalPredicate))return false;
        IntervalPredicate other = (IntervalPredicate) obj;
        return this.lowerBound == other.lowerBound
                && this.upperBound == other.upperBound;
    }

    @Override
    public int hashCode() {
        return 31 * Objects.hashCode(lowerBound) + 23 * Objects.hashCode(upperBound);
    }

}
