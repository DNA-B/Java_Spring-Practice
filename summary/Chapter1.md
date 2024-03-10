# 강한 결합

- MarioGame을 GameRunner 클래스에서 실행시키고 있었다.
새로운 게임인 PackManGame 클래스를 만들었다면 손수 GameRunner 클래스에서 게임을 변경해주어야 한다.
    
    ```java
    package com.dna.praticespringdna.game;
    
    public class GameRunner {
    		private MarioGame game;
    
        public GameRunner(MarioGame game) { // MarioGame을 PackManGame으로 변경해야 한다.
            this.game = game;
        }
        
        public void run() {
            System.out.println("Running game: " + this.game);
            game.up();
            game.down();
            game.left();
            game.right();
        }
    }
    ```
    
- ex) 차의 엔진  

<br><br>
  
# 느슨한 결합

- Interface를 도입하여 게임들이 해당 interface를 implement하게 만든다.
    - inferface란 특정 클래스 세트에서 수행할 수 있는 공통된 작업.
    
    ```java
    package com.dna.praticespringdna.game;
    
    public class GameRunner {
    		private GamingConsole game;
        
        public GameRunner(GamingConsole game) {
            this.game = game;
        }
        
        public void run() {
            System.out.println("Running game: " + this.game);
            game.up();
            game.down();
            game.left();
            game.right();
        }
    }
    ```
    
- 이렇게 되면 Main 클래스에서 어떤 게임을 실행할 것인지만 결정하면 된다.
- GameRunner 클래스가 특정 클래스에 결합되어 있지 않게 된다.
- ex) 차의 바퀴

<br><br>

# Annotation (Configuration, Primary, Qualifier)

- **Configuration**
    
    ```java
    @Bean(name = "address2")
    @Primary
    public Address address() {
        return new Address("Baker Street", "London");
    }
        
        
    @Bean(name = "address3")
    @Qualifier("address3qualifier")
    public Address address3() {
        return new Address("Baker Street", "London");
    }
    ```
    
    - Configuration Class를 통해 context launch 가능
- **Primary**
    - 만약 같은 이름의 Bean이 여러 개라면?
    - Spring이 어떤 Bean을 써야할 지 우선순위를 정할 수 없다.
    - 여러 개의 Bean이 있다면 이것을 우선 사용하게 하는 Annotation → Primary
- **Qualifier**
    - 여러 개의 Bean이 있는 상황에서 특정 Bean을 사용할 수 있게끔 해주는 Annotation
    - 특정 Bean에 Qalifier를 설정하면 다른 Bean에서 해당 Qualifier를 통해 Bean에 접근 가능

<br><br>

# Auto wiring

- **Bean에 name지정** → `@Bean(name = "myBean")`
- **Method 호출 방식** → `new Object(beanName())`
- **Parameter 방식** → `public String Object(String beanName1, String beanName2)`

<br><br>

# Spring Container

- **Spring Container** : Spring Bean과 그것들의 lifecycle 관리
    - Bean Factory : Basic Spring Container
    - Apllication Context : 주로 쓰이는 Container (web application, web service, REST API, microservice) → `new AnnotationConfigApplicationContext`

<br><br>

# Bean

```java
record Person(String name, int age, Address address) {
};

@Configuration
public class HelloWorldConfiguration {
    
    @Bean
    public String name() {
        return "Ranga";
    }
    
    @Bean
    public int age() {
        return 24;
    }
    
    @Bean
    public Person person() {
        return new Person("Ravi", 20, new Address("Main Street", "Utrecht"));
    }
}
```

- **Java Bean** : 3가지 제약이 있는 클래스
    - public, no argument인 생성자 보유
    - getter, setter method의 구현
    - Serializable의 implement
- **POJO** : Old Java Object
    - 제약 X
    - 모든 Java의 Object들이 POJO
- **Spring Bean** : Spring에 의해 관리되는 Java Object, Spring은 IOC Container를 사용해 이 object들을 관리

<br>

<aside>
💡 **모든 Bean 출력** `Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);`
</aside>
