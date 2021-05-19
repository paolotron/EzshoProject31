package it.polito.ezshop.internalTests.UnitTest;

import it.polito.ezshop.exceptions.InvalidCreditCardException;
import it.polito.ezshop.model.CreditCardPaymentModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PaymentWhiteTest {

    @Test
    void testConstructor(){
        CreditCardPaymentModel cardP = new CreditCardPaymentModel(100., true);
        cardP.setOutcome(50.);
        Assertions.assertEquals(100,cardP.getAmount());
        Assertions.assertEquals(50, cardP.getOutcome());
    }

    @Test
    void testCompleteCoverage() throws IOException, InvalidCreditCardException {
        CreditCardPaymentModel payment = new CreditCardPaymentModel();
        payment.setAmount(20.0);
        String validCardNumber = "5100293991053009";
        String nonPresentCardNumber = "4716258050958645";
        File f = new File("PaymentGateway/cards.txt");
        f.delete();
        Assertions.assertFalse(payment.sendPaymentRequestThroughAPI(validCardNumber));
        f.createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter("PaymentGateway/cards.txt"));
        writer.write("#Comment\n4485370086510891;150.00\n5100293991053009;100.00");
        writer.close();
        Assertions.assertTrue(payment.sendPaymentRequestThroughAPI(validCardNumber)); //T1
        Assertions.assertFalse(payment.sendPaymentRequestThroughAPI(nonPresentCardNumber)); //T3
        Assertions.assertThrows(InvalidCreditCardException.class , ()-> payment.sendPaymentRequestThroughAPI(null));//T4
        writer = new BufferedWriter(new FileWriter("PaymentGateway/cards.txt"));
        writer.write("#Comment\n4485370086510891;150.00\n5100293991053009;10.00");
        writer.close();
        Assertions.assertFalse(payment.sendPaymentRequestThroughAPI(validCardNumber)); //T3

    }

    @Test
    void testMultipleCondition(){
        CreditCardPaymentModel payment = new CreditCardPaymentModel();
        Assertions.assertThrows(InvalidCreditCardException.class , ()-> payment.sendPaymentRequestThroughAPI(null));//T4
        Assertions.assertThrows(InvalidCreditCardException.class , ()-> payment.sendPaymentRequestThroughAPI(""));//T5
        Assertions.assertThrows(InvalidCreditCardException.class , ()-> payment.sendPaymentRequestThroughAPI("5100293111053009"));//T6
    }


    @Test
    void testLoopCoverage() throws IOException, InvalidCreditCardException {
        CreditCardPaymentModel payment = new CreditCardPaymentModel();
        payment.setAmount(20.0);
        String validCardNumber = "5100293991053009";
        Assertions.assertFalse(payment.sendPaymentRequestThroughAPI(validCardNumber));// T7
        BufferedWriter writer = new BufferedWriter(new FileWriter("PaymentGateway/cards.txt"));
        writer.write("#Comment");
        writer.close();
        Assertions.assertFalse(payment.sendPaymentRequestThroughAPI(validCardNumber)); // T8
        writer = new BufferedWriter(new FileWriter("PaymentGateway/cards.txt"));
        writer.write("#Comment\n4485370086510891;150.00\n5100293991053009;100.00");
        writer.close();
        Assertions.assertTrue(payment.sendPaymentRequestThroughAPI(validCardNumber)); //T1
    }
}
