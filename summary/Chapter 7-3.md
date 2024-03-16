# REST API HATEOAS
```xml
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-hateoas</artifactId>
		</dependency>
```

- 애플리케이션의 상태를 나타내는 엔진으로 사용하는 하이퍼미디어
- HATEOAS는 Hypermedia as the engine of Application State의 약자
- HATEOAS가 던지는 질문은 `"REST API를 향상하여 데이터를 반환할 뿐만 아니라 리소스에 관한 작업을 수행하는 방법의 정보를 제공하면 어떨까?"`이다.
- **HAL** → 리소스와 하이퍼링크를 연결하는 방법에 대한 표준
    - `_links`를 생성하여 그 안에 링크 혹은 href를 입력한다.

```java
    @GetMapping("/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id) {
        User user = userDaoService.findOne(id);
        
        if (user == null)
            throw new UserNotFoundExcepttion("id: " + id);
        
        EntityModel<User> entityModel = EntityModel.of(user);
        WebMvcLinkBuilder link = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(
                        this.getClass()).retrieveAllUsers()
        );
        entityModel.add(link.withRel("all-users"));
        
        return entityModel;
    }
```

- `users/{id}`에서 모든 사용자를 보여주는 API의 후속 링크를 같이 반환하고 싶다면?
- HATEOAS를 사용하면 된다.
    - `EntityModel`
        - 링크를 추가하려면 우선 Entity를 EntityModel에 래핑한다.
        - `EntityModel.of(user);`
    - `WebMvcLinkBuilder`
        - 이제 EntityModel에서 반환할 URL을 전달해야한다.
        - `WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(
                                this.getClass()).retrieveAllUsers()
                );`
        - `entityModel.add(link.withRel("all-users"));`
    - 여기서 `Rel`은 url에 붙는 명칭이라고 생각하면 된다.

<br><br>

# REST API 정적 필터링
- **Serialization**(직렬화) → 객체를 스트림으로 전환하는 프로세스
    - ex) `EntityModel`을 반환하거나 `List<User>`를 반환.
    - 이것을 JSON이나 XML로 전환하는 작업을 **직렬화**
    - 가장 인기가 많은 자바의 JSON 직렬화 프레임워크는 **Jackson**
- 때로는 구조 반환 형식을 커스터마이징 하고싶을 때가 있다.
    - `@JSONProperty` 필드 이름을 커스터마이징 하고 싶은 경우 사용
    
    ```java
        @JsonProperty("user_name")
        private String name;
    ```
    
- 때로는 선택한 필드만 반환하고 싶을 수도 있다. → **Filtering**
    - **Static Filtering → 모든 api에 대해 적용**
        
        ```java
            private String field1;
            @JsonIgnore
            private String field2;
            private String field3;
        ```
        
        ```java
        @JsonIgnoreProperties("field1")
        public class SomeBean {
            private String field1;
            private String field2;
            private String field3;
           .
           .
           .
        }
        ```
        
    - **Dynamic Filtering → api 별로 다른 제한 적용**
        
        ```java
        @JsonFilter("SomeBeanFilter")
        public class SomeBean {
        .
        .
        }
        ```
        
        ```java
        @GetMapping("/filtering")
        public MappingJacksonValue filtering() {
                SomeBean someBean = new SomeBean("value1", "value2", "value3");
                
                **MappingJacksonValue** mappingJacksonValue = new MappingJacksonValue(someBean);
                **PropertyFilter** filter = SimpleBeanPropertyFilter.filterOutAllExcept("field1", "field3");
                **FilterProvider** filterProvider = new SimpleFilterProvider().addFilter("**SomeBeanFilter**", filter);
                
                mappingJacksonValue.setFilters(filterProvider);
                
                return mappingJacksonValue;
        }
        ```
        
<br><br>

# Spring Boot Actuator
```xml
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-actuator</artifactId>
		</dependency>
```

- **Spring Boot Actuator**는 Spring Boot에 운영 가능한 수준의 기능들을 제공
- 다양한 엔드포인트 제공
    - **beans** → 빈 전체 목록 제공
    - **health** → 애플리케이션 상태 정보 제공
    - **metrics** → 관련 메트릭 제공 → **http.server.requests**
    - **mappings** → 요청 매핑에 관한 세부 정보 제공

<br><br>

# Spring Boot HAL Explorer
```xml
		<dependency>
			<groupId>org.springframework.data</groupId>
			<artifactId>spring-data-rest-hal-explorer</artifactId>
		</dependency>
```

