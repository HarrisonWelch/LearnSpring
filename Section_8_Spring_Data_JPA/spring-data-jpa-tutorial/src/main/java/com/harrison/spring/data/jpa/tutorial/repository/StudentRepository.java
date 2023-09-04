package com.harrison.spring.data.jpa.tutorial.repository;

import com.harrison.spring.data.jpa.tutorial.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Spring will understand how to use this
public interface StudentRepository extends JpaRepository<Student, Long> {

    public List<Student> findByFirstName(String firstName); // Jpa does some magic to know you want to search by firstName. Must match casing exactly as needed.

    public List<Student> findByFirstNameContaining(String name); // Containing chars '%...%'

    public List<Student> findByLastNameNotNull(); // Want records which are not null

    public List<Student> findByGuardianName(String guardianName); // Reach into the embed obj

    public Student findByFirstNameAndLastName(String firstName, String lastName); // Or can also work

    // JPQL
    @Query("select s from Student s where s.emailId = ?1") // ?1 for the first param
    public Student getStudentByEmailAddress(String emailId);

    // JPQL
    @Query("select s.firstName from Student s where s.emailId = ?1")
    public String getFirstNameByEmailAddress(String emailId);

}
