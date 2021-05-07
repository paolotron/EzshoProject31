package it.polito.ezshop.model;

import it.polito.ezshop.data.BalanceOperation;

import java.time.LocalDate;


public class BalanceOperationModel implements BalanceOperation {
    Integer balanceId;
    String operationType;
    Double money;
    LocalDate date;
    static Integer currentMaxId = 0;

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

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public void setDate(LocalDate date) {
        this.date = date;
    }

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
}
