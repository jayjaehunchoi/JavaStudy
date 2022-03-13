package polymorphism.woowacontract.notifier;

import polymorphism.woowacontract.AuthResult;
import polymorphism.woowacontract.User;

public interface Notifier {

    public AuthResult authenticate(final User user, final String number);

    public String sendAuthenticateNumber(final User user);
}
