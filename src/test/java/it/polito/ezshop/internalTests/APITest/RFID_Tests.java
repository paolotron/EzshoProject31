package it.polito.ezshop.internalTests.APITest;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.Order;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.exceptions.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class RFID_Tests {

    EZShop ez;
    String username = "Paolo";
    String pass = "pass";
    String barcode="6291041500213";
    String barcode2="000055555555";
    String RFID = "000000000000";
    String wrongRFID = "000000010000";

    public void login() throws InvalidPasswordException, InvalidUsernameException {
        ez.login(username, pass);
    }

    @Before
    public void startup() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidQuantityException, InvalidRFIDException, InvalidOrderIdException {
        ez = new EZShop();
        ez.reset();
        ez.createUser(username, pass, "Administrator");
        ez.createUser("cashier", "cashier", "Cashier");
        login();
        Integer id = ez.createProductType("A Test Product", barcode, 0.5, "This is a test Note");
        ez.updatePosition(id, "123-AAA-123");
        ez.recordBalanceUpdate(1000);
        int orderID = ez.issueOrder(barcode, 1, 0.1);
        ez.payOrder(orderID);
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
        assertThrows(InvalidRFIDException.class, ()->ez.recordOrderArrivalRFID(ord_id, "000000001000"));
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
    public void testAddProductToSaleRFID() throws InvalidPasswordException, InvalidUsernameException, InvalidRFIDException, InvalidQuantityException, InvalidTransactionIdException, UnauthorizedException, InvalidPaymentException {
        login();
        int tId = ez.startSaleTransaction();
        assertTrue(ez.addProductToSaleRFID(tId, RFID));
        assertFalse(ez.addProductToSaleRFID(tId, RFID));
        assertTrue(ez.endSaleTransaction(tId));
        ez.receiveCashPayment(ez.getSaleTransaction(tId).getTicketNumber(), 0.5);
        int rId = ez.startReturnTransaction(tId);
        ez.returnProductRFID(rId, RFID);
        ez.returnCashPayment(rId);
        int tId2 = ez.startSaleTransaction();
        assertTrue(ez.endReturnTransaction(rId, true));
        assertTrue(ez.addProductToSaleRFID(tId2, RFID));
        assertFalse(ez.addProductToSaleRFID(100, RFID));
        assertFalse(ez.addProductToSaleRFID(tId2, wrongRFID));
        assertThrows(InvalidRFIDException.class ,() -> ez.addProductToSaleRFID(tId2, null));
        assertThrows(InvalidRFIDException.class ,() -> ez.addProductToSaleRFID(tId2, ""));
        assertThrows(InvalidRFIDException.class ,() -> ez.addProductToSaleRFID(tId2, "lollollollol"));
        assertThrows(InvalidRFIDException.class ,() -> ez.addProductToSaleRFID(tId2, "12345"));
        assertThrows(InvalidTransactionIdException.class ,() -> ez.addProductToSaleRFID(-1, RFID));
        assertThrows(InvalidTransactionIdException.class ,() -> ez.addProductToSaleRFID(null, RFID));
    }

    @Test
    public void testDeleteProductFromSaleRFID() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidRFIDException, InvalidQuantityException, InvalidTransactionIdException {
        login();
        int tId = ez.startSaleTransaction();
        ez.addProductToSaleRFID(tId, RFID);
        assertTrue(ez.deleteProductFromSaleRFID(tId, RFID));
        assertFalse(ez.deleteProductFromSaleRFID(tId, RFID));
        assertTrue(ez.addProductToSaleRFID(tId, RFID));
        assertFalse(ez.deleteProductFromSaleRFID(tId, wrongRFID));
        assertFalse(ez.deleteProductFromSaleRFID(100, RFID));
        assertThrows(InvalidRFIDException.class ,() -> ez.deleteProductFromSaleRFID(tId, null));
        assertThrows(InvalidRFIDException.class ,() -> ez.deleteProductFromSaleRFID(tId, ""));
        assertThrows(InvalidRFIDException.class ,() -> ez.deleteProductFromSaleRFID(tId, "lollollollol"));
        assertThrows(InvalidRFIDException.class ,() -> ez.deleteProductFromSaleRFID(tId, "12345"));
        assertThrows(InvalidTransactionIdException.class ,() -> ez.deleteProductFromSaleRFID(-1, RFID));
        assertThrows(InvalidTransactionIdException.class ,() -> ez.deleteProductFromSaleRFID(null, RFID));
    }


    @Test
    public void goodRecordOrderArrivalRFID() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidLocationException, InvalidProductIdException, InvalidOrderIdException, InvalidRFIDException {
        login();
        Integer id = ez.createProductType("A Test Product", barcode2, 2.0, "This is a test Note");
        assertTrue(id > 0);
        Integer orderId = ez.issueOrder(barcode2, 10, 1.0);
        assertTrue(orderId > 0);
        assertTrue(ez.updatePosition(id, "123-BBA-123"));
        assertTrue(ez.payOrder(orderId));

        List<Order> ordList = ez.getAllOrders();
        for (Order order : ordList) {
            if (order.getOrderId().equals(orderId)) {
                assertEquals("PAYED", order.getStatus());
                break;
            }
        }

        ProductType p = ez.getProductTypeByBarCode(barcode2);
        assertNotNull(p);

        assertEquals(0, p.getQuantity(), 0);
        assertTrue(ez.recordOrderArrivalRFID(orderId, "000000000010"));
        assertEquals(10, p.getQuantity(), 0);

        for (Order order : ordList) {
            if (order.getOrderId().equals(orderId)) {
                assertEquals("COMPLETED", order.getStatus());
                break;
            }
        }

        assertFalse(ez.recordOrderArrivalRFID(orderId, "000000000020"));

    }

    @Test
    public void badRecordOrderArrivalRFID() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException, InvalidRFIDException, InvalidLocationException, InvalidOrderIdException, InvalidProductIdException {
        assertThrows(UnauthorizedException.class, ()-> ez.recordOrderArrivalRFID(1, "000000000010"));
        ez.login("cashier", "cashier");
        assertThrows(UnauthorizedException.class, ()-> ez.recordOrderArrivalRFID(1, "000000000010"));
        ez.logout();
        login();
        Integer id = ez.createProductType("A Test Product", barcode2, 2.0, "This is a test Note");
        assertTrue(id>0);
        Integer orderId = ez.issueOrder(barcode2, 10, 1.0);
        assertTrue(orderId>0);
        assertThrows(InvalidLocationException.class, ()->ez.recordOrderArrivalRFID(orderId, "000000000010"));
        assertTrue(ez.updatePosition(id,"123-BBA-123" ));
        assertTrue(ez.payOrder(orderId));
        assertThrows(InvalidRFIDException.class, ()-> ez.recordOrderArrivalRFID(orderId, RFID));
        assertThrows(InvalidRFIDException.class, ()-> ez.recordOrderArrivalRFID(orderId, "012345"));
        assertThrows(InvalidRFIDException.class, ()-> ez.recordOrderArrivalRFID(orderId, "a1b2c3d4e5"));
        assertThrows(InvalidOrderIdException.class, ()-> ez.recordOrderArrivalRFID(0, "000000000010"));
        assertThrows(InvalidOrderIdException.class, ()-> ez.recordOrderArrivalRFID(-1, "000000000010"));
        assertThrows(InvalidOrderIdException.class, ()-> ez.recordOrderArrivalRFID(null, "000000000010"));
        assertFalse(ez.recordOrderArrivalRFID(40, "000000002000"));

    }

    @Test
    public void badReturnProductRFID() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidRFIDException, InvalidQuantityException, InvalidTransactionIdException, InvalidPaymentException, InvalidProductCodeException {
        assertThrows(UnauthorizedException.class, ()->ez.returnProductRFID(1, RFID));
        login();
        int id = ez.startSaleTransaction();
        ProductType p = ez.getProductTypeByBarCode(barcode);
        assertNotNull(p);
        assertEquals(1, p.getQuantity(), 0);
        assertTrue(ez.addProductToSaleRFID(id, RFID));
        assertTrue(ez.endSaleTransaction(id));
        assertTrue(ez.receiveCashPayment(id, 100) > 0);
        assertEquals(0, p.getQuantity(), 0);

        Integer rId = ez.startReturnTransaction(id);
        assertTrue(rId > 0);

        assertThrows(InvalidTransactionIdException.class, ()->ez.returnProductRFID(-1, RFID));
        assertThrows(InvalidTransactionIdException.class, ()->ez.returnProductRFID(0, RFID));
        assertThrows(InvalidTransactionIdException.class, ()->ez.returnProductRFID(null, RFID));
        assertThrows(InvalidRFIDException.class, ()->ez.returnProductRFID(id, ""));
        assertThrows(InvalidRFIDException.class, ()->ez.returnProductRFID(id, null));
        assertThrows(InvalidRFIDException.class, ()->ez.returnProductRFID(id, "A1B2C3D4E5"));

        assertFalse(ez.returnProductRFID(id, "000010000050"));
        assertFalse(ez.returnProductRFID(id, "000000000001"));
        assertFalse(ez.returnProductRFID(100, "000000000000"));

        assertTrue(ez.returnProductRFID(id, RFID));
        assertEquals(0, p.getQuantity(), 0);
        ez.endReturnTransaction(rId, true);
        assertEquals(1, p.getQuantity(), 0);
    }

    @Test
    public void goodReturnProductRFID() throws InvalidRFIDException, InvalidQuantityException, InvalidTransactionIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidOrderIdException, InvalidPaymentException {
        ez.logout();
        assertThrows(UnauthorizedException.class, ()-> ez.returnProductRFID(10, RFID));
        login();
        int ord_id = ez.issueOrder(barcode, 10, 2.5);
        Assert.assertTrue(ez.payOrder(ord_id));
        Assert.assertTrue(ez.recordOrderArrivalRFID(ord_id, "000000001000"));
        int sale_id = ez.startSaleTransaction();
        Assert.assertTrue(ez.addProductToSaleRFID(sale_id, "000000001000"));
        Assert.assertTrue(ez.addProductToSaleRFID(sale_id, "000000001001"));
        Assert.assertTrue(ez.addProductToSaleRFID(sale_id, "000000001002"));
        Assert.assertTrue(ez.addProductToSaleRFID(sale_id, "000000001009"));
        Assert.assertTrue(ez.endSaleTransaction(sale_id));
        ez.receiveCashPayment(sale_id,7.5);

        int returnid= ez.startReturnTransaction(sale_id);
        assertTrue(returnid>0);

        Assert.assertTrue(ez.returnProductRFID(returnid, "000000001000"));
        Assert.assertTrue(ez.returnProductRFID(returnid, "000000001001"));
        Assert.assertTrue(ez.returnProductRFID(returnid, "000000001002"));
        Assert.assertTrue(ez.returnProductRFID(returnid, "000000001009"));

        Assert.assertTrue(ez.endReturnTransaction(returnid,true));

    }


}
