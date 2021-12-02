# Spring Data Redis

우연히 ```Redis```에 대한 글을 읽고 이건 꼭 배워야 한다!! 라고 생각하여 간단하게 실습해보았다.

## Redis
```Redis```는 시스템 메모리를 사용하는 ```key```-```value```형태의 데이터 스토어이다. 인메모리 상태에서 데이터를 처리함으로써
```RDS``` 보다 빠르고 가볍게 동작한다.

### 언제 사용해야 할까?
- 운영중인 웹 서버에서 ```key```-```value```형태의 데이터 타입을 처리해야 할 때
- I/O가 빈번히 발생해 효율이 떨어진다고 생각될 때

해당 글에서는 조회수를 예를 들어 ```Redis```를 사용하는 이유를 설명했다.

> 유튜브 조회수는 빠르면 1시간이 안되어 100만 조회수를 넘긴다.
> 그러면 분당 1만6천번의 데이터가 매번 RDS에 저장된다.
> 이러한 I/O의 반복은 엄청난 자원이 사용될 것이다. (조회수를 올리는 것 뿐 아니라 여러 다른 활동들도 있지 않는가?)

```Redis```는 이러한 상황에 데이터를 캐싱하고, 일정한 주기에 따라 RDS를 업데이트 해줘, 부담을 줄이고 성능을 최적화 할 수 있다.

또 많이 사용되는 영역은 ```사용자 세션 관리```이다.
사용자의 세션을 유지하고, 불러오고, 여러 활동들을 추적한다. 또, 메시지 큐잉에도 사용할 수 있다.

또한, ```API```를 캐싱하여 라우트로 들어온 값을 캐싱해 동일한 요청이 발생할 시 캐싱된 데이터를 리턴한다.

이외 내용은 [출처](https://brunch.co.kr/@skykamja24/575) 에서 확인할 수 있다.

## Practice
> docker , spring boot 가 필요함

먼저 ```docker```에서 ```redis``` 이미지를 ```pull``` 해온다.
```docker
docker pull redis
```

> docker images를 입력하여 이미지가 잘 다운됐는지 확인하자
![image](https://user-images.githubusercontent.com/87312401/144376693-b5be46dd-1157-4c85-80a9-e80eafebdf54.png)

그리고 ```docker```이미지를 실행시키자
```docker
docker run --name redis-container -d -p 6379:6379 redis
```
```-d``` 백그라운드 실행  
```-p``` 로컬과 docker 컨테이너 포트 연결

> 알 수없는 문자들이 나오면 background에서 docker 컨테이너가 실행된 것이다.
![image](https://user-images.githubusercontent.com/87312401/144376971-350aaead-b26c-4b70-b45b-9c791023a9e5.png)

이제 컨테이너를 실행시켰으니 ```redis-cli```에 접근해보자
```docker
docker run -it --link redis-container:redis --rm redis redis-cli -h redis -p 6379
```
```--rm``` 컨테이너 종료시 자동으로 컨테이너 삭제

redis cli에 접근했으니 ```redis```에 데이터를 하나 저장해보자.

```
set jaehun choi  
```
> jaehun이라는 key 값에 choi가 들어간다.

![image](https://user-images.githubusercontent.com/87312401/144377988-006206ff-f896-4ea3-baeb-766bfaf71dfc.png)

참고 : [docker hub redis](https://hub.docker.com/_/redis/?tab=description)

이제 ```Spring boot```를 켜보자

```Spring boot starter```를 이용해서 ```springDataRedis```와 ```lombok```을 ```dependency```에 넣고 실행시켰다.

### RedisService
> 레디스에 대한 상세 내용은 나중에 알아보고 바로 코드를 작성해보자

```java
@Slf4j
@RequiredArgsConstructor
@Service
public class RedisService {
    
    // redis와 연결하기 위해 redisTemplate을 가져온다
    private final StringRedisTemplate stringRedisTemplate;

    // redis에 key - value 형태로 저장된 값을 가져온다.
    public String getRedisStringValue(String key){
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        String value = stringValueOperations.get(key);
        log.info("Redis key = {}",key);
        log.info("Redis value = {}", value);
        return value;
    }

   // redis를 set한다.
    public void setRedisStringValue(String key, String value){
        ValueOperations<String, String> stringValueOperations = stringRedisTemplate.opsForValue();
        stringValueOperations.set(key,value);
    }

}
```

이제 테스트 코드를 통해 ```redis```와 잘 연동 됐는지 확인하자

### Test
```java
@ExtendWith(SpringExtension.class)
@SpringBootTest
class RedisServiceTest {

    @Autowired
    RedisService redisService;

    @Test
    void  redis_set_get_test(){
        Random random = new Random();
        int i = random.nextInt();
        String key = "thanks" + i;
        redisService.setRedisStringValue(key,"alot");
        String value = redisService.getRedisStringValue(key);

        Assertions.assertThat(value).isEqualTo("alot");
    }

```

아래와 같이 ```key``` ```value```를 로그로 확인할 수 있다. 연동이 잘 됐다!!
![image](https://user-images.githubusercontent.com/87312401/144378212-2350f904-95f1-4155-b9e4-0ec84e942264.png)

```redis-cli```에서도 확인해보면, 지금까지 저장한 내용이 잘 들어있다.
```
keys *
```

![image](https://user-images.githubusercontent.com/87312401/144378362-146af2a1-794c-4402-afe9-723d36115856.png)


## Redis CLI 명령어 정리
이제 ```Redis```예제도 해봤겠다, 명령어를 정리해 보자.

```keys *``` : 현재 저장된 모든 key값 확인, 저장된 데이터가 많을 경우 부하가 심하다.  
```set key value``` : key, value 저장  
```mset [key][value]...``` : 여러개 key value 저장  
```setex key sec value``` : 소멸시간 정하고 저장하기, 초단위  
```get [key]``` : 저장된 key 가져오기  
```del [key]``` : 지정된 key 삭제 , 성공시 1 반환  
```ttl [key]``` : 소멸까지 남은 시간 확인  
```keys *[search]*``` : 검색어를 포함한 key 가져옴  
```rename [key] [renamekey]``` : 이름 변경하기  
```flushall``` : 모든 데이터 삭제  

다음 플젝에는 ```redis```를 사용해보자!



