package subway.view;

import java.util.Scanner;

public class InputView {
    private static final String INSERT_FEATURE = "## 원하는 기능을 선택하세요.";
    private static final String INSERT_START = "## 출발역을 입력하세요.";
    private static final String INSERT_END = "## 도착역을 입력하세요.";

    public static String insertFeature(Scanner scanner) {
        System.out.println(INSERT_FEATURE);
        return scanner.nextLine();
    }

    public static String insertStart(Scanner scanner) {
        System.out.println(INSERT_START);
        return scanner.nextLine();
    }

    public static String insertEnd(Scanner scanner) {
        System.out.println(INSERT_END);
        return scanner.nextLine();
    }
}
