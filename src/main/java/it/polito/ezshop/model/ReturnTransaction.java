package it.polito.ezshop.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class ReturnTransaction extends BalanceOperationModel{
    ArrayList<Integer> productTypeIdList;
    //Double returnedAmount; Maybe useless because inherit the attribute money
    Ticket ticket;

    public ReturnTransaction(){super();}

    public ReturnTransaction(Double amount, LocalDate date, Ticket ticket){
        super("return", amount, date);
        productTypeIdList = new ArrayList<>();
        this.ticket = ticket;
    }

    public Ticket getTicket(){
        return this.ticket;
    }

    public void setTicket(Ticket t){ticket = t;}

    public void deleteTicket(){
        ticket = null;
    }

    public void setProductTypeIdList(ArrayList<Integer> productTypeIdList) {
        this.productTypeIdList = productTypeIdList;
    }

    public ArrayList<Integer> getProductTypeIdList() {
        return productTypeIdList;
    }

    public void setProductTypeList(ArrayList<Integer> productTypeIdList) {
        this.productTypeIdList = productTypeIdList;
    }

    public void addProductType(Integer productId){
        productTypeIdList.add(productId);
    }
}
