# Spring Boot Study
> Spring Boot Project를 진행하며 경험한 issue를 해결하며 체득, 학습한 내용 모든 것을 정리하는 공간.

## JPA
1. [JPA 조회 최적화](https://github.com/jayjaehunchoi/JavaStudy/blob/main/SpringBootProject/JPA/JPA%20%EC%A1%B0%ED%9A%8C%20%EC%B5%9C%EC%A0%81%ED%99%94.md) : ```JPA```에서 조회할 때 발생하는 issue (대표적으로 1+N) 을 ```fetch join```으로 해결하고, ```dto```를 직접 조회하여 성능 최적화. 
2. [JPA 조회 최적화(2)](https://github.com/jayjaehunchoi/JavaStudy/blob/main/SpringBootProject/JPA/JPA%20%EC%A1%B0%ED%9A%8C%20%EC%B5%9C%EC%A0%81%ED%99%94(2).md) : ```1대다```연관관계에서 발생하는 ```join``` 이슈(중복 데이터, 페이징 불가) 1+1로 해결 ```default_batch_fetch```
3. [삭제 성능 개선](https://github.com/jayjaehunchoi/JavaStudy/blob/main/SpringBootProject/JPA/%EC%82%AD%EC%A0%9C%20%EC%84%B1%EB%8A%A5%20%EA%B0%9C%EC%84%A0.md) : 영속성 전이를 통한 삭제에서 발생하는 n개의 ```delete``` 쿼리 문제를, 영속성 전이 해제 후, 벌크 연산을 통해 해결

## Login
1. [Login 구현 aop vs filter](https://github.com/jayjaehunchoi/JavaStudy/blob/main/SpringBootProject/Login/Login%20%EA%B5%AC%ED%98%84%20aop%20vs%20filter.md) : ```jwt```를 사용하면 ```filter```를 사용하고 그렇지 않은 경우에는 ```aop``` 혹은 ```interceptor``` 를 활용한다. 직접 구현해보고 장점을 배교해봄
2. [interceptor](https://github.com/jayjaehunchoi/JavaStudy/blob/main/SpringBootProject/Login/interceptor.md) : ```Login``` 시리즈 2, 예외, 인증 위치를 선택할 수 있다는 점에서의 편리함 발견
3. [사용할 수 있는 annotation 만들기](https://github.com/jayjaehunchoi/JavaStudy/blob/main/SpringBootProject/Login/%EC%82%AC%EC%9A%A9%ED%95%A0%20%EC%88%98%20%EC%9E%88%EB%8A%94%20annotation%20%EB%A7%8C%EB%93%A4%EA%B8%B0.md) : ```argumentresolver```를 이용하여 사용할 수 있는 ```annoatation```을 만들어 session 로그인 값을 편하게 받아올 수 있음

## Querydsl
1. [Querydsl로 페이징 구현하기(1)](https://github.com/jayjaehunchoi/JavaStudy/blob/main/SpringBootProject/Querydsl/Querydsl%EB%A1%9C%20%ED%8E%98%EC%9D%B4%EC%A7%95%20%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0(1).md) : 순수 jpa로 ```query```를 작성해보고, ```querydsl```로 전환하면 얼마나 편리한지 확인
2. [Querydsl로 페이징 구현하기(2)](https://github.com/jayjaehunchoi/JavaStudy/blob/main/SpringBootProject/Querydsl/Querydsl%EB%A1%9C%20%ED%8E%98%EC%9D%B4%EC%A7%95%20%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0(2).md) : 성능 개선 없이 단순히 ```offset``` 페이징 구현
3. [Querydsl로 페이징 구현하기(3)](https://github.com/jayjaehunchoi/JavaStudy/blob/main/SpringBootProject/Querydsl/Querydsl%EB%A1%9C%20%ED%8E%98%EC%9D%B4%EC%A7%95%20%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0(3).md) : 동적 쿼리와, 커서 기반의 페이징을 구현하여 무한스크롤, 혹은 view more에서 사용할 수 있는 성능 최적화 페이징 구현
4. [Querydsl로 페이징 구현하기(4)](https://github.com/jayjaehunchoi/JavaStudy/blob/main/SpringBootProject/Querydsl/Querydsl%EB%A1%9C%20%ED%8E%98%EC%9D%B4%EC%A7%95%20%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0(4).md) : 프로젝트에서 ```offset``` 기반의 페이징을 사용하기 때문에 , ```index```를 태워 ```offset``` 페이징 성능 최적화

## Spring Boot Study
> 직접 단계별 예제를 만들어 스프링 고급 활용 Study 운영
1. [aop, interceptor, session](https://github.com/jayjaehunchoi/JavaStudy/tree/main/SpringBootProject/SpringBootStudy/1.%20aop)
2. [validation & exception handler](https://github.com/jayjaehunchoi/JavaStudy/tree/main/SpringBootProject/SpringBootStudy/2.%20API%20validate%2C%20exception)

## Test
> 예제 내용 Controller Test
1. [MemberControllerTest](https://github.com/jayjaehunchoi/JavaStudy/tree/main/SpringBootProject/Test)

## 개념
1. [@Component vs @Bean](https://github.com/jayjaehunchoi/JavaStudy/blob/main/SpringBootProject/%EA%B0%9C%EB%85%90/%40Component%20vs%20%40Bean.md) : Component와 Bean의 차이점
2. [Aspect 객체에서 Exception을 먹어버릴때](https://github.com/jayjaehunchoi/JavaStudy/blob/main/SpringBootProject/%EA%B0%9C%EB%85%90/Aspect%20%EA%B0%9D%EC%B2%B4%EC%97%90%EC%84%9C%20Exception%EC%9D%84%20%EB%A8%B9%EC%96%B4%EB%B2%84%EB%A6%B4%EB%95%8C.md) : ```aspect```의 ```around``` 포인트 컷 작동 중 에러가 발생하면 에러를 ```catch```로 잡아버림, 이를 해결할 수 있는 방법 작성
3. [CORS 에러 해결하기](https://github.com/jayjaehunchoi/JavaStudy/blob/main/SpringBootProject/%EA%B0%9C%EB%85%90/CORS%20%EC%97%90%EB%9F%AC%20%ED%95%B4%EA%B2%B0%ED%95%98%EA%B8%B0.md) : 프론트와 백엔드 포트 차이로 인해 발생하는 ```CORS``` 이슈 해결
4. [Json으로 enum타입 파싱하기](https://github.com/jayjaehunchoi/JavaStudy/blob/main/SpringBootProject/%EA%B0%9C%EB%85%90/Json%EC%9C%BC%EB%A1%9C%20enum%ED%83%80%EC%9E%85%20%ED%8C%8C%EC%8B%B1%ED%95%98%EA%B8%B0.md) : Json으로 enum 타입에 데이터를 보낼 때 발생하는 에러 해결
5. [Spring Boot에서 AWS S3 연동하기](https://github.com/jayjaehunchoi/JavaStudy/blob/main/SpringBootProject/%EA%B0%9C%EB%85%90/Spring%20Boot%EC%97%90%EC%84%9C%20AWS%20S3%20%EC%97%B0%EB%8F%99%ED%95%98%EA%B8%B0.md) : 이미지 기반 sns 사이트라면 꼭 필요한 ```AWS S3``` 연동기
6. [SpringSecurity속 Swagger setting하기](https://github.com/jayjaehunchoi/JavaStudy/blob/main/SpringBootProject/%EA%B0%9C%EB%85%90/SpringSecurity%EC%86%8D%20%20Swagger%20setting%ED%95%98%EA%B8%B0.md) : Spring Security가 걸려있을 때, Swagger를 어떻게 작동시키는가
7. [controller에서 validate하지 않기](https://github.com/jayjaehunchoi/JavaStudy/blob/main/SpringBootProject/%EA%B0%9C%EB%85%90/controller%EC%97%90%EC%84%9C%20validate%ED%95%98%EC%A7%80%20%EC%95%8A%EA%B8%B0.md) : Spring framwork의 Validator를 이용하여 비즈니스 요구사항에 따라 복잡하게 엉켜있는 data 로직 검증하기
8. [에러 핸들링](https://github.com/jayjaehunchoi/JavaStudy/blob/main/SpringBootProject/%EA%B0%9C%EB%85%90/%EC%97%90%EB%9F%AC%20%ED%95%B8%EB%93%A4%EB%A7%81.md) : 일반 컨트롤러에서 ```exception handler```의 작동 원리와 api에서 예외 핸들링하기.
9. [Spring Security](https://github.com/jayjaehunchoi/JavaStudy/blob/main/SpringBootProject/%EA%B0%9C%EB%85%90/Spring%20Security.md) : Spring Security Reference를 읽고 필요한 내용을 골라 정리한 포스팅
10. [Spring REST docs 적용](https://github.com/jayjaehunchoi/JavaStudy/blob/main/SpringBootProject/%EA%B0%9C%EB%85%90/Spring%20Rest%20doc%20%EC%A0%81%EC%9A%A9.md) : 직접 Swagger와 REST docs를 적용해보며 느낀 경험과, REST docs를 적용할 수 있는 간단 예제 
11. [운영 db와 test db의 분리](https://github.com/jayjaehunchoi/JavaStudy/blob/main/SpringBootProject/%EA%B0%9C%EB%85%90/%EC%9A%B4%EC%98%81%20db%20%2C%20test%20db%EC%9D%98%20%EB%B6%84%EB%A6%AC.md) : EC2에 배포하고 운영 db에서 test가 돌아가는 것을 경험한 뒤 trouble shooting한 내용 소개!
12. [REST API XSS 방지](https://github.com/jayjaehunchoi/JavaStudy/blob/main/SpringBootProject/%EA%B0%9C%EB%85%90/REST%20API%20XSS%20%EB%B0%A9%EC%A7%80.md): MessageConverter에 HtmlEscape를 세팅하고, JacksonCoverter에 추가하여 사용하기

## 배포
1. [Docker와 AWS EB로 배포하기(1)](https://github.com/jayjaehunchoi/JavaStudy/blob/main/SpringBootProject/%EB%B0%B0%ED%8F%AC/Docker%EC%99%80%20AWS%20EB%EB%A1%9C%20%EB%B0%B0%ED%8F%AC%ED%95%98%EA%B8%B0(1).md) : AWS EB, Docker에 부딪히고 삽질하며 배포해보자. EB와 RDS 생성하고 보안그룹 등록한 뒤 EB, RDS 연결하기
