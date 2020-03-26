import java.io.File;
import java.io.FileNotFoundException;

public interface MyDataLoader {
    /*
     Reads a CSV into a DataFrame.
     */
    DataFrame readCSV(File file) throws FileNotFoundException;

    /*
     Reads a JSON and returns a filled DataFrame containing the data from the JSON.
     */
    DataFrame readJSON(File file) throws FileNotFoundException;
}
