package it.polito.ezshop.model;

import java.time.LocalDate;

public class OrderTransactionModel extends BalanceOperationModel{
    OrderModel order;

    public OrderTransactionModel(){
        super();
    }

    public OrderTransactionModel(OrderModel order, LocalDate date){
        super("ORDER", -order.getTotalPrice(),date);
        this.order=order;
    }

    public OrderModel getOrder() {
        return order;
    }


    public void setOrder(OrderModel order){this.order = order;}

}
