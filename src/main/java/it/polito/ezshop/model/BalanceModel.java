package it.polito.ezshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.exceptions.UnauthorizedException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

public class BalanceModel {
    HashMap<Integer, OrderTransactionModel> orderTransactionMap;
    HashMap<Integer, ReturnTransactionModel> returnTransactionMap;
    HashMap<Integer, SaleTransactionModel> saleTransactionMap;
    ArrayList<BalanceOperation> balanceOperationList;
    double balanceAmount;

    public HashMap<Integer, OrderTransactionModel> getOrderTransactionMap() {
        return orderTransactionMap;
    }

    public void setOrderTransactionMap(HashMap<Integer, OrderTransactionModel> orderTransactionMap) {
        this.orderTransactionMap = orderTransactionMap;
    }

    public HashMap<Integer, ReturnTransactionModel> getReturnTransactionMap() {
        return returnTransactionMap;
    }

    public void setReturnTransactionMap(HashMap<Integer, ReturnTransactionModel> returnTransactionMap) {
        this.returnTransactionMap = returnTransactionMap;
    }

    public HashMap<Integer, SaleTransactionModel> getSaleTransactionMap() {
        return saleTransactionMap;
    }

    public void setSaleTransactionMap(HashMap<Integer, SaleTransactionModel> saleTransactionMap) {
        this.saleTransactionMap = saleTransactionMap;
    }

    public double getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(double balanceAmount) {
        this.balanceAmount = balanceAmount;
    }



    public BalanceModel(){
        orderTransactionMap = new HashMap<>();
        returnTransactionMap = new HashMap<>();
        saleTransactionMap = new HashMap<>();
        balanceOperationList = new ArrayList<>();
        balanceAmount = 0;
    }

    public SaleTransactionModel getSaleTransactionById(Integer id){
        return saleTransactionMap.get(id);
        /* SaleTransaction saleT = saleTransaction.get(id)
        if(saleT == null)
            throw new invalidBalanceOperationId();
         return saleT;
         */
    }

    @JsonIgnore
    public ReturnTransactionModel getReturnTransactionById(Integer id){
        return returnTransactionMap.get(id);
    }

    @JsonIgnore
    public OrderTransactionModel getOrderTransactionById(Integer id){
        return orderTransactionMap.get(id);
    }

    //TODO: test this code
    @JsonIgnore
    public Optional<BalanceOperation> getTransactionById(Integer id){
        return balanceOperationList.stream().filter((balanceOperation) -> balanceOperation.getBalanceId() == id).findFirst();
    }

    @JsonIgnore
    public List<BalanceOperation> getAllBalanceOperations(){
        return balanceOperationList;
    }


    /**
     * Made by Manuel
     * @param b It is any BalanceOperation that should be added to the list
     */
    public void addBalanceOperation(BalanceOperationModel b){
        balanceOperationList.add(b);
        balanceAmount += b.getMoney();
    }

    /**
     * Made by Manuel
     * @param from the start date : if null it means that there should be no constraint on the start date
     * @param to the end date : if null it means that there should be no constraint on the end date
     *
     * @return All the operations on the balance whose date is <= to and >= from
     */
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to){
        if(from == null || to == null){
            if(from == null) {
                LocalDate finalTo1 = to;
                return balanceOperationList.stream().filter(balanceOperation -> balanceOperation.getDate().isBefore(finalTo1.plus(1,ChronoUnit.DAYS))).collect(Collectors.toList());
            }
            else {
                LocalDate finalFrom1 = from;
                return balanceOperationList.stream().filter(balanceOperation -> balanceOperation.getDate().isAfter(finalFrom1.minus(1, ChronoUnit.DAYS))).collect(Collectors.toList());
            }
        }
        if(from.isAfter(to)) {
            LocalDate aux = from;
            from = to;
            to = aux;
        }
        LocalDate finalFrom = from;
        LocalDate finalTo = to;
        return balanceOperationList.stream().filter(balanceOperation -> balanceOperation.getDate().isAfter(finalFrom.minus(1, ChronoUnit.DAYS)) && balanceOperation.getDate().isBefore(finalTo.plus(1,ChronoUnit.DAYS))).collect(Collectors.toList());
    }

    /**
     * Made by Manuel
     * @return The total amount of the actual balance
     */
    public double computeBalance() {
        return balanceAmount;
    }

    //MADE BY OMAR
    //if there isn't Balance availability return false
    public boolean checkAvailability(Double toPay) throws UnauthorizedException{
        return toPay <= this.computeBalance();
    }
    //MADE BY OMAR
    public void addOrderTransaction(OrderTransactionModel orderTransactionModel){
        this.orderTransactionMap.put(orderTransactionModel.getBalanceId(), orderTransactionModel);
    }
}





