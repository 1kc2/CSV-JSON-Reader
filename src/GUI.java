import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

class GUI implements ActionListener {

    private int ON_CLOSE;
    private Model model;
    private JFileChooser fileChooser;
    private FileNameExtensionFilter filter;
    private JSplitPane splitPane;
    private JTextField textField;
    private TableRowSorter<TableModel> rowSorter;
    private JTable table;
    private JFrame jFrame;
    private JMenu menuFileButton;
    private JMenuItem optionMenuOpen;
    private JLabel welcome;
    private JPanel boxPanel;
    private JCheckBox[] boxes;
    private Dimension screenSize;
    private boolean csv;

    public GUI() {
        this.model = ModelFactory.getModel();
        this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.jFrame = new JFrame("CSV-JSON-Reader");
        this.fileChooser = new JFileChooser();
        this.filter = new FileNameExtensionFilter("csv, json", "csv", "json");
        this.ON_CLOSE = JFrame.EXIT_ON_CLOSE;
    }

    public GUI(int ON_CLOSE) {
        this.model = new Model();
        this.screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.jFrame = new JFrame("CSV-JSON-Reader");
        this.fileChooser = new JFileChooser();
        this.filter = new FileNameExtensionFilter("csv, json", "csv", "json");
        this.ON_CLOSE = ON_CLOSE;
    }

