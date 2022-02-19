package enum_posting;

public enum Mission {
    RACINGCAR("레이싱카"),
    LOTTO("로또"),
    CHESS("체스"),
    BLACKJACK("블랙잭");

    private final String name;

    Mission(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
