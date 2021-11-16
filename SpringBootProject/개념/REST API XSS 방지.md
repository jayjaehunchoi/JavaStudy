# REST API XSS 방지
```Cross Site Scripting```이라는 가장 기본적이고 널리 쓰이는 해킹 방식이 있다. 간단하게 설명하자면 서버에 ```html```코드를 보내 계속해서 알람이 뜨게 한다거나, 요상한 화면이 뜬다거나 뭐 그런 경우이다. 

서버 개발자는 이런 문제를 사전에 방지해야 한다. 따라서 이번 프로젝트에서 XSS 예방 코드를 작성하며 경험한 내용을 공유하고자 한다. 

## 해결 방법?
### 공통 처리
해결방법을 코드로 작성하기 앞서서, Spring의 작동 원리를 생각해보자. 어느 ```Controller```로 ```json```값이 들어오든 XSS 예방 코드는 공통적으로 돌아가야 한다. Spring에서 공통 처리할 수 있는 부분은? 바로 ```Filter```, ```Intercepter```, ```aop``` 등이 있다. 하지만 이 세 가지 방법을 통해 문제를 해결하려면 어쨌든 브라우저에서 넘어온 ```json``` 을 객체로 변환하는 과정이 있을 것이다. 

### MessageConverter
```Spring```에는 ```MessageConverter```클래스가 있다. Spring 내부적으로 브라우저에서 넘어온 ```content type```과 실제 ```message```를 기반으로 서버에 어떤 형태로 보내줄지 결정하고, 잘 변환해서 넘어간다. 이 ```MessageConverter```는 ```argumentResolver```에서 호출하며, ```request```, ```response``` 시에 작동한다. 즉, ```json``` 형태로 브라우저에서 서버로 값이 넘어오면 들어올 때, 나갈 때 모두 이 ```MessageConverter```를 거친다는 뜻이다.

### 결론
 그렇다면 ```MessageConverter``` 에 ```XSS``` 방지 기능을 추가해서 사용하자! 

## 코드
### build gradle
> StringEscapeUtils 사용을 위한 의존성 추가
```groovy
implementation 'org.apache.commons:commons-text:1.8'
```
### HtmlCharacterEscapes
> 넘어온 내용의 html 관련 특수문자는 여기서 모두 escape시켜준다. 특수문자가 escape되면 url에서 인코드 되는 것과 비슷한 형태로 생성된다.
```java
public class HtmlCharacterEscapes extends CharacterEscapes {

	private final int[] asciiEscapes;

	@Override
	public int[] getEscapeCodesForAscii() {
		return asciiEscapes;
	}

	//emoji jackson parse 오류에 따른 예외 처리 (utfmb8 emoji 파싱)
	@Override
	public SerializableString getEscapeSequence(int ch) {
		SerializedString serializedString = null;

		char charAt = (char) ch;

		if (Character.isHighSurrogate(charAt) || Character.isLowSurrogate(charAt)) {
			StringBuilder sb = new StringBuilder();
			sb.append("\\u");
			sb.append(String.format("%04x",ch));
			serializedString = new SerializedString(sb.toString());

		} else {
			serializedString = new SerializedString(StringEscapeUtils.escapeHtml4(Character.toString(charAt)));
		}
		return serializedString;
	}

	// XSS 방지 특수문자
	public HtmlCharacterEscapes(){
	
		asciiEscapes = CharacterEscapes.standardAsciiEscapesForJSON();
		asciiEscapes['<'] = CharacterEscapes.ESCAPE_CUSTOM;
		asciiEscapes['>'] = CharacterEscapes.ESCAPE_CUSTOM;
		asciiEscapes['\"'] = CharacterEscapes.ESCAPE_CUSTOM;
		asciiEscapes['('] = CharacterEscapes.ESCAPE_CUSTOM;
		asciiEscapes[')'] = CharacterEscapes.ESCAPE_CUSTOM;
		asciiEscapes['#'] = CharacterEscapes.ESCAPE_CUSTOM;
		asciiEscapes['\''] = CharacterEscapes.ESCAPE_CUSTOM;
	}
}
```

### WebConfig
```java
@Bean
public MappingJackson2HttpMessageConverter jsonEscapeConverter() {
	ObjectMapper copy = objectMapper.copy(); // 기존 ObjectMapper customize한 내용 copy
	copy.getFactory().setCharacterEscapes(new HtmlCharacterEscapes()); // 작성한 esacape 코드 등록
	return new MappingJackson2HttpMessageConverter(copy); // 해당 내용 @bean 등록
}
```

#### 마무리
XSS 방지 코드를 작성하는 것은 크게 어렵지 않으나, 이 문제를 어디서, 어떻게 해결할 수 있을까를 아는 것이 중요하다. 
특히 Spring으로 ```REST API```를 개발하고 있으면서도 ```MessageConverter```의 존재에 대해 알지 못하거나 제대로 이해하고 있지 못한다면 이 방지 코드를 프로젝트에 쓰지 않는 것이 더 낫다는 입장이다. 
하지만 이 방지 코드는 플젝에 꼭 필요하다. 결론은 Spring의 작동원리를 어느 정도는 이해하고 XSS 방지 코드를 작성해보면 개인의 역량 개발에 더 도움이 될 것 같다!
