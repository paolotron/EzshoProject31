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
}
class User{
    getId()
    getName()
    setName()
    getRole()
    setRole()
}
class Customer{
    getId()
    getName()
    setName()
    getLoyalityCardId()
}
class LoyalityCard{
    getId()
    getPoints()
    setPoints()
}
class ProductType{
    getId()
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
    getOptionalLoyalityCard()
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
}
class Ticket{
    getId()
    getProductList()
    getAmount()
    getDiscountList()
    getTotalDiscount()
}
class ReturnTransaction{
    getQuantity()
    getReturnedValue()
    getTicket()
}
class Return{
    getTicket()
    addProduct()
    closeTransaction()
}
Transaction <|-- SaleTransaction
Transaction <|-- ReturnTransaction
Sale  -- Ticket
Ticket -- SaleTransaction
ReturnTransaction -- Ticket
Return -- Ticket
Return -- ReturnTransaction
```





# Verification traceability matrix

\<for each functional requirement from the requirement document, list which classes concur to implement it>











# Verification sequence diagrams 
\<select key scenarios from the requirement document. For each of them define a sequence diagram showing that the scenario can be implemented by the classes and methods in the design>

