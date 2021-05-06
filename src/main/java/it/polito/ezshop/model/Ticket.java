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

    int getId(){
        return this.id;
    }

    void setId(int id){
        this.id = id;
    }

    int getAmount(){
        return this.amount;
    }

    void setAmount(int amount){
        this.amount = amount;
    }

    String getStatus(){
        return status;
    }

    void setStatus(String status){
        this.status = status;
    }

    List<ProductType> getProductTypeList(){
        return productTypeList;
    }

    void setProductTypeList(List<ProductType> productTypeList){
        this.productTypeList = productTypeList;
    }

}
