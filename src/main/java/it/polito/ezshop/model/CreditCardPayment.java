package it.polito.ezshop.model;

public class CreditCardPayment extends Payment{
    //int card; //TODO: define a type for card
    String cardType;
    double outcome;
    public CreditCardPayment(double amount, boolean isReturn) {
        super(amount, isReturn);
    }
    //TODO: implement sendPaymentRequestThroughAPI()

    public String getCardType(){
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public double getOutcome() {
        return outcome;
    }

    public void setOutcome(double outcome) {
        this.outcome = outcome;
    }

}
