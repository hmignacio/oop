/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employeerecords.service;

/**
 *
 * @author Hanna
 */
import java.io.*;
import java.util.Objects;
import com.mycompany.employeerecords.model.Employee;

public class EmployeeService {

    private static final String FILE_PATH = "src/docs/MotorPH-Employee-Data.csv";

    /** Save a new employee to CSV */
    public static void saveEmployee(Employee emp) throws IOException {
        // Validate required fields
        if (emp.getEmployeeId().isBlank() || emp.getFirstName().isBlank() || emp.getLastName().isBlank()) {
            throw new IllegalArgumentException("Employee ID, First Name, and Last Name are required.");
        }

        File file = new File(FILE_PATH);
        boolean fileExists = file.exists();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            if (!fileExists) {
                // Write CSV header
                writer.write(
                        "EmployeeID,FirstName,LastName,BirthDate," +
                        "Address,Phone," +
                        "SSS,Philhealth,TIN,PagIbig," +
                        "Status,Position,Supervisor," +
                        "Salary,RiceSubsidy,PhoneAllowance,ClothingAllowance,GrossRate,HourlyRate"
                );
                writer.newLine();
            }

            // Flatten employee object into CSV row
            String[] csvRow = new String[] {
                    emp.getEmployeeId(),
                    emp.getFirstName(),
                    emp.getLastName(),
                    emp.getBirthDate(),
                    emp.getPersonalInfo().getAddress(),
                    emp.getPersonalInfo().getPhone(),
                    emp.getBenefits().getSss(),
                    emp.getBenefits().getPhilhealth(),
                    emp.getBenefits().getTin(),
                    emp.getBenefits().getPagIbig(),
                    emp.getEmployment().getStatus(),
                    emp.getEmployment().getPosition(),
                    emp.getEmployment().getSupervisor(),
                    String.valueOf(emp.getSalary().getSalary()),            
                    String.valueOf(emp.getSalary().getRiceSubsidy()),       
                    String.valueOf(emp.getSalary().getPhoneAllowance()),    
                    String.valueOf(emp.getSalary().getClothingAllowance()),
                    String.valueOf(emp.getSalary().getGrossRate()),         
                    String.valueOf(emp.getSalary().getHourlyRate()) 
            };

            writer.write(String.join(",", csvRow));
            writer.newLine();
        }
    }
}
