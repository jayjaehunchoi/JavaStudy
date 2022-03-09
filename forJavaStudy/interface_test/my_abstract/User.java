package interface_test.my_abstract;

public class User extends Gamer{

    @Override
    public boolean isReceivable() {
        return getResult().size() < 1;
    }
}
