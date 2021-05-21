package it.polito.ezshop.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonRead {

    File ProductFile;
    File BalanceFile;
    File UserFile;
    File CustomerFile;
    File OrderFile;
    File LoyaltyFile;

    public JsonRead(String Folder){
        this.ProductFile = new File(Folder+"/product.json");
        this.BalanceFile = new File(Folder+"/balance.json");
        this.UserFile = new File(Folder+"/user.json");
        this.CustomerFile = new File(Folder+"/customer.json");
        this.OrderFile = new File(Folder+"/order.json");
        this.LoyaltyFile = new File(Folder+"/loyalty.json");
    }

    public BalanceModel parseBalance(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            BalanceModel bal = objectMapper.readValue(this.BalanceFile, BalanceModel.class);
            bal.balanceOperationList.addAll(bal.getSaleTransactionMap().values());
            bal.balanceOperationList.addAll(bal.getOrderTransactionMap().values());
            bal.balanceOperationList.addAll(bal.getReturnTransactionMap().values());
            return bal;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new BalanceModel();

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

    public Map<Integer, Integer> parseLoyalty(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(this.LoyaltyFile, new TypeReference<HashMap<Integer, Integer>>(){});
        } catch (IOException e) {
            return new HashMap<>();
        }
    }




}
