# 지연 초기화 / 즉시 초기화(Eager)

- Spring의 기본 초기화는 **즉시 초기화**이다.
- Bean을 사용하지않아도 자동으로 초기화된다.
    
    ```java
    @Component
    class ClassA {
    
    }
    
    @Component
    class ClassB {
        private ClassA classA;
        
        public ClassB(ClassA classA) {
            // Logic
            System.out.println("Some Initialization logic");
            this.classA = classA;
        }
    }
    
    @Configuration
    @ComponentScan
    public class LazyInitializationLauncherApplication {
        public static void main(String[] args) {
            try (var context = new AnnotationConfigApplicationContext(LazyInitializationLauncherApplication.class)) {
            
            }
        }
    }
    
    // Output
    // "Some Initialization logic"
    ```
    
- 이를 방지하고 싶다면 `@Lazy` Annotation을 추가해주면 된다.
- 이 경우 `ClassB`를 사용하는 시점에 초기화가 된다.
    
    ```java
    @Component
    @Lazy
    class ClassB {
        private ClassA classA;
        
        public ClassB(ClassA classA) {
            // Logic
            System.out.println("Some Initialization logic");
            this.classA = classA;
        }
    }
    
    @Configuration
    @ComponentScan
    public class LazyInitializationLauncherApplication {
        public static void main(String[] args) {
            try (var context = new AnnotationConfigApplicationContext(LazyInitializationLauncherApplication.class)) {
                System.out.println(context.getBean(ClassB.class));
            }
        }
    }
    
    // Output
    // "Some Initialization logic"
    ```
    
- 대부분의 경우 **Eager initialization**을 추천한다.
    - 오류가 있을 경우 즉시 초기화를 사용한다면 App이 시작될 때 오류를 바로 확인할 수 있기 때문이다.
- Lazy initialization은 자주 사용되지 않는다.
    - App이 시작될 때 오류를 발견하기 어렵기 때문이다.
- Lazy initialization의 특징
    - Bean이 초기화 될 때까지 메모리를 사용하지 않으므로 메모리 절약이 가능하다.
    - Component와 Bean이 있는 곳에 사용할 수 있다.
    - 지연-해결 프록시가 실제 의존성 대신 주입된다.
    - Configuration class에서도 사용할 수 있다.

<br><br>

# Bean Scope(SingleTone, Prototype)

- **Singletone Class**의 경우 여러 번 출력시에 동일한 Hash값을 가진다.
    - Singletone의 경우 Spring IoC 컨테이너당 객체 인스터스가 하나이다.
    
    ```java
    @Component
    class NormalClass {
    
    }
    
    System.out.println(context.getBean(NormalClass.class));
    System.out.println(context.getBean(NormalClass.class));
    
    /* Output
    com.dna.praticespringdna.examples.e1.NormalClass@662b4c69
    com.dna.praticespringdna.examples.e1.NormalClass@662b4c69
    */
    ```
    
- **Prototype Class**의 경우 여러번 출력시에 각자 다른 Hash값을 가진다.
    - 프로토타입의 경우 Bean을 참조할 때마다 매번 다른 인스턴스를 생성한다.
    
    ```java
    @Scope(value= ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Component
    class PrototypeClass {
    
    }
    
    /* Output
    com.dna.praticespringdna.examples.e1.PrototypeClass@fa49800
    com.dna.praticespringdna.examples.e1.PrototypeClass@71238fc2
    com.dna.praticespringdna.examples.e1.PrototypeClass@2a54a73f
    */
    ```
    
- 기본적으로 Spring에서 생성되는 모든 Bean은 **Singletone Class**이다.
- Web-aware
    - HTTP Request Scope의 경우 요청마다 인스턴스가 하나씩 생성된다.
    - HTTP Session Scope의 경우 유저 HTTP Session마다 인스턴스가 하나씩 생성된다.
    - HTTP Application Scope의 경우 웹 어플리케이션 전체에 객체 인스턴스가 하나이다.
    - HTTP WebSocket Scope의 경우 웹소켓 인스턴스 당 객체 인스턴스가 하나이다.
- **Java Singletone VS Spring Singletone**
    - 스프링 싱글톤의 경우 Spring IoC 컨테이너 하나에 객체 인스턴스가 하나이다.
    - 자바 싱글톤은 자바 가상머신(JVM) 하나당 객체 인스턴스가 하나이다.
    - JVM에 Spring IoC 컨테이너를 하나만 실행한다면 스프링 싱글톤과 자바 싱글톤은 같은 의미일 수 있다.
    - JVM에 여러 개의 Spring IoC 컨테이너를 실행한다면 의미가 달라진다.

<br><br>

# @PostConstruct와 @PreDestroy

`@PostConstruct` : 의존성을 연결하는 순간 초기화 작업이 필요한 경우 사용

`@PreDestroy` : Context를 없애기 전에 해야할 작업이 있다면 사용

