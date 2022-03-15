package design_pattern.strategy_pattern;

public class BadCase {

    public String calculateGrade(final int score) {
        if (score < 50) {
            return "F";
        } else if (score < 60) {
            return "D";
        } else if (score < 70) {
            return "C";
        } else if (score < 80) {
            return "B";
        } else if (score < 90) {
            return "A";
        } else {
            return "S";
        }
    }

    public static void main(String[] args) {

    }
}
