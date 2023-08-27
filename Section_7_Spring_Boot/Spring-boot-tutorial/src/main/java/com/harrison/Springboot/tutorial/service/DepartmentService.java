package com.harrison.Springboot.tutorial.service;

import com.harrison.Springboot.tutorial.entity.Department;

import java.util.List;

public interface DepartmentService {

    public Department saveDepartment(Department department);

    public List<Department> fetchDepartmentList();
}
