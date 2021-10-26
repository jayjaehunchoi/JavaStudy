package user;

public class StubWeakPasswordChecker implements WeakPasswordChecker {
    private boolean weak;

    @Override
    public boolean checkPasswordWeak(String pw) {
        return weak;
    }

    public void setWeak(boolean weak){
        this.weak = weak;
    }
}
