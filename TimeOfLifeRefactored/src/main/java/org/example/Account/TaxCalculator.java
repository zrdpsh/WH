package org.example.Account;

public class TaxCalculator {
    public static void main(String[] args) {
        Account savings = new SavingAccount(1000);
        Account fixedDeposit = new FixedDepositAccount(1000);
        Account mutualFund = new MutualFundAccount(1000);

        System.out.println("Savings Tax: " + savings.calculateTax());
        System.out.println("Fixed Deposit Tax: " + fixedDeposit.calculateTax());
        System.out.println("Mutual Fund Tax: " + mutualFund.calculateTax());
    }
}
