package org.example.Account;

abstract class Account {
    protected double balance;

    public Account(double balance) {
        this.balance = balance;
    }

//    public abstract double calculateTax();

    public abstract void accept(AccountVisitor visitor);
}
