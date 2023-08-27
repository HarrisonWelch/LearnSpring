package com.harrison.Springboot.tutorial.controller;

import com.harrison.Springboot.tutorial.entity.Department;
import com.harrison.Springboot.tutorial.service.DepartmentService;
import com.harrison.Springboot.tutorial.service.DepartmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DepartmentController {

    @Autowired // Auto wire the service you already have and attach to this controller object
    private DepartmentService departmentService;
    @PostMapping("/departments")
    public Department saveDepartment(@RequestBody Department department) { // JSON converted to Department
        // Spring will convert the data itself
        // Old way where we ask for the control, DepartmentService service = new DepartmentServiceImpl();
        // Invert control by using the Autowired annotation as seen above
        return departmentService.saveDepartment(department);
    }

    @GetMapping("/departments")
    public List<Department> fetchDepartmentList() {
        return departmentService.fetchDepartmentList(); // no input b/c sending all data back
    }

}
