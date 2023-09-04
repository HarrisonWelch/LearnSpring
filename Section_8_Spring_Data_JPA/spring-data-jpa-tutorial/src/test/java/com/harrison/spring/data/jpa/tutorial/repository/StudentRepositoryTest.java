package com.harrison.spring.data.jpa.tutorial.repository;

import com.harrison.spring.data.jpa.tutorial.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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

    @Test
    public void printAllStudents() {
        List<Student> studentList = studentRepository.findAll();

        System.out.println("studentList = " + studentList);
    }
}