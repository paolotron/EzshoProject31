package it.polito.ezshop.internalTests.UnitTest;

import it.polito.ezshop.model.ProductTypeModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProductTypeTests {

    @Test
    void testCorrectBarcodeAlgorithm(){
        Assertions.assertTrue(ProductTypeModel.checkBarCodeWithAlgorithm("6291041500213"));
        Assertions.assertTrue(ProductTypeModel.checkBarCodeWithAlgorithm("47845126544844"));
        Assertions.assertTrue(ProductTypeModel.checkBarCodeWithAlgorithm("989661725630"));
    }

    @Test
    void testWrongBarcodeAlgorithm(){
        Assertions.assertFalse(ProductTypeModel.checkBarCodeWithAlgorithm("6291041500211"));
        Assertions.assertFalse(ProductTypeModel.checkBarCodeWithAlgorithm("1242"));
        Assertions.assertFalse(ProductTypeModel.checkBarCodeWithAlgorithm(null));
        Assertions.assertFalse(ProductTypeModel.checkBarCodeWithAlgorithm("ABCD"));
        Assertions.assertFalse(ProductTypeModel.checkBarCodeWithAlgorithm("!*){"));
    }

    @Test
    void testUpdateAvailableQuantity(){
        ProductTypeModel p = new ProductTypeModel(1, "desc", "1", 66.6, "note");
        p.setQuantity(100);
        Assertions.assertFalse(p.updateAvailableQuantity(1));
        p.setLocation("location");
        Assertions.assertTrue(p.updateAvailableQuantity(-1));
        Assertions.assertFalse(p.updateAvailableQuantity(-100), "quantity should be > 0");
    }

}
