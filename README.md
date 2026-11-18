# CyberShield - Android Security & Safety App

## Project Overview
CyberShield is a comprehensive Android cybersecurity companion application (project namespace `com.example.h2o`, app label `CyberShield`). The application empowers users with real-time cybersecurity evaluation tools, interactive risk analysis, and educational resources directly on their mobile device.

---

## Package Name & Folder Structure
- **Package Name**: `com.example.h2o`
- **Application Label**: `CyberShield` (configured in `app/src/main/res/values/strings.xml`)

### Directory Tree
```text
D:/h2o/
├── app/
│   ├── build.gradle.kts               # Module-level Gradle configuration
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml    # Android Manifest with INTERNET permission
│       │   ├── java/com/example/h2o/
│       │   │   ├── MainActivity.java             # Main Activity & Navigation
│       │   │   ├── evaluator/
│       │   │   │   ├── PasswordEvaluator.java   # Entropy & password strength logic
│       │   │   │   ├── UrlSafetyAnalyzer.java   # URL heuristics & phishing scanner
│       │   │   │   └── SecurityScoreManager.java# Composite score & SharedPreferences
│       │   │   ├── model/
│       │   │   │   ├── SecurityAlert.java       # Threat advisory data model
│       │   │   │   └── SecurityTip.java         # Defense tip data model
│       │   │   └── quiz/
│       │   │       ├── QuizQuestion.java        # Quiz model
│       │   │       └── QuizRepository.java      # Cybersecurity questions & explanations
│       │   └── res/
│       │       ├── drawable/                    # Custom security vector icons
│       │       ├── layout/
│       │       │   ├── activity_main.xml        # Root layout with BottomNav & TopBar
│       │       │   ├── view_dashboard.xml       # Security Health Score dashboard
│       │       │   ├── view_password.xml        # Password Evaluator UI
│       │       │   ├── view_url_scan.xml        # URL Safety Scanner UI
│       │       │   ├── view_quiz.xml            # Awareness Quiz UI
│       │       │   └── view_tips_alerts.xml     # Tips & Threat advisories UI
│       │       ├── menu/
│       │       │   └── bottom_nav_menu.xml      # Bottom Navigation menu items
│       │       ├── values/
│       │       │   ├── colors.xml               # Modern security color palette
│       │       │   ├── strings.xml
│       │       │   └── themes.xml
│       │       ├── values-night/
│       │       │   └── themes.xml
│       │       └── xml/
│       │           ├── backup_rules.xml
│       │           └── data_extraction_rules.xml
│       ├── test/java/com/example/h2o/
│       │   └── ExampleUnitTest.java             # Unit tests for password & URL engines
│       └── androidTest/java/com/example/h2o/
│           └── ExampleInstrumentedTest.java     # Android instrumented test stub
├── build.gradle.kts                             # Top-level Gradle configuration
├── settings.gradle.kts                          # Gradle settings
├── gradle/
│   └── libs.versions.toml                       # Gradle Version Catalog
└── README.md                                    # Project Documentation
```

---

## Technical Specifications & Build Configuration

### SDK Versions
- **Minimum SDK (`minSdk`)**: `24` (Android 7.0 Nougat)
- **Target SDK (`targetSdk`)**: `36` (Android 15/16)
- **Compile SDK (`compileSdk`)**: `36` (minor API level 1)
- **Java Compatibility**: `JavaVersion.VERSION_11`

### Dependencies (`app/build.gradle.kts` & `gradle/libs.versions.toml`)
- **AndroidX Activity KTX**: `androidx.activity:activity-ktx:1.8.0`
- **AndroidX AppCompat**: `androidx.appcompat:appcompat:1.6.1`
- **Material Components**: `com.google.android.material:material:1.10.0`
- **ConstraintLayout**: `androidx.constraintlayout:constraintlayout:2.1.4`
- **Testing Dependencies**:
  - `junit:junit:4.13.2`
  - `androidx.test.ext:junit:1.1.5`
  - `androidx.test.espresso:espresso-core:3.5.1`

---

## Android Manifest & Permissions Analysis

### File: `app/src/main/AndroidManifest.xml`
- **Declared Permissions**:
  - `android.permission.INTERNET`: Required for web-based security and URL safety evaluations.
- **Application Configuration**:
  - `android:allowBackup="true"`: Enables app backup via Android backup framework.
  - `android:dataExtractionRules="@xml/data_extraction_rules"`: Specifies rules for cloud backup and device transfer.
  - `android:fullBackupContent="@xml/backup_rules"`: Legacy backup rules configuration.
  - `android:supportsRtl="true"`: Supports Right-To-Left layouts.
  - `android:theme="@style/Theme.H2o"`: Custom Material 3 theme.
