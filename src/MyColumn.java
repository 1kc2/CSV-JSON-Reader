public interface MyColumn {

    /*
     Returns the nae of the column.
     */
    String getName();

    /*
     Returns the size of the column.
     */
    int getSize();

    /*
     Returns the value at the index.
     */
    String getRowValue(int index) throws Exception;

    /*
     Changes the value at an index.
     */
    void setRowValue(int index, String newValue);

    /*
     Adds a new row value for a new patient.
     */
    void addRowValue(String value);
}
