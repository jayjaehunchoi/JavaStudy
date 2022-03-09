package interface_test.my_abstract;

public class GameUser extends Gamer{

    @Override
    public boolean isReceivable() {
        return getResult().size() < 2;
    }
}
