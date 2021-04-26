# Design Document 


Authors: 

Date:

Version:


# Contents

- [High level design](#package-diagram)
- [Low level design](#class-diagram)
- [Verification traceability matrix](#verification-traceability-matrix)
- [Verification sequence diagrams](#verification-sequence-diagrams)

# Instructions

The design must satisfy the Official Requirements document, notably functional and non functional requirements

# High level design 

<discuss architectural styles used, if any>
<report package diagram>
```plantuml
package frontend{

    package it.polito.ezshop.controller 
    package it.polito.ezshop.GUI
    }
package backend{
    package it.polito.ezshop.exceptions
    package it.polito.ezshop.model
    package it.polito.ezshop.data
    package it.polito.ezshop.back.controller
}


/'it.polito.ezshop.GUI --> it.polito.ezshop.model'/ 
/'it.polito.ezshop.controller --> it.polito.ezshop.model'/ 
it.polito.ezshop.exceptions --> it.polito.ezshop.data
it.polito.ezshop.model --> it.polito.ezshop.data
it.polito.ezshop.back.controller --> it.polito.ezshop.model
/'it.polito.ezshop.model --> it.polito.ezshop.data'/
```





# Low level design

<for each package, report class diagram>


```plantuml


class EzShop{
    login()
    logout()
    getAllUsers()
    getAllCustomers()
    getAllProducts()
    getBalance()
    getUserById()
    getCustomerById()
    getProductTypeByBarCode()
    getProductTypeByDescritption()
    createUser()
    createCustomer()
    createProductType()
    deleteProductType()
    deleteCustomer()
    deleteUserById()
    reset()
    createSale()
    createOrder()
    createTransaction()
    createReturnFromTicket()
    validateCardWithLuhn()
    currentlyLoggedUser
}
class User{
    Name
    Role
    PasswordHash
    getId()
    getName()
    setName()
    getRole()
    setRole()
    checkPassword()
}
class Customer{
    getId()
    getName()
    setName()
    getLoyalityCard()
    setLoyalityCard()
}
class LoyalityCard{
    getId()
    getPoints()
    setPoints()
    addPoints()
}
class ProductType{
    getBarCode()
    getDescription()
    setDescription()
    getProductCode()
    setProductCode()
    getPricePerUnit()
    setPricePerUnit()
    getNote()
    setNote()
    getDiscountRate()
    setDiscountRate()
    getQuantity()
    setQuantity()
    getPosition()
    setPosition()
}
class Order{
    getId()
    getSupplier()
    setSupplier()
    getProductList()
    setProductList()
    getQuantityList()
    setQuantityList()
    getStatus()
    setStatus()
    issue()
}
class Transaction{
    getId()
    getDesctiption()
    getAmount()
    getDate()
    
}
class SaleTransaction{
    getTime()
    getTicket()
    getCost()
    getPaymentType()
    deleteTicket()
    computePoints()
}
class Sale{
    getId()
    addProduct()
    deleteProduct()
    setDiscountRateForProduct()
    setDiscountRateForSale()
    getTotalPoints()
    closeTransaction()
    getStatus()
    generateTicket()
}
class Ticket{
    ProductList
    
    getId()
    getProductList()
    getAmount()
    setPayment()
    getPayment()
    getStatus()
    setStatus()
}
class Payment{
    Amount
    getAmount()
    setAmount()
    isReturn()
}
class CashPayment{
    Cash
    getCash()
    setCash()
    computeChange()
}
class CreditCardPayment{
    Card
    Outcome
    getCard()
    setCard()
    getOutcome()
    setOutcome()
    sendPaymentRequestThroughAPI()
}

class ReturnTransaction{
    Amount
    QuantityList
    getProductList()
    getQuantity()
    getReturnedValue()
    getTicket()
    deleteTicket()
}
class Return{
    ProductTypeList
    getTicket()
    addProduct()
    closeTransaction()
}
class Balance{
    TransactionList
    ReturnTransactionMap
    SaleTransactionMap
    OrderTransactionMap
    BalanceOperationMap
    getCreditsAndDebits()
    computeBalance()
    getAllTransactions()
    getAllOrderTransactions()
    getTransactionById()
    getSaleTransactionById()
    getOrderTransactionById()
    getReturnTransactionById()
    getAllReturnTransactions()
    getAllSaleTransactions()
    getAllTickets()
}
class BalanceOperation{
    OperationType
    Amount
    Date
    getOperationType()
    setOperationType()
    getAmount()
    getDate()
    setDate()
}
class OrderTransaction{
    getOrder()
}
class JsonRead{
    parseProductTypeList()
    readProductTypeList()
    parseBalance()
    readbalance()
    parseUserList()
    readUserList()
    parseCustomerList()
    readCustomerList()
}
class JsonWrite{
    enableWrite()
    disableWrite()
    writeProductTypeList()
    writeBalance()
    writeUserList()
    writeCustomerList()
    writeAll()
}
Transaction <|-- SaleTransaction
Transaction <|-- ReturnTransaction
Ticket -- SaleTransaction
ReturnTransaction -- Ticket
Return -- ReturnTransaction
Payment <|-- CreditCardPayment
Payment <|-- CashPayment
Ticket -- Payment
Balance  -- "*" BalanceOperation
Balance -- "*" Transaction
Transaction <|-- OrderTransaction
OrderTransaction"0..1" -- Order
Customer"0..1" -- LoyalityCard
EzShop -- "*" User
EzShop -- "*" Customer
EzShop -- Balance
EzShop -- "*" ProductType
EzShop -- Order
EzShop -- Return
EzShop --  Sale
Sale -- SaleTransaction
EzShop -- JsonRead
EzShop -- JsonWrite
Return -- Ticket


```





# Verification traceability matrix

\<for each functional requirement from the requirement document, list which classes concur to implement it>











# Verification sequence diagrams 
\<select key scenarios from the requirement document. For each of them define a sequence diagram showing that the scenario can be implemented by the classes and methods in the design>

SC6-1 and SC7-4

``` plantuml
@startuml
title "Sequence Diagram 1"
actor User
participant Data
participant EzShop
participant Sale
participant ProductType
participant SaleTransaction
participant Ticket
participant CashPayment

User -> Data: 1: startSaleTransaction()
Data -> EzShop: 2: getNewSale()
EzShop -> Sale: 3: Sale()
Sale -> EzShop: 4: return Sale
EzShop -> Sale: 5: getId()
Sale -> EzShop: 6: return transactionId
EzShop -> Data: 7: return transactionId
Data -> User: 8: return transactionId
User -> Data: 9: addProductToSale()
Data -> Sale: 10: addProduct()
Sale -> EzShop: 11: getProductByBarCode()
EzShop -> Sale: 12: return ProductType
Sale -> ProductType: 13: setQuantity()
Sale -> Data: 14: return outcome (boolean)
Data -> User: 15: return outcome (boolean)
User -> Data: 16: closeSaleTransaction()
Data -> Sale: 17: closeTransaction()
Sale -> SaleTransaction: 18: SaleTransaction()
SaleTransaction -> Ticket: 19: Ticket()
Ticket -> SaleTransaction: 20: return Ticket
SaleTransaction -> Sale: 21: return SaleTransaction
Sale -> EzShop: 22: getBalance()
EzShop -> Sale: 23: return Balance
Sale -> Balance: 24: SaleTransactionMap.add(Id, SaleTransaction)
Sale -> Balance: 25: TransactionList.push(SaleTransaction)
Sale -> Data: 26: return outcome(boolean)
Data -> User: 27: return outcome(boolean)
User -> Data: 28: receiveCashPayment()
Data -> EzShop: 29: getBalance()
EzShop -> Data: 30: return Balance
Data -> Balance: 31: getSaleTransactionById()
Balance -> Data: 32: return SaleTransaction
Data -> SaleTransaction: 32: getTicket()
SaleTransaction -> Data: 33: return Ticket
Data -> Ticket: 34: setPayment()
Ticket -> CashPayment: 35: CashPayment()
CashPayment -> Ticket: 36: return CashPayment
Ticket -> Ticket: 37: setStatus()
Ticket -> CashPayment: 38: computeChange()
CashPayment -> Data: 39: return Change
Data -> User: 40: return Change
User -> Data: 41: getSaleTicket()
Data -> EzShop: 42: getBalance()
EzShop -> Data: 43: return Balance
Data -> Balance: 44: getSaleTransactionById()
Balance -> Data: 45: return SaleTransaction
Data -> SaleTransaction: 46: getTicket()
SaleTransaction -> User: 47: return Ticket

@enduml
```

SC6-4 and SC7-1
```plantuml
@startuml
scale 0.8
title "Sequence Diagram 2"
actor User
participant Data
participant EzShop
participant Sale
participant ProductType
participant SaleTransaction
participant Ticket
participant CreditCardPayment

User -> Data: 1: startSaleTransaction()
Data -> EzShop: 2: getNewSale()
EzShop -> Sale: 3: Sale()
Sale -> EzShop: 4: return Sale
EzShop -> Sale: 5: getId()
Sale -> EzShop: 6: return transactionId
EzShop -> Data: 7: return transactionId
Data -> User: 8: return transactionId
User -> Data: 9: addProductToSale()
Data -> Sale: 10: addProduct()
Sale -> EzShop: 11: getProductByBarCode()
EzShop -> Sale: 12: return ProductType
Sale -> ProductType: 13: setQuantity()
Sale -> Data: 14: return outcome (boolean)
Data -> User: 15: return outcome (boolean)
User -> Data: 16: closeSaleTransaction()
Data -> Sale: 17: closeTransaction()
Sale -> SaleTransaction: 18: SaleTransaction()
SaleTransaction -> Ticket: 19: Ticket()
Ticket -> SaleTransaction: 20: return Ticket
SaleTransaction -> Sale: 21: return SaleTransaction
Sale -> EzShop: 22: getBalance()
EzShop -> Sale: 23: return Balance
Sale -> Balance: 24: SaleTransactionMap.add(Id, SaleTransaction)
Sale -> Balance: 25: TransactionList.push(SaleTransaction)
Sale -> Data: 26: return outcome(boolean)
Data -> User: 27: return outcome(boolean)
User -> Data: 28: receiveCreditCardPayment()
Data -> EzShop: 29: validateCardWithLuhn()
EzShop -> Data: 30: return outcome(boolean)
Data -> EzShop: 31: getBalance()
EzShop -> Data: 32: return Balance
Data -> Balance: 33: getSaleTransactionById()
Balance -> Data: 34: return SaleTransaction
Data -> SaleTransaction: 35: getTicket()
SaleTransaction -> Data: 36: return Ticket
Data -> Ticket: 37: setPayment()
Ticket -> CreditCardPayment: 38: CreditCardPayment()
CreditCardPayment -> Ticket: 39: return CreditCardPayment
Ticket -> CreditCardPayment: 40: sendPaymentRequestThroughAPI()
CreditCardPayment -> CreditCardPayment: 41: setOutcome()
CreditCardPayment -> Ticket: 42: return outcome(boolean)
Ticket -> Ticket: 43: setStatus()
Ticket -> User: 44: return outcome(boolean)
User -> Data: 45: computePointsForSale()
Data -> EzShop: 46: getBalance()
EzShop -> Data: 47: return Balance
Data -> Balance: 48: getSaleTransactionById()
Balance -> Data: 49: return SaleTransaction
Data -> SaleTransaction: 50: computePoints()
SaleTransaction -> User: 51: return points
User -> Data: 52: modifyPointsOnCard()
Data -> EzShop: 53: getCustomerById()
EzShop -> Data: 54: return Customer
Data -> Customer: 55: getLoyalityCard()
Customer -> Data: 56: return LoyalityCard
Data -> LoyalityCard: 57: addPoints()
LoyalityCard -> User: 58: return outcome (boolean)
User -> Data: 59: getSaleTicket()
Data -> EzShop: 60: getBalance()
EzShop -> Data: 61: return Balance
Data -> Balance: 62: getSaleTransactionById()
Balance -> Data: 63: return SaleTransaction
Data -> SaleTransaction: 64: getTicket()
SaleTransaction -> User: 65: return Ticket

@enduml
```
