package org.example.Account;

public class SavingAccount extends Account{
    public SavingAccount(double balance) {
        super(balance);
    }

    @Override
    public double calculateTax() {
        return balance * 0.10;
    }
}
