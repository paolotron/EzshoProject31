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

    package it.polito.ezshop.exceptions
    package it.polito.ezshop.model
    package it.polito.ezshop.data
    package it.polito.ezshop.GUI
    
    it.polito.ezshop.GUI -> it.polito.ezshop.data
    it.polito.ezshop.data -> it.polito.ezshop.exceptions
    it.polito.ezshop.data -> it.polito.ezshop.model
```





# Low level design

<for each package, report class diagram>


```plantuml

@startuml

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
    createBalanceOperation()
    createReturnFromTicket()
    validateCardWithLuhn()
    currentlyLoggedUser
    ActiveSaleMap
    ActiveReturnMap
    ActiveOrderMap
    ProductTypeList
    UserList
    CustomerList
    Balance
}
class User{
    Name
    Role
    PasswordHash
    checkPassword()
    setPasswordHash()
}
class Customer{
    Id
    Name
}
class LoyalityCard{
    Id
    Name
    addPoints()
}
class ProductType{
    getBarCode()
    setBarCode()
    getDescription()
    setDescription()
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
    ProductType
    orderId
    getId()
    getSupplier()
    setSupplier()
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
    Id
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
    Id
    Amount
    Status
    getProductList()
    setPayment()
    getPayment()
}
class Payment{
    Amount
    isReturn()
}
class CashPayment{
    Cash
    computeChange()
}
class CreditCardPayment{
    Card
    Outcome
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
    Status
    Id
    getTicket()
    addProduct()
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
Order -- ProductType


@enduml

```





# Verification traceability matrix

\<for each functional requirement from the requirement document, list which classes concur to implement it>

|    FR ID         | EzShop | BalanceOperation | OrderTransaction | SaleTransaction | ReturnTransaction | Order | Sale | Return | Customer | LoyalityCard | User | ProductType | Ticket | Payment | CashPayment | CreditCardPayment | JsonWrite | JsonRead |   
| :----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: |
| FR1 | X | | | | | | | | | X |||||||||
| FR3 |||||||||||||||||||
| FR4 |||||||||||||||||||
| FR5 |||||||||||||||||||
| FR6 |||||||||||||||||||
| FR7 |||||||||||||||||||
| FR8 |||||||||||||||||||









# Verification sequence diagrams 
\<select key scenarios from the requirement document. For each of them define a sequence diagram showing that the scenario can be implemented by the classes and methods in the design>

### SC1-1
``` plantuml
@startuml
title "Sequence Diagram 1"
actor User as U
participant Data
participant EzShop
participant ProductType as P

U->Data: 1: createProductType()
Data->EzShop: 2: createProductType()
EzShop -> P: 3: ProductType()
P -> EzShop: 4: return ProductType
EzShop ->P: 5: setDescription()
EzShop->P: 6: setProductCode()
EzShop->P: 7: setPricePerUnit()
EzShop->P: 8: setNote()
EzShop->P: 9: setPosition()
P->EzShop: 10: return result
EzShop->EzShop: 11 ProductTypeList.Add(ProductType)
EzShop->Data: 12: return ProductTypeId
Data->U: 13: return ProductTypeId
@enduml
```

### SC1-3

``` plantuml
@startuml
title "Sequence Diagram 2"
actor User as U
participant Data
participant EzShop
participant ProductType as P

U->Data: 1: updateProduct()
Data->EzShop: 2: getProductTypeByBarCode()
EzShop->P: 3: setPricePerUnit()
P->EzShop: 4: return result
EzShop->Data: 5: return result(boolean)
Data->U: 6: return result(boolean)
@enduml
```

### SC2-1

``` plantuml
@startuml
title "Sequence Diagram 3"
actor User as U1
participant Data
participant EzShop
participant User as U2

U1->Data: 1: createUser()
Data->EzShop: 2: createUser()
EzShop->U2: 3: User()
U2->EzShop: 4: return User
EzShop->U2: 5: setName()
EzShop->U2: 6: setRole()
EzShop->U2: 7: setPasswordHash()
EzShop->EzShop: 8: UserList.Add(User)
EzShop->U2: 9: getId()
U2->EzShop: 10: return UserId
EzShop->Data: 11: return UserId
Data->U1: 12: return UserId
@enduml
```

### SC2-2

``` plantuml
@startuml
title "Sequence Diagram 4"
actor User as U1
participant Data
participant EzShop

U1->Data: 1: deleteUser()
Data->EzShop: 2: deleteUserById()
EzShop->EzShop: 3: UserList.delete(User)
EzShop->Data: 4: return result (boolean)
Data->U1: 5: return result(boolean)
@enduml
```

### SC3-1
``` plantuml
@startuml
title "Sequence Diagram 5"
actor User as U1
participant Data
participant EzShop
participant Order as O

U1->Data: 1: issueReorder()
Data->EzShop: 2: createOrder()
EzShop->O: 3: Order()
O->EzShop: 4 return Order
EzShop->EzShop: 5: getProductByBarCode()
EzShop->O: 6: setProduct()
EzShop->O: 7: setQuantity();
EzShop->O: 8: setPricePerUnit()
EzShop->O: 9: setStatus()
EzShop->EzShop: 10: ActiveOrderMap.Add(Order)
EzShop->O: 11: getId()
O->EzShop: 12: return OrderId
EzShop->Data: 13: return OrderId
Data->U1: 14: return OrderId
@enduml
```

### SC3-2
``` plantuml
@startuml
title "Sequence Diagram 6"
actor User as U1
participant Data
participant EzShop
participant Order as O
participant Balance

U1->Data: 1: payOrder()
Data->EzShop: 2: ActiveOrderMap.get(OrderId)
EzShop->Data: 3: return Order
Data->EzShop: 4: getBalance()
EzShop-> Data: 5: return Balance
Data->Balance: 6: OrderTransactionMap.add(Order)
Data->Balance: 7: BalanceOperationList.push(Order)
Balance->Data: 8: return result
Data->O: 9: setStatus()
O->Data: 10: return result
Data->U1: 11: return result (boolean)
@enduml
```

### SC3-3
``` plantuml
@startuml
title "Sequence Diagram 7"
actor User as U1
participant Data
participant EzShop
participant Order as O
participant ProductType as P

U1->Data: 1: recordOrderArrival()
Data->EzShop: 2: ActiveOrderMap.get(OrderId)
EzShop->Data: 3: return Order
Data->O: 4: get ProductType
O-> Data: 5: return ProductType
Data->P: 6: setQuantity()
P->Data: 7: return result (boolean)
Data->O: 8: setStatus()
O->Data: 9: return result
Data->U1: 10: return result (boolean)
@enduml
```

### SC4-1
``` plantuml
@startuml
title "Sequence Diagram 8"
actor User as U1
participant Data
participant EzShop
participant Customer 

U1->Data: 1: defineCustomer()
Data->EzShop: 2: createCustomer()
EzShop->Customer: 3: Customer()
Customer->EzShop: 4: return Customer
EzShop->Customer: 5: setName()
EzShop->EzShop: 6: CustomerList.add(Customer)
EzShop->Customer: 7: getId()
Customer->EzShop: 8: return CustomerId
EzShop->Data: 9: return CustomerId
Data->U1: 10: return CustomerId
@enduml
```

### SC5-1
``` plantuml
@startuml
title "Sequence Diagram 9"
actor User as U1
participant Data
participant EzShop
participant User as U2

U1->Data: 1: login()
Data->EzShop: 2: getUserByUserName()
EzShop -> Data : 3: return User
Data -> U2 : 4: checkPassword()
U2 -> Data : 5: return result
Data->EzShop: 6: currentlyLoggedUser.set(User)
Data -> U1 : 7: return result
@enduml
```

### SC6-1 and SC7-4

``` plantuml
@startuml
title "Sequence Diagram 10"
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

### SC6-4 and SC7-1
```plantuml
@startuml
scale 0.8
title "Sequence Diagram 11"
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

### SC8-2 and SC10-2

```plantuml
@startuml
title "Sequence Diagram 12"
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
### SC9-1

```plantuml
@startuml
title "Sequence Diagram 13"
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
