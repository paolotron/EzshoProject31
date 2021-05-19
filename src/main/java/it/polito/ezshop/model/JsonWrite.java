package it.polito.ezshop.model;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonWrite {


    BufferedWriter ProductWriter;
    BufferedWriter BalanceWriter;
    BufferedWriter UserWriter;
    BufferedWriter CustomerWriter;
    BufferedWriter OrderWriter;
    BufferedWriter LoyaltyWriter;
    String ProductTypeFile;
    String BalanceFile;
    String UserFile;
    String CustomerFile;
    String OrderFile;
    String LoyaltyFile;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public JsonWrite(String Folder) throws IOException {
        this.ProductTypeFile = Folder+"/product.json";
        this.BalanceFile = Folder+"/balance.json";
        this.UserFile = Folder+"/user.json";
        this.CustomerFile = Folder+"/customer.json";
        this.OrderFile = Folder+"/order.json";
        this.LoyaltyFile = Folder+"/loyalty.json";

        if(!new File(UserFile).exists())
            new File(UserFile).createNewFile();
        if(!new File(BalanceFile).exists())
            new File(BalanceFile).createNewFile();
        if(!new File(ProductTypeFile).createNewFile())
            new File(ProductTypeFile).createNewFile();
        if(!new File(CustomerFile).createNewFile())
            new File(CustomerFile).createNewFile();
        if(!new File(OrderFile).createNewFile())
            new File(OrderFile).createNewFile();
    }

    public JsonWrite(){}

    public boolean reset(){
        try {
            new BufferedWriter(new FileWriter(UserFile)).close();
            new BufferedWriter(new FileWriter(ProductTypeFile)).close();
            new BufferedWriter(new FileWriter(BalanceFile)).close();
            new BufferedWriter(new FileWriter(CustomerFile)).close();
            new BufferedWriter(new FileWriter(OrderFile)).close();
            new BufferedWriter(new FileWriter(LoyaltyFile)).close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean writeProducts(Map<String, ProductTypeModel> ProductMap){ return writeProducts(new ArrayList<>(ProductMap.values())); }
    public boolean writeOrders(Map<Integer, OrderModel> OrderMap){ return writeOrders(new ArrayList<>(OrderMap.values())); }
    public boolean writeUsers(Map<String, UserModel> UserMap){ return writeUsers(new ArrayList<>(UserMap.values())); }
    public boolean writeCustomers(Map<String, CustomerModel> CustomerMap){ return writeCustomers(new ArrayList<>(CustomerMap.values())); }
    public boolean writeLoyaltyCards(Map<Integer, LoyaltyCardModel> LoyaltyMap){ return writeLoyaltyCards(new ArrayList<>(LoyaltyMap.values())); }

    public boolean writeProducts(List<ProductTypeModel> ProductList){
        ObjectMapper mapper = new ObjectMapper();
        try {
            ProductWriter = new BufferedWriter(new FileWriter(ProductTypeFile));
            ProductWriter.write(mapper.writeValueAsString(ProductList));
            ProductWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean writeUsers(List<UserModel> UserList){

        ObjectMapper mapper = new ObjectMapper();
        try {
            UserWriter = new BufferedWriter(new FileWriter(UserFile));
            UserWriter.write(mapper.writeValueAsString(UserList));
            UserWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean writeCustomers(List<CustomerModel> CustomerList){
        ObjectMapper mapper = new ObjectMapper();
        try {
            CustomerWriter = new BufferedWriter(new FileWriter(CustomerFile));
            CustomerWriter.write(mapper.writeValueAsString(CustomerList));
            CustomerWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean writeBalance(BalanceModel balance){
        ObjectMapper mapper = new ObjectMapper();
        try {
            BalanceWriter = new BufferedWriter(new FileWriter(BalanceFile));
            BalanceWriter.write(mapper.writeValueAsString(balance));
            BalanceWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean writeOrders(List<OrderModel> OrderList){
        ObjectMapper mapper = new ObjectMapper();
        try {
            OrderWriter = new BufferedWriter(new FileWriter(OrderFile));
            OrderWriter.write(mapper.writeValueAsString(OrderList));
            OrderWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean writeLoyaltyCards(List<LoyaltyCardModel> cardList){
        ObjectMapper mapper = new ObjectMapper();
        try {
            LoyaltyWriter = new BufferedWriter(new FileWriter(LoyaltyFile));
            LoyaltyWriter.write(mapper.writeValueAsString(cardList));
            LoyaltyWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
