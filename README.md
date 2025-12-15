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
<p align="center">
<img width="197" height="440" alt="image" src="https://github.com/user-attachments/assets/ecb04aa8-7552-4038-b4fc-b57788063cd4" />
<img width="192" height="437" alt="image" src="https://github.com/user-attachments/assets/b8b811ae-2fe6-405a-9316-6c72653a8c14" />
<img width="196" height="441" alt="image" src="https://github.com/user-attachments/assets/250416f5-dd91-4e6d-8a13-f0d3450679d7" />
</p>
<p align="center">
First&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Second&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Third <br>
</p>

### Backend + Camera Implementation - Aadam

### History + API Related Features - Anna

### Profile/Backend Utilization + General UI/Testing - Rami

#### Profile Page + Backend Utilization:
<p align="center">
<img width="202.5" height="450" alt="image" src="https://github.com/user-attachments/assets/663537c9-cb6f-4d6a-8b56-7bcaa204a024" />
<img width="202.5" height="450" alt="image" src="https://github.com/user-attachments/assets/c3b633a0-d7cd-40fd-b84d-b53f30cfbdca" />
<img width="202.5" height="450" alt="image" src="https://github.com/user-attachments/assets/b6c16a54-a29e-4304-8961-fd2b2a488e2f" />
</p>
<p align="center">
Overview&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Edit&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Custom Allergen <br>
</p>
The Profile page seen above is broken into three sections, with all of it being written in Kotlin and with SQL for accessing the database:

The first page the user will see is the "Overview" page. It displays the user's name, along with their currently active allergens. It pulls the active allergens from the local database when the page is opened so that it can update in real-time after editing a profile.
To edit the profile, the user can press the pen icon in the top right.

The second page is the edit page, where the user can change their listed name, use the checkboxes to easily add major allergens (those seen in the big bold **CONTAINS** sections of labels), choose to add custom allergens, or delete all user data from the app at once.
When entering the edit page, the activated checkboxes will be pre-selected based on their activation status so the user can change their allergens as needed without having to recreate their whole profile. Pressing on a checkbox will change the activation status of that allergen to whatever it currently isn't in the database (so true to false, false to true). Pressing the delete button in the bottom left will set all activation values in the database to false while also clearing out the user's name. The save button on the top right (where the pen icon was) will bring the user back to the previous page, saving all changes.

The last page is where the user can add custom allergens after pressing the "ADD CUSTOM ALLERGEN" button while editing their profile. It works exactly as one would expect. The user types in the allergen in the top bar, and when they press "ADD", the allergen will be added to the database with its activation status set to true. The allergen will also be seen below this bar, showing the user all their current custom allergens, along with giving them the ability to delete custom allergens as needed by pressing the "X" to the right of the allergen. Pressing the "X" will remove the allergen from the screen and set its activation value to false.

Once the profile page is completed and the user has declared their allergens, the database will now contain all the user info needed to cross-check against food labels, enabling the ability to approve or deny the safety of scanned food products.

#### General UI + Testing:

To ensure compatibility with many devices, and as the only developer using an Android device, I was in charge of ensuring the user interface was both clean and compatible with multiple devices. I was also in charge of testing AllerScan outside of the developer environment due to having a developer-enabled phone. 

General UI: Throughout the entire project, I adjusted and changed UI elements as they were created to ensure they were uniform across pages while also adjusting the calculations for object positioning to ensure device compatibility. The calculations are now mostly reliant on the positionings of the screen boundaries, enabling UI elements to shift automatically based on screen size. This way, the app looks the same for all devices, has no UI element being overlapped by another, and shares a uniform theme across all pages, creating a seamless user experience.  

Testing: Testing each branch and PR with my own device made it possible to test certain functionality, like camera barcode scanning, user data persistence, UI element positioning, and real-world use in stores when shopping, all things that cannot be done in the emulator. This was done to ensure that all functions of the app were maintained during development, with none being silently broken due to a PR. Using the app allowed me to create feedback that otherwise could not be seen in the emulator due to its constraints, allowing team members and me to take that info and make changes based on it. Testing in my daily life has drastically changed the project for the better, as I ran into scenarios that none of us thought of during development.

## Installation
There are multiple ways to install the application:
### Method 1 - Install APK from Releases:
In releases, install the APK file.
Once

### Method 2 - Android Studio:
1. The application can also be installed via Android Studio, which can be installed [here](https://developer.android.com/studio). 
2. In Android Studio, you will be prompted to download the Android SDK, which you should accept.
3. Once in Android Studio, pull from main and press "Sync with Gradle" after pressing the top left button, a directory warning should pop up letting you know that it will search for the default SDK location, press OK to continue.
4. On the top right, you should see a hammer icon. This will build the project. Press the hammer icon.
5. Once the project is done building, you can do either of two things.<br>

Option 1. Press the green play button at the top of the screen, which will run the app in the emulator.<br>
Option 2. Plug in an Android device with developer options enabled and USB debugging enabled. If you don't know how to do that, then look to [Google's Developer Guides](https://developer.android.com/studio/debug/dev-options**), as they are quite detailed. Then press the green button to install the app locally on your Android phone.<br>

6. Enjoy AllerScan!   



## Contributors 
[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/19BwrNgF)
