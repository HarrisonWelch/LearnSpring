package com.harrison.Springboot.tutorial.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data // From Lombok
//@Getter
//@Setter // Can also use these for just the pieces you want
@NoArgsConstructor // Default constructor added
@AllArgsConstructor // All fields used constructor
@Builder // Add a builder pattern
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long departmentID;

    @NotBlank(message = "Please Add Department Name") // Error message given if blank
    private String departmentName;
    private String departmentAddress;
    private String departmentCode;


}
