# Graphical User Interface Prototype  

Authors:Paolo Rabino, Andrea Sindoni, Omar Gai, Manuel Messina

Date:18/04/2021

Version:1.0

\<Report here the GUI that you propose. You are free to organize it as you prefer. A suggested presentation matches the Use cases and scenarios defined in the Requirement document. The GUI can be shown as a sequence of graphical files (jpg, png)  >
# General Employee Interface
## Employee and Owner Login
![LoginEmployee.png](Deliverables/GUIpngs/Login.png)
## Employee Interface Selection
Depending on the role of the Employee one or more buttons will be enabled
![GenericEmployee.png](./GUIpngs/GenericEmployee.png)
### Owner Interface
#### General View on Employees (FR8)
![ManageEmployees.png](./GUIpngs/GenericOwner.png)
#### Define new Employee (FR8.1)
![NewEmployee.png](./GUIpngs/New_Employee.png)
#### Modify Employee (FR8.1)
![ModifyEmployee.png](./GUIpngs/ModifyEmployee.png)
### Users Manager
#### General View on Customers
![ManageCustomer.png](./GUIpngs/ManageCustomers.png)
#### Transfer Points Between Customers (FR1.4.4)
![TransferPoints.png](./GUIpngs/TransferPoints.png)
#### Exchange Points for Product
![SpendPoints.png](./GUIpngs/OnlineShop/SpendPoints.png)
#### Create new Customer (FR1.1)
![CreateUser.png](./GUIpngs/NewCustomer.png)
#### Modify Customer (FR1.1)
![ModifyCustomer.png](./GUIpngs/ModifyCustomer.png)
#### View Customer Purchase and Orders 
![ViewCustomer.png](./GUIpngs/ViewCustomer.png)


# Online Order Interface
#### General Online Page for catalogue (FR7)
![OnlineShop.png](./GUIpngs/OnlineShop/OnlineShop.png)
![OnlineShopExpanded.png](./GUIpngs/OnlineShop/OnlineShopExpanded.png)
![OnlineShopLogged.png](./GUIpngs/OnlineShop/OnlineShopLogged.png)
#### Show Customer orders
![OnlineShopLogged.png](./GUIpngs/OnlineShop/ViewProfile.png)
#### Shop cart view (FR7.4)
![ShopCart.png](./GUIpngs/OnlineShop/ShopCart.png)
![ShopCart.png](./GUIpngs/OnlineShop/ShopCartConfirm.png)
![ShopCart.png](./GUIpngs/OnlineShop/ShopCartFailed.png)

# Cashier Interface

### Main Page
![Main_Page.png](./GUIpngs/Cashier Gui/Main_Page.png)
### Transaction in progress
### Transaction main page (FR9)
![Transaction_in_Progress_No_item_selected.png](./GUIpngs/Cashier Gui/Transaction_in_Progress_No_item_selected.png)
![Transaction_in_Progress_item_selected.png](./GUIpngs/Cashier Gui/Transaction_in_Progress_item_selected.png)
### Removing product (FR9.1.2)
![Removing_Product_Qty_Valid.png](./GUIpngs/Cashier Gui/Removing_Product_Qty_Valid.png)
![Removing_Product_Qty_Not Valid.png](./GUIpngs/Cashier Gui/Removing_Product_Qty_Not Valid.png)
### Adding product (FR9.1.1)
![Adding_Product.png](./GUIpngs/Cashier Gui/Adding_Product.png)
![Adding_product_Bar_Code_OK.png](./GUIpngs/Cashier Gui/Adding_product_Bar_Code_OK.png)
![Adding_product_Bar_Code_Invalid.png](./GUIpngs/Cashier Gui/Adding_product_Bar_Code_Invalid.png)
### End Transaction with Credit Card (FR9.1.6)
![End_Transaction_Credit_Card.png](./GUIpngs/Cashier Gui/End_Transaction_Credit_Card.png)
![End_Transaction_Credit_Card_Payment_Done.png](./GUIpngs/Cashier Gui/End_Transaction_Credit_Card_Payment_Done.png)
![End_Transaction_Credit_Card_Error.png](./GUIpngs/Cashier Gui/End_Transaction_Credit_Card_Error.png)
### End Transaction with Cash (FR9.1.5)
![End_transaction_Cash.png](./GUIpngs/Cashier Gui/End_transaction_Cash.png)
![End_Transaction_Cash_Charge_calculated.png](./GUIpngs/Cashier Gui/End_Transaction_Cash_Charge_calculated.png)
![End_Transaction_Cash_Completed.png](./GUIpngs/Cashier Gui/End_Transaction_Cash_Completed.png)
### Discarding Transaction (FR9.1.7)
![Discard_Transaction.png](./GUIpngs/Cashier Gui/Discard_Transaction.png)

