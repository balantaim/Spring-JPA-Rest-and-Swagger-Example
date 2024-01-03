package com.martinatanasov.restapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "Employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "first_name")
    //Validation: required value from 3 to 20
    @NotNull
    @Size(min = 3, max = 20, message = "Request between 3 and 20 characters for first name")
    private String firstName;

    @Column(name = "last_name")
    //Validation: not required value from 3 to 20
    @Size(min = 3, max = 20, message = "Request between 3 and 20 characters for last name")
    private String lastName;

    @Column(name = "email")
    //Validation: request value for valid email
    @NotNull
    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Invalid email")
    private String email;

    //Empty constructor required for JPA
    public Employee(){}

    //Generate constructor
    public Employee(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    //Generate getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //Generate toString method
    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
