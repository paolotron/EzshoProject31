package it.polito.ezshop.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CreditCardPayment extends Payment{
    //int card; //TODO: define a type for card
    final String gateway = "PaymentGateway/cards.txt";

    String cardType;
    double outcome;
    public CreditCardPayment(double amount, boolean isReturn) {
        super(amount, isReturn);
    }

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

    boolean sendPaymentRequestThroughAPI(String cardNumber){
        try {
            BufferedReader read = new BufferedReader(new FileReader(gateway));
            String line;
            line = read.readLine();
            for(;line!=null; line = read.readLine()){
                if(line.startsWith("#"))
                    continue;
                String[] elems = line.split(";");
                if(!elems[0].equals(cardNumber))
                    continue;
                if(Double.parseDouble(elems[1]) < this.amount)
                    return false;
                else{
                    // TODO WRITE
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
