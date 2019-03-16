package service;

import exception.AccountNotFoundException;
import exception.AppException;
import exception.NotEnoughAccountBalanceException;
import model.Transfer;

public interface TransferService {

    Transfer createTransfer(Transfer transfer) throws AccountNotFoundException, NotEnoughAccountBalanceException, AppException;

    void finishTransfer(Transfer transfer) throws AccountNotFoundException, AppException;
}
