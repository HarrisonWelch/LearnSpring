package com.harrison.Springboot.tutorial.controller;

import com.harrison.Springboot.tutorial.entity.Department;
import com.harrison.Springboot.tutorial.error.DepartmentNotFoundException;
import com.harrison.Springboot.tutorial.service.DepartmentService;
import com.harrison.Springboot.tutorial.service.DepartmentServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DepartmentController {

    @Autowired // Auto wire the service you already have and attach to this controller object
    private DepartmentService departmentService;

    private final Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);

    @PostMapping("/departments")
    public Department saveDepartment(@Valid @RequestBody Department department) { // JSON converted to Department
        // Spring will convert the data itself
        // Old way where we ask for the control, DepartmentService service = new DepartmentServiceImpl();
        // Invert control by using the Autowired annotation as seen above
        // @Valid will check agains the @NotBlank over in the Department class

        LOGGER.info("Inside saveDepartment of Department controller");

        return departmentService.saveDepartment(department);
    }

    @GetMapping("/departments")
    public List<Department> fetchDepartmentList() {
        LOGGER.info("Inside fetchDepartmentList of Department controller");
        return departmentService.fetchDepartmentList(); // no input b/c sending all data back
    }

    @GetMapping("/departments/{id}") // path variable
    public Department fetchDepartmentById(@PathVariable("id") Long departmentId) throws DepartmentNotFoundException {
        LOGGER.info("Inside fetchDepartmentById of Department controller");
        return departmentService.fetchDepartmentById(departmentId);
    }

    @DeleteMapping("/departments/{id}")
    public String deleteDepartmentById(@PathVariable("id") Long departmentId) {
        LOGGER.info("Inside deleteDepartmentById of Department controller");
        departmentService.deleteDepartmentById(departmentId);
        return "Department deleted successfully";
    }

    @PutMapping("/departments/{id}")
    public Department updateDepartment(@PathVariable("id") Long departmentId, @RequestBody Department department) {
        LOGGER.info("Inside updateDepartment of Department controller");
        return departmentService.updateDepartment(departmentId, department);
    }

    @GetMapping("/department/name/{name}")
    public Department fetchDepartmentByName(@PathVariable("name") String departmentName) {
        LOGGER.info("Inside fetchDepartmentByName of Department controller");
        return departmentService.fetchDepartmentByName(departmentName);
    }

}
