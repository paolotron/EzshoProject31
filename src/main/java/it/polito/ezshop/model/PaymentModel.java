package it.polito.ezshop.model;

//Made by Andrea

public class PaymentModel {
    double amount;
    boolean isReturn;

    public PaymentModel(double amount, boolean isReturn){
        this.amount = amount;
        this.isReturn = isReturn;
    }

    public PaymentModel() {
    }

    public boolean isReturn(){
        return isReturn;
    }

    public double getAmount(){
        return amount;
    }

    public void setAmount(double amount){
            this.amount = amount;
    }
}
