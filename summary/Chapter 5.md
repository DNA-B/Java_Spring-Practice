# 개요

- 필요 종속성
    - Spring Web
    - Spring Data JDBC
    - Spring Data JPA
    - H2 Database → 적은 용량에 빠른 인메모리 데이터베이스

<br><br>

# h2 Database

- H2 Console 사용
    - `application.properties` → `spring.h2.console.enabled=true`
- 접속 URL → **`localhost:8080/h2-console`
- 매번 JDBC URL이 변경된다. → 정적 URL 필요
    - `application.properties` → `spring.datasource.url="url"`
- TABLE 생성
    - **resources** 경로에 `schema.sql`을 생성한다.
    
    ```sql
    create table course
    (
        id bigint not null,
        name varchar(255) not null,
        author varchar(255) not null,
        primary key (id)
    );
    ```
    
<br><br>

# Spring JDBC

- H2 Console에서 query를 직접 입력하여 DB를 조작하기에는 불편한 감이 있다.
- JDBC는 Spring 내에서 query 조작을 가능케 해준다.
- Spring JDBC는 JDBC보다 더 적은 query 작성이 가능한 장점이 있다.
    
    ```java
    @Repository
    public class CourseJdbcRepository {
        @Autowired
        private JdbcTemplate springJdbcTemplate;
        private static String INSERT_QUERY = """
                        insert into course (id, name, author)
                        values(?, ?, ?);
                """;
        private static String DELETE_QUERY = """
                        delete from course where id=?;
                """;
        private static String SELECT_QUERY = """
                        select * from course where id=?;
                """;
        
        public void insert(Course course) {
            springJdbcTemplate.update(INSERT_QUERY, course.getId(), course.getName(), course.getAuthor());
        }
        
        public void deleteById(long id) {
            springJdbcTemplate.update(DELETE_QUERY, id);
        }
        
        public Course findById(long id) {
            // ResultSet -> Bean => Row Mapper
            return springJdbcTemplate.queryForObject(SELECT_QUERY, new BeanPropertyRowMapper<>(Course.class), id);
        }
    }
    ```
    
    - 클래스가 DB에 연결될 때는 보통 `@Repository` 를 사용한다.
    - `JdbcTemplate` 을 통해 query를 수행한다.
    - quary를 미리 작성해두고 함수에 전달하는 방식을 사용한다. → `"""`을 통해 텍스트 블록을 사용할 수 있다. →  query 형식 유지 가능
    - **INSERT -** `springjdbctemplate`의 `update` 함수를 통해 구현할 수 있다.
    - **DELETE -** `springjdbctemplate`의 `update` 함수를 통해 구현할 수 있다.
    - **SELECT -** `springjdbctemplate`의 `queryForObject` 함수를 통해 구현할 수 있다.

<br>

`Runner**`

```java
@Component
public class CourseCommandLineRunner implements CommandLineRunner {
    @Autowired
    private CourseJdbcRepository repository1;
    
    @Override
    public void run(String... args) throws Exception {
        repository1.insert(new Course(1, "Learn AWS JPA", "DNA"));
        repository1.insert(new Course(2, "Learn Azure JPA", "DNA"));
        repository1.insert(new Course(3, "Learn Spring JPA", "DNA"));
        
        repository1.deleteById(1);
        
        System.out.println(repository1.findById(3));
    }
}
```

- 구현한 JDBC repository는 Runner에서 실행시킨다.

<br><br>

# Table Class

```java
public class Course {
    private long id;
    private String name;
    private String author;
    
    public Course() {
    
    }
    
    public Course(long id, String name, String author) {
        super();
        this.id = id;
        this.name = name;
        this.author = author;
    }
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
```

- 해당 클래스를 이용한 작업을 위해서는 **생성자, getter, setter**가 필요하다.
- 이 방식을 사용하면  `select * from course where id=?;` 와 같이 query에 값을 직접 대입시켜줄 필요가 없어지고 `?`에 값을 전달할 수 있다.

<br><br>

# Spring JPA

```java
@Entity
public class Course {
    @Id
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "author")
    private String author;
```

- `@Entity` → Java Bean과 Table 사이에 Mapping을 생성한다. 이를 통해 값을 삽입, 검색하는 작업을 수행할 수 있다.
- `@Id` → Primary Key가 되는 필드는 `@Id` Annotation으로 명시해준다. 필드의 이름과 변수명이 같은 경우에는 생략이 가능하다.
- `@Column` → 기본 필드들은 `@Column`을 통해 명시해준다. 필드의 이름과 변수명이 같은 경우에는 생략이 가능하다. (이름이 name인 column에 매핑된다는 뜻)

