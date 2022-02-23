package caching;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

public class LottoNumber2 implements Comparable<LottoNumber2> {

    public static final int MIN_LOTTO_NUMBER = 1;
    public static final int MAX_LOTTO_NUMBER = 45;

    private final int number;

    public LottoNumber2(final int number) {
        if (number < MIN_LOTTO_NUMBER || number > MAX_LOTTO_NUMBER) {
            throw new IllegalArgumentException("[ERROR] 입력값이 1 이상 45 이하여야 합니다.");
        }
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public int compareTo(LottoNumber2 o) {
        return Integer.compare(this.number, o.number);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LottoNumber2 that = (LottoNumber2) o;
        return number == that.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}
