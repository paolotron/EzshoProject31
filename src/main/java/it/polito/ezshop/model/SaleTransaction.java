package it.polito.ezshop.model;


import java.time.LocalDate;

public class SaleTransaction extends BalanceOperationModel{
    String paymentType;
    String time;
    //Double cost; Maybe useless because inherit the attribute money

    public SaleTransaction(Double amount, LocalDate date, String paymentType, String time){
        super("sale", amount, date);
        this.paymentType = paymentType;
        this.time = time;
    }

    //TODO: Add an attribute of type Ticket
    //TODO: Add methods getTicket(), deleteTicket(), computePoints()

}
