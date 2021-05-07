package it.polito.ezshop.model;

import java.time.LocalDate;

public class OrderTransaction extends BalanceOperationModel{
    OrderModel order;
    LocalDate date;

    public OrderTransaction(OrderModel order, LocalDate date){
        super("OrderTransaction", order.getTotalPrice(),date );
        this.order=order;
        this.date=date;
    }

    public OrderModel getOrder() {
        return order;
    }

}
