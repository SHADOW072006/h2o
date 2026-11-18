package com.example.h2o;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.h2o.evaluator.PasswordEvaluator;
import com.example.h2o.evaluator.UrlSafetyAnalyzer;

public class ExampleUnitTest {

    @Test
    public void testPasswordEvaluation_weakPassword() {
        PasswordEvaluator.Result result = PasswordEvaluator.evaluate("123456");
        assertTrue(result.score < 50);
        assertEquals(PasswordEvaluator.Strength.VERY_WEAK, result.strength);
        assertFalse(result.noCommonPatterns);
    }

    @Test
    public void testPasswordEvaluation_strongPassword() {
        PasswordEvaluator.Result result = PasswordEvaluator.evaluate("Cyb3r#Sh1eld_2026!Secure");
        assertTrue(result.score >= 80);
        assertTrue(result.minLength);
        assertTrue(result.recommendedLength);
        assertTrue(result.hasUppercase);
        assertTrue(result.hasLowercase);
        assertTrue(result.hasDigit);
        assertTrue(result.hasSpecial);
        assertTrue(result.noCommonPatterns);
    }

    @Test
    public void testPasswordGenerator() {
        String generated = PasswordEvaluator.generateStrongPassword(16);
        assertEquals(16, generated.length());
        PasswordEvaluator.Result result = PasswordEvaluator.evaluate(generated);
        assertTrue(result.score >= 70);
    }

    @Test
    public void testUrlSafety_safeUrl() {
        UrlSafetyAnalyzer.AnalysisResult result = UrlSafetyAnalyzer.analyze("https://github.com/torvalds/linux");
        assertEquals(UrlSafetyAnalyzer.Status.SAFE, result.status);
        assertTrue(result.riskScore < 20);
    }

    @Test
    public void testUrlSafety_phishingUrl() {
        UrlSafetyAnalyzer.AnalysisResult result = UrlSafetyAnalyzer.analyze("http://paypal-verify-login.xyz/update");
        assertTrue(result.status == UrlSafetyAnalyzer.Status.SUSPICIOUS || result.status == UrlSafetyAnalyzer.Status.DANGEROUS);
        assertTrue(result.riskScore >= 25);
    }

    @Test
    public void testUrlSafety_ipAddressUrl() {
        UrlSafetyAnalyzer.AnalysisResult result = UrlSafetyAnalyzer.analyze("http://192.168.1.1/admin.apk");
        assertEquals(UrlSafetyAnalyzer.Status.DANGEROUS, result.status);
        assertTrue(result.riskScore >= 50);
    }
}
