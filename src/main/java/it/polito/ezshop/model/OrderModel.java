package it.polito.ezshop.model;
import it.polito.ezshop.data.Order;

import java.time.LocalDate;

public class OrderModel implements Order{

    String productCode;
    Integer orderId;
    String status;
    double pricePerUnit;
    int quantity;
    double totalPrice;
    LocalDate date;

    //CONSTRUCTOR
    public OrderModel(String productCode, int quantity, double pricePerUnit){
        this.productCode = productCode;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
    }

    @Override //TODO
    public Integer getBalanceId() {
        return null;
    }

    @Override //TODO
    public void setBalanceId(Integer balanceId) {

    }
    @Override
    public String getProductCode() {
        return this.productCode;
    }
    @Override
    public void setProductCode(String productCode) {
        this.productCode=productCode;
    }

    @Override
    public double getPricePerUnit() {
        return this.pricePerUnit;
    }

    @Override
    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit=pricePerUnit;
    }

    @Override
    public int getQuantity() {
        return this.quantity;
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity=quantity;
    }

    @Override
    public String getStatus() {
        return this.status;
    }

    @Override
    public void setStatus(String status) {
        this.status=status;
    }

    @Override
    public Integer getOrderId() {
        return this.orderId;
    }

    @Override
    public void setOrderId(Integer orderId) {
        this.orderId=orderId;
    }

    public LocalDate getDate(){
        return this.date;
    }


    /**
     * Made by OMAR
     * this method returns the total cost of the order
     */
    public double getTotalPrice(){
        return this.totalPrice;
    }
    /**
     * Made by OMAR
     * this method computes the total cost of the order
     */
    public void computeTotalPrice(){
        this.totalPrice=pricePerUnit*quantity;
    }
}
