package racingcar;

import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        // Todo ±¸Çö
        Racingcar car = new Racingcar(scanner);
        car.run();
    }
}