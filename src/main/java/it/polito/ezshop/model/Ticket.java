package it.polito.ezshop.model;

import it.polito.ezshop.data.ProductType;

import java.util.List;

public class Ticket {
    List<ProductTypeModel> productTypeList;
    int id;
    double amount;
    String status;
    static int currentId = 0;
    Payment payment = null;

    public Ticket(String status, double amount, int id, List<ProductTypeModel> productTypeList){
        this.status = status;
        this.amount = amount;
        this.id = id;
        this.productTypeList = productTypeList;
    }

    public Ticket(String status, double amount, List<ProductTypeModel> productTypeList){
        this.id = ++currentId;
        this.status = status;
        this.amount = amount;
        this.productTypeList = productTypeList;
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

    public void setPayment(){
        payment = new Payment(amount, false);
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public List<ProductTypeModel> getProductTypeList(){
        return productTypeList;
    }

    public void setProductTypeList(List<ProductTypeModel> productTypeList){
        this.productTypeList = productTypeList;
    }

}
