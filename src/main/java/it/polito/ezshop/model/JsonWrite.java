package it.polito.ezshop.model;


import com.fasterxml.jackson.databind.ObjectMapper;
import it.polito.ezshop.data.Customer;

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
    String ProductTypeFile;
    String BalanceFile;
    String UserFile;
    String CustomerFile;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public JsonWrite(String Folder) throws IOException {
        this.ProductTypeFile = Folder+"/product.json";
        this.BalanceFile = Folder+"/balance.json";
        this.UserFile = Folder+"/user.json";
        this.CustomerFile = Folder+"/customer.json";
        new File(UserFile).createNewFile();
        new File(BalanceFile).createNewFile();
        new File(ProductTypeFile).createNewFile();
        new File(CustomerFile).createNewFile();
    }

    public JsonWrite(){}

    public boolean enableWrite(){
        try {
            ProductWriter = new BufferedWriter(new FileWriter(ProductTypeFile));
            BalanceWriter = new BufferedWriter(new FileWriter(BalanceFile));
            UserWriter = new BufferedWriter(new FileWriter(UserFile));
            CustomerWriter = new BufferedWriter(new FileWriter(CustomerFile));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean disableWrite(){
        try {
            ProductWriter.close();
            BalanceWriter.close();
            UserWriter.close();
            CustomerWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean writeProducts(Map<String, ProductTypeModel> ProductMap){
        return writeProducts(new ArrayList<>(ProductMap.values()));
    }

    public boolean writeUsers(Map<String, UserModel> UserMap){
        return writeUsers(new ArrayList<>(UserMap.values()));
    }

    public boolean writeProducts(List<ProductTypeModel> ProductList){
        ObjectMapper mapper = new ObjectMapper();
        try {
            ProductWriter.write(mapper.writeValueAsString(ProductList));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean writeUsers(List<UserModel> UserList){
        ObjectMapper mapper = new ObjectMapper();
        try {
            UserWriter.write(mapper.writeValueAsString(UserList));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean writeCustomers(List<CustomerModel> CustomerList){
        ObjectMapper mapper = new ObjectMapper();
        try {
            CustomerWriter.write(mapper.writeValueAsString(CustomerList));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
