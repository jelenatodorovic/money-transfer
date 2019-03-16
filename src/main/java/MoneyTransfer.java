import com.google.inject.Guice;
import com.google.inject.Injector;
import dao.AccountDAO;
import dao.TransferDAO;
import exception.AccountNotFoundException;
import exception.AppException;
import exception.NotEnoughAccountBalanceException;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Launcher;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import model.Account;
import model.ErrorResponse;
import model.Transfer;
import model.TransferStatus;
import service.AccountService;
import service.TransferService;
import util.AppInjector;
import util.H2Util;

import java.util.List;

public class MoneyTransfer extends AbstractVerticle {

    private Injector injector = Guice.createInjector(new AppInjector());
    private AccountDAO accountDAO = injector.getInstance(AccountDAO.class);
    private TransferDAO transferDAO = injector.getInstance(TransferDAO.class);
    private AccountService accountService = injector.getInstance(AccountService.class);
    private TransferService transferService = injector.getInstance(TransferService.class);

    private String contentType = "application/json; charset=utf-8";

    public static void main(String[] args) {
        H2Util h2 = new H2Util();
        h2.createSchema();
        Launcher.executeCommand("run", MoneyTransfer.class.getName());
    }

    @Override
    public void start(Future<Void> fut) throws Exception {
        Router router = Router.router(vertx);

        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/html").end("Money Transfer");
        });

        vertx
                .createHttpServer()
                .requestHandler(router::accept)
                .listen(
                        8080,
                        result -> {
                            if (result.succeeded()) {
                                fut.complete();
                            } else {
                                fut.fail(result.cause());
                            }
                        }
                );

        router.route().handler(BodyHandler.create());

        router.get("/accounts").handler(this::getAccounts);
        router.get("/accounts/:id").handler(this::getAccount);
        router.post("/accounts").handler(this::createAccount);
        router.put("/accounts").handler(this::updateAccountBalance);
        router.get("/transfers").handler(this::getTransfers);
        router.get("/transfers/:id").handler(this::getTransfer);
        router.post("/transfers").handler(this::createTransfer);
        router.put("/transfers").handler(this::updateTransfer);

    }

    private void getAccounts(RoutingContext routingContext) {
        try {
            routingContext.response()
                    .putHeader("content-type", contentType)
                    .end(Json.encodePrettily(accountDAO.getAllAccounts()));
        } catch (AppException e) {
            routingContext.response()
                    .setStatusCode(500)
                    .end(Json.encodePrettily(new ErrorResponse(500, "Internal server error")));
        }
    }

    private void getAccount(RoutingContext routingContext) {
        try {
            String id = routingContext.request().getParam("id");
            Integer idAsInt = Integer.valueOf(id);
            Account account = accountService.getAccount(idAsInt);
            routingContext.response().setStatusCode(201)
                    .putHeader("content-type", contentType)
                    .end(Json.encodePrettily(account));

        } catch (AppException e) {
            routingContext.response().setStatusCode(500)
                    .putHeader("content-type", contentType)
                    .end(Json.encodePrettily(new ErrorResponse(500, "Internal server error")));
        } catch (NumberFormatException ne) {
            routingContext.response().setStatusCode(400)
                    .putHeader("content-type", contentType)
                    .end(Json.encodePrettily(new ErrorResponse(400, "Use number for account id")));
        } catch (AccountNotFoundException ant) {
            routingContext.response().setStatusCode(500)
                    .putHeader("content-type", contentType)
                    .end(Json.encodePrettily(new ErrorResponse(500, ant.getMessage())));
        }
    }

    private void createAccount(RoutingContext routingContext) {
        try {
            Account account = Json.decodeValue(routingContext.getBodyAsString(), Account.class);
            int id = accountDAO.createAccount(account);
            account.setId(id);
            routingContext.response()
                    .setStatusCode(201)
                    .putHeader("content-type", contentType)
                    .end(Json.encodePrettily(account));
        } catch (AppException s) {
            routingContext.response()
                    .setStatusCode(500)
                    .putHeader("content-type", contentType)
                    .end("Internal server error");
        }
    }

    private void updateAccountBalance(RoutingContext routingContext) {
        try {
            Account account = Json.decodeValue(routingContext.getBodyAsString(), Account.class);
            accountDAO.updateAccount(account.getId(), account.getBalance());
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", contentType)
                    .end(Json.encodePrettily(account));
        } catch (AppException e) {
            routingContext.response()
                    .setStatusCode(500)
                    .putHeader("content-type", contentType)
                    .end(Json.encodePrettily(new ErrorResponse(500, "Internal server error")));
        }
    }

   /* private void deleteAccount(RoutingContext routingContext) {

    }*/

    private void getTransfers(RoutingContext routingContext) {
        try {
            List<Transfer> transfers = transferDAO.getAllTransfers();
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("content-type", contentType)
                    .end(Json.encodePrettily(transfers));
        } catch (AppException e) {
            routingContext.response()
                    .setStatusCode(500)
                    .putHeader("content-type", contentType)
                    .end(Json.encodePrettily(new ErrorResponse(500, "Internal server error")));
        }
    }

    private void getTransfer(RoutingContext routingContext) {
        try {
            String id = routingContext.request().getParam("id");
            Integer idAsInt = Integer.valueOf(id);
            Transfer transfer = transferDAO.getTransfer(idAsInt);
            if (transfer == null) {
                routingContext.response().setStatusCode(204).end("No content");
            } else {
                routingContext.response().setStatusCode(201)
                        .putHeader("content-type", contentType)
                        .end(Json.encodePrettily(transfer));
            }
        } catch (AppException e) {
            routingContext.response().setStatusCode(500)
                    .putHeader("content-type", contentType)
                    .end(Json.encodePrettily(new ErrorResponse(500, "Internal server error")));
        } catch (NumberFormatException ne) {
            routingContext.response().setStatusCode(400)
                    .putHeader("content-type", contentType)
                    .end(Json.encodePrettily(new ErrorResponse(400, "Use number for account id")));
        }
    }

    private void createTransfer(RoutingContext routingContext) {
        try {
            Transfer transfer = Json.decodeValue(routingContext.getBodyAsString(), Transfer.class);
            if (!transfer.getTransferStatus().equals(TransferStatus.CREATED)) {
                routingContext.response().setStatusCode(400)
                        .putHeader("content-type", contentType)
                        .end(Json.encodePrettily(new ErrorResponse(400, "Transfer status should be CREATED")));
            }
            if (transfer.getFromId() == transfer.getToId()) {
                routingContext.response().setStatusCode(400)
                        .putHeader("content-type", contentType)
                        .end(Json.encodePrettily(new ErrorResponse(400, "Use different account for transfer")));
            }
            try {
                Transfer transferCreated = transferService.createTransfer(transfer);
                if (transferCreated == null) {
                    routingContext.response().setStatusCode(500)
                            .putHeader("content-type", contentType)
                            .end(Json.encodePrettily(new ErrorResponse(500, "Transfer could not be created.")));
                } else if (transferCreated.getTransferStatus() == TransferStatus.CREATED) {
                    routingContext.response().setStatusCode(201)
                            .putHeader("content-type", contentType)
                            .end(Json.encodePrettily(transfer));
                } else if (transferCreated.getTransferStatus() == TransferStatus.FAILED) {
                    routingContext.response().setStatusCode(201)
                            .putHeader("content-type", contentType)
                            .end(Json.encodePrettily(transfer));
                } else {
                    routingContext.response().setStatusCode(500)
                            .putHeader("content-type", contentType)
                            .end(Json.encodePrettily(new ErrorResponse(500, "Transfer could not be created.")));
                }
            } catch (NotEnoughAccountBalanceException e) {
                transferDAO.updateTransfer(transfer.getId(), TransferStatus.FAILED);
                routingContext.response().setStatusCode(500)
                        .putHeader("content-type", contentType)
                        .end(Json.encodePrettily(new ErrorResponse(500, e.getMessage())));
            } catch (AccountNotFoundException anf) {
                routingContext.response().setStatusCode(500)
                        .putHeader("content-type", contentType)
                        .end(Json.encodePrettily(new ErrorResponse(500, anf.getMessage())));
            }
        } catch (AppException e) {
            routingContext.response().setStatusCode(500)
                    .putHeader("content-type", contentType)
                    .end(Json.encodePrettily(new ErrorResponse(500, "Could not created transfer")));
        }
    }

    private void updateTransfer(RoutingContext routingContext) {
        try {
            Transfer transfer = Json.decodeValue(routingContext.getBodyAsString(), Transfer.class);
            transferService.finishTransfer(transfer);
            routingContext.response().setStatusCode(200)
                    .putHeader("content-type", contentType)
                    .end(Json.encodePrettily(transfer));
        } catch (AppException e) {
            routingContext.response().setStatusCode(500)
                    .putHeader("content-type", contentType)
                    .end(Json.encodePrettily(new ErrorResponse(500, "Could not update transfer. " + e.getMessage())));
        } catch (AccountNotFoundException ant) {
            routingContext.response().setStatusCode(500)
                    .putHeader("content-type", contentType)
                    .end(Json.encodePrettily(new ErrorResponse(500, ant.getMessage())));
        }
    }
}
