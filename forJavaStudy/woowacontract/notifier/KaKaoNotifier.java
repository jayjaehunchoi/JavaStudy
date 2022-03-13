package polymorphism.woowacontract.notifier;

import polymorphism.woowacontract.AuthResult;
import polymorphism.woowacontract.KakaoApi;
import polymorphism.woowacontract.User;

public class KaKaoNotifier implements Notifier{

    @Override
    public AuthResult authenticate(User user, String number) {
        return KakaoApi.authenticate(user, number);
    }

    @Override
    public String sendAuthenticateNumber(User user) {
        return KakaoApi.authenticateNumber(user);
    }
}
