package oop_simple;

public class HanaBank implements Bank {

    private int balance;

    public HanaBank() {
        this.balance = 0;
    }

    public void deposit(final int money) {
        balance += money;
    }

    public void withdraw(final int money) {
        if (money < balance) {
            throw new IllegalArgumentException();
        }
        balance -= money;
    }

    @Override
    public void checkBalance() {

    }
}
