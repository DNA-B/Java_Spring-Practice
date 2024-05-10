# 단위 테스트를 하는 이유?
- 대규모 어플리케이션에는 수천 개의 코드 파일과 수백만 줄의 코드가 있을 수 있다. 또한 이 코드들은 여러 레이어에 분산되어 있을 수도 있다.
- 애플리케이션을 빌드할 때마다 그게 제대로 작동하는지 확인해야 한다. 이 때, 예상하는 동작과 비교하여 애플리케이션의 동작을 확인하는 테스트를 진행한다.
- 테스트를 수행하는 방법
    1. System Testing, Integration Testing
        1. 전체 애플리케이션을 배포하고 테스트
        2. jar, war파일을 빌드하고 테스트 팀이 테스트하도록 한다.
    2. Unit Test
        1. 애플리케이션 코드의 특정한 단위를 독립적으로 테스트
        2. 어떤 특정 메소드나 메소드 그룹을 테스트한다.
- Unit Test의 장점
    - 버그를 조기에 발견할 수 있다.
        - 누군가가 내 코드를 커밋하거나 다른 버전을 관리할 때, 모든 단위 테스트를 실행하도록 빌드할 수 있으며, 그중 하나라도 실패하면 조기에 버그를 찾아낼 수 있다.
    - 버그를 수정하기 쉽다.
        - 단위 테스트에서는 특정 메서드에 해당하는 단위 테스트가 실패하면 그 특정 메서드에 문제가 있다는 사실을 알 수 있다. → System Testing과 반대
    - 장기적으로 비용을 절감한다.
        - 장기적으로 애플리케이션의 종합 유지 보수 관리에서 비용 절감에 기여한다.
- 가장 널리 쓰이는 Unit Test 프레임워크
    - JUnit → 단위 테스트 프레임워크
    - Mockito → 모킹 프레임워크

<br><br>

# JUnit 실행
- 단위테스트는 src 폴더에서 진행하지 않고, 별도의 폴더를 만든다.

```java
package com.dna.junit;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class MyMathTest {
    
    @Test
    void calculateSum() {
        // fail("Not yet implemented");
        
        int[] numbers ={1, 2, 3};
        MyMath myMath = new MyMath();
        int res = myMath.calculateSum(numbers);
        int expectedResult = 6;
        
        assertEquals(expectedResult, res);
        
        System.out.println("계산 결과는 " + res);
    }
}
```

- `fail` → assertion으로써 단위 테스트가 실패하면 에러를 발생시킨다.
- JUnit은 실패가 없으면 통과한다. 따라서 조건을 작성해야하고 이것을 `assert`라고 한다.
- 배열의 값을 모두 더하는 함수를 테스트 중이다. 계산 결과가 내 예측값과 다를 때 `assert`를 발생시켜야 하고, 여기서 `assertEquals`를 사용하면 (예측값, 결과값)을 비교할 수 있다.
    - 만약 여기서 값이 다르다면 `assert`가 발생한다.

[IntelliJ IDEA에 JUnit 추가하기  / 테스트 코드 작성](https://ildann.tistory.com/5)

<br><br>

# 테스트 개선
```java
class MyMathTest {
    private MyMath myMath = new MyMath();
    
    @Test
    void calculateSum_ThreeMemberArray() {
        assertEquals(6, myMath.calculateSum(new int[] {1, 2, 3}));
    }
    
    @Test
    void calculateSum_NoMemberArray() {
        assertEquals(0, myMath.calculateSum(new int[] {}));
    }
}
```

- 공통 변수를 클래스의 필드로 선언하고, 함수에 전달하는 값들은 인라인으로 정리하여 코드를 간결하게 작성할 수 있다.

<br><br>

# Assert 메소드
```java
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyAssertTest {
    
    List<String> todos = Arrays.asList("AWS", "Azure", "DevOps");
    
    @Test
    void testAsserts() {
        boolean test = todos.contains("AWS");//Result
        boolean test2 = todos.contains("GCP");//Result
        
        //assertEquals(true, test);
        assertTrue(test);
        assertFalse(test2);
        //assertNull, assertNotNull
        Assertions.assertArrayEquals(new int[] {1, 2}, new int[] {1, 2});
        
        assertEquals(3, todos.size());
    }
}
```

- `JUnit4`에서 `JUnit5`로 업그레이드되면서 import가 변경되었다.
    - `org.junit.Assert` → `org.junit.jupiter.api`

<br><br>

# JUnit Annotation

---

```java
class MyBeforeAfterTest {
    @Test
    void test1() {
        System.out.println("test1");
    }
    
    @Test
    void test2() {
        System.out.println("test2");
    }
    
    @Test
    void test3() {
        System.out.println("test3");
    }
}
```

- JUnit에서는 테스트 간의 실행 순서를 보장할 수 없다.
- 그래서 JUnit에서는 몇 가지 Annotation을 제공한다.
    - `@BeforeEach` → 각각의 test전에 `BeforeEach`가 적용된 함수를 실행한다.
        
        ```java
        @BeforeEach
        void beforeEach() {
            System.out.println("Before Each");
        }
        
        /*
        Before Each
        test1
        Before Each
        test2
        Before Each
        test3 
        */
        ```
        
        - 메소드 위치는 중요하지 않다.
    - `@AftereEach` → 각각의 test후에 `AftereEach`가 적용된 함수를 실행한다.
        
        ```java
        @AfterEach
        void afterEach() {
            System.out.println("After Each");
        }
        
        /*
        test1
        After Each
        test2
        After Each
        test3
        After Each
        */
        ```
        
- 테스트를 실행하기 전에 수행해야 할 설정이 있다면 `BeforeEach` 메서드를 활용한다. 각 테스트 후에 정리가 필요하다면 `AfterEach` 메서드에서 진행한다.
- 만약 모든 테스트에서 공통적으로 수행하려는 설정이 있을 경우?
    - `@BeforeAll` → test전에 `BeforeAll`을 ****실행한다.
        
        ```java
        @BeforeAll
        static void beforeAll() {
            System.out.println("Before All");
        }
        
        /*
        Before All
        test1
        test2
        test3
        */
        ```
        
    - `@AfterAll` → test후에 `AfterAll`을 ****실행한다.
        
        ```java
        @AfterAll
        static void afterAll() {
            System.out.println("After All");
        }
        
        /*
        test1
        test2
        test3
        After All
        */
        ```
        
    - `BeforeAll`과 `AfterAll`은 클래스 레벨 메서드이고, 모든 테스트 이전 혹은 이후에 실행되기 때문에 static으로 설정해야 한다.

```java
// 최종 출력 결과
Before All
Before Each
test1
After Each
Before Each
test2
After Each
Before Each
test3
After Each
After All
```
