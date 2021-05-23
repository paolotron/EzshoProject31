package it.polito.ezshop.internalTests.APITest;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.exceptions.*;
import org.junit.After;
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

    @After
    public void tearDown(){
        model.reset();
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

    @Test
    public void CorrectDeleteProductType() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException {
        login();
        Integer id = model.createProductType("Descr", ProductCode, 0.2, "Note");
        assertTrue(id>0);
        assertNotNull(model.getProductTypeByBarCode(ProductCode));
        assertTrue(model.deleteProductType(id));
        assertNull(model.getProductTypeByBarCode(ProductCode));
    }

    @Test
    public void IncorrectDeleteProductType() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException {
        assertThrows(UnauthorizedException.class, ()->model.deleteProductType(1));
        login();
        assertThrows(InvalidProductIdException.class, ()->model.deleteProductType(0));
        assertThrows(InvalidProductIdException.class, ()->model.deleteProductType(-1));
        Integer id = model.createProductType("Descr", ProductCode, 0.2, "Note");
        assertFalse(model.deleteProductType(id+1));
        assertTrue(model.deleteProductType(id));
        assertFalse(model.deleteProductType(id));
    }

    @Test
    public void getProductTypeByBarCodeTest() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException {
        assertThrows(UnauthorizedException.class, ()->model.getProductTypeByBarCode(ProductCode));
        login();
        model.createProductType("Ciao", ProductCode, 0.1, "note");
        assertThrows(InvalidProductCodeException.class, ()->model.getProductTypeByBarCode("CIAO"));
    }

    @Test
    public void getProductTypesByDescriptionTest() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException {
        String newCode = "47845126544844";
        assertThrows(UnauthorizedException.class, ()->model.getProductTypesByDescription(null));
        login();
        Integer id1 = model.createProductType("Una descrizione interessante", ProductCode, 0.1, "Note");
        Integer id2 = model.createProductType("intano", newCode, 0.2, "Note");
        assertEquals(2, model.getProductTypesByDescription(null).size());
        assertEquals(1, model.getProductTypesByDescription("Una").size());
        assertEquals(0, model.getProductTypesByDescription("Ciao").size());
        assertEquals(2, model.getProductTypesByDescription("int").size());
        assertEquals(1, model.getProductTypesByDescription("intano").size());
        assertEquals(id2, model.getProductTypesByDescription("intano").get(0).getId());
        assertEquals(id1, model.getProductTypesByDescription("Una descrizione interessante").get(0).getId());
    }


}
