package polymorphism.woowacontract.notifier;

import polymorphism.woowacontract.AuthResult;
import polymorphism.woowacontract.SmsApi;
import polymorphism.woowacontract.User;

public class SmsNotifier implements Notifier{

    @Override
    public AuthResult authenticate(User user, String number) {
        return SmsApi.authenticate(user, number);
    }

    @Override
    public String sendAuthenticateNumber(User user) {
        return SmsApi.authenticateNumber(user);
    }
}
