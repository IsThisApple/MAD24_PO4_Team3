package sg.edu.np.mad.nearbuy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class LoginPageHolder extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private DatabaseHelper db;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        db = new DatabaseHelper(this);
        tts = new TextToSpeech(this, this);

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
        View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            private long lastClickTime = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    long clickTime = System.currentTimeMillis();
                    if (clickTime - lastClickTime < ViewConfiguration.getDoubleTapTimeout()) {
                        // handle double-click
                        if (v.getId() == R.id.etUsername) {
                            speakText("Enter username");
                            etUsername.requestFocus();
                            showKeyboard(etUsername);
                        } else if (v.getId() == R.id.etPassword) {
                            speakText("Enter password");
                            etPassword.requestFocus();
                            showKeyboard(etPassword);
                        } else if (v.getId() == R.id.btnLogin) {
                            speakText("Login");
                            login();
                        } else if (v.getId() == R.id.tvRegister) {
                            speakText("Register");
                            Intent intent = new Intent(LoginPageHolder.this, RegisterPageHolder.class);
                            startActivity(intent);
                        }
                        lastClickTime = 0; // reset last click time
                    } else {
                        // handle single-click
                        if (v.getId() == R.id.etUsername) {
                            speakText("Enter username");
                        } else if (v.getId() == R.id.etPassword) {
                            speakText("Enter password");
                        } else if (v.getId() == R.id.btnLogin) {
                            speakText("Login");
                        } else if (v.getId() == R.id.tvRegister) {
                            speakText("Register");
                        }
                    }
                    lastClickTime = clickTime;
                }
                return true;
            }
        };

        etUsername.setOnTouchListener(onTouchListener);
        etPassword.setOnTouchListener(onTouchListener);
        btnLogin.setOnTouchListener(onTouchListener);
        tvRegister.setOnTouchListener(onTouchListener);
    }

    private void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void login() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (validateInputs(username, password)) {
            if (db.checkUser(username, password)) {
                // Store the user ID (or username) for future use
                SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("currentUserId", username); // Or use a unique user ID
                editor.apply();

                Toast.makeText(LoginPageHolder.this, "Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginPageHolder.this, MainActivity.class));
                finish(); // finish login activity
            } else {
                Toast.makeText(LoginPageHolder.this, "Wrong Username or Password", Toast.LENGTH_SHORT).show();
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

        /*
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String username = etUsername.getText().toString();
                    String password = etPassword.getText().toString();

                    Log.d("LoginActivity", "Username: " + username + " Password: " + password);

                    if (db.checkUser(username, password)) {
                        Toast.makeText(LoginPageHolder.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginPageHolder.this, MainActivity.class));
                        finish(); // finish login activity
                    } else {
                        Toast.makeText(LoginPageHolder.this, "Wrong Username or Password", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("LoginActivity", "Error on login: " + e.getMessage(), e);
                    Toast.makeText(LoginPageHolder.this, "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
            Intent intent = new Intent(LoginPageHolder.this, RegisterPageHolder.class);
                startActivity(intent);
            }
        });
    }

    private void login() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (validateInputs(username, password)) {
            if (db.checkUser(username, password)) {
                Toast.makeText(LoginPageHolder.this, "Login Successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginPageHolder.this, "Wrong Username or Password", Toast.LENGTH_SHORT).show();
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
*/