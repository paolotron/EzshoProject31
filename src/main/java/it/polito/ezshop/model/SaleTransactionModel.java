package it.polito.ezshop.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import it.polito.ezshop.data.TicketEntry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//TODO: modify Ticket and complete setEntries and getEntries methods

public class SaleTransactionModel extends BalanceOperationModel implements it.polito.ezshop.data.SaleTransaction {
    String paymentType;
    double discountRate;
    Integer balanceOperationId;
    Ticket ticket;

    public SaleTransactionModel(){super();}

    public SaleTransactionModel(Double amount, LocalDate date, String paymentType, String time, Ticket ticket, double discountRate){
        super("SALE", amount, date);
        this.paymentType = paymentType;
        this.ticket = ticket;
        this.discountRate = discountRate;
    }

    public SaleTransactionModel(SaleModel sale){
        super("SALE", 0., LocalDate.now());
        this.ticket = sale.generateTicket();
        this.discountRate = sale.getSaleDiscountRate();
    }

    //USE THIS AND NOT TICKET.setPayment
    @JsonIgnore
    public void setTicketPayment(Payment payment){
        this.ticket.setPayment(payment);
        super.setMoney(ticket.getAmount());
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
        return new ArrayList<>(ticket.getTicketEntryModelList());
    }

    @JsonIgnore
    @Override
    public void setEntries(List<TicketEntry> entries) {
        //ticket.setTicketEntryModelList(new ArrayList<>((Collection<? extends TicketEntryModel>) entries));
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

    /**
     * This function returns the total price based on the current TicketEntryList
     * it is useful when to be used when there is an update due to a Return
     * @return returns the new total price
     */
    public double computeCost() {
        double amountAfterReturn = 0 ;
        for(TicketEntryModel entry : ticket.getTicketEntryModelList()){
            amountAfterReturn += entry.computeCost();
        }
        if(amountAfterReturn <= 0)
            return 0;
        return amountAfterReturn;
    }

    /**
     * This function updates the amount payed due to a return
     * @return return the amount to return
     *          (i.e the money payed at first - the total cost of the product returned)
     */
    public double updateAmount(){
        double beforeMoney = money;
        money = computeCost();
        ticket.getPayment().setAmount(money);
        return beforeMoney - money;
    }
}
