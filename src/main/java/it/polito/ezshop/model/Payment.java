package it.polito.ezshop.model;

//Made by Andrea

public class Payment {
    double amount;
    boolean isReturn;

    public Payment(double amount, boolean isReturn){
        this.amount = amount;
        this.isReturn = isReturn;
    }

    public boolean isReturn(){
        return isReturn;
    }

    public double getAmount(){
        return amount;
    }

    public void setAmount(double amount){
        if(amount>0){
            this.amount = amount;
        }
    }
}
