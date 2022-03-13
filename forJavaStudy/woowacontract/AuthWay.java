package polymorphism.woowacontract;

import polymorphism.woowacontract.notifier.KaKaoNotifier;
import polymorphism.woowacontract.notifier.Notifier;
import polymorphism.woowacontract.notifier.PassNotifier;
import polymorphism.woowacontract.notifier.SmsNotifier;

public enum AuthWay {

    KAKAO(new KaKaoNotifier()),
    SMS(new SmsNotifier()),
    PASS(new PassNotifier()),
    ;

    private final Notifier notifier;

    AuthWay(Notifier notifier) {
        this.notifier = notifier;
    }

    public Notifier getNotifier() {
        return notifier;
    }
}
