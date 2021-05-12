package it.polito.ezshop.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonRead {

    File ProductFile;
    File BalanceFile;
    File UserFile;
    File CustomerFile;
    File OrderFile;

    public JsonRead(String Folder){
        this.ProductFile = new File(Folder+"/product.json");
        this.BalanceFile = new File(Folder+"/balance.json");
        this.UserFile = new File(Folder+"/user.json");
        this.CustomerFile = new File(Folder+"/customer.json");
        this.OrderFile = new File(Folder+"/order.json");
    }

    public BalanceModel parseBalance(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(this.BalanceFile, BalanceModel.class);
        } catch (IOException e) {
            return new BalanceModel();
        }

    }

    public List<ProductTypeModel> parseProductType() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.<ArrayList<ProductTypeModel>>readValue(this.ProductFile, new TypeReference<List<ProductTypeModel>>(){});
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public List<UserModel> parseUsers(){

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(this.UserFile, new TypeReference<List<UserModel>>(){});
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public List<CustomerModel> parseCustomers(){

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(this.CustomerFile, new TypeReference<List<CustomerModel>>(){});
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public List<OrderModel> parseOrders(){

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(this.CustomerFile, new TypeReference<List<CustomerModel>>(){});
        } catch (IOException e) {
            return new ArrayList<>();
        }

    }




}
