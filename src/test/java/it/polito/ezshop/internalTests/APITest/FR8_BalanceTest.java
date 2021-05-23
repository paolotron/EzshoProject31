package it.polito.ezshop.internalTests.APITest;


import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.*;

import it.polito.ezshop.model.BalanceModel;
import it.polito.ezshop.model.BalanceOperationModel;
import it.polito.ezshop.model.SaleModel;
import it.polito.ezshop.model.SaleTransactionModel;
import org.junit.Before;
import org.junit.Test;


import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAmount;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;


public class FR8_BalanceTest {
    EZShopInterface model;
    final String username = "Dummy";
    final String password = "Dummy";
    final String c_username = "cname";
    final String c_password = "cname";


    void login() throws InvalidPasswordException, InvalidUsernameException {
        model.login(username, password);
    }

    void unauthorized_login() throws InvalidPasswordException, InvalidUsernameException {
        model.login(c_username, c_password);
    }

    @Before
    public void startEzShop() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        model = new it.polito.ezshop.data.EZShop();
        model.reset();
        model.createUser(username, password, "Administrator");
        model.createUser(c_username, c_password, "Cashier");
        login();
        model.logout();
    }

    @Test
    public void correctComputeBalance() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException {
        login();
        assertTrue(model.recordBalanceUpdate(300.0));
        assertTrue(model.recordBalanceUpdate(-150.0));
        assertEquals(model.computeBalance(), 150.0, 0.01);
        assertFalse(model.recordBalanceUpdate(-200));
    }

    @Test
    public void unauthorizedBalanceUpdate() throws InvalidPasswordException, InvalidUsernameException {
        unauthorized_login();
        assertThrows(UnauthorizedException.class, ()-> model.recordBalanceUpdate(300.0));
    }

    @Test
    public void unauthorizedComputeBalance() throws InvalidPasswordException, InvalidUsernameException {
        unauthorized_login();
        assertThrows(UnauthorizedException.class, ()-> model.computeBalance());
    }


    @Test
    public void correctShowCreditsAndDebits() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException {
        login();
        LocalDate d1 = LocalDate.of(2021, 10, 5);
        LocalDate d2 = LocalDate.of(2021, 10, 10);
        model.recordBalanceUpdate(200);
        model.recordBalanceUpdate(300.0);
        assertEquals(2, model.getCreditsAndDebits(null, null).size());
        List<BalanceOperation> balanceOperationList = model.getCreditsAndDebits(null, null);
        balanceOperationList.get(0).setDate(d1);
        balanceOperationList.get(1).setDate(d2);

        assertEquals(model.getCreditsAndDebits(d1, d2).size(), 2);
        assertEquals(model.getCreditsAndDebits(d1, d2.minus(Period.ofDays(1))).size(), 1);
        assertEquals(model.getCreditsAndDebits(d2.minus(Period.ofDays(1)), d1).size(), 1);
        assertEquals(model.getCreditsAndDebits(d2.minus(Period.ofDays(100)), null).size(), 2);
        assertEquals(model.getCreditsAndDebits(null, d2.minus(Period.ofDays(100))).size(), 0);
        assertEquals(model.getCreditsAndDebits(null, null).size(), 2);
    }

    @Test
    public void unauthorizedShowCreditsAndDebits() throws InvalidPasswordException, InvalidUsernameException {
        unauthorized_login();
        LocalDate d1 = LocalDate.now();
        assertThrows(UnauthorizedException.class, () -> model.getCreditsAndDebits(d1, d1));
    }
}
