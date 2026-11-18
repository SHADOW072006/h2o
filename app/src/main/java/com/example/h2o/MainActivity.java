package com.example.h2o;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.h2o.evaluator.PasswordEvaluator;
import com.example.h2o.evaluator.SecurityScoreManager;
import com.example.h2o.evaluator.UrlSafetyAnalyzer;
import com.example.h2o.model.SecurityAlert;
import com.example.h2o.model.SecurityTip;
import com.example.h2o.quiz.QuizQuestion;
import com.example.h2o.quiz.QuizRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SecurityScoreManager scoreManager;

    // Content Views
    private View viewDashboard;
    private View viewPassword;
    private View viewUrlScan;
    private View viewQuiz;
    private View viewTipsAlerts;
    private BottomNavigationView bottomNavigation;

    // Dashboard Views
    private TextView tvOverallScore;
    private TextView tvScoreRating;
    private LinearProgressIndicator progressOverallScore;
    private TextView tvTopShieldStatus;
    private TextView tvDashPasswordStatus;
    private TextView tvDashUrlStatus;
    private TextView tvDashQuizStatus;

    // Password Views
    private EditText etPassword;
    private TextView tvPasswordStrength;
    private LinearProgressIndicator progressPasswordStrength;
    private TextView tvCrackTime;
    private TextView tvEntropy;
    private TextView critMinLength;
    private TextView critRecLength;
    private TextView critUpper;
    private TextView critLower;
    private TextView critDigit;
    private TextView critSpecial;
    private TextView critPatterns;
    private TextView tvPasswordSuggestions;

    // URL Scanner Views
    private EditText etUrl;
    private View cardUrlResult;
    private TextView tvUrlVerdict;
    private TextView tvUrlHost;
    private TextView tvUrlRiskLabel;
    private LinearProgressIndicator progressUrlRisk;
    private TextView tvUrlFindings;

    // Quiz Views
    private List<QuizQuestion> quizQuestions;
    private int currentQuizIndex = 0;
    private int correctQuizAnswers = 0;
    private TextView tvQuizProgressLabel;
    private TextView tvQuizScore;
    private LinearProgressIndicator progressQuiz;
    private TextView tvQuizQuestion;
    private MaterialButton[] btnOptions;
    private View cardQuizFeedback;
    private TextView tvFeedbackTitle;
    private TextView tvFeedbackExplanation;
    private MaterialButton btnQuizNext;

    // Tips and Alerts Views
    private LinearLayout layoutItemsContainer;
    private MaterialButton btnFilterTips;
    private MaterialButton btnFilterAlerts;
    private boolean showingTips = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        scoreManager = new SecurityScoreManager(this);

        initViews();
        setupNavigation();
        setupDashboard();
        setupPasswordEvaluator();
        setupUrlScanner();
        setupQuiz();
        setupTipsAndAlerts();

        refreshDashboard();
    }

    private void initViews() {
        viewDashboard = findViewById(R.id.included_dashboard);
        viewPassword = findViewById(R.id.included_password);
        viewUrlScan = findViewById(R.id.included_url_scan);
        viewQuiz = findViewById(R.id.included_quiz);
        viewTipsAlerts = findViewById(R.id.included_tips_alerts);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        tvTopShieldStatus = findViewById(R.id.tv_top_shield_status);

        // Dashboard
        tvOverallScore = findViewById(R.id.tv_overall_score);
        tvScoreRating = findViewById(R.id.tv_score_rating);
        progressOverallScore = findViewById(R.id.progress_overall_score);
        tvDashPasswordStatus = findViewById(R.id.tv_dash_password_status);
        tvDashUrlStatus = findViewById(R.id.tv_dash_url_status);
        tvDashQuizStatus = findViewById(R.id.tv_dash_quiz_status);

        // Password
        etPassword = findViewById(R.id.et_password);
        tvPasswordStrength = findViewById(R.id.tv_password_strength);
        progressPasswordStrength = findViewById(R.id.progress_password_strength);
        tvCrackTime = findViewById(R.id.tv_crack_time);
        tvEntropy = findViewById(R.id.tv_entropy);
        critMinLength = findViewById(R.id.crit_min_length);
        critRecLength = findViewById(R.id.crit_rec_length);
        critUpper = findViewById(R.id.crit_upper);
        critLower = findViewById(R.id.crit_lower);
        critDigit = findViewById(R.id.crit_digit);
        critSpecial = findViewById(R.id.crit_special);
        critPatterns = findViewById(R.id.crit_patterns);
        tvPasswordSuggestions = findViewById(R.id.tv_password_suggestions);

        // URL Scanner
        etUrl = findViewById(R.id.et_url);
        cardUrlResult = findViewById(R.id.card_url_result);
        tvUrlVerdict = findViewById(R.id.tv_url_verdict);
        tvUrlHost = findViewById(R.id.tv_url_host);
        tvUrlRiskLabel = findViewById(R.id.tv_url_risk_label);
        progressUrlRisk = findViewById(R.id.progress_url_risk);
        tvUrlFindings = findViewById(R.id.tv_url_findings);

        // Quiz
        tvQuizProgressLabel = findViewById(R.id.tv_quiz_progress_label);
        tvQuizScore = findViewById(R.id.tv_quiz_score);
        progressQuiz = findViewById(R.id.progress_quiz);
        tvQuizQuestion = findViewById(R.id.tv_quiz_question);
        btnOptions = new MaterialButton[]{
                findViewById(R.id.btn_option_0),
                findViewById(R.id.btn_option_1),
                findViewById(R.id.btn_option_2),
                findViewById(R.id.btn_option_3)
        };
        cardQuizFeedback = findViewById(R.id.card_quiz_feedback);
        tvFeedbackTitle = findViewById(R.id.tv_feedback_title);
        tvFeedbackExplanation = findViewById(R.id.tv_feedback_explanation);
        btnQuizNext = findViewById(R.id.btn_quiz_next);

        // Tips and Alerts
        layoutItemsContainer = findViewById(R.id.layout_items_container);
        btnFilterTips = findViewById(R.id.btn_filter_tips);
        btnFilterAlerts = findViewById(R.id.btn_filter_alerts);
    }

    private void setupNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_dashboard) {
                showTab(viewDashboard);
                refreshDashboard();
                return true;
            } else if (itemId == R.id.menu_password) {
                showTab(viewPassword);
                return true;
            } else if (itemId == R.id.menu_url) {
                showTab(viewUrlScan);
                return true;
            } else if (itemId == R.id.menu_quiz) {
                showTab(viewQuiz);
                return true;
            } else if (itemId == R.id.menu_tips) {
                showTab(viewTipsAlerts);
                return true;
            }
            return false;
        });
    }

    private void showTab(View target) {
        viewDashboard.setVisibility(target == viewDashboard ? View.VISIBLE : View.GONE);
        viewPassword.setVisibility(target == viewPassword ? View.VISIBLE : View.GONE);
        viewUrlScan.setVisibility(target == viewUrlScan ? View.VISIBLE : View.GONE);
        viewQuiz.setVisibility(target == viewQuiz ? View.VISIBLE : View.GONE);
        viewTipsAlerts.setVisibility(target == viewTipsAlerts ? View.VISIBLE : View.GONE);
    }

    private void setupDashboard() {
        MaterialCardView cardNavPassword = findViewById(R.id.card_nav_password);
        MaterialCardView cardNavUrl = findViewById(R.id.card_nav_url);
        MaterialCardView cardNavQuiz = findViewById(R.id.card_nav_quiz);

        cardNavPassword.setOnClickListener(v -> bottomNavigation.setSelectedItemId(R.id.menu_password));
        cardNavUrl.setOnClickListener(v -> bottomNavigation.setSelectedItemId(R.id.menu_url));
        cardNavQuiz.setOnClickListener(v -> bottomNavigation.setSelectedItemId(R.id.menu_quiz));
    }

    private void refreshDashboard() {
        int score = scoreManager.calculateOverallScore();
        String rating = scoreManager.getScoreRating();
        int color = scoreManager.getRatingColor();

        tvOverallScore.setText(String.valueOf(score));
        tvOverallScore.setTextColor(color);
        tvScoreRating.setText(rating);
        progressOverallScore.setProgress(score);
        progressOverallScore.setIndicatorColor(color);

        tvTopShieldStatus.setText(score >= 70 ? "PROTECTED" : "AT RISK");

        int pwdScore = scoreManager.getPasswordScore();
        tvDashPasswordStatus.setText("Current password rating: " + pwdScore + "/100");

        int urlScore = scoreManager.getUrlScore();
        tvDashUrlStatus.setText("Link safety integrity: " + urlScore + "/100");

        int quizScore = scoreManager.getQuizScore();
        tvDashQuizStatus.setText("Quiz score: " + quizScore + "% knowledge rating");
    }

    private void setupPasswordEvaluator() {
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                evaluatePassword(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        findViewById(R.id.btn_generate_password).setOnClickListener(v -> {
            String strongPwd = PasswordEvaluator.generateStrongPassword(16);
            etPassword.setText(strongPwd);
            etPassword.setSelection(strongPwd.length());
            Toast.makeText(this, "Strong password generated!", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btn_copy_password).setOnClickListener(v -> {
            String pwd = etPassword.getText().toString();
            if (pwd.isEmpty()) {
                Toast.makeText(this, "No password to copy.", Toast.LENGTH_SHORT).show();
                return;
            }
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Password", pwd);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Password copied to clipboard!", Toast.LENGTH_SHORT).show();
        });
    }

    private void evaluatePassword(String password) {
        PasswordEvaluator.Result result = PasswordEvaluator.evaluate(password);

        tvPasswordStrength.setText(result.strength.getLabel());
        tvPasswordStrength.setTextColor(result.strength.getColor());
        progressPasswordStrength.setProgress(result.score);
        progressPasswordStrength.setIndicatorColor(result.strength.getColor());

        tvCrackTime.setText("Crack time: " + result.crackTime);
        tvEntropy.setText("Entropy: " + result.entropyBits + " bits");

        updateChecklist(critMinLength, result.minLength, "At least 8 characters long");
        updateChecklist(critRecLength, result.recommendedLength, "Recommended 12+ characters long");
        updateChecklist(critUpper, result.hasUppercase, "Uppercase letter (A-Z)");
        updateChecklist(critLower, result.hasLowercase, "Lowercase letter (a-z)");
        updateChecklist(critDigit, result.hasDigit, "Numerical digit (0-9)");
        updateChecklist(critSpecial, result.hasSpecial, "Special character (!@#$%^&*)");
        updateChecklist(critPatterns, result.noCommonPatterns, "No common dictionary or repeating patterns");

        StringBuilder sb = new StringBuilder();
        for (String tip : result.suggestions) {
            sb.append("• ").append(tip).append("\n");
        }
        tvPasswordSuggestions.setText(sb.toString().trim());

        if (!password.isEmpty()) {
            scoreManager.updatePasswordScore(result.score);
        }
    }

    private void updateChecklist(TextView tv, boolean passed, String text) {
        if (passed) {
            tv.setText("✓  " + text);
            tv.setTextColor(0xFF2E7D32); // Green
        } else {
            tv.setText("✕  " + text);
            tv.setTextColor(0xFF546E7A); // Neutral gray
        }
    }

    private void setupUrlScanner() {
        findViewById(R.id.btn_sample_safe).setOnClickListener(v -> {
            etUrl.setText("https://github.com/torvalds/linux");
            scanCurrentUrl();
        });

        findViewById(R.id.btn_sample_phish).setOnClickListener(v -> {
            etUrl.setText("http://paypal-security-update.account-verify.xyz/login");
            scanCurrentUrl();
        });

        findViewById(R.id.btn_sample_danger).setOnClickListener(v -> {
            etUrl.setText("http://192.168.1.100:8080/free-gift-trojan.apk");
            scanCurrentUrl();
        });

        findViewById(R.id.btn_scan_url).setOnClickListener(v -> scanCurrentUrl());
    }

    private void scanCurrentUrl() {
        String input = etUrl.getText().toString();
        if (input.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a URL to inspect.", Toast.LENGTH_SHORT).show();
            return;
        }

        UrlSafetyAnalyzer.AnalysisResult result = UrlSafetyAnalyzer.analyze(input);
        cardUrlResult.setVisibility(View.VISIBLE);

        tvUrlVerdict.setText(result.status.getLabel());
        tvUrlVerdict.setTextColor(result.status.getTextColor());
        tvUrlVerdict.setBackgroundColor(result.status.getBackgroundColor());

        tvUrlHost.setText("Host: " + (result.normalizedHost.isEmpty() ? "Unknown" : result.normalizedHost));
        tvUrlRiskLabel.setText("Risk Score: " + result.riskScore + "/100 (" + result.status.getLabel() + ")");
        progressUrlRisk.setProgress(result.riskScore);
        progressUrlRisk.setIndicatorColor(result.status.getTextColor());

        StringBuilder findings = new StringBuilder();
        if (!result.warnings.isEmpty()) {
            findings.append("⚠️ Warnings & Risks:\n");
            for (String w : result.warnings) {
                findings.append(" • ").append(w).append("\n");
            }
        }
        if (!result.positives.isEmpty()) {
            if (findings.length() > 0) findings.append("\n");
            findings.append("🛡️ Safe Indicators:\n");
            for (String p : result.positives) {
                findings.append(" • ").append(p).append("\n");
            }
        }
        tvUrlFindings.setText(findings.toString().trim());

        // Update score manager: higher risk -> lower URL safety score
        int safetyScore = Math.max(10, 100 - result.riskScore);
        scoreManager.updateUrlScore(safetyScore);
    }

    private void setupQuiz() {
        quizQuestions = QuizRepository.getQuestions();
        progressQuiz.setMax(quizQuestions.size());
        loadQuizQuestion(0);

        for (int i = 0; i < btnOptions.length; i++) {
            final int optionIndex = i;
            btnOptions[i].setOnClickListener(v -> onQuizOptionSelected(optionIndex));
        }

        btnQuizNext.setOnClickListener(v -> {
            currentQuizIndex++;
            if (currentQuizIndex < quizQuestions.size()) {
                loadQuizQuestion(currentQuizIndex);
            } else {
                // Quiz completed
                finishQuiz();
            }
        });
    }

    private void loadQuizQuestion(int index) {
        if (index >= quizQuestions.size()) return;

        QuizQuestion q = quizQuestions.get(index);
        tvQuizProgressLabel.setText("Question " + (index + 1) + " of " + quizQuestions.size());
        tvQuizScore.setText("Score: " + correctQuizAnswers + "/" + index);
        progressQuiz.setProgress(index + 1);

        tvQuizQuestion.setText(q.getQuestion());

        List<String> options = q.getOptions();
        for (int i = 0; i < btnOptions.length; i++) {
            if (i < options.size()) {
                btnOptions[i].setVisibility(View.VISIBLE);
                btnOptions[i].setEnabled(true);
                btnOptions[i].setText(options.get(i));
                btnOptions[i].setStrokeColor(ColorStateList.valueOf(0xFFE0E4EC));
                btnOptions[i].setTextColor(0xFF1A1A24);
                btnOptions[i].setBackgroundColor(0x00000000);
            } else {
                btnOptions[i].setVisibility(View.GONE);
            }
        }

        cardQuizFeedback.setVisibility(View.GONE);
        btnQuizNext.setVisibility(View.GONE);
    }

    private void onQuizOptionSelected(int selectedIndex) {
        QuizQuestion q = quizQuestions.get(currentQuizIndex);
        boolean isCorrect = selectedIndex == q.getCorrectIndex();

        if (isCorrect) {
            correctQuizAnswers++;
            tvFeedbackTitle.setText("✓ Correct Answer!");
            tvFeedbackTitle.setTextColor(0xFF2E7D32);
        } else {
            tvFeedbackTitle.setText("✕ Incorrect");
            tvFeedbackTitle.setTextColor(0xFFC62828);
        }

        tvFeedbackExplanation.setText(q.getExplanation());
        cardQuizFeedback.setVisibility(View.VISIBLE);

        // Highlight buttons
        for (int i = 0; i < btnOptions.length; i++) {
            btnOptions[i].setEnabled(false);
            if (i == q.getCorrectIndex()) {
                btnOptions[i].setBackgroundColor(0xFFE8F5E9);
                btnOptions[i].setTextColor(0xFF2E7D32);
                btnOptions[i].setStrokeColor(ColorStateList.valueOf(0xFF2E7D32));
            } else if (i == selectedIndex) {
                btnOptions[i].setBackgroundColor(0xFFFFEBEE);
                btnOptions[i].setTextColor(0xFFC62828);
                btnOptions[i].setStrokeColor(ColorStateList.valueOf(0xFFC62828));
            }
        }

        tvQuizScore.setText("Score: " + correctQuizAnswers + "/" + (currentQuizIndex + 1));

        btnQuizNext.setVisibility(View.VISIBLE);
        btnQuizNext.setText(currentQuizIndex == quizQuestions.size() - 1 ? "Complete Quiz" : "Next Question");
    }

    private void finishQuiz() {
        int percentage = (int) Math.round(((double) correctQuizAnswers / quizQuestions.size()) * 100);
        scoreManager.updateQuizScore(percentage);

        tvQuizQuestion.setText("🎉 Quiz Completed!\n\nYou scored " + correctQuizAnswers + " out of " + quizQuestions.size() + " (" + percentage + "%).");
        for (MaterialButton btn : btnOptions) {
            btn.setVisibility(View.GONE);
        }
        cardQuizFeedback.setVisibility(View.GONE);

        btnQuizNext.setText("Restart Quiz");
        btnQuizNext.setVisibility(View.VISIBLE);
        btnQuizNext.setOnClickListener(v -> {
            currentQuizIndex = 0;
            correctQuizAnswers = 0;
            btnQuizNext.setOnClickListener(v2 -> {
                currentQuizIndex++;
                if (currentQuizIndex < quizQuestions.size()) {
                    loadQuizQuestion(currentQuizIndex);
                } else {
                    finishQuiz();
                }
            });
            loadQuizQuestion(0);
        });

        refreshDashboard();
    }

    private void setupTipsAndAlerts() {
        btnFilterTips.setOnClickListener(v -> {
            showingTips = true;
            btnFilterTips.setBackgroundColor(0xFF0D47A1);
            btnFilterTips.setTextColor(0xFFFFFFFF);
            btnFilterAlerts.setBackgroundColor(0x00000000);
            btnFilterAlerts.setTextColor(0xFF0D47A1);
            renderTipsAndAlerts();
        });

        btnFilterAlerts.setOnClickListener(v -> {
            showingTips = false;
            btnFilterAlerts.setBackgroundColor(0xFF0D47A1);
            btnFilterAlerts.setTextColor(0xFFFFFFFF);
            btnFilterTips.setBackgroundColor(0x00000000);
            btnFilterTips.setTextColor(0xFF0D47A1);
            renderTipsAndAlerts();
        });

        renderTipsAndAlerts();
    }

    private void renderTipsAndAlerts() {
        layoutItemsContainer.removeAllViews();

        if (showingTips) {
            List<SecurityTip> tips = SecurityTip.getDefaultTips();
            for (SecurityTip tip : tips) {
                MaterialCardView card = new MaterialCardView(this);
                card.setRadius(24);
                card.setStrokeWidth(2);
                card.setStrokeColor(0xFFE0E4EC);
                card.setCardElevation(2);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 0, 0, 24);
                card.setLayoutParams(lp);

                LinearLayout content = new LinearLayout(this);
                content.setOrientation(LinearLayout.VERTICAL);
                content.setPadding(32, 32, 32, 32);

                TextView tvCategory = new TextView(this);
                tvCategory.setText(tip.getCategory().toUpperCase());
                tvCategory.setTextColor(0xFF0077B6);
                tvCategory.setTextSize(11);
                tvCategory.setTypeface(null, Typeface.BOLD);
                content.addView(tvCategory);

                TextView tvTitle = new TextView(this);
                tvTitle.setText(tip.getTitle());
                tvTitle.setTextColor(0xFF1A1A24);
                tvTitle.setTextSize(16);
                tvTitle.setTypeface(null, Typeface.BOLD);
                tvTitle.setPadding(0, 8, 0, 8);
                content.addView(tvTitle);

                TextView tvDesc = new TextView(this);
                tvDesc.setText(tip.getDescription());
                tvDesc.setTextColor(0xFF666B7A);
                tvDesc.setTextSize(13);
                tvDesc.setLineSpacing(6, 1);
                content.addView(tvDesc);

                card.addView(content);
                layoutItemsContainer.addView(card);
            }
        } else {
            List<SecurityAlert> alerts = SecurityAlert.getDefaultAlerts();
            for (SecurityAlert alert : alerts) {
                MaterialCardView card = new MaterialCardView(this);
                card.setRadius(24);
                card.setStrokeWidth(2);
                card.setStrokeColor(0xFFE0E4EC);
                card.setCardElevation(2);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 0, 0, 24);
                card.setLayoutParams(lp);

                LinearLayout content = new LinearLayout(this);
                content.setOrientation(LinearLayout.VERTICAL);
                content.setPadding(32, 32, 32, 32);

                int sevColor = "Critical".equalsIgnoreCase(alert.getSeverity()) ? 0xFFC62828 : 0xFFEF6C00;
                TextView tvSeverity = new TextView(this);
                tvSeverity.setText("SEVERITY: " + alert.getSeverity().toUpperCase());
                tvSeverity.setTextColor(sevColor);
                tvSeverity.setTextSize(11);
                tvSeverity.setTypeface(null, Typeface.BOLD);
                content.addView(tvSeverity);

                TextView tvTitle = new TextView(this);
                tvTitle.setText(alert.getTitle());
                tvTitle.setTextColor(0xFF1A1A24);
                tvTitle.setTextSize(16);
                tvTitle.setTypeface(null, Typeface.BOLD);
                tvTitle.setPadding(0, 8, 0, 8);
                content.addView(tvTitle);

                TextView tvSummary = new TextView(this);
                tvSummary.setText(alert.getSummary());
                tvSummary.setTextColor(0xFF666B7A);
                tvSummary.setTextSize(13);
                content.addView(tvSummary);

                TextView tvAction = new TextView(this);
                tvAction.setText("🛡️ Defense: " + alert.getCountermeasure());
                tvAction.setTextColor(0xFF2E7D32);
                tvAction.setTextSize(13);
                tvAction.setTypeface(null, Typeface.BOLD);
                tvAction.setPadding(0, 12, 0, 0);
                content.addView(tvAction);

                card.addView(content);
                layoutItemsContainer.addView(card);
            }
        }
    }
}
