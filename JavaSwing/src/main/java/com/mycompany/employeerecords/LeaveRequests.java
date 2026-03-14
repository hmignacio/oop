/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employeerecords;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.mycompany.employeerecords.model.Employee;

public class LeaveRequests extends JFrame {

    private JTable leaveTable;
    private DefaultTableModel tableModel;
    private static final String FILE = "src/docs/Employee-Leave-Requests.csv";
    private Employee loggedInEmployee;

    public LeaveRequests(Employee loggedInEmployee) {
        this.loggedInEmployee = loggedInEmployee;

        setTitle("Employee Leave Requests");
        setSize(950, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Ensure CSV exists
        ensureCSVExists();

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10,10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // Submit leave form at the top
        mainPanel.add(createNewLeaveRequestForm(), BorderLayout.NORTH);

        // Table panel at center
        mainPanel.add(createLeaveRequestsTablePanel(), BorderLayout.CENTER);

        add(mainPanel);
    }

    /** =================== New Leave Request Form =================== **/
    private JPanel createNewLeaveRequestForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("New Leave Request"));
        form.setMaximumSize(new Dimension(600, 300)); // Small size
        form.setAlignmentX(Component.CENTER_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int y=0;

        // Employee info
        JTextField txtEmpId = new JTextField(loggedInEmployee.getEmployeeId()); txtEmpId.setEditable(false);
        JTextField txtLast = new JTextField(loggedInEmployee.getLastName()); txtLast.setEditable(false);
        JTextField txtFirst = new JTextField(loggedInEmployee.getFirstName()); txtFirst.setEditable(false);

        gbc.gridx=0; gbc.gridy=y; form.add(new JLabel("Employee #"), gbc);
        gbc.gridx=1; form.add(txtEmpId, gbc); y++;
        gbc.gridx=0; gbc.gridy=y; form.add(new JLabel("Last Name"), gbc);
        gbc.gridx=1; form.add(txtLast, gbc); y++;
        gbc.gridx=0; gbc.gridy=y; form.add(new JLabel("First Name"), gbc);
        gbc.gridx=1; form.add(txtFirst, gbc); y++;

        // Leave type
        gbc.gridx=0; gbc.gridy=y; form.add(new JLabel("Leave Type"), gbc);
        gbc.gridx=1; JComboBox<String> leaveType = new JComboBox<>(new String[]{"Vacation","Sick","Personal","Others"});
        form.add(leaveType, gbc); y++;

        // Start & End dates
        gbc.gridx=0; gbc.gridy=y; form.add(new JLabel("Start Date (MM/dd/yyyy)"), gbc);
        gbc.gridx=1; JTextField txtStart = new JTextField(); form.add(txtStart, gbc); y++;
        gbc.gridx=0; gbc.gridy=y; form.add(new JLabel("End Date (MM/dd/yyyy)"), gbc);
        gbc.gridx=1; JTextField txtEnd = new JTextField(); form.add(txtEnd, gbc); y++;

        // Reason
        gbc.gridx=0; gbc.gridy=y; form.add(new JLabel("Reason"), gbc);
        gbc.gridx=1; JTextField txtReason = new JTextField(); form.add(txtReason, gbc); y++;

        // Submit button
        JButton btnSubmit = new JButton("Submit Leave Request");
        btnSubmit.setPreferredSize(new Dimension(180, 25)); 
        btnSubmit.setFont(new Font("SansSerif", Font.PLAIN, 12)); 
        gbc.gridx=0; gbc.gridy=y; gbc.gridwidth=2; gbc.anchor = GridBagConstraints.CENTER;
        form.add(btnSubmit, gbc);

        btnSubmit.addActionListener(e -> {
            if (txtStart.getText().isBlank() || txtEnd.getText().isBlank() || txtReason.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }

            if (hasDuplicateLeave(txtStart.getText(), txtEnd.getText())) {
                JOptionPane.showMessageDialog(this, "You already have a leave request overlapping these dates.");
                return;
            }

            appendLeaveRequestToCSV(
                    txtEmpId.getText(),
                    txtLast.getText(),
                    txtFirst.getText(),
                    leaveType.getSelectedItem().toString(),
                    txtStart.getText(),
                    txtEnd.getText(),
                    txtReason.getText(),
                    "Pending"
            );

            JOptionPane.showMessageDialog(this,"Leave request submitted!");
            txtStart.setText(""); txtEnd.setText(""); txtReason.setText("");

            // Reload table for employee
            if (tableModel != null) loadLeaveRequests();
        });

        return form;
    }

