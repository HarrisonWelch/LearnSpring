package com.harrison.spring.data.jpa.tutorial.repository;

import com.harrison.spring.data.jpa.tutorial.entity.Course;
import com.harrison.spring.data.jpa.tutorial.entity.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    public void printCourses() {
        List<Course> courses = courseRepository.findAll();
        System.out.println("courses = " + courses);
    }

    @Test
    public void saveCourseWithTeacher() {
        Teacher teacher = Teacher
                .builder()
                .firstName("Priyanka")
                .lastName("Singh")
                .build();

        Course course = Course
                .builder()
                .title("Python")
                .credit(6)
                .teacher(teacher)
                .build();

        courseRepository.save(course);
    }

    @Test
    public void findAllPagination() {
        Pageable firstPageWithThreeRecords = PageRequest.of(0,3);
        Pageable secondPageWithTwoRecords = PageRequest.of(1,2);

        {
            List<Course> courses = courseRepository.findAll(firstPageWithThreeRecords).getContent();
            System.out.println("courses = " + courses);

            long totalElements = courseRepository.findAll(firstPageWithThreeRecords).getTotalElements();
            System.out.println("totalElements = " + totalElements);

            long totalPages = courseRepository.findAll(firstPageWithThreeRecords).getTotalPages();
            System.out.println("totalPages = " + totalPages);
        }

        // ----

        {
            List<Course> courses = courseRepository.findAll(secondPageWithTwoRecords).getContent();
            System.out.println("courses = " + courses);

            long totalElements = courseRepository.findAll(secondPageWithTwoRecords).getTotalElements();
            System.out.println("totalElements = " + totalElements);

            long totalPages = courseRepository.findAll(secondPageWithTwoRecords).getTotalPages();
            System.out.println("totalPages = " + totalPages);
        }

    }

    @Test
    public void findAllSorting() {
        Pageable sortByTitle =
                PageRequest.of(
                        0,
                        2,
                        Sort.by("title")
                );
        Pageable sortByCreditDesc =
                PageRequest.of(
                        0,
                        2,
                        Sort.by("credit").descending()
                );
        Pageable sortByTitleAndCreditDesc =
                PageRequest.of(
                        0,
                        2,
                        Sort.by("title")
                            .descending()
                            .and(Sort.by("credit"))
                );

        List<Course> courses = courseRepository.findAll(sortByTitle).getContent();
        System.out.println("courses = " + courses);
    }

    @Test
    public void findByTitleContaining() {
        Pageable firstPageTenRecords =
                PageRequest.of(0, 10);

        List<Course> courses = courseRepository.findByTitleContaining(
                "D",
                firstPageTenRecords).getContent();

        System.out.println("courses = " + courses);
    }

}