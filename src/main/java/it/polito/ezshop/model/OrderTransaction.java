package it.polito.ezshop.model;

import java.time.LocalDate;

public class OrderTransaction extends BalanceOperationModel{
    //TODO: getOrder()

    public OrderTransaction(Double amount, LocalDate date){
        super("order", amount, date);
    }

}
