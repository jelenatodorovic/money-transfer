package model;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

public class Transfer {
    private static final AtomicInteger counter = new AtomicInteger();
    private int id;
    private int fromId;
    private int toId;
    private BigDecimal amount;
    private TransferStatus transferStatus;

    public Transfer() {
    }

    public Transfer(int id, int fromId, int toId, BigDecimal amount, TransferStatus transferStatus) {
        this.id = id;
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount;
        this.transferStatus = transferStatus;
    }

    public Transfer(int fromId, int toId, BigDecimal amount, TransferStatus transferStatus) {
        this.id = counter.getAndIncrement();
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount;
        this.transferStatus = transferStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransferStatus getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(TransferStatus transferStatus) {
        this.transferStatus = transferStatus;
    }
}
