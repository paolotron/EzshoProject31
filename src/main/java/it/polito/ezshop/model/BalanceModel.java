package it.polito.ezshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.polito.ezshop.data.BalanceOperation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

public class BalanceModel {

    private HashMap<Integer, OrderTransactionModel> orderTransactionMap;
    private HashMap<Integer, ReturnTransactionModel> returnTransactionMap;
    private HashMap<Integer, SaleTransactionModel> saleTransactionMap;
    ArrayList<BalanceOperationModel> balanceOperationList;

    public BalanceModel(){
        orderTransactionMap = new HashMap<>();
        returnTransactionMap = new HashMap<>();
        saleTransactionMap = new HashMap<>();
        balanceOperationList = new ArrayList<>();
    }

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

    @JsonIgnore
    public SaleTransactionModel getSaleTransactionById(Integer id) {
        return saleTransactionMap.get(id);
    }

    @JsonIgnore
    public ReturnTransactionModel getReturnTransactionById(Integer id){
        return returnTransactionMap.get(id);
    }

    @JsonIgnore
    public OrderTransactionModel getOrderTransactionById(Integer id){
        return orderTransactionMap.get(id);
    }

    @JsonIgnore
    public Optional<BalanceOperationModel> getTransactionById(Integer id){
        return getAllBalanceOperations().stream().filter((balanceOperation) -> balanceOperation.getBalanceId() == id).findFirst();
    }

    @JsonIgnore
    public List<BalanceOperationModel> getAllBalanceOperations(){
        return new ArrayList<>(balanceOperationList);
    }



    /**
     * Made by Manuel
     * @param b It is any BalanceOperation that should be added to the list
     */
    @JsonIgnore
    public void addBalanceOperation(BalanceOperationModel b){
        balanceOperationList.add(b);
    }

    /**
     * Made by Manuel
     * @param from the start date : if null it means that there should be no constraint on the start date
     * @param to the end date : if null it means that there should be no constraint on the end date
     *
     * @return All the operations on the balance whose date is <= to and >= from
     */
    @JsonIgnore
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
    @JsonIgnore
    public double computeBalance() {
        return this.balanceOperationList.stream().filter((op)->!op.isReturn()).mapToDouble(BalanceOperation::getMoney).sum();
    }

    //MADE BY OMAR
    //if there isn't Balance availability return false
    @JsonIgnore
    public boolean checkAvailability(Double toPay){
        return !(toPay > computeBalance());
    }
    //MADE BY OMAR
    @JsonIgnore
    public void addOrderTransaction(OrderTransactionModel orderTransactionModel){
        addBalanceOperation(orderTransactionModel);
        this.orderTransactionMap.put(orderTransactionModel.getBalanceId(), orderTransactionModel);
    }

    public void addSaleTransactionModel(Integer saleId, SaleTransactionModel sale){
        addBalanceOperation(sale);
        this.saleTransactionMap.put(saleId, sale);
    }

    public void addReturnTransactionModel(Integer saleId, ReturnTransactionModel retur){
        addBalanceOperation(retur);
        this.returnTransactionMap.put(saleId, retur);
    }
}