```java
@Component
class SomeClass {
    private SomeDependency someDependency;
    
    public SomeClass(SomeDependency someDependency) {
        super();
        this.someDependency = someDependency;
        System.out.println("All dependencies are ready!");
    }
    
    **@PostConstruct
    public void initialize() {
        someDependency.getReady();
    }
    
    @PreDestroy
    public void cleanup() {
        System.out.println("Cleanup");
    }**
}

@Component
class SomeDependency {
    
    public void getReady() {
        System.out.println("Some logic using SomeDependency");
    }
}

@Configuration
@ComponentScan
public class PrePostAnnotationsContextLauncherApplication {
    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(PrePostAnnotationsContextLauncherApplication.class)) {
            System.out.println("Destroy Context");
        }
    }
}

/* Output
All dependencies are ready!
Some logic using SomeDependency
Destroy Context
Cleanup
*/
```

<br><br>

# CDI Annotation

- **Jakarta EE** : PC에서 동작하는 표준 플랫폼인 Java SE에 부가하여, 웹 애플리케이션 서버에서 동작하는 장애복구 및 분산 멀티티어를 제공하는 자바 소프트웨어의 기능을 추가한 서버를 위한 플랫폼
- **CDI Annotation** : Jakarta EE에서 지원하는 Context Dependency injection API이다.
- API 설치를 위해서 `pom.xml` 파일 수정이 필요하다.

```xml
**<dependency>
			<groupId>jakarta.inject</groupId>
			<artifactId>jakarta.inject-api</artifactId>
			<version>2.0.1</version>
</dependency>**

// 위치는 spring.stater 밑에 추가
// 코드 작성 후 **Ctrl+Shift+O**로 import 수정 필요
```

- `**@Named**` : Spring의 `@Component`와 대치
- `**@Inject**` : Spring의 `@Autowired`와 대치

```java
**@Named**
class BusinessService {
    DataService dataService;
    
    // @Autowired
    **@Inject**
    public void setDataService(DataService dataService) {
        System.out.println("Setter Injection");
        this.dataService = dataService;
    }
    
    public DataService getDataService() {
        return dataService;
    }
}
```

<br><br>

# XML Comfiguration

- Java Configuration으로 구현할 수 있는 것은 XML Configuration으로도 가능하다.
    
    ```java
    public class XMLConfigurationContextLauncherApplication {
        public static void main(String[] args) {
            try (var context = new **ClassPathXmlApplicationContext**("contextConfiguration.xml")) {
                Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);
                System.out.println(context.getBean("name"));
                System.out.println(context.getBean("age"));
                System.out.println("============== run ===============");
                context.getBean(GameRunner.class).run();
            }
        }
    }
    ```
    
    - 원래 사용하던 `AnnotationConfigApplicationContext`가 아닌`**ClassPathXmlApplicationContext**`를 사용하였다.
    - `context`를 이용해 Bean을 사용하는 방식은 같다.
    
    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd"> <!-- bean definitions here -->
    
        <bean id="name" class="java.lang.String">
            <constructor-arg value="BSH"/>
        </bean>
    
        <bean id="age" class="java.lang.Integer">
            <constructor-arg value="25"/>
        </bean>
    
        <!--    <context:component-scan base-package="com.dna.praticespringdna.game"/> -->
        <bean id="game" class="com.dna.praticespringdna.game.PackManGame"/>
        <bean id="gameRunner"
              class="com.dna.praticespringdna.game.GameRunner">
            <constructor-arg ref="game"/>
        </bean>
    
    </beans>
    ```
    
    - 해당 xml Configuration에는 `name, age, game, gameRunner` Bean이 선언되어 있다.
    - `game`의 경우 **game 패키지에 있던 Bean을 가져와 사용**하였다.
    - `gameRunner`의 경우 **생성자에 인자가 들어가기 때문에 ref를 이용**하여 `game`을 전달하였다.
- 오래된 프로젝트 같은 경우 XML을 사용하는 경우가 있기 때문에 알아두는 것이 좋다.
- 둘 중 무엇을 사용하느냐보다 전체 프로젝트에서 하나의 방식을 일관적으로 사용하는 것이 중요하다. → 섞어 쓰지 마라.

<br><br>

# Spring Stereotype Annotations - @Component & more..

- `@Component` : **Generic annotation**이며 모든 클래스에 적용이 가능하다.
- Specializations of Component
    - `@Service` : Annotation한 클래스에 비즈니스 논리가 있음을 나타낸다.
    - `@Controller` : 웹 컨트롤러와 같이 컨트롤러 클래스인 경우에 사용한다. - ex) 웹 어플리케이션과 REST API에서의 컨트롤러
    - `@Repository` : Bean이 데이터베이스와 통신하는 경우, 데이터를 저장하거나 검색하거나 조작하는 경우 사용한다.
- 최대한 구체적인 Annotation을 사용하는 것을 추천한다.
    - 구체적인 Annotation을 사용하면 프레임워크에 자신이 의도했던 바를 더 자세하게 나타낼 수 있다.
    - 나중에 AOP(관점 지향 프로그래밍)을 사용하여 Annotation을 감지하고 그 위에 부가적인 동작을 추가할 수 있다.
        - ex) `@Repository` Annotation이 있다면 스프링 프레임워크에서 자동으로 JDBC 예외 변환 기능에 연결한다.
