# 🛒 Point of Sale (POS) System in JavaFX

> **Final Project - Object-Oriented Software Development**

A comprehensive Point of Sale (POS) and inventory management system developed with Java and JavaFX. This project applies advanced software design principles, Role-Based Access Control (RBAC), and MVC architecture to simulate a real-world retail environment.

---

## 🚀 Key Features & Business Logic

This system was designed with scalability, data integrity, and User Experience (UX) in mind.

### 🔐 1. Security & Role-Based Access Control (RBAC)
* **Smart Login:** A single entry point that dynamically routes users to their respective modules based on their role.
* **Session Protection:** The system prevents unauthorized access to critical views through global session validation.
* **Failsafe Mechanisms:** Strict business rules prevent the demotion or deletion of the last active Administrator, ensuring the software never becomes locked or inaccessible.

### 📦 2. Inventory Management (CRUD)
* **Soft Delete:** Products and users are never physically removed from memory to preserve the integrity of the sales history; instead, their status is toggled to inactive.
* **Smart Reactivation:** When attempting to create a product/user that already exists but is inactive, the system detects it and seamlessly reactivates it with the new parameters instead of creating a duplicate.
* **Visual Alerts:** Inventory tables calculate reorder points in real-time and provide visual warnings using JavaFX *CellFactories* and *RowFactories*.

### 🛍️ 3. Sales Engine & Shopping Cart
* **Temporary vs. Real Stock:** When items are added to the cart, the system validates availability without altering the global inventory. Physical stock is only deducted once the sale is confirmed and finalized.
* **Dynamic Cart Management:** The cart allows cashiers to add, subtract quantities, or remove products on the fly, automatically recalculating subtotals and the grand total.
* **Receipt Generation:** Upon finalizing a transaction, a monospaced formatted receipt is generated, simulating a real thermal printer output.

### 📊 4. Business Intelligence & Cash Register Cutoff
* **Shift Summary:** Upon logging out, cashiers receive an automatic cutoff summary detailing their transactions and the total revenue collected during their shift.
* **Global Dashboard:** Administrators have access to a financial dashboard displaying transaction volume, total revenue, and a detailed sales log, complete with an "End of Day" closing functionality.

---

## 🏗️ System Architecture

The project strictly follows the **MVC (Model-View-Controller)** design pattern:
* **Models (`model`):** Pure Java classes (`Producto`, `Usuario`, `Venta`, `DetalleVenta`) encapsulated alongside global Managers to handle data lists and `Sesion` state.
* **Views (`resources`):** Graphical User Interfaces designed in `.fxml` files, cleanly separating the visual layer from the business logic.
* **Controllers (`controllers`):** Classes responsible for event handling, input validation (safeguarding against `NumberFormatException` and empty fields), and dynamically updating tables using `ObservableList`.

---

## 🛠️ Tech Stack
* **Language:** Java
* **GUI Framework:** JavaFX (FXML + CSS for styling)
* **Version Control:** Git / GitHub

---

## 📌 Roadmap & Next Steps
Currently, the system runs smoothly using in-memory storage during runtime. Upcoming updates to the project include:
* [ ] **Data Persistence:** Integration with a relational database (SQL) or local file serialization to permanently store users, inventory, and sales history.

---

*Developed as an academic final project for the Object-Oriented Software Development course.*