# Accountant Interface

### Main page (FR4)
![Balance.png](./GUIpngs/AccountantPNG/Balance.png)
### Description of each transaction (FR4.3 and FR4.4)
![ViewTransaction_Cash.png](./GUIpngs/AccountantPNG/ViewTransaction_Cash.png)
![ViewTransaction_CreditCard.png](./GUIpngs/AccountantPNG/ViewTransaction_CreditCard.png)
![ViewTransaction_Order.png](./GUIpngs/AccountantPNG/ViewTransaction_Order.png)
### Generate Balance (FR4.5)
![Generate_Balance.png](./GUIpngs/AccountantPNG/Generate_Balance.png)
### Manage expenses (FR4.2)
![RECORD_EXPENSES.png](./GUIpngs/AccountantPNG/RECORD_EXPENSES.png)
![ADD_EXPENSE.png](./GUIpngs/AccountantPNG/ADD_EXPENSE.png)
![REMOVE_EXPENSE.png](./GUIpngs/AccountantPNG/REMOVE_EXPENSE.png)

# Inventory Interface

### Manage Inventory (FR3)
![ManageInventory.png](./GUIpngs/Inventory GUI/ManageInventory.png)
#### Add Product/Worktool to Inventory (FR3.1)
![ManageInventory_AddItem.png](./GUIpngs/Inventory GUI/ManageInventory_AddItem.png)
![ManageInventory_AddworkTool.png](./GUIpngs/Inventory GUI/ManageInventory_AddworkTool.png)
#### Modify Item(FR3.1)
![ManageInventoryModifyItem.png](./GUIpngs/Inventory GUI/ManageInventoryModifyItem.png)
#### Modify quantity(FR3.3)
![ManageInventoryModifyquantity.png](./GUIpngs/Inventory GUI/ManageInventoryModifyquantity.png)
#### Remove Item(FR3.2)
![ManageInventory_RemoveItem.png](./GUIpngs/Inventory GUI/ManageInventory_RemoveItem.png)
#### View Description
![ManageInventory_ViewDescription.png](./GUIpngs/Inventory GUI/ManageInventory_ViewDescription.png)

### Manage Supply (FR5 and FR3.7)
![ManageSupply.png](./GUIpngs/Inventory GUI/ManageSupply.png)
#### Set Product's threshold (FR3.7.1)
![ManageSupply_SetThreshold.png](./GUIpngs/Inventory GUI/ManageSupply_SetThreshold.png)
#### Remove Product's threshold (FR3.7.2)
![ManageSupply_RemoveThreshold.png](./GUIpngs/Inventory GUI/ManageSupply_RemoveThreshold.png)
#### Add Supplier (FR5.1)
![ManageSupply_AddSupplier.png](./GUIpngs/Inventory GUI/ManageSupply_AddSupplier.png)
#### Modify Supplier (FR5.1)
![ManageSupply_ModifySupplier.png](./GUIpngs/Inventory GUI/ManageSupply_ModifySupplier.png)
#### View Supplier Description
![ManageSupply_ViewDescription.png](./GUIpngs/Inventory GUI/ManageSupply_ViewDescription.png)
#### Register Order (FR6.4)
![ManageSupply_RequestSupplies.png](./GUIpngs/Inventory GUI/ManageSupply_RequestSupplies.png)
#### View Orders
![ManageSupply_ViewOrders.png](./GUIpngs/Inventory GUI/ManageSupply_ViewOrders.png)

### Manage catalogue (FR3.8)
![ManageCatalogue.png](./GUIpngs/Inventory GUI/ManageCatalogue.png)
#### Add Product to Catalogue (FR3.8.1)
![ManageCatalogue_AddProduct.png](./GUIpngs/Inventory GUI/ManageCatalogue_AddProduct.png)
#### Modify Product (FR3.8.1)
![ManageCatalogue_ModifyProduct.png](./GUIpngs/Inventory GUI/ManageCatalogue_ModifyProduct.png)
#### Remove Product from Catalogue (FR3.8.2)
![ManageCatalogue_RemoveProduct.png](./GUIpngs/Inventory GUI/ManageCatalogue_RemoveProduct.png)
#### View Description
![ManageCatalogue_ViewDescription.png](./GUIpngs/Inventory GUI/ManageCatalogue_ViewDescription.png)






