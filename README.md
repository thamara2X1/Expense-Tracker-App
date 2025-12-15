# 💰 Expense Tracker

A beautiful and intuitive Android application for tracking your income and expenses with powerful analytics and reporting features.

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://www.android.com/)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## 📱 Screenshots
<img width="150" alt="Screenshot 2025-12-15 at 21 26 10" src="https://github.com/user-attachments/assets/a7d14c6c-7092-493e-8cf9-7f36e03d7bda" />
<img width="150" alt="Screenshot 2025-12-15 at 21 27 16" src="https://github.com/user-attachments/assets/75fd87e8-a620-4139-b1e4-fefb613f8fff" />
<img width="150" alt="Screenshot 2025-12-15 at 21 27 56" src="https://github.com/user-attachments/assets/c1c37bf7-f52b-4ec5-8c6c-518487c26fc1" />
<img width="150" alt="Screenshot 2025-12-15 at 21 28 44" src="https://github.com/user-attachments/assets/f1277848-3a1a-455a-b887-cc43460b5b72" />
<img width="150" alt="Screenshot 2025-12-15 at 21 29 37" src="https://github.com/user-attachments/assets/54dd5cb2-a7ae-47ca-87fb-4ffb0509d581" />


## ✨ Features

### 🏠 Dashboard
- **Real-time Balance Display** - See your total balance at a glance
- **Income & Expense Overview** - Quick summary of your financial status
- **Beautiful Gradient UI** - Modern Material Design interface
- **Quick Action Cards** - Easy access to all features

### 💸 Add Transactions
- **Income & Expense Toggle** - Switch between transaction types
- **Smart Categories** - Different categories for income and expenses
- **Date Tracking** - Automatic date population
- **Input Validation** - Ensures data accuracy
- **Currency Formatting** - Locale-aware display

### 📋 Transaction List
- **Comprehensive View** - All transactions in one place
- **Color-Coded Entries** - Green for income, red for expenses
- **Visual Icons** - Easy identification of transaction types
- **Swipe to Delete** - Remove transactions with confirmation
- **Empty State** - Helpful message when no data exists
- **Real-time Updates** - List refreshes after changes

### 📊 Reports & Analytics
- **Interactive Charts** - Pie and bar charts for visual analysis
- **Expense Breakdown** - Category-wise expense distribution
- **Income Analysis** - Track income sources
- **Key Statistics** - Total, highest, and average calculations
- **Income vs Expenses** - Visual comparison
- **Animated Visualizations** - Smooth chart animations

### ⚙️ Settings
- **Dark Mode** - Toggle between light and dark themes
- **Notifications** - Enable/disable alerts
- **Currency Selection** - Choose from 6 major currencies
- **Data Management** - Clear all data with confirmation
- **About & Help** - App information and usage guide
- **Privacy Policy** - Data security information

## 🛠️ Technologies Used

- **Language:** Java
- **Minimum SDK:** API 21 (Android 5.0 Lollipop)
- **Target SDK:** API 34 (Android 14)
- **Database:** SQLite
- **Architecture:** MVC (Model-View-Controller)
- **UI Components:** 
  - Material Design Components
  - CardView
  - RecyclerView
  - MPAndroidChart (v3.1.0)
- **Storage:** 
  - SQLite for transactions
  - SharedPreferences for settings

## 📦 Libraries & Dependencies

```gradle
dependencies {
    // AndroidX Libraries
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'androidx.cardview:cardview:1.0.0'
    implementation 'androidx.recyclerview:recyclerview:1.3.2'
    
    // Charts & Graphs
    implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'
}
```

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- JDK 8 or later
- Android SDK with API 21+

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/expense-tracker.git
   cd expense-tracker
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned directory
   - Click "OK"

3. **Sync Gradle**
   - Android Studio will automatically sync Gradle
   - Wait for the sync to complete

4. **Add JitPack Repository (if not already added)**
   
   In `settings.gradle`:
   ```gradle
   dependencyResolutionManagement {
       repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
       repositories {
           google()
           mavenCentral()
           maven { url 'https://jitpack.io' }
       }
   }
   ```

