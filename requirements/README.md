## Version 1

### **T7E-1: User Authentication**

**T7S-1: Register a new user**
*   **Priority:** Must Have
*   **Estimated Effort:** 1 day
*   **Type:** Functional
*   **Description:** A new user must be able to register for an account by providing a unique username and a password with at least 6 characters. The system must verify the username is not already in use and store the user's credentials securely.

**T7S-2: Log in as a user**
*   **Priority:** Must Have
*   **Estimated Effort:** 0.5 days
*   **Type:** Functional
*   **Description:** A registered user must be able to log in by providing their username and password. The system must validate the credentials and maintain the user's session.

**T7S-3: Create an admin user**
*   **Priority:** Must Have
*   **Estimated Effort:** 0.5 days
*   **Type:** Functional
*   **Description:** An existing admin must be able to transform a registered user into an admin user through a user interface. The system must update the user's role to admin and maintain this information in the database.

**T7S-4: Log in as an admin**
*   **Priority:** Must Have
*   **Estimated Effort:** 0.25 days
*   **Type:** Functional
*   **Description:** An admin user must be able to log in with the same process as regular users. After login, the system must provide access to admin-specific features.

**T7S-5: Log out**
*   **Priority:** Must Have
*   **Estimated Effort:** 0.25 days
*   **Type:** Functional
*   **Description:** A logged-in user must have a "Logout" button available. Clicking this button must terminate their session and redirect them to the main inventory page. If the user subsequently attempts to access any page that requires authentication, the system must prompt for login.

**T7S-6: Create the initial admin account**
*   **Priority:** Must Have
*   **Estimated Effort:** 0.25 days
*   **Type:** Functional
*   **Description:** The system must provide a secure, one-time mechanism to create the first administrative user. This process must be clearly documented for initial system setup.

---

### **T7E-2: Inventory Management**

**T7S-7: Display the inventory list**
*   **Priority:** Must Have
*   **Estimated Effort:** 1 day
*   **Type:** Functional
*   **Description:** The system must display all available inventory items sorted by price from highest to lowest. Each item listing represents a unique, single-quantity item. Each item must show its name, price (formatted with $ sign, commas for thousands separators, and two decimal points), brief description, and at least one picture. Items that have been sold must not appear in the list.

**T7S-8: Search the inventory**
*   **Priority:** Must Have
*   **Estimated Effort:** 1 day
*   **Type:** Functional
*   **Description:** Users must be able to search inventory by typing words into a search box. The system must match the search terms against item names and descriptions and display matching results in the same format as the regular inventory list. Search results must only include available (unsold) items; sold items must not appear in search.

**T7S-9: Store currency values correctly**
*   **Priority:** Must Have
*   **Estimated Effort:** 0.25 days
*   **Type:** Functional
*   **Description:** The system must store and calculate all monetary values with perfect precision, ensuring there are no rounding errors in financial calculations like subtotals, taxes, or totals.

**T7S-10: Handle initial empty inventory state**
*   **Priority:** Must Have
*   **Estimated Effort:** 0.25 days
*   **Type:** Functional
*   **Description:** The system must gracefully handle an initial empty inventory state when the store is first set up, displaying an appropriate message to users when no inventory items exist.

---

### **T7E-3: Shopping Cart**

**T7S-11: Add an item to the cart**
*   **Priority:** Must Have
*   **Estimated Effort:** 0.5 days
*   **Type:** Functional
*   **Description:** The system must provide an "Add to Cart" button for each inventory item. Since each item is unique, adding it to the cart reserves that specific item for the user. The system must allow users to add multiple distinct items to their cart.

**T7S-12: View and manage the shopping cart**
*   **Priority:** Must Have
*   **Estimated Effort:** 1 day
*   **Type:** Functional
*   **Description:** The system must provide a way for users to view the contents of their shopping cart, showing each item's name and price. A subtotal of all items in the cart must be displayed. The cart page must include a "Checkout" button that starts the checkout process and a "Return to Shopping" option to go back without checking out.

**T7S-13: Remove an item from the cart**
*   **Priority:** Must Have
*   **Estimated Effort:** 0.5 days
*   **Type:** Functional
*   **Description:** The system must allow users to remove items from their shopping cart. If all items are removed, the system must automatically redirect the user to the main inventory page.

**T7S-14: Enable the checkout button**
*   **Priority:** Must Have
*   **Estimated Effort:** 0.25 days
*   **Type:** Functional
*   **Description:** The system must display a Checkout button that is disabled when the cart is empty and enabled when the cart contains items.

**T7S-15: Access the cart from anywhere**
*   **Priority:** Must Have
*   **Estimated Effort:** 0.5 days
*   **Type:** Functional
*   **Description:** A persistent shopping cart icon showing the number of items in the cart must be visible in the application header. Clicking this icon takes the user to the shopping cart view page.

