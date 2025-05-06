# Delivery Management System

A simple desktop application built using **Java Swing** for managing delivery package records. It provides functionality for user authentication, data entry, viewing, searching, deleting, and managing delivery data stored in a **MySQL** database.

---

## 💡 Overview

This system helps in maintaining a record of packages being sent and received, tracking delivery details such as sender/receiver information, address, contact, delivery date, package weight, and type of delivery (e.g., express or normal).

---

## ✅ Key Features

### 1. **Login System**

* A secure login page appears when the application starts.
* Accepts a predefined username and password.
* Grants access only upon correct authentication.

### 2. **Add Delivery**

* User can fill in details such as:

  * Package ID
  * Sender Name
  * Receiver Name
  * Address
  * Contact Number
  * Delivery Date (Format: YYYY-MM-DD)
  * Package Weight (in kilograms)
  * Delivery Type
* After validation, the data is stored in the MySQL database.

### 3. **View Deliveries**

* Opens a new window displaying all delivery records in a table.
* Data is fetched from the MySQL database.
* Includes options to search, refresh, delete, or simulate editing.

### 4. **Search Function**

* Allows searching by package ID, sender, or receiver name from both the main form and the delivery list view.
* Displays matching results.

### 5. **Delete Record**

* Allows users to select a row from the delivery table and delete it.
* Confirms deletion via a dialog box.

### 6. **Clear Form**

* Clears all input fields on the main form to allow fresh entry.

### 7. **Field Navigation**

* Pressing Enter moves the cursor to the next input field.

---

## 🛠️ Technology Stack

* **Frontend:** Java Swing (GUI)
* **Backend:** MySQL
* **Database Connection:** JDBC (Java Database Connectivity)
* **IDE Suggested:** IntelliJ IDEA / Eclipse

---

## 🗃️ Database Configuration

### Database Name: `DeliveryDB`

**Table: `deliveries`**
Contains the following columns:

* `package_id` (Primary Key)
* `sender`
* `receiver`
* `address`
* `contact`
* `delivery_date` (DATE)
* `weight` (DOUBLE)
* `delivery_type`

> Ensure MySQL server is running and accessible.
> Update database credentials in the code (`username`, `password`, and `DB URL`).

---

## 🔐 Default Login Credentials

* **Username:** `delivary`
* **Password:** `delivary12`

---

## 🚀 How to Run

1. **Setup the database:**

   * Create the `DeliveryDB` database.
   * Create the `deliveries` table as described above.

2. **Configure the project:**

   * Ensure Java is installed on your machine.
   * Add the MySQL JDBC driver (`.jar`) to your project libraries.

3. **Launch the App:**

   * Compile and run the `Main.java` file.
   * Enter login credentials.
   * Begin managing delivery records!

---

## ⚠️ Validation & Error Handling

* Form checks for empty fields.
* Validates weight as a number.
* Ensures proper delivery date format.
* Handles database connection errors gracefully.

---

## 📌 Future Improvements (Optional Ideas)

* Implement full edit functionality for delivery records.
* Add export to CSV or PDF.
* Add date picker component for selecting delivery dates.
* Role-based access for admin and standard users.
