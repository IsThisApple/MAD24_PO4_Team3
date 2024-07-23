package sg.edu.np.mad.nearbuy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class RegisterPageHolder extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private EditText etUsername;
    private EditText etPassword;
    private Button btnRegister;
    private PreferenceManager preferenceManager;
    private TextToSpeech tts;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        preferenceManager = new PreferenceManager(this);
        tts = new TextToSpeech(this, this);
        db = new DatabaseHelper(this);

        setClickListeners();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language not supported");
            }
        } else {
            Log.e("TTS", "Initialisation failed");
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

    private void speakText(String text) {
        if (tts.isSpeaking()) {
            tts.stop(); // stop current speech before starting new one
        }
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    private void setClickListeners() {
        boolean isAccessibilityEnabled = preferenceManager.isAccessibilityEnabled();

        View.OnClickListener singleClickListener;
        View.OnClickListener doubleClickListener;

        if (isAccessibilityEnabled) {
            singleClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // single-click action
                    if (v.getId() == R.id.etUsername) {
                        speakText("Enter username");
                    } else if (v.getId() == R.id.etPassword) {
                        speakText("Enter password");
                    } else if (v.getId() == R.id.btnRegister) {
                        speakText("Register");
                    }
                }
            };

            doubleClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // double-click action
                    if (v.getId() == R.id.etUsername) {
                        speakText("Enter username");
                        etUsername.requestFocus();
                        showKeyboard(etUsername);
                    } else if (v.getId() == R.id.etPassword) {
                        speakText("Enter password");
                        etPassword.requestFocus();
                        showKeyboard(etPassword);
                    } else if (v.getId() == R.id.btnRegister) {
                        speakText("Register");
                        registerUser();
                    }
                }
            };
        } else {
            singleClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // single-click action
                    if (v.getId() == R.id.etUsername) {
                        etUsername.requestFocus();
                        showKeyboard(etUsername);
                    } else if (v.getId() == R.id.etPassword) {
                        etPassword.requestFocus();
                        showKeyboard(etPassword);
                    } else if (v.getId() == R.id.btnRegister) {
                        registerUser();
                    }
                }
            };

            doubleClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // double-click action
                    if (v.getId() == R.id.etUsername) {
                        //
                    } else if (v.getId() == R.id.etPassword) {
                        //
                    } else if (v.getId() == R.id.btnRegister) {
                        //
                    }
                }
            };
        }

        DoubleClickListener listener = new DoubleClickListener(singleClickListener, doubleClickListener);

        etUsername.setOnTouchListener(listener);
        etPassword.setOnTouchListener(listener);
        btnRegister.setOnTouchListener(listener);
    }

    private void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void registerUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (validateInputs(username, password)) {
            if (db.insertUser(username, password)) {
                Toast.makeText(RegisterPageHolder.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                finish(); // go back to the login screen
            } else {
                Toast.makeText(RegisterPageHolder.this, "Registration Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInputs(String username, String password) {
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Username is required");
            etUsername.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }

}