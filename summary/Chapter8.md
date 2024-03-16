# node.js / npm / npx
- **Node.js**는 JavaScript의 서버 쪽 컴포넌트 기기에 Java를 설치하듯이 기기에 Node.js를 설치해서JavaScript 코드를 브라우저 없이 기기에서 바로 실행.
- **npm**은 JavaScript의 패키지 관리자
    - npm 프로젝트, 혹은 Node.js 프로젝트를 생성 → `npm init`
    - 라이브러리 추가 → `npm install <name>`
- npx는 패키지 실행 도구입니다
    - JavaScript 패키지를 설치하지 않고도 바로 실행할 수 있게 해준다.
    - react 설치 → `npx create-react-app <app name>`
    - react 실행 → 설치된 폴더로 이동 후, `npm start`
    - `npm start`로 앱을 실행하고 코드를 변경하면 `npm`은 자동으로 앱을 빌드하고 브라우저에 렌더링한다

<br><br>

# 주요 Node.js 명령어
- `npm start` → 애플리케이션을 개발 모드로 실행시킨다.
- `npm test` → JUnit과 비슷하게 유닛 단위 테스트를 할 수 있다.
- `npm run build` → 배포 가능 유닛을 프로덕션으로 만드는 것을 도와준다.
    - 최소화
    - 성능 최적화
- `npm install --save react-router-dom`

<br><br>

# React 파일 구조
- `README.md` → 문서
- `package.json` → `pom.xml`과 같은 종속성 정의 파일
- `node_modules/` → `package.json`에 정의된 모든 종속성이 다운로드 되는 폴더
- React Initialization
    - `public/index.html` → 브라우저에서 가장 먼저 로드되는 파일
    - `src/index.js` → 리액트 앱을 초기화하고 앱 컴포넌트를 로드
        - root div에 포함된다.
        - 앱 컴포넌트 → 실제 화면에 표시되는 컴포넌트
    - `src/App.js` → 앱 컴포넌트에 대한 코드
    - `index.css` → 전체 앱에 대한 스타일 정보
    - `App.css` → 특정 컴포넌트에만 적용되는 css
    - `App.test.js` → 앱 컴포넌트의 Unit test 파일

<br><br>

# React 컴포넌트
- 리액트를 사용하면 각 페이지 요소에 대해 별도 컴포넌트를 만든다.
- 리액트 애플리케이션을 모듈화 하는 것.
    - 메뉴 컴포넌트, 헤더 컴포넌트, 바닥글 컴포넌트 등이 있는 것이다.
- 별도의 컴포넌트가 있으면 모듈화가 잘 된 것이고 컴포넌트 재사용도 더 잘 된다.
- 보통 리액트 앱에서 처음으로 로드되는 컴포넌트는 **App** 컴포넌트이다.
- Component의 요소
    - **View** (JSX or JS) → HTML과 비슷한 View, 리액트는 JSX사용
    - **Logic** (JS) → 로직 정의
    - **Styling** (CSS) → 스타일 정의
    - **State** (Internal Data Store) → 컴포넌트의 내부 데이터 저장소와 같음.
    - **Props** (Pass Data) → 컴포넌트 사이에 데이터를 전달
- Component의 이름은 대문자로 시작해야한다.

<br><br>

# React 컴포넌트 생성
`Function Component`

```jsx
function App() {
  return (
    <div className="App">
      My Todo Application
      <FirstComponent></FirstComponent>
    </div>
  );
}

function FirstComponent() {
  return <div className="FirstComponent">First Component</div>;
}
```

- 컴포넌트 함수를 정의한다. → 함수를 사용하기 때문에 함수 컴포넌트라고 부른다.
- 그리고 App div에 추가해준다.

`Class Component`

```jsx
import { Component } from "react";

class ThirdComponent extends Component {
  render() {
    return <div className="ThirdComponent">Third Component</div>;
  }
}
```

