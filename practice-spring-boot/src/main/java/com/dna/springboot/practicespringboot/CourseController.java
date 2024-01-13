package com.dna.springboot.practicespringboot;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/*
    http://localhost:8080/courses
    Course: id, name, author
*/

@RestController
public class CourseController {
    @RequestMapping("/courses")
    public List<Course> retrieveAllCourse() {
        return Arrays.asList(
                new Course(1, "Learn AWS", "DNA"),
                new Course(2, "Learn DevOps", "DNA"),
                new Course(3, "Learn Azure", "DNA"),
                new Course(4, "Learn GCP", "DNA")
        );
    }
}
