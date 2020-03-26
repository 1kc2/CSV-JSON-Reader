import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class DataFrame implements MyDataFrame {

    private ArrayList<Column> columns;

    public DataFrame() {
        this.columns = new ArrayList<>();
    }

    /*
     Returns the column field of the class.
     */
    public ArrayList<Column> getColumns() {
        return columns;
    }

    /*
     Adds a new column (i.e. another data type).
     */
    public void addColumn(String columnName) {
        this.columns.add(new Column(columnName));
    }

    /*
     Returns a boolean indicating whether a column already exists in the DataFrame.
     */
    public boolean columnExists(String columnName) {
       for (Column column : this.columns) if (column.getName().equals(columnName)) return true;
       return false;
    }

    /*
     Returns the names of all the columns (i.e. all the types of data held).
     */
    public String[] getColumnNames() {
        String[] columnNames = new String[this.getColumnCount()];
        for(int i = 0; i < this.getColumnCount(); i++) columnNames[i] = this.getColumns().get(i).getName();
        return columnNames;
    }

    /*
     Returns an array of arrays with each inner array containing all the data of one patient.
     */
    public String[][] getRows() throws Exception {
        int numColumns = this.getColumnCount();
        int numRows = this.getRowCount();
        String[][] data = new String[numRows][numColumns];
        for (int i = 0; i < numColumns; i++) for (int j = 0; j < numRows; j++) data[j][i] = this.getValue(i,j);
        return data;
    }

    /*
     Returns the number of rows/patients.
     */
    public int getRowCount() {
        return columns.get(0).getSize();
    }

    /*
     Returns the number of columns.
     */
    public int getColumnCount() {
        return this.columns.size();
    }

    /*
     Returns the value at a particular index within the DataFrame.
     */
    public String getValue(int column, int row) throws Exception {
        return this.columns.get(column).getRowValue(row);
    }

    /*
     Takes the type of data and the patient index and returns the corresponding data for that patient.
     */
    public String getValue(String columnName, int row) throws Exception {
        for (Column column : this.columns) if (column.getName().equals(columnName)) return column.getRowValue(row);
        return null;
    }

    /*
     Takes the type of data, patient index and the data value and adds it to the column.
     */
    public void putValue(String columnName, int row, String value) {
        for (Column column : this.columns) {
            if (column.getName().equals(columnName)) {
                column.setRowValue(row, value);
            }
        }
    }

    /*
     Takes a type of data and a data value and adds a new row.
     */
    public void addValue(String columnName, String value) {
        for (Column column: this.columns) if (column.getName().equals(columnName)) column.addRowValue(value);
    }

    /*
     Takes the index of the data type in the column and a value and adds the value at the index.
     */
    public void addToColumn(int index, String value) {
        this.columns.get(index).addRowValue(value);
    }
}
