package it.polito.ezshop.model;

import java.io.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

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
            ArrayList<String> line = new ArrayList<>();
            for(String s = read.readLine(); s!=null; s = read.readLine())
                line.add(s);
            for(int i=0; i<line.size(); i++){
                if(line.get(i).startsWith("#"))
                    continue;
                String[] elems = line.get(i).split(";");
                if(!elems[0].equals(cardNumber))
                    continue;
                if(Double.parseDouble(elems[1]) < this.amount)
                    return false;
                else{
                    elems[1] = (new Double(Double.parseDouble(elems[1]) - this.amount)).toString();
                    StringBuilder build = new StringBuilder();
                    build.append(elems[0]);
                    build.append(";");
                    build.append(elems[1]);
                    build.append("\n");
                    line.set(i, build.toString());
                    BufferedWriter write = new BufferedWriter(new FileWriter(gateway));

                    StringBuilder sb = new StringBuilder();
                    for (String s : line) {
                        sb.append(s);
                    }
                    String str = sb.toString();
                    write.write(str);
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    static public boolean validateCardWithLuhn(String cardNo) {
        int nDigits = cardNo.length();

        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--) {
            int d = cardNo.charAt(i) - '0';
            if (isSecond){
                d = d * 2;
            }

            // We add two digits to handle
            // cases that make two digits
            // after doubling
            nSum += d / 10;
            nSum += d % 10;
            isSecond = !isSecond;
        }
        return (nSum % 10 != 0);
    }
}
