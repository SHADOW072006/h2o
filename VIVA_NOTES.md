# CyberShield - Viva Voce Questions & Answers

This document contains 20 comprehensive viva questions and answers based strictly on the actual codebase implementation of the CyberShield project (`D:/h2o`).

---

### Q1: What is the purpose of this project and what is its current code status?
**Answer:**
CyberShield is a complete, runnable Android cybersecurity companion application developed in Java using AndroidX and Material Design 3. The codebase implements:
1. Real-time Password Strength Evaluation & Password Generator.
2. Heuristic URL Safety & Phishing Scanner.
3. Interactive Cyber Awareness Quiz with instant feedback.
4. Security Tips & Threat Alerts feed.
5. Overall Security Health Score calculated across all modules and persisted via `SharedPreferences`.

---

### Q2: Which Activity serves as the entry point of the app, and how is it launched?
**Answer:**
`MainActivity` (`com.example.h2o.MainActivity`) is the entry point. It is declared in `AndroidManifest.xml` with an `<intent-filter>` containing:
- `<action android:name="android.intent.action.MAIN" />`
- `<category android:name="android.intent.category.LAUNCHER" />`

---

### Q3: How is Password Strength Evaluation implemented in the codebase?
**Answer:**
In `PasswordEvaluator.java`, passwords are evaluated against:
- Length: Minimum 8 characters (+20 pts), Recommended 12+ (+15 pts), 16+ (+10 pts).
- Character Diversity: Uppercase, Lowercase, Numerical digits, and Special characters.
- Pattern Analysis: Checks against a blacklist of common passwords and repeated character patterns.
- Entropy & Crack Time: Calculates Shannon entropy bits ($E = L \times \log_2(R)$) and categorizes strength into Very Weak, Weak, Moderate, Strong, or Very Strong.

---

### Q4: How is URL / Website Safety Analysis performed in the code?
**Answer:**
In `UrlSafetyAnalyzer.java`, links are parsed using `java.net.URI` and analyzed across multiple heuristics:
1. **Protocol**: Verifies secure `https://` vs unencrypted `http://`.
2. **Host checks**: Flags raw IPv4 addresses, high-risk TLDs (e.g. `.tk`, `.xyz`, `.top`), excessive subdomain depth (>4 levels), and brand spoofing (e.g., `paypal` in non-official domains).
3. **Path & Query**: Detects phishing trigger words (`login`, `verify`, `update`) and dangerous executable extensions (`.apk`, `.exe`, `.bat`).
4. **Verdict**: Categorizes risk into `SAFE`, `SUSPICIOUS`, or `DANGEROUS`.

---

### Q5: What is the formula and weight breakdown for the Security Score?
**Answer:**
In `SecurityScoreManager.java`, the Overall Security Score (0 to 100) is calculated using a weighted average:
$$\text{Overall Score} = (0.30 \times \text{Password}) + (0.25 \times \text{URL}) + (0.25 \times \text{Quiz}) + (0.20 \times \text{Hygiene})$$
- 85–100: Excellent (Green)
- 70–84: Good (Blue)
- 50–69: Moderate (Orange)
- 0–49: Needs Attention (Red)

---

### Q6: How is user data stored in this application?
**Answer:**
User scores and evaluation history are stored locally using Android `SharedPreferences` in `SecurityScoreManager.java`. Importantly, evaluated passwords are never saved or stored, guaranteeing user privacy.

---

### Q7: What permissions are declared in `AndroidManifest.xml` and why?
**Answer:**
`android.permission.INTERNET` is declared in `AndroidManifest.xml` to allow network connectivity for checking link destinations and future cloud threat intelligence API queries.

---

### Q8: What are the `minSdk` and `targetSdk` versions specified in `app/build.gradle.kts`?
**Answer:**
- **`minSdk`**: `24` (Android 7.0 Nougat), ensuring broad device compatibility.
- **`targetSdk`**: `36` (Android 15/16), ensuring compatibility with the latest Android platform behaviors.

---

