# NearBuy

**by MAD24_PO4_Team3**

## About NearBuy

NearBuy is a grocery shopping app designed to help users find and purchase groceries from nearby supermarkets conveniently. With personalized accounts, users can securely log in and register. The app includes accessibility features such as double-click and text-to-speech options for visually impaired users, making it easier for everyone to navigate and use the app.

Users can browse various grocery items on the home page and add them to their shopping cart. The navigation bar at the bottom allows seamless switching between the home page, a map showing nearby supermarkets within 1km, and the shopping cart. The map provides detailed information about each supermarket. The shopping cart displays added items, and users can easily add or delete items, with the total price automatically updated. Once ready, users can navigate to the payment type page, choose from various card options like MasterCard and VISA, and save their card details for future use. After selecting a payment type, users proceed to the payment page, where they can review the total price, chosen payment method, and saved address. Confirming the payment empties the cart, allowing users to start a new shopping session.

## Motivation/Objective
NearBuy aims to simplify grocery shopping by connecting users with local supermarkets, making the process efficient and accessible. By providing an easy-to-use platform, NearBuy helps reduce the time and effort spent on grocery shopping. The app's accessibility features ensure inclusivity for visually impaired users, promoting a user-friendly experience for all.

The app also encourages users to shop locally, supporting nearby supermarkets. The integration of a map feature helps users discover and explore supermarkets within their vicinity, enhancing the convenience of shopping. By offering secure login and personalized accounts, NearBuy ensures the safety and privacy of user information. Additionally, the ability to save payment methods and addresses streamlines future purchases, fostering a seamless shopping experience.

## App Category
Shopping

## App Features

NearBuy comes equipped with several helpful features to make your shopping experience smooth and enjoyable. Below is a brief overview of the core features of our app:

**Stage 1**
1. **Feature 1: Login Page** - Leeuwin (_ResponsiveLayout_)
   - SQLite database to store user information
   - Register function for new time users
   - Validation checks to proceed (username and password must exist and must be correct)

2. **Feature 2: Product Browsing Page** - Jovan (_RecyclerView_)
   - A plethora of products to choose from in the home page

3. **Feature 3: Individual Product Pages** - Jovan (_RecyclerView_) & Fariha (_MultiMedia_)
   - Description: [Expand on this feature]

4. **Feature 4: Product Images** - Fariha (_MultiMedia_)
   - Description: [Expand on this feature]

5. **Feature 5: Chat Function** - Nabihah (_Firebase_)
   - Ability to text store Vendor - as a user
   - Ability to text user - as a store Vendor

6. **Feature 6: ShoppingCart & Checkout** - Jovan & Hpone (_Persistent Memory_)
   - RecyclerView for items in the cart (Jovan)
   - Add/Subtract feature for each item in the cart (Jovan)
   - SQLite database for the shopping cart items


**Stage 2**

1. **Feature 7: Double-Click and Text-To-Speech (TTS)** - Fariha (_Custom Gesture Handling & TTS Integration_)
   - Description: [Expand on this feature]
     
   ![NearBuy App Screenshot](https://github.com/IsThisApple/MAD24_PO4_Team3/blob/main/toggle.jpg?raw=true)
    
2. **Feature 8: Shopping Cart** - Nabihah (_Calculations_)
   - Automatic calculation feature to update the total price of items in cart 
   - Proceed to checkout button with validations, there must be at least 1 item in the cart 
   - "Add more Items" feature when the cart is empty
     
   ![NearBuy App Screenshot](https://github.com/IsThisApple/MAD24_PO4_Team3/blob/main/emptycart.jpg?raw=true)
   ![NearBuy App Screenshot](https://github.com/IsThisApple/MAD24_PO4_Team3/blob/main/fullcart.jpg?raw=true)
    
3. **Feature 9: Payment Types** - Nabihah (_RecyclerView in RecyclerView_)
   - Many available card types - MasterCard, VISA, etc..
   - Ability to add card accounts for each card type
   - Validation for all card account information inputs - Card Number, Expiry Dates, CVN, etc.., card accounts must be unique
   - Card accounts saved in SQLite database, synced with login information (accounts can only be seen in the user's chosen profile account)
   - Card accounts automatically updated and added to the PaymentType (CheckOut) page
   - Card accounts can be deleted from the page and database automatically
   - Proceed to Payment button with validations, a card account must be chosen
     
     ![NearBuy App Screenshot](https://github.com/IsThisApple/MAD24_PO4_Team3/blob/main/closedpaymenttypes.jpg?raw=true)
     ![NearBuy App Screenshot](https://github.com/IsThisApple/MAD24_PO4_Team3/blob/main/paymentexpand.jpg?raw=true)
     ![NearBuy App Screenshot](https://github.com/IsThisApple/MAD24_PO4_Team3/blob/main/addcard.jpg?raw=true)
    
4. **Feature 10: Checkout** - Nabihah (_Calculations_)
   - View of updated subtotal and total price
   - View of payment type chosen
   - View of Addresses existing in the system
   - Confirm Payment button with validation, an address must be chosen
     
     ![NearBuy App Screenshot](https://github.com/IsThisApple/MAD24_PO4_Team3/blob/main/payemntpage.jpg?raw=true)
    
5. **Feature 11: Address** - Nabihah (_SQLite & EditTexts_)
   - Validation for address information inputs where necessary - Postal Codes, Unit Number, etc..
   - Addresses saved in SQLite database - users cannot create an address that already exists
   - Addresses automatically added in Payment (CheckOut) page
   - Addresses can be deleted from the page and database automatically
     
     ![NearBuy App Screenshot](https://github.com/IsThisApple/MAD24_PO4_Team3/blob/main/addaddress.jpg?raw=true)

6. **Feature 12: Navigation Bar** - Jovan
   - Description: [Expand on this feature]
  
     ![NearBuy App Screenshot](https://github.com/IsThisApple/MAD24_PO4_Team3/blob/main/navibar.jpg?raw=true)

7. **Feature 13: Map & Location Tracking** - Jovan
   - Map with precise location tracking and button to find nearby supermarkets
   - The button will also show details on the supermarkets nearby in a recyclerview, providing images if its available
   - Another button will be there to hide the recylerview of the places to see the full map
   - Clicking on the places in the recyclerview will show more details on the places
   
    ![Map](https://github.com/user-attachments/assets/3467022d-38dc-458a-828d-2da475876d23)
    ![Specific Places](https://github.com/user-attachments/assets/385146e5-5f32-4725-9040-7ad2b58c3bc3)


## Contributors

We are **MAD24_PO4_Team3**, and here are our team members:

- Jovan Lee
- Nabihah Fadilah
- Fariha Tasnim
- Leeuwin
- Hpone Myint Myat Paing
