/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employeerecords;

/**
 *
 * @author Admin
 */
import com.mycompany.employeerecords.model.EmploymentDetails;
import com.mycompany.employeerecords.model.GovernmentBenefits;
import com.mycompany.employeerecords.model.PersonalInfo;
import com.mycompany.employeerecords.model.SalaryDetails;
import com.mycompany.employeerecords.model.Employee;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import com.mycompany.employeerecords.service.EmployeeService;

public class AddEmployee extends JFrame {
    
    private EmployeeRecords parent;
    
    private JPanel contentPanel;

    private JTextField txtEmployeeId, txtLastName, txtFirstName, txtBirthDate, txtAddress, txtPhone,
            txtSSS, txtPhilhealth, txtTIN, txtPagIbig, txtStatus, txtPosition, txtSupervisor,
            txtSalary, txtRiceSubsidy, txtPhoneAllowance, txtClothingAllowance,
            txtGrossRate, txtHourlyRate;

    public AddEmployee(EmployeeRecords parent) {
        setTitle("Add New Employee");
        setSize(800, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header label
        JLabel headerLabel = new JLabel("Employee Details");
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JSeparator separator = new JSeparator();
        separator.setForeground(Color.BLACK);
        separator.setMaximumSize(new Dimension(600, 2));
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);


        JPanel separatorPanel = new JPanel(new BorderLayout());
        separatorPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20)); 
        separatorPanel.add(separator, BorderLayout.CENTER);

        
        // Top panel with vertical BoxLayout
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(headerLabel);
        topPanel.add(separatorPanel);

        add(topPanel, BorderLayout.NORTH);
        
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        

        String[] labels = {
            "Employee ID", "Last Name", "First Name", "Birth Date", "Address", "Phone Number",
            "SSS Number", "PhilHealth Number", "TIN", "Pag-IBIG Number", "Status", "Position",
            "Supervisor", "Salary", "Rice Subsidy", "Phone Allowance", "Clothing Allowance",
            "Gross Rate", "Hourly Rate"
        };

        JTextField[] textFields = new JTextField[labels.length];

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            panel.add(new JLabel(labels[i] + ":"), gbc);

            gbc.gridx = 1;
            textFields[i] = new JTextField(20);
            panel.add(textFields[i], gbc);
        }

        // Assign to class fields for access
        txtEmployeeId = textFields[0];
        txtLastName = textFields[1];
        txtFirstName = textFields[2];
        txtBirthDate = textFields[3];
        txtAddress = textFields[4];
        txtPhone = textFields[5];
        txtSSS = textFields[6];
        txtPhilhealth = textFields[7];
        txtTIN = textFields[8];
        txtPagIbig = textFields[9];
        txtStatus = textFields[10];
        txtPosition = textFields[11];
        txtSupervisor = textFields[12];
        txtSalary = textFields[13];
        txtRiceSubsidy = textFields[14];
        txtPhoneAllowance = textFields[15];
        txtClothingAllowance = textFields[16];
        txtGrossRate = textFields[17];
        txtHourlyRate = textFields[18];

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");

        buttonsPanel.add(btnSave);
        buttonsPanel.add(btnCancel);

        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(buttonsPanel, gbc);

        add(panel);

        // Button actions
        btnCancel.addActionListener(e -> dispose());

            btnSave.addActionListener(e -> {
            try {
                // Strings
                String firstName = txtFirstName.getText().trim();
                String lastName  = txtLastName.getText().trim();
                String employeeId = txtEmployeeId.getText().trim();
                String birthDate = txtBirthDate.getText().trim();
                String address = txtAddress.getText().trim();
                String phone = txtPhone.getText().trim();
                String sss = txtSSS.getText().trim();
                String philhealth = txtPhilhealth.getText().trim();
                String tin = txtTIN.getText().trim();
                String pagIbig = txtPagIbig.getText().trim();
                String status = txtStatus.getText().trim();
                String position = txtPosition.getText().trim();
                String supervisor = txtSupervisor.getText().trim();

                // Doubles (numeric fields only)
                double salaryVal = Double.parseDouble(txtSalary.getText().trim());
                double riceSubsidyVal = Double.parseDouble(txtRiceSubsidy.getText().trim());
                double phoneAllowanceVal = Double.parseDouble(txtPhoneAllowance.getText().trim());
                double clothingAllowanceVal = Double.parseDouble(txtClothingAllowance.getText().trim());
                double grossRateVal = Double.parseDouble(txtGrossRate.getText().trim());
                double hourlyRateVal = Double.parseDouble(txtHourlyRate.getText().trim());

                // Build nested objects
                PersonalInfo personalInfo = new PersonalInfo(address, phone);
                GovernmentBenefits benefits = new GovernmentBenefits(sss, philhealth, tin, pagIbig);
                EmploymentDetails employment = new EmploymentDetails(status, position, supervisor);
                SalaryDetails salary = new SalaryDetails(
                        salaryVal, riceSubsidyVal, phoneAllowanceVal,
                        clothingAllowanceVal, grossRateVal, hourlyRateVal
                );

                // Build Employee
                Employee emp = new Employee(
                        employeeId, firstName, lastName, birthDate,
                        personalInfo, benefits, employment, salary
                );

                // Save via service
                EmployeeService.saveEmployee(emp);

                JOptionPane.showMessageDialog(this, "Employee saved successfully!");
                if (parent != null) parent.reloadEmployeeTable();
                dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric values for salary and allowance fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to save employee data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
    }
}
