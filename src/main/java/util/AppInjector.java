package util;

import com.google.inject.AbstractModule;
import dao.AccountDAO;
import dao.AccountDAOImpl;
import dao.TransferDAO;
import dao.TransferDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.AccountService;
import service.AccountServiceImpl;
import service.TransferService;
import service.TransferServiceImpl;

/**
 * Guice module
 */
public class AppInjector extends AbstractModule {

    private static final Logger log = LoggerFactory.getLogger(AppInjector.class);

    @Override
    protected void configure() {
        bind(AccountDAO.class).to(AccountDAOImpl.class);
        bind(TransferDAO.class).to(TransferDAOImpl.class);
        bind(TransferService.class).to(TransferServiceImpl.class);
        bind(AccountService.class).to(AccountServiceImpl.class);
    }
}
