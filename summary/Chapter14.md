# 보안의 기초
- 시스템의 보안을 구현하지 않는다면 애플리케이션이나 시스템을 이용할 사용자의 ID를 어떻게 파악할 것인지에 대해 문제가 발생한다.
- 또한 사용자가 액세스할 수 있는 리소스와 각 리소스별로 수행할 수 있는 작업은 어떻게 설정해야 할지 고려해야 한다.
- **Authentication** : 적절한 사용자인가?
    1. 사용자 ID와 패스워드의 조합과 같이 사용자가 기억할 수 있는 정보를 이용 (계정 로그인)
    2. 사용자의 소유물을 이용 (지문, 안면 인식)
    3. 여러 방법을 결합하는 다단계 인증 (구글 첫 로그인 시 모바일 인증 진행)
- **Authorization** : 적절한 액세스 권한을 보유했는가?

<br><br>

# 보안의 원칙
1. 무엇도 신뢰하지 않는다.
    1. 들어오는 모든 요청을 검증한다.
2. 최소 권한 할당
    1. 시스템 설계를 시작할 때 처음부터 보안을 염두에 둔다.
    2. 모든 레벨에서 가능한 최소한의 권한을 할당하고, 모든 요소에 대한 액세스를 최소화한다.
3. 완벽한 조율 구축
    1. 모든 요청이 하나의 문을 통과해야 한다.
    2. 요청이 들어올 때마다 권한을 확인한다.
4. 심층적인 방어 구축
    1. 애플리케이션을 여러 층으로 구현하듯이 보안도 여러 층으로 구축한다.
5. 보안 아키텍처를 가능한 한 간단하게 유지한다.
    1. 간단한 시스템은 보호하기가 더 쉽다. 즉, 메커니즘의 효율성이 필요한 것
6. 설계의 개방성을 보장한다.
    1. 개방형 보안 설계를 구축하고, 개방형 보안 표준을 사용한다.
    2. ex) JWT

<br><br>

# Spring Security 시작하기
- Spring Security는 커스터마이즈가 가능한 유연한 보안 시스템을 제공한다. 보안은 기본적으로 적용된다.
- 기본적인 Spring MVC의 동작
    - Spring으로 웹 애플리케이션이나 REST API를 빌드할 때 백그라운드에서 Spring MVC를 사용한다.
    - 요청이 들어오면 제일 먼저 디스패처 서블릿에서 처리한다. Spring 웹 애플리케이션으로 오는 모든 요청은 먼저 디스패처 서블릿에서 처리.
    - 디스패처 서블릿은 URL, 요청 메서드를 확인한 후 요청을 적절한 컨트롤러로 라우팅한다. 즉, 디스패처 서블릿은 프론트 컨트롤러 역할
- Spring Security는 어떻게 동작할까?
    - Spring Security는 Spring MVC 체계에 또 다른 레이어로 추가된다.
    - 들어오는 모든 요청을 Spring Security가 인터셉트한다. 웹 애플리케이션, REST API, 마이크로서비스로 오는 모든 요청을 Spring Security가 인터셉트.
    - 필터 체인을 설정하고 이 필터 체인으로 요청을 인터셉트하게 된다.
    - 그리고 Spring Security가 인증과 권한 부여를 확인하고 나면 요청이 디스패처 서블릿으로 전송되고 그 다음 단계인 컨트롤러까지 처리된다.

<br><br>

# Spring Security 기본 설정
- 필터 체인의 기능
    - Authentication → ex) BasicAuthenticationFilter
    - Authorization → ex) AuthorizationFilter
    - Best Practice
        - CORS → 오리진 간 리소스 공유를 설정할 수 있다. (프론트 ↔ 백엔드)
        - CSRF → 사이트 간 요청 위조 (쿠키 취약성을 막기 위해 CSRF가 자동으로 보호한다)
    - 기본 로그인, 로그아웃 페이지 제공
    - 인증, 권한 부여와 관련해 예외가 발생하면 ExceptionTranslationFilter를 통해 HTTP 응답으로 변환해준다.
