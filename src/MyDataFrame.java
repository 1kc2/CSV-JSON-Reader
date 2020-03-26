import java.util.ArrayList;

public interface MyDataFrame {
    /*
     Returns the column field of the class.
     */
    ArrayList<Column> getColumns();

    /*
     Adds a new column (i.e. another data type).
     */
    void addColumn(String columnName);

    /*
     Returns a boolean indicating whether a column already exists in the DataFrame.
     */
    boolean columnExists(String columnName);

    /*
     Returns the names of all the columns (i.e. all the types of data held).
     */
    String[] getColumnNames();

    /*
     Returns an array of arrays with each inner array containing all the data for one patient.
     */
    String[][] getRows() throws Exception;

    /*
     Returns the number of rows/patients.
     */
    int getRowCount();

    /*
     Returns the number of columns.
     */
    int getColumnCount();

    /*
     Returns the value at a particular index within the DataFrame.
     */
    String getValue(int column, int row) throws Exception;

    /*
     Takes the type of data and the patient index and returns the corresponding data for that patient.
     */
    String getValue(String columnName, int row) throws Exception;

    /*
     Takes the type of data, patient index and the data value and adds it to the column.
     */
    void putValue(String columnName, int row, String value);

    /*
     Takes a type of data and a data value and adds a new row.
     */
    void addValue(String columnName, String value);

    /*
     Takes the index of the data type in the column and a value and adds the value at the index.
     */
    void addToColumn(int index, String value);
}