```java
@Repository
@Transactional
public class CourseJpaRepository {
    @PersistenceContext
    EntityManager entityManager;
    
    public void insert(Course course) {
        entityManager.merge(course);
    }
    
    public Course findById(long id) {
        return entityManager.find(Course.class, id);
    }
    
    public void deleteById(long id) {
        Course course = entityManager.find(Course.class, id);
        entityManager.remove(course);
    }
}
```

- `EntityManager`를 활용하여 엔티티를 관리한다.
    - `@PersistenceContext` → `@Autowired`보다 더 구체적인 Annotation이다.
- 이미 테이블이 Mapping 되어있기 때문에 Course만 전달하여도 query의 수행이 가능하다.
- **INSERT -** `EntityManager`의 `merge`함수를 통해 구현할 수 있다.
- **DELETE -** `EntityManager`의 `find`함수를 통해 구현할 수 있다.
- **SELECT -** `EntityManager`의 `remove`함수를 통해 구현할 수 있다.
    - remove 수행 전에 find를 통해 entity를 얻어내야 한다.
- `@Transactional` → JPA로 query를 실행할 때마다 Transaction을 허용해야하는데, 이때 `@Transactional` 어노테이션이 필요하다.

<br>

`Runner**`

```java
@Component
public class CourseCommandLineRunner implements CommandLineRunner {    
    @Autowired
    private CourseJpaRepository repository2;
    
    @Override
    public void run(String... args) throws Exception {
        repository2.insert(new Course(1, "Learn AWS JPA", "DNA"));
        repository2.insert(new Course(2, "Learn Azure JPA", "DNA"));
        repository2.insert(new Course(3, "Learn Spring JPA", "DNA"));
        
        repository2.deleteById(1);
        
        System.out.println(repository2.findById(3));
    }
}
```

<aside>
💡  **`spring.jpa.show-sql=true`를 통해 수행된 query를 콘솔 창에서 확인할 수 있다.**

</aside>

<br><br>

# Spring Data JPA

- Spring JPA 자체로도 query를 신경쓰지 않아 매우 편리했지만 Spring Data JPA는 이를 더욱 더 간편하게 만들어준다. → EntityManager도 신경 쓰지 않아도 된다.

```java
public interface CourseSpringDataJpaRepository extends JpaRepository<Course, Long> {}
```

- Spring Data JPA는 interface이다.
- JpaRepository를 상속해야하고 `<관리할 Entity 이름, Id 타입>`이 요구된다.  

<br>

`Runner**`

```java
@Component
public class CourseCommandLineRunner implements CommandLineRunner {    
    @Autowired
    private CourseJpaRepository repository3;
    
    @Override
    public void run(String... args) throws Exception {
				repository3.save(new Course(1, "Learn AWS JPA", "DNA"));
        repository3.save(new Course(2, "Learn Azure JPA", "DNA"));
        repository3.save(new Course(3, "Learn Spring JPA", "DNA"));
        repository3.save(new Course(4, "Learn Spring Data JPA", "DNA"));
        
        repository3.deleteById(2L);
  
        System.out.println(repository3.findById(3L));
        
        System.out.println(repository3.findAll());
        System.out.println(repository3.count());
        System.out.println(repository3.findByAuthor("DNA"));
        System.out.println(repository3.findByName("Learn Spring Data JPA"));
        System.out.println(repository3.findByNameLike("Learn%"));
    }
}
```

- **INSERT -** `save`함수를 통해 구현할 수 있다.
- **DELETE -** `deleteBy`함수를 통해 구현할 수 있다.
- **SELECT -** `EntityManager`의 `findBy`함수를 통해 구현할 수 있다.
- 이 뿐만 아니라 `count, Like, existBy` 등등 여러 기능을 제공하고 있다.

<br>

`Custom**`

```java
public interface CourseSpringDataJpaRepository extends JpaRepository<Course, Long> {
    List<Course> findByAuthor(String author); // Custom Function
    List<Course> findByName(String name);
    List<Course> findByNameLike(String name);
}
```

- Spring Data JPA의 많은 장점 중 하나는 커스텀 메소드의 정의가 가능하다는 것이다.
- 위의 코드와 같이 커스텀 함수를 정의해 놓으면 Runner에서 바로 실행시킬 수 있다.
