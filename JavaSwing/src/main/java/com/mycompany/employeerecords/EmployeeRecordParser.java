/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employeerecords;

import com.mycompany.employeerecords.model.EmploymentDetails;
import com.mycompany.employeerecords.model.GovernmentBenefits;
import com.mycompany.employeerecords.model.PersonalInfo;
import com.mycompany.employeerecords.model.SalaryDetails;
import com.mycompany.employeerecords.model.Employee;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Vector;

/**
 *
 * @author Admin
 */
public class EmployeeRecordParser {
    
    
    public static Map<String, Employee> employeeMap = new LinkedHashMap<>();
    private JTable employeeTable = new JTable();
    Path csvPath = Paths.get("src/docs/MotorPH-Employee-Data.csv");
    
    public void loadEmployeeData(Vector<String> columnNames, Vector<Vector<String>> data) {

    columnNames.clear();  
    data.clear();          
    employeeMap.clear();   

    try (BufferedReader br = new BufferedReader(new FileReader(csvPath.toFile()))) {

        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setQuote('"')
                .setIgnoreSurroundingSpaces(true)
                .build();

        CSVParser csvParser = new CSVParser(br, format);

        // Table headers
        columnNames.add("Employee #");
        columnNames.add("Last Name");
        columnNames.add("First Name");
        columnNames.add("SSS");
        columnNames.add("Philhealth");
        columnNames.add("TIN");
        columnNames.add("Pag-ibig");
        
        int columnCount = columnNames.size();

        for (CSVRecord record : csvParser) {

            PersonalInfo personal = new PersonalInfo(
                    record.get("Address").trim(),
                    record.get("Phone Number").trim()
            );

            GovernmentBenefits benefits = new GovernmentBenefits(
                    record.get("SSS #").trim(),
                    record.get("Philhealth #").trim(),
                    record.get("TIN #").trim(),
                    record.get("Pag-ibig #").trim()
            );

            EmploymentDetails employment = new EmploymentDetails(
                    record.get("Status").trim(),
                    record.get("Position").trim(),
                    record.get("Immediate Supervisor").trim()
            );

            SalaryDetails salary = new SalaryDetails(
                    parseDouble(record.get("Basic Salary")),
                    parseDouble(record.get("Rice Subsidy")),
                    parseDouble(record.get("Phone Allowance")),
                    parseDouble(record.get("Clothing Allowance")),
                    parseDouble(record.get("Gross Semi-monthly Rate")),
                    parseDouble(record.get("Hourly Rate"))
            );

            Employee emp = new Employee(
                    record.get("Employee #").trim(),
                    record.get("First Name").trim(),
                    record.get("Last Name").trim(),
                    record.get("Birthday").trim(),
                    personal,
                    benefits,
                    employment,
                    salary
            );

            employeeMap.put(emp.getEmployeeId(), emp);

            Vector<String> row = new Vector<>();
            row.add(emp.getEmployeeId());
            row.add(emp.getLastName());
            row.add(emp.getFirstName());
            row.add(emp.getBenefits().getSss());
            row.add(emp.getBenefits().getPhilhealth());
            row.add(emp.getBenefits().getTin());
            row.add(emp.getBenefits().getPagIbig());
            
            
            while (row.size() < columnCount) {
                row.add("");
            }

            data.add(row);
        }

    } catch (IOException e) {
        JOptionPane.showMessageDialog(
                null,
                "Error loading CSV: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
    
        public void styleEmployeeTable(JTable table) {
        table.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 14));
        table.setRowHeight(24);
        table.setGridColor(java.awt.Color.LIGHT_GRAY);
        table.setShowGrid(true);
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? new java.awt.Color(245, 245, 245) : java.awt.Color.WHITE);
                } else {
                    c.setBackground(new java.awt.Color(184, 207, 229));
                }
                return c;
            }
        });
    }
        
   public void loadMapOnly() {

    try (BufferedReader br = new BufferedReader(new FileReader(csvPath.toFile()))) {
        
        employeeMap.clear();

        // CSVFormat with quotes and trimmed spaces
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader()                     
                .setSkipHeaderRecord(true)       
                .setQuote('"')                   
                .setIgnoreSurroundingSpaces(true)
                .build();

        CSVParser csvParser = new CSVParser(br, format);

        for (CSVRecord record : csvParser) {

            // Personal info
            PersonalInfo personal = new PersonalInfo(
                    record.get("Address").trim(),
                    record.get("Phone Number").trim()
            );

            // Government benefits
            GovernmentBenefits benefits = new GovernmentBenefits(
                    record.get("SSS #").trim(),
                    record.get("Philhealth #").trim(),
                    record.get("TIN #").trim(),
                    record.get("Pag-ibig #").trim()
            );

            // Employment details (Supervisor may contain commas)
            EmploymentDetails employment = new EmploymentDetails(
                    record.get("Status").trim(),
                    record.get("Position").trim(),
                    record.get("Immediate Supervisor").trim()
            );

            // Salary details – remove commas before parsing
            SalaryDetails salary = new SalaryDetails(
                    parseDouble(record.get("Basic Salary")),
                    parseDouble(record.get("Rice Subsidy")),
                    parseDouble(record.get("Phone Allowance")),
                    parseDouble(record.get("Clothing Allowance")),
                    parseDouble(record.get("Gross Semi-monthly Rate")),
                    parseDouble(record.get("Hourly Rate"))
            );

            // Employee object
            Employee emp = new Employee(
                    record.get("Employee #").trim(),
                    record.get("First Name").trim(),
                    record.get("Last Name").trim(),
                    record.get("Birthday").trim(),
                    personal,
                    benefits,
                    employment,
                    salary
            );

            employeeMap.put(emp.getEmployeeId(), emp);
        }

    } catch (IOException e) {
        JOptionPane.showMessageDialog(
                null,
                "Error loading employee data: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}

    /** Helper method to safely parse doubles from CSV, removing commas */
    private double parseDouble(String value) {
        if (value == null || value.isBlank()) return 0;
        return Double.parseDouble(value.replace(",", "").trim());
    }
}



