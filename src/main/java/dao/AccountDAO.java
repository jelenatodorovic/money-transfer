package dao;

import exception.AppException;
import model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDAO {

    public List<Account> getAllAccounts() throws AppException;

    public int createAccount(Account account) throws AppException;

    public Account getAccount(int id) throws AppException;

    public int updateAccount(int id, BigDecimal newBalance) throws AppException;
}
