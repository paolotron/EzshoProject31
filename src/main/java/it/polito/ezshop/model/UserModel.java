package it.polito.ezshop.model;

import it.polito.ezshop.exceptions.InvalidRoleException;

import java.util.Locale;

public class UserModel implements it.polito.ezshop.data.User {

    Integer Id;
    String password;
    String Username;
    Roles role;
    static Integer currentId = 0;

    UserModel(){

    }
    UserModel(String password, String username, Roles role){
        this.Id = currentId;
        currentId++;
        this.password = password;
        this.Username = username;
        this.role = role;
    }
    UserModel(String password, String username, String role) throws InvalidRoleException {
        this.Id = currentId;
        currentId++;
        this.password = password;
        this.Username = username;
        switch (role.toLowerCase(Locale.ROOT)){
            case "shopmanager":
                this.role = Roles.ShopOwner;
                break;
            case "administrator":
                this.role = Roles.Administrator;
                break;
            case "cashier":
                this.role = Roles.Cashier;
                break;
            default:
                throw new InvalidRoleException("The Role does not exist");
        }

    }

    @Override
    public Integer getId() {
        return null;
    }

    @Override
    public void setId(Integer id) {

    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public void setUsername(String username) {

    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public void setPassword(String password) {

    }

    @Override
    public String getRole() {
        return null;
    }

    @Override
    public void setRole(String role) {

    }
}
