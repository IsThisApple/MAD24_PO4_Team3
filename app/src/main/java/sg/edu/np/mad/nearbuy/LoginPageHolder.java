package sg.edu.np.mad.nearbuy;

import android.os.Bundle;
import android.text.TextUtils;
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
    private TextView tvForgotPassword;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResetPasswordDialog();
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
    private void showResetPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Password");

        final EditText inputUsername = new EditText(this);
        inputUsername.setHint("Enter your username");
        builder.setView(inputUsername);

        builder.setPositiveButton("Submit", (dialog, which) -> {
            String username = inputUsername.getText().toString().trim();
            if (TextUtils.isEmpty(username)) {
                Toast.makeText(LoginPageHolder.this, "Please enter your username", Toast.LENGTH_SHORT).show();
            } else {
                showNewPasswordDialog(username);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void showNewPasswordDialog(String username) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter New Password");

        final EditText inputNewPassword = new EditText(this);
        inputNewPassword.setHint("Enter new password");
        builder.setView(inputNewPassword);

        builder.setPositiveButton("Submit", (dialog, which) -> {
            String newPassword = inputNewPassword.getText().toString().trim();
            if (TextUtils.isEmpty(newPassword)) {
                Toast.makeText(LoginPageHolder.this, "Please enter a new password", Toast.LENGTH_SHORT).show();
            } else {
                if (db.updateUserPassword(username, newPassword)) {
                    Toast.makeText(LoginPageHolder.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginPageHolder.this, "Error updating password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
    }
}

