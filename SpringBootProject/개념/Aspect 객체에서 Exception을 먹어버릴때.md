# aop를 사용할 때, 예외처리가 되지 않는다면?
```@ExceptionHandler```를 이용하여 열심히 예외처리를 하고 있었다... 그런데 무조건 떠야하는 에러가 뜨지 않는 것이다.. 도대체 무슨 이유 였을까 한참을 찾아봤다. 코드는 다음과 같았다.

### MemberServiceImple
```java
@Override  
public void join(Member member){  
    saveValidator(member);  
  member.setEncodedPassword(passwordEncoder.encode(member.getPassword()));  
  memberRepository.save(member);  
}
private void saveValidator(Member member){  
    if(memberRepository.findByName(member.getName()) != null){  
        throw new DuplicateIdException("중복된 아이디 입니다.");  
  }  
    if(memberRepository.findByEmail(member.getEmail()) != null){  
        throw new DuplicateEmailException("중복된 이메일 입니다");  
  }  
}
```
### MemberController
```java
@TimeChecker  
@PostMapping("/signup")  
public ResponseEntity<?> signUp(@RequestBody @Validated MemberDto dto){  
    Member member = new Member(dto);  
	memberService.join(member);  
	return new ResponseEntity<>(dto, HttpStatus.OK);  
}
```
당연하게도 , 중복된 아이디가 들어가면 ```DuplicatedIdException```이 , 중복 이메일이 들어가면 ```DuplicatedEmailException``` 이 나와야 한다. 이 Exception들은 아래 코드로 처리해준다.
### RestExceptionHandler
```java
@ResponseStatus(HttpStatus.BAD_REQUEST)  
@ExceptionHandler(RuntimeException.class)  
public ResponseDto runTimeExHandler(RuntimeException e){  
    log.error("[runTimeException] error message = {}",e.getMessage());  
    String message = e.getMessage() == null ? "런타임 에러" : e.getMessage();  
    ResponseDto<Object> dto = ResponseDto.builder()  
            .error(message)  
            .build();  
	return dto;  
}
```
```ExceptionHandler``` 에서 ```dto```를 만들어주고 바로 ```HttpResponse```로 return 해줘야 하는데,,, 아무리 해도 에러가 throw되지 않는 것이다. 하지만 이때 콘솔창에는 이런 로그가 뜬다.
![image](https://user-images.githubusercontent.com/87312401/138920608-d00c3f67-479e-4de1-98cd-430f54508646.png)
```@Around```로 구현한 ```Aspect``` 내부 메서드에서 에러를 먹어버리는 것이다. (로깅의 중요성) 처음 이 문제를 깨달았을 때 딱 하나의 해결법밖에 떠오르지 않았다..
### MemberController에서 error를 잡고 직접 핸들링 해주기
```java
@TimeChecker  
@PostMapping("/signup")  
public ResponseEntity<?> signUp(@RequestBody @Validated MemberDto dto){  
    Member member = new Member(dto);
    try{
		memberService.join(member);  
	}catch(RuntimeException e){
		ResponseDto rDto = ResponseDto.builder().error(e.getMessage).build();
		return new ResponseEntity<>(rDto, HttpStatus.BAD_REQUEST);
	}
	return new ResponseEntity<>(dto, HttpStatus.OK);  
}
```
이렇게 처리를 해주면 error 처리가 가능해졌지만, 이는 ```ExceptionHandler```를 전혀 사용하지 못하는 방법이다. 이런 저런 고민을 해보다,,, 인프런의 김영한님 질문 게시판에 질문을 남겼고.. aop에서 다시 오류를 던져주라는 답변을 받았다!!! 작성 코드는 정말 간단하다.

### TimeCheckAspect
```java
@Around("@annotation(study.validation.util.annotation.TimeChecker)")  
public Object timeChecker(ProceedingJoinPoint proceedingJoinPoint){  
    Object result = null;  
 try{  
        long start = System.currentTimeMillis();  
		result = proceedingJoinPoint.proceed();  
		long end = System.currentTimeMillis();  
		log.info("[타임 체커 AOP] 소요 시간 {}ms",(end-start));  
  }catch(RuntimeException e){  
        throw e;  // 여기에서 Exception을 다시 뱉어준다.
  } catch (Throwable throwable) {  
        log.error("타임 체커 오류");  
  }  
    return result;  
}
```
바로 ```ExceptionHandler```에서 처리한 ```RuntimeException```을 다시 던져주는 방법이다.
이렇게 사용하면 ```aop```와 ```ExceptionHandler``` 모두 수월하게 사용 가능하다~!!

## 결과
![image](https://user-images.githubusercontent.com/87312401/138923401-96c9da12-76aa-4732-82ba-7f2ec5f7ba16.png)
