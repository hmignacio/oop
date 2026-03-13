package com.mycompany.employeerecords;



import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;


import javax.swing.*;
import java.awt.*;
import java.time.Month;
import java.util.ArrayList;
import com.mycompany.employeerecords.model.Employee;
import java.io.FileOutputStream;

public class Salary extends JFrame {
    private Employee employee;
    private JComboBox<String> monthComboBox, yearComboBox;
    private JLabel grossPayLabel, netPayLabel, totalHoursLabel, minutesWorkedLabel, otMinutesLabel, resultLabel;
    private JTextArea benefitsArea, deductionsArea;


    public Salary(Employee employee) {
        this.employee = employee;

        setTitle("Salary Calculator");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        JLabel headerLabel = new JLabel(employee.getFirstName() + " " + employee.getLastName() + " Salary Information");
        headerLabel.setFont(new java.awt.Font("SansSerif", Font.BOLD, 18));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        headerPanel.add(headerLabel, BorderLayout.NORTH);

        //separator
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(Color.GRAY);
        separator.setPreferredSize(new Dimension(1, 3)); // Thickness: 3px
        separator.setBackground(Color.GRAY); // Ensures visibility on all LAFs

        //add left and right margins
        JPanel separatorPanel = new JPanel(new BorderLayout());
        separatorPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20)); // Top, Left, Bottom, Right padding
        separatorPanel.add(separator, BorderLayout.CENTER);

        // Add to the frame or header section
        headerPanel.add(separatorPanel, BorderLayout.SOUTH);
        
        add(headerPanel, BorderLayout.NORTH);


        // Center Panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Column 1 - Labels
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(new JLabel("Select Year:"), gbc);
        gbc.gridy++;
        centerPanel.add(new JLabel("Select Month:"), gbc);
        gbc.gridy++;
        centerPanel.add(new JLabel("Gross Pay:"), gbc);
        gbc.gridy++;
        centerPanel.add(new JLabel("Net Pay:"), gbc);
        gbc.gridy++;
        centerPanel.add(new JLabel("Total Hours Worked:"), gbc);
        gbc.gridy++;
        centerPanel.add(new JLabel("Minutes Worked:"), gbc);
        gbc.gridy++;
        centerPanel.add(new JLabel("OT Minutes Worked:"), gbc);

        // Column 2 - Fields
        gbc.gridx = 1;
        gbc.gridy = 0;
        yearComboBox = new JComboBox<>(new String[]{"2024", "2025"});
        centerPanel.add(yearComboBox, gbc);
        gbc.gridy++;
        monthComboBox = new JComboBox<>(new String[]{
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        });
        centerPanel.add(monthComboBox, gbc);
        gbc.gridy++;
        grossPayLabel = new JLabel();
        centerPanel.add(grossPayLabel, gbc);
        gbc.gridy++;
        netPayLabel = new JLabel();
        centerPanel.add(netPayLabel, gbc);
        gbc.gridy++;
        totalHoursLabel = new JLabel();
        centerPanel.add(totalHoursLabel, gbc);
        gbc.gridy++;
        minutesWorkedLabel = new JLabel();
        centerPanel.add(minutesWorkedLabel, gbc);
        gbc.gridy++;
        otMinutesLabel = new JLabel();
        centerPanel.add(otMinutesLabel, gbc);

        // Column 3 - Labels for Benefits & Deductions
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        centerPanel.add(new JLabel("Benefits:"), gbc);
        gbc.gridy = 1;
        benefitsArea = new JTextArea(6, 20);
        benefitsArea.setLineWrap(true);
        benefitsArea.setWrapStyleWord(true);
        benefitsArea.setEditable(false);
        centerPanel.add(new JScrollPane(benefitsArea), gbc);

        gbc.gridy = 3;
        centerPanel.add(new JLabel("Deductions:"), gbc);
        gbc.gridy = 4;
        deductionsArea = new JTextArea(6, 20);
        deductionsArea.setLineWrap(true);
        deductionsArea.setWrapStyleWord(true);
        deductionsArea.setEditable(false);
        centerPanel.add(new JScrollPane(deductionsArea), gbc);

        add(centerPanel, BorderLayout.CENTER);

        // Footer buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton computeButton = new JButton("Compute");
        JButton printButton = new JButton("Print Payslip");
        JButton closeButton = new JButton("Close");
        buttonPanel.add(computeButton);
        buttonPanel.add(printButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);


         //Action listeners
        computeButton.addActionListener(e -> {
            String selectedMonth = (String) monthComboBox.getSelectedItem();
            String selectedYear = (String) yearComboBox.getSelectedItem();
            int monthNumber = getMonthNumber(selectedMonth);
            int year = Integer.parseInt(selectedYear);
            
            if(year != 2024){
                JOptionPane.showMessageDialog(this, "No records found for this year.");
                return;
            }
            if (monthNumber == -1) {
                JOptionPane.showMessageDialog(this, "Please select a valid month.");
                return;
            }
            else if (monthNumber < 6) {
                JOptionPane.showMessageDialog(this, "No records found for this month.");
                return;
            }

            ArrayList<ArrayList<Double>> attendanceData = new Attendance().displayAttendance(employee.getEmployeeId(), year, monthNumber);

            double totalGross = 0.0;
            double totalNet = 0.0;
            double totalMinutes = 0.0;
            double totalOT = 0.0;

            StringBuilder benefitsText = new StringBuilder();
            StringBuilder deductionsText = new StringBuilder();

            for (int i = 0; i < attendanceData.size(); i++) {
                ArrayList<Double> weekData = attendanceData.get(i);
                if (weekData.size() >= 3) {
                    double work = weekData.get(0);
                    double late = weekData.get(1);
                    double ot   = weekData.get(2);

                    double grossPay = SalaryComputation.GrossPay(employee.getEmployeeId(), work, ot);
                    double netPay = SalaryComputation.computeDeductions(grossPay, employee.getEmployeeId(), late);

                    totalGross += grossPay;
                    totalNet += netPay;
                    totalMinutes += work;
                    totalOT += ot;
                }
            }

            
            grossPayLabel.setText(String.format("PHP %, .2f", totalGross));
            netPayLabel.setText(String.format("PHP %, .2f", totalNet));
            totalHoursLabel.setText(String.format("%.2f", totalMinutes / 60));
            minutesWorkedLabel.setText(String.format("%.0f mins", totalMinutes));
            otMinutesLabel.setText(String.format("%.0f mins", totalOT));

            benefitsText.append("• Rice Subsidy\n• Phone Allowance\n• OT Pay");
            deductionsText.append("• SSS\n• PhilHealth\n• Pag-IBIG\n• Withholding Tax\n• Late Deductions");

            benefitsArea.setText(benefitsText.toString());
            deductionsArea.setText(deductionsText.toString());
        });
        
        
        closeButton.addActionListener(e -> dispose());
        printButton.addActionListener(e -> generatePayslipPDF());
    }

    
    private int getMonthNumber(String monthName) {
        try {
            Month month = Month.valueOf(monthName.toUpperCase());
            return month.getValue(); // 1 to 12
        } catch (IllegalArgumentException e) {
            return -1; // invalid month
        }
    }
    
    private void generatePayslipPDF() {

        try {

            String fileName = "Payslip_" + employee.getEmployeeId() + ".pdf";

            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(
                    document, new FileOutputStream(fileName));

            document.open();

            // Fonts
            Font titleFont = new Font(FontFamily.HELVETICA, 18, Font.BOLD);
            Font normalFont = new Font(FontFamily.HELVETICA, 12, Font.NORMAL);
            Font headerFont = new Font(FontFamily.HELVETICA, 12, Font.BOLD);

            // Title
            com.itextpdf.text.Paragraph title =
                    new com.itextpdf.text.Paragraph("MotorPH Payroll System\nEmployee Payslip\n\n", titleFont);

            title.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            document.add(title);

            // Employee Info Table
            com.itextpdf.text.pdf.PdfPTable infoTable = new com.itextpdf.text.pdf.PdfPTable(2);
            infoTable.setWidthPercentage(100);

            infoTable.addCell(new com.itextpdf.text.Phrase("Employee Name", headerFont));
            infoTable.addCell(new com.itextpdf.text.Phrase(employee.getFirstName() + " " + employee.getLastName(), normalFont));

            infoTable.addCell(new com.itextpdf.text.Phrase("Employee ID", headerFont));
            infoTable.addCell(new com.itextpdf.text.Phrase(employee.getEmployeeId(), normalFont));

            infoTable.addCell(new com.itextpdf.text.Phrase("Month", headerFont));
            infoTable.addCell(new com.itextpdf.text.Phrase(monthComboBox.getSelectedItem().toString(), normalFont));

            infoTable.addCell(new com.itextpdf.text.Phrase("Year", headerFont));
            infoTable.addCell(new com.itextpdf.text.Phrase(yearComboBox.getSelectedItem().toString(), normalFont));

            document.add(infoTable);
            document.add(new com.itextpdf.text.Paragraph("\n"));

            // Work Summary Table
            com.itextpdf.text.pdf.PdfPTable workTable = new com.itextpdf.text.pdf.PdfPTable(2);
            workTable.setWidthPercentage(100);

            workTable.addCell(new com.itextpdf.text.Phrase("Total Hours Worked", headerFont));
            workTable.addCell(new com.itextpdf.text.Phrase(totalHoursLabel.getText(), normalFont));

            workTable.addCell(new com.itextpdf.text.Phrase("Minutes Worked", headerFont));
            workTable.addCell(new com.itextpdf.text.Phrase(minutesWorkedLabel.getText(), normalFont));

            workTable.addCell(new com.itextpdf.text.Phrase("OT Minutes", headerFont));
            workTable.addCell(new com.itextpdf.text.Phrase(otMinutesLabel.getText(), normalFont));

            document.add(workTable);
            document.add(new com.itextpdf.text.Paragraph("\n"));

            // Salary Table
            com.itextpdf.text.pdf.PdfPTable salaryTable = new com.itextpdf.text.pdf.PdfPTable(2);
            salaryTable.setWidthPercentage(100);

            salaryTable.addCell(new com.itextpdf.text.Phrase("Gross Pay", headerFont));
            salaryTable.addCell(new com.itextpdf.text.Phrase(grossPayLabel.getText(), normalFont));

            salaryTable.addCell(new com.itextpdf.text.Phrase("Net Pay", headerFont));
            salaryTable.addCell(new com.itextpdf.text.Phrase(netPayLabel.getText(), normalFont));

            document.add(salaryTable);
            document.add(new com.itextpdf.text.Paragraph("\n"));

            // Benefits & Deductions Table
            com.itextpdf.text.pdf.PdfPTable bdTable = new com.itextpdf.text.pdf.PdfPTable(2);
            bdTable.setWidthPercentage(100);

            bdTable.addCell(new com.itextpdf.text.Phrase("Benefits", headerFont));
            bdTable.addCell(new com.itextpdf.text.Phrase("Deductions", headerFont));

            bdTable.addCell(new com.itextpdf.text.Phrase(benefitsArea.getText(), normalFont));
            bdTable.addCell(new com.itextpdf.text.Phrase(deductionsArea.getText(), normalFont));

            document.add(bdTable);

            document.close();

            JOptionPane.showMessageDialog(this,
                    "Payslip saved as " + fileName);

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this,
                    "Error generating PDF: " + ex.getMessage());
        }
    }

}
