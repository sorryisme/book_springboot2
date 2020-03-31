#  처음으로 배우는 스프링 부트 2



## gradlew

- chomod 755 gradlew
- ./gradlew wrapper --gradle-version 4.8.1
- ./gradlew -v 



## 그레이들 멀티 프로젝트 구성

- 여러 프로젝트를 하나의 프로젝트 처럼 구성 가능
  - 웹 , API, 배치, 기타 프로젝트
  - 네 프로젝트 모두 공통된 도메인 이나 유틸리티를 사용하는데 멀티 프로젝트로 구성하지 않으면 일일히 모두 수정해야하는 문제가 발생
  - 멀티프로젝트로 구성하면 중복코드 제거와 실수를 줄일 수 있음
- 설정
  - settings.gradle : 그레이들 설정파일
    - rootProejct.name = '[프로젝트명]'
  - 테스트로 사용할 모듈과 공용 도메인 모듈 추가
    - [new] - [Module] -[gradle] -[java] 선택 후 프로젝트명 작성
  - 기본 패키지 경로를 수동으로 추가
    - src/main/java
    - src/test/java
    - src/main/resource/static
    - src/main/resources/teplates
  - settings.gradle에 include되었는지 확인

## 환경 프로퍼티 파일 서정하기

- Application.properties로 서버설정이 가능

  - server.port: 80

- 최근이 YAML파일을 많이 사용(*.yml)

- 프로파일 별로 환경 구성 분리 : --- 으로 구분

  ```
  server:
      port:80
  
  ---
  spring:
    profiles: local
  
  server:
    port:8080
  
  ---
  spirng:
    profiles: dev
  
  server:
      port:8081
  
  ---
  spring:
    profiles: real
  server:
    port: 8082
  ```

- Application-{profile}.yml을 이용

  - {profile}에 원하는 프로파일 값으로 추가하면 우선순위 1순위로 설정이 지정됨

- JAR 파일로 빌드 하여 프로파일 값을 활성화 가능

  ```
  java -jar ... -D spring.profiles.active=dev
  ```

- YAML 파일 매핑

  - @Value : 유연한 바인딩 X , 메타데이터 지원 X, SpEL 평가 O

  - @ConfigurationProperties : 유연한 바인딩 O , 메타데이터 지원 O, SpEL 평가 X

    - 유연한 바인딩 : 프로퍼티 값을 낙타 표기법으로 선언하고 다른 표기법으로 선언하도록 처리
    - 메타 데이터 지원 : 프로퍼티 키에 대한 정보를 메타데이터 파일(JSON)로 제공
    - SpEL : 객체 참조에 대해 질의하고 조작하는 기능을 지원하는 언어

  - @Value : test Property

    ```java
    property :
      test :
        name : property depth test
    
    propertyTest : test
    propertyTestList: a,b,c
    ```

  - Value 매핑

    ```java
    		@Value("${property.test.name}")
        private String propertyTestName;
    
        @Value("${propertyTest}")
        private String propertyTest;
    
        @Value("${noKey:default value}")
        private String defaultValue;
    
        @Value("${propertyTestList}")
        private String[] propertyTestArray;
    
        @Value("#{'${propertyTestList}'.split(',')}")
        private List<String> propertyTestList;
    ```

    - 존재하는 키에 대해서 ' . ' 구분하여 매핑
    - 단일 키 매핑
    - 키가 존재하지 않으면 디폴트 값 지정
    - 배열형으로 매핑
    - SpEL을 사용하여 List에 매핑

  - @ConfigurationProperties : 접두사(prefix)를 사용하여 바인딩 

    ```
    fruit:
      list:
        - name : banana
          color : yellow
        - name : apple
          color : red
        - name : water melon
          color: green
    ```

  - Properteis 매핑

    ```java
    @Data
    @Component
    @ConfigurationProperties("fruit")
    public class FruitProperty {
        private List<Map> list;
    
    }
    ```

  - @ConfigurationProperties의 유연한 바인딩

    - 필드를 낙타 표기법으로 선언 하고 프로퍼티의 키는 다양한 형식으로 선언 및 바인딩
- 스프링부트2.0 부터 소문자나 케밥 표기법만 지원



### 자동 환경 설정 이해하기

