# redirect:”URL”

```java
@RequestMapping("delete-todo")
    public String deleteTodo(@RequestParam int id) {
        todoService.deleteById(id);
        return "redirect:list-todos";
}
```

- redirect를 하게되면 url로 들어가기 전에 변수들을 갱신해주거나 복원해야한다.
- 이런 경우 로직이 중복되는 경우가 많으므로 `redirect:"jsp"`를 통해 이전 페이지로 돌아갈 수 있게 한다.

<br><br>

# Validation with Spring Boot

- HTML로 구현한 검증이나 JavaScript로 구현한 검증은 해커가 쉽게 건너뛸 수 있다. 그래서 항상 최선의 방식은 서버 측 검증인 것이다. Spring Boot와 Spring MVC를 사용하면 쉽게 검증을 구현할 수 있다.
1. `spring-boot-starter-validation` 임포트
2. 커맨드 빈 또는 양식 보조 객체 개념 사용 → 양방향 바인딩 구현
    1. RequestParam을 이용해 값을 받지 않고 Bean(해당 코드에서는 Todo)를 method 매개변수로 추가해준다. → 이 때 ModelMap이 첫번째 파라미터가 되어야 한다.(순서)
    2. 또한 값을 추가하기 위해 인자를 전달할 때, `RequestParam`을 통해 받았던 매개변수가 아닌`todo.get변수`를 이용한다.
    3. jsp에서 양식 보조 객체를 설정하기 위해 양식 태그 라이브러리를 사용해야 한다.
        
        [43. spring-form JSP Tag Library](https://docs.spring.io/spring-framework/docs/4.2.x/spring-framework-reference/html/spring-form-tld.html)
        
    4. form:from 태그에서 `todo`를 매핑해준다.
3. Bean에 검증을 추가
    1. 최소 길이를 설정하고 싶다면 필드에 `@Size` 어노테이션을 추가할 수 있다.
    2. 또한 검증을 트리거하기 위해 Controller method의 매개변수에 `@Valid` 어노테이션을 추가해주어야 한다.
    3. 이를 통해 바인딩이 이루어지기 전에 Todo Bean을 검증하게 된다.
4. 검증 오류를 뷰에 표시
    1. method의 매개변수에 `BindingResult` 속성을 추가하여 `result.hasErrors()`를 통해 예외처리를 진행할 수 있다.
    2. jsp에서는 `form:errors`를 통해 오류를 표시해줄 수 있다.

<br><br>

# 네비게이션 바

```xml
<nav class="navbar navbar-expand-md navbar-light bg-light mb-3 p-1">
	<a class="navbar-brand m-1" href="list-todos">DNA</a>
		<div class="collapse navbar-collapse">
			<ul class="navbar-nav">
				<li class="nav-item"><a class="nav-link" href="/">Home</a></li>
	      <li class="nav-item"><a class="nav-link" href="/list-todos">Todos</a></li>
      </ul>
    </div>
      <ul class="navbar-nav">
				<li class="nav-item"><a class="nav-link" href="/logout">Logout</a></li>
      </ul>
</nav>
```

<br><br>

# Spring Security

- ID : user
- Using generated security password: 개발자용 패스워드

```jsx
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

- `InMemoryUserDetailsManager`
- `UserDetails`
- `PasswordEncoder passwordEncoder()`
- `CryptPasswordEncoder();`
- `SecurityContextHolder`

# Security로부터 정보얻기

---

```java
private String getLoggedinUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        return authentication.getName();
}
```

<br><br>

# Spring Data JPA와 H2 Database

- Spring Security와의 충돌해결
    - CSRF 비활성화
    - Frames
    - @SecurityFilterChain
- Table Mapping → `@Entity`
- `datq.sql` → `spring.jpa.defer-datasource-initialization=true`
- `spring.jpa.show-sql=true`

<br><br>

# docker - MySQL

`**install MySQL on docker**`

```
docker run --detach --env MYSQL_ROOT_PASSWORD=dummypassword --env MYSQL_USER=todos-user --env MYSQL_PASSWORD=dummytodos --env MYSQL_DATABASE=todos --name mysql --publish 3306:3306 mysql:8-oracle
```

`**pom.xml**`

```xml
<dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
</dependency>
```

`application.properties`

```xml
spring.datasource.url=jdbc:mysql://localhost:3306/todos
spring.datasource.username=todos-user
spring.datasource.password=dummytodos
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
```

`**mysqlsh**`
