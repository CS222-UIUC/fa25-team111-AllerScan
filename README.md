# AllerScan
Nearly 1 in 11 people have food allergies in the United States of America. For those in that group, their allergies control the food they buy, avoid, and consume. Allergens also take away precious time, with 3 to 5 minutes per product on average being used to decide on a purchase based on the allergens from the label. Our app AllerScan is a solution that gives users the comfort of safety in their food purchases, along with giving back the time wasted by staring at food labels.

AllerScan is an Android mobile app that uses food product barcodes to check food labels and informs the user whether a product is safe to consume. AllerScan achieves this by using the mobile phone's built-in camera to scan barcodes, with manual barcode insertion available as needed. When creating a profile, the user will be asked to insert their allergens, which, when a barcode is scanned, will be cross-checked against the food label's listed ingredients. All of this happens within seconds, saving the user minutes of reading and eye strain with a quick response prioritizing user safety above all else. 

But what makes AllerScan special compared to competitors? AllerScan does not have any form of data collection or "middle-man" interaction. This means that the user's data is truly theirs, with everything being stored and processed locally on device. This gives the user the ability to have full control over their data without the possibility of leaks, data selling, or unwanted sharing of user info. AllerScan offers the same abilities as its competitors without intruding on user privacy.

## Technical Architecture

### Overview
AllerScan works by utilizing a local database as a persistent library under SQLite, also known as the Room functionality in Android's Kotlin language. This is used to store the required info to cross-check allergens with the info listed on the Open Food Facts website, which we pull data from using their public API. 

Below is a visual demonstration of how AllerScan functions between different layers of the scanning process.

<p align="center">
  The architectural design:
</p>

<p align="center">
  <img width="657" height="473" alt="image" src="https://github.com/user-attachments/assets/6e73af3d-b8fc-42a2-946c-58fbe92d88b0" />
</p>

The Frontend: The user is prompted to create their profile, which allows AllerScan to compare allergens when scanning food labels. Once the user has created their profile, they can start scanning barcodes. The user can go to the scan page and enable camera permissions to allow AllerScan to scan and interpret barcodes. The end result will be displayed at the bottom of the screen and will be stored in the history page. 

<p align="center">
  The frontend display seen by the user:
</p>

<p align="center">
  <img width="675" height="425" alt="image" src="https://github.com/user-attachments/assets/27eccb17-9863-4db3-93b4-70f40e10120f" />
</p>

The Backend: In the backend, AllerScan uses the Room functionality to create a local database, storing the user profile (including allergens and the user's name) and the history of the user's scans. When the user scans a barcode, AllerScan sends that barcode to the request handler, where it calls the Open Food Facts API to gather information on that specific food product. Product name and ingredients are both pulled from the website, and the ingredients list will be cross-checked with the user's listed allergens from their previously made profile page. Any overlapping ingredients will cause AllerScan to flag the product as unsafe to eat, adding the product and its scan result to the history in Room for display on the user's history page.  

<p align="center">
  Images demonstrating the allergen properties in the database: <br>                         
</p>

<p align="center">
  <img width="323" height="88" alt="image" src="https://github.com/user-attachments/assets/ed75d2c9-4941-4256-9b24-ad0e8077c61e" /> <br>

  <img width="517" height="273" alt="image" src="https://github.com/user-attachments/assets/ad218725-8282-49f7-be79-a9a5bed43ffc" />
</p>


### Welcome Screen - Luke

### Backend + Camera Implementation - Aadam

### History + API Related Features - Anna

### Profile + General UI + Backend Utilization - Rami


## Installation


## Contributors 
[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/19BwrNgF)