**T7S-16: Persist cart contents**
*   **Priority:** Must Have
*   **Estimated Effort:** 0.5 days
*   **Type:** Functional
*   **Description:** The system must preserve the contents of a user's shopping cart if they navigate away from the checkout process without completing their order, allowing them to return and complete the purchase later.

---

### **T7E-4: Checkout Process**

**T7S-17: Collect shipping information**
*   **Priority:** Must Have
*   **Estimated Effort:** 1 day
*   **Type:** Functional
*   **Description:** During checkout, the system must collect the user's shipping address, phone number, and preferred shipping speed (Overnight: $29, 3-Day: $19, Ground: $0). All fields must be required and validated.

**T7S-18: Collect payment information**
*   **Priority:** Must Have
*   **Estimated Effort:** 1 day
*   **Type:** Functional
*   **Description:** During checkout, the system must collect the user's credit card number, expiration month/year, and CVV security code. All payment fields must be required and validated for correct format. After valid payment details are entered (along with required shipping information), the user must click "Confirm Order" to proceed to the order confirmation page.

**T7S-19: Allow return to shopping**
*   **Priority:** Must Have
*   **Estimated Effort:** 0.25 days
*   **Type:** Functional
*   **Description:** The checkout page must provide a button allowing users to return to shopping without completing their purchase, preserving their cart contents.

**T7S-41: Require login to complete checkout**
*   **Priority:** Must Have
*   **Estimated Effort:** 0.25 days
*   **Type:** Functional
*   **Description:** The system must require the user to be logged in to start checkout and must associate the shopping cart and all completed orders with that user’s account.

**T7S-44: Protect payment data**
*   **Priority:** Must Have
*   **Estimated Effort:** 0.5 days
*   **Type:** Non-Functional
*   **Description:** The system must not store full credit card numbers or CVV, must mask card inputs on screen, must transmit payment data only over secure connections, and must persist only the last four digits for display on receipts.

---

### **T7E-5: Order Processing**

**T7S-20: Calculate the order total**
*   **Priority:** Must Have
*   **Estimated Effort:** 0.5 days
*   **Type:** Functional
*   **Description:** The system must calculate and display the order total, including the subtotal of items, sales tax at 6% of the subtotal, and the selected shipping cost.

**T7S-21: Display the order confirmation page**
*   **Priority:** Must Have
*   **Estimated Effort:** 0.5 days
*   **Type:** Functional
*   **Description:** The system must display a confirmation page showing a list of items being purchased (displaying name and price only for each item), the subtotal, tax, shipping cost, and grand total. The page must include a "Complete Order" button and options to return to the checkout or main pages.

**T7S-22: Process the order**
*   **Priority:** Must Have
*   **Estimated Effort:** 1 day
*   **Type:** Functional
*   **Description:** When the user clicks "Complete Order," the system will simulate a successful payment transaction. It will then mark all purchased items as sold, removing these unique items permanently from the available inventory view but preserving them for sales reports.

**T7S-23: Generate a purchase receipt**
*   **Priority:** Must Have
*   **Estimated Effort:** 0.5 days
*   **Type:** Functional
*   **Description:** After processing an order, the system must display a receipt showing all purchased items, the subtotal, tax, shipping cost, grand total, the last four digits of the credit card, and the shipping address.

**T7S-42: Verify item availability at checkout**
*   **Priority:** Must Have
*   **Estimated Effort:** 0.5 days
*   **Type:** Functional
*   **Description:** When the user views the order confirmation or clicks "Complete Order," the system must verify that each item in the cart is still available. Unavailable items must be removed from the cart with a clear message, and totals must be recalculated before proceeding.

**T7S-25: Exit the receipt screen**
*   **Priority:** Must Have
*   **Estimated Effort:** 0.25 days
*   **Type:** Functional
*   **Description:** The receipt page must include an OK button that returns the user to the main inventory page. After this point, the user cannot return to checkout as the cart is empty.

**T7S-26: Update sales report immediately**
*   **Priority:** Must Have
*   **Estimated Effort:** 0.25 days
*   **Type:** Functional
*   **Description:** When an order is completed, the purchased items must immediately become visible in the admin sales report. The system must ensure real-time synchronization between completed orders and the sales report data.

**T7S-43: Apply currency rounding rules**
*   **Priority:** Must Have
*   **Estimated Effort:** 0.25 days
*   **Type:** Non-Functional
*   **Description:** The system must compute monetary values using base-10 decimal arithmetic and round tax and totals to the nearest cent using round half up. Example: subtotal $19.99, tax at 6% = $1.1994, rounded to $1.20.

---

### **T7E-6: Admin Functions**

