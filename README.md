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

      