- **Declared Activities**:
  - `.MainActivity`:
    - `android:exported="true"`: Entry point for Android launcher.
    - `android:windowSoftInputMode="adjustResize"`: Dynamically resizes UI when soft keyboard appears.
    - Intent Filter: `MAIN` / `LAUNCHER`.

---

## Feature Implementation Status

| Feature | Code Status | Notes / Logic Breakdown |
| :--- | :--- | :--- |
| **Baseline Activity & Layout** | **Implemented** | `MainActivity.java` with Material 3 Bottom Navigation and Edge-to-Edge insets. |
| **Password Strength Evaluation** | **Implemented** | `PasswordEvaluator.java`: Checks length, character sets, patterns, entropy bits, and crack time. |
| **Password Generator & Copy** | **Implemented** | Cryptographically secure random generator (`SecureRandom`) with 1-tap clipboard copying. |
| **URL / Website Safety Analysis** | **Implemented** | `UrlSafetyAnalyzer.java`: Heuristics for HTTPS, raw IP hosts, high-risk TLDs, brand spoofing, and file extensions. |
| **Security Score Calculation** | **Implemented** | `SecurityScoreManager.java`: Weighted composite scoring (Password 30%, URL 25%, Quiz 25%, Hygiene 20%). |
| **Cybersecurity Tips** | **Implemented** | `SecurityTip.java`: Categorized tips for passwords, 2FA, phishing, and Wi-Fi security. |
| **Security Alerts / Threat Feeds** | **Implemented** | `SecurityAlert.java`: Up-to-date threat advisories with severity badges and countermeasures. |
| **Cyber Awareness Quiz** | **Implemented** | `QuizRepository.java`: 5 real-world questions with explanations and instant visual feedback. |
| **Data Storage / Persistence** | **Implemented** | `SharedPreferences` persists user evaluation history and composite security scores. |

---

## Core Feature Logic Specifications

### 1. Password Strength Evaluation (`PasswordEvaluator.java`)
- **Criteria**:
  - Minimum 8 characters & Recommended 12+ characters
  - Character diversity: Uppercase, Lowercase, Digits, Special symbols
  - Pattern check: Checks against common passwords list and repeated character sequences
- **Entropy Calculation**: $E = L \times \log_2(R)$ where $L$ is length and $R$ is character pool size.
- **Strength Bands**: Very Weak (<30), Weak (<50), Moderate (<70), Strong (<88), Very Strong (88-100).
- **Crack Time**: Calculated dynamically from character entropy.

### 2. URL / Website Safety Analysis (`UrlSafetyAnalyzer.java`)
- **Protocol Verification**: Validates HTTPS encryption vs unencrypted HTTP.
- **Host Reputation Heuristics**:
  - Detects raw IPv4 address used in place of domain name.
  - Flags high-risk top-level domains (`.tk`, `.ml`, `.xyz`, `.top`, `.buzz`, etc.).
  - Identifies excessive subdomain nesting and multiple hyphens.
  - Detects brand name impersonation (e.g., `paypal`, `google`, `apple` inside deceptive domains).
- **Path & Query Analysis**:
  - Scans for executable and script extensions (`.apk`, `.exe`, `.bat`).
  - Detects phishing trigger words (`login`, `verify`, `update-account`).
  - Flags embedded `@` credentials obfuscation.
- **Verdicts**: `SAFE`, `SUSPICIOUS`, or `DANGEROUS`.

### 3. Security Score Calculation (`SecurityScoreManager.java`)
- Formula:
  $$\text{Overall Score} = (0.30 \times \text{Password}) + (0.25 \times \text{URL}) + (0.25 \times \text{Quiz}) + (0.20 \times \text{Hygiene})$$
- Bands:
  - 85–100: **Excellent** (Green)
  - 70–84: **Good** (Blue)
  - 50–69: **Moderate** (Orange)
  - 0–49: **Needs Attention** (Red)

### 4. Data Storage & Privacy
- Stores composite metrics locally using Android `SharedPreferences`.
- **Privacy Guarantee**: Passwords evaluated are processed strictly in memory and are **never** logged, persisted, or transmitted over the network.

---

## How to Build and Run
1. Open the project root folder (`D:/h2o`) in Android Studio.
2. Ensure Android SDK 36 and JDK 11 are configured.
3. Sync the Gradle project (`./gradlew sync` or Android Studio Gradle Sync).
4. Run the application on an emulator or physical device running Android 7.0 (API 24) or higher.
5. The app compiles successfully (`./gradlew assembleDebug`), passes unit tests (`./gradlew testDebugUnitTest`), and launches `MainActivity`.
