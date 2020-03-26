import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class DataLoader implements MyDataLoader {

    /*
     Reads a CSV into a DataFrame.
     */
    public DataFrame readCSV(File file) throws FileNotFoundException {
        DataFrame dataFrame = new DataFrame();
        Scanner in = new Scanner(file);
        String[] tokens;
        boolean first = true;

        while (in.hasNextLine()) {
            tokens = in.nextLine().split(",", -1); // split all the values in the row
            if (first) { // for the first row containing the data types
                for (String token : tokens) dataFrame.addColumn(token); // creates a new column for each type of data
                first = false;
                continue;
            }
            for (int j = 0; j < tokens.length; j++) dataFrame.addToColumn(j, tokens[j]);
        }
        return dataFrame;
    }
    /*
     Reads a JSON and returns a filled DataFrame containing the data from the JSON.
     */
    public DataFrame readJSON(File file) throws FileNotFoundException {
        return new JSONReader().readJSON(file);
    }
}
