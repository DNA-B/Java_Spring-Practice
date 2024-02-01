# 개요

- Spring Boot 이전에는 프로젝트를 설정하는 것에 시간을 많이 소모했다.
    - `pom.xml` → 의존성 관리
    - `web.xml` → 웹 애플리케이션 설정
    - Spring Configuration
    - Logging, Error handling, etc….
- 이러한 어려움을 spring initializer를 사용해 아주 쉽게 프로젝트를 만들어 낼 수 있게 되었다.
    
    [](https://start.spring.io/)
    
- REST API를 간단하게 빌드할 수 있다.

<br><br>

# Spring Boot의 목표

- 가능한 애플리케이션을 빠르게 빌드할 수 있다.
    - Spring Initializer
    - Spring Boot Starter Projects
    - Spring Boot Auto Configuration
    - Spring Boot Devtools
- Production-Ready
    - Logging
    - 여러 환경에 맞는 다양한 설정
    - Monitoring (Spring Boot Actuator)
        
<br><br>

# Spring Boot Starter Project

- `spring-boot-starter-web`
    - Spring MVC를 사용해 애플리케이션, RESTful을 비롯한 웹을 빌드하는 스타터
    - tomcat, json, web, etc…
- Unit-Test
- JPA
- JDBC
- Security

<br><br>

# Spring Boot Auto Configuration

- Component Scan, DispatcherServlet, Data Sources, JSON Conversion 등의 설정이 필요하다.
- 이것을 간소화하기 위해 Auto Configuration을 사용한다.
    - 클래스 경로에 있는 framework에 따라 많은 것들을 자동 설정할 수 있다.
    - Spring Boot는 디폴트 자동 설정을 제공한다. 하지만 자체 설정을 통해 오버라이드 할 수도 있다.
    - `spring-boot-autoconfigure.jar`에서 확인할 수 있다.
    - application.properties에서 자체 설정을 할 수도 있다.
        - `logging.level.org.springframework=debug` → 디버깅 모드

<br><br>

# API 생성

```java
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
```

- `@RestController` → REST API를 생성하기 위해서는 `@RestController` Annotation이 필요하다.
- `@RequestMapping` → URL을 메소드에 Mapping 시키기 위해서는 `@RequestMapping` Annotation이 필요하다.
- Spring Boot를 통해 다른 것에 집중할 필요가 없어진다.
    - Spring beans나 xml을 설정하지 않아도 된다.

<br><br>

# Spring Boot DevTools

```xml
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
</dependency>
```

- `pom.xml`에 `spring-boot-devtools` 추가
- 이를 통해 프로그램을 재시작하지 않아도 알아서 re-build를 진행한다.
- 이를 통해 생산성을 증가 시킬 수 있다.

<br><br>

# 환경별 설정

`**application.properties**`

```java
logging.level.org.springframework=debug
spring.profiles.active=dev
```

**`application-dev.properties`**

```java
logging.level.org.springframework=trace

currency-service.url=http://dev.dna.com
currency-service.username=devUsername
currency-service.key=devKey
```

`**application-prod.properties**`

```java
logging.level.org.springframework=info
```

- 환경에 따라 로그 레벨을 다르게 하고 싶다면?
- application.properties를 `application-dev.properties, application-prod.properties`와 같이 나눈 후 아래 코드와 같이 설정을 구별할 수 있다.
- 이후 `application.properties`에서 `spring.profiles.active=dev`로 설정해주면 해당 설정이 적용된다.

<br><br>

# 로깅 레벨

- trace
- debug
- info
- warning
- error
- off

<br><br>

# Embedded Server

- `mvn clean install` 명령어를 터미널에서 실행함으로써 `jar` 파일을 만들 수 있다.
- 이제 `jar` 파일이 만들어진 경로에서 `java -jar 파일이름`으로 실행을하면 java를 설치한 이용자는 해당 프로그램을 실행할 수 있게 된다.
- 웹 서버의 설치 없이 자바를 설치하기만 하여도 프로그램을 배포할 수 있게 된다.

<br><br>

# Spring Actuator

```xml
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

- `pom.xml`에 `spring-starter-actuator`를 dependency에 추가해준다.

`**application.properties**`

```java
management.endpoints.web.exposure.include=health, metrics
```

- [localhost:8080/actuator를](http://localhost:8080/actuator를) 통해 monitoring을 할 수 있다.
