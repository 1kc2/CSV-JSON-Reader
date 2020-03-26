import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONReader {

    /*
     Returns a filled DataFrame after parsing the JSON.
     */
    public DataFrame readJSON(File file) throws FileNotFoundException {
        DataFrame dataFrame = new DataFrame();
        Pattern pattern = Pattern.compile("\"([^\"]*)\""); // regex to extract values between quotes
        Matcher matcher;
        Scanner in = new Scanner(file);
        String[] tokens = new String[2];
        int tokensIndex;
        String line;

        while (in.hasNextLine()) {
            tokensIndex = 0;
            line = in.nextLine();
            if (!line.contains("\"") || (line.contains("\"") && line.contains("["))) continue; // only read lines containing the data
            matcher = pattern.matcher(line);
            while (matcher.find()) {
                tokens[tokensIndex] = matcher.group(1); // put the datatype and value in a array of size 2
                tokensIndex++;
            }
            if (!dataFrame.columnExists(tokens[0])) { dataFrame.addColumn(tokens[0]); } // creating the columns
            dataFrame.addValue(tokens[0], tokens[1]); // populating the DataFrame
        }
    return dataFrame;
    }
}
