package design_pattern.strategy_pattern;

public class Benefit {

    private int amount;
    private BenefitStrategy benefitStrategy;

    public Benefit(int amount, BenefitStrategy benefitStrategy) {
        this.amount = amount;
        this.benefitStrategy = benefitStrategy;
    }

    public void calculateBenefit() {
        amount = benefitStrategy.calculate(amount);
    }

    public int getAmount() {
        return amount;
    }
}
