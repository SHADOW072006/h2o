package com.example.h2o.model;

import java.util.ArrayList;
import java.util.List;

public class SecurityTip {
    private final String title;
    private final String category;
    private final String description;

    public SecurityTip(String title, String category, String description) {
        this.title = title;
        this.category = category;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public static List<SecurityTip> getDefaultTips() {
        List<SecurityTip> tips = new ArrayList<>();
        tips.add(new SecurityTip(
                "Use Unique Passphrases",
                "Passwords",
                "Combine 4 or more random words (e.g., 'correct-horse-battery-staple'). They are easy to remember and exponentially harder for brute-force tools to crack."
        ));
        tips.add(new SecurityTip(
                "Turn On App-Based 2FA",
                "Authentication",
                "Switch from SMS verification to an authenticator app (Google Authenticator, Microsoft Authenticator, or Bitwarden) to avoid SIM-swapping vulnerabilities."
        ));
        tips.add(new SecurityTip(
                "Verify The Sender Before Clicking",
                "Phishing",
                "Examine email headers and sender addresses closely. Phishers use lookalike domains (e.g., paypa1.com or support-apple-id.net) to deceive you."
        ));
        tips.add(new SecurityTip(
                "Avoid Free Public Wi-Fi for Banking",
                "Network",
                "Never access sensitive financial accounts or enter credit card numbers on unencrypted public Wi-Fi networks without an active, trusted VPN."
        ));
        tips.add(new SecurityTip(
                "Keep Software & OS Updated",
                "System",
                "Security patches address newly discovered zero-day exploits. Always install Android system updates and app security patches immediately."
        ));
        return tips;
    }
}
