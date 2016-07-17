package nl.gridshore.employees;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

public class Employee {
    private String name;
    private String email;
    @JsonProperty(value = "phone_number")
    private String phoneNumber;
    private String[] specialties;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String[] getSpecialties() {
        return specialties;
    }

    public void setSpecialties(String[] specialties) {
        this.specialties = specialties;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", specialties=" + Arrays.toString(specialties) +
                '}';
    }
}
