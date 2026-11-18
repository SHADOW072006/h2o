package com.example.h2o.model;

import java.util.ArrayList;
import java.util.List;

public class SecurityAlert {
    private final String title;
    private final String severity; // High, Critical, Medium
    private final String summary;
    private final String countermeasure;

    public SecurityAlert(String title, String severity, String summary, String countermeasure) {
        this.title = title;
        this.severity = severity;
        this.summary = summary;
        this.countermeasure = countermeasure;
    }

    public String getTitle() {
        return title;
    }

    public String getSeverity() {
        return severity;
    }

    public String getSummary() {
        return summary;
    }

    public String getCountermeasure() {
        return countermeasure;
    }

    public static List<SecurityAlert> getDefaultAlerts() {
        List<SecurityAlert> alerts = new ArrayList<>();
        alerts.add(new SecurityAlert(
                "Postal / Delivery SMS Phishing Surge",
                "Critical",
                "Scammers send fake texts claiming 'Your package has an unpaid customs fee of $2.50. Click here to reschedule.'",
                "Never click links from unexpected delivery SMS. Track items exclusively via the official courier website or app."
        ));
        alerts.add(new SecurityAlert(
                "Malicious QR Code ('Quishing') In Public Spaces",
                "High",
                "Tampered QR codes pasted over parking meters, restaurant menus, or transit hubs redirect users to credential harvesting sites.",
                "Inspect physical stickers before scanning. Preview the destination URL before confirming navigation."
        ));
        alerts.add(new SecurityAlert(
                "AI Voice Cloning & Emergency Scam",
                "High",
                "Fraudsters use short audio clips from social media to synthesize a loved one's voice, claiming an emergency requiring funds.",
                "Establish a secret family emergency passphrase and call back the person directly on their known phone number."
        ));
        alerts.add(new SecurityAlert(
                "Rogue Android Loan & Cleaner Apps",
                "Medium",
                "Sideloaded APKs impersonating phone optimizers request invasive Accessibility and SMS permissions.",
                "Only install applications from Google Play or trusted verified developers. Keep Google Play Protect active."
        ));
        return alerts;
    }
}
