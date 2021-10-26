package jdk10;

import java.util.ArrayList;

public class VarTest {

    public static void main(String[] args) {
        // 로컬변수로만 사용 가능 (필드, 파라미터 x)
        var a = 10; // int로 로컬변수 타입 추론
        var result = new ArrayList<Integer>(); // list로 추론
    }
}
