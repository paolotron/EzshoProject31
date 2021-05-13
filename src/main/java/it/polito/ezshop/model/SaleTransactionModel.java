package it.polito.ezshop.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import it.polito.ezshop.data.TicketEntry;

import java.time.LocalDate;
import java.util.List;

//TODO: modify Ticket and complete setEntries and getEntries methods

public class SaleTransactionModel extends BalanceOperationModel implements it.polito.ezshop.data.SaleTransaction {
    String paymentType;
    double discountRate;
    //Double cost; Maybe useless because inherit the attribute money
    Ticket ticket;

    public SaleTransactionModel(){super();}

    public SaleTransactionModel(Double amount, LocalDate date, String paymentType, String time, Ticket ticket, double discountRate){
        super("sale", amount, date);
        this.paymentType = paymentType;
        this.ticket = ticket;
        this.discountRate = discountRate;
    }

    public SaleTransactionModel(SaleModel sale){
        super("sale", sale.getTicket().getAmount(), LocalDate.now());
        this.ticket = sale.getTicket();
        this.discountRate = sale.getSaleDiscountRate();
    }

    public Ticket getTicket(){
        return this.ticket;
    }

    public void setTicket(Ticket ticket){this.ticket = ticket;}

    public void deleteTicket(){
        ticket = null;
    }


    public String getPaymentType(){
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Integer computePoint() {
        double points = money / 10;
        return (int) points;
    }

    @JsonIgnore
    @Override
    public Integer getTicketNumber() {
        return ticket.getId();
    }

    @JsonIgnore
    @Override
    public void setTicketNumber(Integer ticketNumber) {
        ticket.setId(ticketNumber);
    }

    @JsonIgnore
    @Override
    public List<TicketEntry> getEntries() {
        return null;
    }

    @JsonIgnore
    @Override
    public void setEntries(List<TicketEntry> entries) {

    }

    @Override
    public double getDiscountRate() {
        return discountRate;
    }

    @Override
    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    @JsonIgnore
    @Override
    public double getPrice() {
        return ticket.amount;
    }

    @JsonIgnore
    @Override
    public void setPrice(double price) {
        ticket.setAmount(price);
    }
}
