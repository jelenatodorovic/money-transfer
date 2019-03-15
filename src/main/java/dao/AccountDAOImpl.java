package dao;

import exception.AppException;
import model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.H2Util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAOImpl implements AccountDAO {

    private static final Logger log = LoggerFactory.getLogger(AccountDAOImpl.class);

    private static final String SQL_GET_ALL_ACC = "SELECT * FROM Account";
    private static final String SQL_INSERT_ACC = "INSERT INTO Account(name, balance, currency) VALUES (?, ?, ?)";
    private static final String SQL_GET_ACC = "SELECT * FROM Account WHERE id=?";
    private static final String SQL_UPDATE_ACC = "UPDATE Account SET balance=? WHERE id=?";

    @Override
    public List<Account> getAllAccounts() throws AppException {
        Connection connection = null;
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        List<Account> accounts = new ArrayList<Account>();
        try {
            connection = H2Util.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_ALL_ACC);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Account account = new Account(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getBigDecimal("balance"),
                        resultSet.getString("currency"));
                accounts.add(account);
            }
            return accounts;
        } catch (SQLException e) {
            throw new AppException("Accounts could not be fetched");
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int createAccount(Account account) throws AppException{
        Connection connection = null;
        PreparedStatement preparedStatement;
        ResultSet generatedKeys;
        try {
            connection = H2Util.getConnection();
            preparedStatement = connection.prepareStatement(SQL_INSERT_ACC);
            preparedStatement.setString(1, account.getName());
            preparedStatement.setBigDecimal(2, account.getBalance());
            preparedStatement.setString(3, account.getCurrency());
            int created = preparedStatement.executeUpdate();
            if (created == 0) {
                throw new AppException("Account could not be created");
            }
            generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                log.error("Account could not be created.");
                throw new AppException("Account could not be created.");
            }

        } catch (SQLException e) {
            throw new AppException("Account could not be created.");
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Account getAccount(int id) throws AppException {
        Connection connection = null;
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        Account account = null;
        try {
            connection = H2Util.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_ACC);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                account = new Account(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getBigDecimal("balance"),
                        resultSet.getString("currency"));

                    log.debug("Get Account: " + account);
            }
            return account;
        } catch (SQLException e) {
            throw new AppException("Account could not be fetched.");
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int updateAccount(int id, BigDecimal newBalance)throws AppException {
        Connection connection = null;
        PreparedStatement preparedStatement;
        try {
            connection = H2Util.getConnection();
            preparedStatement = connection.prepareStatement(SQL_UPDATE_ACC);
            preparedStatement.setInt(2, id);
            preparedStatement.setBigDecimal(1, newBalance);
            int updated = preparedStatement.executeUpdate();
            if(updated == 0) {
                log.error("Account could not be updated");
                throw new AppException("Account could not be updated");
            } else
                return updated;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new AppException("Account could not be updated.");
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
