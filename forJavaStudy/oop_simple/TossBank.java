package oop_simple;

public class TossBank implements Bank{

    private int balance;

    public TossBank() {
        this.balance = 0;
    }

    public void deposit(final int money) {
        balance += (money + 100);
    }

    public void withdraw(final int money) {
        if (money < balance) {
            throw new IllegalArgumentException();
        }
        balance -= money;
    }

    @Override
    public void checkBalance() {
        System.out.println(balance);
    }

}