- 이러한 기능들은 순서대로 실행되고, 순서는 매우 중요하다.

<br><br>

# 폼 인증
```java
    @GetMapping("/hello-world")
    public String helloWorld() {
        return "Hello World";
    }
```

- `localhost:8080/hello-world`에 접속하면 되면 폼 화면이 자동으로 나타난다.
- 다른 리소스를 여러 개 만들어도 각각에 대해 인증하게 된다.
- Spring Security는 서버에 존재하지 않는 리소스까지도 보호한다. → 외부에서는 어떤 리소스가 서버에 존재하는지 알 수 없으므로 완벽한 조율 구축 원칙을 수행한다.
- 폼 기반 인증은 대부분의 웹 어플리케이션에서 일반적으로 사용하는 인증이다.
    - 로그인을 하면 백그라운드에서는 세션 쿠키가 생성된다.
    - 이후 웹사이트에서 일어나는 모든 작업에 대해 세션 쿠키가 요청과 함께 전송된다.

<br><br>

# 기본 인증
- 기본 인증은 REST API 보안에 사용되는 가장 기본적인 옵션이다. 하지만 단점이 많아서 프로덕션 환경으로는 권장되지 않는다.
- 기본 인증에서는 Base 64 인코딩 사용자 이름과 패스워드가 request header로 전송된다.
    - `"Basic {encoding value}"`의 형태이다.
- Base 64 인코딩의 단점
    - 디코딩하기가 쉽다. → Authorization Header를 얻어낼 수 있다면 누구라도 쉽게 사용자 이름과 패스워드를 알아낼 수 있다.
    - 또 하나 중요한 점은 기본 권한 부여 헤더에는 권한 부여 정보가 없다는 것이다. → 사용자 이름과 패스워드만 들어 있을 뿐 사용자 액세스 권한이나 역할에 관한 정보는 없다.
    - 만료일이 없다. → 기본 권한 부여 헤더를 한 번 생성하면 영구적으로 유효하다.

<br><br>

# Cross-Site Request Forgery (CSRF)
```java
    @GetMapping("/users/{username}/todos")
    public Todo retrieveTodosForSpecificUser(@PathVariable String username) {
        return TODOS_LIST.get(0);
    }
    
    @PostMapping("/users/{username}/todos")
    public void createTodos(@PathVariable String username, @RequestBody Todo todo) {
        logger.info("Create {} for {}", todo, username);
    }
```

- 사용자가 은행 웹사이트에 로그인했다고 가정해보자.
    - 은행 웹사이트는 대개 웹 애플리케이션이므로 쿠키가 생성돼서 웹 브라우저에 저장된다. 
    그런데 사용자가 로그아웃하지 않은 상태에서 악성 웹사이트로 이동을 했다. → 은행 웹사이트에서 로그아웃하지 않고 악성 웹사이트로 이동한 것
    - 사용자가 알지 못하는 상태에서 악성 웹사이트는 쿠키를 이용해 은행 웹사이트에 요청을 실행한다. → 브라우저의 쿠키는 어떻게 될까?
    - 다른 웹사이트에서는 이 쿠키에 액세스하고 이용해서 사용자 대신 요청을 실행할 수 있다. 
    이러한 문제를 사이트 간 요청 위조라고 한다.
- 어떻게 이를 보호할 수 있을까?
    1. 동기화 토큰 패턴을 이용한다.
        1. 요청마다 토큰을 생성하는 것
        2. 그리고 POST, PUT과 같은 업데이트 요청이 있을 때, 예를 들어 송금이 발생하면 이 토큰으로 인증한다.
        3. Spring Security는 이 기능을 기본적으로 사용한다.

