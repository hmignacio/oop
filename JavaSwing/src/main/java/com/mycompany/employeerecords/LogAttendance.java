/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employeerecords;

import com.mycompany.employeerecords.model.Employee;
import com.mycompany.employeerecords.service.AttendanceUtils;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LogAttendance extends JFrame {

    private JTextField txtEmpId;
    private JTextField txtLastName;
    private JTextField txtFirstName;
    private JTextField txtDate;
    private JTextField txtLogIn;
    private JTextField txtLogOut;

    public LogAttendance(Employee emp) {

        setTitle("Attendance Record");
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // padding around edges
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 8, 8, 8); // spacing between components
        gbc.anchor = GridBagConstraints.WEST;

        // Labels - column 0
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Employee #"), gbc);

        gbc.gridy++;
        panel.add(new JLabel("Last Name"), gbc);

        gbc.gridy++;
        panel.add(new JLabel("First Name"), gbc);

        gbc.gridy++;
        panel.add(new JLabel("Date"), gbc);

        gbc.gridy++;
        panel.add(new JLabel("Log In"), gbc);

        gbc.gridy++;
        panel.add(new JLabel("Log Out"), gbc);

        // Fields - column 1
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        txtEmpId = new JTextField(emp.getEmployeeId());
        txtEmpId.setEditable(false);
        panel.add(txtEmpId, gbc);

        gbc.gridy++;
        txtLastName = new JTextField(emp.getLastName());
        txtLastName.setEditable(false);
        panel.add(txtLastName, gbc);

        gbc.gridy++;
        txtFirstName = new JTextField(emp.getFirstName());
        txtFirstName.setEditable(false);
        panel.add(txtFirstName, gbc);

        gbc.gridy++;
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        txtDate = new JTextField(today);
        txtDate.setEditable(false);
        panel.add(txtDate, gbc);

        gbc.gridy++;
        txtLogIn = new JTextField();
        panel.add(txtLogIn, gbc);

        gbc.gridy++;
        txtLogOut = new JTextField();
        panel.add(txtLogOut, gbc);

        // Save Button - spans two columns centered
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton btnSave = new JButton("Save Attendance");
        panel.add(btnSave, gbc);

        add(panel);

        btnSave.addActionListener(e -> saveAttendance(emp));

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
    }

    private void saveAttendance(Employee loggedInEmployee) {
        
        
   
        if(txtLogIn.getText().isBlank() || txtLogOut.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Please enter both Log In and Log Out times.");
            return;
        }

        if (AttendanceUtils.hasAttendanceToday(loggedInEmployee)) {
            JOptionPane.showMessageDialog(this, 
                "Attendance for today is already recorded for this employee.",
                "Duplicate Entry",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (FileWriter fw = new FileWriter("src/docs/MotorPH-Employee-Data-Attendance-Record.csv", true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            
            bw.newLine();

            String line = txtEmpId.getText() + "," +
                          txtLastName.getText() + "," +
                          txtFirstName.getText() + "," +
                          txtDate.getText() + "," +
                          txtLogIn.getText() + "," +
                          txtLogOut.getText();

            bw.write(line);
            bw.newLine(); 

            JOptionPane.showMessageDialog(this,"Attendance saved.");

            dispose();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
