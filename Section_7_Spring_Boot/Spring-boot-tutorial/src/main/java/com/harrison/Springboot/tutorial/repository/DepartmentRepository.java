package com.harrison.Springboot.tutorial.repository;

import com.harrison.Springboot.tutorial.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> { // Entity and primary key type
}
