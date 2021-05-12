package it.polito.ezshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class Ticket {
    List<TicketEntryModel> ticketEntryModelList;
    int id;
    double amount;
    String status;
    static int currentId = 0;
    Payment payment = null;

    public Ticket(){}

    public Ticket(String status, double amount, int id, List<TicketEntryModel> ticketEntryModelList){
        this.status = status;
        this.amount = amount;
        this.id = id;
        this.ticketEntryModelList = ticketEntryModelList;
    }

    public Ticket(String status, double amount, List<TicketEntryModel> ticketEntryModelList){
        this.id = ++currentId;
        this.status = status;
        this.amount = amount;
        this.ticketEntryModelList = ticketEntryModelList;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public double getAmount(){
        return this.amount;
    }

    public void setAmount(double amount){
        this.amount = amount;
    }

    public Payment getPayment(){
        return payment;
    }

    public void setPayment(Payment p){this.payment = p;}

    @JsonIgnore
    public void setNewPayment(){
        payment = new Payment(amount, false);
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public List<TicketEntryModel> getTicketEntryModelList(){
        return ticketEntryModelList;
    }

    public void setTicketEntryModelList(List<TicketEntryModel> ticketEntryModelList){
        this.ticketEntryModelList = ticketEntryModelList;
    }

}
