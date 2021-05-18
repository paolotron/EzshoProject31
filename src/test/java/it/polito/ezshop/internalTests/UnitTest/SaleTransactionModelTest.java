package it.polito.ezshop.internalTests.UnitTest;

import it.polito.ezshop.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class SaleTransactionModelTest {
    ArrayList<TicketEntryModel> t;
    @BeforeEach
    void prepare(){
        t = new ArrayList<TicketEntryModel>();
        for(int i = 0; i<10; i++){
            t.add(new TicketEntryModel(new ProductTypeModel(i,"desc", "lol"+ i,10.0,"note" ), 1));
        }
    }

    @Test
    void testComputeCost(){
        TicketModel ticket = new TicketModel("status", 0.0, t);
        ticket.setAmount(100.0);
        ticket.setPayment(new PaymentModel(100.0, false));
        SaleTransactionModel s = new SaleTransactionModel();
        s.setTicket(ticket);
        Assertions.assertEquals(100, s.computeCost());
    }

    @Test
    void testComputeCostWithDiscount(){
        for(int i = 0; i<10; i++)
            t.get(i).setDiscountRate(0.1);
        TicketModel ticket = new TicketModel("status", 0.0, t);

        ticket.setAmount(100.0);
        ticket.setPayment(new PaymentModel(100.0, false));
        SaleTransactionModel s = new SaleTransactionModel();
        s.setTicket(ticket);
        Assertions.assertEquals(90, s.computeCost());
    }

}
