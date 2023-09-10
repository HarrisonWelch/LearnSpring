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

## JPA One to One Relationship
* Look back at the ERD from earlier in this section (8)
* Course class and course Material is a 1 to 1 relationship
* Create course and course material

Course material must have a course. CM cannot exist without C.

Course.java
```java
package com.harrison.spring.data.jpa.tutorial.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {
    // Unique ID
    // Sequence generator
    @Id
    @SequenceGenerator(
            name = "course_sequence",
            sequenceName = "course_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "course_sequence"
    )
    private Long courseId;
    private String title;
    private Integer credit;

}
```

CourseMaterial.java
```java
package com.harrison.spring.data.jpa.tutorial.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseMaterial {
    @Id
    @SequenceGenerator(
            name = "course_material_sequence",
            sequenceName = "course_material_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "course_material_sequence"
    )
    private Long courseMaterialId;
    private String url;

    // This CM table will have an extra col "course_id" that connects CM to C.
    @OneToOne
    @JoinColumn(
            name = "course_id",
            referencedColumnName = "courseId"
    )
    private Course course;
}
```


Table output Hibernate
```
Hibernate: create table course (course_id bigint not null, credit integer, title varchar(255), primary key (course_id)) engine=InnoDB
Hibernate: create table course_material_sequence (next_val bigint) engine=InnoDB
Hibernate: insert into course_material_sequence values ( 1 )
Hibernate: create table course_sequence (next_val bigint) engine=InnoDB
Hibernate: insert into course_sequence values ( 1 )
Hibernate: create table course_material (course_material_id bigint not null, url varchar(255), course_id bigint, primary key (course_material_id)) engine=InnoDB
Hibernate: alter table course_material drop index UK_h1og6srs8s1xhabgqtkhdf96q
Hibernate: alter table course_material add constraint UK_h1og6srs8s1xhabgqtkhdf96q unique (course_id)
Hibernate: alter table course_material add constraint FK6qgylrot7cxgungunwyrslpeo foreign key (course_id) references course (course_id)
```


Table creation:

Course:

![jpa_course screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/jpa_update_by_email.png)

Course Material:

![jpa_course_material screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/jpa_course_material.png)

Build the repositories and test

CourseRepository:
```java
package com.harrison.spring.data.jpa.tutorial.repository;

import com.harrison.spring.data.jpa.tutorial.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
```

CourseRepository:
```java
package com.harrison.spring.data.jpa.tutorial.repository;

import com.harrison.spring.data.jpa.tutorial.entity.CourseMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseMaterialRepository extends JpaRepository<CourseMaterial, Long> {
}
```

And then we make a test.

CourseMaterialRepositoryTest.java
```java
package com.harrison.spring.data.jpa.tutorial.repository;

import com.harrison.spring.data.jpa.tutorial.entity.Course;
import com.harrison.spring.data.jpa.tutorial.entity.CourseMaterial;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CourseMaterialRepositoryTest {

    @Autowired
    private CourseMaterialRepository courseMaterialRepository;

    @Test
    public void SaveCourseMaterial() {
        Course course = Course.builder()
                .title("DSA")
                .credit(6)
                .build();

        CourseMaterial courseMaterial = CourseMaterial.builder()
                .url("www.google.com")
                .course(course)
                .build();

        courseMaterialRepository.save(courseMaterial);
    }
}
```

Running this gives error:
```
org.springframework.dao.InvalidDataAccessApiUsageException: org.hibernate.TransientPropertyValueException: object references an unsaved transient instance - save the transient instance before flushing : com.harrison.spring.data.jpa.tutorial.entity.CourseMaterial.course -> com.harrison.spring.data.jpa.tutorial.entity.Course
```

We are saving the CourseMaterial before saving the course. Cascading comes into effect.

## Cascading

Cascading means to pass the propertiess/permissions to the child element. I want to save the CourseMaterial and I want it persisited. We need to pass that info down. We need to tell Spring to try and persist it as well.

