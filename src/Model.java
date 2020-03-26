import jdk.jfr.Frequency;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Model {

    private DataFrame dataFrame;

    public Model() {
        this.dataFrame = new DataFrame();
    }

    public DataFrame getFrame() {
        return this.dataFrame;
    }

    public void readCSV(File file) throws FileNotFoundException {
        dataFrame = new DataLoader().readCSV(file);
    }

    public void readJSON(File file) throws FileNotFoundException {
        dataFrame = new DataLoader().readJSON(file);
    }

    public String[] getColumnNames() {
        return this.dataFrame.getColumnNames();
    }

    public String[][] getRows() throws Exception {
        return this.dataFrame.getRows();
    }

    public String[][] getPatientData() throws Exception {

        String[][] patients = new String[this.dataFrame.getRowCount()][this.dataFrame.getColumnCount()];
        for(int i = 0; i < this.dataFrame.getRowCount(); i++) {
            for(int j = 0; j < this.dataFrame.getColumnCount() - 1; j++) {
                patients[i][j] = this.dataFrame.getValue(j,i);
            }
        }
        return patients;
    }


    /*
     Helper method to convert the date to the correct format so it can be compared.
     */
    private String convertDateToCorrectFormat(String date) {
        String[] splitDate = date.split("-");
        Collections.reverse(Arrays.asList(splitDate));
        return splitDate[0] + "/" + splitDate[1] + "/" + splitDate[2];
    }

    /*
     Helper method which given the position in the column checks if the patient has dead.
     */
    private boolean isDead(int index) throws Exception { return dataFrame.getColumns().get(2).getRowValue(index).equals(""); }

    /*
     Finds the oldest person and returns their name and age.
     */
    public String oldestPerson() throws Exception {
        ArrayList<Date> dates = new ArrayList<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        for (Column column : dataFrame.getColumns()) {
            if (column.getName().toLowerCase().equals("birthdate")) {
                for (int i = 0; i < column.getSize(); i++) {
                    String date = isDead(i) ? column.getRowValue(i) : "2020-03-25"; // ignore dead people
                    dates.add(formatter.parse(convertDateToCorrectFormat(date)));
                }
                int index = dates.indexOf(Collections.min(dates));
                String firstName = dataFrame.getColumns().get(7).getRowValue(index); // get first name of oldest
                String lastName = dataFrame.getColumns().get(8).getRowValue(index); // get last name of oldest
                return firstName + " " + lastName;
            }
        }
        return null;
    }

    /*
     Helper method returns a string containing data of the occurrences
     of each of the different values in a column.
     */
    private String countFrequency(String columnName) throws Exception {
        StringBuilder data = new StringBuilder();
        ArrayList<String> races = new ArrayList<>();
        for (Column column : dataFrame.getColumns()) {
            if (column.getName().toLowerCase().equals(columnName)) {
                for (int i = 0; i < column.getSize(); i++) races.add(column.getRowValue(i));
                Set<String> set = new HashSet<String>(races);
                for (String value : set)
                    data.append(value).append(": ").append(Collections.frequency(races, value)).append(System.lineSeparator());
                data.setLength(data.length() - 1);
                return data.toString();
            }
        }
        return null;
    }

    /*
     Returns a string showing the occurrences of each different race.
     */
    public String raceData() throws Exception {
        return countFrequency("race");
    }

    /*
     Returns a string showing the occurrences of each different ethnicity.
     */
    public String ethnicityData() throws Exception {
        return countFrequency("ethnicity");
    }

    /*
     Returns a string showing the occurrences of each different city.
     */
    public String cityData() throws Exception {
        return countFrequency("city");
    }

    /*
     Returns a string showing the occurrences of each gender.
     */
    public String genderData() throws Exception {
        return countFrequency("gender");
    }

    /*
     Returns a string showing the occurrences of each different birthplace.
     */
    public String birthplaceData() throws Exception {
        return countFrequency("birthplace");
    }

}
