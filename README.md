# Location Tracker App

**Location Tracker** is a simple Android application that records and saves a user’s location on the device. The app works offline and saves location data locally using SQLite database. Users can sign up, sign in, and view their location history.  

---

## Key Features

- Automatically tracks user location when the switch is turned on  
- Saves location (latitude & longitude) every five minutes with timestamp (date & time) and shows a toast notification  
- Works offline (no internet required)  
- User authentication (Sign Up & Sign In)  
- Location history stored locally  
- Displays location history in a list  
- Start and stop location tracking anytime  
- Logged-in user session saved using SharedPreferences  

---

## Technologies Used

- **IDE:** Android Studio  
- **Programming Language:** Java  
- **UI:** XML  
- **Database:** SQLite – for storing users and location data  
- **SharedPreferences:** for login session management  
- **Material Components (Material 3):** for modern UI design  
- **Android Location Services (GPS):** for retrieving user location  

---

## Libraries & APIs

1. **com.google.android.material** – Material3 UI components  
2. **Android Location Services (GPS)** – Get current location  
3. **SQLiteOpenHelper** – Saves user and location data locally  
4. **OpenStreetMap (OSMDroid)** – Display map and location markers  
5. **Nominatim API** – Reverse geocoding (convert latitude & longitude to location name)  
6. **Open-Meteo API** – Fetch current weather information  

---

## Screenshots
<table align="center" cellpadding="10">
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/36c6ad7a-5711-4129-914c-cd2c729b9511" width="250"/><br/>
      <b>Get Started</b>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/69428650-5871-43d9-a66c-2969ad5bf4d7" width="250"/><br/>
      <b>Sign Up</b>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/4be6ae43-9a7f-4a8a-a1fe-fd8ad020d0d8" width="250"/><br/>
      <b>Sign Up (Alt)</b>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/7aae40df-e9b0-419a-afc2-27e7d75b63bf" width="250"/><br/>
      <b>Sign In</b>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/1260f5df-9e34-403b-9cd6-4c6192e2f796" width="250"/><br/>
      <b>Sign In (Alt)</b>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/36f899a3-85be-4d42-abc0-0e9befb265de" width="250"/><br/>
      <b>Home Screen</b>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/94329726-b289-4fe7-b3eb-fad7f517bff4" width="250"/><br/>
      <b>Home Screen (Alt)</b>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/c71629e7-c1bb-44d8-981b-2ddf33748037" width="250"/><br/>
      <b>Location History</b>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/72a54040-d431-4f50-8713-ab3290480531" width="250"/><br/>
      <b>View Details</b>
    </td>
  </tr>
  <tr>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/e88b1f35-51f4-4108-a7cd-dceec200384f" width="250"/><br/>
      <b>View Details (Alt)</b>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/04f5872b-9159-4f50-91f7-cf3f4eef85cb" width="250"/><br/>
      <b>Profile</b>
    </td>
    <td align="center">
      <img src="https://github.com/user-attachments/assets/73f84c01-6eeb-4c1f-8a83-44a3fabb2c16" width="250"/><br/>
      <b>Profile (Alt)</b>
    </td>
  </tr>
</table>

---

## Developed by

**SK. MD. SHAKIB IMRAN**
