package org.example.Account;

public class SavingAccount extends Account{
    public SavingAccount(double balance) {
        super(balance);
    }

//    @Override
//    public double calculateTax() {
//        return balance * 0.10;
//    }

    @Override
    public void accept(AccountVisitor visitor) {
        visitor.visit(this);
    }
}
