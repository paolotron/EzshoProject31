package it.polito.ezshop.internalTests.APITest;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class FR6_SaleTransactionTest {
    EZShopInterface model;
    final String username = "Dummy";
    final String password = "Dummy";
    final String c_username = "cname";
    final String c_password = "cname";
    final String f_username = "fname";
    final String f_password = "fname";
    Integer productId;
    String productCode = "6291041500213";
    String productCode2 = "47845126544844";


    void login() throws InvalidPasswordException, InvalidUsernameException {
        model.login(username, password);
    }

    void unauthorized_login() throws InvalidPasswordException, InvalidUsernameException {
        model.login(f_username, f_password);
    }

    @Before
    public void startEzShop() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidProductIdException, InvalidLocationException {
        model = new it.polito.ezshop.data.EZShop();
        model.reset();
        model.createUser(username, password, "Administrator");
        model.createUser(c_username, c_password, "Cashier");
        login();
        productId = model.createProductType("test product", productCode, 10, "");
        model.updatePosition(productId, "23-ABC-2");
        model.updateQuantity(productId, 3);
        login();
        model.logout();
    }

    @Test
    public void correctStartSale() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException {
        login();
        assertTrue(model.startSaleTransaction() > 0);
    }

    @Test
    public void unauthorizedStartSale() throws InvalidPasswordException, InvalidUsernameException {
        unauthorized_login();
        assertThrows(UnauthorizedException.class, ()->model.startSaleTransaction());
    }

    @Test
    public void badAddProductToSale() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException {
        assertThrows(UnauthorizedException.class, ()->model.addProductToSale(1, productCode, 1));
        login();
        int id = model.startSaleTransaction();
        assertThrows(InvalidTransactionIdException.class, ()->model.addProductToSale(-1, "1", 1));
        assertThrows(InvalidTransactionIdException.class, ()->model.addProductToSale(0, "1", 1));
        assertThrows(InvalidTransactionIdException.class, ()->model.addProductToSale(null, "1", 1));
        assertThrows(InvalidProductCodeException.class, ()->model.addProductToSale(id, "", 1));
        assertThrows(InvalidProductCodeException.class, ()->model.addProductToSale(id, null, 1));
        assertThrows(InvalidProductCodeException.class, ()->model.addProductToSale(id, "01", 1));
        assertThrows(InvalidQuantityException.class, ()->model.addProductToSale(id, productCode, -1));

        assertFalse(model.addProductToSale(id, productCode2, 1));
        assertFalse(model.addProductToSale(id, productCode, 10));
        assertFalse(model.addProductToSale(23, productCode, 10));
    }

    @Test
    public void goodAddProductToSale() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException {
        login();
        int id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id,productCode, 1));
    }

    @Test
    public void badDeleteProductFromSale() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException {
        assertThrows(UnauthorizedException.class, ()->model.deleteProductFromSale(1, productCode, 1));
        login();
        int id = model.startSaleTransaction();
        assertThrows(InvalidTransactionIdException.class, ()->model.deleteProductFromSale(-1, "1", 1));
        assertThrows(InvalidTransactionIdException.class, ()->model.deleteProductFromSale(0, "1", 1));
        assertThrows(InvalidTransactionIdException.class, ()->model.deleteProductFromSale(null, "1", 1));
        assertThrows(InvalidProductCodeException.class, ()->model.deleteProductFromSale(id, "", 1));
        assertThrows(InvalidProductCodeException.class, ()->model.deleteProductFromSale(id, null, 1));
        assertThrows(InvalidProductCodeException.class, ()->model.deleteProductFromSale(id, "01", 1));
        assertThrows(InvalidQuantityException.class, ()->model.deleteProductFromSale(id, productCode, -1));

        assertFalse(model.deleteProductFromSale(id, productCode2, 1));
        assertTrue(model.addProductToSale(id,productCode, 1));
        assertFalse(model.deleteProductFromSale(id,productCode, 5));
        assertFalse(model.deleteProductFromSale(233,productCode, 1));
    }

    @Test
    public void goodDeleteProductFromSale() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException {
        login();
        int id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id,productCode, 2));
        assertTrue(model.deleteProductFromSale(id,productCode, 1));
        assertTrue(model.endSaleTransaction(id));
        assertEquals(1, model.getSaleTransaction(id).getEntries().size());
        id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id,productCode, 2));
        assertTrue(model.deleteProductFromSale(id,productCode, 2));
        assertTrue(model.endSaleTransaction(id));
        assertEquals(0, model.getSaleTransaction(id).getEntries().size());
    }

    @Test
    public void badApplyDiscountRateToProduct() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException {
        assertThrows(UnauthorizedException.class, ()->model.applyDiscountRateToProduct(1, productCode, 1));
        login();
        int id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id,productCode, 2));
        assertThrows(InvalidTransactionIdException.class, ()->model.applyDiscountRateToProduct(-1, productCode, 1));
        assertThrows(InvalidTransactionIdException.class, ()->model.applyDiscountRateToProduct(0, productCode, 1));
        assertThrows(InvalidTransactionIdException.class, ()->model.applyDiscountRateToProduct(null, productCode, 1));
        assertThrows(InvalidProductCodeException.class, ()->model.applyDiscountRateToProduct(id, "", 1));
        assertThrows(InvalidProductCodeException.class, ()->model.applyDiscountRateToProduct(id, null, 1));
        assertThrows(InvalidProductCodeException.class, ()->model.applyDiscountRateToProduct(id, "01", 1));
        assertThrows(InvalidDiscountRateException.class, ()->model.applyDiscountRateToProduct(id, productCode, 1.0));
        assertThrows(InvalidDiscountRateException.class, ()->model.applyDiscountRateToProduct(id, productCode, 1.1));
        assertThrows(InvalidDiscountRateException.class, ()->model.applyDiscountRateToProduct(id, productCode, -0.1));

        assertFalse(model.applyDiscountRateToProduct(id, productCode2, 0.1));
        assertFalse(model.applyDiscountRateToProduct(23,productCode, 0.1));
    }

    @Test
    public void goodApplyDiscountRateToProduct() throws InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, InvalidTransactionIdException, UnauthorizedException, InvalidProductCodeException, InvalidDiscountRateException {
        login();
        int id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id,productCode, 2));
        assertTrue(model.applyDiscountRateToProduct(id, productCode, 0.3));
    }

    @Test
    public void badApplyDiscountRateToSale() throws UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException, InvalidDiscountRateException {
        assertThrows(UnauthorizedException.class, ()->model.applyDiscountRateToSale(1, 0.1));
        login();
        int id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id,productCode, 2));
        assertThrows(InvalidTransactionIdException.class, ()->model.applyDiscountRateToSale(-1, 0.1));
        assertThrows(InvalidTransactionIdException.class, ()->model.applyDiscountRateToSale(0, 0.1));
        assertThrows(InvalidTransactionIdException.class, ()->model.applyDiscountRateToSale(null, 0.1));
        assertThrows(InvalidDiscountRateException.class, ()->model.applyDiscountRateToSale(id, 1.));
        assertThrows(InvalidDiscountRateException.class, ()->model.applyDiscountRateToSale(id, 1.1));
        assertThrows(InvalidDiscountRateException.class, ()->model.applyDiscountRateToSale(id, -0.1));

        assertFalse(model.applyDiscountRateToSale(123, 0.1));
    }

    @Test
    public void goodApplyDiscountRateToSale() throws InvalidQuantityException, InvalidTransactionIdException, UnauthorizedException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException, InvalidDiscountRateException {
        login();
        int id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id,productCode, 2));
        assertTrue(model.applyDiscountRateToSale(id, 0.1));
    }

    @Test
    public void badComputePointsForSale() throws InvalidQuantityException, InvalidTransactionIdException, UnauthorizedException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException {
        assertThrows(UnauthorizedException.class, ()->model.computePointsForSale(1));
        login();
        int id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id,productCode, 2));
        assertThrows(InvalidTransactionIdException.class, ()->model.computePointsForSale(-1));
        assertThrows(InvalidTransactionIdException.class, ()->model.computePointsForSale(0));
        assertThrows(InvalidTransactionIdException.class, ()->model.computePointsForSale(null));

        assertEquals(-1, model.computePointsForSale(23), 0);
    }

    @Test
    public void goodComputePointsForSale() throws InvalidQuantityException, InvalidTransactionIdException, UnauthorizedException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException, InvalidDiscountRateException {
        login();
        int id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id,productCode, 2));
        assertEquals(2, model.computePointsForSale(id));
        model.applyDiscountRateToSale(id, 0.2);
        assertEquals(1, model.computePointsForSale(id));
    }

    @Test
    public void badEndSaleTransaction() throws InvalidQuantityException, InvalidTransactionIdException, UnauthorizedException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException {
        assertThrows(UnauthorizedException.class, ()->model.endSaleTransaction(1));
        login();
        int id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id,productCode, 2));
        assertThrows(InvalidTransactionIdException.class, ()->model.endSaleTransaction(-1));
        assertThrows(InvalidTransactionIdException.class, ()->model.endSaleTransaction(0));
        assertThrows(InvalidTransactionIdException.class, ()->model.endSaleTransaction(null));

        assertFalse(model.endSaleTransaction(23));
        model.endSaleTransaction(id);
        assertFalse(model.endSaleTransaction(1));
    }

    @Test
    public void goodEndSaleTransaction() throws InvalidQuantityException, InvalidTransactionIdException, UnauthorizedException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException {
        login();
        int id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id,productCode, 2));
        assertTrue(model.endSaleTransaction(id));
    }

    @Test
    public void badDeleteSaleTransaction() throws UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException, InvalidPaymentException {
        assertThrows(UnauthorizedException.class, ()->model.deleteSaleTransaction(1));
        login();
        int id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id,productCode, 2));
        assertThrows(InvalidTransactionIdException.class, ()->model.deleteSaleTransaction(-1));
        assertThrows(InvalidTransactionIdException.class, ()->model.deleteSaleTransaction(0));
        assertThrows(InvalidTransactionIdException.class, ()->model.deleteSaleTransaction(null));

        assertFalse(model.deleteSaleTransaction(23));
        model.endSaleTransaction(id);
        model.receiveCashPayment(id, 500);
        assertFalse(model.deleteSaleTransaction(1));
    }

    @Test
    public void goodDeleteSaleTransaction() throws InvalidQuantityException, InvalidTransactionIdException, UnauthorizedException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException {
        login();
        int id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id,productCode, 2));
        assertTrue(model.endSaleTransaction(id));
        assertTrue(model.deleteSaleTransaction(id));
    }

    @Test
    public void badGetSaleTransaction() throws InvalidQuantityException, InvalidTransactionIdException, UnauthorizedException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException {
        assertThrows(UnauthorizedException.class, ()->model.getSaleTransaction(1));
        login();
        int id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id,productCode, 2));
        assertThrows(InvalidTransactionIdException.class, ()->model.getSaleTransaction(-1));
        assertThrows(InvalidTransactionIdException.class, ()->model.getSaleTransaction(0));
        assertThrows(InvalidTransactionIdException.class, ()->model.getSaleTransaction(null));

        assertNull(model.getSaleTransaction(id));
        assertNull(model.getSaleTransaction(23));
    }

    @Test
    public void goodGetSaleTransaction() throws InvalidQuantityException, InvalidTransactionIdException, UnauthorizedException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException {
        login();
        int id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id,productCode, 2));
        model.endSaleTransaction(id);
        assertNotNull(model.getSaleTransaction(id));
    }

    @Test
    public void badStartReturnTransaction() throws InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, InvalidTransactionIdException, UnauthorizedException, InvalidProductCodeException {
        assertThrows(UnauthorizedException.class, ()->model.startReturnTransaction(1));
        login();
        int id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id,productCode, 2));
        model.endSaleTransaction(id);

        assertThrows(InvalidTransactionIdException.class, ()->model.startReturnTransaction(-1));
        assertThrows(InvalidTransactionIdException.class, ()->model.startReturnTransaction(0));
        assertThrows(InvalidTransactionIdException.class, ()->model.startReturnTransaction(null));

        assertFalse(model.startReturnTransaction(23) > 0);
    }

    @Test
    public void goodStartReturnTransaction() throws InvalidQuantityException, InvalidTransactionIdException, UnauthorizedException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException, InvalidPaymentException {
        login();
        int id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id,productCode, 2));
        model.endSaleTransaction(id);
        model.receiveCashPayment(id, 100);
        assertTrue(model.startReturnTransaction(id) > 0);
    }

    @Test
    public void badReturnProduct() throws InvalidQuantityException, InvalidTransactionIdException, UnauthorizedException, InvalidProductCodeException, InvalidPaymentException, InvalidPasswordException, InvalidUsernameException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidLocationException, InvalidProductIdException {
        assertThrows(UnauthorizedException.class, ()->model.returnProduct(1, productCode, 1));
        login();
        int id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id,productCode, 2));
        model.endSaleTransaction(id);
        model.receiveCashPayment(id, 100);
        assertTrue(model.startReturnTransaction(id) > 0);

        assertThrows(InvalidTransactionIdException.class, ()->model.returnProduct(-1, productCode, 1));
        assertThrows(InvalidTransactionIdException.class, ()->model.returnProduct(0, productCode, 1));
        assertThrows(InvalidTransactionIdException.class, ()->model.returnProduct(null, productCode, 1));
        assertThrows(InvalidProductCodeException.class, ()->model.returnProduct(id, "", 1));
        assertThrows(InvalidProductCodeException.class, ()->model.returnProduct(id, null, 1));
        assertThrows(InvalidProductCodeException.class, ()->model.returnProduct(id, "10", 1));
        assertThrows(InvalidQuantityException.class, ()->model.returnProduct(id, productCode, 0));
        assertThrows(InvalidQuantityException.class, ()->model.returnProduct(id, productCode, -1));
        
        assertFalse(model.returnProduct(id, productCode2, 1));
        Integer pId = model.createProductType("test product", productCode2, 10, "");
        model.updatePosition(pId, "23-ABC-2");
        model.updateQuantity(pId, 3);
        assertFalse(model.returnProduct(id, productCode2, 1));
        assertFalse(model.returnProduct(id, productCode, 10));
        assertFalse(model.returnProduct(23, productCode, 1));


    }

    @Test
    public void goodReturnProduct() throws InvalidQuantityException, InvalidTransactionIdException, UnauthorizedException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException, InvalidPaymentException {
        login();
        int id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id,productCode, 2));
        model.endSaleTransaction(id);
        model.receiveCashPayment(id, 100);
        Integer returnId = model.startReturnTransaction(id);
        assertTrue(returnId > 0);
        assertTrue(model.returnProduct(returnId, productCode, 1));
        assertTrue(model.returnProduct(returnId, productCode, 1));
    }

    @Test
    public void badEndReturnTransaction() throws InvalidQuantityException, InvalidTransactionIdException, UnauthorizedException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException, InvalidPaymentException {
        assertThrows(UnauthorizedException.class, ()->model.endReturnTransaction(1, true));
        login();
        int id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id,productCode, 2));
        model.endSaleTransaction(id);
        model.receiveCashPayment(id, 100);
        Integer returnId = model.startReturnTransaction(id);
        assertTrue(returnId > 0);
        assertTrue(model.returnProduct(returnId, productCode, 1));

        assertThrows(InvalidTransactionIdException.class, ()->model.endReturnTransaction(-1, true));
        assertThrows(InvalidTransactionIdException.class, ()->model.endReturnTransaction(0, true));
        assertThrows(InvalidTransactionIdException.class, ()->model.endReturnTransaction(null, true));

        assertFalse(model.endReturnTransaction(23, true));
    }