5. **Run the app**
   - Connect an Android device or start an emulator
   - Click the "Run" button in Android Studio
   - Select your device/emulator
   - Wait for the app to build and install

## 📁 Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/expensetracker/
│   │   │   ├── SplashActivity.java          # App entry point
│   │   │   ├── DashboardActivity.java       # Main dashboard
│   │   │   ├── AddExpenseActivity.java      # Add transactions
│   │   │   ├── ExpenseListActivity.java     # Transaction list
│   │   │   ├── ExpenseAdapter.java          # RecyclerView adapter
│   │   │   ├── ReportsActivity.java         # Analytics & charts
│   │   │   ├── SettingsActivity.java        # Settings & preferences
│   │   │   ├── DatabaseHelper.java          # SQLite database
│   │   │   └── Expense.java                 # Data model
│   │   │
│   │   ├── res/
│   │   │   ├── layout/                      # XML layouts
│   │   │   ├── drawable/                    # Gradient backgrounds
│   │   │   ├── values/                      # Strings, colors, themes
│   │   │   └── mipmap/                      # App icons
│   │   │
│   │   └── AndroidManifest.xml              # App configuration
│   │
│   └── test/                                # Unit tests
│
└── build.gradle                             # Module-level Gradle
```

## 🎨 Color Scheme

The app uses a beautiful gradient-based color scheme:

- **Primary Gradient:** Purple (`#667eea`) to Deep Purple (`#764ba2`)
- **Income:** Green (`#4CAF50`)
- **Expense:** Red (`#F44336`)
- **Accent Colors:** Blue, Orange, Purple for various features

## 💾 Database Schema

### Expenses Table
| Column | Type | Description |
|--------|------|-------------|
| id | INTEGER | Primary key (auto-increment) |
| user_id | INTEGER | User identifier |
| amount | REAL | Transaction amount (negative for expenses) |
| category | TEXT | Transaction category |
| description | TEXT | Transaction description |
| date | TEXT | Transaction date (YYYY-MM-DD) |

## 🔒 Privacy & Security

- **Local Storage:** All data is stored locally on the device
- **No Internet Required:** App works completely offline
- **No Data Collection:** No user data is collected or shared
- **Secure:** Uses Android's built-in security features

## 🐛 Known Issues

- Dark mode requires app restart for full effect in some screens
- Chart animations may lag on older devices
- Currency symbol doesn't automatically update for all locales

## 🚧 Future Enhancements

- [ ] Cloud backup and sync
- [ ] Multiple user accounts
- [ ] Recurring transactions
- [ ] Budget setting and tracking
- [ ] Monthly/Yearly reports
- [ ] Export to CSV/PDF
- [ ] Biometric authentication
- [ ] Widget support
- [ ] Custom categories
- [ ] Multi-currency support
- [ ] Spending alerts and notifications
- [ ] Receipt photo attachment

### Code Style
- Follow standard Java conventions
- Use meaningful variable and method names
- Add comments for complex logic
- Keep methods focused and concise

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2025 [Vinindu Thamara]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## 👨‍💻 Author

**Vinindu Thamara**
- GitHub: [[@vininduthamara]([https://github.com/vininduthamara](https://github.com/thamara2X1))](https://github.com/thamara2X1)
- Email: vininduvtph@gmail.com
- LinkedIn: [Vinindu Thamara](www.linkedin.com/in/vinindu-thamara)

## 🙏 Acknowledgments

- [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) for beautiful charts
- [Material Design](https://material.io/) for design guidelines
- Android community for inspiration and support

## 📞 Support

If you encounter any issues or have questions:

1. Check the [Issues](https://github.com/yourusername/expense-tracker/issues) page
2. Create a new issue if your problem isn't already listed
3. Email: vininduvtph@gmail.com

## ⭐ Show Your Support

If you find this project useful, please consider giving it a star ⭐ on GitHub!

---

*Last Updated: December 2025*
