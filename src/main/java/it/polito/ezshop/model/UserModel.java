package it.polito.ezshop.model;

import it.polito.ezshop.exceptions.InvalidCustomerIdException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;

import java.util.Locale;

public class UserModel implements it.polito.ezshop.data.User {

    Integer Id;
    String password;
    String Username;
    Roles role;
    static Integer currentId = 0;


    /**
     * Made By Paolo
     * Should be used when creating class from Json file
     * @param id int sets the static variable that is used as a counter for new users
     */
    static void setCurrentId(int id){
        currentId = id;
    }

    private UserModel(){ }

    UserModel(Integer Id, String Username, String password, Roles role){
        this.Id = Id;
        this.Username = Username;
        this.password = password;
        this.role = role;
    }

    UserModel(String username, String password, Roles role){
        this.Id = currentId;
        currentId++;
        this.password = password;
        this.Username = username;
        this.role = role;
    }

    /**
     * Made By Paolo
     * @param username String
     * @param password String
     * @param role String, is saved as an enum from the Roles enum class, must be ShopManager, Administrator or Cashier
     * @throws InvalidRoleException if role is not of part of the enum role class
     */
    public UserModel(String username, String password, String role) throws InvalidRoleException {
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

    public boolean checkPassword(String password) throws InvalidPasswordException {
        if(password == null || password.equals(""))
            throw new InvalidPasswordException("Password is null or empty");
        return this.password.equals(password);
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
        return this.password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getRole() {
        return null;
    }

    @Override
    public void setRole(String role) {

    }
}
