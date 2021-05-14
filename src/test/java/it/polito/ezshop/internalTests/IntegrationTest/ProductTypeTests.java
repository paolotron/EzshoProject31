package it.polito.ezshop.internalTests.IntegrationTest;

import it.polito.ezshop.model.EzShopModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProductTypeTests {

    @Test
    void testBarcodeAlgorithm(){
        Assertions.assertTrue(EzShopModel.checkBarCodeWithAlgorithm("6291041500213"));
        Assertions.assertFalse(EzShopModel.checkBarCodeWithAlgorithm("6291041500211"));
        Assertions.assertFalse(EzShopModel.checkBarCodeWithAlgorithm("1242"));
        Assertions.assertFalse(EzShopModel.checkBarCodeWithAlgorithm(null));
        Assertions.assertTrue(EzShopModel.checkBarCodeWithAlgorithm("62910415002134"));
    }
}
