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

Ideally the Guardian attributes of the student class need to be a diff class. Lets move them.

But I don't want the guardian to be it's own table.

Since we already have the columns deployed we can use an override to get the column names the same.

Guardian.java:
```java
package com.harrison.spring.data.jpa.tutorial.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable // Can be put into another class
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@AttributeOverrides({
        @AttributeOverride(
                name = "name",
                column = @Column(name = "guardian_name")
        ),
        @AttributeOverride(
                name = "email",
                column = @Column(name = "guardian_email")
        ),
        @AttributeOverride(
                name = "mobile",
                column = @Column(name = "guardian_mobile")
        )
})
public class Guardian {

    private String name;
    private String email;
    private String mobile;
}
```

Now we need to fix our tests because we can't use the builder patter method of setting the guardian name.

StudentRepositoryTest.java:
```java
    @Test
    public void saveStudent() {
        // Fetch data and save data is important here
        Student student = Student.builder()
                .emailId("shabbir@gmail.com")
                .firstName("Shabbir")
                .lastName("Dawoodi")
                //.guardianName("Nikhil")
                //.guardianEmail("nikhil@gmail.com")
                //.guardianMobile("9999999999")
                .build();

        studentRepository.save(student);
    }
```

And now we need to make a test that includes the guardian like so:
* Note we added the `@Builder` anno to the Guardian class.

StudentRepositoryTest.java:
```java

    @Test
    public void saveStudentWithGuardian() {

        Guardian guardian = Guardian.builder()
                .name("Nikhil")
                .email("nikhil@gmail.com")
                .mobile("9999999999")
                .build();

        Student student = Student.builder()
                .firstName("Shivam")
                .emailId("shivam@gmail.com")
                .lastName("Kumar")
                .guardian(guardian) // <--- important step
                .build();

        studentRepository.save(student);
    }
```

Run the test and you get the same effect on the logs and DB

Log output:
```
Hibernate: select next_val as id_val from student_sequence for update
Hibernate: update student_sequence set next_val= ? where next_val=?
Hibernate: insert into tbl_student (email_address,first_name,guardian_email,guardian_mobile,guardian_name,last_name,student_id) values (?,?,?,?,?,?,?)
```

![jpa_student_with_embed_guardian_was_made screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/jpa_student_with_embed_guardian_was_made.png)

## JPA Repositories Methods

Lets see the diff methods in our student repository

StudentRepository.java:
```java
public List<Student> findByFirstName(String firstName); // Jpa does some magic to know you want to search by firstName. Must match casing exactly as needed.

```

Then we use it

StudentRepositoryTest.java:
```java
    @Test
    public void printStudentByFirstName() {
        List<Student> students = studentRepository.findByFirstName("Shivam");
        System.out.println("students = " + students);
    }
```

Run the test and we can see it works fine:
```
Hibernate: select s1_0.student_id,s1_0.email_address,s1_0.first_name,s1_0.guardian_email,s1_0.guardian_mobile,s1_0.guardian_name,s1_0.last_name from tbl_student s1_0 where s1_0.first_name=?
students = [Student(studentId=2, firstName=Shivam, lastName=Kumar, emailId=shivam@gmail.com, guardian=Guardian(name=Nikhil, email=nikhil@gmail.com, mobile=9999999999))]
```

Now lets do a `containing` query


StudentRepository.java:
```java
    public List<Student> findByFirstNameContaining(String name);
```

Then we use it

StudentRepositoryTest.java:
```java
    @Test
    public void printStudentByFirstNameContaining() {
        List<Student> students = studentRepository.findByFirstNameContaining("sh");
        System.out.println("students = " + students);
    }
```

Output both records:
```
Hibernate: select s1_0.student_id,s1_0.email_address,s1_0.first_name,s1_0.guardian_email,s1_0.guardian_mobile,s1_0.guardian_name,s1_0.last_name from tbl_student s1_0 where s1_0.first_name like ? escape '\\'
students = [Student(studentId=1, firstName=Shabbir, lastName=Dawoodi, emailId=shabbir@gmail.com, guardian=Guardian(name=Nikhil, email=nikhil@gmail.com, mobile=9999999999)), Student(studentId=2, firstName=Shivam, lastName=Kumar, emailId=shivam@gmail.com, guardian=Guardian(name=Nikhil, email=nikhil@gmail.com, mobile=9999999999))]
```

JPA did the escape character `'%...%'` for us.

Make these methods

StudentRepository.java:
```java
    public List<Student> findByLastNameNotNull(); // Want records which are not null

    public List<Student> findByGuardianName(String guardianName); // Reach into the embed obj
```

Impl the 2nd method `findByGuardianName`

StudentRepositoryTest.java:
```java
    @Test
    public void printStudentBasedOnGuardianName() {
        List<Student> students = studentRepository.findByGuardianName("Nikhil");
        System.out.println("students = " + students);
    }
```

Test output:

