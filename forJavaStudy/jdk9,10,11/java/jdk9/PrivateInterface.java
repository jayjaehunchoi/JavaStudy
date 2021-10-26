package jdk9;

// jdk 9 부터 인터페이스에 private 메서드가 사용가능해졌다.
// jdk 8 부터 인터페이스에 추상메소드를 정의하고 구현이 가능했는데 jdk9 부터는 private 메서드가 사용가능해지면서 더 자유도가 올랐다
public interface PrivateInterface {
    void exchange();
    default void get(){
        logging("before");
        exchange();
        logging("getCall");
    }

    private void logging(String msg){
        System.out.println(msg);
    }

    private static void logging2(String msg){
        System.out.println(msg);
    }
}
