package dao;

import exception.AppException;
import model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDAO {

    List<Account> getAllAccounts() throws AppException;

    int createAccount(Account account) throws AppException;

    Account getAccount(int id) throws AppException;

    int updateAccount(int id, BigDecimal newBalance) throws AppException;
}
