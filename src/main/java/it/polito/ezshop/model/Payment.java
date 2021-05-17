package it.polito.ezshop.model;

//Made by Andrea

public class Payment {
    double amount;
    boolean isReturn;

    public Payment(double amount, boolean isReturn){
        this.amount = amount;
        this.isReturn = isReturn;
    }

    public Payment() {
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
