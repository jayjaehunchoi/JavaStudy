package main.java.baseball;

import java.util.Scanner;

public class Application {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        // TODO ���� ����
       BaseBallGame bg = new BaseBallGame();
       bg.run(scanner);
        
        
    }
}