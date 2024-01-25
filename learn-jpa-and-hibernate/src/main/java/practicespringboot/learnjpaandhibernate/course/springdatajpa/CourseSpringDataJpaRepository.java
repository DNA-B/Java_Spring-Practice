package practicespringboot.learnjpaandhibernate.course.springdatajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import practicespringboot.learnjpaandhibernate.course.Course;

import java.util.List;

public interface CourseSpringDataJpaRepository extends JpaRepository<Course, Long> {
    List<Course> findByAuthor(String author); // Custom Function
    List<Course> findByName(String name);
    List<Course> findByNameLike(String name);
    
}