Change the `@OneToOne` annotation to this in CourseMaterial.java
```java
    @OneToOne(
            cascade = CascadeType.ALL // Everything will happen
    )
```

Now run the test

Appears to be successful:
```
Hibernate: select next_val as id_val from course_material_sequence for update
Hibernate: update course_material_sequence set next_val= ? where next_val=?
Hibernate: select next_val as id_val from course_sequence for update
Hibernate: update course_sequence set next_val= ? where next_val=?
Hibernate: insert into course (credit,title,course_id) values (?,?,?)
Hibernate: insert into course_material (course_id,url,course_material_id) values (?,?,?)
```

Course now on the DB:

![jpa_course_1 screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/jpa_update_by_email.png)

Course Material now on the DB:

![jpa_course_material_1 screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/jpa_course_material_1.png)

## Fetch Types

There are two types.
* Eager fetching.
* Lazy fetching.

If and when you want to search the CourseMaterial do you always need to bring in the Material as well.

This exists in the OneToOne anno for example:
```java
@OneToOne(
        cascade = CascadeType.ALL, // Everything will happen
        fetch = FetchType.LAZY
)
```

For this we develop the simple test
```java
@Test
public void printAllCourseMaterials() {
List<CourseMaterial> courseMaterials = courseMaterialRepository.findAll();

System.out.println("courseMaterials = " + courseMaterials);
}
```
For this we get error "could not initialize proxy"

```

OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
Hibernate: select c1_0.course_material_id,c1_0.course_id,c1_0.url from course_material c1_0

org.hibernate.LazyInitializationException: could not initialize proxy [com.harrison.spring.data.jpa.tutorial.entity.Course#1] - no Session
```

To fix this we need to add ```@ToString(exclude = "course")``` to the CourseMaterial java object.

So you get this:
```java
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = "course")
public class CourseMaterial {...}
```

And the test works now

```
Hibernate: select c1_0.course_material_id,c1_0.course_id,c1_0.url from course_material c1_0
courseMaterials = [CourseMaterial(courseMaterialId=1, url=www.google.com)]
```

## Uni & Bi directional Relationships

Build out a test class
```java
package com.harrison.spring.data.jpa.tutorial.repository;

import com.harrison.spring.data.jpa.tutorial.entity.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    public void printCourses() {
        List<Course> courses = courseRepository.findAll();
        System.out.println("courses = " + courses);
    }

}
```

Output

```
Hibernate: select c1_0.course_id,c1_0.credit,c1_0.title from course c1_0
courses = [Course(courseId=1, title=DSA, credit=6)]
```

Add this to Course to flip it to bi directional
```java
@OneToOne(
        mappedBy = "course"
)
private CourseMaterial courseMaterial;
```

Now the test shows us bringing in the CourseMaterial

```
Hibernate: select c1_0.course_id,c1_0.credit,c1_0.title from course c1_0
Hibernate: select c1_0.course_material_id,c1_0.course_id,c1_0.url from course_material c1_0 where c1_0.course_id=?
courses = [Course(courseId=1, title=DSA, credit=6, courseMaterial=CourseMaterial(courseMaterialId=1, url=www.google.com))]
```

This is how you define uni-directional in the many relationship types.

## JPA One to Many Relationship

Teacher to Course
* 1 Teacher can teach multiple courses
* Or 1 Course can be taught by multiple teachers

Run the app

```java
package com.harrison.spring.data.jpa.tutorial.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Teacher {

    @Id
    @SequenceGenerator(
            name = "teacher_sequence",
            sequenceName = "teacher_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "teacher_sequence"
    )
    private Long teacherId;
    private String firstName;
    private String lastName;

    @OneToMany(
            cascade = CascadeType.ALL
    )
    @JoinColumn(
            name = "teacher_id",
            referencedColumnName = "teacherId"
    )
    private List<Course> courses;
}
```