![스크린샷 2024-06-14 022239](https://github.com/DNA-B/Java_Spring-Practice/assets/102334596/2e1f42d0-e755-4f08-9aa1-4f24de409452)

- Base 64를 사용할 때, GET 요청은 성공하지만 POST를 요청하면 403 error가 발생한다.
- Spring Security에서 읽기 요청은 그대로 허용하지만, 업데이트 요청에는 CSRF 토큰이 필요하기 때문이다.

<br><br>

# REST API에서의 CSRF
![스크린샷 2024-06-14 022704](https://github.com/DNA-B/Java_Spring-Practice/assets/102334596/71acddf2-072d-4258-a83e-720abc8fcaa3)

- Spring Security는 로그아웃에 폼을 사용한다.
- 로그아웃 페이지는 /logout URL에 POST 요청을 실행하고 있다. 그리고 POST 요청이기 때문에 CSRF 토큰이 필요하다.
    - POST나 PUT 요청에는 CSRF 토큰이 필요
- Spring에서 CSRF 토큰을 자동으로 생성해 추가한 것을 볼 수 있다.
- 유의할 점은 세션을 사용하지 않는다면 CSRF는 필요 없다.
    - 상태를 저장하지 않는 Rest API 같은 경우에는 CSRF가 필요하지 않다.
    - CSRF는 일반적으로 세션이나 세션 쿠키와 관련이 있다.
    - stateless REST API를 사용한다면 CSRF를 사용 해제하는 게 좋다.

```java
    @GetMapping("/csrf-token")
    public CsrfToken csrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }
```

- 위의 코드와 같이 csrf-token을 받아와 request header에 토큰값을 추가하여 POST, PUT 요청을 보낼 수 있다.

<br><br>

# CSRF 사용 해제
```java
    http
        .cors(c -> {
          CorsConfigurationSource source = request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(List.of("http://localhost:8080", "test.com"));
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
            return config;
          };
          c.configurationSource(source);
        })
        .csrf(AbstractHttpConfigurer::disable)  // csrf 비활성화
        .authorizeHttpRequests(authz -> authz.anyRequest().permitAll());  // 인증되지 않아도 모든 요청 허용
    return http.build();
```

- 동기화 토큰 패턴 외에 다른 방법으로는 SameSite 쿠키를 이용하는 것이다.
- `application.properties`에서 `server.servlet.session.cookie.same-site=strict`로 설정하면 쿠키는 해당 사이트로만 전송되고, 다른 사이트로 전송되지 않는다.
- **SessionCreationPolicy → 세션 사용 설정**
    - `ALWAYS` →  세션을 항상 생성
    - `NEVER` → HTTP 세션을 생성하지 않지만 이미 있을 경우에는 사용
    - `IF_REQUIRED` → 필요 시에만 세션을 생성
    - `STATELESS` → HTTP 세션을 생성하지도 않고, 사용하지도 않는다.
- 세션을 사용하지 않을 때는 csrf를 사용 해제해야 하므로 `http.csrf().disable();`로 csrf 해제

<br><br>

# Cross-Origin-Resource-Share (CORS)
- 풀 스택 애플리케이션에서 사용자가 다른 도메인, 다른 URL에 REST API 호출을 실행한다고 가정해보자.
    - 브라우저에서 이러한 호출은 대개 허용되지 않는다.
    - 그런데 도메인 간 허용되는 요청을 설정할 수 있는 규격이 **CORS**이다.
- 설정
    - **Global** → 글로벌 설정은 모든 컨트롤러, 모든 리소스, 모든 REST 컨트롤러에 적용
        - 특정 오리진에서 오는 것이라면 어떠한 요청 메서드를 사용하든 모든 URL에 대한 요청을 모두 허용한다.
    - **Local** → 특정 요청 메서드나 특정 컨트롤러 클래스에 `@CrossOrigin`을 추가한다.
        ```java
        @CrossOrigin("http://localhost:8080")
        public String test() {
          log.info("TEST Method");
          return "hello";
        }
        ```
        - 기본적으로 `@CrossOrigin`은 모든 오리진을 허용하지만 필요에 따라 커스터마이즈 할 수 있다.
        - `@CrossOrigin(origins=”http://www.dna.com”)`으로 지정한다면, 이 특정 오리진의 요청만 허용된다.

<br><br>

# 메모리에 사용자 자격증명 저장하기
- 사용자 자격증명은 메모리에 저장할 수 있다. 테스트 목적으로는 좋지만, 프로덕션 환경용으로는 권장되지 않는다.
- 다른 옵션은 데이터베이스이다. JDBC나 JPA를 이용해 자격증명에 액세스하고 Spring Security를데이터베이스에 저장된 테이블과 통합하는 것
- 많이 사용되는 또 다른 옵션은 LDAP, 경량 디렉터리 액세스 프로토콜이다. 디렉터리 서비스와 인증에 사용되는 오픈 프로토콜이다.
- `UserDetailsService` → 사용자별 데이터를 로드하는 코어 인터페이스
    - 사용자 세부 정보를 가져올 때 이 인터페이스를 사용
- `InMemoryUserDetailsManager`  → `UserDetailsManager`의 비지속적 구현이다.
    
    ```java
     @Bean
        public UserDetailsService userDetailsService() {
            var user = User.withUsername("dna")
                    .password("{noop}dummy")
                    .roles("USER")
                    .build();
            var admin = User.withUsername("admin")
                    .password("{noop}dummy")
                    .roles("ADMIN")
                    .build();
            
            return new InMemoryUserDetailsManager(user, admin);
        }
    ```
    
    - `{noop}` → 인코딩 사용하지 않기 위해 적용
    - 역할을 할당할 때 best practice는 모든 역할에 Enum을 생성하는 것.
    - `Enum.USER, Enum.ADMIN`과 같이 사용

<br><br>

# JDBC를 사용해 사용자 자격증명 저장하기
- H2 콘솔은 프레임을 사용하기 때문에 `http.headers().frameOptions().sameOrigin()`을 적용해주어야 한다.
    - 요청이 동일한 오리진에서 오는 경우 해당 애플리케이션에 프레임을 허용하도록 지정하는 것
    - 예를 들어 example.com에서 호스팅되는 애플리케이션에는 프레임을 적용하고, evil.com의 애플리케이션에는 적용하지 않는 것이다.

```java
	@Bean
	public UserDetailsService userDetailService(DataSource dataSource) {
		
		var user = User.withUsername("in28minutes")
			.password("{noop}dummy")
			.roles("USER")
			.build();
		
		var admin = User.withUsername("admin")
				.password("{noop}dummy")
				.roles("ADMIN", "USER")
				.build();
		
		var jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
		jdbcUserDetailsManager.createUser(user);
		jdbcUserDetailsManager.createUser(admin);

		return jdbcUserDetailsManager;
}

@Bean
public DataSource dataSource() {
	return new EmbeddedDatabaseBuilder()
			.setType(EmbeddedDatabaseType.H2)
			.addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
			.build();
}

```

- 위 코드로 데이터베이스 소스 생성

<br><br>

# 인코딩, 해싱, 암호화
- **인코딩**
    - 인코딩은 데이터를 원래 형식에서 다른 형식으로 변환하는 것
    - 인코딩에서는 키나 패스워드를 사용하지 않는다.
    - 인코딩은 가역적이다. 텍스트를 인코딩한 후 실제 텍스트로 다시 바꿀 수 있다.
    - 일반적으로 인코딩은 데이터 보안에 사용되지 않는다.
    - 데이터를 압축하거나 스트리밍하는 것이 목적이다.
- **해싱**
    - 해싱에서는 데이터를 해시 문자열로 변환한다.
    - 단방향 프로세스이고 비가역적이어서 해시에서 원래 데이터를 구할 수 없다.
    - 데이터 무결성을 검증하는 데 사용한다.
    - 데이터와 이 데이터의 해시를 함께 전송하면, 수신 측에서는 데이터를 받고 해싱한 후, 계산한 해시와 원래 메시지의 해시가 일치하는지 확인한다.
    - 이를 통해 메시지가 전송한 시점 그대로인지 확인할 수 있다.
    - 대표적인 해싱 알고리즘으로 **bcrypt**와 **scrypt**가 있다.
- **암호화**
    - 암호화의 예로 RSA가 있다.
    - 암호화 알고리즘이 데이터 암호화 키를 사용해 암호화된 텍스트를 생성한다.
    - 그리고 암호화한 경우에는 복호화 알고리즘과 데이터 암호화 키를 사용해 실제 텍스트로 되돌릴 수 있다.
    - 암호화는 데이터 보호를 목적으로 한다.
- 패스워드를 평문 그대로 저장하는 것은 바람직하지 않다.
    - 누군가 데이터베이스에 액세스해서 평문 암호를 획득할 수 있기 때문이다.
    - 그래서 패스워드를 저장할 때 해싱을 이용한다.
    - 해싱 알고리즘으로 패스워드를 해싱한 후 데이터베이스에 저장하는 것
    - 해싱 알고리즘으로 패스워드를 해싱한 후 데이터베이스에 저장하고, 사용자가 로그인을 시도하면 패스워드를 확인해 해싱한다.
    - 그리고 이 해싱된 값을 데이터베이스에 저장된 해시와 비교한다.

<br><br>

# Bcrypt 인코딩 암호
- Spring Security에서 패스워드를 저장할 때 권장되는 방법은 1초를 워크 팩터로 적용해 적응형 단방향 함수를 사용하는 것이다.
    - 워크 팩터란 시스템에서 패스워드를 확인하는 데 걸리는 시간을 의미
    - 적응형 단방향 함수의 대표적인 예로 **bcrypt, scrypt, argon2** 등이 있다.
    - Spring Security에는 PasswordEncoder라는 인터페이스가 있고, 단방향으로 패스워드 변환을 수행하는 인터페이스이다.
    - 이름이 Encoder지만 모두 해싱을 수행한다. 따라서 PasswordEncoder들은 실제 텍스트를 구할 수 없다.
- 권장되는 PasswordEncoder는 `BCryptPasswordEncoder`이다.  
    ```java
    @Bean
    	public UserDetailsService userDetailService(DataSource dataSource) {
    		
    		var user = User.withUsername("in28minutes")
    			.password("dummy")
    			.passwordEncoder(str -> passwordEncoder().encode(str))
    			.roles("USER")
    			.build();
    		
    		var admin = User.withUsername("admin")
    				.password("dummy")
    				.passwordEncoder(str -> passwordEncoder().encode(str))
    				.roles("ADMIN", "USER")
    				.build();
    		
    		var jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
    		jdbcUserDetailsManager.createUser(user);
    		jdbcUserDetailsManager.createUser(admin);
    
    		return jdbcUserDetailsManager;
    	}
    
    @Bean
    	public BCryptPasswordEncoder passwordEncoder() {
    		return new BCryptPasswordEncoder();
    	}
    ```   
    - 함수 인자로 버전과 강도를 선택할 수 있다.
    - 강도가 클수록 패스워드를 해싱하는 데 필요한 작업이 기하급수적으로 증가한다. (기본값은 10)
    
<br><br>

# JWT
- 기본 인증의 경우에는 기본 인증 헤더에 만료 기한이 없다.
- 또한 사용자에 관련된 세부정보도 전혀 없고, 사용자에게 어떤 권한이 있는지도 알 수 없다.
- 따라서 사용자 지정 토큰 시스템과 구조를 만들어야 한다.
    - 그러나 보안 결함이 있을 수 있다는 문제가 있고, 서비스 제공자와 서비스 소비자가 사용자 지정 토큰 시스템을 이해해야 한다는 문제도 있다.
- 여기서 **JWT**(Json Web Token, JOT)가 사용된다.
    - JWT는 두 당사자들 간에 안전하게 클레임을 표시하기 위한 공개 산업 표준이다.
    - JWT에는 사용자 세부정보와 인증을 모두 담을 수 있다.
    - 자신만의 사용자 지정 속성을 JWT에 추가할 수도 있다.
    - 그리고 JWT에는 시그니처도 포함되어 있다.
    - JWT는 비대칭 키를 주로 사용한다.
- **JWT flow**
    1. JWT 생성
        1. Encoding
            1. user credentials
            2. user data (payload)
            3. RSA key pair
        2. JWT resource 생성
    2. JWT를 request header의 일부로 전송
        1. Authorization Header
        2. Bearer Token
        3. Authorization: Bearer ${JWT_TOKEN}
    3. JWT 검증
        1. Decoding
        2. RSA key pair (public key)

<br><br>

# JWT 인증 설정
- `ResourceServerConfigurer` → OAuth2 리소스 서버 지원을 제공해줄 수 있는 클래스
    - JWT는 요청 헤더의 일부로서 Bearer 토큰 형태로 전송된다.
    - 리소스 서버는 이 토큰을 받아서 디코딩하고 인증과 승인을 적절히 수행하는 역할
- JWTDecoder를 만들기위해서는 RSA key pair를 생성해야한다.
    
    ```java
        @Bean
        public KeyPair keyPair() {
            try {
                var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(2048);
                return keyPairGenerator.generateKeyPair();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        
        @Bean
        public RSAKey rsaKey(KeyPair keyPair) {
            
            return new RSAKey
                    .Builder((RSAPublicKey)keyPair.getPublic())
                    .privateKey(keyPair.getPrivate())
                    .keyID(UUID.randomUUID().toString())
                    .build();
        }
    ```
    
- 키를 생성했다면 JSON Web Key source를 만들어야 한다.
    
    ```java
        @Bean
        public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {
            var jwkSet = new JWKSet(rsaKey);
            return (jwkSelector, context) ->  jwkSelector.select(jwkSet);
            
        }
    ```
    
- 마지막으로 Encoder, Decoder를 생성해준다.
    
    ```java
        @Bean
        public JwtDecoder jwtDecoder(RSAKey rsaKey) throws JOSEException {
            return NimbusJwtDecoder
                    .withPublicKey(rsaKey.toRSAPublicKey())
                    .build();
            
        }
        
        @Bean
        public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
            return new NimbusJwtEncoder(jwkSource);
        }
    ```
    
- 이제 JWT 토큰을 생성해준다.
    
    ```java
    private String createToken(Authentication authentication) {
            var claims = JwtClaimsSet.builder()
                    .issuer("self")
                    .issuedAt(Instant.now())
                    .expiresAt(Instant.now().plusSeconds(60 * 30))
                    .subject(authentication.getName())
                    .claim("scope", createScope(authentication))
                    .build();
            
            return jwtEncoder.encode(JwtEncoderParameters.from(claims))
                    .getTokenValue();
        }
        
        private String createScope(Authentication authentication) {
            return authentication.getAuthorities().stream()
                    .map(a -> a.getAuthority())
                    .collect(Collectors.joining(" "));
        }
    ```
    
    - `iss`는 JWT 토큰의 발행자를 설정하는 것
    - `issuedAt(Instans.now())`는 현재의 인스턴스를 획득하겠다는 설정
    - `expiresAt(Instant.now().plusSeconds(60 * 30))`은 만료시간을 30분으로 설정
    - `subject(authentication.getName())`은 주체의 이름
    - scope에는 유저의 권한이 들어간다.
        - `createScope`에서는 유저에게 부여된 모든 권한을 join하고 있다.

<br><br>

# Authorization
- Global Security → `authorizeHttpRequests`
    - `.requestMatchers("/users").hasRole("USER")`
- Method Security → `@EnableMethodSecurity`
    - `@PreAuthorize("hasRole("USER") and #username == authentication.name")`
    - `@PostAuthorize("returnObject.username == 'DNA'")`
