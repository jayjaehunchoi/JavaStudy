import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

// 빠르게 기능만 구현하기 위해 설계는 무시한 점 참고 바람.
/* 설명
1. 같은 이메일이 들어올 때 오류가 발생한다.
2. 올바르지 않은 형식의 이메일에 오류가 발생한다.
3. 만약 두조건 모두 통과하면 uuid 를 통해 4개 하이픈을 가진 4개그룹 16진수 난수를 발급해준다.
4. while문을 돌린 이유는 자바 코드 내에서 db 없이 map으로 db 역할을 구현하기 위해 사용 , 실제라면 while문은 없고 db와 연동해서 확인하게 될거임! 
(프로그램이 종료가 안돼야 정상! 하지만 지금은 어쩔 수 없이 오류코드 나면 종료되게 설정)
5. 프로그램이었다면 맡아줘야 할 역할을 주석에 달아줬음 참고!
*/


// email이 올바른 형식이 아닐때
class NotEmailException extends Exception{
	private String addr;
	
	public NotEmailException(String message) {
		super(message);
		
	}
	
}

// 이미 주어진 쿠폰일때
class AlreadyGivenException extends Exception{
	private String addr;
	
	public AlreadyGivenException(String message) {
		super(message);
		
	}
	
}

// 데이터베이스에서 이메일을 확인하는 클래스
// 원래 설계대로라면  service > repository로 나뉘어야함.
class CheckEmail{
	
	public void checkNot(String addr) throws NotEmailException {
		if(addr.length() < 4) {
			throw new NotEmailException("올바른 메일주소가 아닙니다");
		}
		else {
			if(!addr.substring(addr.length()-4, addr.length()).equals(".com")) {
				throw new NotEmailException("올바른 메일주소가 아닙니다");
			}
		}
		
	}
	
	public void checkAlr(String addr) throws AlreadyGivenException {
		if(Main.maps.containsKey(addr)) {
			throw new AlreadyGivenException("이미 쿠폰이 발급된 메일입니다.");
		}
	}
	
}

// 기능이 수행되는 Controller에 쿠폰 번호 발급이라는 service가 담겨있는거라고 생각하면 됨.
class Main{
	
	static final String email = "recipent@example.com"; // email 구조
	static Map<String, String> maps = new HashMap<>(); // db역할
	
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        boolean exceptionHappend = false;
        
        // 입력값은 getMapping 이라고 생각하고 값을 가져와서 domain으로 보내주는 거라고 생각하면됨
        // userEmail domain에 id, addr getters and setters 모두 존재하는거라고 생각해주삼
        
        while(!exceptionHappend) { // 우선 db빼고 기능이 되는지 확인하기 위해
        	 System.out.println("아이디를 입력하세요 ===============");
             String id = sc.next();
             String addr = "";
             String userEmail =  email.replace("recipent", id);
             System.out.println("주소를 선택하세요 ===============");
             System.out.println("1. naver.com // 2. gmail.com // 3. 직접입력");
             int selection = sc.nextInt();
            
             
             // 원래라면 getMapping으로 선택값 가져와서
             // 선택값을 입력하는 과정이라고 생각하면 편함
             switch(selection) {
             case 1 :
             	addr = "naver.com";
             	userEmail = userEmail.replace("example.com", addr);
             	break;
             case 2 :
             	addr = "naver.com";
             	userEmail = userEmail.replace("example.com", addr);
             	break;
             case 3 :
            	System.out.println("주소를 입력하세요 ===============");
             	addr = sc.next();
     			    userEmail = userEmail.replace("example.com", addr);
             	
             	break;
             }
             
             // 요놈이 dao를 돌리는 녀석이라고 보면됨.
             // db 체크, 발급 서비스 까지의 과정
             CheckEmail c = new CheckEmail();
             
             try {
              c.checkNot(userEmail);
              c.checkAlr(userEmail);

              String uuid = UUID.randomUUID().toString().substring(9,28);
              maps.put(userEmail,uuid);

              System.out.println("쿠폰 발급 대상저장 완료 : " + userEmail);
                  System.out.println("쿠폰이 발급되었습니다 = " + uuid);

            } catch (NotEmailException ne) {
              ne.printStackTrace();
              exceptionHappend =true;
            } catch (AlreadyGivenException ae) {
              ae.printStackTrace();
              exceptionHappend =true;
            } finally {
              // dao에서는 수행이 모두 끝나면 닫아주는 작업이 필요함 

            }
    
        }
    }
}
