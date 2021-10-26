## 배열
### 배열이란?

배열이란 자바에서 지원하는 기본 자료구조로, 고정된 크기를 갖고 인덱스로 관리된다는 특징을 지니고 있다.
그외에도 다음과 같은 특징을 갖고있다.
1. 각 위치를 요소라고 부른다
2. 인덱스로 관리되어 값을 수정할때, 빠르게 접근할 수 있다. 인덱스로 관리될 수 있는 이유는 논리 주소와 물리 주소가 일치하기 때문이다
3. 선언할때 크기가 고정되며, 참조변수가 Stack영역에 실제 주소값은 heap영역에 생성된다.

### Stack과 heap영역

배열이 Stack과 Heap영역에서 어떻게 메모리를 차지하는지 , 코드와 그림을 통해 설명하겠다!

```java
int[] a = {1,2,3,4,5,6,7};
```
* main 메서드에 a 생성, 이때 참조변수 a는 stack 영역에 , 실제 값의 주소는 heap영역에 생성된다. 


```java
	public static int[] remove(int[] arr, int n) {
		
		if(n < 0 || n >= arr.length) {
			throw new IllegalStateException("제거가 불가합니다!!");
		}
		.
    .
    .
	}
 ``` 
* 배열에서 특정 값을 제거하는 메서드 (O(n) 시간)
* 이때 main에서 선언한 a는 stack에서 잠시 사용할 수 없는 메모리로 처리되고
* 새로운 arr과 n 이 스택영역에 생성된다

![image](https://user-images.githubusercontent.com/87312401/134536003-e0aa0cf1-0e68-4681-8267-2ebbc9478085.png)

```java
//main 메서드에 새 배열 생성
int[] b = remove(a, 2);

// remove 메서드 완성
public static int[] remove(int[] arr, int n) {
		if(n < 0 || n >= arr.length) {
			throw new IllegalStateException("제거가 불가합니다!!");
		}
		int[] proxy = new int[arr.length-1];
		for(int i = n ; i < arr.length-1; i++) {
			arr[i] = arr[i+1];
		}
		for(int i = 0 ; i < proxy.length; i++) {
			proxy[i] = arr[i];
		}
		System.out.println(proxy);
		return proxy;
	}
```
* int[] b 가 생성되면서 new int[] 인 b가 stack영역에 생성된다.
* remove 메서드에 arr은 a의 주소값을 참조하고, n 은 stack영역에 2로 명시된다.
* remove 메서드 내부에서 proxy라는 배열이 생성되면서 stack영역에 참조변수가 , 그리고 그 주소가 heap영역에 새로 생성된다.

```java
// main 메서드
System.out.println(Arrays.toString(b));
System.out.println(b);
```
* remove 메서드를 나가면서 arr, n , proxy는 gc에 의해 stack영역에서 제거된다.
* 하지만, 그 참조 주소들은 기존 a, b 에 의해 reachable하기 때문에 gc로 제거되지 않고 참조관계가 유지된다.
* b의 주소값을 출력해보면 , remove 메서드의 proxy와 동일한 주소값이었다는 것을 알 수 있다.

![image](https://user-images.githubusercontent.com/87312401/134537789-5a9f0090-f1d5-4cfe-8c34-f1280a2c4577.png)

### 배열의 장점
1. 인덱스로 접근할 수 있기 때문에 특정 엘리먼트를 수정하거나 get하는데 O(1)의 성능을 가진다.
2. 연속된 메모리로 원하는 값을 잘 정리해서 저장할 수 있다.

### 배열의 단점
1. 크기가 고정되어 있어, 값을 제거하거나 추가하려면 하나하나 자리를 밀어주고 새로운 값을 생성해줘야 한다. O(n)의 성능을 가지며 효율적이지 못하다

### 가변 배열
가변배열은 n차원 배열에서 행 index가 가진 열값이 일치하지 않는 것을 의미한다.
그 배열은 아래 코드와 같이 구현된다!
```java
int[][] arr = new int[2][];
arr[0] = new int[]{1,2,3,4};
arr[1] = new int[]{1,2};
```

 
