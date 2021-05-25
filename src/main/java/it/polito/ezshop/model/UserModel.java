package it.polito.ezshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;

import java.util.Locale;

public class UserModel implements it.polito.ezshop.data.User {

    Integer Id;
    String Password;
    String Username;
    Roles Role;
    static Integer currentId = 1;


    /**
     * Made By Paolo
     * Should be used when creating class from Json file
     * @param id int sets the static variable that is used as a counter for new users
     */
    static void setCurrentId(int id){
        currentId = id;
    }

    public UserModel(){}


    /**
     * Made By Paolo
     * @param Username String
     * @param Password String
     * @param Role String, is saved as an enum from the Roles enum class, must be ShopManager, Administrator or Cashier
     * @throws InvalidRoleException if role is not of part of the enum role class
     */
    public UserModel(String Username, String Password, String Role) throws InvalidRoleException {
        this.Id = currentId;
        currentId++;
        this.Password = Password;
        this.Username = Username;
        this.Role = getRoleFromString(Role);
        if (this.Role == null)
            throw new InvalidRoleException("Inserted Role Does not exist");

    }

    public boolean checkPassword(String password) throws InvalidPasswordException {
        if(password == null || password.equals(""))
            throw new InvalidPasswordException("Password is null or empty");
        return this.Password.equals(password);
    }

    @Override
    public Integer getId() { return this.Id; }

    @Override
    public void setId(Integer id) {
        this.Id = id;
    }

    @Override
    public String getUsername() {
        return this.Username;
    }

    @Override
    public void setUsername(String username) {
        this.Username = username;
    }

    @Override
    public String getPassword() {
        return this.Password;
    }

    @Override
    public void setPassword(String password) {
        this.Password = password;
    }

    @Override
    public String getRole() {
        return getStringFromRole(this.Role);
    }

    @Override
    public void setRole(String role) {
        this.Role = getRoleFromString(role);
    }


    @JsonIgnore
    public Roles getEnumRole(){
        return this.Role;
    }

    static private Roles getRoleFromString(String s){
        switch (s.toLowerCase(Locale.ROOT)){
            case "shopmanager":
                return Roles.ShopManager;
            case "administrator":
                return Roles.Administrator;
            case "cashier":
                return Roles.Cashier;
            default:
                return null;
        }
    }

    static private String getStringFromRole(Roles R){
        if(R == null)
            return null;
        switch (R){
            case Cashier:
                return "Cashier";
            case Administrator:
                return "Administrator";
            case ShopManager:
                return "ShopManager";
        }
        return null;
    }
}
