/* Database Viewer created by Christopher David Lawton using Swing */

package databaseviewer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class DatabaseViewer extends JFrame {
    
    static public ArrayList<Employee> queryResults = new ArrayList<>();
    static public DefaultTableModel resultsModel = new DefaultTableModel(0, 0);

    private static Connection getConnection() throws SQLException, ClassNotFoundException {
        String dbURL = "jdbc:sqlite:sms.db";
        Connection connection = DriverManager.getConnection(dbURL);
        return connection;
    }
    
    public DatabaseViewer () {
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Chris's Database Viewer");
        setSize(1000, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        
        try {
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException |
                 IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.out.println(e);
        }
        
        JPanel title = new JPanel();
        title.setLayout(new FlowLayout(FlowLayout.LEFT));
        title.setPreferredSize(new Dimension(30, 40));
        JLabel titleText = new JLabel("  Stop Making Sense Database");
        Font font = new Font("Serif", Font.BOLD, 26);
        titleText.setFont(font);
        title.add(titleText);
        add(title, BorderLayout.NORTH);
        
        JPanel searchTerms = new JPanel();
        searchTerms.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 20, 10, 20);
        c.anchor = GridBagConstraints.LINE_START;
        
        Dimension dim = new Dimension(100, 20);
        JTextField searchID = new JTextField(30);
        searchID.setPreferredSize(dim);
        searchID.setMinimumSize(dim);
        JTextField searchFN = new JTextField(30);
        searchFN.setPreferredSize(dim);
        searchFN.setMinimumSize(dim);
        JTextField searchLN = new JTextField(30);
        searchLN.setPreferredSize(dim);
        searchLN.setMinimumSize(dim);
        JTextField searchPO = new JTextField(30);
        searchPO.setPreferredSize(dim);
        searchPO.setMinimumSize(dim);
        JTextField searchS1 = new JTextField(30);
        searchS1.setPreferredSize(dim);
        searchS1.setMinimumSize(dim);
        JTextField searchS2 = new JTextField(30);
        searchS2.setPreferredSize(dim);
        searchS2.setMinimumSize(dim);

        JLabel searchText = new JLabel("Search");
        Font font2 = new Font("Serif", Font.BOLD, 24);
        searchText.setFont(font2);
        
        c.gridx = 0; c.gridy = 0;
        searchTerms.add(searchText, c);
        c.gridx = 0; c.gridy = 1;
        searchTerms.add(new JLabel("Employee ID"), c);
        c.gridx = 0; c.gridy = 2;
        searchTerms.add(searchID, c);
        c.gridx = 1; c.gridy = 1;
        searchTerms.add(new JLabel("Last Name:"), c);
        c.gridx = 1; c.gridy = 2;
        searchTerms.add(searchLN, c);
        c.gridx = 2; c.gridy = 1;
        searchTerms.add(new JLabel("First Name:"), c);
        c.gridx = 2; c.gridy = 2;
        searchTerms.add(searchFN, c);
        c.gridx = 3; c.gridy = 1;
        searchTerms.add(new JLabel("Position:"), c);
        c.gridx = 3; c.gridy = 2;
        searchTerms.add(searchPO, c);
        c.gridx = 4; c.gridy = 1;
        searchTerms.add(new JLabel("Min Salary:"), c);
        c.gridx = 4; c.gridy = 2;
        searchTerms.add(searchS1, c);
        c.gridx = 5; c.gridy = 1;
        searchTerms.add(new JLabel("Max Salary:"), c);
        c.gridx = 5; c.gridy = 2;
        searchTerms.add(searchS2, c);
        
        c.gridx = 0; c.gridy = 3; c.gridwidth = 3;
        JPanel buttons = new JPanel();
        JButton search = new JButton("Search");
        JButton add = new JButton("Add");
        JButton clear = new JButton("Clear");
        buttons.add(search);
        buttons.add(add);
        buttons.add(clear);
        searchTerms.add(buttons, c);
        
        c.anchor = GridBagConstraints.LINE_END;
        add(searchTerms, BorderLayout.CENTER);

        JPanel results = new JPanel();
        results.setLayout(new GridBagLayout());
        GridBagConstraints j = new GridBagConstraints();
        j.insets = new Insets(5, 5, 5, 5);
        j.anchor = GridBagConstraints.LINE_START;
        add(results, BorderLayout.SOUTH);
        
        clear.addActionListener(e -> {
            searchID.setText("");
            searchFN.setText("");
            searchLN.setText("");
            searchS1.setText("");
            searchS2.setText("");
            setSize(1000, 300);
            JPanel clearedResults = new JPanel();
            Dimension dim2 = new Dimension(1000, 0);
            clearedResults.setPreferredSize(dim2);
            add(clearedResults, BorderLayout.SOUTH);
            resultsModel.setRowCount(0);
            
        });
        
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Double minSal = new Double(0.0);
                Double maxSal = new Double(0.0);
                queryResults.clear();
                
                if (searchS1.getText().equals("")) {
                    minSal = 0.0;
                } else { minSal = Double.parseDouble(searchS1.getText()); }
                if (searchS2.getText().equals("")) {
                    maxSal = 1000000.00;
                } else { maxSal = Double.parseDouble(searchS2.getText()); }
                queryResults = searchDB(searchID.getText(), searchFN.getText(), searchLN.getText(), searchPO.getText(), minSal, maxSal);
                
                JTable resultsTable = new JTable();
                
                JPanel Results = new JPanel();
                Results.setLayout(new FlowLayout(FlowLayout.LEFT));
                
                Object[] col = {"ID","Last Name","First Name", "Address", "Position", "Salary", "Hire Date"};
                resultsModel.setRowCount(0);
                resultsModel.setColumnIdentifiers(col);
                resultsTable.getTableHeader().setBackground(Color.black);
                resultsTable.getTableHeader().setForeground(Color.blue);
                resultsTable.setModel(resultsModel);
                resultsTable.getColumnModel().getColumn(0).setPreferredWidth(10);
                resultsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
                Dimension dim = new Dimension(975, 200);
                Results.setPreferredSize(dim);
                resultsTable.setPreferredSize(dim);
                setSize(1000, 500);
                
                for (int i = 0; i < queryResults.size(); i++) {
                    DecimalFormat myFormatter = new DecimalFormat("$###,###.00");

                    Object[] objs = {queryResults.get(i).getID(), queryResults.get(i).getLastName(), queryResults.get(i).getFirstName(), queryResults.get(i).getAddress(), queryResults.get(i).getPosition(), myFormatter.format(queryResults.get(i).getSalary()), queryResults.get(i).getHireDate()};
                    resultsModel.addRow(objs);
                }
                
                JScrollPane ScrollResults = new JScrollPane(resultsTable);
                ScrollResults.setPreferredSize(dim);
                Results.add(ScrollResults);
                add(Results, BorderLayout.SOUTH);
            }
        });

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDB();
            }
        });
        
        setVisible(true);
    }
    
    public static ArrayList searchDB(String ID, String FN, String LN, String PO, Double S1, Double S2) {
        
        ID = ID + "%";
        FN = FN + "%"; 
        LN = LN + "%";
        PO = PO + "%";
        
        ArrayList<Employee> QR = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE EmployeeID LIKE '" + ID + "' AND FirstName LIKE '" + FN + "' AND LastName LIKE '" + LN + "' AND Position LIKE '" + PO + "' AND Salary BETWEEN " + S1.toString() + " AND " + S2.toString();
        
        try {Connection connection = getConnection();
              PreparedStatement ps = connection.prepareStatement(sql);
              ResultSet rs = ps.executeQuery();

              while (rs.next()) {

                  Employee employee = new Employee();
                  
                  employee.setID(rs.getString("EmployeeID"));
                  employee.setFirstName(rs.getString("FirstName"));
                  employee.setLastName(rs.getString("LastName"));
                  employee.setAddress(rs.getString("Address"));
                  employee.setPosition(rs.getString("Position"));
                  employee.setSalary(rs.getDouble("Salary"));
                  employee.setHireDate(rs.getString("HireDate"));
                  
                  QR.add(employee); 
                  
                }
        } catch (SQLException e) {
            System.err.println(e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseViewer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return QR;
    }
    
    public static void addDB() {
        JFrame addWindow = new JFrame();
        addWindow.setTitle("Add New Employee");
        addWindow.setSize(400, 350);
        addWindow.setLocationByPlatform(true);
        
        try {
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException |
                 IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.out.println(e);
        }
        
        JPanel addTitle = new JPanel();
        addTitle.setLayout(new FlowLayout(FlowLayout.LEFT));
        addTitle.setPreferredSize(new Dimension(30, 40));
        JLabel addText = new JLabel("  Add New Employee");
        Font font3 = new Font("Serif", Font.BOLD, 26);
        addText.setFont(font3);
        addTitle.add(addText);
        addWindow.add(addTitle, BorderLayout.NORTH);
        
        JPanel empInfo = new JPanel();
        empInfo.setLayout(new GridBagLayout());
        GridBagConstraints j = new GridBagConstraints();
        j.insets = new Insets(5, 5, 5, 5);
        j.anchor = GridBagConstraints.LINE_START;
        
        Dimension dim = new Dimension(100, 20);
        JTextField infoFN = new JTextField(30);
        infoFN.setPreferredSize(dim);
        infoFN.setMinimumSize(dim);
        JTextField infoLN = new JTextField(30);
        infoLN.setPreferredSize(dim);
        infoLN.setMinimumSize(dim);
        JTextField infoAdd = new JTextField(30);
        infoAdd.setPreferredSize(dim);
        infoAdd.setMinimumSize(dim);
        JTextField infoPO = new JTextField(30);
        infoPO.setPreferredSize(dim);
        infoPO.setMinimumSize(dim);
        JTextField infoS1 = new JTextField(30);
        infoS1.setPreferredSize(dim);
        infoS1.setMinimumSize(dim);
        JTextField infoDT = new JTextField("YYYY-MM-DD", 30);
        infoDT.setPreferredSize(dim);
        infoDT.setMinimumSize(dim);
        
        j.gridx = 0; j.gridy = 0;
        empInfo.add(new JLabel("Last Name: "), j);
        j.gridx = 1; j.gridy = 0;
        empInfo.add(infoLN, j);
        j.gridx = 0; j.gridy = 1;
        empInfo.add(new JLabel("First Name:"), j);
        j.gridx = 1; j.gridy = 1;
        empInfo.add(infoFN, j);
        j.gridx = 0; j.gridy = 2;
        empInfo.add(new JLabel("Address:"), j);
        j.gridx = 1; j.gridy = 2;
        empInfo.add(infoAdd, j);
        j.gridx = 0; j.gridy = 3;
        empInfo.add(new JLabel("Position:"), j);
        j.gridx = 1; j.gridy = 3;
        empInfo.add(infoPO, j);
        j.gridx = 0; j.gridy = 4;
        empInfo.add(new JLabel("Salary:"), j);
        j.gridx = 1; j.gridy = 4;
        empInfo.add(infoS1, j);
        j.gridx = 0; j.gridy = 5;
        empInfo.add(new JLabel("Hire Date:"), j);
        j.gridx = 1; j.gridy = 5;
        empInfo.add(infoDT, j);
        
        j.gridx = 0; j.gridy = 6; j.gridwidth = 2;
        JPanel addButtons = new JPanel();
        JButton newAdd = new JButton("Add");
        JButton newClear = new JButton("Clear");
        addButtons.add(newAdd);
        addButtons.add(newClear);
        empInfo.add(addButtons, j);
        
        j.anchor = GridBagConstraints.LINE_END;
        addWindow.add(empInfo, BorderLayout.CENTER);
        
        newAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
        
                try {Connection connection = getConnection();
                    PreparedStatement ps1 = connection.prepareStatement("SELECT COUNT(*) FROM employees");
                    ResultSet rs1 = ps1.executeQuery();
                    rs1.next();
                    int count = rs1.getInt(1) + 1;
                    PreparedStatement ps = connection.prepareStatement("INSERT INTO employees VALUES (" + count + ", '" + infoFN.getText() + "', '" + infoLN.getText() + "', '" + infoAdd.getText() + "', '" + infoPO.getText() + "', '" + infoS1.getText() + "', '" + infoDT.getText() + "')");
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Employee Added!");
                    addWindow.setVisible(false); //you can't see me!
                    addWindow.dispose();
                }                 
                 catch (SQLException r) {
                    System.err.println(r);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(DatabaseViewer.class.getName()).log(Level.SEVERE, null, ex);
                }}});
        
        newClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                infoFN.setText("");
                infoLN.setText("");
                infoAdd.setText("");
                infoPO.setText("");
                infoS1.setText("");
                infoDT.setText("");
            }
        });



        addWindow.setVisible(true);

    }
            
    public static void main(String[] args) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new DatabaseViewer();
            }
        });
    }
}
    
