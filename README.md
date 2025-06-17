# 🥗 Food Mitra - Smart Donation Matcher App

**Food Mitra** connects food donors (restaurants, hostels, events) with NGOs in real time using AI, geolocation, and image recognition.

## 📦 Module: Donor Registration & Food Donation

This module allows food donors to register, upload food details with an image, and verify phone numbers via OTP.

---

## 🚀 Features

- 📸 Upload food image (with compression)
- 📍 Add food description and quantity
- 📞 Phone number OTP verification (via Twilio)
- 🗂 Data stored on **Supabase** (Image + Metadata)
- 📡 Realtime updates across devices

---

## 🛠 Tech Stack

| Tech         | Usage                           |
|--------------|---------------------------------|
| Kotlin       | Android app using Jetpack Compose |
| Supabase     | Backend (Storage + Postgres DB) |
| Twilio       | OTP verification                |
| Coil         | Image loading and compression   |
| Retrofit     | API communication               |
| MVVM         | Architecture                    |

---

## 📲 Screens

1. **Donor Registration Screen**  
   - Name  
   - Phone Number (with OTP verification)  
   - Upload Image  
   - Description  
   - Submit

2. **OTP Verification**  
   - Enter 6-digit OTP  
   - Validate using Twilio API

3. **Confirmation & Thank You Screen**

---

## 🧪 How it Works

1. User enters **name** and **phone number**.
2. OTP is sent via Twilio.
3. Upon successful OTP verification, user uploads:
   - Food image
   - Description
4. Data is uploaded to Supabase:
   - Image stored in Supabase Storage.
   - Metadata (name, phone, description, image URL) stored in Postgres table.

---

## 📂 Supabase Structure

- **Bucket**: `donations`
- **Table**: `donors`
  | Column      | Type     |
  |-------------|----------|
  | id          | UUID     |
  | name        | Text     |
  | phone       | Text     |
  | description | Text     |
  | image_url   | Text     |
  | timestamp   | Timestamp |

---

## ✅ Future Enhancements

- NGO dashboard to view nearby donations
- Pickup request and live tracking
- Impact score for donors
- Multi-language support

---

## 👨‍💻 Author

Made with ❤️ by **Abhishek Roushan**  
[GitHub Profile](https://github.com/Abhi95081)

---

## 📸 Screenshots (Add images if needed)

