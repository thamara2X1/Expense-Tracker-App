package com.example.expensetracker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2800; // Total splash duration in ms

    private View iconContainer;
    private TextView textAppName;
    private TextView textTagline;
    private LinearLayout loadingDots;
    private View dot1, dot2, dot3;
    private View circleTopRight, circleBottomLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the action bar for full-screen splash
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_splash);

        initViews();
        startAnimations();
        navigateToMain();
    }

    private void initViews() {
        iconContainer    = findViewById(R.id.icon_container);
        textAppName      = findViewById(R.id.text_app_name);
        textTagline      = findViewById(R.id.text_tagline);
        loadingDots      = findViewById(R.id.loading_dots);
        dot1             = findViewById(R.id.dot1);
        dot2             = findViewById(R.id.dot2);
        dot3             = findViewById(R.id.dot3);
        circleTopRight   = findViewById(R.id.circle_top_right);
        circleBottomLeft = findViewById(R.id.circle_bottom_left);

        // Start everything invisible for entrance animations
        iconContainer.setAlpha(0f);
        iconContainer.setScaleX(0.3f);
        iconContainer.setScaleY(0.3f);
        textAppName.setAlpha(0f);
        textAppName.setTranslationY(40f);
        textTagline.setAlpha(0f);
        textTagline.setTranslationY(30f);
        loadingDots.setAlpha(0f);
        circleTopRight.setScaleX(0f);
        circleTopRight.setScaleY(0f);
        circleBottomLeft.setScaleX(0f);
        circleBottomLeft.setScaleY(0f);
    }

    private void startAnimations() {
        // 1. Background circles pop in
        AnimatorSet circlesAnim = new AnimatorSet();
        circlesAnim.playTogether(
                scaleIn(circleTopRight, 0, 600),
                scaleIn(circleBottomLeft, 200, 600)
        );

        // 2. Icon bounces in
        AnimatorSet iconAnim = new AnimatorSet();
        iconAnim.playTogether(
                ObjectAnimator.ofFloat(iconContainer, "alpha", 0f, 1f).setDuration(400),
                scaleInOvershoot(iconContainer, 0, 500)
        );

        // 3. App name slides up
        AnimatorSet nameAnim = new AnimatorSet();
        nameAnim.playTogether(
                ObjectAnimator.ofFloat(textAppName, "alpha", 0f, 1f).setDuration(400),
                ObjectAnimator.ofFloat(textAppName, "translationY", 40f, 0f).setDuration(400)
        );

        // 4. Tagline slides up
        AnimatorSet taglineAnim = new AnimatorSet();
        taglineAnim.playTogether(
                ObjectAnimator.ofFloat(textTagline, "alpha", 0f, 0.8f).setDuration(400),
                ObjectAnimator.ofFloat(textTagline, "translationY", 30f, 0f).setDuration(400)
        );

        // 5. Loading dots fade in
        ObjectAnimator dotsAnim = ObjectAnimator.ofFloat(loadingDots, "alpha", 0f, 1f);
        dotsAnim.setDuration(300);

        // Chain animations with delays
        circlesAnim.start();

        new Handler(Looper.getMainLooper()).postDelayed(iconAnim::start, 200);
        new Handler(Looper.getMainLooper()).postDelayed(nameAnim::start, 600);
        new Handler(Looper.getMainLooper()).postDelayed(taglineAnim::start, 800);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            dotsAnim.start();
            startDotPulse();
        }, 1100);

        // Subtle icon pulse after it appears
        new Handler(Looper.getMainLooper()).postDelayed(this::pulseIcon, 800);
    }

    private void startDotPulse() {
        // Animate dots sequentially to simulate loading
        animateDot(dot1, 0);
        animateDot(dot2, 200);
        animateDot(dot3, 400);
    }

    private void animateDot(View dot, long delay) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(dot, "scaleX", 1f, 1.5f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(dot, "scaleY", 1f, 1.5f, 1f);
        ObjectAnimator alpha  = ObjectAnimator.ofFloat(dot, "alpha", dot.getAlpha(), 1f, dot.getAlpha());

        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY, alpha);
        set.setDuration(500);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.setStartDelay(delay);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Repeat continuously
                set.setStartDelay(600);
                set.start();
            }
        });
        set.start();
    }

    private void pulseIcon() {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(iconContainer, "scaleX", 1f, 1.08f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(iconContainer, "scaleY", 1f, 1.08f, 1f);
        AnimatorSet pulse = new AnimatorSet();
        pulse.playTogether(scaleX, scaleY);
        pulse.setDuration(600);
        pulse.setInterpolator(new AccelerateDecelerateInterpolator());
        pulse.start();
    }

    private ObjectAnimator scaleIn(View view, long delay, long duration) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator sx = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f);
        ObjectAnimator sy = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f);
        sx.setDuration(duration);
        sx.setStartDelay(delay);
        sx.setInterpolator(new OvershootInterpolator(1.2f));
        sy.setDuration(duration);
        sy.setStartDelay(delay);
        sy.setInterpolator(new OvershootInterpolator(1.2f));
        sx.start();
        sy.start();
        return sx; // Return one for AnimatorSet compatibility
    }

    private ObjectAnimator scaleInOvershoot(View view, long delay, long duration) {
        ObjectAnimator sx = ObjectAnimator.ofFloat(view, "scaleX", 0.3f, 1f);
        sx.setDuration(duration);
        sx.setStartDelay(delay);
        sx.setInterpolator(new OvershootInterpolator(2.0f));
        ObjectAnimator sy = ObjectAnimator.ofFloat(view, "scaleY", 0.3f, 1f);
        sy.setDuration(duration);
        sy.setStartDelay(delay);
        sy.setInterpolator(new OvershootInterpolator(2.0f));
        sy.start();
        return sx;
    }

    private void navigateToMain() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Fade out entire screen before launching MainActivity
            View rootView = findViewById(android.R.id.content);
            ObjectAnimator fadeOut = ObjectAnimator.ofFloat(rootView, "alpha", 1f, 0f);
            fadeOut.setDuration(400);
            fadeOut.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    // Use a crossfade transition
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
            });
            fadeOut.start();
        }, SPLASH_DURATION);
    }
}