package sg.edu.np.mad.nearbuy;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.widget.Button;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class AccessibilitySettingsActivity extends AppCompatActivity {
    private Switch switchAccessibility;
    private Button buttonProceed;
    private PreferenceManager preferenceManager;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accessibility_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        switchAccessibility = findViewById(R.id.switch_accessibility);
        buttonProceed = findViewById(R.id.button_proceed);
        preferenceManager = new PreferenceManager(this);

        // initialise TTS
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.ENGLISH);
            }
        });

        // load saved settings
        boolean isAccessibilityEnabled = preferenceManager.isAccessibilityEnabled();
        switchAccessibility.setChecked(isAccessibilityEnabled);

        // save settings and provide tts feedback when switches are toggled
        switchAccessibility.setOnCheckedChangeListener((buttonView, isChecked) -> {
                preferenceManager.setAccessibilityEnabled(isChecked);
                speak("Voiceover and double-click " + (isChecked ? "enabled" : "disabled"));
        });

        // handle proceed button clicks
        buttonProceed.setOnTouchListener(new DoubleClickListener(
                v -> handleSingleClick(),
                v -> handleDoubleClick()
        ));
    }

    private void handleSingleClick() {
        boolean isAccessibilityEnabled = preferenceManager.isAccessibilityEnabled();
        if (isAccessibilityEnabled) {
            speak("Proceed");
        } else {
            navigateToLogin();
        }
    }

    private void handleDoubleClick() {
        boolean isAccessibilityEnabled = preferenceManager.isAccessibilityEnabled();
        if (isAccessibilityEnabled) {
            speak("Proceed", this::navigateToLogin);
        }
    }

    private void navigateToLogin() {
        Intent intent = new Intent(AccessibilitySettingsActivity.this, LoginPageHolder.class);
        startActivity(intent);
        finish();
    }

    private void speak(String text) {
        speak(text, null);
    }

    private void speak(String text, Runnable onComplete) {
        if (preferenceManager.isAccessibilityEnabled()) {
            String utteranceId = this.hashCode() + "";
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {}

                @Override
                public void onDone(String utteranceId) {
                    runOnUiThread(() -> {
                        if (onComplete != null) {
                            onComplete.run();
                        }
                    });
                }

                @Override
                public void onError(String utteranceId) {}
            });
        } else {
            if (onComplete != null) {
                onComplete.run();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

}