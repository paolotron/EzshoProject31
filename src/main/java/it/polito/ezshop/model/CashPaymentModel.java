package it.polito.ezshop.model;

public class CashPaymentModel extends PaymentModel {
    double cash;
    public CashPaymentModel(double amount, boolean isReturn, double cash) {
        super(amount, isReturn);
        this.cash = cash;
    }

    public CashPaymentModel(){};

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
