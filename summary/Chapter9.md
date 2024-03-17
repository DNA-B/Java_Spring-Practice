# Counter
- `onClick`에 함수를 넣을 때
    - 문자열로 넣으면 안된다.
    - 함수 호출형태로 넣으면 안된다 → `() X`
    - 중괄호를 열고 함수의 이름을 넣어야 한다.
    - 중괄호를 열고 함수 이름 뒤에 소괄호를 넣는 것도 안된다. → onClick에 함수 그 자체를 매핑해야 하기 때문이다. → `()`를 넣으면 함수 자체를 실행하는 셈.

<br><br>

# React에서의 styling 옵션
1. style 속성 → `<button style={{borderRadius: "30px"}>`
    1. style에는 객체 형식이 들어가야 하기 때문에 중괄호를 넣어주어야 한다.
    2. 또한 javascript에서는 프로퍼티 이름에 `-`를 포함할 수 없다. → 대문자로 표시
    3. 앞뒤에서 중괄호를 한 번 더 여닫아줘야 한다.
    4. 속성값을 javaScript 객체로 정의하는 방식도 있다.
        1. `const buttonStyle = { fontSize: "30px" };`
        2. `style={buttonStyle}`
    5. 하지만 위와 같은 JavaScript 객체 정의보다 css파일을 생성하는 것이 권장되는 방식이다.
2. className
    1. css에서는 대문자가 아니라 `-`를 사용해야한다.
    2. 또한 모든 속성의 끝에 `;`를 붙여야 한다.
    3. 그리고 속성에 `" "`를 붙여줄 필요가 없다.
    4. js파일에서는 css파일을 `import`해야 한다.

<br><br>

# useState
- State는 리액트의 내장 객체로서 컴포넌트의 데이터 또는 정보를 저장하는 데 사용된다.
    - 초기 버전의 리액트에선는 클래스 컴포넌트만 state를 가질 수 있었다.
    - 16.8번전에 Hooks가 도입되면서 함수형 컴포넌트에서도 사용할 수 있게 되었다. → `useState Hook`
- useState에는 기본값을 전달할 수 있다. → `const state = useState(0);`
- useState는 2가지를 return한다.
    1. current state
    2. A function to update state
    3. 위의 경우 `state[0]`은 값, `state[1]`은 함수이다.
- 위의 경우 count값을 변경시키려면 `state[1](state[0] + 1)`과 같이 작성할 수 있다.
    - 그러나 이러한 복잡한 코드는 효율성이 떨어지므로 state를 선언할 때 `const [count, setCount] = useState(0);`와 같이 **구조 분해**를 사용한다.
    - 이 경우, `count`에는 `useState()`에서 반환된 첫 번째 요소, 즉 현재 상태 값이 저장되고 `setCount`에는 상태 값을 업데이트하는 함수가 저장된다.

<br><br>

# 백그라운드에서 일어나는 일
- 버튼을 누를 때마다 리액트가 뷰를 업데이트 했다.
    - 일반적으로 HTML 페이지는 DOM 요소로 표현된다.
    - HTML 페이지의 각 요소는 DOM 노드에 해당한다.
    - 요소를 업데이트하려면 DOM을 업데이트 해야 한다.
    - 하지만 DOM 업데이트 코드를 작성하는 것은 복잡하고 시간이 많이 든다. → HTML 코드 안에서 업데이트가 일어나는 값의 위치를 찾아내 변경해야 했다. → 직접 업데이트했다.
- 그러나 리액트는 Virtual DOM을 이용한다. → 가상으로 만든 UI 표현
    - 리액트는 HTML 전체를 가상으로 만들어 메모리에 보관한다.
    - 우리는 이 가상 DOM을 업데이트하는 코드를 작성한다.
    - 가상 DOM이 업데이트되면 리액트가 변경 사항을 파악해 HTML 페이지에 동기화 시킨다.
- Count 프로그램에서의 동작과정
    1. 페이지가 로딩되면 리액트는 가장 먼저 이에 대한 첫 번째 버전의 가상 DOM을 생성한다.
    2. +1버튼을 클릭한다면 state가 업데이트된다.
    3. 리액트는 state가 업데이트되었으므로 컴포넌트를 다시 렌더링하고 두 번째 버전의 가상 DOM을 생성한다.
    4. 가상 DOM v1과 v2를 비교해 차이점을 파악하고 달라진 부분을 HTML 페이지에 반영한다.

<br><br>

