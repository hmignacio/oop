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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.mycompany.employeerecords.model.Employee;

public class AttendanceUtils {

    private static final String FILE = "src/docs/MotorPH-Employee-Data-Attendance-Record.csv";

        public static boolean hasAttendanceToday(Employee emp) {
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

            try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // Skip header
                    if (line.isBlank() || line.startsWith("Employee #")) continue;

                    String[] parts = line.split(",");
                    String empId = parts[0].trim();
                    String date = parts[3].trim();

                    if (empId.equals(emp.getEmployeeId()) && date.equals(today)) {
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false; // No record found
        }
    }
