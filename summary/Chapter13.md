# 모킹이란?
- 단위 테스트가 훌륭하면 유지 보수가 간단해지지만 훌륭한 단위 테스트를 작성하기는 쉽지 않다.
- 수 많은 클래스가 서로 통신을 하고 있는 상황에서
    - 여러 종속 항목을 포함하는 클래스에서 단위 테스트를 어떻게 작성할까?
    - 특정 클래스에 존재하는 종속 항목을 어떻게 대체할 수 있을까?
- 데이터베이스에 저장된 데이터를 사용하지 않고, 비즈니스 계층의 단위 테스트를 실행하고 싶다면 **Stub**과 **Mock**를 사용할 수 있다.

<br><br>

# 예시 프로젝트 만들기
- Mockito는 spring-boot-starter-test에 포함되어 있다.
    
    ```xml
    		<dependency>
    			<groupId>org.springframework.boot</groupId>
    			<artifactId>spring-boot-starter-test</artifactId>
    			<scope>test</scope>
    		</dependency>
    ```
    
- 비즈니스 계층과 데이터 계층을 포함하는 간단한 프로젝트를 만들어보자.
    
    ```java
    package com.dna.mockito.mockito_demo.business;
    
    public class SomeBusinessImpl {
        
        private DataService dataService;
        
        public int findTheGreatestFromAllData() {
            int[] data = dataService.retrieveAllData();
            int greatestValue = Integer.MIN_VALUE;
            
            for (int value : data) {
                if (value > greatestValue)
                    greatestValue = value;
            }
            
            return greatestValue;
        }
    }
    
    interface DataService {
        int[] retrieveAllData();
    }
    
    ```
    
<br><br>

# Stub을 이용한 단위 테스트
- 위의 클래스를 Test 코드로 작성해준다. → test 폴더에 작성해주어야 한다.
    
    ```java
    package com.dna.mockito.mockito_demo.business;
    
    import org.junit.jupiter.api.Test;
    
    class SomeBusinessImplTest {
        
        @Test
        void test() {
            SomeBusinessImpl businessImpl = new SomeBusinessImpl();
            businessImpl.findTheGreatestFromAllData();
        }
        
    }
    ```
    
    - 만약 그대로 실행한다면 `SomeBusinessImpl`의 `dataService`는 null값이므로 `NullPointerException`이 발생한다.
    - 따라서 생성자를 만들어준 후 test코드에서 DataService에 대한 `Stub`을 생성하여 전달해주어야 한다.
    
    ```java
    package com.dna.mockito.mockito_demo.business;
    
    import org.junit.jupiter.api.Test;
    
    import static org.junit.jupiter.api.Assertions.assertEquals;
    
    class SomeBusinessImplTest {
        
        @Test
        void findTheGreatestFromAllData_basicScenario() {
            SomeBusinessImpl businessImpl = new SomeBusinessImpl(new DataServiceStub());
            int result = businessImpl.findTheGreatestFromAllData();
            assertEquals(25, result);
        }
        
    }
    
    class DataServiceStub implements DataService {
        @Override
        public int[] retrieveAllData() {
            return new int[] {25, 15, 5};
        }
    }
    ```
    
- 이 방법의 문제점은 무엇일까?
    - DataService에 새 메소드를 추가할 때마다 DataServiceStub 구현 클래스를 업데이트해줘야 한다.
    - Stub을 이용하면 많은 시나리오를 테스트하기가 매우 어렵다. 시나리오가 늘어날 때마다 Stub이 추가되기 때문이다.

<br><br>

# Mockito
- Stub을 사용하면 생기는 문제점을 보완하고자 사용하는 것이 Mock이다.
- Mockito가 제공하는 `mock()`함수에 interface를 전달하여 Mock을 생성할 수 있다.
    
    ```java
    package com.dna.mockito.mockito_demo.business;
    
    import org.junit.jupiter.api.Test;
    
    import static org.junit.jupiter.api.Assertions.assertEquals;
    import static org.mockito.Mockito.mock;
    
    class SomeBusinessImplMockTest {
        @Test
        void findTheGreatestFromAllData_basicScenario() {
            DataService dataServiceMock = mock(DataService.class);
            SomeBusinessImpl businessImpl = new SomeBusinessImpl(dataServiceMock);
            int result = businessImpl.findTheGreatestFromAllData();
            assertEquals(25, result);
        }
    }
    ```
    
    - 그러나 이대로 실행하면 `NullPointerException`이 발생한다.
    - mock이 값을 반환하게 만들어줘야 한다.
    
    ```java
        @Test
        void findTheGreatestFromAllData_basicScenario() {
            DataService dataServiceMock = mock(DataService.class);
            when(dataServiceMock.retrieveAllData()).thenReturn(new int[] {25, 15, 5});
            
            SomeBusinessImpl businessImpl = new SomeBusinessImpl(dataServiceMock);
            int result = businessImpl.findTheGreatestFromAllData();
            assertEquals(25, result);
        }
    ```
    
