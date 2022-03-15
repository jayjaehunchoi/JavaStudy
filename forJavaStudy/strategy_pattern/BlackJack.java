package design_pattern.strategy_pattern;

public class BlackJack implements BenefitStrategy {

    @Override
    public int calculate(int amount) {
        return (int) (amount * 1.5);
    }
}
