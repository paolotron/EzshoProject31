package it.polito.ezshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.polito.ezshop.data.BalanceOperation;

import java.time.LocalDate;


public class BalanceOperationModel implements BalanceOperation {
    Integer balanceId;
    String operationType;
    Double money;
    LocalDate date;
    static Integer currentMaxId = 1;
    public BalanceOperationModel(){}

    public BalanceOperationModel(String type, Double amount, LocalDate date){
        balanceId = currentMaxId++;
        operationType = type;
        money = amount;
        this.date = date;
    }

    @Override
    public int getBalanceId() {
        return balanceId;
    }

    @Override
    public void setBalanceId(int balanceId) {
        this.balanceId = balanceId;
    }

    @JsonIgnore
    @Override
    public LocalDate getDate() {
        return date;
    }

    @JsonIgnore
    @Override
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDateS(){return date.toString();}

    public void setDateS(String dateS){this.date = LocalDate.parse(dateS);}

    @Override
    public double getMoney() {
        return money;
    }

    @Override
    public void setMoney(double money) {
        this.money = money;
    }

    @Override
    public String getType() {
        return operationType;
    }

    @Override
    public void setType(String type) {
        operationType = type;
    }

    @JsonIgnore
    public boolean isReturn(){
        return getType().equals("RETURN");
    }
}
