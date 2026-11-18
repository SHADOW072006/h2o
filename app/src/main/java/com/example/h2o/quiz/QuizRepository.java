package com.example.h2o.quiz;

import java.util.ArrayList;
import java.util.List;

public class QuizRepository {

    public static List<QuizQuestion> getQuestions() {
        List<QuizQuestion> list = new ArrayList<>();

        list.add(new QuizQuestion(
                "You receive an urgent email from your 'bank' asking you to click a link to verify your account immediately. What should you do?",
                List.of(
                        "Click the link and quickly enter your login credentials",
                        "Reply with your password so they know it is you",
                        "Delete the email and independently visit your bank's official website or app",
                        "Forward the email to all your contacts to warn them"
                ),
                2,
                "Urgent account verification emails are classic phishing attacks. Never click links in unexpected emails; always navigate directly to the verified service."
        ));

        list.add(new QuizQuestion(
                "Which of the following is the most secure method for Multi-Factor Authentication (MFA)?",
                List.of(
                        "SMS text message codes",
                        "Authenticator app (TOTP) or Hardware Security Key",
                        "Security questions (e.g., mother's maiden name)",
                        "Email-based verification code"
                ),
                1,
                "SMS can be intercepted via SIM swapping. Authenticator apps and hardware security keys (FIDO2/WebAuthn) provide vastly superior protection."
        ));

        list.add(new QuizQuestion(
                "Why is connecting to an open, public Wi-Fi network at a café risky without a VPN?",
                List.of(
                        "Public Wi-Fi charges high mobile data rates",
                        "Unencrypted traffic can be snooped on via Man-in-the-Middle (MitM) attacks",
                        "Your battery drains 10 times faster",
                        "It automatically deletes your phone's contact list"
                ),
                1,
                "Adversaries on the same unencrypted local network can capture unencrypted packets or set up evil twin rogue access points."
        ));

        list.add(new QuizQuestion(
                "What is the biggest security danger of reusing the same strong password across multiple websites?",
                List.of(
                        "It is too hard to remember",
                        "Credential stuffing: If one site gets breached, attackers test that password on all your other accounts",
                        "Browser caches will corrupt your login",
                        "The password automatically expires every 30 days"
                ),
                1,
                "Data breaches happen frequently. In credential stuffing attacks, automated bots test breached email/password pairs across hundreds of top websites."
        ));

        list.add(new QuizQuestion(
                "What is 'Smishing' in cybersecurity terminology?",
                List.of(
                        "A technique to smash hard drives physically",
                        "Phishing attacks carried out via SMS text messages",
                        "A computer virus that targets sound cards",
                        "A type of denial of service attack on routers"
                ),
                1,
                "Smishing (SMS + Phishing) uses deceptive text messages containing malicious links, such as fake parcel delivery notifications or bank alerts."
        ));

        return list;
    }
}
