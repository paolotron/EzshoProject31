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

    /** made by Manuel
     * If ticket Entry is already present only quantity should be updated
     * @param t TicketEntry to be added
     * @return true when added new ticketentry or updated correctly the qty of an existing one
     *          false if value passed is null or had a BAD AMOUNT (i.e negative or 0)
     */
    public boolean addProduct (TicketEntryModel t) {
        if(t == null)
            return false;
        for(TicketEntryModel entry : productList){
            if(entry.getBarCode().equals(t.getBarCode())){
                return entry.addAmount(t.getAmount());
            }
        }
        productList.add(t);
        return true;
    }

    /**
     * made by Manuel
     * @param barCode barCode of the entry to be removed
     * @param amount qty to be removed
     * @return true if the entry had been removed from the list or if the amouint has been decreased correctly
     *          false if there is no entry with barCode passed or if amount is greater than the amount
     *          of that entry
     */
    public boolean removeProduct(String barCode, int amount) {
        //TODO: implement a method, maybe static to validated barCode
        /*if(barCode is valid)
            return false;*/
        for(TicketEntryModel entry : productList){
            if(entry.getBarCode().equals(barCode)){
                boolean res = entry.removeAmount(amount);
                if(res && entry.getAmount() == 0)
                    productList.remove(entry);
                return res;
            }
        }
        return false;
    }

}
