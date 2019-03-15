package service;

import com.google.inject.Inject;
import dao.TransferDAO;
import exception.AccountNotFoundException;
import exception.AppException;
import exception.NotEnoughAccountBalanceException;
import model.Transfer;
import model.TransferStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransferServiceImpl implements TransferService {

    private static final Logger log = LoggerFactory.getLogger(TransferServiceImpl.class);

    @Inject
    private AccountService accountService;

    @Inject
    private TransferDAO transferDAO;

    /**
     * Transfer DAO: create transfer with status: CREATED
     * Account service: withdraw money from account if available
     *
     * @param transfer
     * @return
     * @throws NotEnoughAccountBalanceException
     * @throws AppException
     */
    @Override
    public Transfer createTransfer(Transfer transfer) throws AccountNotFoundException, NotEnoughAccountBalanceException {
        try {
            int id = transferDAO.createTransfer(transfer);
            transfer.setId(id);
            boolean withdrawn = accountService.withdraw(transfer.getFromId(), transfer.getAmount());
            if (!withdrawn) {
                // could not withdraw money
                transfer.setTransferStatus(TransferStatus.FAILED);
                finishTransfer(transfer);
                return transfer;
            }
            return transfer;
        } catch (AppException e) {
            log.error("Transfer could not be created: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Account service: deposit money to specified account
     * Transfer DAO: if success update transfer with status
     * if finished with succes status = FINISHED
     * if finished with failure status = FAILED
     * free reserved amount on account from transfer started
     *
     * @param transfer
     * @throws AppException
     */
    @Override
    public void finishTransfer(Transfer transfer) throws AppException {
        Transfer transfer1 = transferDAO.getTransfer(transfer.getId());
        if(transfer1 != null) {
            boolean deposited = accountService.deposit(transfer.getToId(), transfer1.getAmount());
            if (deposited) {
                //finish success
                transferDAO.updateTransfer(transfer.getId(), TransferStatus.FINISHED);
            } else {
                //finish failed
                transferDAO.updateTransfer(transfer.getId(), TransferStatus.FAILED);
                //return amount
                accountService.deposit(transfer.getFromId(), transfer1.getAmount());
            }
        } else {
            throw new AppException("Transfer is null");
        }
    }
}
