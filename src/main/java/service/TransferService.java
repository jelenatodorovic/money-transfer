package service;

import exception.AccountNotFoundException;
import exception.AppException;
import exception.NotEnoughAccountBalanceException;
import model.Transfer;

public interface TransferService {

    public Transfer createTransfer(Transfer transfer) throws AccountNotFoundException, NotEnoughAccountBalanceException, AppException;

    public void finishTransfer(Transfer transfer) throws AppException;
}
