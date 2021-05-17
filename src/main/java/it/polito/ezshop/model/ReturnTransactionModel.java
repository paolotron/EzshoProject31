package it.polito.ezshop.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReturnTransactionModel extends BalanceOperationModel{
    String status;
    List<TicketEntryModel> returnedProductList;
    Integer saleId;
    Payment payment;
    Double amountToReturn;

    public ReturnTransactionModel(){super();}


    public ReturnTransactionModel(ReturnModel returnM){
        super("RETURN", 0., LocalDate.now());
        this.status = returnM.getStatus();
        this.returnedProductList = returnM.getProductList();
        this.saleId = returnM.sale.getBalanceId();
        this.amountToReturn = returnM.getReturnedAmount();
    }

    public ReturnTransactionModel(Double amount, LocalDate date, Ticket ticket){
        super("RETURN", amount, date);
        returnedProductList = new ArrayList<>();
        //this.ticket = ticket;
    }


    public List<TicketEntryModel> getReturnedProductList() {
        return returnedProductList;
    }

    public void setReturnedProductList(List<TicketEntryModel> returnedProductList) {
        this.returnedProductList = returnedProductList;
    }

    public Integer getSaleId() {
        return saleId;
    }

    public void setSaleId(Integer saleId) {
        this.saleId = saleId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        if(payment != null )
            super.money = payment.getAmount();
        this.payment = payment;
    }

    public Double getAmountToReturn() {
        return amountToReturn;
    }

    public void setAmountToReturn(Double amountToReturn) {
        this.amountToReturn = amountToReturn;
    }
}