### Q9: What third-party and AndroidX dependencies are included in `app/build.gradle.kts`?
**Answer:**
- `androidx.appcompat:appcompat:1.6.1`
- `com.google.android.material:material:1.10.0` (Material 3 components like BottomNavigationView, CardView, LinearProgressIndicator)
- `androidx.activity:activity-ktx:1.8.0` (EdgeToEdge support)
- `androidx.constraintlayout:constraintlayout:2.1.4`
- Testing: `junit:junit:4.13.2`, `androidx.test.ext:junit:1.1.5`, `androidx.test.espresso:espresso-core:3.5.1`

---

### Q10: What is the package name and theme used by the app?
**Answer:**
- **Package Name**: `com.example.h2o`
- **App Theme**: `@style/Theme.H2o`, inheriting from `Theme.Material3.DayNight.NoActionBar`.

---

### Q11: What is Edge-to-Edge display and how is it implemented in `MainActivity.java`?
**Answer:**
Edge-to-Edge allows the app to lay out behind system status and navigation bars. In `MainActivity.java`:
```java
EdgeToEdge.enable(this);
ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
    return insets;
});
```
This ensures content is not obstructed by the system status bar, camera cutouts, or gesture navigation bar.

---

### Q12: What does `android:exported="true"` mean in `AndroidManifest.xml`?
**Answer:**
`android:exported="true"` explicitly allows external components (such as the Android system OS launcher) to launch `MainActivity`. Android 12 (API level 31) and higher requires `android:exported` to be explicitly defined for any activity with intent filters.

---

### Q13: What is `android:windowSoftInputMode="adjustResize"` used for?
**Answer:**
It configures the activity window so that when the soft keyboard is displayed, the UI resizes dynamically rather than panning or hiding input fields, maintaining smooth accessibility when entering passwords or URLs.

---

### Q14: What are `dataExtractionRules` and `fullBackupContent` in `AndroidManifest.xml`?
**Answer:**
- `android:dataExtractionRules`: References `@xml/data_extraction_rules` for Android 12+ cloud and device-to-device backup controls.
- `android:fullBackupContent`: References `@xml/backup_rules` for Android 11 and lower auto-backup configuration.

---

### Q15: How are unit tests structured in this project?
**Answer:**
Local JVM unit tests reside in `app/src/test/java/com/example/h2o/ExampleUnitTest.java` and execute with `./gradlew testDebugUnitTest`. They validate:
1. Weak and strong password evaluation logic.
2. Secure random password generator entropy and length.
3. URL safety detection across safe, phishing, and IP-based malicious URLs.

---

### Q16: How does the Password Generator work?
**Answer:**
In `PasswordEvaluator.generateStrongPassword(int length)`:
- Uses `java.security.SecureRandom` for cryptographic unpredictability.
- Guarantees at least one uppercase, one lowercase, one digit, and one special character.
- Fills remaining characters from a combined pool, then performs a Fisher-Yates shuffle.

---

### Q17: How does the Cyber Awareness Quiz operate?
**Answer:**
- Managed via `QuizRepository.java` which holds multiple-choice scenario questions (e.g. phishing emails, 2FA methods, public Wi-Fi risks).
- `MainActivity` tracks progress, provides immediate visual feedback (correct in green, wrong in red), explains the answer, and updates the user's overall Security Score.

---

### Q18: How does the Tips & Threat Alerts section work?
**Answer:**
- Defined in `SecurityTip.java` and `SecurityAlert.java`.
- Allows toggling between best practice hygiene tips and real-world scam warnings (Smishing, QR code quishing, AI voice cloning) with severity tags and specific countermeasures.

---

### Q19: How are UI tabs switched in `MainActivity.java`?
**Answer:**
The layout includes 5 modular views (`view_dashboard.xml`, `view_password.xml`, `view_url_scan.xml`, `view_quiz.xml`, `view_tips_alerts.xml`). Tapping items in `BottomNavigationView` triggers `showTab()` which toggles `View.VISIBLE` and `View.GONE`, preserving user input and state seamlessly without reloading fragments.

---

### Q20: What security best practices are strictly enforced in this app?
**Answer:**
1. **In-Memory Password Processing**: Never log, store, or transmit user-entered passwords.
2. **Cryptographically Secure Randomness**: Uses `SecureRandom` instead of pseudo-random `Math.random()`.
3. **Defense Against Phishing**: Analyzes URL obfuscation techniques (embedded `@`, deceptive TLDs, IP hosts).
4. **Least Privilege Principle**: Only requests `INTERNET` permission.
