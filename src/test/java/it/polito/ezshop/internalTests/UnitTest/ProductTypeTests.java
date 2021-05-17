package it.polito.ezshop.internalTests.UnitTest;

import it.polito.ezshop.model.EzShopModel;
import it.polito.ezshop.model.ProductTypeModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProductTypeTests {

    @Test
    void testBarcodeAlgorithm(){
        Assertions.assertTrue(ProductTypeModel.checkBarCodeWithAlgorithm("6291041500213"));
        Assertions.assertFalse(ProductTypeModel.checkBarCodeWithAlgorithm("6291041500211"));
        Assertions.assertFalse(ProductTypeModel.checkBarCodeWithAlgorithm("1242"));
        Assertions.assertFalse(ProductTypeModel.checkBarCodeWithAlgorithm(null));
        Assertions.assertTrue(ProductTypeModel.checkBarCodeWithAlgorithm("62910415002134"));
    }
}