```
Hibernate: alter table course add column teacher_id bigint
Hibernate: create table teacher (teacher_id bigint not null, first_name varchar(255), last_name varchar(255), primary key (teacher_id)) engine=InnoDB
Hibernate: create table teacher_sequence (next_val bigint) engine=InnoDB
Hibernate: insert into teacher_sequence values ( 1 )
Hibernate: alter table course add constraint FKsybhlxoejr4j3teomm5u2bx1n foreign key (teacher_id) references teacher (teacher_id)
```

Now make the repository and a test

```java
package com.harrison.spring.data.jpa.tutorial.repository;

import com.harrison.spring.data.jpa.tutorial.entity.Course;
import com.harrison.spring.data.jpa.tutorial.entity.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TeacherRepositoryTest {
    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    public void saveTeacher() {

        Course courseDBA = Course.builder()
                .title("DBA")
                .credit(5)
                .build();

        Course courseJava = Course.builder()
                .title("Java")
                .credit(6)
                .build();

        Teacher teacher = Teacher.builder()
                .firstName("Qutub")
                .lastName("Khan")
                .courses(List.of(courseDBA, courseJava))
                .build();

        teacherRepository.save(teacher);
    }

}
```

Run the test and see that the teacher is now there with the connected courses.

Hibernate will do its thing and make this for us
```
Hibernate: select next_val as id_val from teacher_sequence for update
Hibernate: update teacher_sequence set next_val= ? where next_val=?
Hibernate: select next_val as id_val from course_sequence for update
Hibernate: update course_sequence set next_val= ? where next_val=?
Hibernate: select next_val as id_val from course_sequence for update
Hibernate: update course_sequence set next_val= ? where next_val=?
Hibernate: insert into teacher (first_name,last_name,teacher_id) values (?,?,?)
Hibernate: insert into course (credit,title,course_id) values (?,?,?)
Hibernate: insert into course (credit,title,course_id) values (?,?,?)
Hibernate: update course set teacher_id=? where course_id=?
Hibernate: update course set teacher_id=? where course_id=?
```

Teacher is now:
![jpa_teacher_test screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/jpa_teacher_test.png)

And the courses were added:
![jpa_course_test_after_teacher screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/jpa_course_test_after_teacher.png)

We were allowed to save a Course without a CourseMaterial
* Optionality
* By default everything is optional

```java
@OneToOne(
        cascade = CascadeType.ALL, // Everything will happen
        fetch = FetchType.LAZY,
        optional = false
)
```

For example lets try and test a peice of code that will save a Course w/out CM.

Test:
```java
    @Test
    public void SaveCourseMaterial() {
        Course course = Course.builder()
                .title("DSA")
                .credit(6)
                .build();

        CourseMaterial courseMaterial = CourseMaterial.builder()
                .url("www.dailycodebuffer.com")
//                .course(course)
                .build();

        courseMaterialRepository.save(courseMaterial);
    }
```
```
Caused by: org.hibernate.PropertyValueException: not-null property references a null or transient value : com.harrison.spring.data.jpa.tutorial.entity.CourseMaterial.course
```

Fix it to this:

```java
    @Test
    public void SaveCourseMaterial() {
        Course course = Course.builder()
                .title(".NET")
                .credit(6)
                .build();

        CourseMaterial courseMaterial = CourseMaterial.builder()
                .url("www.dailycodebuffer.com")
                .course(course)
                .build();

        courseMaterialRepository.save(courseMaterial);
    }
```

And it works so says Hibernate.

```
Hibernate: select next_val as id_val from course_material_sequence for update
Hibernate: update course_material_sequence set next_val= ? where next_val=?
Hibernate: select next_val as id_val from course_sequence for update
Hibernate: update course_sequence set next_val= ? where next_val=?
Hibernate: insert into course (credit,title,course_id) values (?,?,?)
Hibernate: insert into course_material (course_id,url,course_material_id) values (?,?,?)
```

Look at the DB and it will show the new C & CM.

And the course was added:
![jpa_course_after_optional_fix_test screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/jpa_course_after_optional_fix_test.png)

