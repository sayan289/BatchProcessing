package com.springbatch.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="customer_info")
public class Customer {
    @Id
    private int id;
    private String firstname;
    private String lastname;
    private String email;
    private String gender;
    private String contactNo;
    private String country;
    private String dob;
}
