- Идея паттерна Посетитель: 
можно передавать один объект (со всем, что внутри) другому объекту в качестве аргумента у метода.
--> можно передать методу одного объекта экземпляр второго, а уже изнутри второго, теперь попавшего в scope первого, вызвать первый и совершить с ним что-то полезное. 

- Такого вкладывание работает, если надо любой ценой избегать редактирования кода первого объекта - т.е. при работе с любым легаси, т.е. почти всегда.
Мы меняем состояние первого объекта извне, не меняя его код.

- В примере ниже первоначальные классы реализуют минимально отличающиеся варианты подсчёта налоговой ставки - отличие в том, какой множитель употребляется в методе calculateTax. Мы уже экономим что-то за счёт наследования, но систему можно упростить ещё, вынеся всё меняющееся поведение вовне, и собрав его в одном месте (можно предположить, что ставка будет меняться, или что добавится новый метод подсчёта).  

- Совсем не редактировать нельзя, однако единственное, что надо добавить в исходный код - точку, где Посетитель попадёт внутрь искомого объекта (здесь это новый метод accept(visitor), Плюс закомментировать избыточный calculateTax(), и собрать нужный функционал внутри TaxCalculatorVisitor.
Теперь весь потенциально варьируемый код находится в этом классе, а изначальную иерархию можно оставить в покое. Аналогичным образом можно работать вообще с любым состоянием первого объекта.


```java
 abstract class Account {
    protected double balance;

    public Account(double balance) {
        this.balance = balance;
    }

//    public abstract double calculateTax();

    public abstract void accept(AccountVisitor visitor);
}

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

public class TaxCalculator {
    public static void main(String[] args) {
        Account savings = new SavingAccount(1000);
        Account fixedDeposit = new FixedDepositAccount(1000);
        Account mutualFund = new MutualFundAccount(1000);

        AccountVisitor taxCalculator = new TaxCalculatorVisitor();

//        System.out.println("Savings Tax: " + savings.calculateTax());
//        System.out.println("Fixed Deposit Tax: " + fixedDeposit.calculateTax());
//        System.out.println("Mutual Fund Tax: " + mutualFund.calculateTax());

        savings.accept(taxCalculator);
        fixedDeposit.accept(taxCalculator);
        mutualFund.accept(taxCalculator);
    }
}

// REFACTORING:

interface AccountVisitor {
    void visit(SavingAccount sa);
    void visit(FixedDepositAccount fda);
    void visit(MutualFundAccount mfa);
}

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


```
