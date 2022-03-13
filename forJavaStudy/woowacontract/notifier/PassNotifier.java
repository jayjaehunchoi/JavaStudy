package polymorphism.woowacontract.notifier;

import polymorphism.woowacontract.AuthResult;
import polymorphism.woowacontract.PassApi;
import polymorphism.woowacontract.User;

public class PassNotifier implements Notifier{

    @Override
    public AuthResult authenticate(User user, String number) {
        return PassApi.authenticate(user, number);
    }

    @Override
    public String sendAuthenticateNumber(User user) {
        return PassApi.authenticateNumber(user);
    }
}
