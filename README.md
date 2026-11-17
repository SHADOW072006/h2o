CyberShield

An Android app that helps everyday users build better digital security habits.

CyberShield combines practical security tools (password and URL checks) with awareness content (tips, alerts, quizzes) and a personal Security Score, so users can both check what is risky and learn why.

Show Image Show Image Show Image

Table of Contents
Overview
Features
Threats Covered
Security Score
Tech Stack
Getting Started
Project Structure
Screenshots
Privacy and Limitations
Future Scope
Author
License
Overview

Most people know cyber threats exist but struggle to spot them in daily life: a convincing phishing link, an OTP request from a "bank executive", a fake login page. CyberShield puts simple, understandable checks and learning material in one app so users can make safer decisions on their phone.

Features
Feature	What it does
Password Strength Checker	Evaluates a password and explains what makes it weak or strong (length, character variety, common patterns).
Website / URL Safety Analysis	Analyses a URL for common warning signs and reports whether it looks safe or suspicious.
Cybersecurity Awareness Tips	Short, practical tips on staying safe online.
Security Alerts	Notices about common and emerging threats.
Quizzes	Short quizzes that test and reinforce what users have learned.
Security Score	A score that reflects the user's security practices and shows where to improve.
Threats Covered
Phishing: deceptive links and messages that imitate trusted brands
OTP scams: attackers tricking users into sharing one-time passwords
Fake websites: look-alike sites built to steal credentials or payments
Social engineering: manipulation that targets people instead of systems
Security Score

The Security Score gives users one number that reflects their security habits.

TODO: edit this section to match your implementation. Describe exactly what feeds the score, for example: password strength results, URLs checked, quiz performance, tips read. State the range (e.g. 0 to 100) and what each band means.

Tech Stack
Layer	Technology
Language	Java
UI / Layouts	XML
Platform	Android
IDE	Android Studio
Build system	Gradle
Data storage	[e.g. SharedPreferences / SQLite / Firebase, fill in]
URL analysis	[e.g. rule-based heuristics / external API, fill in]
Getting Started
Prerequisites
Android Studio (latest stable recommended)
JDK [version]
An Android device or emulator running Android [min SDK version] or above
Installation
bash
# 1. Clone the repository
git clone https://github.com/<your-username>/CyberShield.git

# 2. Open the project in Android Studio
#    File > Open > select the CyberShield folder

# 3. Let Gradle sync finish

# 4. Run the app on an emulator or a connected device
Project Structure

Adjust folder and package names to match your project.

CyberShield/
├── app/
│   ├── src/main/
│   │   ├── java/<your.package.name>/
│   │   │   ├── ...Activity.java      # Screens (password, URL, quiz, score, etc.)
│   │   │   └── ...                   # Helpers / utilities / models
│   │   ├── res/
│   │   │   ├── layout/               # XML layouts for each screen
│   │   │   ├── values/               # strings, colors, themes
│   │   │   └── drawable/             # icons and images
│   │   └── AndroidManifest.xml
│   └── build.gradle
└── build.gradle
Screenshots

Add screenshots of the main screens here.

Home	Password Checker	URL Analysis	Quiz	Security Score
screenshots/home.png	screenshots/password.png	screenshots/url.png	screenshots/quiz.png	screenshots/score.png
Privacy and Limitations
Password checks should run on the device. Passwords should never be sent to a server or logged.
URL analysis helps flag suspicious links, but no automated check is perfect. A "safe" result is not a guarantee, and a "suspicious" result can occasionally be a false alarm.
CyberShield is an awareness and assistance tool. It does not replace antivirus software or good judgement.
Future Scope
Deeper URL reputation checks using threat-intelligence sources
SMS / notification scanning for phishing and OTP-scam patterns (with user consent)
Breached-password checks using a privacy-preserving method
Multi-language support for wider reach
Progress tracking and badges to encourage better habits
Author

Harshit Prajapat BCA (Cybersecurity), Jaipur National University

License

Choose a license (e.g. MIT) and add a LICENSE file to the repository.