```
Hibernate: select s1_0.student_id,s1_0.email_address,s1_0.first_name,s1_0.guardian_email,s1_0.guardian_mobile,s1_0.guardian_name,s1_0.last_name from tbl_student s1_0 where s1_0.guardian_name=?
students = [Student(studentId=1, firstName=Shabbir, lastName=Dawoodi, emailId=shabbir@gmail.com, guardian=Guardian(name=Nikhil, email=nikhil@gmail.com, mobile=9999999999)), Student(studentId=2, firstName=Shivam, lastName=Kumar, emailId=shivam@gmail.com, guardian=Guardian(name=Nikhil, email=nikhil@gmail.com, mobile=9999999999))]

```

Query and records are coming back fine.

Link to other methods: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
* And - for lastName and firstName

Example:

StudentRepository.java:
```java
    public Student findByFirstNameAndLastName(String firstName, String lastName); // Or can also work
```

* Between is there
* IgnoreCase is there

# JPA `@Query` Annotation

* Creating the method name is not enough
* You have to put in the query yourself

StudentRepository.java:
```java
// JPQL
@Query("select s from Student s where s.emailId = ?1") // ?1 for the first param
public Student getStudentByEmailAddress(String emailId);
```

StudentRepositoryTest.java
```java
@Test
public void printGetStudentByEmailAddress() {
    Student student = studentRepository.getStudentByEmailAddress("shabbir@gmail.com");
    System.out.println("student = " + student);
}
```

Output:
```
Hibernate: select s1_0.student_id,s1_0.email_address,s1_0.first_name,s1_0.guardian_email,s1_0.guardian_mobile,s1_0.guardian_name,s1_0.last_name from tbl_student s1_0 where s1_0.email_address=?
student = Student(studentId=1, firstName=Shabbir, lastName=Dawoodi, emailId=shabbir@gmail.com, guardian=Guardian(name=Nikhil, email=nikhil@gmail.com, mobile=9999999999))
```

Can also do query attrs and return them like the first name for example.

StudentRepository.java:
```java
// JPQL
@Query("select s.firstName from Student s where s.emailId = ?1")
public String getFirstNameByEmailAddress(String emailId);
```

StudentRepositoryTest.java
```java
@Test
public void printGetFirstNameByEmailAddress() {
    String firstName = studentRepository.getFirstNameByEmailAddress("shivam@gmail.com");
    System.out.println("firstName = " + firstName);
}
```

Output
```
Hibernate: select s1_0.first_name from tbl_student s1_0 where s1_0.email_address=?
firstName = Shivam
```

## Native Queries Example

Used when executing complex queries. Example for proof of concepts.

StudentRepository.java:
```java
// Native query
@Query(
        value = "SELECT * FROM tbl_student s where s.email_address = ?1",
        nativeQuery = true
)
public Student getStudentByEmailAddressNative(String emailId);
```

StudentRepositoryTest.java
```java
@Test
public void printGetStudentByEmailAddressNative() {
    Student student = studentRepository.getStudentByEmailAddressNative("shabbir@gmail.com");
    System.out.println("student = " + student);
}
```

Output
```
Hibernate: SELECT * FROM tbl_student s where s.email_address = ?
student = Student(studentId=1, firstName=Shabbir, lastName=Dawoodi, emailId=shabbir@gmail.com, guardian=Guardian(name=Nikhil, email=nikhil@gmail.com, mobile=9999999999))
```

## Query Named Params
* To stop passing the question marks `?1`, `?2`, etc.
* Good for labeling

StudentRepository.java:
```java
// Native Named Param
@Query(
        value = "SELECT * FROM tbl_student s where s.email_address = :emailId",
        nativeQuery = true
)
public Student getStudentByEmailAddressNativeNamedParam(@Param("emailId") String emailId);
```

StudentRepositoryTest.java
```java
@Test
public void printGetStudentByEmailAddressNativeNamedParam() {
    Student student = studentRepository.getStudentByEmailAddressNativeNamedParam("shabbir@gmail.com");
    System.out.println("student = " + student);
}
```

Output
```
Hibernate: SELECT * FROM tbl_student s where s.email_address = ?
student = Student(studentId=1, firstName=Shabbir, lastName=Dawoodi, emailId=shabbir@gmail.com, guardian=Guardian(name=Nikhil, email=nikhil@gmail.com, mobile=9999999999))
```

## `@Transactional` & `@Modifying` Annotation

* Method to update the record
* Update the first name of the student by email id

StudentRepository.java:
```java
@Modifying // We are going to modify data
@Transactional // We need to open a transaction and commit things at the end. Only on success, else rollback.
@Query(
        value = "update tbl_student set first_name = ?1 where email_address = ?2",
        nativeQuery = true
)
public int updateStudentNameByEmailId(String firstName, String emailId);
```

StudentRepositoryTest.java
```java
@Test
public void updateStudentNameByEmailIdTest() {
    studentRepository.updateStudentNameByEmailId(
            "shabbir dawoodi",
            "shabbir@gmail.com");
}
```

Output
```
Hibernate: update tbl_student set first_name = ? where email_address = ?
```

DB update:
![jpa_update_by_email screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/jpa_update_by_email.png)

We saw a lot on diff DBs and annotations. Time to move on to the JPA relationship

