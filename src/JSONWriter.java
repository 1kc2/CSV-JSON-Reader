import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class JSONWriter {

    private DataFrame dataFrame;
    private String[] columnNames;
    private String newLine;
    private String commaNewline;
    private String quote;
    private String tab1;
    private String tab2;
    private String tab3;


    public JSONWriter(DataFrame dataFrame) {
        this.dataFrame = dataFrame;
        this.columnNames = dataFrame.getColumnNames();
        this.newLine = System.lineSeparator();
        this.commaNewline = "," + newLine; // defining certain commonly used strings in the json
        this.quote = "\"";
        this.tab1 = "  ";
        this.tab2 = "    ";
        this.tab3 = "      ";
    }

    /*
     Helper method which returns as a String the data for one item (i.e one row in the GUI).
     */
    private String writeRow(int rowNum, boolean finalRow) throws Exception {
        StringBuilder res = new StringBuilder();
        res.append(newLine).append(tab2).append("{").append(newLine);
        for (int i = 0; i < columnNames.length; i++) {
            res
                .append(tab3).append(quote).append(columnNames[i])
                .append(quote).append(": ").append(quote)
                .append(dataFrame.getValue(i, rowNum)).append(quote).append( (i == columnNames.length-1) ? newLine : commaNewline);
        }
        res.append(tab2).append("}").append( finalRow ? newLine : commaNewline);
        return res.toString();
    }

    /*
     Helper method which returns as a String all the data.
     */
    private String createJSONContents(String dataName) throws Exception {
        StringBuilder res = new StringBuilder();
        res.append("{").append(newLine);
        res.append(tab1).append(quote).append(dataName).append(quote).append(":").append(" [").append(newLine);
        for (int i = 0; i < dataFrame.getRowCount() - 1; i++) res.append(writeRow(i, false));
        res.append(writeRow(dataFrame.getRowCount() - 1, true));
        res.append(tab1).append("]").append(newLine);
        res.append("}");
        return res.toString();
    }

    /*
     Writes the JSON as a file
     */
    public void writeJSON(String filePath) throws Exception {
        String[] nameTokens = filePath.split("/"); // splitting the filepath using '/' as a delimiter
        String dataName = nameTokens[nameTokens.length - 1]; // getting the filename
        dataName = dataName.replace(".json",""); // removing the extension as it is not required
        String contents = createJSONContents(dataName);
        File file = new File(filePath);
        PrintWriter fileWriter = new PrintWriter(file);
        fileWriter.print(contents);
        fileWriter.close();
    }
}
