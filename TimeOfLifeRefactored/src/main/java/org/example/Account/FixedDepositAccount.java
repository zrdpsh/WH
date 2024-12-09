package org.example.Account;

public class FixedDepositAccount extends Account {
    public FixedDepositAccount(double balance) {
        super(balance);
    }

//    @Override
//    public double calculateTax() {
//        balance = balance * 0.15;
//        if (balance >= 100) return balance- 24;
//        return balance;
//    }

    @Override
    public void accept(AccountVisitor visitor) {
        visitor.visit(this);
    }
}
