package design_pattern.strategy_pattern;

import java.util.Arrays;
import java.util.function.Predicate;

public enum Score {

    A("A", score -> score > 90),
    B("B", score -> score > 80),
    C("C", score -> score > 70),
    F("F", score -> score < 50);

    private final String name;
    private final Predicate<Integer> predicate;

    Score(String name, Predicate<Integer> predicate) {
        this.name = name;
        this.predicate = predicate;
    }

    public static Score valueOf(int score) {
        return Arrays.stream(values())
                .filter(value -> value.predicate.test(score))
                .findFirst()
                .orElse(F);
    }
}