# React Props
- 1, 2, 5를 증감 시키는 카운터를 따로 만들고싶다. 그러나 중복되는 코드를 여러 개 작성하는 것은 바람직하지 않다.
- 어떻게 증분 값을 다르게 설정할 수 있을까? → **Props(property)**
    - 하나의 컴포넌트는 여러 개의 프로퍼티를 가질 수 있다.
    - 이 Props를 다른 리액트 컴포넌트에 넘겨줄 수도 있다.
    - Props는 컴포넌트가 실행되는 동안 유지해야 하는 값이 있을 때 사용한다.

```jsx
function App() {
  return (
    <div className="App">
      <Counter initCount={1} />
      <Counter initCount={2} />
      <Counter initCount={5} />
    </div>
  );
}
```

- 컴포넌트로 전달할 property를 써준다.
    - 숫자 값을 넘길 때는 중괄호를 써준다.
    - 문자열을 넘길 때는 따옴표로 감싸준다.

```jsx
export default function Counter({ initCount }) {
  const [count, setCount] = useState(0);

  function incrementCounterFunction() {
    setCount(count + initCount);
  }

  function decrementCounterFunction() {
    setCount(count - initCount);
  }
	.
	.
	.
}
```

- 전해 준 property를 받기 위해 function의 인자에 property를 입력한다.
    - `{}`를 사용하지 않으면 property.initCount와 같은 방식으로 사용하게 된다.
    - 이 때문에, state를 정의할 때 사용한 구조 분해를 이용할 수 있고 중괄호 안에 넣으면 initCount를 그대로 사용할 수 있게 된다.
- Props에 특정 타입만 들어갈 수 있게 하려면?
    - PropTypes를 정의하면 된다. → `import { PropTypes } from "prop-types";`
    - 또한 defaultProps를 통해 property를 전달하지 않더라도 prop의 기본값을 지정할 수 있다.
        
        ```
        // prop의 타입 지정 -> 프로그램이 종료되지는 않고, warning이 발생한다
        Counter.propTypes = {
          initCount: PropTypes.number,
        };
        
        // 기본값 지정
        Counter.defaultProps = {
          initCount: 1,
        };
        ```
        
<br><br>

# Common State
- 모든 Counter가 하나의 state 값을 변경할 수 있게 하고 싶다.
- 이 때 Common State를 사용한다. → 계층 구조를 생성한다.
- 하위 컴포넌트는 상위 컴포넌트의 메소드에 접근할 수 없다.
    - 때문에 상위 컴포넌트에서 하위 컴포넌트로 메소드를 넘겨주어야 한다.
    - 받은 메소드를 onClick에 사용할 때는 다음과 같이 작성하면 된다.
        - `onclick={() => incrementCounterFunction(by)}`
- `Counter.jsx`
    
    ```jsx
    import { useState } from "react";
    import CounterButton from "./CounterButton";
    import ResetButton from "./ResetButton";
    import "./Counter.css";
    
    export default function Counter() {
      const [count, setCount] = useState(0);
    
      function incrementCounterParentFunction(by) {
        setCount(count + by);
      }
    
      function decrementCounterParentFunction(by) {
        setCount(count - by);
      }
    
      function resetCounter() {
        setCount(0);
      }
    
      return (
        <div>
          <CounterButton
            by={1}
            incrementMethod={incrementCounterParentFunction}
            decrementMethod={decrementCounterParentFunction}
          />
          <CounterButton
            by={2}
            incrementMethod={incrementCounterParentFunction}
            decrementMethod={decrementCounterParentFunction}
          />
          <CounterButton
            by={5}
            incrementMethod={incrementCounterParentFunction}
            decrementMethod={decrementCounterParentFunction}
          />
          <span className="totalCount">{count}</span>
          <ResetButton resetMethod={resetCounter} />
        </div>
      );
    }
    
    ```
    
- `CounterButton.jsx`
    
    ```jsx
    import { PropTypes } from "prop-types";
    
    export default function CounterButton({
      by,
      incrementMethod,
      decrementMethod,
    }) {
      return (
        <div className="CounterButton">
          <div>
            <button className="counterButton" onClick={() => incrementMethod(by)}>
              +{by}
            </button>
            <button className="counterButton" onClick={() => decrementMethod(by)}>
              -{by}
            </button>
          </div>
        </div>
      );
    }
    
    CounterButton.propTypes = {
      by: PropTypes.number,
    };
    
    CounterButton.defaultProps = {
      by: 1,
    };
    
    ```
    
- `ResetButton.jsx`