- @EnableAutoConfiguration 또는 @SpringBootApplication 중 하나를 사용

  ```java
  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  @Inherited
  @SpringBootConfiguration
  @EnableAutoConfiguration
  @ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
  		@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
  public @interface SpringBootApplication {
  ```

  - @SpringBootConfigiration : 스프링의 @Configuration을 대체함 
  - @EnableAutoConfiguration : 자동 설정의 핵심 애노테이션, 클래스 경로에 지정된 내용을 기반으로 설정 자동화 수행
  - @ComponentScane : 별도 값을 서정하지 않으면 루트 경로가 설정됨

- @EnableAutoConfiguration 

  ```java
  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  @Inherited
  @AutoConfigurationPackage
  @Import(AutoConfigurationImportSelector.class)
  public @interface EnableAutoConfiguration {
  ```

  - @Import(AutoConfigurtionImnportSelector.class)
    - 빈의 등록과 자동 설정에 필요한 파일
      - META-INF/spring.factories : 자동설정 클래스 목록, @EnableAutoConfiguration 사용 시 자동 설정 타깃
      - META-INF/spring-configuration-metadata.json : 자동설정에 사용할 프로퍼티 정의 파일
      - org/springframework/boot/autoconfigure : 미리 구현해 놓은 자동설정 리스트 
    - Spring-boot-autoconfiguration에 미리 정의 되어 있음 
  - 스프링 프로퍼티 문서를 통해서 프로퍼티 값을 확인할 수 있음
    - https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/reference/html/appendix-application-properties.html#common-application-properties

## 스프링 부트 테스트

 -	Spring-boot-test, spring-boot-test-autoconfigure 모듈로 구성
 -	자주 사용하는 애노테이션
      	-	@SpringBootTest
         	-	@WebMvcTest
         	-	@DataJpaTest
   	-	@RestClientTest
   	-	@JsonTest



### @SpringBootTest

- @SpringBootTest 어노테이션을 사용하려면 SpringJunit4ClassRunner 클래스를 상속받은 @Runwith(SpringRunner.class)를 사용해야한다
  - @Runwith 어노테이션을 사용하면 Junit에 내장된 러너 대신 어노테이션에 정의된 러너 클래스 사용

```javascript
@RunWith(SpringRunner.class)
@SpringBootTest(
        value = "value=test",
        properties = {"property.value= propertyTest "},
        classes = {SpringBootTestApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringBootTestApplication {
```

- value : 테스트 실행전 프로퍼티 주입
- properties : key = value 형태로 프로퍼티 추가
- classes : 어플리케이션 컨텍스트에 로드할 클래스 지정 가능 ,따로 지정하지 않으면 SpringBootConfigiration을 찾아서 로드
- webEnvirment : 웹 환경 설정 , 기본값은 Mock 서블릿 로드



### 추가적인 팁

- 프로파일과 다른 데이터소스를 가진다면 어떻게 해야할까요?
  - @ActiveProfiles("local")과 같은 방식으로 원하는 프로파일 환경을 부여
- 테스트에서 @Transactional을 사용하면 데이터가 롤백됩니다. 다만 테스트가 서버의 다른 스레드에서 실행 중이면 WebEnviroment의 RANDOM_PORT나 DEFINED_PORT를 사용하여 테스트를 수행해도 트랜잭션이 롤백되지 않는다.
- @SpringBootTest는 기본적으로 검색알고리즘을 사용하여 @SpringBootApplication이나 @SpringBootConfiguiration어노테이션을 찾습니다. 스프링부트 테스트는 해당 어노테이션 중 하나는 필수입니다.



### @WebMvcTest

- 웹에서 테스트하기 힘든 컨트롤러를 테스트하는데 적합
- 요청과 응답에 대해 테스트
- 시큐리티 필터까지 자동으로 테스트, 수동으로 추가/삭제 가능



#### 사용 이유

 다음과 같은 관련 내용만 로드하기에 @SpringBootTest보다 가볍게 테스트 할 수 있음

- @Controller
- @ControllerAdvice
- @JsonComponent
- filter
- WebMvcConfigure
- HandlerMethodArgumentResolver



```java
package community.community.controller;


import community.community.domain.Book;
import community.community.service.BookService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @Test
    public void Book_MVC_테스트() throws Exception {
        Book book = new Book("Spring Boot book", LocalDateTime.now());
        given(bookService.getBookList()).willReturn(Collections.singletonList(book));
      
        mvc.perform(get("/books"))
            .andExpect(status().isOk())
                .andExpect(view().name("book"))
                .andExpect(model().attributeExists("bookList"))
                .andExpect(model().attribute("bookList", Matchers.contains(book)));
    }

}

```

