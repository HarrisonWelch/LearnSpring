package com.harrison.spring.data.jpa.tutorial.repository;

import com.harrison.spring.data.jpa.tutorial.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Spring will understand how to use this
public interface StudentRepository extends JpaRepository<Student, Long> {
}
