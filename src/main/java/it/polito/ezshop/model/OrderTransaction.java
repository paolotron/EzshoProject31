package it.polito.ezshop.model;

import java.time.LocalDate;

public class OrderTransaction extends BalanceOperationModel{
    OrderModel order;

    public OrderTransaction(OrderModel order, LocalDate date){
        super("OrderTransaction", order.getTotalPrice(),date );
        this.order=order;
    }

    public OrderModel getOrder() {
        return order;
    }

}