And the course material was added:
![jpa_course_material_after_optional_fix_test screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/jpa_course_material_after_optional_fix_test.png)

## JPA Many to One Relationship

Rather than have a teacher doing a specific course you can have a course that is designed for multiple teachers.
* You should go for many to one releationships whereever possible

Comment out the OneToMany in Teacher

Add this to the Course Class

```java
@ManyToOne(
        cascade = CascadeType.ALL
)
@JoinColumn(
        name = "teacher_id",
        referencedColumnName = "teacherId"
)
private Teacher teacher;
```

Make a test in `CourseRepositoryTest.java`

```java
@Test
public void saveCourseWithTeacher() {
Teacher teacher = Teacher
        .builder()
        .firstName("Priyanka")
        .lastName("Singh")
        .build();

Course course = Course
        .builder()
        .title("Python")
        .credit(6)
        .teacher(teacher)
        .build();

courseRepository.save(course);
}
```

Note: Initial error because of the DB change. Comment out `.courses(List.of(courseDBA, courseJava))`.

Error:
```
C:\...\LearnSpring\Section_8_Spring_Data_JPA\spring-data-jpa-tutorial\src\test\java\com\harrison\spring\data\jpa\tutorial\repository\TeacherRepositoryTest.java:34:17
java: cannot find symbol
  symbol:   method courses(java.util.List<com.harrison.spring.data.jpa.tutorial.entity.Course>)
  location: class com.harrison.spring.data.jpa.tutorial.entity.Teacher.TeacherBuilder
```

But after fixing the error you see something like this in the test log

```
Hibernate: select next_val as id_val from course_sequence for update
Hibernate: update course_sequence set next_val= ? where next_val=?
Hibernate: select next_val as id_val from teacher_sequence for update
Hibernate: update teacher_sequence set next_val= ? where next_val=?
Hibernate: insert into teacher (first_name,last_name,teacher_id) values (?,?,?)
Hibernate: insert into course (credit,teacher_id,title,course_id) values (?,?,?,?)
```

And the DB will reflect that

The Teacher was added:
![jpa_course_material_after_optional_fix_test screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/jpa_course_material_after_optional_fix_test.png)

The Course was addded:
![jpa_teacher_many_to_one_test screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/jpa_teacher_many_to_one_test.png)

## Paging and Sorting

For every repo, we extend JPA-repo, which extends the `PagingAndSortingRepository<T, ID>`
* Learn how to use the default method
* And how to impl custom behavior

```java

    @Test
    public void findAllPagination() {
        Pageable firstPageWithThreeRecords = PageRequest.of(0,3);
        Pageable secondPageWithTwoRecords = PageRequest.of(1,2);

        {
            List<Course> courses = courseRepository.findAll(firstPageWithThreeRecords).getContent();
            System.out.println("courses = " + courses);

            long totalElements = courseRepository.findAll(firstPageWithThreeRecords).getTotalElements();
            System.out.println("totalElements = " + totalElements);

            long totalPages = courseRepository.findAll(firstPageWithThreeRecords).getTotalPages();
            System.out.println("totalPages = " + totalPages);
        }

        // ----

        {
            List<Course> courses = courseRepository.findAll(secondPageWithTwoRecords).getContent();
            System.out.println("courses = " + courses);

            long totalElements = courseRepository.findAll(secondPageWithTwoRecords).getTotalElements();
            System.out.println("totalElements = " + totalElements);

            long totalPages = courseRepository.findAll(secondPageWithTwoRecords).getTotalPages();
            System.out.println("totalPages = " + totalPages);
        }

    }
```

Output

