# Spring boot와 AWS S3를 연동하여 이미지 저장하기
이번 프로젝트는 많은 이미지 파일이 저장될 것으로 예상하기 때문에 (여행 관련 sns 서비스) 로컬 저장소나 resource 폴더에 이미지를 저장하는 것은 절대 불가능하다고 생각하였다. 따라서 찾은 대안이 ```AWS S3```를 연동하여 브라우저에서 넘어온 ```multipartfiles```를 ```버킷```에 저장하는 형태이다.

## 1. 버킷 생성
사실 버킷 생성 부분은 온라인에 찾아보면 많다.  
간단한 과정은 버킷 생성 -> 모든 퍼블릭 액세스 차단 -> ARN키와 Put, Get 등록하여 액세스 조건 키 주입 -> IAM 사용자 설정 의 과정을 거친다.

## 2. Spring boot
이제 부터 Spring boot에서 진행되어야 할 설정을 설명하고자 한다.

### build.gradle
아래 ```dependency``` 를 통해 ```aws```와 연동할 수 있는 기본 라이브러리를 받아올 수 있다.
```groovy
implementation 'org.springframework.cloud:spring-cloud-starter-aws:2.0.1.RELEASE'
```

### application-aws.properties
```properties
cloud.aws.credentials.accessKey={access key}
cloud.aws.credentials.secretKey={secret key}
cloud.aws.s3.bucket={버킷 명}
cloud.aws.region.static=ap-northeast-2  
cloud.aws.stack.auto=false

#application.properties==============================
spring.profiles.include=aws
```

> access key 와 secret key는 굉장히 중요한 정보이다. 따라서 , .gitignore에 해당 폴더를 추가해서 github에 공개되는일은 막자. 배포할 때는 AWS에서 또 다른 설정이 있기때문에 걱정말고 올리지 말자. 기본 properties에 등록하기 위해 마지막 줄과 같은 코드를 application.properties에 추가하자.

### AwsConfig
> 사실 Spring cloud AWS를 사용하면 AmazonS3Client는 자동으로 의존성 주입을 받는다고 한다. 하지만 이번에는 수동으로 설정을 관리하는 작업을 진행해보려한다.

```java
@Configuration
public class AmazonS3Config{
	@Value("${cloud.aws.credentials.accessKey}") // properties에 세팅한 값 가져오기
	private String accessKey;
	
	@Value("${cloud.aws.credentials.secretKey}")
	private String secretKey;
	
	@Value("${cloud.aws.region.static}")
	private String region;
	
	// 위에서 세팅한 값 바탕으로 S3Client build
	@Bean
	public AmazonS3Client amazonS3Client(){
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);  
		return (AmazonS3Client) AmazonS3ClientBuilder.standard()  
		        .withRegion(region)  
		        .withCredentials(new AWSStaticCredentialsProvider(awsCreds))  
		        .build();
	}
}
```

## S3Uploader

> 실제 S3에 업로드를 하는 로직이다. 기본적인 순서는 간단하다.

1. ```multiPartFile```을 ```file```로 전환하여 ```local``` 작업 폴더에 저장해둔다.
2. 이미지의 이름이 겹치는 것을 방지하기 위해 ```UUID```값을 준 저장용 이미지 파일명을 부여하고 s3에 put한다.
3. ```local```작업 폴더에 있는 파일을 삭제한다.
4. ```운영db```에 저장된 루트 폴더를 ```insert```한다. (개인적으로 개발 예정)

이제 위 순서에 맞게 ```S3Uploader``` 코드를 작성해보자.

```java
@Slf4j  
@RequiredArgsConstructor  
@Component  
public class S3Uploader {
	private final AmazonS3Client amazonS3Client;
	
	private static String DIR_NAME = "static";
	private String localFileDir = System.getProperty("user.dir"); // 현재 작업 폴더

	@Value("${cloud.aws.s3.bucket}")
	public String bucket;
}
```
기본 세팅을 위 코드처럼 작성해준다.
1. ```AmazonS3Client```를 의존성 주입받아 사용하고
2. DIR_NAME을 하나의 필드변수로 두어 루트 폴더를 선언한다.
3. ```local``` 저장 폴더 또한 선언해준다.

