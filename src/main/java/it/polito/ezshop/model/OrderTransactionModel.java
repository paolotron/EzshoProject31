package it.polito.ezshop.model;

import java.time.LocalDate;

public class OrderTransactionModel extends BalanceOperationModel{

    public OrderTransactionModel(){
        super();
    }

    public OrderTransactionModel(OrderModel order, LocalDate date){
        super("ORDER", -order.getTotalPrice(),date);
    }

}