```
Hibernate: select c1_0.course_id,c1_0.credit,c1_0.teacher_id,c1_0.title from course c1_0 limit ?,?
Hibernate: select c1_0.course_material_id,c1_0.course_id,c1_0.url from course_material c1_0 where c1_0.course_id=?
Hibernate: select c1_0.course_material_id,c1_0.course_id,c1_0.url from course_material c1_0 where c1_0.course_id=?
Hibernate: select t1_0.teacher_id,t1_0.first_name,t1_0.last_name from teacher t1_0 where t1_0.teacher_id=?
Hibernate: select c1_0.course_material_id,c1_0.course_id,c1_0.url from course_material c1_0 where c1_0.course_id=?
Hibernate: select count(c1_0.course_id) from course c1_0
courses = [Course(courseId=1, title=DSA, credit=6, courseMaterial=CourseMaterial(courseMaterialId=1, url=www.google.com), teacher=null), Course(courseId=2, title=DBA, credit=5, courseMaterial=null, teacher=Teacher(teacherId=1, firstName=Qutub, lastName=Khan)), Course(courseId=3, title=Java, credit=6, courseMaterial=null, teacher=Teacher(teacherId=1, firstName=Qutub, lastName=Khan))]
Hibernate: select c1_0.course_id,c1_0.credit,c1_0.teacher_id,c1_0.title from course c1_0 limit ?,?
Hibernate: select c1_0.course_material_id,c1_0.course_id,c1_0.url from course_material c1_0 where c1_0.course_id=?
Hibernate: select c1_0.course_material_id,c1_0.course_id,c1_0.url from course_material c1_0 where c1_0.course_id=?
Hibernate: select t1_0.teacher_id,t1_0.first_name,t1_0.last_name from teacher t1_0 where t1_0.teacher_id=?
Hibernate: select c1_0.course_material_id,c1_0.course_id,c1_0.url from course_material c1_0 where c1_0.course_id=?
Hibernate: select count(c1_0.course_id) from course c1_0
totalElements = 5
Hibernate: select c1_0.course_id,c1_0.credit,c1_0.teacher_id,c1_0.title from course c1_0 limit ?,?
Hibernate: select c1_0.course_material_id,c1_0.course_id,c1_0.url from course_material c1_0 where c1_0.course_id=?
Hibernate: select c1_0.course_material_id,c1_0.course_id,c1_0.url from course_material c1_0 where c1_0.course_id=?
Hibernate: select t1_0.teacher_id,t1_0.first_name,t1_0.last_name from teacher t1_0 where t1_0.teacher_id=?
Hibernate: select c1_0.course_material_id,c1_0.course_id,c1_0.url from course_material c1_0 where c1_0.course_id=?
Hibernate: select count(c1_0.course_id) from course c1_0
totalPages = 2
Hibernate: select c1_0.course_id,c1_0.credit,c1_0.teacher_id,c1_0.title from course c1_0 limit ?,?
Hibernate: select c1_0.course_material_id,c1_0.course_id,c1_0.url from course_material c1_0 where c1_0.course_id=?
Hibernate: select t1_0.teacher_id,t1_0.first_name,t1_0.last_name from teacher t1_0 where t1_0.teacher_id=?
Hibernate: select c1_0.course_material_id,c1_0.course_id,c1_0.url from course_material c1_0 where c1_0.course_id=?
Hibernate: select count(c1_0.course_id) from course c1_0
courses = [Course(courseId=3, title=Java, credit=6, courseMaterial=null, teacher=Teacher(teacherId=1, firstName=Qutub, lastName=Khan)), Course(courseId=4, title=.NET, credit=6, courseMaterial=CourseMaterial(courseMaterialId=3, url=www.dailycodebuffer.com), teacher=null)]
Hibernate: select c1_0.course_id,c1_0.credit,c1_0.teacher_id,c1_0.title from course c1_0 limit ?,?
Hibernate: select c1_0.course_material_id,c1_0.course_id,c1_0.url from course_material c1_0 where c1_0.course_id=?
Hibernate: select t1_0.teacher_id,t1_0.first_name,t1_0.last_name from teacher t1_0 where t1_0.teacher_id=?
Hibernate: select c1_0.course_material_id,c1_0.course_id,c1_0.url from course_material c1_0 where c1_0.course_id=?
Hibernate: select count(c1_0.course_id) from course c1_0
totalElements = 5
Hibernate: select c1_0.course_id,c1_0.credit,c1_0.teacher_id,c1_0.title from course c1_0 limit ?,?
Hibernate: select c1_0.course_material_id,c1_0.course_id,c1_0.url from course_material c1_0 where c1_0.course_id=?
Hibernate: select t1_0.teacher_id,t1_0.first_name,t1_0.last_name from teacher t1_0 where t1_0.teacher_id=?
Hibernate: select c1_0.course_material_id,c1_0.course_id,c1_0.url from course_material c1_0 where c1_0.course_id=?
Hibernate: select count(c1_0.course_id) from course c1_0
totalPages = 3

```

