package it.polito.ezshop.model;

import java.util.ArrayList;

public class ReturnModel {
    Integer id;
    String status;
    ArrayList<TicketEntryModel> productList;
    static Integer currentId = 0;

    ReturnModel(){
        id = ++currentId;
        status = "open";
        productList = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<TicketEntryModel> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<TicketEntryModel> productList) {
        this.productList = productList;
    }
}
