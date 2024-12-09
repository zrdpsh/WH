package org.example.Account;

public class FixedDepositAccount extends Account {
    public FixedDepositAccount(double balance) {
        super(balance);
    }

    @Override
    public double calculateTax() {
        return balance * 0.15 - 24;
    }

}
