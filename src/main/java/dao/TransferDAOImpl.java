package dao;

import exception.AppException;
import model.Transfer;
import model.TransferStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.H2Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransferDAOImpl implements TransferDAO {

    private static final Logger log = LoggerFactory.getLogger(TransferDAOImpl.class);

    private static final String SQL_GET_ALL_TRANSFERS = "SELECT * FROM Transfer";
    private static final String SQL_GET_TRANSFER = "SELECT * FROM Transfer WHERE id=?";
    private static final String SQL_INSERT_TRANSFER = "INSERT INTO Transfer(fromId, toId, amount, transferStatus) VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE_TRANSFER = "UPDATE Transfer SET transferStatus=? WHERE id=?";

    @Override
    public List<Transfer> getAllTransfers() throws AppException {
        Connection connection = null;
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        List<Transfer> transfers = new ArrayList<Transfer>();
        try {
            connection = H2Util.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_ALL_TRANSFERS);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Transfer transfer = new Transfer(resultSet.getInt("id"),
                        resultSet.getInt("fromId"),
                        resultSet.getInt("toId"),
                        resultSet.getBigDecimal("amount"),
                        TransferStatus.valueOf(resultSet.getString("transferStatus")));
                transfers.add(transfer);
            }
            return transfers;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Error on DAO getAllTransfers: {}", e.getMessage());
            return null;
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Transfer getTransfer(int id) throws AppException {
        Connection connection = null;
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        Transfer transfer = null;
        try {
            connection = H2Util.getConnection();
            preparedStatement = connection.prepareStatement(SQL_GET_TRANSFER);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                transfer = new Transfer(resultSet.getInt("id"),
                        resultSet.getInt("fromId"),
                        resultSet.getInt("toId"),
                        resultSet.getBigDecimal("amount"),
                        TransferStatus.valueOf(resultSet.getString("transferStatus")));

                log.debug("Get Transfer: " + transfer);
            }
            return transfer;
        } catch (SQLException e) {
            throw new AppException("Transfer could not be fetched.");
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int createTransfer(Transfer transfer) throws AppException {
        Connection connection = null;
        PreparedStatement preparedStatement;
        ResultSet generatedKeys;
        try {
            connection = H2Util.getConnection();
            preparedStatement = connection.prepareStatement(SQL_INSERT_TRANSFER);
            preparedStatement.setInt(1, transfer.getFromId());
            preparedStatement.setInt(2, transfer.getToId());
            preparedStatement.setBigDecimal(3, transfer.getAmount());
            preparedStatement.setString(4, transfer.getTransferStatus().name());
            int created = preparedStatement.executeUpdate();
            if (created == 0) {
                throw new AppException("Transfer could not be created");
            }
            generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new AppException("Transfer could not be created.");
            }
        } catch (SQLException e) {
            throw new AppException("Transfer could not be created.");
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int updateTransfer(int id, TransferStatus transferStatus) throws AppException {
        Connection connection = null;
        PreparedStatement preparedStatement;
        try {
            Transfer transfer = getTransfer(id);
            connection = H2Util.getConnection();
            preparedStatement = connection.prepareStatement(SQL_UPDATE_TRANSFER);
            preparedStatement.setInt(2, id);
            preparedStatement.setString(1, transferStatus.name());
            int updated = preparedStatement.executeUpdate();
            if (updated == 0) {
                throw new AppException("Transfer could not be updated");
            } else
                return updated;
        } catch (SQLException e) {
            throw new AppException("Transfer could not be updated.");
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
