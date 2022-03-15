package design_pattern.strategy_pattern;

public class Win implements BenefitStrategy {

    @Override
    public int calculate(int amount) {
        return amount;
    }
}
