package interface_test.my_interface;

public class Me implements Player{

    @Override
    public void hi() {
        System.out.println("나 안녕");
    }

    @Override
    public void bye() {
        System.out.println("나 잘가");
    }
}
