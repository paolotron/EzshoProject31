package it.polito.ezshop.internalTests.UnitTest;

import it.polito.ezshop.exceptions.InvalidCreditCardException;
import it.polito.ezshop.model.CreditCardPaymentModel;
import org.junit.After;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.*;

public class CreditCardPaymentTest {
    @Test
    public void testCorrectLuhn(){
        assertTrue(CreditCardPaymentModel.validateCardWithLuhn("5265807692"));
        assertTrue(CreditCardPaymentModel.validateCardWithLuhn("6214838176"));
    }

    @Test
    public void testWrongLuhn(){
        assertFalse(CreditCardPaymentModel.validateCardWithLuhn("ABC"));
        assertFalse(CreditCardPaymentModel.validateCardWithLuhn("51658026"));
        assertFalse(CreditCardPaymentModel.validateCardWithLuhn("6234838176"));
    }

    @Test
    public void testCorrectPaymentWithAPI() throws IOException, InvalidCreditCardException {
        CreditCardPaymentModel payment = new CreditCardPaymentModel();
        String cardNumber = "5265807692";
        payment.setAmount(20);
        BufferedWriter writer = new BufferedWriter(new FileWriter("PaymentGateway/cards.txt"));
        writer.write("#Comment\n#Comment\n5265807692;30");
        writer.close();
        assertTrue(payment.sendPaymentRequestThroughAPI(cardNumber));
        new BufferedWriter(new FileWriter("PaymentGateway/cards.txt")).close();
    }

    @Test
    public void testFailPaymentWithAPI() throws IOException, InvalidCreditCardException {
        CreditCardPaymentModel payment = new CreditCardPaymentModel();
        String cardNumber = "5265807692";
        payment.setAmount(20);
        File f = new File("PaymentGateway/cards.txt");
        f.delete();
        BufferedWriter writer = new BufferedWriter(new FileWriter("PaymentGateway/cards.txt"));
        writer.write("#Comment\n#Comment\n5265807692;10");
        writer.close();
        assertFalse(payment.sendPaymentRequestThroughAPI(cardNumber));
        assertFalse(payment.sendPaymentRequestThroughAPI("6214838176"));
        new BufferedWriter(new FileWriter("PaymentGateway/cards.txt")).close();
    }

    @Test
    public void testInvalidPaymentWithAPI() throws IOException{
        CreditCardPaymentModel payment = new CreditCardPaymentModel();
        String cardNumber = "1234";
        payment.setAmount(20);
        BufferedWriter writer = new BufferedWriter(new FileWriter("PaymentGateway/cards.txt"));
        writer.write("#Comment\n#Comment\n1234;30");
        writer.close();
        assertThrows(InvalidCreditCardException.class, ()->payment.sendPaymentRequestThroughAPI(cardNumber));
        new BufferedWriter(new FileWriter("PaymentGateway/cards.txt")).close();
    }

    @Test
    public void testConstructor(){
        CreditCardPaymentModel cardP = new CreditCardPaymentModel(100., true);
        cardP.setOutcome(50.);
        assertEquals(100,cardP.getAmount(), 0.01);
        assertEquals(50, cardP.getOutcome(), 0.01);
    }

    @Test
    public void testCompleteCoverage() throws IOException, InvalidCreditCardException {
        CreditCardPaymentModel payment = new CreditCardPaymentModel();
        payment.setAmount(20.0);
        String validCardNumber = "5100293991053009";
        String nonPresentCardNumber = "4716258050958645";
        File f = new File("PaymentGateway/cards.txt");
        f.delete();
        assertFalse(payment.sendPaymentRequestThroughAPI(validCardNumber));
        f.createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter("PaymentGateway/cards.txt"));
        writer.write("#Comment\n4485370086510891;150.00\n5100293991053009;100.00");
        writer.close();
        assertTrue(payment.sendPaymentRequestThroughAPI(validCardNumber)); //T1
        assertFalse(payment.sendPaymentRequestThroughAPI(nonPresentCardNumber)); //T3
        assertThrows(InvalidCreditCardException.class , ()-> payment.sendPaymentRequestThroughAPI(null));//T4
        writer = new BufferedWriter(new FileWriter("PaymentGateway/cards.txt"));
        writer.write("#Comment\n4485370086510891;150.00\n5100293991053009;10.00");
        writer.close();
        assertFalse(payment.sendPaymentRequestThroughAPI(validCardNumber)); //T3

    }

    @Test
    public void testMultipleCondition(){
        CreditCardPaymentModel payment = new CreditCardPaymentModel();
        assertThrows(InvalidCreditCardException.class , ()-> payment.sendPaymentRequestThroughAPI(null));//T4
        assertThrows(InvalidCreditCardException.class , ()-> payment.sendPaymentRequestThroughAPI(""));//T5
        assertThrows(InvalidCreditCardException.class , ()-> payment.sendPaymentRequestThroughAPI("5100293111053009"));//T6
    }


    @Test
    public void testLoopCoverage() throws IOException, InvalidCreditCardException {
        CreditCardPaymentModel payment = new CreditCardPaymentModel();
        payment.setAmount(20.0);
        String validCardNumber = "5100293991053009";
        assertFalse(payment.sendPaymentRequestThroughAPI(validCardNumber));// T7
        BufferedWriter writer = new BufferedWriter(new FileWriter("PaymentGateway/cards.txt"));
        writer.write("#Comment");
        writer.close();
        assertFalse(payment.sendPaymentRequestThroughAPI(validCardNumber)); // T8
        writer = new BufferedWriter(new FileWriter("PaymentGateway/cards.txt"));
        writer.write("#Comment\n4485370086510891;150.00\n5100293991053009;100.00");
        writer.close();
        assertTrue(payment.sendPaymentRequestThroughAPI(validCardNumber)); //T1
    }

    @After
    public void clearFileText() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("PaymentGateway/cards.txt"));
        writer.write("");
        writer.close();
    }

}
