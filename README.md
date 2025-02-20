# GymMembership

## **1. Project Overview**

**Project Name:** Gym Membership App  
**Purpose:** A mobile application that allows users to **register**, **log in**, **view** and **renew** memberships, and **make payments** for membership plans.

---

## **2. Technologies & Tools**

1. **Programming Language**  
   - **Kotlin** (using **Jetpack Compose** for UI).

2. **IDE**  
   - **Android Studio** (Arctic Fox or later), with **Gradle** for build automation.

3. **Minimum & Target SDK**  
   - **minSdkVersion**: 24 (Android 7.0)  
   - **targetSdkVersion**: (latest stable, e.g., 33)

4. **Architecture Pattern**  
   - **MVVM (Model-View-ViewModel)**  
   - **Repository** pattern for data handling (local and remote).

---

## **3. Key Dependencies**

1. **Jetpack Compose**  
   - Modern declarative UI for Android.

2. **Hilt (Dagger-Hilt)**  
   - For **Dependency Injection**, simplifying the creation/provision of repositories, viewmodels, etc.

3. **Retrofit + OkHttp**  
   - For **networking** and REST API calls.

4. **Room (Android Jetpack)**  
   - For **local database** storing user sessions (`UserSession` entity).

5. **Coroutines**  
   - For **asynchronous** tasks (network, database) in a clean, non-blocking way.

6. **Kotlinx DateTime** (optional)  
   - If used for date/time arithmetic in some parts.

---

## **4. Project Structure**

A simplified look at the **package** and **file organization**:

```
my.edu.utem.gymmembership
 ├── GymMembershipApp.kt           // @HiltAndroidApp
 ├── MainActivity.kt               // @AndroidEntryPoint
 ├── data/
 │   ├── local/
 │   │   ├── dao/
 │   │   ├── entity/
 │   │   └── AppDatabase.kt
 │   ├── remote/
 │   │   ├── model/                // Data classes: Membership, Payment, Plan, etc.
 │   │   └── network/              // Retrofit clients, interceptors
 │   └── repository/               // Repositories (Auth, Membership, Payment, Plan, UserSession)
 ├── di/
 │   └── NetworkModule.kt          // Hilt module for API & DB
 ├── ui/
 │   ├── components/               // Reusable composables (dialogs, swipe-to-reveal, etc.)
 │   ├── navigation/               // Screen.kt or NavGraph
 │   └── screen/                   // Login, Membership, Payment, PlanDetail, etc.
 ├── utils/
 │   └── DateUtils.kt              // Utility for date/time formatting
 └── viewmodel/
     ├── AuthViewModel.kt
     ├── MembershipViewModel.kt
     ├── PaymentViewModel.kt
     └── PlanViewModel.kt
```

---

## **5. Local Database**

- **Room**:  
  - Stores **user sessions** (`UserSession` table) for the currently logged-in user (JWT token, userId, role).
  - Handled by **`UserSessionDao.kt`** and **`UserSessionRepository.kt`**.

---

## **6. Backend & API**

The **backend** for this mobile app is located at:  
> **[https://github.com/mizannoor/MSMD5163-GYM-MEMBERSHIP](https://github.com/mizannoor/MSMD5163-GYM-MEMBERSHIP)**

It includes **Node.js** with **Express** server code, using JWT for authentication. The mobile app communicates with endpoints such as:

- **Auth**  
  - `POST /login` – user login  
  - `POST /register` – user registration

- **Membership**  
  - `GET /memberships` – fetch memberships  
  - `POST /memberships` – create membership  
  - `PUT /memberships/{id}` – update membership (e.g., status)  
  - `DELETE /memberships/{id}` – delete membership

- **Plan**  
  - `GET /plans` – fetch plan list  
  - `GET /plans/{id}` – fetch single plan  
  - `DELETE /plans/{id}` – delete plan

- **Payment**  
  - `GET /payments` – fetch payments  
  - `POST /payments` – create payment  
  - `PUT /payments/{id}` – update payment status  
  - `DELETE /payments/{id}` – delete payment

**JWT** is passed in the **Authorization header** for protected endpoints, handled by **`AuthInterceptor.kt`**.

---

## **7. Application Flow**

1. **User Login**  
   - On **LoginScreen**, user enters credentials.  
   - On success, token & user info saved in Room (`UserSession`).

2. **Membership Management**  
   - **MembershipScreen** fetches memberships from the API.  
   - Allows **renew** or **delete** membership (swipe-to-delete).  
   - Renew triggers plan selection → user picks plan → goes to PlanDetail → handles payment.

3. **Plan & Payment**  
   - **PlanDetailScreen** shows plan info and a **Pay** button.  
   - User picks a payment method in a popup, a new membership is created with status `"Inactive"`.  
   - A payment is created with status `"Pending"`.  
   - Payment is updated to `"Paid"`.  
   - Finally, membership is updated to `"Active"`.  
   - The app shows a success popup and navigates back to the membership list.

4. **Logout**  
   - Clears local user session from Room DB.

---

## **8. Screenshots**

The attached screenshots show:
1. **Login** screen with email/password fields.  
![Login Screen](images/login_screen.png)

2. **Membership** screen listing memberships (with status, start/end dates).  
![Membership List](images/membership_list.png)

3. **Swipe** to delete membership.  
![Remove Membership](images/remove_membership.png)

4. **Plan selection** popup.  
![Select Plan](images/select_renew_plan.png)

5. **Payment** method selection (radio buttons).  
![Payment Methods](images/payment_methods.png)

6. **Loading overlay** with various steps (authorization, waiting, finalizing).  
![Processing Payment](images/payment_processing.png)

7. **Success** popup after payment.
![Processing Success](images/payment_success_notify.png)

8. **Navigation drawer** with different sections (Membership, Plans, Payments, Logout).
![Navigation](images/drawer_menu.png)

---

## **9. Conclusion**

The **Gym Membership App** is a Kotlin-based Android application using **Jetpack Compose** for UI, **MVVM** architecture with **Hilt** for DI, **Retrofit** for networking, and **Room** for local data storage. It integrates with a **Node.js** backend (source: [https://github.com/mizannoor/MSMD5163-GYM-MEMBERSHIP](https://github.com/mizannoor/MSMD5163-GYM-MEMBERSHIP)) via RESTful endpoints, handling user authentication, membership management, and payment flows.

The final product:
- **Enables** secure login/registration with JWT.  
- **Manages** membership creation, renewal, and deletion.  
- **Processes** payments with status updates from `"Pending"` to `"Paid"`.  
- **Demonstrates** modern Android best practices for a robust and user-friendly experience.