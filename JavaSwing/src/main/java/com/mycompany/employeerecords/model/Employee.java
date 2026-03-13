/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employeerecords.model;

/**
 *
 * @author Hanna
 */
public class Employee {
    private String employeeId;
    private String firstName;
    private String lastName;
    private String birthDate;

    private PersonalInfo personalInfo;
    private GovernmentBenefits benefits;
    private EmploymentDetails employment;
    private SalaryDetails salary;

    public Employee(String employeeId, String firstName, String lastName, String birthDate,
                    PersonalInfo personalInfo,
                    GovernmentBenefits benefits,
                    EmploymentDetails employment,
                    SalaryDetails salary) {

        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.personalInfo = personalInfo;
        this.benefits = benefits;
        this.employment = employment;
        this.salary = salary;
    }

    public String getEmployeeId() { return employeeId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getBirthDate() {return birthDate; }

    public PersonalInfo getPersonalInfo() { return personalInfo; }
    public GovernmentBenefits getBenefits() { return benefits; }
    public EmploymentDetails getEmployment() { return employment; }
    public SalaryDetails getSalary() { return salary; }

    @Override
    public String toString() {
        return employeeId + " - " + lastName + ", " + firstName;
    }

}
