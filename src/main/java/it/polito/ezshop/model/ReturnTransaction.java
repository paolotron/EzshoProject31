package it.polito.ezshop.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class ReturnTransaction extends BalanceOperationModel{
    ArrayList<ProductTypeModel> productTypeList;
    //Double returnedAmount; Maybe useless because inherit the attribute money
    Ticket ticket;

    public ReturnTransaction(Double amount, LocalDate date, Ticket ticket){
        super("return", amount, date);
        productTypeList = new ArrayList<>();
        this.ticket = ticket;
    }

    public Ticket getTicket(){
        return this.ticket;
    }

    public void deleteTicket(){
        ticket = null;
    }

    public ArrayList<ProductTypeModel> getProductTypeList() {
        return productTypeList;
    }

    public void setProductTypeList(ArrayList<ProductTypeModel> productTypeList) {
        this.productTypeList = productTypeList;
    }

    public void addProductType(ProductTypeModel product){
        productTypeList.add(product);
    }
}
