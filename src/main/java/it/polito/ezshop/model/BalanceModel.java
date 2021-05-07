package it.polito.ezshop.model;

import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.data.Order;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Optional;

public class BalanceModel {
    HashMap<Integer, OrderTransaction> orderTransactionMap;
    HashMap<Integer, ReturnTransaction> returnTransactionMap;
    HashMap<Integer, SaleTransaction> saleTransactionMap;
    ArrayList<BalanceOperation> balanceOperationList;
    Double BalanceAvailability;

    public BalanceModel(){
        orderTransactionMap = new HashMap<>();
        returnTransactionMap = new HashMap<>();
        saleTransactionMap = new HashMap<>();
        balanceOperationList = new ArrayList<>();
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

    //added by OMAR
    public void addOrderTransaction(Integer orderId, OrderTransaction ord){
        this.orderTransactionMap.put(orderId, ord);
    }
    //added by OMAR
    public void addBalanceOperation(BalanceOperation balanceOperation){
        this.balanceOperationList.add(balanceOperation);
    }

    //TODO to be implemented
    //return the available balance
    public Double computeBalance(){
        return null;
    }
    //MADE BY OMAR
    //return false if there isn't availability
    public boolean checkAvailability(Double toPay){

        if (this.BalanceAvailability > toPay ){
            return true;
        }else{
            return false;
        }
    }


}
