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


