# Spring Rest docs 적용
이번 프로젝트는 ```REST API```를 사용하기 때문에, ```doc``` 파일을 필수적으로 만들어야만 했다. ```Spring```환경에서 ```doc``` 파일을 만들 수 있는 방법은 ```swagger```와 ```Spring rest docs```가 있는데, 두 가지를 모두 적용해보고, ```Spring Rest docs```가 훨씬 좋겠다고 판단하여 이를 적용하기로 했다. 이렇게 판단한 이유는 다음과 같다.

### 내가 느낀 Swagger의 단점
1. ```Swagger```를 사용하면 소스 코드가 너무 더러워진다. 컨트롤러에 많은 애노테이션이 들어가 ```Swagger```를 잘 모르는 팀원들은 ```controller```를 보는데 어려움을 느낄 것이라고 생각했다.
2. 썩 만족스럽지 못한 ```docs```가 제공된다. 일일히 ```annotation```을 달면 퀄리티가 괜찮아지겠지만, 그러기엔 또 소스코드가 너무 더러워진다.
3. 적용해본 결과 그냥 테스트를 할 수 있는 공간처럼 느껴졌다. 

### 내가 느낀 REST Docs의 장점
1. ```controller``` test 코드를 작성하는 것이 조금 난이도가 있을 수 있지만 어차피 해야하는 일이었고, ```test```를 통과한 ```controller``` 메서드만 ```docs```에 넣을 수 있어 신뢰도가 높았다.
2. 기본 세팅만 잘해놓으면 자동으로 ```docs```를 생성하기 때문에 정말 편리하다.

결국 ```Spring REST docs```를 활용하기로 결정했고, 지금부터 ```REST Docs``` 적용을 간단하게 요약하고자 한다!

## 세팅
### build.gradle
> 개인적인 의견으로 REST docs의 약 30퍼센트는 이 세팅인 것 같다.
#### 1. plugin
```groovy
plugins{
	id "org.asciidoctor.jvm.convert" version "3.3.2"
}
```
> 공식 문서에 따르면 plugin에 "org.asciidoctor.convert" version "1.5.9.2" 를 추가하라고 나온다. 하지만 이 버전을 사용하니 docs가 html로 전환되지 않아고 약간의 구글링을 해보니, gradle 7버전 이상에서는 위에 작성한 plugin을 받아와야 한다고 한다.

#### 2. 추가 세팅
```groovy
// 1. ext
/// ext는 gradle에서 전역변수로 선언할 수 있는 공간이다.
// 앞으로 build/generated-snippets 경로에 adoc 파일이 담길 것이다.
ext{  
  snippetsDir = file('build/generated-snippets')  
}  
  
// 2. asciidoctor 세팅
// doc이 생성되기 전에 test가 원활하게 작동되어야 하고, 전역변수로 선언한 directory에 담긴다.  
asciidoctor {  
  dependsOn test  
   inputs.dir snippetsDir  
}  
  
// build 시 기존 html을 삭제하고 최신화 한다. 
asciidoctor.doFirst {  
  delete file('src/main/resources/static/docs')  
}  
  
// jar가 build 되기 전에 실행된다.
bootJar {  
  dependsOn asciidoctor  
   copy {  
  from "${asciidoctor.outputDir}"  
  into 'BOOT-INF/classes/static/docs'  
  }  
}  
  
task copyDocument(type: Copy) {  
  dependsOn asciidoctor  
   from file("build/docs/asciidoc")  
   into file("src/main/resources/static/docs")  
}
```
> 주석 참고

### dependencies
```groovy
testImplementation 'org.springframework.restdocs:spring-restdocs-mockmvc'  
testImplementation 'capital.scalable:spring-auto-restdocs-core:2.0.11'
```
> mock test로 docs를 생성할 예정이고, Restdoc 관련 라이브러리를 사용하기 위해 위 두 가지 라이브러리를 받아온다.
> 이제부터 실제로 test 코드를 작성해보고, docs를 만들어보자 이번 포스팅에서는 간단한 예제로 대체하며, 실제 사용한 test와 docs 는 palette repository에서 확인 가능하다.

## Controller
```java
@RequiredArgsConstructor
@RestController  
public class HomeController {  
	private final MemberService memberService;

    @PostMapping("/")  
    public Map<String, Object> greeting(@RequestBody MemberDto member) {
	    String greet = member + " hello";  
	    memberService.save(new Member(member));
        return Collections.singletonMap("message", greet);  
  }
@Data
 static class MemberDto{
	private String name;
 }  
}
```
> json 타입으로 받아오면서, service 로직을 실행시키고, json으로 값을 리턴한다. 이 내용을 test하고, doc으로 만들어보자.

## Controller Test
```java
@WebMvcTest(HomeController.class)
@AutoConfigureRestDocs
public class HomeControllerTest{
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean 
	private MemberService memberService;

	@Test
	void 멤버_인사하기(){
		MemberDto memberDto = new MemberDto("wogns");
		BDDMockito.given(memberService.save(any())).willReturn(memberDto);
		// post -> use "org.springframework.test.web.servlet.request.MockMvcRequestBuilders"
		// status -> use "org.springframework.test.web.servlet.result.MockMvcResultMatchers;"
		// document -> use "org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders"
		// preprocessResponse -> use "org.springframework.restdocs.operation.preprocess.Preprocessors"
		mockMvc.perform(post("/")
						.content(objectMapper.writeValueAsString(memberDto))
						.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOK())
					.andExpect(content().json(objectMapper.writeValueAsString("{\"message\":\"wogns hello\"}")))
					.andDo(document("home"), preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));
	}
}
```
> 간단하게 test를 돌리고 성공시키면, 우리가 원하는 위치에 generate-snippets가 생성된다. 이제 이 내용들을 html로 만들기 위해, docs api guide를 만들어보자. src -> docs -> asciidoc -> api-guide.adoc 생성

## Api-guide.adoc
```adoc
== 해당 위치의 doc을 가져옴
ifndef::snippets[]  
:snippets: ./build/generated-snippets

== 아래 두 내용을 html로 만듦
include::{snippets}/home/http-request.adoc[] 
include::{snippets}/home/http-response.adoc[]
```
> 이후 terminal에 ./gradlew build 를 해주면 build -> docs 폴더가 생성되면서 html 파일이 생성된다!!

간단하게 ```RestController```작성 ,```Mock test```실행, ```REST docs``` 생성 예제를 만들어 보았다. ```REST docs``` 같은 경우에는 [reference](https://docs.spring.io/spring-restdocs/docs/current/reference/html5/)가 정말 친절하게 잘 나와있기 때문에 참고하면 쉽게 만들 수 있을 것이다. 다만 ```Mock test```의 기본은 공부해야 ```REST docs``` 를 사용할 수 있을 듯 하다.
