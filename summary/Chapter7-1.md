# Chapter 7 - 1
**💡 URL은 되도록 복수형을 사용하는 것이 이해하기 좋다.**

<br><br>

# `@RequestMapping`

- 해당 Annotation은 mathod와 path를 지정해야함
- GetMapping, PatchMapping 등등 Request를 지정하는 Annotation은 mathod가 필요 없음

<br><br>

# 백엔드에서 일어나는 일
- 모든 요청은 가장 먼저 **디스패처 서블릿**이라는 곳으로 간다. → front controller pattern
    - Mapping servlets: dispatcherServlet urls=[/]
    - 디스패처 서블릿은 `**AutoConfiguration**`에 의해 설정된다.
- 디스패처 서블릿이 URL을 확인하고 알맞은 **컨트롤러 메소드**에 매핑한다.
- 어떻게 Bean 객체가 JSON으로 변환되는가?
    - `@ResponseBody` + **JacksonHttpMessageConverters**
    - `@ResponseBody` → Bean을 있는 그대로 반환해라
    - **JacksonHttpMessageConverters →** AutoConfiguration에 의해 설정된다.  반환된 Bean을 JSON으로 변환해준다.
- **Starter Project**가 모든 의존성을 가져온다……

<br><br>

# Path Parameters
```java
@GetMapping(path = "/hello-world-path-variable/{name}")
    public HelloWorldBean helloWorldPathVariable(@PathVariable String name) {
        return new HelloWorldBean("Hello World");
}
```

