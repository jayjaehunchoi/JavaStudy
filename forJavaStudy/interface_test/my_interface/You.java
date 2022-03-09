package interface_test.my_interface;

public class You implements Player{

    @Override
    public void hi() {
        System.out.println("너 안녕");
    }

    @Override
    public void bye() {
        System.out.println("너 잘가");
    }
}
