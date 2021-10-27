# 대역으로 Controller Test하기
## source code
Spring Boot Test: [MemberControllerTest](https://github.com/jayjaehunchoi/JavaStudy/blob/main/SpringBootProject/Test/MemberControllerTest.java)
Mock Test : [MemberControllerByMockTest](https://github.com/jayjaehunchoi/JavaStudy/blob/main/SpringBootProject/Test/MemberControllerTest2.java)
## How?
사실, 완벽한 대역으로 Test 코드를 작성하는 것이 목적이었다. 따라서 처음 작성했던 코드를 요약하면 다음과 같다. 
**스프링 부트 테스트에서 확인 가능**
```java
@WebMvcTest(MemberController.class)
public class MemberControllerTest{
	@Autowired
	private MockMvc mvc;
	@MockBean
	private MemberService memberService;
	@Autowired
	private MemberRepository mockMemberRepository = Mockito.mock(MemberRepository.class);
}
```
위와 같이 코드를 작성하였지만 문제는 다음과 같았다.
* Mockmvc로 코드를 작성하면 정상 작동하나 , controller를 타고 들어가는 데이터가 저장이 되지 않음. 
-> 생각해보면 당연히 저장되지 않음, 모의 환경에서 진행되는 테스트이기 때문에 당연히 ```JPA``` 가 작동되지 않기 때문에 어쩔 수없음
-> 그러면 Spring 환경에서 먼저 테스트를 해보자!

위와 같은 사고로 Spring환경에서 Test를 완료했다. 하지만 매번 불필요하게 실제 ```Repository```가 생성되고,  값이 저장되고, 이를 위해 사전에 세팅하는 과정이 꽤나 번거롭다고 느껴졌고,
**만약 테스트코드가 늘어나면, 그만큼 성능저하도 가져올 수 있다고 생각했다.**
### 예시 Test (Spring Boot Test 링크에서 코드 확인 가능)
```java
@BeforeEach  
void setUp(){  
    mvc = MockMvcBuilders  
            .webAppContextSetup(context)  
            .addFilters(new CharacterEncodingFilter("UTF-8", true))  
            .apply(springSecurity())  
            .build();  
  memberRepository.save(new Member("wogns0108", passwordEncoder.encode("qwer12345!") ,"wogns0108@naver.com",25));  
}
@AfterEach  
void tearDown(){  
    memberRepository.deleteAll();  
}  
  
@Test  
void 회원가입_성공() throws Exception {  
    String member =  createMemberDto("wogns0107", "qwer12345!","wogns0108@nate.com");  
  
	mvc.perform(post("/member/signup")  
            .contentType(MediaType.APPLICATION_JSON).characterEncoding(StandardCharsets.UTF_8)  
            .content(member)  
            .accept(MediaType.APPLICATION_JSON))  
            .andExpect(status().isOk())  
            .andDo(print());  
  
    assertThat(memberRepository.findAll().size()).isEqualTo(2);  
}
```
내부적으로 실질적인 값을 주고 받으니 정밀한 테스트는 가능하겠으나, 성능이 issue인 듯 하다. 하지만 이런 테스트도 나쁘지는 않다고 생각한다...? 

하지만 ```WebMvc``` 환경에서의 테스를 포기할 수 는 없다. 그래서 다시 한 번 ```WebMvc``` 환경에서 테스트를 진행도전했다.

 ### WebMvcTest(WebMvcTest 링크에서 상세 코드 확인 가능)
**기본 의존성 주입**
```java
@WebMvcTest(MemberController.class)
public class MemberControllerTest2{
	@Autowired  
	private MockMvc mvc;  
	@Autowired  
	private ObjectMapper objectMapper;  
	@MockBean  
	private MemberService memberService;
}
```
**대역생성**
```java
@Test  
void 회원가입_중복아이디_실패() throws Exception{  
  // repository에 "wogns0108"이라는 아이디를 가진 멤버가 있다고 가정  
  Member mockMember = new Member("wogns0108", "qwer12345!", "wogns0108@gmail.com", 25);  
  doThrow(new DuplicateIdException("중복 아이디")).when(memberService).join(mockMember);  
  ...
}
@Test  
void 로그인성공() throws Exception{  
	// repository에 현재 해당 데이터 존재
    Member mockMember = new Member("wogns0108", "qwer12345!", "wogns0108@naver.com", 25);  
    given(memberService.checkLoginInfoCorrect("wogns0108","qwer12345!")).willReturn(mockMember);
 }
```
위 두 코드에서 거짓으로 저장소의 값을 return 하거나, void 값의 경우 error를 발생시키는 대역을 만들었다.
> 이때, Member는 immutable객체로, parameter로 들어오는 객체를 비교할 때 equals와 hashcode가 override되어있지 않으면 원하는 테스트를 하지 못할 것이다.

**테스트 작성**
```java
@Test  
void 로그인성공() throws Exception{  
    Member mockMember = new Member("wogns0108", "qwer12345!", "wogns0108@naver.com", 25);  
  
	given(memberService.checkLoginInfoCorrect("wogns0108","qwer12345!")).willReturn(mockMember);  
	String member = createMemberDto("wogns0108","qwer12345!","wogns0108@naver.com");  
	mvc.perform(post("/member/login").contentType(MediaType.APPLICATION_JSON)  
            .content(member)  
            .accept(MediaType.APPLICATION_JSON))  
            .andExpect(status().isOk())  
            .andExpect(jsonPath("$.[?(@.name == '%s')]","wogns0108").exists())  
            .andDo(print());  
  
}
```
이렇게 테스트 코드를 작성하고 모든 Test에 통과를 만들었으면 이제 ```Controller``` , ```Service```, ```Repository```에서 세밀하게 데이터를 처리해주면 된다!!
