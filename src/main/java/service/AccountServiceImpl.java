package service;

import com.google.inject.Inject;
import dao.AccountDAO;
import exception.AccountNotFoundException;
import exception.AppException;
import exception.NotEnoughAccountBalanceException;
import model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class AccountServiceImpl implements AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Inject
    AccountDAO accountDAO;

    @Override
    public boolean withdraw(int accountId, BigDecimal amount) throws AccountNotFoundException, NotEnoughAccountBalanceException {
        Account account = null;
        try {
            account = accountDAO.getAccount(accountId);
            if(account == null) {
                throw new AccountNotFoundException("Account was not found for account id: " + accountId);
            }
            account.withdraw(amount);
            int updated = accountDAO.updateAccount(accountId, account.getBalance());
            return updated == 1;
        } catch (AppException e) {
            return false;
        }
    }

    @Override
    public boolean deposit(int accountId, BigDecimal amount) {
        Account account = null;
        try {
            account = accountDAO.getAccount(accountId);
            account.deposit(amount);
            int updated = accountDAO.updateAccount(accountId, account.getBalance());
            return updated == 1;
        } catch (AppException e) {
            return false;
        }
    }
}
