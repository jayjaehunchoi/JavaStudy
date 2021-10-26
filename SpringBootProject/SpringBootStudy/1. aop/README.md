# Spring Aop 예제
## 학습 목표
1. Spring AOP를 이용하여 공통기능을 비즈니스 로직에서 분리하여 Aspect로 관리할 수 있다.
2. Interceptor를 이용하여 Session 로그인을 Interceptor에서 공통으로 관리할 수 있다.
3. Argument Resolver를 활용하여 사용할 수 있는 Annotation을 만들어 Session 로그인을 검증할 수 있다

## Requirements
* JDK11
* Spring boot 2.5.6
* gradle
* H2 Database 1.4.200 : [다운로드](https://www.h2database.com/html/main.html)
* JPA
* Postman : [다운로드](https://www.postman.com/downloads/)
* lombok
* aop

## 문제상황
1. 처음에는 회원가입 실행 시간만 체크해달라고 요구하였으나, 모든 서비스에 실행 시간을 추가해달라고 요청이 들어온 상황.
2. 개발 초기라 Session 로그인 처리로직을 모든 컨트롤러에 넣어두었으나, 사업이 확장되어 공통으로 관리해야 하는 상황.
3. 매번 ```@SessionAttribute```를 사용하는 것이 귀찮았던 선배가 편하게 Session값을 받아올 수 있게 만들어달라고 요청함.

## 소스코드 받는 법
1. 깃 클론하기 : [https://officeworker-a.tistory.com/10](https://officeworker-a.tistory.com/10)
2. 일부 소스만 pull 하기 : [https://napasun-programming.tistory.com/43](https://napasun-programming.tistory.com/43)
