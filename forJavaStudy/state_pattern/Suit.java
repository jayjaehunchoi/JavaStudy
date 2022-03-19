package state_pattern;

public enum Suit {

    DIAMOND("다이아몬드"),
    CLOVER("클로버"),
    HEART("하트"),
    SPADE("스페이드"),
    ;

    private final String name;

    Suit(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
