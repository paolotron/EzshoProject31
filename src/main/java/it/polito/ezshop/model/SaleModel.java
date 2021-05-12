package it.polito.ezshop.model;

import java.util.ArrayList;

public class SaleModel {
    Integer id;
    String status;
    ArrayList<TicketEntryModel> productList;
    double saleDiscountRate;
    static Integer currentId = 0;

    SaleModel() {
        id = ++currentId;
        status = "open";
        productList = new ArrayList<>();
        saleDiscountRate = 0;
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

    public double getSaleDiscountRate() {
        return saleDiscountRate;
    }

    public void setSaleDiscountRate(double saleDiscountRate) {
        this.saleDiscountRate = saleDiscountRate;
    }


}
