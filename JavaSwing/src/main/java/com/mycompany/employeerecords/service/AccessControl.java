/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employeerecords.service;

import com.mycompany.employeerecords.model.Employee;

/**
 *
 * @author Hanna
 */
public class AccessControl {
    

        private static String getRole(Employee emp) {
            if (emp == null || emp.getEmployment() == null) return "";
            return emp.getEmployment().getPosition().toUpperCase();
        }

        public static boolean canAddEmployee(Employee emp) {
            String role = getRole(emp);

            switch (role) {
                case "CHIEF EXECUTIVE OFFICER":
                case "CHIEF OPERATING OFFICER":
                case "CHIEF FINANCE OFFICER":
                case "HR MANAGER":
                    return true;
                default:
                    return false;
            }
        }

        public static boolean canViewEmployees(Employee emp) {
            String role = getRole(emp);

            switch (role) {
                default:
                    return true;
            }
        }

        public static boolean canAccessSalary(Employee emp) {
            String role = getRole(emp);

            switch (role) {
                case "CHIEF EXECUTIVE OFFICER":
                case "CHIEF OPERATING OFFICER":
                case "CHIEF FINANCE OFFICER":
                case "PAYROLL MANAGER":
                case "ACCOUNTING HEAD":
                    return true;
                default:
                    return false;
            }
        }

        public static boolean canManageUsers(Employee emp) {
            String role = getRole(emp);

            switch (role) {
                case "CHIEF EXECUTIVE OFFICER":
                case "CHIEF OPERATING OFFICER":
                    return true;
                default:
                    return false;
         }
     }
        
        public static boolean canEditEmployee(Employee emp){
            String role = getRole(emp);

            switch (role) {
                case "CHIEF EXECUTIVE OFFICER":
                case "CHIEF OPERATING OFFICER":
                case "HR MANAGER":
                    return true;
                default:
                    return false;
         }
     }
        public static boolean canDeleteEmployee(Employee emp){
            String role = getRole(emp);
            
            switch (role) {
                case "CHIEF EXECUTIVE OFFICER":
                case "CHIEF OPERATING OFFICER":
                case "HR MANAGER":
                    return true;
                default:
                    return false;
         }
     }
}
