package it.polito.ezshop.model;


import java.time.LocalDate;

public class SaleTransaction extends BalanceOperationModel{
    String paymentType;
    String time;
    //Double cost; Maybe useless because inherit the attribute money
    Ticket ticket;

    public SaleTransaction(Double amount, LocalDate date, String paymentType, String time, Ticket ticket){
        super("sale", amount, date);
        this.paymentType = paymentType;
        this.time = time;
        this.ticket = ticket;
    }

    //TODO: Add methods getTicket(), deleteTicket(), computePoints()

    public Ticket getTicket(){
        return this.ticket;
    }

    public void deleteTicket(){
        ticket = null;
    }

    public int computePoints(){
        return ticket.getAmount(); //
    }

    public String getTime(){
        return time;
    }

    public void setTime(String time){
        this.time = time;
    }

    public String getPaymentType(){
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}