- @WebMvcTest를 사용하기 위해 테스트할 특정 Controller 명을 지정해줘야한다
- MockMvc는 BookController 관련 빈만 로드하여 Http 서버를 실행하지 않고 테스트할 수 있다.
- @Service 어노테이션은 @WebMvcTest 대상이 아니기 때문에 MockBean이라는 가짜 객체를 통해 테스트를 진행
- given을 통해서 반환값을 미리 정의한다
- MockMvc를 이용하여 다음과 같이 측정하였음
  - status().isOk : 상태값 200 확인
  - view().name("book") : 반환 뷰이름이 book인지 테스트
  - andExpect(model().attributeExists("bookList")) : bookList라는 프로퍼티 존재 여부 확인
  - AndExpect(model().attribute("bookList", contains(book))) : 모델 프로퍼티 중 bookList라는 객체가 담겨져 있는지 테스트

### @DataJpaTest

- JPA 관련 테스트 설정만 로드 

- 기본적으로 인메모리 데이터베이스를 사용

  ```java
  @RunWith(SpringRunner.class)
  @DataJpaTest
  @ActiveProfiles("...")
  @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
  public class JpaTest {
  }
  
  ```

  - Replace.NONE으로 설정한 뒤에 @ActiveProfiles에 설정한 프로파일 환경값에 따라 데이터 소스가 적용



- 이 외에도 JDBC를 테세트하는 @JdbcTest, 몽고디비를 테스트하는 @DataMongoTest 어노테이션 등이 있음




### @RestClientTest

- REST 관련 테스트를 도와주는 애노테이션

- @RestController

  - 객체를 리턴해도 RestController 애노테이션이 JSON 형태 String으로 반환한다.

- RestTemplate best practice

  ```java
  @Service
  public class BookRestService {
  
      private final RestTemplate restTemplate;
  
      public BookRestService(RestTemplateBuilder restTemplateBuilder){
          this.restTemplate = restTemplateBuilder.rootUri("/rest/test").build();
      }
  
      public Book getRestBook(){
          return this.restTemplate.getForObject("/rest/test", Book.class);
      }
  }
  ```

  - RestTemplateBuilder는 RestTemplate를 핸들링 하는 빌더 객체 , connetionTimeout,ReadTimeout 설정 외에 다른 설정을 간편히 처리하도록 해줌

- RestClientTest 코드

  ```java
  @RunWith(SpringRunner.class)
  @RestClientTest(BookRestService.class)
  class BookRestControllerTest {
  
      @Rule
      public ExpectedException thrown = ExpectedException.none();
  
      @Autowired
      private BookRestService bookRestService;
  
      @Autowired
      private MockRestServiceServer server;
  
      @Test
      public void rest_테스트(){
          this.server.expect(requestTo("/rest/test"))
                      .andRespond(withSuccess(new ClassPathResource("/test.json",getClass()), MediaType.APPLICATION_JSON));
  
          Book book = this.bookRestService.getRestBook();
          assertThat(book.getTitle()).isEqualTo("테스트");
      }
  
      @Test
      public void rest_error_테스트(){
          this.server.expect(requestTo("/rest/test"))
                      .andRespond(withServerError());
          this.thrown.expect(HttpServerErrorException.class);
          this.bookRestService.getRestBook();
      }
  }
  ```

  - @RestClinetTest 테스트 대상이 되는 빈을 주입받음
  - @Rule로 지정한 필드값은 @Before, @After 애노테이션 상관없이 값을 초기화
  - Mock RestServiceServer는 클라이언트와 서버 사이의 Rest테스트를 위한 객체이며 예상되는 반환값과 에러를 반환하도록 명시하여 테스트를 진행할 수 있도록 구현
  - ClassPathResource("/test.json",getClass()) 
    - /test/resources/test.json 파일에서 값을 가져와서 응답을 주도록 처리
    - 경로는 /test/resource로 지정할 것
  - rest_error_테스트 의 경우 500에러를 발생시키고 thrown.expect에서 500에러를 감지하여 확인하는 테스트 코드



### @JsonTest





