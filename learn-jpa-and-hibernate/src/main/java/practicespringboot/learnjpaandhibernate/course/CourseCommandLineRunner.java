package practicespringboot.learnjpaandhibernate.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import practicespringboot.learnjpaandhibernate.course.jdbc.CourseJdbcRepository;
import practicespringboot.learnjpaandhibernate.course.jpa.CourseJpaRepository;
import practicespringboot.learnjpaandhibernate.course.springdatajpa.CourseSpringDataJpaRepository;

@Component
public class CourseCommandLineRunner implements CommandLineRunner {
    @Autowired
    private CourseJdbcRepository repository1;
    
    @Autowired
    private CourseJpaRepository repository2;
    
    @Autowired
    private CourseSpringDataJpaRepository repository3;
    
    @Override
    public void run(String... args) throws Exception {
        repository2.insert(new Course(1, "Learn AWS JPA", "DNA"));
        repository2.insert(new Course(2, "Learn Azure JPA", "DNA"));
        repository2.insert(new Course(3, "Learn Spring JPA", "DNA"));
        repository3.save(new Course(4, "Learn Spring Data JPA", "DNA"));
        
        repository2.deleteById(1);
        repository3.deleteById(2L);
        
        System.out.println(repository1.findById(3));
        System.out.println(repository2.findById(3));
        System.out.println(repository3.findById(3L));
        
        System.out.println(repository3.findAll());
        System.out.println(repository3.count());
        System.out.println(repository3.findByAuthor("DNA"));
        System.out.println(repository3.findByName("Learn Spring Data JPA"));
        System.out.println(repository3.findByNameLike("Learn%"));
    }
}
