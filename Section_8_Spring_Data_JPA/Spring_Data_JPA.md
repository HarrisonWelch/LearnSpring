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
