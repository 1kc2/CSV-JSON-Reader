import java.util.ArrayList;

public class Column implements MyColumn {

    private String name;
    private ArrayList<String> rows;

    public Column(String name) {
        this.name = name;
        this.rows = new ArrayList<>();
    }

    /*
     Returns the nae of the column.
     */
    public String getName() {
        return name;
    }

    /*
     Returns the size of the column.
     */
    public int getSize() {
        return this.rows.size();
    }

    /*
     Returns the value at the index.
     */
    public String getRowValue(int index) throws Exception {
        if (index < 0) throw new Exception("Index must be greater than 0.");
        return this.rows.get(index);
    }

    /*
     Changes the value at an index.
     */
    public void setRowValue(int index, String newValue) {
        this.rows.set(index, newValue);
    }

    /*
     Adds a new row value for a new patient.
     */
    public void addRowValue(String value) {
        this.rows.add(value);
    }
}
