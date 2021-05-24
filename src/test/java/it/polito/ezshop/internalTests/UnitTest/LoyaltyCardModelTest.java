package it.polito.ezshop.internalTests.UnitTest;

import it.polito.ezshop.model.LoyaltyCardModel;
import org.junit.Test;

import static org.junit.Assert.*;


public class LoyaltyCardModelTest {
    @Test
    public void addPointsTest(){
        LoyaltyCardModel l = new LoyaltyCardModel(1);
        l.updatePoints(1);
        assertEquals(1, l.getPoints());
        l.updatePoints(Integer.MAX_VALUE);
        assertEquals(1, l.getPoints());

        l = new LoyaltyCardModel(1);
        l.updatePoints(50);
        l.updatePoints(-20);
        assertEquals(30, l.getPoints());
        l.updatePoints(-100);
        assertEquals(30, l.getPoints());

        l.updatePoints(0);
        assertEquals(30, l.getPoints());
    }

    @Test
    public void checkCardTest(){
        assertFalse(LoyaltyCardModel.checkCard(null));
        assertFalse(LoyaltyCardModel.checkCard(""));
        assertFalse(LoyaltyCardModel.checkCard("0123456789a"));
        assertFalse(LoyaltyCardModel.checkCard("012345678"));
        assertFalse(LoyaltyCardModel.checkCard("aaaaaaaaaa"));
        assertFalse(LoyaltyCardModel.checkCard("1a1a1a1a1a"));
        assertTrue(LoyaltyCardModel.checkCard("0123456789"));
    }
}
