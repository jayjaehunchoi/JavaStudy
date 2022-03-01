package optional_practice;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

public class OptionalTest {

    public static void main(String[] args) {
        Optional<User> user = Optional.of(new User("최재훈", LocalDate.of(1998, 1, 8)));
        Integer age = user.map(User::calculateAge)
                .filter(tempAge -> tempAge > 20)
                .orElseThrow(NoSuchElementException::new);

        int i = OptionalInt.of(1).orElseGet(() -> 2);
        long l = OptionalLong.of(1L).orElseThrow(IllegalAccessError::new);
        double v = OptionalDouble.of(1.1).orElse(2.2);

        List<OptionalInt> optionalIntList = new ArrayList<>();


        System.out.println(age);
    }

    static class User {
        private final String name;
        private final LocalDate birthDay;

        public User(String name, LocalDate birthDay) {
            this.name = name;
            this.birthDay = birthDay;
        }

        public int calculateAge() {
            return LocalDateTime.now().getYear() - birthDay.getYear();
        }
    }
}
