package com.example.h2o.evaluator;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

public class UrlSafetyAnalyzer {

    public enum Status {
        SAFE("Safe", 0xFF2E7D32, 0xFFE8F5E9),
        SUSPICIOUS("Suspicious", 0xFFEF6C00, 0xFFFFF3E0),
        DANGEROUS("High Risk / Dangerous", 0xFFC62828, 0xFFFFEBEE);

        private final String label;
        private final int textColor;
        private final int backgroundColor;

        Status(String label, int textColor, int backgroundColor) {
            this.label = label;
            this.textColor = textColor;
            this.backgroundColor = backgroundColor;
        }

        public String getLabel() {
            return label;
        }

        public int getTextColor() {
            return textColor;
        }

        public int getBackgroundColor() {
            return backgroundColor;
        }
    }

    public static class AnalysisResult {
        public final String originalUrl;
        public final String normalizedHost;
        public final Status status;
        public final int riskScore; // 0 (safest) to 100 (highest risk)
        public final List<String> warnings;
        public final List<String> positives;

        public AnalysisResult(String originalUrl, String normalizedHost, Status status,
                              int riskScore, List<String> warnings, List<String> positives) {
            this.originalUrl = originalUrl;
            this.normalizedHost = normalizedHost;
            this.status = status;
            this.riskScore = riskScore;
            this.warnings = warnings;
            this.positives = positives;
        }
    }

    private static final Pattern IPV4_PATTERN =
            Pattern.compile("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$");

    private static final Set<String> HIGH_RISK_TLDS = new HashSet<>(Arrays.asList(
            ".tk", ".ml", ".ga", ".cf", ".gq", ".top", ".work", ".buzz", ".xyz",
            ".loan", ".surf", ".rest", ".fit", ".icu", ".cam"
    ));

    private static final String[] TARGET_BRANDS = {
            "paypal", "google", "apple", "microsoft", "amazon", "netflix",
            "facebook", "instagram", "whatsapp", "wellsfargo", "chase", "bankofamerica"
    };

    private static final String[] SUSPICIOUS_KEYWORDS = {
            "verify", "login", "signin", "update-account", "secure", "banking",
            "wallet", "free-gift", "claim", "prize", "credential", "recover"
    };

    private static final String[] DANGEROUS_EXTENSIONS = {
            ".apk", ".exe", ".scr", ".bat", ".vbs", ".cmd", ".msi"
    };

    public static AnalysisResult analyze(String inputUrl) {
        if (inputUrl == null || inputUrl.trim().isEmpty()) {
            return new AnalysisResult("", "", Status.SUSPICIOUS, 50,
                    List.of("No URL provided."), List.of());
        }

        String raw = inputUrl.trim();
        String urlString = raw;
        if (!urlString.startsWith("http://") && !urlString.startsWith("https://")) {
            urlString = "https://" + urlString;
        }

        URI uri;
        try {
            uri = new URI(urlString);
        } catch (URISyntaxException e) {
            return new AnalysisResult(raw, "", Status.DANGEROUS, 95,
                    List.of("Malformed URL syntax: Cannot parse as a standard Internet URI."),
                    List.of());
        }

        String scheme = uri.getScheme() != null ? uri.getScheme().toLowerCase(Locale.ROOT) : "";
        String host = uri.getHost() != null ? uri.getHost().toLowerCase(Locale.ROOT) : "";
        String path = uri.getPath() != null ? uri.getPath().toLowerCase(Locale.ROOT) : "";
        String query = uri.getQuery() != null ? uri.getQuery().toLowerCase(Locale.ROOT) : "";

        List<String> warnings = new ArrayList<>();
        List<String> positives = new ArrayList<>();
        int riskScore = 0;

        // Check protocol
        if ("https".equals(scheme)) {
            positives.add("Uses secure encrypted HTTPS communication.");
        } else if ("http".equals(scheme)) {
            warnings.add("Unencrypted HTTP protocol in use: Traffic can be intercepted or altered.");
            riskScore += 25;
        } else {
            warnings.add("Unusual protocol scheme: " + scheme);
            riskScore += 40;
        }

        if (host.isEmpty()) {
            warnings.add("Unable to identify valid hostname/domain.");
            riskScore += 50;
        } else {
            // Check IP address as host
            if (IPV4_PATTERN.matcher(host).matches()) {
                warnings.add("Raw IP address used as hostname instead of a domain name (common phishing tactic).");
                riskScore += 45;
            }

            // Check High Risk TLDs
            for (String tld : HIGH_RISK_TLDS) {
                if (host.endsWith(tld)) {
                    warnings.add("Domain uses high-risk/frequently abused top-level domain (" + tld + ").");
                    riskScore += 30;
                    break;
                }
            }

            // Check subdomain depth (e.g. a.b.c.d.example.com)
            String[] parts = host.split("\\.");
            if (parts.length > 4) {
                warnings.add("Excessive subdomain depth (" + parts.length + " levels), often used to disguise actual domains.");
                riskScore += 20;
            }

            // Check deceptive brand impersonation
            for (String brand : TARGET_BRANDS) {
                if (host.contains(brand)) {
                    // Check if legitimate brand domain
                    boolean isLegit = host.equals(brand + ".com") || host.endsWith("." + brand + ".com");
                    if (!isLegit) {
                        warnings.add("Possible brand spoofing detected: Domain contains '" + brand + "' but does not belong to the official service.");
                        riskScore += 40;
                    } else {
                        positives.add("Belongs to legitimate recognized domain for " + brand + ".");
                    }
                }
            }

            // Check hyphens count in host
            long hyphenCount = host.chars().filter(ch -> ch == '-').count();
            if (hyphenCount >= 3) {
                warnings.add("Multiple hyphens in domain name (" + hyphenCount + "), characteristic of spoofing.");
                riskScore += 15;
            }
        }

        // Check @ symbol in raw URL (RFC 1738 credentials trick)
        if (raw.contains("@")) {
            warnings.add("Embedded '@' character found. Attackers use this to obscure the true destination host.");
            riskScore += 45;
        }

        // Check custom ports
        if (uri.getPort() != -1 && uri.getPort() != 80 && uri.getPort() != 443) {
            warnings.add("Non-standard web port used (Port " + uri.getPort() + ").");
            riskScore += 15;
        }

        // Check dangerous file extensions in path
        for (String ext : DANGEROUS_EXTENSIONS) {
            if (path.endsWith(ext)) {
                warnings.add("URL links directly to an executable/script file (" + ext + ") which may contain malware.");
                riskScore += 50;
                break;
            }
        }

        // Check suspicious keywords in path or query
        String combined = path + "?" + query;
        int keywordCount = 0;
        for (String kw : SUSPICIOUS_KEYWORDS) {
            if (combined.contains(kw)) {
                keywordCount++;
            }
        }
        if (keywordCount >= 2) {
            warnings.add("Multiple security/sensitive action keywords in URL path (" + keywordCount + " matches).");
            riskScore += 20;
        }

        // Check total URL length
        if (raw.length() > 150) {
            warnings.add("Unusually long URL (" + raw.length() + " chars), frequently used in token hiding or redirects.");
            riskScore += 10;
        }

        // Final status determination
        Status status;
        if (riskScore >= 50) {
            status = Status.DANGEROUS;
        } else if (riskScore >= 20) {
            status = Status.SUSPICIOUS;
        } else {
            status = Status.SAFE;
            if (positives.isEmpty()) {
                positives.add("Standard URL structure with no obvious phishing or malware indicators detected.");
            }
        }

        return new AnalysisResult(raw, host, status, Math.min(100, riskScore), warnings, positives);
    }
}
