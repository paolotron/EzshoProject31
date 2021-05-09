package it.polito.ezshop.model;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import it.polito.ezshop.data.ProductType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class JsonWrite {

    BufferedWriter ProductReader;
    BufferedWriter BalanceReader;
    BufferedWriter UserReader;
    public JsonWrite(String ProductTypeFile, String BalanceFile, String UserFile) throws IOException {
        ProductReader = new BufferedWriter(new FileWriter(ProductTypeFile));
        BalanceReader = new BufferedWriter(new FileWriter(BalanceFile));
        UserReader = new BufferedWriter(new FileWriter(UserFile));
    }

    static class ProductSerializer extends JsonSerializer<ProductType>{

        @Override
        public void serialize(ProductType productType, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        }
    }

    void writeProducts(Map<String, ProductTypeModel> ProductMap){

    }


}
