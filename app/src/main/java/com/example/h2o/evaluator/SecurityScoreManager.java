package com.example.h2o.evaluator;

import android.content.Context;
import android.content.SharedPreferences;

public class SecurityScoreManager {

    private static final String PREF_NAME = "cybershield_security_prefs";
    private static final String KEY_PASSWORD_SCORE = "key_password_score";
    private static final String KEY_URL_SCORE = "key_url_score";
    private static final String KEY_QUIZ_SCORE = "key_quiz_score";
    private static final String KEY_HYGIENE_SCORE = "key_hygiene_score";

    private final SharedPreferences prefs;

    public SecurityScoreManager(Context context) {
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void updatePasswordScore(int score) {
        prefs.edit().putInt(KEY_PASSWORD_SCORE, Math.max(0, Math.min(100, score))).apply();
    }

    public void updateUrlScore(int safetyScore) {
        prefs.edit().putInt(KEY_URL_SCORE, Math.max(0, Math.min(100, safetyScore))).apply();
    }

    public void updateQuizScore(int quizScore) {
        prefs.edit().putInt(KEY_QUIZ_SCORE, Math.max(0, Math.min(100, quizScore))).apply();
    }

    public void updateHygieneScore(int hygieneScore) {
        prefs.edit().putInt(KEY_HYGIENE_SCORE, Math.max(0, Math.min(100, hygieneScore))).apply();
    }

    public int getPasswordScore() {
        return prefs.getInt(KEY_PASSWORD_SCORE, 60); // default baseline
    }

    public int getUrlScore() {
        return prefs.getInt(KEY_URL_SCORE, 75); // default baseline
    }

    public int getQuizScore() {
        return prefs.getInt(KEY_QUIZ_SCORE, 50); // default baseline
    }

    public int getHygieneScore() {
        return prefs.getInt(KEY_HYGIENE_SCORE, 80); // default baseline
    }

    /**
     * Weighted composite security score (0 - 100):
     * - Password audit: 30%
     * - URL scan safety: 25%
     * - Cybersecurity quiz knowledge: 25%
     * - Device hygiene awareness: 20%
     */
    public int calculateOverallScore() {
        int pwd = getPasswordScore();
        int url = getUrlScore();
        int quiz = getQuizScore();
        int hygiene = getHygieneScore();

        double weighted = (pwd * 0.30) + (url * 0.25) + (quiz * 0.25) + (hygiene * 0.20);
        return (int) Math.round(weighted);
    }

    public String getScoreRating() {
        int score = calculateOverallScore();
        if (score >= 85) return "Excellent";
        if (score >= 70) return "Good";
        if (score >= 50) return "Moderate";
        return "Needs Attention";
    }

    public int getRatingColor() {
        int score = calculateOverallScore();
        if (score >= 85) return 0xFF2E7D32; // Green
        if (score >= 70) return 0xFF0077B6; // Blue
        if (score >= 50) return 0xFFEF6C00; // Orange
        return 0xFFC62828; // Red
    }
}
