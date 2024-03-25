# JSX 주석
- `/**/`에다가 중괄호로 감싸줘야 된다.
    - `{/*<TEXT>*/}`

<br><br>

# 컴포넌트 제어
- **form element (ex: login, password)**
    1. **React State** → **form**에 사용되는 변수 값을 유지할 수 있다.
    2. **form element** **자체의 값** → 요소에 오른쪽 마우스를 클릭하고 검사해보면 값을 저장하는 것이 DOM 요소인 것을 볼 수 있다. 이 값을 바꾸면 특정 DOM 요소가 자동으로 업데이트 된다.
    3. 리액트에서 **form element**를 사용할 때 이 두 가지가 동기화되어야 한다.

<br><br>

# JS에서의 좀 특이한….
- JS에서는 다른 언어에서 쓰이는 `==` 연산자를 `===`으로 쓴다.
- JS에서 `true && 어떤 텍스트`라면 반환값으로 텍스트가 나온다…..
- 반대로 `false && 어떤 텍스트`라면 반환값으로 `false`가 나온다…..

<br><br>

# React Router DOM - redirection
- `npm install react-router-dom` - in terminal
- 각 컴포넌트마다 route를 설정하고 싶다면?
    - `import { BrowserRouter, Routes, Route } from "react-router-dom";`
    
    ```jsx
          <BrowserRouter>
            <Routes>
              <Route path="/" element={<LoginComponent />}></Route>
              <Route path="/login" element={<LoginComponent />}></Route>
              <Route path="/welcome" element={<WelcomeComponent />}></Route>
            </Routes>
          </BrowserRouter>
    ```
    
- 다른 컴포넌트로 redirection을 하고 싶다면?
    - `import {useNavigate} from "react-router-dom";`
    - `const navigate = useNavigate();`
    - `navigate("/welcome");`
- 에러 페이지를 보여주고 싶다면?
    - `<Route path="*" element={<ErrorComponent />}></Route>`
    - `Routing`한 경로가 아니라면 모든 주소를 다 `ErrorComponent`에 연결
- path parameter를 이용하고 싶다면?
    - `import {useParams} from "react-router-dom";`
    - jsx에서 path parameter를 추가하려면 → `/login/:username`과 같이 사용
    - 해당 파라미터를 사용할 컴포넌트에서 `const params = useParams();`를 정의해주면 `params`에는 `username`이 전달된다.
    - 여기서 `params`는 객체이고, `params.username`는 속성이다.
    - 여기서도 구조 분해를 사용할 수 있는데 `const {username} = userParams();`를 통해 값을 직접 받을 수도 있다.
- JS 코드에서 문자열 안에 변수를 넣고 싶다면?
    - **틱**을 사용해야한다. → ESC 밑에 있는 것 ``
    - 그리고 `${variable}`과 같은 식으로 사용한다 → ``/welcome/${username}``

<br><br>

# JS - forEach
- 배열에는 forEach 함수가 있다. → C++과 Java의 iterate 반복문과 비슷하다.
    - `arrays.forEach( element => console.log(element))`
    - 배열의 각 요소를 `console.log`한다.
- 또한 map 함수도 있다. → Python의 map과 비슷하다.
    - `arrays.map(elt => elt.id)`
    - 배열의 각 요소를 id에 매핑하여 배열을 생성한다.
- 이를 활용해 todo list를 화면에 출력하고 싶다면?
    
    ```jsx
    {todos.map(
    	(todo) => (
    		<tr key={todo.id}>
    			<td>{todo.id}</td>
    			<td>{todo.description}</td>
    		</tr>
    	)
    )}
    ```
    
    - 리액트는 ID로 DOM 요소를 업데이트한다.
    - 때문에 리액트를 효율적으로 잘 사용하려면 각 요소가 유일한 키를 갖는 것이 좋다.
- 참고로 데이터들을 화면에 보여줄 때는 `String`으로 변환이 필요하다.

<br><br>

# redirection에 대하여
- welcome 페이지에서 todo 페이지로 넘어가기 위해 아래와 같이 작성했다고 가정하자.
    - `Manage Your Todos. - <a href="/todos">Go here</a>`
    - 이 코드의 문제는 전체 todos 페이지가 새로고침된다는 것이다.