//TODO: Question= Can be a return ended if there is no product returned?
    @Test
    public void goodEndReturnTransactionWithCommit() throws InvalidQuantityException, InvalidTransactionIdException, UnauthorizedException, InvalidProductCodeException, InvalidPaymentException, InvalidPasswordException, InvalidUsernameException {
        login();
        int id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id,productCode, 2));
        model.endSaleTransaction(id);
        model.receiveCashPayment(id, 100);
        Integer returnId = model.startReturnTransaction(id);
        assertTrue(returnId > 0);
        assertTrue(model.returnProduct(returnId, productCode, 2));

        assertEquals(20, model.computeBalance(), 0);
        assertEquals(1, model.getSaleTransaction(id).getEntries().size());
        assertTrue(model.endReturnTransaction(returnId, true));
        assertEquals(0, model.computeBalance(), 0);
        //assertEquals(0, model.getSaleTransaction(id).getEntries().size());

        id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id,productCode, 2));
        model.endSaleTransaction(id);
        model.receiveCashPayment(id, 100);
        returnId = model.startReturnTransaction(id);
        assertTrue(returnId > 0);
        assertTrue(model.returnProduct(returnId, productCode, 1));
        assertEquals(20, model.computeBalance(), 0);
        assertEquals(1, model.getSaleTransaction(id).getEntries().size());
        assertTrue(model.endReturnTransaction(returnId, true));
        assertEquals(10, model.computeBalance(), 0);
        assertEquals(1, model.getSaleTransaction(id).getEntries().size());

    }

    @Test
    public void goodEndSaleTransactionWithoutCommit() throws InvalidQuantityException, InvalidTransactionIdException, UnauthorizedException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException, InvalidPaymentException {
        login();
        int id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id,productCode, 2));
        model.endSaleTransaction(id);
        model.receiveCashPayment(id, 100);
        Integer returnId = model.startReturnTransaction(id);
        assertTrue(returnId > 0);
        assertTrue(model.returnProduct(returnId, productCode, 2));

        assertEquals(20, model.computeBalance(), 0);
        assertEquals(1, model.getSaleTransaction(id).getEntries().size());
        assertTrue(model.endReturnTransaction(returnId, false));
        assertEquals(20, model.computeBalance(), 0);
        assertEquals(1, model.getSaleTransaction(id).getEntries().size());

        assertFalse(model.endReturnTransaction(returnId, true));
    }

    @Test
    public void badDeleteReturnTransaction() throws InvalidQuantityException, InvalidTransactionIdException, UnauthorizedException, InvalidProductCodeException, InvalidPaymentException, InvalidPasswordException, InvalidUsernameException {
        assertThrows(UnauthorizedException.class, ()->model.deleteReturnTransaction(1));
        login();
        int id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id,productCode, 2));
        model.endSaleTransaction(id);
        model.receiveCashPayment(id, 100);
        Integer returnId = model.startReturnTransaction(id);
        assertTrue(returnId > 0);
        assertTrue(model.returnProduct(returnId, productCode, 1));
        assertTrue(model.endReturnTransaction(returnId, true));

        assertThrows(InvalidTransactionIdException.class, ()->model.deleteReturnTransaction(-1));
        assertThrows(InvalidTransactionIdException.class, ()->model.deleteReturnTransaction(0));
        assertThrows(InvalidTransactionIdException.class, ()->model.deleteReturnTransaction(null));

        assertFalse(model.deleteReturnTransaction(23));
        model.returnCashPayment(returnId);
        assertFalse(model.deleteReturnTransaction(returnId));
    }

    @Test
    public void goodDeleteReturnTransaction() throws InvalidQuantityException, InvalidTransactionIdException, UnauthorizedException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException, InvalidPaymentException {
        login();
        int id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id,productCode, 2));
        model.endSaleTransaction(id);
        model.receiveCashPayment(id, 100);
        Integer returnId = model.startReturnTransaction(id);
        assertTrue(returnId > 0);
        assertTrue(model.returnProduct(returnId, productCode, 1));

        assertEquals(20, model.computeBalance(), 0);
        assertEquals(1, model.getSaleTransaction(id).getEntries().size());
        assertTrue(model.endReturnTransaction(returnId, true));
        assertEquals(10, model.computeBalance(), 0);
        assertEquals(1, model.getSaleTransaction(id).getEntries().size());

        assertTrue(model.deleteReturnTransaction(returnId));
        assertEquals(20, model.computeBalance(), 0);
        assertEquals(1, model.getSaleTransaction(id).getEntries().size());


        id = model.startSaleTransaction();
        assertTrue(model.addProductToSale(id,productCode, 2));
        model.endSaleTransaction(id);
        model.receiveCashPayment(id, 100);
        returnId = model.startReturnTransaction(id);
        assertTrue(returnId > 0);
        assertTrue(model.returnProduct(returnId, productCode, 2));

        assertEquals(40, model.computeBalance(), 0);
        assertEquals(1, model.getSaleTransaction(id).getEntries().size());
        assertTrue(model.endReturnTransaction(returnId, true));
        assertEquals(20, model.computeBalance(), 0);
        //assertEquals(0, model.getSaleTransaction(id).getEntries().size());

        assertTrue(model.deleteReturnTransaction(returnId));
        assertEquals(40, model.computeBalance(), 0);
        assertEquals(1, model.getSaleTransaction(id).getEntries().size());
    }
}