- `/users/{id}/todos/{id}`과 같은 형태
- `@PathVariable` 어노테이션을 통해 url의 name을 method의 매개변수에 매핑시킬 수 있다.
    
    ```java
    @GetMapping(path = "/hello-world/path-variable/{name}")
        public HelloWorldBean helloWorldPathVariable(@PathVariable String name) {
            return new HelloWorldBean("Hello World " + name);
    }    
    ```
    
    [@RequestParam @PathVariable 차이점 비교](https://willbesoon.tistory.com/102)
    
- 만약 PathVariable을 전달하지 않는 경우? → 본인은 아래와 같이 코드를 작성하였다.
    
    ```java
    @GetMapping(path = {"/hello-world/path-variable-or-null/{name}", "/hello-world/path-variable-or-null/"})
        public HelloWorldBean helloWorldPathVariableOrNull(@PathVariable(required = false) String name) {
            if (name == null || name.length() == 0)
                return new HelloWorldBean("Hello World");
            else
                return new HelloWorldBean("Hello World " + name);
    }
    ```
    
<br><br>

# Request Methods for REST API
- GET → 디테일한 정보를 얻고싶을 때
- POST → 새로운 정보를 만들어낼 때
- PUT → 기존의 정보를 업데이트해야할 때
- PATCH → 기존의 정보를 부분적으로 업데이트해야할 때
- DELETE → 기존의 정보를 삭제할 때

<br><br>

# `@RequestBody`
- 해당 어노테이션은 메소드 인자를 웹 요청의 본문과 매핑하라는 지시이다.
- 웹 요청을 보낼 때 요청 본문을 함께 보내는데, 요청 본문에는 사용자의 정보를 담는다. 해당 코드에서는 User 빈과 매핑된다.
- POST에서 json 포맷의 헤더는 `**application/json**`

<br><br>

# Response Status for REST API
[HTTP 상태 코드 정리 | 와탭 블로그](https://www.whatap.io/ko/blog/40/)

- **4XX : Client error responses**
    - **400** → Validation error
    - **404** → Resource is not found
    - **401** → Unauthorized(when authorization fails)
- **5XX : Server error responses**
    - **500** → Server exception
- **2XX : Successful responses**
    - **200** → Success
    - **201** → Created
    - **204** → No Content

<br><br>

# createUser 개선
- `ResponseEntity.*created*(null).build();`
    - Controller에서 status를 지정하여 반환하고싶은 경우 사용하는 것이 `**ResponserEntity**`
    - `build()` → `created()`와 같은 함수를 사용하면 `ResponseEntity`가 아닌 `BodyBuilder`가 반환된다. 그러나 우리는 `ResponseEntity`를 반환할 것이므로 `build()`를 통해 타입을 변환해준다.
- RESP API를 구현할 때는 API 소비자의 입장을 고려해야한다.
    - 사용자가 생성되면 201 상태를 반환하는 것 뿐만 아니라 어떤 사용자가 생성됐는지 알려주고 싶다면?
    - /users/4를 URL로 반환하면 소비자는 여기에 요청을 보낼 수 있게 된다.
        
        ```java
        @PostMapping("/users")
        public ResponseEntity<User> createUser(@RequestBody User user) {
                User savedUser = userDaoService.save(user);
                
        				URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        				                .path("/{id}")
        				                .buildAndExpand(savedUser.getId())
        				                .toUri();
        
                return ResponseEntity.created(location).build();
        }
        ```
        
        - userDaoService에서 save한 user 반환값을 변수에 저장해준다.
        - `created()`는 URI location을 인자로 받는다.
        - ServletUriComponentsBuilder를 통해 현재 요청이 보내지는 URL을 얻어낸다.
        - /users/{id}에서 {id}부분을 추가해주기 위해 `path("/{id}")`를 이용한다.
        - {id} 부분에 들어갈 실제 id값을 `buildAndExpand(savedUser.getId())`를 통해 명시한다.
        - 마지막으로 `toUri()`를 통해 URI 타입으로 변환해준 후 `created()` 함수에 전달하면 아래 사진과 같이 사용자 생성 이후 **location**을 반환한다.
            
            ![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/459a6cf1-37d9-4d6d-b571-c19248cc3b95/c58d8c8c-e6c9-4c62-9790-852d2065dbbf/Untitled.png)
            
        
<br><br>

# 예외 처리 구현
```java
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundExcepttion extends RuntimeException {
    public UserNotFoundExcepttion(String message) {
        super(message);
    }
}
```

- Error가 발생하면 화이트 라벨 페이지가 띄워진다. 이 때 에러처리를 따로 진행해주지 않으면 적절한 status 값이 반환되지 않는다.
- status 값을 우리가 원하는 값으로 반환하기 위해서 Exception 클래스에서 예외처리를 진행해준다. → NOT_FOUND는 404
- app을 실행할 때는 JAR 파일로 만들고 실행한다.
- 그렇게 실행하면 Devtools는 자동으로 비활성화된다.

<br><br>

# 예외 처리 구조 커스텀
- 내가 원하는 포맷으로 status form을 구성하고 싶다면?
- Model, Controller의 구조와 비슷하게 → ErrorDetatils에서 form을 구현해주고, ExceptionHandler를 통해 구조를 적용시킨다.

```java
public class ErrorDetails {
    private LocalDateTime timestamp;
    private String message;
    private String details;
    
    public ErrorDetails(LocalDateTime timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public String getMessage() {
        return message;
    }
    
    public String getDetails() {
        return details;
    }
}
```

- 생성자, Getter 필요

```java
@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorDetails> handlerAllException(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        
        return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(UserNotFoundExcepttion.class)
    public final ResponseEntity<ErrorDetails> handlerUserNotFoundException(Exception ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        
        return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND);
    }
}

```

- 특정 error는 함수를 통해 구현해줄 수 있다 → `handlerUserNotFoundException`

<br><br>

# DELETE API
`Service`

```java
    public void deleteById(int id) {
        Predicate<? super User> predicate = user -> user.getId().equals(id);
        users.removeIf(predicate);
    }
```

`Controller`

```java
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        userDaoService.deleteById(id);
    }
```

<br><br>

# 유효성 검증
- `@Valid` Annotation → @Valid 어노테이션을 붙이면 바인딩이 수행될 때 객체에 정의된 유효성 검증이 자동으로 수행된다.
    - `public ResponseEntity<User> createUser(@Valid @RequestBody User user)`

```java
public class User {
    private Integer id;
    
    @Size(min=2)
    private String name;
    
    @Past
    private LocalDate birthDate;
'
'
'
'
}
```

- 여기서 @Size와 @Past같은 검증을 `@Valid`가 알아서 확인해준다는 것이다.
- `@Size(min)`은 최소 글자, `@Past`는 과거 날짜를 의미하고, 매우 많은 검증 어노테이션이 있으므로 필요한 것을 찾아 사용하도록 하자.
- 유효성 검증도 특정 exception을 구현할 수 있다.

```java
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),
                "Total Errors:" + ex.getErrorCount() + " First Error:" + ex.getFieldError().getDefaultMessage(), request.getDescription(false));
        
        
        return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
    }
```

- 위에서 구현한 `ExceptionHandler` 클래스에 위와 같은 코드를 추가해주면 Controller Method에 대한 Error form을 형성할 수 있다.

```java
    @Size(min=2, message = "Name should have at least 2 characters")
    private String name;
    
    @Past(message = "Birth Date should be in the past")
    private LocalDate birthDate;
```

- 만약 Model에서 message를 따로 설정해 놓았다면 `ex.getFieldError().getDefaultMessage()`를 통해 message를 출력할 수 있다. → 원래는 `ex.getMessage()`이다.
