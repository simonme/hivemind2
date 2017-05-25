import java.io.PrintWriter;
import java.security.InvalidParameterException;
import java.util.Objects;
import java.util.Random;
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

    public boolean isMoreGeneral(IntervalPredicate other)
    {
        return (this.lowerBound <= other.lowerBound) && (this.upperBound >= other.upperBound);
    }

    public void crossover(IntervalPredicate other, boolean firstHalf)
    {
        if(firstHalf)
        {
            double tmp = this.lowerBound;
            this.lowerBound = other.lowerBound;
            other.lowerBound = tmp;
        }
        else
        {
            double tmp = this.upperBound;
            this.upperBound = other.upperBound;
            other.upperBound = tmp;
        }
        reorder();
        other.reorder();
    }

    public void mutate(Random random)
    {
        double sign1 = random.nextDouble();
        double sign2 = random.nextDouble();
        double rand1 = random.nextDouble() * XCSConfig.m0;
        double rand2 = random.nextDouble() * XCSConfig.m0;
        lowerBound += rand1 * sign1 >= 0.5 ? 1 : -1;
        upperBound += rand2 * sign2 >= 0.5 ? 1 : -1;
        reorder();
    }

    public void reorder()
    {
        if(lowerBound > upperBound)
        {
            double tmp = lowerBound;
            lowerBound = upperBound;
            upperBound = tmp;
        }
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
