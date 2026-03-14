/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.employeerecords;


import com.mycompany.employeerecords.model.Employee;
import com.mycompany.employeerecords.service.AccessControl;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.util.Vector;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;


public class EmployeeRecords extends JFrame {
    
   
    // Create and configure JTable
    private static final EmployeeRecordParser employeeData = new EmployeeRecordParser();
    private static final AccessControl service = new AccessControl();
    private JTable employeeTable;
    private JPanel mainPanel;
    private JTextField txtSearch;
    private JButton btnView, btnAdd, btnUsers, btnSalary, btnClearSearch, btnAttendance, btnLeave;
    private Employee loggedInEmployee;
    
    

    public EmployeeRecords(String employeeNumber) {
        employeeData.loadMapOnly();
        
        if (employeeNumber != null && !employeeNumber.isBlank()) {
            loggedInEmployee = EmployeeRecordParser.employeeMap.get(employeeNumber.trim());
        }
        
        setTitle("Motor PH Employee Records");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Vector<String> columnNames = new Vector<>();
        Vector<Vector<String>> data = new Vector<>();
        
        employeeTable = new JTable();  // Initialize before use

        // Load data into columnNames and data vectors, and set it to the table
        employeeData.loadEmployeeData(columnNames, data);

        // Build model and attach it
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        employeeTable.setModel(model);
        employeeData.styleEmployeeTable(employeeTable);

        JScrollPane scrollPane = new JScrollPane(employeeTable);
         
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel headerLabel = new JLabel("MOTOR PH EMPLOYEE DATA");
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String welcomeText = "Welcome!";
        if (loggedInEmployee != null) {
            welcomeText = "Welcome, " + loggedInEmployee.getFirstName() + "!";
        }

        JLabel welcomeLabel = new JLabel(welcomeText);
        welcomeLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        headerPanel.add(headerLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(welcomeLabel);


        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        btnView = new JButton("View Employee");
        btnAdd = new JButton("Add New Employee");
        btnSalary = new JButton("View Salary");
        btnUsers = new JButton("Manage Users");
        btnAttendance = new JButton("Log Attendance");
        btnLeave = new JButton("File Leave Request");

        buttonPanel.add(btnView);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnSalary);
        buttonPanel.add(btnUsers);
        buttonPanel.add(btnAttendance);
        buttonPanel.add(btnLeave);


        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtSearch = new JTextField(20);
        btnClearSearch = new JButton("Clear");

        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(txtSearch);
        searchPanel.add(btnClearSearch);


        // Separators
        JSeparator separator1 = new JSeparator();
        separator1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));

        JSeparator separator2 = new JSeparator();
        separator2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));


        // Top Panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        topPanel.add(headerPanel);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(separator1);
        topPanel.add(buttonPanel);
        topPanel.add(separator2);
        topPanel.add(searchPanel);

        mainPanel = new JPanel(new BorderLayout(10,10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
        
        
        applyAccessControl(loggedInEmployee);
        
        
        // View button action
            btnView.addActionListener(e -> {
                int selectedRow = employeeTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(this, "Please select an employee row to view.", "No Selection", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Employee ID is in the first column (index 0)
                String empId = employeeTable.getValueAt(selectedRow, 0).toString();

                Employee employee = EmployeeRecordParser.employeeMap.get(empId);
                if (employee != null) {
                    new ViewEmployee(EmployeeRecords.this, loggedInEmployee, employee).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Employee data not found for ID: " + empId, "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            btnAdd.addActionListener(e -> {
                new AddEmployee(this).setVisible(true);
            });
            
            btnSalary.addActionListener(e -> {
                int selectedRow = employeeTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(this, "Please select an employee row to view.", "No Selection", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Employee ID is in the first column (index 0)
                String empId = employeeTable.getValueAt(selectedRow, 0).toString();

                Employee employee = EmployeeRecordParser.employeeMap.get(empId);
                if (employee != null) {
                    Salary frame = new Salary(employee);
                    frame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Employee data not found for ID: " + empId, "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            
            btnUsers.addActionListener(e -> {
                new UserManagement().setVisible(true);
            });
            
            btnAttendance.addActionListener(e -> {
                if (loggedInEmployee == null) {
                    JOptionPane.showMessageDialog(this,
                        "No employee is logged in.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                LogAttendance frame = new LogAttendance(loggedInEmployee);
                frame.setVisible(true);
            });
            
            btnLeave.addActionListener(e -> {
                LeaveRequests leaveFrame = new LeaveRequests(loggedInEmployee);
                leaveFrame.setVisible(true);
            });
            
            DefaultTableModel tableModel = (DefaultTableModel) employeeTable.getModel();
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
            employeeTable.setRowSorter(sorter);

            txtSearch.getDocument().addDocumentListener(new DocumentListener() {

                private void search() {
                    String text = txtSearch.getText();

                    if (text.trim().isEmpty()) {
                        sorter.setRowFilter(null);
                    } else {
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    }
                }

                public void insertUpdate(DocumentEvent e) { search(); }
                public void removeUpdate(DocumentEvent e) { search(); }
                public void changedUpdate(DocumentEvent e) { search(); }
            });
            
            btnClearSearch.addActionListener(e -> {
                txtSearch.setText("");
            }); 
    }


        public void reloadEmployeeTable() {

            SwingUtilities.invokeLater(() -> {

                Vector<String> columnNames = new Vector<>();
                Vector<Vector<String>> data = new Vector<>();

                employeeData.loadEmployeeData(columnNames, data);

                DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

                employeeTable.setModel(model);
                employeeTable.clearSelection();
                employeeTable.repaint();

                employeeData.styleEmployeeTable(employeeTable);
            });
        }
        
        private void applyAccessControl(Employee emp) {
            btnAdd.setVisible(AccessControl.canAddEmployee(emp));
            btnView.setVisible(AccessControl.canViewEmployees(emp));
            btnSalary.setVisible(AccessControl.canAccessSalary(emp));
            btnUsers.setVisible(AccessControl.canManageUsers(emp));
        }
        
        public JTable getEmployeeTable() {
            return employeeTable;
        }
}


