package it.polito.ezshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReturnModel {
    Integer id;
    SaleTransactionModel sale;
    String status;
    ArrayList<TicketEntryModel> productList;
    double returnedAmount;
    static Integer currentId = 0;

    public ReturnModel(SaleTransactionModel sale){
        this.sale = sale;
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

    public double setPayment(){
        return -1;
    }

    public double getReturnedAmount() {
        return returnedAmount;
    }

    public void setReturnedAmount(double returnedAmount) {
        this.returnedAmount = returnedAmount;
    }

    public SaleTransactionModel getSale() {
        return sale;
    }

    public void setSale(SaleTransactionModel sale) {
        this.sale = sale;
    }

    public void commit(Map<String, ProductTypeModel> productMap){
        this.status = "closed";
        for (TicketEntryModel entry : productList) {
            productMap.get(entry.getBarCode()).updateAvailableQuantity(entry.getAmount());
        }
        for (TicketEntryModel entry : productList) {
            for (TicketEntryModel saleEntry : sale.getTicket().getTicketEntryModelList()) {
                if (saleEntry.getBarCode().equals(entry.getBarCode())) {
                    saleEntry.removeAmount(entry.getAmount());
                    if(saleEntry.getAmount() == 0)
                        sale.getTicket().getTicketEntryModelList().remove(saleEntry);
                    break;
                }
            }
        }
        returnedAmount = sale.updateAmount();
    }
}