* The first pack of code lines does the 1st page with 3 records to 3 then 2 more for a total of 5
* The second pack of code lines does the 2nd page with 2 records so 2, 2, 1 for a total of 5. I think.

### Defining sorting

```java
    @Test
    public void findAllSorting() {
        Pageable sortByTitle =
                PageRequest.of(
                        0,
                        2,
                        Sort.by("title")
                );
        Pageable sortByCreditDesc =
                PageRequest.of(
                        0,
                        2,
                        Sort.by("credit").descending()
                );
        Pageable sortByTitleAndCreditDesc =
                PageRequest.of(
                        0,
                        2,
                        Sort.by("title")
                            .descending()
                            .and(Sort.by("credit"))
                );

        List<Course> courses = courseRepository.findAll(sortByTitle).getContent();
        System.out.println("courses = " + courses);
    }
```

Output

```
Hibernate: select c1_0.course_id,c1_0.credit,c1_0.teacher_id,c1_0.title from course c1_0 order by c1_0.title limit ?,?
Hibernate: select c1_0.course_material_id,c1_0.course_id,c1_0.url from course_material c1_0 where c1_0.course_id=?
Hibernate: select c1_0.course_material_id,c1_0.course_id,c1_0.url from course_material c1_0 where c1_0.course_id=?
Hibernate: select t1_0.teacher_id,t1_0.first_name,t1_0.last_name from teacher t1_0 where t1_0.teacher_id=?
Hibernate: select count(c1_0.course_id) from course c1_0
courses = [Course(courseId=4, title=.NET, credit=6, courseMaterial=CourseMaterial(courseMaterialId=3, url=www.dailycodebuffer.com), teacher=null), Course(courseId=2, title=DBA, credit=5, courseMaterial=null, teacher=Teacher(teacherId=1, firstName=Qutub, lastName=Khan))]
```

Which shows the sort. ".NET" with the period should go before "DBA"

### Custom methods

In the CourseRepo interface define the method `findByTitleContaining`.

```java
package com.harrison.spring.data.jpa.tutorial.repository;

import com.harrison.spring.data.jpa.tutorial.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Page<Course> findByTitleContaining(
            String title,
            Pageable pageable);
}

```

Then we can use this in a quick test

```java
@Test
public void findByTitleContaining() {
Pageable firstPageTenRecords =
        PageRequest.of(0, 10);

List<Course> courses = courseRepository.findByTitleContaining(
        "D",
        firstPageTenRecords).getContent();

System.out.println("courses = " + courses);
}
```

Output
```
Hibernate: select c1_0.course_id,c1_0.credit,c1_0.teacher_id,c1_0.title from course c1_0 where c1_0.title like ? escape '\\' limit ?,?
Hibernate: select c1_0.course_material_id,c1_0.course_id,c1_0.url from course_material c1_0 where c1_0.course_id=?
Hibernate: select c1_0.course_material_id,c1_0.course_id,c1_0.url from course_material c1_0 where c1_0.course_id=?
Hibernate: select t1_0.teacher_id,t1_0.first_name,t1_0.last_name from teacher t1_0 where t1_0.teacher_id=?
courses = [Course(courseId=1, title=DSA, credit=6, courseMaterial=CourseMaterial(courseMaterialId=1, url=www.google.com), teacher=null), Course(courseId=2, title=DBA, credit=5, courseMaterial=null, teacher=Teacher(teacherId=1, firstName=Qutub, lastName=Khan))]
```