```java
public List<MyFile> uploadFile(List<MultipartFile> multipartFiles) throws IOException{
	List<MyFile> myFiles = new ArrayList<>();
	 
	 // 멀티 파트 파일을 내가 원하는 객체의 값으로 받아오는 과정, 여러장의 사진을 가져올예정이기 때문에 여러장을 처리해주는 로직을 작성한다. 
	for (MultipartFile multipartFile : multipartFiles) {
		File uploadFile = convert(multipartFile).orElseThrow(() -> new IllegalArgumentException("전환 실패"));
		myFiles.add(uploadToS3(uploadFile));
	}
	return myFiles;
}
```

### convert
> multipartFile을 file로 전환하여 local에 저장하는 작업
```java
private Optional<File> convert(MultipartFile multipartFile) throws IOException{
	File convertFile = new File(localFileDir + "/" + multipartFile.getOriginalFilename());  
	if(convertFile.createNewFile()){  
	    try (FileOutputStream fos = new FileOutputStream(convertFile)){  // byte코드로 전환
	        fos.write(multipartFile.getBytes());  
		}  
	    return Optional.of(convertFile);  
	}  
	return Optional.empty();
}
```

### uploadToS3
> 로컬에 저장된 파일을 , S3에 저장하기 알맞게 변환해주고 저장하는 메서드
```java
private MyFile uploadToS3(File uploadFile){
	String originalName = uploadFile.getName();  
	String fileName = DIR_NAME+"/"+ UUID.randomUUID()+originalName; // 저장을 위한 준비 
	String storeFileName = putS3(uploadFile, fileName); // 저장 로직 수행
	removeLocalFile(uploadFile); // 로컬 파일 제거
	return new MyFile(originalName, storeFileName);
}
```

### putS3
> 직접적으로 S3 버킷에 put하는 메서드
```java
private String putS3(File uploadFile, String fileName){
	amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
	.withCannedAcl(CannedAccesControlList.PublicRead)) // public access 접근 가능한 파일 생성
	return amazonS3Client.getUrl(bucket,fileName).toString(); // 이미지 요청 받을 수 있는 path
}
```

### removeLocalFile
> Local에  S3 file 전송용으로 저장되어있던 파일 삭제
```java
private void removetLocalFile(File file){
	if(file.delete()){
		log.info("로컬 파일 제거");
		return;
	}
	log.info("로컬 파일 제거 실패");
}
```

위와같이 코드를 작성하고 간단한 ```Controller```를 만들어 ```Postman```으로 테스트해보자.
## Controller 작성 및 Test
```java
@RequiredArgsConstructor  
@RestController  
public class UploadController {  
  
    private final S3Uploader s3Uploader;  
  
  @PostMapping("/upload")  
    public ResponseEntity<?> testUpload(@RequestParam("images") List<MultipartFile> multipartFiles) throws IOException{  
        return ResponseEntity.ok(s3Uploader.uploadFiles(multipartFiles).stream().map(file -> file.getStoreFileName()).collect(Collectors.toList()));  
  }  
}
```

![image](https://user-images.githubusercontent.com/87312401/140319904-c538ae03-e648-4bd2-9767-9d7215201d2f.png)
이렇게 ```real Path```를 받아 browser에서 출력해줄 수 있게 된다.

```S3```를 사용하게 되면 엄청나게 많은 용량의 이미지를 ```버킷```에 대신 저장하여 서버의 부하를 막아줄 수 있어 , 파일을 많이 관리해야 하는 서비스라면 필수적으로 사용해야하는 부분이라고 생각된다.
