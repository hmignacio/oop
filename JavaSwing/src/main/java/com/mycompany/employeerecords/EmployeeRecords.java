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


public class EmployeeRecords extends JFrame {
    
   
    // Create and configure JTable
    private static final EmployeeRecordParser employeeData = new EmployeeRecordParser();
    private static final AccessControl service = new AccessControl();
    private JTable employeeTable;
    private JPanel mainPanel;
    private JButton btnView, btnAdd, btnUsers, btnSalary;
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
         
        // Create the header label
        JLabel headerLabel = new JLabel("MOTOR PH EMPLOYEE DATA");
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT); 
        headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
        
        // Welcome label
        String welcomeText = "Welcome!";
        if (loggedInEmployee != null) {
            welcomeText = "Welcome, " + loggedInEmployee.getFirstName() + "!";
        }
        JLabel welcomeLabel = new JLabel(welcomeText);
        welcomeLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        welcomeLabel.setForeground(Color.BLACK); 
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Create buttons
        btnView = new JButton("Display Employee");
        btnAdd = new JButton("New Employee");
        btnSalary = new JButton("View Salary");
        btnUsers = new JButton("Manage Users");
        

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);  // align left in BoxLayout
        buttonPanel.add(btnView);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnSalary);
        buttonPanel.add(btnUsers);
        
        // Separator line
        JSeparator separator = new JSeparator();
        separator.setForeground(Color.BLACK);
        separator.setAlignmentX(Component.LEFT_ALIGNMENT); // align left in BoxLayout
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2)); // make it full width & 2px height
        
        JSeparator separatorTwo = new JSeparator();
        separatorTwo.setForeground(Color.BLACK);
        separatorTwo.setAlignmentX(Component.LEFT_ALIGNMENT); // align left in BoxLayout
        separatorTwo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2)); // make it full width & 2px height

        // Main top panel with vertical BoxLayout to stack label, separator, buttons vertically
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(headerLabel);
        topPanel.add(welcomeLabel);
        topPanel.add(separator);
        topPanel.add(buttonPanel);
        topPanel.add(separatorTwo);

        // Add the topPanel to your frame
        add(topPanel, BorderLayout.NORTH);
       
        // Main panel layout: topPanel north, table center
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Add main panel to frame
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
    }


        public void reloadEmployeeTable() {
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
        employeeData.styleEmployeeTable(employeeTable);  // re-style
    }
        
        private void applyAccessControl(Employee emp) {
            btnAdd.setVisible(AccessControl.canAddEmployee(emp));
            btnView.setVisible(AccessControl.canViewEmployees(emp));
            btnSalary.setVisible(AccessControl.canAccessSalary(emp));
            btnUsers.setVisible(AccessControl.canManageUsers(emp));
        }
}


