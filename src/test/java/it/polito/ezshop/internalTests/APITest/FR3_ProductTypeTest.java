package it.polito.ezshop.internalTests.APITest;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FR3_ProductTypeTest {
    EZShopInterface model;
    String user = "Paolo";
    String pass = "pass";
    String ProductCode = "6291041500213";

    @Before
    public void setup() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
        model = new EZShop();
        model.reset();
        model.createUser(user, pass, "Administrator");
    }

    void login() throws InvalidPasswordException, InvalidUsernameException {
        model.login(user, pass);
    }

    @Test
    public void correctCreateProductType() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException {
        login();
        String note = "A small note";
        String descr = "Test";
        Integer id = model.createProductType(descr, ProductCode ,10., note);
        assertTrue(id > 0);
        ProductType productType = model.getProductTypeByBarCode(ProductCode);
        assertNotNull(productType);
        assertEquals(ProductCode, productType.getBarCode());
        assertEquals(productType.getQuantity(), 0, 0);
        assertEquals(productType.getProductDescription(), descr);
        assertEquals(productType.getNote(), note);
        assertEquals(productType.getPricePerUnit(), 10., 0.01);
    }

    @Test
    public void incorrectCreateProductType() throws InvalidPasswordException, InvalidUsernameException {
        assertThrows(UnauthorizedException.class,()->model.createProductType("Test", ProductCode ,10., "A small note"));
        login();
        assertThrows(InvalidProductDescriptionException.class,()->model.createProductType("", ProductCode ,10., ""));
        assertThrows(InvalidProductDescriptionException.class,()->model.createProductType(null, ProductCode ,10., ""));
        assertThrows(InvalidProductCodeException.class,()->model.createProductType("Test", "0123" ,10., "note"));
        assertThrows(InvalidProductCodeException.class,()->model.createProductType("Test", "" ,10., "note"));
        assertThrows(InvalidProductCodeException.class,()->model.createProductType("Test", null ,10., "note"));
        assertThrows(InvalidPricePerUnitException.class,()->model.createProductType("Test", ProductCode ,-1, "note"));
    }

    @Test
    public void CorrectUpdateProduct() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException {
        login();
        String newDesc = "newDesc";
        String newCode = "47845126544844";
        String newNote = "newNote";
        Integer id = model.createProductType("Test", ProductCode, 10., "A small note");
        assertTrue(model.updateProduct(id, newDesc, newCode, 0.2, newNote));
        ProductType productType = model.getProductTypeByBarCode(newCode);
        assertNotNull(productType);
        assertEquals(newCode, productType.getBarCode());
        assertEquals(productType.getQuantity(), 0, 0);
        assertEquals(productType.getProductDescription(), newDesc);
        assertEquals(productType.getNote(), newNote);
        assertEquals(productType.getPricePerUnit(), 0.2, 0.01);

    }

    @Test
    public void IncorrectUpdateProduct() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException, InvalidProductCodeException {
        String newDesc = "newDesc";
        String newCode = "47845126544844";
        String newNote = "newNote";
        login();
        assertFalse(model.updateProduct(1, newDesc, newCode, 0.2, newNote));
        Integer id = model.createProductType("Test", ProductCode, 10., "A small note");
        model.createProductType("Test", newCode, 10., "A small note");
        assertFalse(model.updateProduct(id, newDesc, newCode, 2., newNote));
        assertThrows(InvalidProductIdException.class,()->model.updateProduct(-1, newDesc, newCode, 0.2, newNote));
        assertThrows(InvalidProductIdException.class,()->model.updateProduct(null, newDesc, newCode, 0.2, newNote));
        assertThrows(InvalidProductDescriptionException.class,()->model.updateProduct(id, "", newCode, 0.2, newNote));
        assertThrows(InvalidProductDescriptionException.class,()->model.updateProduct(id, null, newCode, 0.2, newNote));
        assertThrows(InvalidProductCodeException.class,()->model.updateProduct(id, newDesc, "newCode", 0.2, newNote));
        assertThrows(InvalidProductCodeException.class,()->model.updateProduct(id, newDesc, "", 0.2, newNote));
        assertThrows(InvalidProductCodeException.class,()->model.updateProduct(id, newDesc, null, 0.2, newNote));
        assertThrows(InvalidPricePerUnitException.class,()->model.updateProduct(id, newDesc, newCode, -2, newNote));
        assertThrows(InvalidPricePerUnitException.class,()->model.updateProduct(id, newDesc, newCode, 0, newNote));
        model.logout();
        assertThrows(UnauthorizedException.class,()->model.updateProduct(id, newDesc, newCode, 0.2, newNote));
    }

}
