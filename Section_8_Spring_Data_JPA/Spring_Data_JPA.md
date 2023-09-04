# Spring_Data_JPA

## Introduction

Deep dive into Spring Data JPA
* How to map a class to a particular DB table
* How to make a DB relationship, 1:1, 1:m, m:m
* What is JPA and why we need it.
* Different annotations, native query, JPA query
* Embedded classes in entities
* Unidirection and bi-directional releationships
* Match contstraints on our entity to the DB one

JDBC
* API provided by Java
* Java objects vs DB tables 
  * How to match together?
* Ideally we are not doing conversion. We need a system.

ORM
* Object Relationship Mapping
* Define an object as a representative of the table
* Hibernate has it's own impl of ORM
* Ibatis also helps here
* JPA is just a sepecification, need to use a framework with an ORM provider (Hibernate or Ibatis)

## What will we build?
* Build a small relationship using Spring Data JPA
* Class diagram below
* Each class defines a relationship with each other
* Class has properties
* Class name = Tables name
* All table attributes are columns.
* All records will be rows created
* Students will have a guardian
* 1:m from Student to Course
* 1:1 from Course and Course Material
* m:m from Teacher to Course

![jpa_what_we_build screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/jpa_what_we_build.png)

## Connecting SpringBoot App with DB
* Now lets make an app
* We will use https://start.spring.io/ to generate the start of our app

Click this link to auto-import: https://start.spring.io/#!type=maven-project&language=java&platformVersion=3.1.3&packaging=jar&jvmVersion=11&groupId=com.harrison&artifactId=spring-data-jpa-tutorial&name=spring-data-jpa-tutorial&description=Demo%20project%20for%20Spring%20Boot&packageName=com.harrison.spring.data.jpa.tutorial&dependencies=data-jpa,lombok,mysql,web

Table if the link expires:
| Property      | Setting                                |
| ------------- | -------------------------------------- |
| Project       | Maven                                  |
| Language      | Java                                   |
| Spring Boot   | 3.1.3 (latest non-snapshot and non-M*) |
| Group         | com.name                               |
| Artifact      | spring-data-jpa-tutorial               |
| Name          | spring-data-jpa-tutorial               |
| Description   | Demo project for Spring Boot           |
| Package name  | com.name.spring.data.jpa.tutorial      |
| Packing       | Jar                                    |
| Java          | 11                                     |

Then we are going to add
* Spring Data JPA
* Lombok
* MySQL Driver
* Spring Web

Note: spring-boot-starter-web uses Hibernate as its default impl.

Create schema "schooldb" in MySQL workbench

![school_db_schema screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/school_db_schema.png)

Update the application.properties in main->resources

```properties
# What ever changes we do in the DB will be reflected in the DB
# So whatever entity DB updates we do in Java will be reflected
# Example: Adding a new field to the school object will update on the DB side
spring.jpa.hibernate.ddl-auto=update

# DB connection
spring.datasource.url=jdbc:mysql://localhost:3306/schooldb
spring.datasource.username=root
spring.datasource.password=Bingbong123$
spring.datasource.driver-class-name=com.mysql.jdbc.Driver

# Show query in the logs
spring.jpa.show-sql: true
```

## Mapping entities with DB

Create package `entity`

Create class Student

```java
package com.harrison.spring.data.jpa.tutorial.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // Now our changes will reflect in the DB
@Data // All getter and setter are generated
@AllArgsConstructor
@NoArgsConstructor
@Builder // Builder pattern
public class Student {

    @Id // Primary key
    private long studentId;
    private String firstName;
    private String lastName;
    private String emailId;
    private String guardianName;
    private String guardianMobile;
}
```

Nothing before app run. Now lets run and see the HQL take over.

```
Hibernate: create table student (student_id bigint not null, email_id varchar(255), first_name varchar(255), guardian_mobile varchar(255), guardian_name varchar(255), last_name varchar(255), primary key (student_id)) engine=InnoDB
```

The table was auto-updated

![jpa_student_was_made screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/jpa_student_was_made.png)

Camel-case was moved to underscores on the formatting

## Different JPA annotations
Note we are using DDL auto update (app.prop) so our DB will auto update

Sometimes this wont work so we need to delete our table. In that case, we delete the table.

Add the anno `@Table(name = "tbl_student")` to change the name of the table

Add the anno `@Column(name = "email_address")` to change the col name away from default

Sequence Generator
```java
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
```

Constraints
* Email must be unique

Add to the `@Table(...)` anno up top

```java
@Table(
        name = "tbl_student",
        uniqueConstraints = @UniqueConstraint(
                name = "emailid_unique",
                columnNames = "email_address"
        )
)
```

Non-null columns

Change the column anno to this for the emailId variable

```java
@Column(
        name = "email_address", // Change default name from email_id to email_address
        nullable = false
)
private String emailId;
```

We can see the tables and constraints being made
```
Hibernate: create table student_sequence (next_val bigint) engine=InnoDB
Hibernate: insert into student_sequence values ( 1 )
Hibernate: create table tbl_student (student_id bigint not null, email_address varchar(255) not null, first_name varchar(255), guardian_email varchar(255), guardian_mobile varchar(255), guardian_name varchar(255), last_name varchar(255), primary key (student_id)) engine=InnoDB
Hibernate: alter table tbl_student drop index emailid_unique
Hibernate: alter table tbl_student add constraint emailid_unique unique (email_address)
```

![jpa_tbl_student screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/jpa_tbl_student.png)

Note: We can delete the table for student to make sure we only generate one and use just that one.

`Student.java` class right now:
```java
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
    private String guardianName;
    private String guardianEmail;
    private String guardianMobile;
}
```

## Understanding Repositories and Methods
* As repositories, there are different impl for the different std operations.
* There are default impls in the Spring Data JPA

Make the StudentRepository
```java
package com.harrison.spring.data.jpa.tutorial.repository;

import com.harrison.spring.data.jpa.tutorial.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Spring will understand how to use this
public interface StudentRepository extends JpaRepository<Student, Long> {
}
```

Dive into the JpaRepository class to find out what is inherited.

Now generate a test file off that using IntelliJ

```java
package com.harrison.spring.data.jpa.tutorial.repository;

import com.harrison.spring.data.jpa.tutorial.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // Note - this will perm impact the DB
// @DataJpaTest // Normally use this to prevent DB impact. But in our case we want our DB to be impacted.
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void saveStudent() {
        // Fetch data and save data is important here
        Student student = Student.builder()
                .emailId("shabbir@gmail.com")
                .firstName("Shabbir")
                .lastName("Dawoodi")
                .guardianName("Nikhil")
                .guardianEmail("nikhil@gmail.com")
                .guardianMobile("9999999999")
                .build();

        studentRepository.save(student);
    }

}
```

Run the test

```
Hibernate: select next_val as id_val from student_sequence for update
Hibernate: update student_sequence set next_val= ? where next_val=?
Hibernate: insert into tbl_student (email_address,first_name,guardian_email,guardian_mobile,guardian_name,last_name,student_id) values (?,?,?,?,?,?,?)
```

The DB was affected after completion.

![jpa_test screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/jpa_test.png)

Now a test for fetching all students
```java
@Test
public void printAllStudents() {
    List<Student> studentList = studentRepository.findAll();

    System.out.println("studentList = " + studentList);
}
```

Test output:
```
studentList = [Student(studentId=1, firstName=Shabbir, lastName=Dawoodi, emailId=shabbir@gmail.com, guardianName=Nikhil, guardianEmail=nikhil@gmail.com, guardianMobile=9999999999)]
```

Next section, lets improve this Student class

## `@Embeddable` and `@Embedded`


