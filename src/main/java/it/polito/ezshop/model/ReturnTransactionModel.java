package it.polito.ezshop.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReturnTransactionModel extends BalanceOperationModel{
    String status;
    List<TicketEntryModel> returnedProductList;
    Integer saleId;
    public ReturnTransactionModel(){super();}

    public ReturnTransactionModel(ReturnModel returnM){
        super("return", returnM.getReturnedAmount(), LocalDate.now());
        this.status = returnM.getStatus();
        this.returnedProductList = returnM.getProductList();
        this.saleId = returnM.sale.getBalanceId();
    }

    public ReturnTransactionModel(Double amount, LocalDate date, Ticket ticket){
        super("return", amount, date);
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
}
