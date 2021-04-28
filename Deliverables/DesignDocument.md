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
    ProductTypeMap
    UserList
    CustomerMap
}
class User{
    Id
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
    Points
    addPoints()
}
class ProductType{
    BarCode
    Description
    PricePerUnit
    Note
    DiscountRate
    Quantity
    Position
}
class Order{
    ProductType
    orderId
    status
    pricePerUnit
    issue()
}
class SaleTransaction{
    PaymentType
    Time
    Cost
    getTicket()
    deleteTicket()
    computePoints()
}
class Sale{
    Id
    Status
    ProductList
    saleDiscountRate
    addProduct()
    deleteProduct()
    setDiscountRateForProduct()
    setDiscountRateForSale()
    getTotalPoints()
    computeCost()
    closeTransaction()
}
class Ticket{
    ProductList
    Id
    Amount
    Status
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
    ProductTypeList
    returnedAmount
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
    getAllBalanceOperations()
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
    writeProductTypeMap()
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
Sale -- "*" ProductType
Return -- "*" ProductType

@enduml
```





# Verification traceability matrix

\<for each functional requirement from the requirement document, list which classes concur to implement it>

| FR ID | EzShop | Balance | BalanceOperation | OrderTransaction | SaleTransaction | ReturnTransaction | Order | Sale | Return | Customer | LoyalityCard | User | ProductType | Ticket | Payment | CashPayment | CreditCardPayment | JsonWrite | JsonRead |
| :----: | :----: | :----: |:----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: | :----: |
| FR1 | X | | |  |  |  |  |  |  |  |  |X||||||X||
| FR3 | X || | | | | | | | | | X |X|||||X||
| FR4 | X |X|X|X|||X|||||X|X|||||X||
| FR5 |X|||||||||X|X|X||||||X||
| FR6 |X|X|X||X|X||X|X|||X|X|X||||X||
| FR7 |X|X|X||X|X||X|X|||X||X|X|X|X|X||
| FR8 |X|X|X|||||||||X||||||X||









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
participant JsonWrite as JW

U->Data: 1: createProductType()
Data->EzShop: 2: createProductType()
EzShop -> P: 3: ProductType()
P -> EzShop: 4: return ProductType
EzShop -> P: 5:getId()
P->EzShop: 6: return id
EzShop->EzShop: 7 ProductTypeMap.Add(Id, ProductType)
EzShop -> JW: 8: enableWrite()
EzShop -> JW: 9: writeProductTypeMap()
EzShop -> JW: 10: disableWrite()
EzShop->Data: 11: return ProductTypeId
Data->U: 12: return ProductTypeId

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
participant JsonWrite as JW

U->Data: 1: updateProduct()
Data->EzShop: 2: getProductTypeByBarCode()
EzShop->P: 3: setPricePerUnit()
P->EzShop: 4: return result
EzShop -> JW: 5: enableWrite()
EzShop -> JW: 6: writeProductTypeMap()
EzShop -> JW: 7: disableWrite()
EzShop->Data: 8: return result(boolean)
Data->U: 9: return result(boolean)
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
participant JsonWrite as JW

U1->Data: 1: createUser()
Data->EzShop: 2: createUser()
EzShop->U2: 3: User()
U2->EzShop: 4: return User
EzShop->EzShop: 5: UserList.Add(User)
EzShop -> JW: 6: enableWrite()
EzShop -> JW: 7: writeUserList()
EzShop -> JW: 8: disableWrite()
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
participant JsonWrite as JW

U1->Data: 1: deleteUser()
Data->EzShop: 2: deleteUserById()
EzShop->EzShop: 3: UserList.delete(User)
EzShop -> JW: 4: enableWrite()
EzShop -> JW: 5: writeUserList()
EzShop -> JW: 6: disableWrite()
EzShop->Data: 7: return result (boolean)
Data->U1: 8: return result(boolean)
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
EzShop->EzShop: 5: getProductByBarCode()
EzShop->O: 3: Order()
O->EzShop: 4 return Order
EzShop->O: 5: getId()
O->EzShop: 6: return OrderId
EzShop->EzShop: 7: ActiveOrderMap.Add(Id, Order)
EzShop->Data: 8: return OrderId
Data->U1: 9: return OrderId
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
participant JsonWrite as JW

U1->Data: 1: payOrder()
Data->EzShop: 2: ActiveOrderMap.get(OrderId)
EzShop->Data: 3: return Order
Data->EzShop: 4: getBalance()
EzShop-> Data: 5: return Balance
Data->Balance: 6: OrderTransactionMap.add(Id, Order)
Data->Balance: 7: BalanceOperationList.push(Order)
Balance->Data: 8: return result
Data->O: 9: setStatus()
O->Data: 10: return result
Data -> JW: 11: enableWrite()
Data -> JW: 12: writeBalance()
Data -> JW: 13: disableWrite()
Data->U1: 14: return result (boolean)
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
participant JsonWrite as JW

U1->Data: 1: recordOrderArrival()
Data->EzShop: 2: ActiveOrderMap.get(OrderId)
EzShop->Data: 3: return Order
Data->O: 4: getProductType()
O-> Data: 5: return ProductType
Data->P: 6: setQuantity()
P->Data: 7: return result (boolean)
Data->O: 8: setStatus()
O->Data: 9: return result
Data -> JW: 10: enableWrite()
Data -> JW: 11: writeBalance()
Data -> JW: 12: disableWrite()
Data->U1: 13: return result (boolean)
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
participant JsonWrite as JW

U1->Data: 1: defineCustomer()
Data->EzShop: 2: createCustomer()
EzShop->Customer: 3: Customer()
Customer->EzShop: 4: return Customer
EzShop->EzShop: 5: CustomerMap.add(Customer)
EzShop->Customer: 6: getId()
Customer->EzShop: 7: return CustomerId
EzShop -> JW: 8: enableWrite()
EzShop -> JW: 9: writeProductTypeMap()
EzShop -> JW: 10: disableWrite()
EzShop->Data: 11: return CustomerId
Data->U1: 12: return CustomerId
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
participant JsonWrite as JW

User -> Data: 1: startSaleTransaction()
Data -> EzShop: 2: createSale()
EzShop -> Sale: 3: Sale()
Sale -> EzShop: 4: return Sale
EzShop -> Sale: 5: getId()
Sale -> EzShop: 6: return transactionId
EzShop -> EzShop: 7: ActiveSaleMap.add(Id, Sale)
EzShop -> Data: 8: return transactionId
Data -> User: 9: return transactionId
User -> Data: 10: addProductToSale()
Data -> EzShop: 11: ActiveSaleMap.get(Id)
EzShop -> Data: 12: return Sale
Data -> Sale: 13: addProduct()
Sale -> EzShop: 14: getProductByBarCode()
EzShop -> Sale: 15: return ProductType
Sale -> Sale: 16: ProductList.push(ProductType)
Sale -> ProductType: 17: setQuantity()
Sale -> Data: 18: return outcome (boolean)
Data -> User: 19: return outcome (boolean)
User -> Data: 20: closeSaleTransaction()
Data -> Sale: 21: closeTransaction()
Sale -> SaleTransaction: 22: SaleTransaction()
SaleTransaction -> Ticket: 23: Ticket()
Ticket -> SaleTransaction: 24: return Ticket
SaleTransaction -> Sale: 25: return SaleTransaction
Sale -> EzShop: 26: getBalance()
EzShop -> Sale: 27: return Balance
Sale -> Balance: 28: SaleTransactionMap.add(Id, SaleTransaction)
Sale -> Balance: 29: BalanceOperationList.push(SaleTransaction)
Sale -> Data: 30: return outcome(boolean)
Data -> JW: 31: enableWrite()
Data -> JW: 32: writeAll()
Data -> JW: 33: disableWrite()
Data -> User: 34: return outcome(boolean)
User -> Data: 35: receiveCashPayment()
Data -> EzShop: 36: getBalance()
EzShop -> Data: 37: return Balance
Data -> Balance: 38: getSaleTransactionById()
Balance -> Data: 39: return SaleTransaction
Data -> SaleTransaction: 40: getTicket()
SaleTransaction -> Data: 41: return Ticket
Data -> Ticket: 42: setPayment()
Ticket -> CashPayment: 43: CashPayment()
CashPayment -> Ticket: 44: return CashPayment
Ticket -> Ticket: 45: setStatus()
Ticket -> CashPayment: 46: computeChange()
CashPayment -> Data: 47: return Change
Data -> JW: 48: enableWrite()
Data -> JW: 49: writeBalance()
Data -> JW: 50: disableWrite()
Data -> User: 51: return Change
User -> Data: 52: getSaleTicket()
Data -> EzShop: 53: getBalance()
EzShop -> Data: 54: return Balance
Data -> Balance: 55: getSaleTransactionById()
Balance -> Data: 56: return SaleTransaction
Data -> SaleTransaction: 57: getTicket()
SaleTransaction -> User: 58: return Ticket

@enduml
```

### SC6-4 and SC7-1
```plantuml
@startuml
scale 0.75
title "Sequence Diagram 11"
actor User
participant Data
participant EzShop
participant Sale
participant ProductType
participant SaleTransaction
participant Ticket
participant CashPayment
participant JsonWrite as JW

User -> Data: 1: startSaleTransaction()
Data -> EzShop: 2: createSale()
EzShop -> Sale: 3: Sale()
Sale -> EzShop: 4: return Sale
EzShop -> Sale: 5: getId()
Sale -> EzShop: 6: return transactionId
EzShop -> EzShop: 7: ActiveSaleMap.add(Id, Sale)
EzShop -> Data: 8: return transactionId
Data -> User: 9: return transactionId
User -> Data: 10: addProductToSale()
Data -> EzShop: 11: ActiveSaleMap.get(Id)
EzShop -> Data: 12: return Sale
Data -> Sale: 13: addProduct()
Sale -> EzShop: 14: getProductByBarCode()
EzShop -> Sale: 15: return ProductType
Sale -> Sale: 16: ProductList.push(ProductType)
Sale -> ProductType: 17: setQuantity()
Sale -> Data: 18: return outcome (boolean)
Data -> User: 19: return outcome (boolean)
User -> Data: 20: closeSaleTransaction()
Data -> Sale: 21: closeTransaction()
Sale -> SaleTransaction: 22: SaleTransaction()
SaleTransaction -> Ticket: 23: Ticket()
Ticket -> SaleTransaction: 24: return Ticket
SaleTransaction -> Sale: 25: return SaleTransaction
Sale -> EzShop: 26: getBalance()
EzShop -> Sale: 27: return Balance
Sale -> Balance: 28: SaleTransactionMap.add(Id, SaleTransaction)
Sale -> Balance: 29: BalanceOperationList.push(SaleTransaction)
Sale -> Data: 30: return outcome(boolean)
Data -> JW: 31: enableWrite()
Data -> JW: 32: writeAll()
Data -> JW: 33: disableWrite()
Data -> User: 34: return outcome(boolean)
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
Ticket -> Data: 44: return outcome(boolean)
EzShop -> JW: 45: enableWrite()
EzShop -> JW: 46: writeBalance()
EzShop -> JW: 47: disableWrite()
Data -> User: 48: return outcome(boolean)
User -> Data: 49: computePointsForSale()
Data -> EzShop: 50: getBalance()
EzShop -> Data: 51: return Balance
Data -> Balance: 52: getSaleTransactionById()
Balance -> Data: 53: return SaleTransaction
Data -> SaleTransaction: 54: computePoints()
SaleTransaction -> User: 55: return points
User -> Data: 56: modifyPointsOnCard()
Data -> EzShop: 57: getCustomerById()
EzShop -> Data: 58: return Customer
Data -> Customer: 59: getLoyalityCard()
Customer -> Data: 60: return LoyalityCard
Data -> LoyalityCard: 61: addPoints()
LoyalityCard -> Data: 62: return outcome (boolean)
EzShop -> JW: 63: enableWrite()
EzShop -> JW: 64: writeCustomerList()
EzShop -> JW: 65: disableWrite()
Data -> User: 66: return outcome(boolean)
User -> Data: 67: getSaleTicket()
Data -> EzShop: 68: getBalance()
EzShop -> Data: 69: return Balance
Data -> Balance: 70: getSaleTransactionById()
Balance -> Data: 71: return SaleTransaction
Data -> SaleTransaction: 72: getTicket()
SaleTransaction -> User: 73: return Ticket

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
participant JsonWrite as JW

User -> Data: 1: startReturnTransaction()
Data -> EzShop: 2: createReturnFromTicket()
EzShop -> Balance: 3: getSaleTransactionById()
Balance -> EzShop: 4: return SaleTransaction
EzShop -> SaleTransaction: 5: getTicket()
SaleTransaction -> EzShop: 6: return Ticket
EzShop -> Return: 7: Return()
Return -> EzShop: 8: return Return
EzShop -> EzShop: : ActiveReturnMap.add(Return)
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
Data -> JW: 35: enableWrite()
Data -> JW: 36: writeAll()
Data -> JW: 37: disableWrite()
Data -> User: 38: return outcome(boolean)
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

User -> Data: 1: getCreditsAndDebits()
Data -> EzShop: 2: getBalance()
EzShop -> Data: 3: return Balance
Data -> Balance: 4: getCreditsAndDebits()
Balance -> BalanceOperation: 5: getDate()
BalanceOperation -> Balance: 6: return Date
Balance -> User: 7: return FilteredBalanceOperationList

@enduml
```
