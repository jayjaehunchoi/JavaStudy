package polymorphism.woowacontract;

import java.util.Objects;

public class PassApi {

    public static String authenticateNumber(final User user) {
        // 외부 api 관련 잔뜩 ~~~~

        String authenticateNumber = "12345";
        System.out.println(user.getPhoneNumber() + " 에 pass 인증번호 : " + authenticateNumber + "를 발송했습니다.");
        return authenticateNumber;
    }

    public static AuthResult authenticate(final User user, final String number) {
        // 외부 api 관련 잔뜩~~~

        if (Objects.isNull(user)) {
            System.out.println("pass 인증실패! 존재하지 않는 유저");
            return AuthResult.FAIL;
        }

        if (!number.equals("12345")) {
            System.out.println("pass 인증실패! 인증 번호 오류");
            return AuthResult.FAIL;
        }

        System.out.println("pass 인증성공!");
        return AuthResult.SUCCESS;
    }
}
