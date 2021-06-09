package it.polito.ezshop.internalTests.APITest;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.ProductTypeModel;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

public class RFID_Tests {

    EZShop ez;
    String username = "Paolo";
    String pass = "pass";
    String barcode="6291041500213";
    String RFID = "000000000000";

    public void login() throws InvalidPasswordException, InvalidUsernameException {
        ez.login(username, pass);
    }

    @Before
    public void startup() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidRFIDException, InvalidOrderIdException {
        ez = new EZShop();
        ez.reset();
        ez.createUser(username, pass, "Administrator");
        login();
        Integer id = ez.createProductType("A Test Product", barcode, 0.5, "This is a test Note");
        ez.updatePosition(id, "123-AAA-123");
        ez.recordBalanceUpdate(1000);
        int orderID = ez.issueOrder(barcode, 10, 0.1);
        ez.recordOrderArrivalRFID(orderID, RFID);
        ez.logout();
    }

    @After
    public void cleanup(){
        ez.reset();
    }

    @Test
    public void testOrder() throws InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidOrderIdException, InvalidRFIDException, InvalidLocationException {
        login();
        int ord_id = ez.issueOrder(barcode, 10, 2.5);
        Assert.assertTrue(ez.payOrder(ord_id));
        Assert.assertTrue(ez.recordOrderArrivalRFID(ord_id, "000000001000"));
        Assert.assertThrows(InvalidRFIDException.class, ()->ez.recordOrderArrivalRFID(ord_id, "000000001000"));
    }

    @Test
    public void testSale() throws InvalidQuantityException, UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidOrderIdException, InvalidRFIDException, InvalidLocationException, InvalidPasswordException, InvalidUsernameException, InvalidTransactionIdException {
        login();
        int ord_id = ez.issueOrder(barcode, 10, 2.5);
        Assert.assertTrue(ez.payOrder(ord_id));
        Assert.assertTrue(ez.recordOrderArrivalRFID(ord_id, "000000001000"));
        int sale_id = ez.startSaleTransaction();
        Assert.assertTrue(ez.addProductToSaleRFID(sale_id, "000000001000"));
        Assert.assertTrue(ez.addProductToSaleRFID(sale_id, "000000001001"));
        Assert.assertTrue(ez.addProductToSaleRFID(sale_id, "000000001002"));
        Assert.assertFalse(ez.addProductToSaleRFID(sale_id, "000000001002"));
    }

    @Test
    public void testAddProductToSaleRFID() throws InvalidPasswordException, InvalidUsernameException, InvalidRFIDException, InvalidQuantityException, InvalidTransactionIdException, UnauthorizedException, InvalidProductCodeException {
        login();
        int tId = ez.startSaleTransaction();
        ez.addProductToSaleRFID(tId, RFID);
    }
}