So we see only the courses with "D" in the title field that was paginated via Spring.

## JPA Many to Many Relationship

* Student to Course
* Many students can opt for many courses
* Has to be a 3rd new table
* This table holds the relationship

```java
@ManyToMany
@JoinTable(
        name = "student_course_map",
        joinColumns = @JoinColumn(
                name = "course_id", // DB column name in MySQL
                referencedColumnName = "courseId" // class property in Java
        ),
        inverseJoinColumns = @JoinColumn( // Invert back on itself
                name = "student_id",
                referencedColumnName = "studentId"
        )
)
private List<Student> students;

public void addStudents(Student student) {
if (student == null) students = new ArrayList<>();
students.add(student);
}
```

Start the main SpringDataJpaTutorialApplication.java

We can see hibernate make the map-table:
```
Hibernate: create table student_course_map (course_id bigint not null, student_id bigint not null) engine=InnoDB
Hibernate: alter table student_course_map add constraint FKhtofos1jo0mi02er8bqx62xrd foreign key (student_id) references tbl_student (student_id)
Hibernate: alter table student_course_map add constraint FK4f31efg3l6lqa5n0yqml6h7c4 foreign key (course_id) references course (course_id)
```

MySQL workbench after a refresh:
![jpa_many_to_many_table_create screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/jpa_many_to_many_table_create.png)

Now let's make a test that saves this.

```java
    @Test
    public void saveCourseWithStudentAndTeacher() {
        Teacher teacher = Teacher
                .builder()
                .firstName("Lizze")
                .lastName("Morgan")
                .build();

        Student student = Student
                .builder()
                .firstName("Abhishek")
                .lastName("Singh")
                .emailId("abhishek@gmail.com")
                .build();

        Course course = Course
                .builder()
                .title("AI")
                .credit(12)
                .teacher(teacher)
                .build();

        course.addStudents(student);

        courseRepository.save(course);
    }

```

Forgot to define the cascade type so we get another `TransientObjectException`:
```
org.springframework.dao.InvalidDataAccessApiUsageException: org.hibernate.TransientObjectException: object references an unsaved transient instance - save the transient instance before flushing: com.harrison.spring.data.jpa.tutorial.entity.Student
```

Change the many to many annotation to have the cascade set.
```java
@ManyToMany(
        cascade = CascadeType.ALL
)
```

Now we have a success and output looks like this:
```
Hibernate: select next_val as id_val from course_sequence for update
Hibernate: update course_sequence set next_val= ? where next_val=?
Hibernate: select next_val as id_val from teacher_sequence for update
Hibernate: update teacher_sequence set next_val= ? where next_val=?
Hibernate: select next_val as id_val from student_sequence for update
Hibernate: update student_sequence set next_val= ? where next_val=?
Hibernate: insert into teacher (first_name,last_name,teacher_id) values (?,?,?)
Hibernate: insert into course (credit,teacher_id,title,course_id) values (?,?,?,?)
Hibernate: insert into tbl_student (email_address,first_name,guardian_email,guardian_mobile,guardian_name,last_name,student_id) values (?,?,?,?,?,?,?)
Hibernate: insert into student_course_map (course_id,student_id) values (?,?)
```

Course:

![jpa_many_to_many_course screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/jpa_many_to_many_course.png)

Teacher:

![jpa_many_to_many_teacher screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/jpa_many_to_many_teacher.png)

Student:

![jpa_many_to_many_student screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/jpa_many_to_many_student.png)

Mapping table:

![jpa_many_to_many_mapping_table screenshot](https://github.com/HarrisonWelch/LearnSpring/blob/main/Screenshots/jpa_many_to_many_mapping_table.png)

## Wrapping up
* Various operations covered
* Default EntityManager, docs - can do own impl yourself
* Jpa is the preferred choice as it will do alot for you.
* Plus really good performance as well.
