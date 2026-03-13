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
import java.util.Vector;

/**
 *
 * @author Admin
 */
public class EmployeeRecordParser extends JFrame{
    
    
    public static Map<String, Employee> employeeMap = new HashMap<>();
    private JTable employeeTable = new JTable();
    Path csvPath = Paths.get("src/docs/MotorPH-Employee-Data.csv");
    
    public void loadEmployeeData(Vector<String> columnNames, Vector<Vector<String>> data) {

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath.toFile()))) {

            CSVFormat format = CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .build();

            CSVParser csvParser = new CSVParser(br, format);

            String[] headersToShow = {
                "Employee #", "Last Name", "First Name",
                "SSS #", "Philhealth #", "TIN #", "Pag-ibig #"
            };

            for (String col : headersToShow) {
                columnNames.add(col.replace("#", "").trim());
            }

            for (CSVRecord record : csvParser) {

                PersonalInfo personal = new PersonalInfo(
                        record.get("Address"),
                        record.get("Phone Number")
                );

                GovernmentBenefits benefits = new GovernmentBenefits(
                        record.get("SSS #"),
                        record.get("Philhealth #"),
                        record.get("TIN #"),
                        record.get("Pag-ibig #")
                );

                EmploymentDetails employment = new EmploymentDetails(
                        record.get("Status"),
                        record.get("Position"),
                        record.get("Immediate Supervisor")
                );

                SalaryDetails salary = new SalaryDetails(
                    Double.parseDouble(record.get("Basic Salary").replace(",", "").trim()),
                    Double.parseDouble(record.get("Rice Subsidy").replace(",", "").trim()),
                    Double.parseDouble(record.get("Phone Allowance").replace(",", "").trim()),
                    Double.parseDouble(record.get("Clothing Allowance").replace(",", "").trim()),
                    Double.parseDouble(record.get("Gross Semi-monthly Rate").replace(",", "").trim()),
                    Double.parseDouble(record.get("Hourly Rate").replace(",", "").trim())
                );

                Employee emp = new Employee(
                        record.get("Employee #"),
                        record.get("First Name"),
                        record.get("Last Name"),
                        record.get("Birthday"),
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

                data.add(row);
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error loading CSV: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    

    /*
    * Loads employee data from an external CSV source.
    * Parser and Default Builder from Apache Commons CSV API
    */
//        public void loadEmployeeData(Vector<String> columnNames, Vector<Vector<String>> data) {
//        try (BufferedReader br = new BufferedReader(new FileReader(csvPath.toFile()))) {
//            CSVFormat format = CSVFormat.DEFAULT.builder()
//                .setHeader()
//                .setSkipHeaderRecord(true)
//                .build();
//
//            CSVParser csvParser = new CSVParser(br, format);
//
//            String[] headersToShow = {
//                "Employee #", "Last Name", "First Name",
//                "SSS #", "Philhealth #", "TIN #", "Pag-ibig #"
//            };
//
//            for (String col : headersToShow) {
//                columnNames.add(col.replace("#", "").trim());
//            }
//
//            for (CSVRecord record : csvParser) {
//                Employee emp = new Employee(
//                    record.get("Employee #"),
//                    record.get("Last Name"),
//                    record.get("First Name"),
//                    record.get("Birthday"),
//                    record.get("Address"),
//                    record.get("Phone Number"),
//                    record.get("SSS #"),
//                    record.get("Philhealth #"),
//                    record.get("TIN #"),
//                    record.get("Pag-ibig #"),
//                    record.get("Status"),
//                    record.get("Position"),
//                    record.get("Immediate Supervisor"),
//                    record.get("Basic Salary"),
//                    record.get("Rice Subsidy"),
//                    record.get("Phone Allowance"),
//                    record.get("Clothing Allowance"),
//                    record.get("Gross Semi-monthly Rate"),
//                    record.get("Hourly Rate")
//                );
//
//                employeeMap.put(emp.getEmployeeId(), emp);
//
//                Vector<String> row = new Vector<>();
//                row.add(emp.getEmployeeId());
//                row.add(emp.getLastName());
//                row.add(emp.getFirstName());
//                row.add(emp.getSss());
//                row.add(emp.getPhilhealth());
//                row.add(emp.getTin());
//                row.add(emp.getPagIbig());
//
//                data.add(row);
//            }
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(null, "Error loading CSV: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }   
  
    
    
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

            CSVFormat format = CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .build();

            CSVParser csvParser = new CSVParser(br, format);

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
                    Double.parseDouble(record.get("Basic Salary").replace(",", "").trim()),
                    Double.parseDouble(record.get("Rice Subsidy").replace(",", "").trim()),
                    Double.parseDouble(record.get("Phone Allowance").replace(",", "").trim()),
                    Double.parseDouble(record.get("Clothing Allowance").replace(",", "").trim()),
                    Double.parseDouble(record.get("Gross Semi-monthly Rate").replace(",", "").trim()),
                    Double.parseDouble(record.get("Hourly Rate").replace(",", "").trim())
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
}
        
//        public void loadMapOnly() {
//        try (BufferedReader br = new BufferedReader(new FileReader(csvPath.toFile()))) {
//            CSVFormat format = CSVFormat.DEFAULT.builder()
//                    .setHeader()
//                    .setSkipHeaderRecord(true)
//                    .build();
//
//            CSVParser csvParser = new CSVParser(br, format);
//
//            for (CSVRecord record : csvParser) {
//                Employee emp = new Employee(
//                        record.get("Employee #").trim(),
//                        record.get("Last Name").trim(),
//                        record.get("First Name").trim(),
//                        record.get("Birthday"),
//                        record.get("Address"),
//                        record.get("Phone Number"),
//                        record.get("SSS #"),
//                        record.get("Philhealth #"),
//                        record.get("TIN #"),
//                        record.get("Pag-ibig #"),
//                        record.get("Status"),
//                        record.get("Position"),
//                        record.get("Immediate Supervisor"),
//                        record.get("Basic Salary"),
//                        record.get("Rice Subsidy"),
//                        record.get("Phone Allowance"),
//                        record.get("Clothing Allowance"),
//                        record.get("Gross Semi-monthly Rate"),
//                        record.get("Hourly Rate")
//                );
//
//                employeeMap.put(emp.getEmployeeId(), emp);
//            }
//
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(null, "Error loading employee data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//        
//}


