package it.polito.ezshop.internalTests.UnitTest;

import it.polito.ezshop.data.SaleTransaction;
import it.polito.ezshop.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class SaleTransactionModelTest {
    static ArrayList<TicketEntryModel> t = new ArrayList<TicketEntryModel>();

    @BeforeAll
    static void prepare(){
        for(int i = 0; i<10; i++){
            t.add(new TicketEntryModel(new ProductTypeModel(i,"desc", "lol"+String.valueOf(i),10.0,"note" ), 1));
        }
    }

    @Test
    void testComputeCost(){
        Ticket ticket = new Ticket("status", 0.0, t);
        ticket.setAmount(100.0);
        ticket.setPayment(new Payment(100.0, false));
        SaleTransactionModel s = new SaleTransactionModel();
        s.setTicket(ticket);
        Assertions.assertEquals(100, s.computeCost());
    }

    @Test
    void testComputeCostWithDiscount(){
        for(int i = 0; i<10; i++)
            t.get(i).setDiscountRate(0.1);
        Ticket ticket = new Ticket("status", 0.0, t);

        ticket.setAmount(100.0);
        ticket.setPayment(new Payment(100.0, false));
        SaleTransactionModel s = new SaleTransactionModel();
        s.setTicket(ticket);
        Assertions.assertEquals(90, s.computeCost());
    }

}
