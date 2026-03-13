/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employeerecords.model;

/**
 *
 * @author Hanna
 */
public class SalaryDetails {
    private double salary;
    private double riceSubsidy;
    private double phoneAllowance;
    private double clothingAllowance;
    private double grossRate;
    private double hourlyRate;

    public SalaryDetails(double salary, double riceSubsidy, double phoneAllowance,
                         double clothingAllowance, double grossRate, double hourlyRate) {
        this.salary = salary;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
        this.grossRate = grossRate;
        this.hourlyRate = hourlyRate;
    }

    public double getSalary() { return salary; }
    public double getHourlyRate() { return hourlyRate; }
    public double getRiceSubsidy() { return riceSubsidy; }
    public double getPhoneAllowance() { return phoneAllowance; }
    public double getClothingAllowance() { return clothingAllowance; }
    public double getGrossRate() { return grossRate; }


}