- 클래스 컴포넌트도 존재한다. → `Component` 클래스를 `import`해주어야 한다.
- 메서드 이름을 render로 지정한다.
- 클래스 컴포넌트에서 우리가 정의하는 건 렌더링 메서드이다.
- 렌더링 메서드는 컴포넌트 일부로 보여주고 싶은 텍스트를 반환해야 한다.

<br><br>

# State
- State는 특정 컴포넌트에 대한 데이터나 정보이다.
- React 초기에는 클래스 컴포넌트만 state를 가질 수 있었다.
- Hooks의 등장으로 인해 함수 컴포넌트에도 state를 추가할 수 있게 되었다.
- 때문에 최근 버전의 React에서는 주로 함수 컴포넌트를 사용한다.

<br><br>

# return 내의 괄호 - JSX
- 괄호는 왜 쓰는 것이고 무엇을 반환하고 있는 것일까?
- React 프로젝트는 프레젠테이션을 JSX로 한다. → JavaScript XML의 약자
- JXM는 HTML보다 엄격하다.
    - 반드시 닫는 태그가 있어야 한다. → 빈 태그로 감싸는 것도 가능 `<></>`
    
    ```jsx
    <FirstComponent> // 오류 발생
    <FirstComponent></FirstComponent> // 정상 동작
    ```
    
    - 하나의 부모 요소가 있어야 한다. → 동시에 여러 태그 반환 X
    
    ```jsx
    <FirstComponent></FirstComponent>
    <SecondComponent></SecondComponent>
    <ThirdComponent></ThirdComponent>
    <FourthComponent></FourthComponent> // 오류
          
    <div className="App">
        <FirstComponent></FirstComponent>
    		<SecondComponent></SecondComponent>
    		<ThirdComponent></ThirdComponent>
    		<FourthComponent></FourthComponent>
    </div> // 정상 동작
    ```
    
    - 컴포넌트의 이름은 대문자로 시작해야 한다.
    - JSX에서는 `class`대신 `className`을 쓴다.
- 태그를 열고 닫는 것 대신 아래와 같이 표현할 수도 있다.
    
    ```jsx
    <div className="App">
        <FirstComponent />
        <SecondComponent />
        <ThirdComponent />
        <FourthComponent />
    </div> 
    ```
    
- JSX 포맷에서는 return이 있는 라인에 반드시 첫 번째 요소가 있어야 한다.
    - 그렇기 때문에 추천하는 방법으로 괄호를 사용하거나
    - return이 있는 라인으로 첫 번째 값의 위치를 옮겨주는 방식이 있다.

<br><br>

# Babel
- 리액트 애플리케이션을 개발할 때 가장 최신 버전의 표준을 사용할 것이다.
- 하지만 오래된 브라우저는 새로운 표준을 지원하지 않을 수 있다.
- 이에 대한 해결책으로 Babel이라는 것이 있다.
- Babel은 현대 JavaScript로 코드를 작성해도 옛날 브라우저에서 실행될 수 있도록 해준다.
- Babel도 JSX를 지원한다. Babel은 JavaScript XML 포맷을 이해하고 JavaScript로 변환해서 옛날 브라우저도 JSX를 이해할 수 있게 만들어준다.

<br><br>

# JavaScript의 모범 사례
- 각 컴포넌트는 각 파일에 들어있어야 한다.
    - 이후 `App.js`에서 컴포넌트를 `import`해야하고 해당 컴포넌트는 `export`해주어야 한다.
    - 모듈당 하나의 `default export`만 가능하다.
    - 중괄호를 이용해서 `import`하는 것은 무엇일까?
        - 중괄호를 쓰지 않으면 무조건 `default export`를 가져온다. → `**Default import**`
        - 만약 다른 컴포넌트를 불러와야한다면 중괄호를 이용해 `{모듈 이름}`으로 import해야 한다. → `**Named import**`