- **HAL Explorer**는 HAL을 이용하는 RESTful 하이퍼미디어 API를 탐색하는 API 탐색기이다.
- http://localhost:8080/explorer 로 접속하면 된다.
    
    - HTTP REQUEST 순서대로 **GET, POST, PUT, PATCH, DELETE**이다.

<br><br>

# 일대다 관계
```java
public class User {
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Post> posts;
   .
   .
}
```

```java
@Entity
public class Post {
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;
    .
    .
}
```

- 하나의 유저가 여러 `Post`를 가질 수 있으므로 **일대다관계**이다.
- 이를 구현하기 위해 `User`에서 `Post`를 불러와 `@OneToMany`를 적용해주고, `Post`에서는 `User`를 불러와 `@ManyToOne`을 적용해준다.
- 이 때, `mappedBy`는 `Post`에서 불린 `User`의 필드 이름이다.
- 또한, `fetch` 속성은 관계가 지연 로딩되는지 아니면 즉시 로딩되는지를 결정한다.
    - `ManyToOne`에서 `fetch`의 기본값은 `EAGER`이다.
    - 게시물 세부 정보와 함께 가져오도록 요청하면 사용자 세부 정보도 같이 가져오게 된다.
- 하지만 게시물을 가져올 때 게시물에 연결된 사용자 세부 정보도 가져오려는 것은 아니기 때문에 `FetchType.LAZY`를 적용한다.

<br><br>

# MySQL 연결
- MySQL을 Docker 컨테이너로 실행
    
    ```java
    docker run --detach 
    --env MYSQL_ROOT_PASSWORD=dummy 
    --env MYSQL_USER=admin 
    --env MYSQL_PASSWORD=dummy 
    --env MYSQL_DATABASE=spring-database 
    --name mysql 
    --publish 3306:3306 
    mysql:8-oracle
    ```
    
- mysqlsh 명령어
    
    ```java
    mysqlsh
    \connect social-media-user@localhost:3306
    \sql
    use social-media-database
    select * from user_details;
    select * from post;
    \quit
    ```
    
- `application.properties`
    
    ```java
    spring.jpa.show-sql=true
    spring.datasource.url=jdbc:mysql://localhost:3306/social-media-database
    spring.datasource.username=social-media-user
    spring.datasource.password=dummypassword
    spring.jpa.hibernate.ddl-auto=update
    // h2의 경우 auto-configuration이 직접 엔터티를 살펴보고 테이블을 생성한다.
    // MySQL 같은 외부 DB는 그렇지 않으므로 spring.jpa.hibernate.ddl-auto=update이 필요하다.
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
    // 해당 설정은 hibernate.dialect를 쓰는 경우 수동 설정이 필요 없다. -> 지워도 된다.
    // MySQLDialect does not need to be specified explicitly using 'hibernate.dialect' (remove the property setting and it will be selected by default)
    ```
    
- `pom.xml`
    
    ```xml
    <dependency>
    	<groupId>com.mysql</groupId>
    	<artifactId>mysql-connector-j</artifactId>
    </dependency>
    ```
    
- `data.sql`의 경우 인메모리 데이터베이스에서 실행되는 것이므로 연결 후 바로 실행한다면 어떠한 데이터도 들어있지 않는 것을 확인할 수 있다.

<br><br>

# Spring Security
`pom.xml`

```xml
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>
```

`application.properties`

```java
spring.security.user.name=admin
spring.security.user.password=dummy
```

- Spring Security의 원리
    - 요청을 보낼 때마다 Spring Security가 해당 요청을 가로챈다.
    - ex) localhost:8080/users로 요청을 보내면, Spring Security가 이 요청을 가로채서 일련의 필터를 실행한다.
    - 이런 일련의 필터를 필터 체인이라고 하고, 필터 체인에서 확인이 여러 차례 이루어진다.
        1.  모든 요청이 인증되어야 한다, 모든 요청에 자격 증명이 첨부되어야한다.
        2. 요청이 인증되지 않았다면, 자격 증명이 없거나 인증되지 않았다면, 기본값으로 웹 페이지가 나타납니다
        3. 필터 체인에서 수행되는 것 중 하나가 CSRF라는 것을 확인할 수 있게 설정하는 것. 이것은 POST와 PUT 요청에 영향을 주게 된다.
    
- Filter Chain Customizing → 기존 필터 체인에서 수정해야 할 게 두 가지 있다.
    1. HTTP 인증을 사용할 때는 localhost users를 입력하게 되면 사용자 자격 증명을 묻는 팝업창이 떠야 한다.
    2. CSRF를 해제해서 POST 요청을 보낼 수 있게 해야 한다.
