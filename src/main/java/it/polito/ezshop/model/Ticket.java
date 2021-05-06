package it.polito.ezshop.model;

import it.polito.ezshop.data.ProductType;

import java.util.List;

public class Ticket {
    List<ProductType> productTypeList;
    int id;
    int amount;
    String status;
    static int currentId = 0;

    public Ticket(String status, int amount, int id, List<ProductType> productTypeList){
        this.status = status;
        this.amount = amount;
        this.id = id;
        this.productTypeList = productTypeList;
    }

    public Ticket(String status, int amount, List<ProductType> productTypeList){
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

    public int getAmount(){
        return this.amount;
    }

    public void setAmount(int amount){
        this.amount = amount;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public List<ProductType> getProductTypeList(){
        return productTypeList;
    }

    public void setProductTypeList(List<ProductType> productTypeList){
        this.productTypeList = productTypeList;
    }

}
