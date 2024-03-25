# API 연결
```jsx
  function callHelloWorldRestApi() {
    axios
      .get("https://localhost:8080/hello-word")
      .then((response) => successfulResponse(response))
      .catch((error) => errorResponse(error))
      .finally(() => console.log('clean up'))

  function successfulResponse(response) {
    console.log(response);
  }

  function errorResponse(error) {
    console.log(error);
  }
```

- spring으로 작성한 hello world API를 리액트에서 호출하려고 한다.
- 리액트에서는 API 통신을 위해 `axios`를 주로 사용한다.
    - `npm install axios`
- `axios`는 `Promise`를 사용한다.  api 통신을 수행한 후 Promise가 반환되면 `then, catch, finally`를 사용하여 후속 작업을 진행할 수 있다.
- 이대로 실행한다면 오류가 나타나는데 localhost:3000(react)에서 localhost:8080(spring)을 호출하려고 하기 때문이다.
    - 한 웹사이트에서 다른 웹사이트를 호출하는 것과 비슷한 것이다.
    - 이러한 요청을 **Cross Origin Requests**라고 부른다.
    - 이를 허용하기 위한 설정을 CORS 설정이라고 부른다.
- CORS 설정
    - 스프링의 `WebMvcConfigurer` 인터페이스에 `addCorsMappings` 함수를 이용하여 크로스 오리진 리퀘스트 처리를 전역적으로 설정할 수 있다.
    - 스프링 application 클래스에 아래와 같이 사용한다.
        
        ```java
        @Bean
        public WebMvcConfigurer corsConfigurer() {
                return new WebMvcConfigurer() {
                    public void addCorsMappings(CorsRegistry registry) {
                        registry.addMapping("/**")
                                .allowedMethods("*")
                                .allowedOrigins("http://localhost:3000");
        		}
        	};
        }
        ```
        
- Axios에서 api url을 직접 노출시키면 위험할 수 있으므로 다음과 같이 리팩토링 할 수 있다.
    - api 디렉토리에서 export를 시킨다.
    
    ```jsx
    import axios from "axios";
    
    /* 1번 방법
    export default function retrieveHelloWorldBean() {
    return axios.get("http://localhost:8080/hello-world-bean");
    }
    */
    
    // 2번 방법
    export const retrieveHelloWorldBean = 
    		() => axios.get("http://localhost:8080/hello-world-bean");
    ```
    
- Axios에서 계속 http://localhost:8080을 입력하기 힘들기 때문에 common url을 설정할 수 있다.
    
    ```jsx
    const apiClient = axios.create({
      baseURL: "http://localhost:8080",
    });
    
    export const retrieveHelloWorldBean = () => apiClient.get("/hello-world-bean");
    ```
    
    - 다음과 같이 axios 객체를 만들고 필드에 common url을 만들어 준 뒤, 생성한 axios 객체를 이용하면 된다.
- api에 path-variable이 있다면?
    
    ```
    export const retrieveHelloWorldWithParameter = (username) =>
      apiClient.get(`/hello-world/path-variable/${username}`);
    ```
    
    - 틱(``)을 이용할 수 있다. → 이 경우, 람다식에 매개변수가 들어갈 것이다.

<br><br>

# useEffect에 대하여
- useEffect는 컴포넌트가 렌더링 될 때마다 특정 함수를 실행하기 위해 사용했다.
- useEffect는 `useEffect(function, depth)`의 형태를 띄고 있는데 여기서 depth를 통해 렌더링 될 때마다 함수를 실행 시킬 것인지, 처음 렌더링 될 때만 실행시킬 것인지 정할 수 있다.
- `useEffect(() => refreshTodos(), []);` → 이와 같이 빈 배열을 줄 경우 함수는 첫 렌더링 때만 실행된다.
- 여기서 배열을 생략하면 렌더링 될 때마다 함수가 실행된다.

<br><br>

# formik, moment
- `npm install formik`
- `npm install moment`
- formik은 데이터 양식을 작성하는데 도움을 주는 라이브러리
    
    ```jsx
    <Formik initialValues={{ description, targetDate }} enableReinitialize={true} onSubmit={onSubmit}>
              {(props) => (
                <Form>
                  <fieldset className="form-group">
                    <label>Description</label>
                    <Field type="text" className="form-control" name="description" />
                  </fieldset>
                  <fieldset className="form-group">
                    <label>Target Date</label>
                    <Field type="date" className="form-control" name="targetDate" />
                  </fieldset>
                  <div>
                    <button className="btn btn-success m-5" type="submit">
                      Save
                    </button>
                  </div>
                </Form>
              )}
    </Formik>
    ```
    
- request body를 전달하는 방법
    - ```jsx
      export const updateTodoApi = (username, id, todo) => apiClient.put(`/users/${username}/todos/${id}`, todo);
      ```
