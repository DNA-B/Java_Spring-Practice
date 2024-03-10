# Spring MVC

- **`@RequestMapping` →** URL을 해당 method에 매핑할 수 있다.
- **404 error** → 존재하지 않는다.
- `@ResponseBody` → method가 리턴한 것 그대로를 브라우저에 리턴

<br><br>

# View - JSP file

- `.jsp` → Java Server Pages라고 부르는 것입니다
    - HTML 코드에 JAVA 코드를 넣어 동적웹페이지를 생성하는 웹어플리케이션 도구
- **prefix, suffix** → return할 파일 이름에서 .jsp와 경로가 중복되기 때문에 고정 시켜놓을 수 있다.
    - **application.properties**
        - `prefix=PATH`
        - `suffix=.jsp`
- **view**에서 `@ResponseBody`를 사용하지 않는 이유
    - `@ResponseBody`가 있으면 Spring MVC는 값을 바로 리턴하게 된다.
    - 뷰 또는 jsp로 리디렉션을 하길 원한다면 사용할 수 없다.
- JSP를 tomcat에서 실행시키기 위한 dependency
    
    ```xml
    <dependency>
    		<groupId>org.apache.tomcat.embed</groupId>
        <artifactId>tomcat-embed-jasper</artifactId>
        <scope>provided</scope>
    </dependency>
    ```

<br><br>

# 백그라운드에서 일어나는 일

- 브라우저에서 request를 보낸다 → HttpRequest
- 서버가 request를 처리하여 response를 반환한다 → HttpResponse

<br><br>

# `@RequestParam`

- 특정한 URL에 정보를 전달하는 방법 중 하나
    - 가장 간단한 방법으로는 **localhost:8080/login?name=**에 **DNA**를 넣어줄 수 있다.
    - RequestParam을 매개변수에 적용하면 DNA를 매개변수에 매핑키실 수 있다.
- 파라미터를 jsp에 어떻게 전달할까
    - `ModelMap`을 통해 jsp에서 사용할 값을 전달해줄 수 있다.
    - 값은 `put`으로 추가하고, 객체는 `addAttribute`를 사용하여 추가한다.
- `ModelMap`
- Expression Language(EL) → `${}`

<br><br>

# Spring의 개발 역사

- 모든 코드를 VIEW(jsp)에 작성하였다. → Controller 개념이 없었다.
    - 그 결과 아주 복잡해지게 되었다. → 주제별로 구별되지 않았다.
    - 이를 `Model 1 Architecture`라고 부른다.
- Model은 View를 생성하는데 사용하는 데이터이고, View는 정보를 사용자에게 보여주는 역할을 하였다. 그리고 Controller또는 Servlet이 전체 흐름을 제어하였다. → `Model 2 Architecture`
    - 이를 통해 로직을 주제별로 구분할 수 있게 되었다. → 유지가 쉽다.
    - 공통 기능을 모든 Controller에 걸쳐 구현하는 방법이 쉽지 않았다.
- 위의 단점 때문에 Front Controller Architecture로 전환하게 되었다. → `Model 2 Front Controller Architecture`
    - 보안 인증같은 공통 기능을 FrontController에서 구현하고, 이를 확인한 후 적절한 Veiw 혹은 Servlet으로 전달할 수 있다.
- `Spring MVC front Controller` - Dispatcher Servlet
    - Front Controller는 모든 HTTP Request를 받는다.
    - 이를 처리하기 위해, `**Model, View, Controller**`를 이용한다.
    - **URL**을 처리할 수 있는 Controller method를 식별한다.
    - 그 method는 `model`과 `view` 이름을 `return`한다.
    - View Resolver가 `**prefix**`와 `**suffix**`를 이용하여 정확한 view 이름을 받아낸다.
    - 그 후 `**xxxx.jsp**` 파일을 실행하게 된다.

<br><br>

# Login - 사용자 인증

- GET과 POST의 차이
    - **GET** → GET을 사용하면 모든 정보가 URL의 일부로서 전송된다. 이 경우, 비밀번호가 유출될 수 있으므로 안전하지 않다.
    - **POST** → Form Data의 일부로서 전송되기 때문에 URL에서 정보가 유출되지 않는다.
- submit 후의 리디렉션
    - `@RequestMapping - value, method`
    - 하나의 페이지에서 GET과 POST의 경우를 구별하여 함수를 만들어야한다.
    - 
- Form Data를 어떻게 받을까?
    - `@RequestParam`을 이용하면 된다.

<br><br>

# Request vs Model vs Session

- Request Scope → single request에 대해서만 값 유효
    - 대부분의 케이스에서 사용하길 권장
- Session Scope → multiple requests에서 값 유효
    - 어떤 값을 session에 저장하는 지 주의해야한다.
- 하나의 요청으로 얻은 값을 유지해야한다면? → Session에 정보를 넣어야한다.
- `@SessionAttributes("속성")` → SessionAttribute를 사용할 때는 항상 그 값을 사용하려는 모든 컨트롤러에 그걸 넣어줘야 한다.

<br><br>

# JSTL

```xml
<dependency>
	<groupId>jakarta.servlet.jsp.jstl</groupId>
	<artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
</dependency>

<dependency>
	<groupId>org.glassfish.web</groupId>
	<artifactId>jakarta.servlet.jsp.jstl</artifactId>
	<version>3.0.1</version>
</dependency>
```

- `<%taglib uri= %>`로 import
- `if`나 `forEach`와 같은 기능을 jsp에서 사용할 수 있게 해준다.

[JSTL core
          (TLDDoc Generated Documentation)](https://docs.oracle.com/javaee/5/jstl/1.1/docs/tlddocs/c/tld-summary.html)

<br><br>

# CSS

- Cascading Style Sheet
- BootStrap
    - `div class="container"`
    - `table class="table"`
- werjars
