## Main Class의 Configuration

- 이제 최적화 refactoring을 해보자
- 지금까지는 Configuration 클래스를 만들고 그 곳에 많은 Bean 코드를 작성하였다.
- Configuration의 역할을 Main Class가 할 수 있게 Annotation을 붙여주면 코드의 양을 줄일 수 있다.
    
    ```java
    @Configuration
    @ComponentScan("com.dna.praticespringdna.game")
    public class GamingAppLauncherApplication {
        public static void main(String[] args) {
            try (var context = new AnnotationConfigApplicationContext(GamingAppLauncherApplication.class)) {
                context.getBean(GamingConsole.class).up();
                System.out.println("----------------------------------------------------");
                context.getBean(GameRunner.class).run();
            }
        }
    }
    ```
    
<br><br>

## Bean의 자동 생성 - **Component**

- 지금까지는 수동으로 Bean을 생성했었다.
- 이를 Spring이 할 수 있게 만들기 위해 미리 만들어둔 Class에 `@Component` Annotation을 붙여준다.
- 이를 통해 Spring이 해당 클래스를 Bean으로 만들 수 있게 된다.
    
    ```java
    @Component
    public class PackManGame implements GamingConsole{
        public PackManGame(){}
        
        public void up() {
            System.out.println("Go up");
        }
        
        public void down() {
            System.out.println("Go down");
        }
        
        public void left() {
            System.out.println("Go back");
        }
        
        public void right() {
            System.out.println("Go ahead");
        }
    }
    ```
    
<br><br>

## Main Class에서의 Bean 탐색

- Component를 부여하기만해서는 Spring이 사용할 수 없다.
- 이를 위해 Configuration Class에 `@ComponentScan` Annotation을 적용해준다.

```java
@Configuration
@ComponentScan("com.dna.praticespringdna.game")
public class GamingAppLauncherApplication {
    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(GamingAppLauncherApplication.class)) {
            context.getBean(GamingConsole.class).up();
            context.getBean(GameRunner.class).run();
        }
    }
}
```

<br><br>

## 여러 Component 사이의 충돌

- GameRunner의 경우 Mario, PackMan, SuperContra 총 3개의 Component들을 이용할 수 있기 때문에 우선순위를 정할 수 없다.
- 이를 방지하기 위해 Annotation을 사용할 수 있다.
    - **`@Primary`**
    - **`@Qualifier`**

```java
@Component
@Primary
public class MarioGame implements GamingConsole

```

```java
@Component
@Qualifier("SuperContraGameQualifier")
public class SuperContraGame implements GamingConsole

@Component
public class GameRunner {
    private GamingConsole game;
    
    public GameRunner(@Qualifier("SuperContraGameQualifier") GamingConsole game) {
        this.game = game;
    }
}
```

<br><br>

## Primary VS Qualifier

- `**@Primary**` : 여러 후보들이 있을 때 우선권을 누구에게 줄 것인가.
- `**@Qualifier**` : 특정 Bean을 사용해야만 할 때 부여
- 해당 코드에서 `ComplexAlgorithm`은 특정 Bean을 사용하지 않아 primary가 부여된 `QuickSort`를 사용하게 된다.
- `AnotherComplexAlgorithm`은 `RadixSort`를 사용해야하므로 Qualifier가 부여된 `RadixSort`를 사용하였다.
- 이를 통해 `@Qualifier`가 `@Primary`보다 우선순위가 높다는 것을 알 수 있다.
- Bean을 사용하는 곳의 입장에서 생각하자.

<br><br>

## Dependency Injection

<aside>
💡 Spring 팀은 **Constructor-based 방식을 추천**한다.
모든 초기화가 `super()` 메소드에서 발생하기 때문이다.
또한 `@Autowired`를 사용할 필요도 없다.

</aside>

- **Autowired**
    - 하나의 클래스에서 Component(Bean)를 사용하려면 Dependency Injection이 필요하다. 이를 위해 `@Autowired` Annotation이 필요하다.
    - 만약 해당 Annotation이 없다면 dependency 객체들은 `null`값을 갖는다.
        
        ```java
        @Component
        class YourBusinessClass {
            Dependency1 dependency1;
            Dependency2 dependency2;
        }
        
        @Component
        class Dependency1
        
        @Component
        class Dependency2
        
        // Output
        // Using null and null
        ```
        
- **Constructor-based**
    - 생성자에서 `@Autowired`를 사용하는 방법.
    
    ```java
    @Component
    class YourBusinessClass {
    		@Autowired
        public YourBusinessClass(Dependency1 dependency1, Dependency2 dependency2) {
            this.dependency1 = dependency1;
            this.dependency2 = dependency2;
        }
    }
    
    @Component
    class Dependency1
    @Component
    class Dependency2
    ```
    
    - Constructor-based의 경우 `@Autowired`를 필수로 요구하지 않는다. → `@Autowired`가 없어도 된다.
    - 3가지의 방법 모두 Spring이 자동으로 생성자를 사용해서 객체를 만든다.
- **Setter-based**
    - Setter method에서 `@Autowired`를 사용하는 방법.
    
    ```java
    @Component
    class YourBusinessClass {
        @Autowired
        public void setDependency1(Dependency1 dependency1) {
            this.dependency1 = dependency1;
        }
    
        @Autowired
        public void setDependency2(Dependency2 dependency2) {
            this.dependency2 = dependency2;
        }
    }
    
    @Component
    class Dependency1
    @Component
    class Dependency2
    ```
    
- **Field**
    - field에서 `@Autowired`를 사용하는 방법.
        
        ```java
        @Component
        class YourBusinessClass {
            @Autowired
            Dependency1 dependency1;
            @Autowired
            Dependency2 dependency2;
        }
        
        @Component
        class Dependency1
        @Component
        class Dependency2
        ```
        
<br><br>

## Spring Framework


- Spring에 의해 관리되는 instance of class
- Component Scan : Spring은 어떻게 Component를 검색하는가?
    - 지정한 패키지를 scan한다. `@ComponentScan("com.dna.praticespringdna.examples.a1")`
    - 만약 지정하지 않았다면 현재 속해있는 패키지를 scan한다.
- Dependency Injection : bean과 그것들의 의존성을 식별하고 모두 wiring하는 프로세스
- IOC : Inversion of Control
    - 원래는 프로그래머가 명시적으로 코드를 작성해서 의존성을 제어했었다.
    - 이제는 프로그래머가 Component를 정의하고 이를 wiring하는 작업은 Spring이 수행하고 있다. → 이를 제어 반전이라고 한다.
- `@Component` VS `@Bean`
    - **자체 애플리케이션 코드용**으로 Bean을 인스턴스화할 때 `@Component`가 권장된다.
    - **Spring Security 설정** 같은 **서드 파티 라이브러리용**으로 Bean을 인스턴스화할 때는 `@Bean`이 권장된다.
- `super()`는 언제 사용할까?
    - 자바 컴파일러는 부모 클래스를 상속한 자식 클래스의 생성자에 자동으로 `super()` 메소드를 추가해준다. 그러나 무조건적인 자동 생성이 일어나는 것은 아니다.
    - 부모 클래스의 생성자를 오버로딩하게 되면 컴파일러가 자동으로 추가해주지 않는다.
    - 즉, 부모 클래스의 생성자를 오버로딩하는 경우 `super()`를 명시적으로 추가하는 것이 좋다.
