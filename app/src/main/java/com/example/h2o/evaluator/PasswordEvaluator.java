package com.example.h2o.evaluator;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class PasswordEvaluator {

    public enum Strength {
        VERY_WEAK("Very Weak", 0xFFC62828),
        WEAK("Weak", 0xFFEF6C00),
        MODERATE("Moderate", 0xFFF9A825),
        STRONG("Strong", 0xFF2E7D32),
        VERY_STRONG("Very Strong", 0xFF00796B);

        private final String label;
        private final int color;

        Strength(String label, int color) {
            this.label = label;
            this.color = color;
        }

        public String getLabel() {
            return label;
        }

        public int getColor() {
            return color;
        }
    }

    public static class Result {
        public final int score; // 0 to 100
        public final Strength strength;
        public final boolean minLength;
        public final boolean recommendedLength;
        public final boolean hasUppercase;
        public final boolean hasLowercase;
        public final boolean hasDigit;
        public final boolean hasSpecial;
        public final boolean noCommonPatterns;
        public final String crackTime;
        public final double entropyBits;
        public final List<String> suggestions;

        public Result(int score, Strength strength, boolean minLength, boolean recommendedLength,
                      boolean hasUppercase, boolean hasLowercase, boolean hasDigit,
                      boolean hasSpecial, boolean noCommonPatterns, String crackTime,
                      double entropyBits, List<String> suggestions) {
            this.score = score;
            this.strength = strength;
            this.minLength = minLength;
            this.recommendedLength = recommendedLength;
            this.hasUppercase = hasUppercase;
            this.hasLowercase = hasLowercase;
            this.hasDigit = hasDigit;
            this.hasSpecial = hasSpecial;
            this.noCommonPatterns = noCommonPatterns;
            this.crackTime = crackTime;
            this.entropyBits = entropyBits;
            this.suggestions = suggestions;
        }
    }

    private static final String[] COMMON_PASSWORDS = {
            "password", "123456", "12345678", "1234", "qwerty", "admin", "welcome",
            "login", "iloveyou", "secret", "pass123", "abc123", "password123", "master"
    };

    public static Result evaluate(String password) {
        if (password == null || password.isEmpty()) {
            return new Result(0, Strength.VERY_WEAK, false, false, false, false,
                    false, false, true, "Instant", 0,
                    List.of("Enter a password to evaluate its strength."));
        }

        int length = password.length();
        boolean minLength = length >= 8;
        boolean recommendedLength = length >= 12;

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (int i = 0; i < length; i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else {
                hasSpecial = true;
            }
        }

        // Pattern checking
        String lowerPwd = password.toLowerCase();
        boolean commonFound = false;
        for (String common : COMMON_PASSWORDS) {
            if (lowerPwd.contains(common)) {
                commonFound = true;
                break;
            }
        }

        // Repeated characters test (e.g. "aaaaa")
        boolean repeatedPattern = password.matches(".*(.)\\1{2,}.*");
        boolean noCommonPatterns = !commonFound && !repeatedPattern;

        // Scoring logic (0 - 100)
        int rawScore = 0;
        if (minLength) rawScore += 20;
        if (recommendedLength) rawScore += 15;
        if (length >= 16) rawScore += 10;
        if (hasUpper) rawScore += 12;
        if (hasLower) rawScore += 12;
        if (hasDigit) rawScore += 12;
        if (hasSpecial) rawScore += 15;
        if (noCommonPatterns) rawScore += 10;
        else rawScore = Math.max(10, rawScore - 25);

        int poolSize = 0;
        if (hasLower) poolSize += 26;
        if (hasUpper) poolSize += 26;
        if (hasDigit) poolSize += 10;
        if (hasSpecial) poolSize += 33;
        if (poolSize == 0) poolSize = 1;

        double entropy = length * (Math.log(poolSize) / Math.log(2));
        int finalScore = Math.min(100, Math.max(5, rawScore));

        Strength strength;
        String crackTime;
        if (finalScore < 30 || entropy < 30) {
            strength = Strength.VERY_WEAK;
            crackTime = "Instant (< 1 second)";
        } else if (finalScore < 50 || entropy < 45) {
            strength = Strength.WEAK;
            crackTime = "A few minutes to hours";
        } else if (finalScore < 70 || entropy < 60) {
            strength = Strength.MODERATE;
            crackTime = "Several days to months";
        } else if (finalScore < 88 || entropy < 75) {
            strength = Strength.STRONG;
            crackTime = "Years to centuries";
        } else {
            strength = Strength.VERY_STRONG;
            crackTime = "Millions of years";
        }

        List<String> suggestions = new ArrayList<>();
        if (!minLength) suggestions.add("Make your password at least 8 characters long.");
        else if (!recommendedLength) suggestions.add("Aim for 12 or more characters for better resilience.");
        if (!hasUpper) suggestions.add("Add at least one uppercase letter (A-Z).");
        if (!hasLower) suggestions.add("Add at least one lowercase letter (a-z).");
        if (!hasDigit) suggestions.add("Include at least one numerical digit (0-9).");
        if (!hasSpecial) suggestions.add("Include special characters (e.g. !@#$%^&*).");
        if (!noCommonPatterns) suggestions.add("Avoid common words, keyboard sequences, or repeated letters.");
        if (suggestions.isEmpty()) {
            suggestions.add("Excellent password! It fulfills all modern entropy standards.");
        }

        return new Result(finalScore, strength, minLength, recommendedLength,
                hasUpper, hasLower, hasDigit, hasSpecial, noCommonPatterns,
                crackTime, Math.round(entropy * 10.0) / 10.0, suggestions);
    }

    public static String generateStrongPassword(int length) {
        int safeLen = Math.max(12, Math.min(32, length));
        String upper = "ABCDEFGHJKLMNPQRSTUVWXYZ";
        String lower = "abcdefghijkmnopqrstuvwxyz";
        String digits = "23456789";
        String special = "!@#$%^&*_-+=";
        String all = upper + lower + digits + special;

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(safeLen);

        // Guarantee at least one of each
        sb.append(upper.charAt(random.nextInt(upper.length())));
        sb.append(lower.charAt(random.nextInt(lower.length())));
        sb.append(digits.charAt(random.nextInt(digits.length())));
        sb.append(special.charAt(random.nextInt(special.length())));

        for (int i = 4; i < safeLen; i++) {
            sb.append(all.charAt(random.nextInt(all.length())));
        }

        // Shuffle characters
        char[] chars = sb.toString().toCharArray();
        for (int i = chars.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }

        return new String(chars);
    }
}