    /*
     Overriding the actionPerformed method to change what happens when user interacts with the GUI.
     */
    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() instanceof JCheckBox) {
            for (int i = 0; i < boxes.length; i++) {
                if (boxes[i].isSelected()) show(i);
                else if (!boxes[i].isSelected()) hide(i);
            }
        }
        else {
            switch (((Component) event.getSource()).getName()) {
                case "Open":
                    fileChooser.addChoosableFileFilter(filter); // only allow user to choose csv or json files
                    fileChooser.setAcceptAllFileFilterUsed(false);
                    if (fileChooser.showOpenDialog(splitPane) == JFileChooser.APPROVE_OPTION) {
                        File file = fileChooser.getSelectedFile();
                        if (file.getName().toLowerCase().endsWith(".csv")) {
                            csv = true; // used as a flag that the user has chosen a csv file
                            try {
                                model.readCSV(file);
                                updateFrame(model.getFrame(), file.getName());
                            } catch (Exception e) {
                                JOptionPane.showMessageDialog(new JFrame("Error"), "File couldn't be parsed successfully", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }

                        if (file.getName().toLowerCase().endsWith(".json")) {
                            try {
                                model.readJSON(file);
                                updateFrame(model.getFrame(), file.getName());
                            } catch (Exception e) {
                                e.printStackTrace();
                                JOptionPane.showMessageDialog(new JFrame("Error"), "File couldn't be parsed successfully", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                    break;
                case "Save as JSON": // what happens if the user clicks the "Save as JSON" button
                    fileChooser.resetChoosableFileFilters();
                    filter = new FileNameExtensionFilter("json","json");
                    fileChooser.addChoosableFileFilter(filter);
                    fileChooser.setDialogTitle("Specify a directory to save in");
                    int userSelection = fileChooser.showSaveDialog(splitPane); // open a file dialog so user can navigate to target directory
                    if (userSelection == JFileChooser.APPROVE_OPTION) {
                        String path = fileChooser.getSelectedFile().getPath();
                        if (!path.endsWith(".json")) path += ".json";
                        try {
                            new JSONWriter(model.getFrame()).writeJSON(path);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case "New window...":
                    new GUI(JFrame.DISPOSE_ON_CLOSE).run(); // open a new GUI
                    break;
                case "Exit":
                    System.exit(0); // end program
            }
        }
    }

    /*
     Displays the data and updates the GUI to add relevant components.
     */
    private void updateFrame(DataFrame dataFrame, String newName) throws Exception {
        jFrame.setTitle(newName); // change window title to file name
        jFrame.remove(welcome);
        menuFileButton.remove(optionMenuOpen); // removing no longer needed components
        JMenuItem newWindow = new JMenuItem("New window...");
        newWindow.setName("New window...");
        newWindow.addActionListener(this);
        newWindow.setFont(new Font("ComicSansMS", Font.PLAIN, newWindow.getFont().getSize()));
        menuFileButton.add(newWindow);
        if (csv) {
            JMenuItem saveAsJSON = new JMenuItem("Save as JSON");
            saveAsJSON.setName("Save as JSON");
            saveAsJSON.addActionListener(this);
            saveAsJSON.setFont(new Font("ComicSansMS", Font.PLAIN, saveAsJSON.getFont().getSize()));
            menuFileButton.add(saveAsJSON);
        }
        JMenuItem exit = new JMenuItem("Exit");
        exit.setName("Exit");
        exit.addActionListener(this);
        exit.setFont(new Font("ComicSansMS", Font.PLAIN, exit.getFont().getSize()));
        menuFileButton.add(exit);
        createCheckboxes(model.getColumnNames());
        table = new JTable(model.getRows(), model.getColumnNames()) { // display JTable containing data
            public boolean isCellEditable ( int row, int column) { return false; }
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) { // change colour of alternate rows in table
                Component c = super.prepareRenderer(renderer, row, column);
                Color color = table.getBackground();
                if (!isRowSelected(row)) c.setBackground(row % 2 == 0 ? getBackground() : new Color(255, 240, 240));
                return c;
            }
        };
        table.setFont(new Font("ComicSansMS", Font.PLAIN, table.getFont().getSize()));
        table.getTableHeader().setReorderingAllowed(false);
        table.setPreferredScrollableViewportSize(new Dimension(500, 100));
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        jFrame.getContentPane().add(BorderLayout.CENTER, scrollPane);
        jFrame.setSize((int) (screenSize.width * 4.0/5.0), (int) (screenSize.height * 4.0/5.0)); // fill 80% of screen with window
        jFrame.setLocationRelativeTo(null); // align frame with centre of screen
        jFrame.add(scrollPane);
        JPanel bottomPanel = new JPanel();
        textField = new JTextField(20);
        rowSorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(rowSorter);
        textField.getDocument().addDocumentListener(new DocumentListener() { // search functionality
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = textField.getText();
                if (text.trim().length() == 0) rowSorter.setRowFilter(null);
                else rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text)); // ignore case in search
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = textField.getText();
                if (text.trim().length() == 0) rowSorter.setRowFilter(null);
                else rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text)); // ignore case in search
            }
            @Override
            public void changedUpdate(DocumentEvent e) { throw new UnsupportedOperationException(); }
        });
        // adding relevant components to GUI
        bottomPanel.add(new JLabel("Search:"), BorderLayout.WEST);
        bottomPanel.add(textField);
        jFrame.getContentPane().add(BorderLayout.SOUTH, bottomPanel);
    }

    /*
     Creates the checkboxes used to filter the data.
     */
    private void createCheckboxes(String[] columnNames) {
        boxes = new JCheckBox[columnNames.length];
        boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
        for (int i = 0; i < columnNames.length; i++) {
            boxes[i] = new JCheckBox(columnNames[i]); // create a checkbox for each column
            boxes[i].setFont(new Font("ComicSansMS", Font.PLAIN, boxes[i].getFont().getSize()));
            boxes[i].setBorder(new EmptyBorder(0,0,0,0));
            boxes[i].setSelected(true);
            boxes[i].addActionListener(this);
            boxPanel.add(boxes[i]);
        }
        jFrame.getContentPane().add(BorderLayout.EAST, boxPanel);
    }

    /*
     Hides the relevant column of data.
     */
    private void hide(int index) {
        table.getColumnModel().getColumn(index).setMaxWidth(0);
        table.getColumnModel().getColumn(index).setMinWidth(0);
        table.getColumnModel().getColumn(index).setWidth(0);
        table.getColumnModel().getColumn(index).setPreferredWidth(0);
    }

    /*
     Shows the relevant column of data.
     */
    private void show(int index) {
        table.getColumnModel().getColumn(index).setMaxWidth(125);
        table.getColumnModel().getColumn(index).setMinWidth(0);
        table.getColumnModel().getColumn(index).setWidth(125);
        table.getColumnModel().getColumn(index).setPreferredWidth(125);
    }

    /*
     Initialises components and the GUI when it is launched.
     */
    public void run() {
        jFrame.setDefaultCloseOperation(this.ON_CLOSE);
        jFrame.setSize(screenSize.width / 2, screenSize.height / 2);
        JMenuBar menuBar = new JMenuBar(); // initialising menubar to user to open a file
        menuFileButton = new JMenu("File");
        menuFileButton.setFont(new Font("ComicSansMS", Font.PLAIN, menuFileButton.getFont().getSize()));
        menuBar.add(menuFileButton);
        optionMenuOpen = new JMenuItem("Open");
        optionMenuOpen.setFont(new Font("ComicSansMS", Font.PLAIN, optionMenuOpen.getFont().getSize()));
        optionMenuOpen.setName("Open");
        menuFileButton.add(optionMenuOpen);
        optionMenuOpen.addActionListener(this);
        welcome = new JLabel("<html><center>Click" + // the text when GUI is launched
                "<br/><center><span style=\"color:#B93519;font-family: 'Courier';font-size: 25px\">File &#8594; Open</span>" +
                "<br/><center>to load a file</center></html>", SwingConstants.CENTER);
        welcome.setFont(new Font("ComicSansMS", Font.PLAIN, 28));
        jFrame.getContentPane().add(BorderLayout.CENTER, welcome);
        jFrame.getContentPane().add(BorderLayout.NORTH, menuBar);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
    }

}