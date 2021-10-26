## Login
자바 FX를 이용하여 DB와 연동되는 로그인 , 회원가입 시스템을 만들었다.

### Database
MySQL을 사용하였다. 

Connection 객체로 DriverManager.getConnection("로컬호스트주소","id","비번")을 사용하여 db와 연결을 이어갔다. (이때 Class.forName 으로 호출한 Driver를 자동으로 DriverManager에 붙여준다고 생각하면 될듯 ) 

그리고 꼭 메서드가 마무리되면 , if(conn != null && !conn.isClosed) 두가지 조건으로 확인하고 db를 닫아주었다. 추가로 PreparedStatement(조건문 간단하게 실행해줌)나 Statement(sql조건문 실행)또한 닫아준다 (닫아주는 것에 대한 이유는 자세히 알아봐야 할 것 같다.)

#### PreparedStatement 와 Statement의 사용
PreparedStatemnet psmt = null;
psmt = conn.prepareStatement(SQL) ; // SQL 변수에 입력한 sql입력을 conn에 세팅해놓는다. 이때 변수 사이에 ? 로 표시된 부분에 채워넣을 수 있다.
psmt.setString(1, id); // 대강 이런방식으로
psmt.executeUpdate //이 방식으로 실행시킨다. 
st = conn.createStatement;
rs = st.executeQuery(SQL);  // SQL 입력을 실행시켜서 ResultSet에 담아준다. (중복확인, 로그인 - 비밀번호 확인 등에 사용될 수 있음)
while(rs.next){ return true;} // 있으면 트루 리턴해준다 ~!

db연동을 편리하게 만들기 위해 DBConnect라는 클래스를 만들었지만, 아직 사용이 서툴어 매 메서드마다 connection 을 다시 만들었다.. 기본적으로 전체 클래스에 적용이 되고 
각 메서드에서는 사용 > 초기화 기능을 추가해주면 좋을 것 같다는 생각이 든다. 

db연동 시에는 예외처리가 필수이다. 거의 매 순간 예외처리를 해야한다고 보면된다. 


### JavaFX
Java FX를 Controller로 작동시켰다. Controller클래스와 fxml코드는 fxml코드 내 각 fx:controller(컨트롤러 클래스) onAction(메소드) fx:id (변수)로 연동된다.

Main에서는 첫 실행을 맡는다. FXML로더를 통해 Parent 객체를 세팅하고 , Scene으로 객체를 받아서 Stage에 scene를 세팅하고 > primaryStage.setScene(new Scene(root)) setTitle, show를 한다.
Controller는 연동할 fxml과 비슷한 이름으로 간다. 그래서 나는 loginController를 만들었다. login , Create 두개로 나뉘었어야 됐을것 같은데 나누지는 못했다.

Alert 객체로 오류나 알림을 처리해줬고 ActionEvent 붙이고 버튼이 눌릴 때 새로운 stage 세팅 하고 (new Stage > root > scene > stage ...) close 메서드를 만들어 버튼으로 현재 scene의 getScene().getWindow()를 받아 close해줬다고 보면 될 것 같다. 

