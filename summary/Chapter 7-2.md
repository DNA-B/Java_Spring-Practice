# Chapter 7 - 2

<br><br>

# REST API 문서
- REST API의 사용자는 노출되고 있는 여러 리소스와 수행되는 작업에 대해서 파악해야 한다.
- 요청 및 응답 구조는 물론 요청 형식도 알아야 한다.
- 이에 따라 RESP API를 문서의 중요성은 올라간다.

<br><br>

# Swagger
- 2011년도에 Swagger Specification과 Swagger Tools가 도입되었다.
- 이 시기에 많은 REST API를 구축하기 시작했고, 이에 따라 REST API를 문서화할 수 있는 형식이 필요했고, 여기서 Swagger가 등장했으며, Swagger는 Swagger Specification이라는 사양을 제공했다.
- 또한 이러한 사양을 시각화하고 이용할 수 있도록 여러 도구를 제공했다.

<br><br>

# Open API
- 2016년에는 이 Swagger Specification을 기반으로 Open API 사양이 만들어졌다.
- 많은 대기업이 참여한 결과 Open API 사양이라는 개방형 표준이 REST API를 문서화하기 위한 표준으로 만들어졌고, 그 기반은 Swagger Specification에 있다.
- 기억해야 할 한 가지는 이 Swagger Tools가 여전히 존재한다는 점이다. Swagger Specification은 Open API 사양이 되었는데 Swagger Tools는 REST API를 시각화하고 소비하는 도구이고, 지금도 계속 존재하고 있다. → ex) Swagger UI

<br><br>

# Swagger 문서 자동 생성
`open api dependency`

```xml
		<dependency>
			<groupId>org.springdoc</groupId>
			<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
			<version>2.0.0</version>
		</dependency>
```

- http://localhost:8080/swagger-ui/index.html 에서 모든 REST API에 대한 정보를 확인할 수 있다.
- /v3/api-docs를 클릭하면 Open API specification을 확인할 수 있다.

<br><br>

# 콘텐츠 협상
`pom.xml`

```xml
<dependency>
  <groupId>com.fasterxml.jackson.dataformat</groupId>
  <artifactId>jackson-dataformat-xml</artifactId>
  <version>2.15.0</version>
</dependency>
```

- 일반적으로 REST 리소스를 다룰 때 동일한 리소스에는 동일한 URI가 있다.
    - 사용자의 경우, `localhost:8080/users`
    - 특정 리소스에 특정 URI가 있는 것.
- 소비자는 JSON 형식으로 세부 사항을 알고 싶을 수도 있고, XML 형식으로 세부 사항을 알고 싶을 수도 있다.
- 이런 경우를 위해, 동일한 리소스에 대해 여러 표현을 보유하는 것이 가능하다.
    - XML, JSON과 같은 여러 형식부터, 다른 언어로 된 응답도 가능한 것이다.
- API 소비자가 다른 형식이나 언어로 응답 받는 것을 원하는 경우, 자신이 원하는 표현을 알려줘야 한다.
    - 이 때 사용하는 용어가 콘텐츠 협상이며, Accept 헤더와 같은 것을 통해 요구사항을 전달할 수 있다.
    - Accept - application/xml을 Header에 적용한 예시 → JSON이 아니라 XML로 반환
  
<br><br>

# REST API의 국제화
- REST API는 전 세계 사람들이 사용할 수 있다.
    - 다른 언어를 사용하는 사람들에게 API를 제공하려면? → Internationalization(i18n)을 사용해야 한다.
    - 이를 처리하기 위해서 HTTP Request Header를 사용하고, 요청의 일부로 Accept-Language라는 헤더를 전송한다.

- 스프링에서 i18n을 다루는 일반적인 방법은 `messages.properties`을 이용하는 것이다.
- `messages.properties`에는 기본값을 적어준다.
- 그리고 지원할 언어에 대해 `message_ko.properties`와 같은 파일을 생성해준 뒤 그 언어에 맞는 message를 적어준다.
- Accept 응답에 대해 올바른 메세지를 전달하기 위해서 `MessageSource`라는 클래스를 이용할 수 있다.
    - `MessageSource`는 메시지의 매개변수화나 국제화에 대한 지원을 통해 메시지를 처리하는 전략 인터페이스이다.
    - `messageSource`의 `getMessage`를 이용하여 전달할 message의 이름과 어느 locale을 사용할지 정의하기 위해 `Locale` 클래스의 `getLocale`을 이용하여 Request 헤더에서 locale을 얻어올 수 있다.