    private JPanel createLeaveRequestsTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(5,5));

        String[] columns = {"Employee #","Last Name","First Name","Leave Type","Start Date","End Date","Reason","Status"};
        tableModel = new DefaultTableModel(columns,0) {
            @Override
            public boolean isCellEditable(int row,int col) { return false; }
        };
        leaveTable = new JTable(tableModel);
        leaveTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(leaveTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Only HR sees approve/reject buttons
        if (loggedInEmployee.getEmployment().getPosition().contains("HR")) {
            JPanel btnPanel = new JPanel();
            JButton btnApprove = new JButton("Approve");
            JButton btnReject = new JButton("Reject");
            btnPanel.add(btnApprove); btnPanel.add(btnReject);
            panel.add(btnPanel, BorderLayout.SOUTH);

            btnApprove.addActionListener(e -> updateSelectedLeaveStatus("Approved"));
            btnReject.addActionListener(e -> updateSelectedLeaveStatus("Rejected"));
        }

        loadLeaveRequests();
        return panel;
    }

    private void ensureCSVExists() {
        File file = new File(FILE);
        if (!file.exists()) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write("Employee #,Last Name,First Name,Leave Type,Start Date,End Date,Reason,Status");
                bw.newLine();
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    private void loadLeaveRequests() {
        tableModel.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line; boolean firstLine = true;
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM/dd/yyyy");

            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                if (firstLine) { firstLine=false; continue; }
                String[] parts = line.split(",", -1);
                if (parts.length<8) continue;

                // Filter: show only own requests if not HR
                if (!loggedInEmployee.getEmployment().getPosition().contains("HR") &&
                        !parts[0].equals(loggedInEmployee.getEmployeeId())) continue;

                tableModel.addRow(parts);
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void appendLeaveRequestToCSV(String empId,String last,String first,String type,
                                         String start,String end,String reason,String status) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE,true))) {
            bw.newLine();
            bw.write(String.join(",",empId,last,first,type,start,end,reason,status));
            bw.newLine();
        } catch (IOException e) { e.printStackTrace(); }
    }

    private boolean hasDuplicateLeave(String start,String end) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            String line; DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate newStart = LocalDate.parse(start,fmt);
            LocalDate newEnd = LocalDate.parse(end,fmt);

            while ((line = br.readLine()) != null) {
                if (line.isBlank() || line.startsWith("Employee #")) continue;
                String[] parts = line.split(",", -1);
                if (parts.length<8) continue;
                if (!parts[0].equals(loggedInEmployee.getEmployeeId())) continue;

                LocalDate existStart = LocalDate.parse(parts[4], fmt);
                LocalDate existEnd = LocalDate.parse(parts[5], fmt);

                if (!(newEnd.isBefore(existStart) || newStart.isAfter(existEnd))) return true;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    private void updateSelectedLeaveStatus(String status) {
        int row = leaveTable.getSelectedRow();
        if (row==-1) { JOptionPane.showMessageDialog(this,"Select a leave request first."); return; }

        String empId = tableModel.getValueAt(row,0).toString();
        String startDate = tableModel.getValueAt(row,4).toString();

        try {
            File inputFile = new File(FILE);
            File tempFile = new File(FILE+".tmp");

            try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
                 BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

                String line; boolean firstLine=true;
                while ((line=br.readLine())!=null) {
                    if (firstLine) { bw.write(line); bw.newLine(); firstLine=false; continue; }
                    if (line.isBlank()) { bw.newLine(); continue; }

                    String[] parts = line.split(",", -1);
                    if (parts.length<8) { bw.write(line); bw.newLine(); continue; }

                    if (parts[0].equals(empId) && parts[4].equals(startDate)) {
                        parts[7] = status;
                        line = String.join(",", parts);
                    }

                    bw.write(line); bw.newLine();
                }
            }

            inputFile.delete();
            tempFile.renameTo(inputFile);
            loadLeaveRequests();
        } catch (IOException e) { e.printStackTrace(); }
    }
}