package jdk9;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionFactory {

    public static void main(String[] args) {

        //List<Integer> makeListJdk8 = Arrays.asList(1, 2, 3);
        // JDK9 부터는 컬렉션에 of 메서드가 붙어 쉽게 컬렉션을 생성할 수 있다.
        List<Integer> list = List.of(1,2,3);
        Map<String, Integer> map = Map.of("a",1,"b",2);
        Set<Integer> set = Set.of(1,2,3);
    }
}
