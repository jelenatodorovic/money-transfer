package dao;

import exception.AppException;
import model.Transfer;
import model.TransferStatus;

import java.util.List;

public interface TransferDAO {

    public List<Transfer> getAllTransfers() throws AppException;

    public Transfer getTransfer(int id) throws AppException;

    public int createTransfer(Transfer transfer) throws AppException;

    public int updateTransfer(int id, TransferStatus transferStatus) throws AppException;
}
