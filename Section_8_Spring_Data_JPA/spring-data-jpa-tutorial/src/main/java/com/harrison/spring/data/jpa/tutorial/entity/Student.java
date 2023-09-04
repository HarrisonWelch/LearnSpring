package com.harrison.spring.data.jpa.tutorial.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // Now our changes will reflect in the DB
@Data // All getter and setter are generated
@AllArgsConstructor
@NoArgsConstructor
@Builder // Builder pattern
@Table(
        name = "tbl_student",
        uniqueConstraints = @UniqueConstraint(
                name = "emailid_unique",
                columnNames = "email_address"
        )
)
public class Student {

    @Id // Primary key
    @SequenceGenerator(
            name = "student_sequence",
            sequenceName = "student_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "student_sequence"
    )
    private long studentId;
    private String firstName;
    private String lastName;

    @Column(
            name = "email_address", // Change default name from email_id to email_address
            nullable = false
    )
    private String emailId;

    @Embedded // Put all attrs from Guardian into this class's produced table
    private Guardian guardian;
}
