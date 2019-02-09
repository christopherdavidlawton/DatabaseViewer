/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseviewer;

import java.sql.Date;

public class Employee {
    private String ID;
    private String FirstName;
    private String LastName;
    private String Address;
    private String Position;
    private double Salary;
    private String HireDate;
    
    public String getID() {
        return ID;
    }
    
    public void setID(String ID) {
        this.ID = ID;
    }
    
    public String getFirstName() {
        return FirstName;
    }
    
    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    public String getLastName() {
        return LastName;
    }
    
    public void setLastName(String LastName) {
        this.LastName = LastName;
    }
    
    public String getAddress() {
        return Address;
    }
    
    public void setAddress(String Address) {
        this.Address = Address;
    }
    
    public String getPosition() {
        return Position;
    }
    
    public void setPosition(String Position) {
        this.Position = Position;
    }
    
    public double getSalary() {
        return Salary;
    }
    
    public void setSalary(double Salary) {
        this.Salary = Salary;
    }
    
    public String getHireDate() {
        return HireDate;
    }
    
    public void setHireDate(String HireDate) {
        this.HireDate = HireDate;
    }
}