- 여기서는 Link와 to를 사용하여 개선할 수 있다.
    - `a → Link / href → to` → 또한 Link to는 `<BrowserRouter>`안에 선언되어 있는 컴포넌트에만 사용할 수 있으므로 주의하자.
    - 지금 빌드하는 것은 단일 페이지 애플리케이션이다. → 외부 페이지라면 `<a>`사용
    - 단일 페이지 애플리케이션을 빌드할 때 전체 페이지를 새로 고치고 싶진 않으므로,
    - 바뀌어야 하는 이 특정 컴포넌트만 새로 고침 해야 할 때 Link를 사용한다.
    - `import {Link} from "react-router-dom";`
    - `Manage Your Todos. - <Link to="/todos">Go here</Link>`

<br><br>

# 헤더, 바닥글, 로그아웃 컴포넌트
```jsx
export default function TodoApp() {
  return (
    <div className="TodoApp">
      <BrowserRouter>
        <HeaderComponent />
        <Routes>
          <Route path="/" element={<LoginComponent />} />
          <Route path="/login" element={<LoginComponent />} />
          <Route path="/logout" element={<LogoutComponent />} />
          <Route path="/welcome/:username" element={<WelcomeComponent />} />
          <Route path="/todos" element={<ListTodosComponent />} />
          <Route path="*" element={<ErrorComponent />} />
        </Routes>
        <FooterComponent />
      </BrowserRouter>
    </div>
  );
}

function HeaderComponent() {
  return (
    <header className="header">
      <div className="container">Your Header</div>
    </header>
  );
}

function FooterComponent() {
  return (
    <footer className="footer">
      <div className="container">Your Footer</div>
    </footer>
  );
}

function LogoutComponent() {
  return (
    <div className="LogoutComponent">
      <h2>You are logged out!</h2>
      <div>Thank you for using our app.</div>
    </div>
  );
}

```

<br><br>

# Login 유지
- LoginComponent의 state는 로그인 후에 사라진다…어떻게 해야할까?
- 로그인을 했으면 로그인 state를 component끼리 공유하고 싶다.
    
    ```jsx
    import { createContext, useState } from "react";
    
    // Create a Context
    export const AuthContext = createContext();
    // Put some state in the context
    // Share the created context with other components
    
    export default function AuthProvider({ children }) {
      const [number, setNubmer] = useState(0);
      return <AuthContext.Provider value={{ number }}>{children}</AuthContext.Provider>;
    }
    
    ```
    
    - 따로 js 파일을 생성하여 AuthContext를 생성하고 AuthProvider를 통해 자식 컴포넌트들이 context를 공유할 수 있게 만든다.
    
    ```jsx
    import AuthProvider from "./security/AuthContext";
    
    export default function TodoApp() {
      return (
        <AuthProvider>
          <div className="TodoApp">
            <BrowserRouter>
              <HeaderComponent />
              <Routes>
                <Route path="/" element={<LoginComponent />} />
                <Route path="/login" element={<LoginComponent />} />
                <Route path="/logout" element={<LogoutComponent />} />
                <Route path="/welcome/:username" element={<WelcomeComponent />} />
                <Route path="/todos" element={<ListTodosComponent />} />
                <Route path="*" element={<ErrorComponent />} />
              </Routes>
              <FooterComponent />
            </BrowserRouter>
          </div>
        </AuthProvider>
      );
    }
    ```
    
    - AuthProvider안에 존재하는 컴포넌트들은 자식 컴포넌트가 되며 context를 공유할 수 있게 된다.
    
    ```jsx
    import { AuthContext } from "./security/AuthContext";
    
    export default function HeaderComponent() {
      const authContext = useContext(AuthContext);
      .
      .
      .
    }
    ```
    
    - 자식 컴포넌트에서 context를 활용하고자 할 때는 AuthContext를 import해서 useContext를 통해 사용하면 된다.

<br><br>

# code refactoring
- `setInterval(() => setNubmer(number + 1), 10);`
    - n초 마다 함수 실행(**ms** 단위)
- 컴포넌트에서 공유 `context`를 사용하려면 아래와 같은 것이 필요하다.
    - `import AuthContext, useContext`
    - 근데 이것을 좀 더 간단하게 만들고 싶다면 `hook`을 생성할 수 있다.
    - `export const useAuth = () => useContext(AuthContext);`
    - 이제 컴포넌트에서 `useAuth`를 `import`하면 더 편하게 `context`를 사용할 수 있는 것이다.
