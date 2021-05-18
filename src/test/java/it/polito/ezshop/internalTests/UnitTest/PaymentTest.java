package it.polito.ezshop.internalTests.UnitTest;

import it.polito.ezshop.exceptions.InvalidCreditCardException;
import it.polito.ezshop.model.CreditCardPaymentModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class PaymentTest {
    @Test
    void testCorrectLuhn(){
        Assertions.assertTrue(CreditCardPaymentModel.validateCardWithLuhn("5265807692"));
        Assertions.assertTrue(CreditCardPaymentModel.validateCardWithLuhn("6214838176"));
    }

    @Test
    void testWrongLuhn(){
        Assertions.assertFalse(CreditCardPaymentModel.validateCardWithLuhn("ABC"));
        Assertions.assertFalse(CreditCardPaymentModel.validateCardWithLuhn("51658026"));
        Assertions.assertFalse(CreditCardPaymentModel.validateCardWithLuhn("6234838176"));
    }

    @Test
    void testCorrectPaymentWithAPI() throws IOException, InvalidCreditCardException {
        CreditCardPaymentModel payment = new CreditCardPaymentModel();
        String cardNumber = "5265807692";
        payment.setAmount(20);
        BufferedWriter writer = new BufferedWriter(new FileWriter("PaymentGateway/cards.txt"));
        writer.write("#Comment\n#Comment\n5265807692;30");
        writer.close();
        Assertions.assertTrue(payment.sendPaymentRequestThroughAPI(cardNumber));
        new BufferedWriter(new FileWriter("PaymentGateway/cards.txt")).close();
    }

    @Test
    void testFailPaymentWithAPI() throws IOException, InvalidCreditCardException {
        CreditCardPaymentModel payment = new CreditCardPaymentModel();
        String cardNumber = "5265807692";
        payment.setAmount(20);
        BufferedWriter writer = new BufferedWriter(new FileWriter("PaymentGateway/cards.txt"));
        writer.write("#Comment\n#Comment\n5265807692;10");
        writer.close();
        Assertions.assertFalse(payment.sendPaymentRequestThroughAPI(cardNumber));
        Assertions.assertFalse(payment.sendPaymentRequestThroughAPI("6214838176"));
        new BufferedWriter(new FileWriter("PaymentGateway/cards.txt")).close();
    }

    @Test
    void testInvalidPaymentWithAPI() throws IOException{
        CreditCardPaymentModel payment = new CreditCardPaymentModel();
        String cardNumber = "1234";
        payment.setAmount(20);
        BufferedWriter writer = new BufferedWriter(new FileWriter("PaymentGateway/cards.txt"));
        writer.write("#Comment\n#Comment\n1234;30");
        writer.close();
        Assertions.assertThrows(InvalidCreditCardException.class, ()->payment.sendPaymentRequestThroughAPI(cardNumber));
        new BufferedWriter(new FileWriter("PaymentGateway/cards.txt")).close();
    }

}
