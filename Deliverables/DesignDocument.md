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

scale 0.7
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
    getUserByUsername()
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
    ActiveSaleMap
    ActiveReturnMap
    ProductTypeList
    UserList
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
    getId()
    getTicket()
    addProduct()
    getStatus()
    setStatus()
    setPayment()
    closeTransaction()
}
class Balance{
    ReturnTransactionMap
    SaleTransactionMap
    OrderTransactionMap
    BalanceOperationList
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
BalanceOperation <|-- SaleTransaction
BalanceOperation <|-- ReturnTransaction
Ticket -- SaleTransaction
ReturnTransaction -- Ticket
Return -- ReturnTransaction
Payment <|-- CreditCardPayment
Payment <|-- CashPayment
Ticket -- Payment
Balance  -- "*" BalanceOperation
BalanceOperation <|-- OrderTransaction
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
Return -- Payment

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
Sale -> Balance: 25: BalanceOperationList.push(SaleTransaction)
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
Sale -> Balance: 25: BalanceOperationList.push(SaleTransaction)
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

SC8-2 and SC10-2

```plantuml
@startuml
title "Sequence Diagram 3"
actor User
participant Data
participant EzShop
participant Return

User -> Data: 1: startReturnTransaction()
Data -> EzShop: 2: createReturnFromTicket()
EzShop -> Balance: 3: getSaleTransactionById()
Balance -> EzShop: 4: return SaleTransaction
EzShop -> SaleTransaction: 5: getTicket()
SaleTransaction -> EzShop: 6: return Ticket
EzShop -> Return: 7: Return()
Return -> EzShop: 8: return Return
EzShop -> Return: 9: getId()
Return -> User: 10: return Id
User -> Data: 11: returnProduct()
Data -> EzShop: 12: activeReturnMap.get()
EzShop -> Data: 13: return Return
Data -> Return: 14: addProduct()
Return -> Data: 15: return outcome
Data -> ProductType: 16: setQuantity()
Data -> User: 17: return outcome
User -> Data: 18: returnCashPayment()
Data -> EzShop: 19: activeReturnMap.get()
EzShop -> Data: 20:return Return
Data -> Return: 21: setPayment()
Return -> CashPayment: 22: CashPayment()
CashPayment -> Return: 23: return CashPayment
Return -> User: 24: return amount
User -> Data: 25: endReturnTransaction()
Data -> EzShop: 26: activeReturnMap.get()
EzShop -> Data: 27:return Return
Data -> Return: 28: closeTransaction()
Return -> ReturnTransaction: 29: ReturnTransaction()
ReturnTransaction -> Data: 30: return ReturnTransaction
Data -> EzShop: 31: getBalance()
EzShop -> Data: 32: return Balance
Data -> Balance: 33: returnTransactionMap.add()
Data -> Balance: 34: BalanceOperationList.push()
Data -> User: 35: return outcome(boolean)
@enduml
```
SC9-1
```plantuml
@startuml
title "Sequence Diagram 4"
actor User
participant Data
participant EzShop
participant Balance
participant BalanceOperation

User -> Data: 0: getCreditsAndDebits()
Data -> EzShop: 1: getBalance()
EzShop -> Data: 2: return Balance
Data -> Balance: 3: getCreditsAndDebits()
Balance -> BalanceOperation: 4: getDate()
BalanceOperation -> Balance: 5: return Date
Balance -> User: 6: return FilteredBalanceOperationList

@enduml
```
