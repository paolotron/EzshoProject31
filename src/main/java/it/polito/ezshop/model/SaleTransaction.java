package it.polito.ezshop.model;


import it.polito.ezshop.data.TicketEntry;

import java.time.LocalDate;
import java.util.List;

//TODO: modify Ticket and complete setEntries and getEntries methods

public class SaleTransaction extends BalanceOperationModel implements it.polito.ezshop.data.SaleTransaction {
    String paymentType;
    String time;
    double discountRate;
    //Double cost; Maybe useless because inherit the attribute money
    Ticket ticket;

    public SaleTransaction(Double amount, LocalDate date, String paymentType, String time, Ticket ticket, double discountRate){
        super("sale", amount, date);
        this.paymentType = paymentType;
        this.time = time;
        this.ticket = ticket;
        this.discountRate = discountRate;
    }

    public Ticket getTicket(){
        return this.ticket;
    }

    public void deleteTicket(){
        ticket = null;
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

    public Integer computePoint() {
        double points = money / 10;
        return (int) points;
    }

    @Override
    public Integer getTicketNumber() {
        return ticket.getId();
    }

    @Override
    public void setTicketNumber(Integer ticketNumber) {
        ticket.setId(ticketNumber);
    }

    @Override
    public List<TicketEntry> getEntries() {
        return null;
    }

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

    @Override
    public double getPrice() {
        return ticket.amount;
    }

    @Override
    public void setPrice(double price) {
        ticket.setAmount(price);
    }
}
