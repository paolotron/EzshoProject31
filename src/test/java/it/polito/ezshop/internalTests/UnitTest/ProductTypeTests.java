package it.polito.ezshop.internalTests.UnitTest;

import it.polito.ezshop.model.ProductTypeModel;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class ProductTypeTests {

    @Test
    public void testCorrectBarcodeAlgorithm(){
        assertTrue(ProductTypeModel.checkBarCodeWithAlgorithm("6291041500213"));
        assertTrue(ProductTypeModel.checkBarCodeWithAlgorithm("47845126544844"));
    }

    @Test
    public void testWrongBarcodeAlgorithm(){
        assertFalse(ProductTypeModel.checkBarCodeWithAlgorithm("6291041500211"));
        assertFalse(ProductTypeModel.checkBarCodeWithAlgorithm("1242"));
        assertFalse(ProductTypeModel.checkBarCodeWithAlgorithm(null));
        assertFalse(ProductTypeModel.checkBarCodeWithAlgorithm("ABCD"));
        assertFalse(ProductTypeModel.checkBarCodeWithAlgorithm("!*){"));
    }

    @Test
    public void testUpdateAvailableQuantity(){
        ProductTypeModel p = new ProductTypeModel(1, "desc", "1", 66.6, "note");
        p.setQuantity(100);
        assertFalse(p.updateAvailableQuantity(1));
        p.setLocation("location");
        assertTrue(p.updateAvailableQuantity(-1));
        assertFalse("quantity should be > 0", p.updateAvailableQuantity(-100));
    }

}
