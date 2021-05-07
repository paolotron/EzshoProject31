package it.polito.ezshop.model;

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
    HashMap<Integer, OrderTransaction> orderTransactionMap;
    HashMap<Integer, ReturnTransaction> returnTransactionMap;
    HashMap<Integer, SaleTransaction> saleTransactionMap;
    ArrayList<BalanceOperation> balanceOperationList;
    double balanceAmount;

    public BalanceModel(){
        orderTransactionMap = new HashMap<>();
        returnTransactionMap = new HashMap<>();
        saleTransactionMap = new HashMap<>();
        balanceOperationList = new ArrayList<>();
        balanceAmount = 0;
    }

    public SaleTransaction getSaleTransactionById(Integer id){
        return saleTransactionMap.get(id);
        /* SaleTransaction saleT = saleTransaction.get(id)
        if(saleT == null)
            throw new invalidBalanceOperationId();
         return saleT;
         */
    }

    public ReturnTransaction getReturnTransactionById(Integer id){
        return returnTransactionMap.get(id);
    }

    public OrderTransaction getOrderTransactionById(Integer id){
        return orderTransactionMap.get(id);
    }

    //TODO: test this code
    public Optional<BalanceOperation> getTransactionById(Integer id){
        return balanceOperationList.stream().filter((balanceOperation) -> balanceOperation.getBalanceId() == id).findFirst();
    }

    public List<BalanceOperation> getAllBalanceOperations(){
        return balanceOperationList;
    }

    public HashMap<Integer, OrderTransaction> getAllOrderTransactions(){
        return orderTransactionMap;
    }
    public HashMap<Integer, SaleTransaction> getAllSaleTransactions(){
        return saleTransactionMap;
    }
    public HashMap<Integer, ReturnTransaction> getAllReturnTransactions(){
        return returnTransactionMap;
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
     * @throws UnauthorizedException
     */
    public double computeBalance() throws UnauthorizedException {
        return this.balanceAmount;
    }

    //MADE BY OMAR
    //if there isn't Balance availability return false
    public boolean checkAvailability(Double toPay) throws UnauthorizedException{
        if(toPay <= this.computeBalance() ) {
            return true;
        }
        return false;
    }
    //MADE BY OMAR
    public void addOrderTransaction(OrderTransaction orderTransaction){
        this.orderTransactionMap.put(orderTransaction.getBalanceId(), orderTransaction);
    }
}





