package it.polito.ezshop.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonRead {

    BufferedReader ProductReader;
    BufferedReader BalanceReader;
    BufferedReader UserReader;
    public JsonRead(String ProductTypeFile, String BalanceFile, String UserFile) throws FileNotFoundException {
        ProductReader = new BufferedReader(new FileReader(ProductTypeFile));
        BalanceReader = new BufferedReader(new FileReader(BalanceFile));
        UserReader = new BufferedReader(new FileReader(UserFile));
    }
    public JsonRead(){}

    public void test(){
        String json = "";
        Map<String, String> result = new HashMap<>();
        Map<String, String> map = new HashMap<>();
        map.put("Ciao", "Miao");
        map.put("Lap", "Tres");
        map.put("Full", "Dos");
        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.writeValueAsString(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(json);
        try {
             result = mapper.readValue(json, new TypeReference<Map<String, String>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        result.forEach((key, value)->System.out.println(key+" :"+value));
    }

    public void parseBalance(){

    }

    public void parseProductType(){

    }


}
