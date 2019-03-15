package service;

import exception.AccountNotFoundException;
import exception.NotEnoughAccountBalanceException;

import java.math.BigDecimal;

public interface AccountService {

    public boolean withdraw(int accountId, BigDecimal amount) throws AccountNotFoundException,NotEnoughAccountBalanceException;

    public boolean deposit(int accountId, BigDecimal amount);
}
