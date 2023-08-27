package com.harrison.Springboot.tutorial.service;

import com.harrison.Springboot.tutorial.entity.Department;
import com.harrison.Springboot.tutorial.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;
    @Override
    public Department saveDepartment(Department department) {
        return departmentRepository.save(department); // from the extended JpaRepository
    }

    @Override
    public List<Department> fetchDepartmentList() {
        return departmentRepository.findAll(); // Done! Easy!
    }
}
