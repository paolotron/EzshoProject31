package it.polito.ezshop.internalTests;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BalanceTest {
    EZShopInterface model;
    final String username = "Dummy";
    final String password = "Dummy";
    final String s_username = "s_Dummy";
    final String s_password = "s_Dummy";


    void login() throws InvalidPasswordException, InvalidUsernameException {
        model.login(username, password);
    }

    void unauthorized_login() throws InvalidPasswordException, InvalidUsernameException {
        model.login(s_username, s_password);
    }

    @BeforeEach
    void startEzShop() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        model = new it.polito.ezshop.data.EZShop();
        model.reset();
        model.createUser(username, password, "Administrator");
        model.createUser(s_username, s_password, "ShopManager");
        login();
        model.logout();
    }

    @Test
    void correctBalanceCalculus() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException {
        login();
        model.recordBalanceUpdate(300.0);
        model.recordBalanceUpdate(-150.0);
        Assertions.assertEquals(model.computeBalance(), 150.0);
    }

}
