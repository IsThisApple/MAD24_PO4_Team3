package sg.edu.np.mad.nearbuy;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class AddAddress extends AppCompatActivity {
    private EditText streetInput;
    private EditText postalCodeInput;
    private EditText unitNumberInput;
    private EditText labelInput;
    private String currentUsername;
    private PreferenceManager preferenceManager;
    private TextToSpeech tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        preferenceManager = new PreferenceManager(this);

        // initialise tts
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.US);
                }
            }
        });

        currentUsername = getIntent().getStringExtra("username"); // Get username from intent

        setupTTSAndClickListeners();
    }

    private void setupTTSAndClickListeners() {
        boolean isAccessibilityEnabled = preferenceManager.isAccessibilityEnabled();

        streetInput = findViewById(R.id.streetName);
        postalCodeInput = findViewById(R.id.postalCode);
        unitNumberInput = findViewById(R.id.unitNumber);
        labelInput = findViewById(R.id.label);
        Button addButton = findViewById(R.id.addAdd);
        ImageView backbtn = findViewById(R.id.backbtn);

        if(isAccessibilityEnabled) {
            streetInput.setOnTouchListener(new DoubleClickListener(
                    v -> speakText("Enter street name"),
                    v -> {
                        speakText("Enter street name");
                        streetInput.requestFocus(); // double-click action
                    }
            ));
        } else {
            streetInput.setOnTouchListener(new DoubleClickListener(
                    v -> streetInput.requestFocus(),
                    v -> {}
            ));
        }

        if(isAccessibilityEnabled) {
            postalCodeInput.setOnTouchListener(new DoubleClickListener(
                    v -> speakText("Enter postal code"),
                    v -> {
                        speakText("Enter postal code");
                        postalCodeInput.requestFocus(); // double-click action
                    }
            ));
        } else {
            postalCodeInput.setOnTouchListener(new DoubleClickListener(
                    v -> postalCodeInput.requestFocus(),
                    v -> {}
            ));
        }

        if(isAccessibilityEnabled) {
            unitNumberInput.setOnTouchListener(new DoubleClickListener(
                    v -> speakText("Enter unit number"),
                    v -> {
                        speakText("Enter unit number");
                        unitNumberInput.requestFocus(); // double-click action
                    }
            ));
        } else {
            unitNumberInput.setOnTouchListener(new DoubleClickListener(
                    v -> unitNumberInput.requestFocus(),
                    v -> {}
            ));
        }

        if(isAccessibilityEnabled) {
            labelInput.setOnTouchListener(new DoubleClickListener(
                    v -> speakText("Enter label"),
                    v -> {
                        speakText("Enter label");
                        labelInput.requestFocus(); // double-click action
                    }
            ));
        } else {
            labelInput.setOnTouchListener(new DoubleClickListener(
                    v -> labelInput.requestFocus(),
                    v -> {}
            ));
        }

        String street = streetInput.getText().toString();
        String postalCode = postalCodeInput.getText().toString();
        String unitNumber = unitNumberInput.getText().toString();
        String label = labelInput.getText().toString();

        if(isAccessibilityEnabled) {
            addButton.setOnTouchListener(new DoubleClickListener(
                    v -> speakText("Add address"),
                    v -> {
                        speakText("Add address");
                        if (validateInputs(this, streetInput.getText().toString().trim(), postalCodeInput.getText().toString().trim(), unitNumberInput.getText().toString().trim(), labelInput.getText().toString().trim())) {
                            DBAddress dbAddress = new DBAddress(AddAddress.this);
                            if (dbAddress.isLabelUnique(labelInput.getText().toString().trim()) && dbAddress.isPostalCodeUnique(postalCodeInput.getText().toString().trim())) {
                                dbAddress.addAddress(streetInput.getText().toString().trim(), postalCodeInput.getText().toString().trim(), unitNumberInput.getText().toString().trim(), labelInput.getText().toString().trim()); // Pass username
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                Toast.makeText(AddAddress.this, "Label or postal code must be unique", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            ));

        } else {
            addButton.setOnTouchListener(new DoubleClickListener(
                    v -> {
                        if (validateInputs(this, streetInput.getText().toString().trim(), postalCodeInput.getText().toString().trim(), unitNumberInput.getText().toString().trim(), labelInput.getText().toString().trim())) {
                            DBAddress dbAddress = new DBAddress(AddAddress.this);
                            if (dbAddress.isLabelUnique(labelInput.getText().toString().trim()) && dbAddress.isPostalCodeUnique(postalCodeInput.getText().toString().trim())) {
                                dbAddress.addAddress(streetInput.getText().toString().trim(), postalCodeInput.getText().toString().trim(), unitNumberInput.getText().toString().trim(), labelInput.getText().toString().trim()); // Pass username
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                Toast.makeText(AddAddress.this, "Label or postal code must be unique", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    v -> {}
            ));

        }

        if(isAccessibilityEnabled) {
            backbtn.setOnTouchListener(new DoubleClickListener(
                    v -> speakText("Back to payment"),
                    v -> {
                        speakText("Back to payment");
                        finish();
                        // double-click action
                    }
            ));
        } else {
            backbtn.setOnTouchListener(new DoubleClickListener(
                    v -> finish(),
                    v -> {}
            ));
        }
    }

    private boolean validateInputs(Context context, String street, String postalCode, String unitNumber, String label) {

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


    private void speakText(String text) {
        if (tts != null && !tts.isSpeaking()) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
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