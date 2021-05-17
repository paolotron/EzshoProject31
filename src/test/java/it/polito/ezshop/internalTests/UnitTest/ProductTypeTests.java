package it.polito.ezshop.internalTests.UnitTest;

import it.polito.ezshop.model.ProductTypeModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProductTypeTests {

    @Test
    void testCorrectBarcodeAlgorithm(){
        Assertions.assertTrue(ProductTypeModel.checkBarCodeWithAlgorithm("6291041500213"));
        Assertions.assertTrue(ProductTypeModel.checkBarCodeWithAlgorithm("62910415002134"));
        Assertions.assertTrue(ProductTypeModel.checkBarCodeWithAlgorithm("7482156234585"));
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



}
