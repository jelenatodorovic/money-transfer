package service;

import exception.AccountNotFoundException;
import exception.AppException;
import exception.NotEnoughAccountBalanceException;
import model.Account;

import java.math.BigDecimal;

public interface AccountService {

    boolean withdraw(int accountId, BigDecimal amount) throws AccountNotFoundException, NotEnoughAccountBalanceException;

    boolean deposit(int accountId, BigDecimal amount) throws AccountNotFoundException;

    Account getAccount(int id) throws AccountNotFoundException, AppException;
}
