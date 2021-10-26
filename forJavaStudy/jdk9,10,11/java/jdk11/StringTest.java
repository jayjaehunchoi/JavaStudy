package jdk11;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringTest {
    public static void main(String[] args) {

        // isBlank() - 빈경우엔 true, isEmpty는 size가 0
        String s = "    ";
        char c = ' ';
        boolean whitespace = Character.isWhitespace(c);
        boolean blank = s.isBlank();// Character.isWhitespace()로 체크

        System.out.println("whitespace = " + whitespace);
        System.out.println("blank = " + blank);

        // lines()
        Stream<String> lines = s.lines(); // 라인을 stream 형태로 받아옴
        List<String> collect = lines.collect(Collectors.toList()); // 요런식으로 각 줄의 내용을 List로 받아올 수가 있겠다!

        // repeat() - 반복시켜 문자열 생성
        String repeat = "1".repeat(10);
        System.out.println("repeat = " + repeat);

        // strip() - 공백 제거, trim 보다 더 많은 공백 제거 가능
        String strip = " 재훈 ".strip();
        System.out.println("strip = " + strip);

    }
}
