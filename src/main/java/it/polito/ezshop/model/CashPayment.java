package it.polito.ezshop.model;

public class CashPayment extends Payment {
    double cash;
    public CashPayment(double amount, boolean isReturn, double cash) {
        super(amount, isReturn);
        this.cash = cash;
    }

    public CashPayment(){};

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }


    public double computeChange(){
        if(this.cash <= 0) return -1;
        if(this.amount > this.cash) return -1;
        if(this.amount <= 0) return -1;
        return cash - amount;
    }

}
