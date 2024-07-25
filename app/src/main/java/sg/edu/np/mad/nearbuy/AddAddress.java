package sg.edu.np.mad.nearbuy;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddAddress extends AppCompatActivity {

    private EditText streetInput;
    private EditText postalCodeInput;
    private EditText unitNumberInput;
    private EditText labelInput;
    private String currentUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        // Initialize UI components
        streetInput = findViewById(R.id.streetName);
        postalCodeInput = findViewById(R.id.postalCode);
        unitNumberInput = findViewById(R.id.unitNumber);
        labelInput = findViewById(R.id.label);
        Button addButton = findViewById(R.id.addAdd);

        currentUsername = getIntent().getStringExtra("username"); // Get username from intent

        addButton.setOnClickListener(v -> {
            String street = streetInput.getText().toString();
            String postalCode = postalCodeInput.getText().toString();
            String unitNumber = unitNumberInput.getText().toString();
            String label = labelInput.getText().toString();

            if (validateInputs(street, postalCode, unitNumber, label)) {
                DBAddress dbAddress = new DBAddress(AddAddress.this);
                if (dbAddress.isLabelUnique(label) && dbAddress.isPostalCodeUnique(postalCode)) {
                    dbAddress.addAddress(street, postalCode, unitNumber, label); // Pass username
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(AddAddress.this, "Label or postal code must be unique", Toast.LENGTH_SHORT).show();
                }
            }


        });
    }

    private boolean validateInputs(String street, String postalCode, String unitNumber, String label) {
        // Validate postal code input: should be exactly 6 digits
        if (!postalCode.matches("\\d{6}")) {
            Toast.makeText(this, "Postal code must be exactly 6 digits", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate unit number input: should contain only digits or "NA"/"na"
        if (!unitNumber.matches("\\d*|(?i)na")) {
            Toast.makeText(this, "Unit number must be digits or 'NA'", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate label input: should not be empty
        if (label.isEmpty()) {
            Toast.makeText(this, "Label cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
