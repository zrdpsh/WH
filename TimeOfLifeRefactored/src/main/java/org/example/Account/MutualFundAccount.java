package org.example.Account;

public class MutualFundAccount extends Account{
    public MutualFundAccount(double balance) {
        super(balance);
    }

//    @Override
//    public double calculateTax() {
//        return balance * 0.45;
//    }

    @Override
    public void accept(AccountVisitor visitor) {
        visitor.visit(this);
    }
}