- Mock을 사용하면 시나리오가 추가되더라도 Stub에 비해 작성하는 코드가 매우 줄어든다.
    
    ```java
    package com.dna.mockito.mockito_demo.business;
    
    import org.junit.jupiter.api.Test;
    
    import static org.junit.jupiter.api.Assertions.assertEquals;
    import static org.mockito.Mockito.mock;
    import static org.mockito.Mockito.when;
    
    class SomeBusinessImplMockTest {
        @Test
        void findTheGreatestFromAllData_basicScenario() {
            DataService dataServiceMock = mock(DataService.class);
            when(dataServiceMock.retrieveAllData()).thenReturn(new int[] {25, 15, 5});
            SomeBusinessImpl businessImpl = new SomeBusinessImpl(dataServiceMock);
            assertEquals(25, businessImpl.findTheGreatestFromAllData());
        }
        
        @Test
        void findTheGreatestFromAllData_OneValue() {
            DataService dataServiceMock = mock(DataService.class);
            when(dataServiceMock.retrieveAllData()).thenReturn(new int[] {35});
            SomeBusinessImpl businessImpl = new SomeBusinessImpl(dataServiceMock);
            assertEquals(35, businessImpl.findTheGreatestFromAllData());
        }
    }
    ```
    
<br><br>

# Mockito 어노테이션
- Mock을 더 쉽게 작성하는 법을 배워보자.
- `@Mock`과 `@InjectMocks`을 이용하여 더 간결한 코드를 작성할 수 있다.
    
    ```java
    package com.dna.mockito.mockito_demo.business;
    
    import org.junit.jupiter.api.Test;
    import org.mockito.InjectMocks;
    import org.mockito.Mock;
    
    import static org.junit.jupiter.api.Assertions.assertEquals;
    import static org.mockito.Mockito.mock;
    import static org.mockito.Mockito.when;
    
    class SomeBusinessImplMockTest {
        @Mock
        private DataService dataServiceMock;
        @InjectMocks
        private SomeBusinessImpl businessImpl;
        
        @Test
        void findTheGreatestFromAllData_basicScenario() {
            when(dataServiceMock.retrieveAllData()).thenReturn(new int[] {25, 15, 5});
            assertEquals(25, businessImpl.findTheGreatestFromAllData());
        }
        
        @Test
        void findTheGreatestFromAllData_OneValue() {
            DataService dataServiceMock = mock(DataService.class);
            when(dataServiceMock.retrieveAllData()).thenReturn(new int[] {35});
            SomeBusinessImpl businessImpl = new SomeBusinessImpl(dataServiceMock);
            assertEquals(35, businessImpl.findTheGreatestFromAllData());
        }
    }
    ```
    
    - `@Mock` → 해당 어노테이션을 통해 DataService를 Mock으로 만들 것을 알려준다.
    - `@InjectMocks` → Mockito가 `SomeBusinessImpl` 클래스에 Mock을 주입해야 함을 알려준다.
    - 이대로 실행하면 또 다시 NullPointerException이 발생한다. 따라서 class위에 `*@ExtendWith*(MockitoExtension.*class*)` 어노테이션을 추가해주어야 한다.
    - MockitoExtention을 통해 Mock과 InjectMock이 실제 Mock으로 대체된다.
- 어노테이션을 통해 더욱 시나리오를 추가하기가 쉬워졌다.

<br><br>

# 여러 값을 연속으로 테스트하기
- 만약 Mock에서 값을 연속으로 return하고 그것에 대한 테스트를 연속으로 진행하고 싶다면?
    
    ```java
        @Test
        void multipleReturnsTest() {
            List listMock = mock(List.class);
            when(listMock.size()).thenReturn(1).thenReturn(2);
            assertEquals(1, listMock.size());
            assertEquals(2, listMock.size());
        }
    ```
    
    - 다음과 같이 `thenReturn`을 여러 번 해주고 `assertEquals`또한 여러 번 진행해주면 된다.
    - 여러 번 진행할 시 마지막 return값이 기본 값이 된다.
    - 즉, `assertEquals`를 밑에 3개를 더 추가하여도 2가 기본값으로 들어간다.
- 특정 파라미터를 통해 테스트 하고 싶다면?
    
    ```java
        @Test
        void specificParametersTest() {
            List listMock = mock(List.class);
            when(listMock.get(0)).thenReturn("SomeString");
            assertEquals("SomeString", listMock.get(0));
            assertEquals(null, listMock.get(1));
        }
    ```
    
    - Mockito는 `listMock.get(1)`과 같이 return값이 없는 상황에서 `null`을 반환한다.
- 정해지지 않은 파라미터를 테스트하고 싶다면?
    
    ```java
        @Test
        void genericParametersTest() {
            List listMock = mock(List.class);
            when(listMock.get(Mockito.anyInt())).thenReturn("SomeOtherString");
            assertEquals("SomeOtherString", listMock.get(0));
            assertEquals("SomeOtherString", listMock.get(1));
        }
    ```
    
    - `Mockito.any{type}`과 같이 들어오는 타입을 정해주고 return값을 지정해줄 수 있다.
    - 어느 `Int`값이 들어오든 `"SomeOtherString"`이 반환되도록 하였으므로 해당 테스트에서는 `listMock.get(1)`이 `null`을 반환하지 않는다.
