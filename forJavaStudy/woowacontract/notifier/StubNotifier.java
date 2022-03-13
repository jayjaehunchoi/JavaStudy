package polymorphism.woowacontract.notifier;

import polymorphism.woowacontract.AuthResult;
import polymorphism.woowacontract.User;

public class StubNotifier implements Notifier {

    @Override
    public AuthResult authenticate(User user, String number) {
        if (!number.equals("12345")) {
            return AuthResult.FAIL;
        }
        return AuthResult.SUCCESS;
    }

    @Override
    public String sendAuthenticateNumber(User user) {
        return "12345";
    }
}
