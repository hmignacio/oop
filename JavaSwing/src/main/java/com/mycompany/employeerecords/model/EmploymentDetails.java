/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employeerecords.model;

/**
 *
 * @author Hanna
 */
public class EmploymentDetails {
    
    private String status;
    private String position;
    private String supervisor;

    public EmploymentDetails(String status, String position, String supervisor) {
        this.status = status;
        this.position = position;
        this.supervisor = supervisor;
    }

    public String getStatus() { return status; }
    public String getPosition() { return position; }
    public String getSupervisor() { return supervisor; }
    
}
