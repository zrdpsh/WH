package org.example.Account;

public class MutualFundAccount extends Account{
    public MutualFundAccount(double balance) {
        super(balance);
    }

    @Override
    public double calculateTax() {
        return balance * 0.45;
    }
}
