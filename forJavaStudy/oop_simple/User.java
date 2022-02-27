package oop_simple;

public class User {

    private final Bank bank;

    public User(Bank bank) {
        this.bank = bank;
    }

    public void depositMoney(int money) {
        bank.deposit(money);
    }

    public void showBalance() {
        bank.checkBalance();
    }

    public static void main(String[] args) {
        User user1 = new User(new HanaBank());
        user1.depositMoney(1000);

        User user2 = new User(new TossBank());
        user2.depositMoney(1000);
        user2.showBalance();
    }
}
