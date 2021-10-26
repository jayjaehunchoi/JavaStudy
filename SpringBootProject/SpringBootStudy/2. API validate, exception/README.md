# Spring Aop 예제
## 학습 목표
1. javax.validation을 이용하여 requestBody값에 대한 validation을 처리할 수 있다.
2. ExceptionHandler를 이용하여 에러 메시지를 response할 수 있다.

## Requirements
* JDK11
* Spring boot 2.5.6
* gradle
* H2 Database 1.4.200 : [다운로드](https://www.h2database.com/html/main.html)
* JPA
* Postman : [다운로드](https://www.postman.com/downloads/)
* lombok
* aop
* validation

## 문제상황
1. aop, interceptor, argumentResolver로 로그인 기능을 만들었으나 validation이 전혀 이뤄지지 않아 중복 데이터, 허용하지 않은 데이터가 잔뜩 들어온 상황
2. error page를 핸들링 하지 않아 프론트 엔드에서 불만을 표현한 상황

## 소스코드 받는 법
1. 깃 클론하기 : [https://officeworker-a.tistory.com/10](https://officeworker-a.tistory.com/10)
2. 일부 소스만 pull 하기 : [https://napasun-programming.tistory.com/43](https://napasun-programming.tistory.com/43)
