package it.polito.ezshop.model;

import it.polito.ezshop.data.TicketEntry;

public class TicketEntryModel implements TicketEntry {

    String barCode;
    String productDescription;
    int amount;
    double pricePerUnit;
    double discountRate;

    TicketEntryModel(){}

    //TODO: DiscountRate should me 0 by default, no need on constructor
    public TicketEntryModel(String barCode, String productDescription, int amount, double pricePerUnit, double discountRate) {
        this.barCode = barCode;
        this.productDescription = productDescription;
        this.amount = amount;
        this.pricePerUnit = pricePerUnit;
        this.discountRate = discountRate;
    }

    public TicketEntryModel(ProductTypeModel product, int amount, double discountRate){
        this.barCode = product.getBarCode();
        this.productDescription = product.getProductDescription();
        this.pricePerUnit = product.getPricePerUnit();
        this.discountRate = discountRate;
        this.amount = amount;
    }

    @Override
    public String getBarCode() {
        return barCode;
    }

    @Override
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    @Override
    public String getProductDescription() {
        return productDescription;
    }

    @Override
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public double getPricePerUnit() {
        return pricePerUnit;
    }

    @Override
    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    @Override
    public double getDiscountRate() {
        return discountRate;
    }

    @Override
    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    /**
     * @param a amount to be added
     * @return true if correctly updated
     *          false if amount to be added is negative
     */
    public boolean addAmount(int a) {
        if (a <= 0 )
            return false;
        amount += a;
        return true;
    }

    /**
     * @param a amount to be removed
     * @return true if correctly updated
     *          false if quantity to be removed is negative or greater than current amount
     */
    public boolean removeAmount(int a){
        if(a <= 0 || amount - a < 0)
            return false;
        amount -= a;
        return true;
    }

    public double computeCost() {
        return amount*pricePerUnit*(1-discountRate);
    }
}