**T7S-27: View the sales report**
*   **Priority:** Must Have
*   **Estimated Effort:** 1.5 days
*   **Type:** Functional
*   **Description:** The system must provide an admin interface that displays a report of all sold inventory items, including purchaser information (purchaser's full name, shipping address, and email address). Each item in the report must be clickable to view the full receipt.

**T7S-28: Export the sales report**
*   **Priority:** Must Have
*   **Estimated Effort:** 1 day
*   **Type:** Functional
*   **Description:** The system must provide functionality for admins to export the sales report data to a CSV format for analysis in external tools like Excel.

**T7S-29: Add new inventory items**
*   **Priority:** Must Have
*   **Estimated Effort:** 2 days
*   **Type:** Functional
*   **Description:** The system must provide an interface for admins to add new inventory items, including name, price, description, and at least one image.

**T7S-30: Edit an inventory item**
*   **Priority:** Must Have
*   **Estimated Effort:** 1 day
*   **Type:** Functional
*   **Description:** An admin must be able to edit the details (name, description, price, picture) of an existing, unsold inventory item from an admin interface.

**T7S-31: Delete an inventory item**
*   **Priority:** Must Have
*   **Estimated Effort:** 0.5 days
*   **Type:** Functional
*   **Description:** An admin must be able to delete an inventory item that has not yet been sold. The system must prevent the deletion of items that are part of a completed sale.

---

### **T7E-7: User Experience & Design**

**T7S-32: Create high-fidelity mockups**
*   **Priority:** Must Have
*   **Estimated Effort:** 3 days
*   **Type:** Non-Functional
*   **Description:** Before coding begins, the team must create high-fidelity mockups of all application screens and user flows to demonstrate the final appearance and functionality of the application.

**T7S-33: Implement responsive design**
*   **Priority:** Must Have
*   **Estimated Effort:** 2 days
*   **Type:** Non-Functional
*   **Description:** The application interface must be responsive and work across different device sizes, including desktop, tablet, and mobile.

**T7S-34: Ensure intuitive navigation**
*   **Priority:** Must Have
*   **Estimated Effort:** 1 day
*   **Type:** Non-Functional
*   **Description:** The application must provide intuitive navigation with clear paths forward, backward, and to cancel actions. All interactive elements must be clearly labeled and respond appropriately to user interaction.

**T7S-35: Implement clear error handling**
*   **Priority:** Must Have
*   **Estimated Effort:** 1 day
*   **Type:** Non-Functional
*   **Description:** The system must provide clear, user-friendly error messages for all error conditions, including invalid input, system errors, and transaction failures.

**T7S-36: Validate user input**
*   **Priority:** Must Have
*   **Estimated Effort:** 1 day
*   **Type:** Non-Functional
*   **Description:** The system must validate all user inputs, including proper formatting of credit card numbers, phone numbers, and other form fields. When an email address is collected, it must be validated for proper format. Validation errors must be displayed with clear messages.

**T7S-37: Manage user sessions**
*   **Priority:** Must Have
*   **Estimated Effort:** 0.75 days
*   **Type:** Non-Functional
*   **Description:** The system must maintain user sessions to keep users logged in as they navigate between pages, with appropriate timeout handling and security measures.

**T7S-38: Securely store passwords**
*   **Priority:** Must Have
*   **Estimated Effort:** 0.5 days
*   **Type:** Non-Functional
*   **Description:** The system must store user passwords using secure hashing techniques and must not display passwords in plain text at any point.

**T7S-45: Standardize checkout CTA labels**
*   **Priority:** Must Have
*   **Estimated Effort:** 0.25 days
*   **Type:** Non-Functional
*   **Description:** The system must use "Checkout" as the button label from the cart to initiate checkout, "Confirm Order" as the button label on the checkout page to proceed to the order confirmation page, and "Complete Order" as the button label to finalize the purchase, ensuring consistent wording throughout.

## Version 2

### **T7E-2: Inventory Management**

**T7S-40: Support multiple pictures per inventory item**
*   **Priority:** Wants to Have
*   **Estimated Effort:** 1.5 days
*   **Type:** Functional
*   **Description:** The system should support multiple pictures per inventory item, allowing users to view the item from different angles. All items must have at least one picture, but multiple pictures per item is a desired feature for future releases if not feasible in Version 1.

---

### **T7E-5: Order Processing**

**T7S-24: Display the email receipt**
*   **Priority:** Wants to Have
*   **Estimated Effort:** 1 day
*   **Type:** Functional
*   **Description:** The system must generate an email version of the receipt and display it in the browser for the user. The email content must match the on-screen receipt.

---

### **T7E-8: User Account Management**

**T7S-39: View order history**
*   **Priority:** Needs to Have
*   **Estimated Effort:** 1.5 days
*   **Type:** Functional
*   **Description:** A logged-in user must be able to access a "My Orders" page from their account. This page will list all their past orders. Clicking on a specific order will display the full receipt for that transaction.