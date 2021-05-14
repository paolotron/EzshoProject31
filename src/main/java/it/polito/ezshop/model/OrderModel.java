package it.polito.ezshop.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import it.polito.ezshop.data.Order;

import java.time.LocalDate;

public class OrderModel implements Order{

    String productCode;
    Integer orderId;
    String status;      //ISSUED(ordered), PAYED(ordered and payed), COMPLETED(arrived)
    double pricePerUnit;
    int quantity;
    double totalPrice;
    LocalDate date;
    static Integer currentOrderId = 1;

    public OrderModel(){}
    //CONSTRUCTOR
    public OrderModel(String productCode, int quantity, double pricePerUnit){
        this.productCode = productCode;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.date=LocalDate.now();
        this.orderId = currentOrderId;
        currentOrderId++;
    }

    @Override
    public Integer getBalanceId() {
        return this.orderId;
    }

    @Override
    public void setBalanceId(Integer balanceId) {
        this.orderId=balanceId;
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

    @JsonIgnore
    public LocalDate getDate(){
        return this.date;
    }

    public void setDateS(String date){
        this.date = LocalDate.parse(date);
    }

    public String getDateS(){
        return this.date.toString();
    }

    /**
     * Made by OMAR
     * this method returns the total cost of the order
     */

    public double getTotalPrice(){
        computeTotalPrice();
        return this.totalPrice;
    }

    public void setTotalPrice(double price){
        this.totalPrice = price;
    }
    /**
     * Made by OMAR
     * this method computes the total cost of the order
     */
    public void computeTotalPrice(){
        this.totalPrice=this.pricePerUnit*this.quantity;
    }
}
