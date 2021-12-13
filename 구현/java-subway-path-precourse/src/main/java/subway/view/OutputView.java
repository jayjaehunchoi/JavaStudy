package subway.view;

import subway.domain.Station;
import subway.utils.ConstantMessages;

import java.util.List;

public class OutputView {
    private static final String PRINT_MAIN_TITLE = "## 메인 화면";
    private static final String PRINT_MAIN_FIND_ROUTE = "1. 경로 조회";
    private static final String PRINT_MAIN_QUIT = "Q. 종료";

    private static final String PRINT_ROUTE_FEATURE_TITLE = "## 경로 기준";
    private static final String PRINT_MIN_DISTANCE = "1. 최단 거리";
    private static final String PRINT_MIN_TIME = "2. 최소 시간";
    private static final String PRINT_GO_BACK = "B. 돌아가기";

    private static final String PRINT_FIND_RESULT_TITLE = "## 조회 결과";
    private static final String PRINT_DISTANCE = "총 거리: ";
    private static final String PRINT_TIME = "총 소요 시간: ";
    private static final String PRINT_KM = "km";
    private static final String PRINT_MIN = "분";
    private static final String PRINT_LINE = "---";

    public static void printMain() {
        System.out.println(PRINT_MAIN_TITLE);
        System.out.println(PRINT_MAIN_FIND_ROUTE);
        System.out.println(PRINT_MAIN_QUIT);
        printLineBreak();
    }

    public static void printRouteFeature() {
        System.out.println(PRINT_ROUTE_FEATURE_TITLE);
        System.out.println(PRINT_MIN_DISTANCE);
        System.out.println(PRINT_MIN_TIME);
        System.out.println(PRINT_GO_BACK);
        printLineBreak();
    }

    public static void printFindResult(List<Station> stations, int distance, int time) {
        System.out.println(PRINT_FIND_RESULT_TITLE);
        System.out.println(ConstantMessages.PREFIX_INFO + PRINT_LINE);
        System.out.println(ConstantMessages.PREFIX_INFO + PRINT_TIME + time + PRINT_MIN);
        System.out.println(ConstantMessages.PREFIX_INFO + PRINT_DISTANCE + distance + PRINT_KM);
        System.out.println(ConstantMessages.PREFIX_INFO + PRINT_LINE);
        stations.forEach(station -> System.out.println(station));
        printLineBreak();
    }

    public static void printGuideMessage(String message) {
        System.out.println(message);
        printLineBreak();
    }

    public static void printLineBreak() {
        System.out.println();
    }
}
