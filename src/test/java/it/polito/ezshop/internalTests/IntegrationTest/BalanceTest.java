package it.polito.ezshop.internalTests.IntegrationTest;


import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.*;

import it.polito.ezshop.model.BalanceModel;
import it.polito.ezshop.model.BalanceOperationModel;
import it.polito.ezshop.model.SaleModel;
import it.polito.ezshop.model.SaleTransactionModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;


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
    void startEzShop() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
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

    /*
    @Test
    void correctShowCreditsAndDebits() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException {
        login();
        model.recordBalanceUpdate(200);
        LocalDate d1 = LocalDate.now();
        model.recordBalanceUpdate(300.0);
        model.recordBalanceUpdate(-150.0);
        LocalDate d2 = LocalDate.now();

        Assertions.assertEquals(model.getCreditsAndDebits(d1, d2).size(), 2);
    } DOES NOT WORK IF YOU DON'T MAKE THE EXECUTION EXACTLY AT MIDNIGHT*/

    @Test
    void unauthorizedShowCreditsAndDebits() throws InvalidPasswordException, InvalidUsernameException {
        unauthorized_login();
        LocalDate d1 = LocalDate.now();
        Assertions.assertThrows(UnauthorizedException.class, () -> model.getCreditsAndDebits(d1, d1));
    }

    @Test
    public void getTransactionByIdTest(){
        BalanceModel b = new BalanceModel();
        BalanceOperationModel credit = new BalanceOperationModel("CREDIT", 50., LocalDate.now());
        BalanceOperationModel debit = new BalanceOperationModel("DEBIT", -50., LocalDate.now());
        SaleTransactionModel sale = new SaleTransactionModel(new SaleModel());

        b.addBalanceOperation(credit);
        b.addBalanceOperation(debit);
        b.addBalanceOperation(sale);

        Optional<BalanceOperationModel> result = b.getTransactionById(credit.getBalanceId());
        Assertions.assertEquals(credit, result.orElse(null));
        result = b.getTransactionById(debit.getBalanceId());
        Assertions.assertEquals(debit, result.orElse(null));
        result = b.getTransactionById(sale.getBalanceId());
        Assertions.assertEquals(sale, result.orElse(null));
        result = b.getTransactionById(100);
        Assertions.assertNull(result.orElse(null));
        result = b.getTransactionById(-10);
        Assertions.assertNull(result.orElse(null));
    }

}