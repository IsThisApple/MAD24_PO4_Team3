package sg.edu.np.mad.nearbuy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginPageHolder extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        db = new DatabaseHelper(this);

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
                        finish(); // Finish login activity
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