<br><br>

# REST API 버전 관리 - URI 버전 관리
- **브레이킹 체인지**
    - 모범 사례에 따르면 변경 사항을 프로덕션에 직접 구현하지 않아야 한다.
    - API를 변경할 때마다 고객도 즉시 변경하도록 강요하면 안된다.
    - 이것이 바로 구현할 큰 변경이 있을 때마다 REST API의 버전 관리를 해야 하는 이유
- REST API 버전 관리의 방법을 결정할 때 고려해야 할 여러 요인
    1. URI Pollution
        1. URL, Request Parameter 방법은 URL Pollution이 잦다.
    2. HTTP 헤더의 오용
        1. HTTP 헤더는 버전 관리 용도로 사용하면 안된다. → header versioning
    3. Caching
        1. 캐싱은 일반적으로 URL을 기반으로 수행되는데, 헤더 버전 관리와 미디어 유형 버전 관리의 경우 두 버전 모두 동일한 URL을 쓴다. 
        2. 헤더 버전 관리와 미디어 유형 버전 관리에서는 URL을 기반으로 캐싱을 할 수 없기 때문에 캐싱을 수행하기 전에 헤더를 살펴봐야 한다.
    4. 브라우저에서 요청을 수행할 수 있는가?
        1. URI 버전 관리와 요청 매개변수 버전 관리의 경우, 브라우저에서 간편하게 실행할 수 있다.
        2. 헤더 버전 관리와 미디어 유형 버전 관리에서는 차이가 헤더에 있다. 일반적으로 명령줄 유틸리티를 갖고 있거나, REST API 클라이언트를 사용해서 헤드를 기준으로 구분할 수 있어야 한다.
    5. API Documentation
        1. URI 버전 관리와 요청 매개변수 버전 관리에 대한 API 문서를 생성하기는 쉽다. 두 버전의 URL이 다르기 때문.
        2. API 문서 생성 툴은 보통 헤더를 기준으로 구분하는 문서의 생성을 지원하지 않을 수 있다. 이에 따라 헤더 버전 관리와 미디어 유형 버전 관리에 대한 문서 생성이 다소 까다로워질 수 있다.

- **URL Versioning - Twitter**
    
    ```java
    @RestController
    public class VersioningPersonController {
        @GetMapping("/v1/person")
        public PersonV1 getFirstVersionOfPerson() {
            return new PersonV1("Bob Charlie");
        }
        
        @GetMapping("/v2/person")
        public PersonV2 getSecondVersionOfPerson() {
            return new PersonV2(new Name("Bob", "Charlie"));
        }
    }
    ```
    
    - V1/USER, V2/USER와 같이 버전에 따른 URL로 구현을 다르게 하는 것을 말한다.
- **Request Parameter Versioning - Amazon**
    
    ```java
    @RestController
    public class VersioningPersonController {
        @GetMapping(path = "/person", params = "version=1")
        public PersonV1 getFirstVersionOfPersonRequestParameter() {
            return new PersonV1("Bob Charlie");
        }
        
        @GetMapping(path = "/person", params = "version=2")
        public PersonV2 getSecondVersionOfPersonRequestParameter() {
            return new PersonV2(new Name("Bob", "Charlie"));
        }
    }
    ```
    
    - request parameter로 넘어오는 값에 따라 구현을 달리하는 방법
- **(Custom) headers versioning - Microsoft**
    
    ```java
    @RestController
    public class VersioningPersonController {
        @GetMapping(path = "/person/header", headers = "X-API-VERSION=1")
        public PersonV1 getFirstVersionOfPersonRequestHeader() {
            return new PersonV1("Bob Charlie");
        }
        
        @GetMapping(path = "/person/header", headers = "X-API-VERSION=2")
        public PersonV2 getSecondVersionOfPersonRequestHeader() {
            return new PersonV2(new Name("Bob", "Charlie"));
        }
    }
    ```
    - request header로 넘어오는 값에 따라 구현을 달리하는 방법
- **Media type versioning(content negotiation, accept header) - Github**
