# For에서 하는 일을 분리하자
## 이유
누군가 이렇게 묻는다, **"왜 For 문을 분리해야 하지? 성능 이슈 생기는거 아니야?"** 물론 생길 수 있다. 하지만 For문을 분리하지 않아 복잡한 For 문을 만들면 성능 이슈가 생기는지 안생기는지 조차 알 수가 없다. 
그렇다면 For에 들어있는 로직을 변경하면 어떤 장점이 있을까?
1. 코드 이해가 정말 쉬워진다. 
2. 메소드 추출과 같이 리팩토링이 용이해진다.
3. 새로운 로직을 추가하는 것이 용이해진다.

객체지향적으로 코드를 작성하는 이유 중 하나는 확장의 편리성 때문이라고 생각한다. 확장의 편리성을 높이기 위해  다형성을 잘 활용하는 것 못지 않게 기능의 분리가 중요하다고 생각하는 입장이다.
이제부터 여러 일을 하는 For문을 분리해보자.

## 예제
오늘 우리 사장님의 음식점과 카페가 행사를 열어 음식점 방문객들이 모두 카페를 방문하여 음료를 주문했다.
한 사람이 두 매장에서 사용한 금액을 합쳐 가계부 서비스에 띄워달라고 하신다. 가볍게 개발을 시작하자!
```java
List<PostApiDto> dtoList = new ArrayList<>();
for(Visitor visitor : visitors){
	try{
		CheckVisitorReq req = ... // visitor 정보 요청
		CheckVisitorRes resp = ...// visitor 정보 유/무
		List<Bill> bills = posApi.findBillByVisitorId(visitor.getId());
		for(Bill b : bills){
			PosApiDto dto = ... // 가계부 서비스에 response 준비
			dtoList.add(dto);
		}
	}catch(Exception e){
		error.sendError(resp);
		List<Bill> bills = posApi.findBillByVisitorId(visitor.getId());
		for(Bill b : bills){
			PosApiDto dto = ... // 가계부 서비스에 fail response 준비
			dtoList.add(dto);
		}
	}
}
if(dtoList.size() > 0 ){callPos(dtoList);..} // dto list 전송	
```
딱히 좋은 예시는 아닌 것 같지만 .. 코드를 작성했다. 간단한 코드임에도 for문이 두 개 들어가니 조금 복잡해진 것 같다. 지금은 괜찮다. 하지만 갑자기 사장님께서 **엑셀파일로 뽑아서 바탕화면에 저장해줘** 라는 요구사항이 들어온다면?  다시 반복문이 생기고 내부적으로 많은 반복문이 돌아갈 것이고 굉장히 복잡한 코드가 등장할 것이다. 우리는 이해하기 쉬운 코드를 작성하기 위해 또, 쉽게 확장하기 위해 메소드를 추출해야 한다. 지금부터 기능을 분리해보자.

### visitor 정보 요청
```java
// 이렇게 visitor 정보 요청이라는 반복문으로 빼줄 수 있다.
private List<Bill> requestVisitorsInfo(List<Visitor> visitors){
	List<Bill> bills = new ArrayList<>();
	try{
		CheckVisitorReq req = ... 
		CheckVisitorRes resp = ...
		bills = posApi.findBillByVisitorId(visitor.getId());
	}catch(Exception e){
		error.sendError(resp);
		bills = posApi.findBillByVisitorId(visitor.getId());
	}
	return bills;
}
```

### bill 취합
```java
private List<PostApiDto> addApiDto(List<Bill> bills){
	List<PostApiDto> dtoList = new ArrayList<>();
	for(Bill b : bills){
		PosApiDto dto = ... // 가계부 서비스에 response 준비
		dtoList.add(dto);
	}
	return dtoList;
}
```

### 최종
```java
List<Bill> bills = requestVisitorsInfo(visitors);
List<PostApiDto> dtoList = addApiDto(bills);
if(dtoList.size() > 0 ){callPos(dtoList);..} 
```
코드가 정말 깔끔해졌다. 만약 엑셀로 추가하라는 요청사항이 생기더라도 다른 메서드 하나를 만들어 사이에 끼워넣어주기만 하면 된다.

루프를 여러번 돌면 성능 문제 없나? 거의 없으니 작성하기 전부터 걱정하지 말고 리팩토링하자!
