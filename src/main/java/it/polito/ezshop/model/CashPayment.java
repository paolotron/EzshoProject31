package it.polito.ezshop.model;

public class CashPayment extends Payment {
    double cash;
    public CashPayment(int amount, boolean isReturn, double cash) {
        super(amount, isReturn);
        this.cash = cash;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public double computeChange(){
        return cash - amount;
    }

}
