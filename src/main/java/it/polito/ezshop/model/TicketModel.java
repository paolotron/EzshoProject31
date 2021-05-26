package it.polito.ezshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class TicketModel {
    List<TicketEntryModel> ticketEntryModelList;
    int id;
    double amount;
    String status;
    static int currentId = 0;
    PaymentModel payment = null;

    public TicketModel(){}


    public TicketModel(String status, double amount, List<TicketEntryModel> ticketEntryModelList){
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

    public PaymentModel getPayment(){
        return payment;
    }

    public void setPayment(PaymentModel p){this.payment = p;}

    @JsonIgnore
    public void setNewPayment(){
        payment = new PaymentModel(amount, false);
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
