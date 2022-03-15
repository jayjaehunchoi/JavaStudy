package design_pattern.strategy_pattern;

public class Main {
    public static void main(String[] args) {
        Benefit benefit = new Benefit(1000, new BlackJack());
        benefit.calculateBenefit();
        System.out.println("블랙잭: " + benefit.getAmount());

        Benefit benefit1 = new Benefit(1000, new Win());
        benefit1.calculateBenefit();
        System.out.println("승리: " + benefit1.getAmount());

        Benefit benefit2 = new Benefit(1000, new Draw());
        benefit2.calculateBenefit();
        System.out.println("무승부: " +benefit2.getAmount());

        Benefit benefit3 = new Benefit(1000, new Lose());
        benefit3.calculateBenefit();
        System.out.println("패배: " + benefit3.getAmount());
    }
}
