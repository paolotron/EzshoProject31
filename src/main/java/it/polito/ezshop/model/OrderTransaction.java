package it.polito.ezshop.model;

import java.time.LocalDate;

public class OrderTransaction extends BalanceOperationModel{
    OrderModel order;

    public OrderTransaction(Double amount, LocalDate date){
        super("order", amount, date);
    }

    public OrderModel getOrder() {
        return order;
    }
}
