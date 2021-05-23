package it.polito.ezshop.model;

import it.polito.ezshop.exceptions.InvalidCreditCardException;

import java.io.*;
import java.util.ArrayList;

public class CreditCardPaymentModel extends PaymentModel {
    final String gateway = "PaymentGateway/cards.txt";

    double outcome;
    public CreditCardPaymentModel(double amount, boolean isReturn) {
        super(amount, isReturn);
    }

    public CreditCardPaymentModel() {
        super();
    }

    public double getOutcome() {
        return outcome;
    }

    public void setOutcome(double outcome) {
        this.outcome = outcome;
    }

    public boolean sendPaymentRequestThroughAPI(String cardNumber) throws InvalidCreditCardException {
        if(cardNumber == null || cardNumber.equals("") || !validateCardWithLuhn(cardNumber))
            throw new InvalidCreditCardException();
        try {
            BufferedReader read = new BufferedReader(new FileReader(gateway));
            ArrayList<String> line = new ArrayList<>();
            for(String s = read.readLine(); s!=null; s = read.readLine())
                line.add(s);
            for (String s : line) {
                if (s.startsWith("#"))
                    continue;
                String[] elems = s.split(";");
                if (!elems[0].equals(cardNumber))
                    continue;
                return super.isReturn() || !(Double.parseDouble(elems[1]) < this.amount);
            }
        } catch (IOException e) {
            return false;
        }
        return false;
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
        return (nSum % 10 == 0);
    }
}
