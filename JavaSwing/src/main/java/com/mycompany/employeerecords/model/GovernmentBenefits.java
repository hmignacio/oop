/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.employeerecords.model;

/**
 *
 * @author Hanna
 */
public class GovernmentBenefits {
    private String sss;
    private String philhealth;
    private String tin;
    private String pagIbig;

    public GovernmentBenefits(String sss, String philhealth, String tin, String pagIbig) {
        this.sss = sss;
        this.philhealth = philhealth;
        this.tin = tin;
        this.pagIbig = pagIbig;
    }

    public String getSss() { return sss; }
    public String getPhilhealth() { return philhealth; }
    public String getTin() { return tin; }
    public String getPagIbig() { return pagIbig; }
}
