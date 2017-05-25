import java.io.PrintWriter;

/**
 * Created by Jakob on 25.05.2017.
 */
public class CSVWriter {
    public static final char VALUE_DELIMITER = ';';
    public static final char ENTRY_DELIMITER = '\n';

    private PrintWriter writer;
    private boolean isNewLine = true;

    public CSVWriter(PrintWriter writer)
    {
        this.writer = writer;
    }

    private void writeDelimiter()
    {
        if(isNewLine == false)
        {
            writer.write(VALUE_DELIMITER);
        }
        else
        {
            isNewLine = false;
        }
    }

    public void write(double value)
    {
        writeDelimiter();
        writer.write(Double.toString(value));
    }

    public void write(int value)
    {
        writeDelimiter();
        writer.write(Integer.toString(value));
    }

    public void write(String value)
    {
        writeDelimiter();
        writer.write(value);
    }

    public void newLine()
    {
        writer.write(ENTRY_DELIMITER);
        isNewLine = true;
    }
}
