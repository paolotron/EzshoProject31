package it.polito.ezshop.internalTests;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class BalanceTest {
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

    @BeforeEach
    void startEzShop() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        model = new it.polito.ezshop.data.EZShop();
        model.reset();
        model.createUser(username, password, "Administrator");
        model.createUser(c_username, c_password, "Cashier");
        login();
        model.logout();
    }

    @Test
    void correctComputeBalance() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException {
        login();
        model.recordBalanceUpdate(300.0);
        model.recordBalanceUpdate(-150.0);
        Assertions.assertEquals(model.computeBalance(), 150.0);
    }

    @Test
    void unauthorizedBalanceUpdate() throws InvalidPasswordException, InvalidUsernameException {
        unauthorized_login();
        Assertions.assertThrows(UnauthorizedException.class, ()-> model.recordBalanceUpdate(300.0));
    }

    @Test
    void unauthorizedComputeBalance() throws InvalidPasswordException, InvalidUsernameException {
        unauthorized_login();
        Assertions.assertThrows(UnauthorizedException.class, ()-> model.computeBalance());
    }

}