- Date를 검증하고 싶다면? → moment 라이브러리
    - `!moment(values.targetDate).isValid()`

<br><br>

# REST API 보안
```jsx
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>
```

- spring security를 설정하면 기본적으로 모든 REST API에 보안을 설정한다.
- 아이디, 패스워드 설정
    
    ```jsx
    spring.security.user.name="DNA"
    spring.security.user.password="dummy"
    ```
    
- 프론트엔드에 인증 method를 추가하려면?
    
    ```
    export const retrieveHelloWorldWithParameter = (username) =>
      apiClient.get(`/hello-world/path-variable/${username}`, {
        headers: {
          Authorization: "Basic RE5BOmR1bW15",
        },
      });
    ```
    
    - 하지만 헤더만 추가한다면 preflight request 요청에 대한 응답이 access control check를 통과하지 못한다는 에러가 발생한다.
    - preflight request는 Options request method이다.  그렇다면 모든 사람에게 Options 요청에 대한 엑세스를 허용해 줘야 한다.
    - 또한, 위 코드와 같이 Authorization을 하드 코딩하는 것에서 데이터를 받아와 입력하는 식으로 바꿔야 한다.
- 이것들을 구현하기 위해 우리는 Token을 활용할 것이다.
    1. 인증을 받을 url을 만들어 준다.
        
        ```jsx
        	@GetMapping(path = "/basicauth")
        	public String helloWorld() {
        		return "Hello World";
        	}
        ```
        
    2. OPTIONS 요청에 대해 접근할 수 있게 한다.
        
        ```jsx
            @Bean
            public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                return http.authorizeHttpRequests(
                                auth -> auth
                                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                        .anyRequest().authenticated())
                        .httpBasic(Customizer.withDefaults())
                        .sessionManagement(
                                session -> session.sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS))
                        .csrf().disable()
                        .build();
            }
        ```
        
    3. 프론트엔드 쪽에서 Base64 인코딩을 통해 토큰을 만든다.
        1. `const baToken='Basic '+ window.btoa(*username* + ":" + *password*)`
        2. 여기서 Basic 뒤에 공백과 `btoa` 형식을 잘 지켜주어야 한다.
    4. 그리고 토큰을 인증 헤더로서 요청을 전송한다.
        
        ```jsx
        export const executeBasicAuthenticationService = (token) =>
          apiClient.get(`/basicauth`, {
            headers: {
              Authorization: token,
            },
          });
        ```
        
<br><br>

# async와 await
```jsx
    executeBasicAuthenticationService(baToken)
      .then((response) => console.log(response))
      .catch((error) => console.log(error));

    setAuthenticated(false);
```

- 위의 코드에서 setAuthenticated는 then 보다 먼저 실행된다.
- 이런 상황에서는 응답을 기다려야 하므로 async와 await가 필요하다.
    
    `AuthContext.js`
    
    ```jsx
        const response = await executeBasicAuthenticationService(baToken);
    
        try {
          if (response.status == 200) {
            setAuthenticated(true);
            setUsername(username);
            return true;
          } else {
            setAuthenticated(true);
            setUsername(username);
            return true;
          }
        } catch (error) {
          setAuthenticated(true);
          setUsername(username);
          return true;
        }
    ```
    
    `LoginComponent.js`
    
    ```jsx
      async function handleSubmit() {
        if (await authContext.login(username, password)) {
          navigate(`/welcome/${username}`);
        } else {
          setShowErrorMessage(true);
        }
      }
    ```
    
- 토큰을 모든 컴포넌트들이 쓸 수 있게 하려면?
    
    `AuthContext.js`
    
    ```jsx
    apiClient.interceptors.request.use(
    		(config) => {
    			console.log('intercepting and adding a token')
    			config.headers.Authorization = baToken
    			return config
    	}
    )
    ```
    
    - `/basicauth`로 부터 200 응답을 받았다면 apiClient의 헤더에 토큰을 추가해 준다.

<br><br>

# Json Web Token(JWT)
[JWT.IO](https://jwt.io/)

- Base64는 쉽게 디코딩할 수 있다. 그래서 프로덕션 시스템에서는 거의 사용되지 않는다.
- 때문에, 사용자 지정 토큰 시스템을 만들고 싶지만 이는 보안이 취약해질 수 있다.
- 이를 위해서 Json Web Token(JWT)라는 표준 토큰 시스템이 존재 한다.
- JWT에는 사용자 세부정보와 인증을 담을 수 있다.

    - **HEADER**
        - `type` → JWT
        - `algorithm` → 해싱 알고리즘이 HS256
    - **PAYLOAD** → JWT의 일부로서 우리가 갖고 있기를 원하는 속성
        - `iss` → JWT 토큰을 발행한 issuer
        - `sub` → 주제
        - `aud` → 대상이 목표로 하는 대상
        - `exp` → 토큰이 언제 만료되는지
        - `iat` → 토큰이 발행된 시기
