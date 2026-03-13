/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employeerecords.model;

/**
 *
 * @author Hanna
 */
public class PersonalInfo {
    
    private String address;
    private String phone;

    public PersonalInfo(String address, String phone) {
        this.address = address;
        this.phone = phone;
    }

    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    
}
