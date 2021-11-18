# JWT 인증으로 전환하기
애초에 프로젝트를 진행할 때 ```JWT```를 사용하지 않고 단순히 ```Session Login```을 사용하려 했다. 그런데 프론트에서 데이터를 연결하던 중 , ```Session```값을 읽어올 수 없다고 하는 것이다.. ```CORS```문제로 파악하고 이것저것 만지며 개선하려 했는데, 결국 실패했고 백엔드가 거의 완성된 상황이지만 , 로그인을 ```JWT```로 전환하겠다고 다짐하였다. 지금부터 그 전환기의 시작이다.

### 간단한 개념
> JWT로의 전환기를 적는 포스팅으로 개념은 간단하게 이야기하고 넘어간다!

```JWT```는 3분할로 나눠진다. ```header```, ```payload```, ```signature``` 
1. header
 > JWT Type과 사용 알고리즘을 기반으로 인코딩 된다.
2. payload
> 토큰의 주인 , 유일하게 식별되는 값 (우리 프로젝트로 치면 id 값) 으로 주인을 판별하게 해줄 수 있는 핵심 정보를 갖고 있다. 이외에도 발행인, 만료일자 등 다양한 정보가 담겨 있다.
3.  signature
> 임의의 signature값을 주고 암호화 시킨다.

#### 인증 순서는 다음과 같다.
1. Client가 로그인 한다. 이 때 서버는 사용자 정보를 바탕으로 Token값을 생성하여 ```Authorization```에 ```Bearer``` + Token 형태로 Token을 전달해준다.  
2. 이후  Client는 요청시 Header에 Token을 담아 서버로 넘긴다. 서버는 ```header```와 ```payload```를 떼어내 디코딩 한 뒤 ```signature```로 토큰을 디코딩하여 넘겨받은 토큰값의 ```signature``` 부분과 비교한다. 같으면 검증이 완료된다.

### How?
 그렇다면 이제 어떻게 전환할 것인가 ? 기존 우리 프로젝트의 로그인 인증 구조를 비교하며 전환할 방향성에 대해 생각해보았다.
|as-is|to-be|
|-|-|
|Login 시 Session을 만들어 Cookie로 넘김|Login 시 Token을 만들어 넘김|
|aop를 이용하여 메서드 실행 전 Session 값을 비교| 그대로 aop를 이용하고 로직을 token 비교로 전환|
|@Login 애노테이션으로 session 값을 member로 전환|@Login 애노테이션으로 token 값을 member로 전환|

> 기존에 Session이었던 내용만 Token으로 바꾸면 되는 사항이었다. 그리 어렵지 않았기에 바로 코드를 전환하기로 마음 먹었다. 
> 그리고 Controller Test 코드가 있었기 때문에 , Refactoring이 두렵지 않았다.

### Why AOP?
 > 일반적으로 Token을 interceptor나 filter에서 검증하더라. 
 > 하지만 내 개인적인 의견으로 aop가 interceptor , filter보단 조금 비즈니스 로직과 가깝지만 그 이전에 검증되기 때문에 문제가 없을 것이라 판단했다. 
 > 하지만 한 곳에서 로그인이 필요한 메서드를 제어하지 못하는 것은 조금 아쉽다.

이제 코드작성으로 넘어가 보자.

### JwtTokenProvider
> Token의 핵심 기능을 수행한다. 토큰을 발급하고, 유효한 토큰인지 검증하는 메서드가 존재한다.
> 자세한 코드는 Palette 참고

```java
// JwtTokenProvider의 핵심 코드이다. member의 Id값과 유효기간을 바탕으로
// member의 정보가 담긴 Token을 만들어준다.
private String createToken(String payload, long expireTime){  
  Claims claims = Jwts.claims().setSubject(payload);  
  Date now = new Date();  
  Date validTime = new Date(now.getTime() + expireTime);  

   return Jwts.builder()  
            .setClaims(claims)  
            .setIssuedAt(now)  
            .setExpiration(validTime)  
            .signWith(SignatureAlgorithm.HS256, secretKey)  
            .compact();  
}  

```

### AuthorizationExtractor
> Header로 넘어온 값을 파싱하여 Header 값을 만들어준다.

```java
public static String extract(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders(AUTHORIZATION); //Authorization header 가져오기
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if ((value.toLowerCase().startsWith(BEARER_TYPE.toLowerCase()))) { // Bearer 값
                String authHeaderValue = value.substring(BEARER_TYPE.length()).trim(); // Bearer를 제외한 진짜 Token값
                request.setAttribute(ACCESS_TOKEN_TYPE, value.substring(0, BEARER_TYPE.length()).trim());
                int commaIndex = authHeaderValue.indexOf(',');
                if (commaIndex > 0) {
                    authHeaderValue = authHeaderValue.substring(0, commaIndex);
                }
                return authHeaderValue;
            }
        }
        return null;
    }
```

> 요런 느낌으로 Token 값을 발급받고 검증한다. 검증은 aop, token을 member 객체로 만들어 주는 부분은 argumentResolver를 확인하면 된다.

### 전환기
처음부터 ```Session``` ```JWT``` 중 무엇을 사용할지 의사결정을 잘 했더라면 이렇게 전환할 일이 없었을텐데.. 하는 생각이 들었다.
하지만 , 다른 포트로의 header 값 전달의 어려움을 알게 됐고 , ```CORS```에 대해서도 조금 더 잘 알게 된 것 같다.
그리고 ```JWT``` 경험도 확실히 한 것 같아 기분이 좋다.

또 대대적인 리팩토링이었음에도 테스트 코드가 있다는 사실에 용기내서 리팩토링 할 수 있었다. 테스트 코드 작성을 습관화하자.
(완벽한 TDD가 아니더라도,, 절반 정도의 TDD ...)
