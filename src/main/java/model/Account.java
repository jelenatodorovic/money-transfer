package model;

import exception.NotEnoughAccountBalanceException;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

public class Account {
    private static final AtomicInteger counter = new AtomicInteger();
    private transient int id;
    private String name;
    private BigDecimal balance;
    private String currency;

    public Account() {
    }

    public Account(int id, String name, BigDecimal balance, String currency) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.currency = currency;
    }

    public Account(String name, BigDecimal balance, String currency) {
        this.id = counter.getAndIncrement();
        this.name = name;
        this.balance = balance;
        this.currency = currency;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void withdraw(BigDecimal amount) throws NotEnoughAccountBalanceException {
        if (balance.compareTo(amount) == -1)
            throw new NotEnoughAccountBalanceException("Withdraw failed from account: " + id + ". Not enough balance.");
        else {
            balance = balance.subtract(amount);
        }
    }

    public void deposit(BigDecimal amount) {
        balance = balance.add(amount);
    }

    @Override
    public String toString() {
        return "Account{" +
                "name='" + name + '\'' +
                ", balance=" + balance +
                ", currency='" + currency + '\'' +
                '}';
    }
}
