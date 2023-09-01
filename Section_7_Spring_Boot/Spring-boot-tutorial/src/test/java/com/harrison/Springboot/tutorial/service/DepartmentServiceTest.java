package com.harrison.Springboot.tutorial.service;

import com.harrison.Springboot.tutorial.entity.Department;
import com.harrison.Springboot.tutorial.repository.DepartmentRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DepartmentServiceTest {

    @Autowired
    private DepartmentService departmentService;

    @MockBean // From Mockito
    private DepartmentRepository departmentRepository;

//    @BeforeAll // Before all test cases, once (ex 5 cases, 1 call here)
    @BeforeEach // Method called for each test case in this file (ex 5 cases, 5 calls)
    void setUp() {
        Department department = Department.builder()
                                .departmentName("IT")
                                .departmentAddress("Ahmedabad")
                                .departmentCode("IT-06")
                                .departmentID(1L)
                                .build();

        // Mock bean and mock method always available
        Mockito.when(departmentRepository.findByDepartmentNameIgnoreCase("IT"))
                .thenReturn(department);
    }

    @Test
    @DisplayName("Get Data based on Valid Department Name") // Gives it a name
//    @Disabled // Disables a test case
    public void whenValidDepartmentName_thenDepartmentShouldFound() { // Long name is ok, needs to be very unique

        // Need to get department
        String departmentName = "IT";
        Department found = departmentService.fetchDepartmentByName(departmentName);

        assertEquals(departmentName, found.getDepartmentName());

        // Need positive and negative cases
        // We need to mock the layers

    }
}