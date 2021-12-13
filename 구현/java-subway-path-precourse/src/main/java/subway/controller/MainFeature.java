package subway.controller;

import subway.utils.ConstantMessages;
import subway.view.InputView;
import subway.view.OutputView;

import java.util.Arrays;
import java.util.Scanner;
import java.util.function.Function;

public enum MainFeature {
    FIND_ROUTE("1",(scanner) -> {
        OutputView.printRouteFeature();
        return InputView.insertFeature(scanner);
    }),
    QUIT("Q",(scanner) -> {
        return null;
    });

    private String selection;
    private Function<Scanner, String> function;

    MainFeature(String selection, Function<Scanner, String> function) {
        this.selection = selection;
        this.function = function;
    }

    public String getSelection() {
        return selection;
    }

    public Function<Scanner, String> getFunction() {
        return function;
    }

    public static MainFeature getMainFeatureByInput(String input) {
        return Arrays.stream(MainFeature.values())
                .filter(value -> value.getSelection().equals(input))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        ConstantMessages.PREFIX_ERROR + ConstantMessages.ERROR_NO_FEATURE));
    }

    public String apply(Scanner scanner) {
        return getFunction().apply(scanner);
    }
}
