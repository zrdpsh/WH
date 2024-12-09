package org.example.Account;

interface AccountVisitor {
    void visit(SavingAccount sa);
    void visit(FixedDepositAccount fda);
    void visit(MutualFundAccount mfa);
}
