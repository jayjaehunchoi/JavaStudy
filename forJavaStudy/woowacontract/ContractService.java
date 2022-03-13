package polymorphism.woowacontract;

import java.io.OutputStream;
import polymorphism.woowacontract.notifier.Notifier;

public class ContractService {

    private final Notifier notifier;

    public ContractService(Notifier notifier) {
        this.notifier = notifier;
    }

    public AuthResult authenticate(final User user, final String number) {
        return notifier.authenticate(user, number);
    }

    public String sendAuthenticateNumber(final User user) {
        return notifier.sendAuthenticateNumber(user);
    }
}
