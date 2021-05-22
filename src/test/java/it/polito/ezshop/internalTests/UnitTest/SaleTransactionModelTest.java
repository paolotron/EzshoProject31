package it.polito.ezshop.internalTests.UnitTest;

import it.polito.ezshop.model.*;
import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class SaleTransactionModelTest {
    ArrayList<TicketEntryModel> t;
    @Before
    public void prepare(){
        t = new ArrayList<>();
        for(int i = 0; i<10; i++){
            t.add(new TicketEntryModel(new ProductTypeModel(i,"desc", "lol"+ i,10.0,"note" ), 1));
        }
    }

    @Test
    public void testComputeCost(){
        TicketModel ticket = new TicketModel("status", 0.0, t);
        ticket.setAmount(100.0);
        ticket.setPayment(new PaymentModel(100.0, false));
        SaleTransactionModel s = new SaleTransactionModel();
        s.setTicket(ticket);
        assertEquals(100, s.computeCost(), 0.01);
    }

    @Test
    public void testComputeCostWithDiscount(){
        for(int i = 0; i<10; i++)
            t.get(i).setDiscountRate(0.1);
        TicketModel ticket = new TicketModel("status", 0.0, t);

        ticket.setAmount(100.0);
        ticket.setPayment(new PaymentModel(100.0, false));
        SaleTransactionModel s = new SaleTransactionModel();
        s.setTicket(ticket);
        assertEquals(90, s.computeCost(), 0.01);
    }

}
