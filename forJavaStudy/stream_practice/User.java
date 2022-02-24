package stream_practice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class User {

    private final String name;
    private final int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public static void main(String[] args) {

        // create IntStream for using reduce
        int sum = getUsers().stream()
                .mapToInt(User::getAge)
                .reduce(0, Integer::sum);

        // create Map by using toMap
        Map<String, Integer> collect = getUsers().stream()
                .collect(Collectors.toMap(User::getName, User::getAge));

        // create Map by using groupingBy
        Map<Integer, Set<User>> collect1 = getUsers().stream()
                .collect(Collectors.groupingBy(User::getAge, Collectors.toSet()));

        // user mapToObj to create Object
        List<Numbers> collect2 = IntStream.rangeClosed(0, 10)
                .mapToObj(Numbers::new)
                .filter(numbers -> numbers.getNumber() <= 5)
                .collect(Collectors.toList());

        System.out.println(sum);
        System.out.println(collect);
        System.out.println(collect1);
    }

    static class Numbers {
        private final int number;

        public Numbers(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }
    }

    private static List<User> getUsers() {
        User user1 = new User("후니", 26);
        User user2 = new User("후니2", 27);
        User user3 = new User("후니3", 27);
        User user4 = new User("후니4", 28);

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);

        return users;
    }
}
