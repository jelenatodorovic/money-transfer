package service;

import exception.AccountNotFoundException;
import exception.NotEnoughAccountBalanceException;

import java.math.BigDecimal;

public interface AccountService {

    boolean withdraw(int accountId, BigDecimal amount) throws AccountNotFoundException, NotEnoughAccountBalanceException;

    boolean deposit(int accountId, BigDecimal amount) throws AccountNotFoundException;
}
