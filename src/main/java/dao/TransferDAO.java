package dao;

import exception.AppException;
import model.Transfer;
import model.TransferStatus;

import java.util.List;

public interface TransferDAO {

    List<Transfer> getAllTransfers() throws AppException;

    Transfer getTransfer(int id) throws AppException;

    int createTransfer(Transfer transfer) throws AppException;

    int updateTransfer(int id, TransferStatus transferStatus) throws AppException;
}
