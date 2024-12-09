package org.example.Account;

public class TaxCalculatorVisitor implements AccountVisitor{
    @Override
    public void visit(SavingAccount sa) {
        System.out.println("Savings Tax: " + sa.balance * 0.10);
    }

    @Override
    public void visit(FixedDepositAccount fda) {
        double tmp = fda.balance * 0.15;
        fda.balance = tmp >= 100? tmp - 24 : tmp;
        System.out.println("Savings Tax: " + fda.balance);
    }

    @Override
    public void visit(MutualFundAccount mfa) {
        System.out.println("Savings Tax: " + mfa.balance * 0.45);
    }
}
