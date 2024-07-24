package sg.edu.np.mad.nearbuy;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText messageInput;
    private Button sendButton;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private PreferenceManager preferenceManager;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("messages");

        recyclerView = findViewById(R.id.recyclerView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        preferenceManager = new PreferenceManager(this);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Message message = dataSnapshot.getValue(Message.class);
                if (message != null) {
                    messageList.add(message);
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                    recyclerView.scrollToPosition(messageList.size() - 1);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        sendButton.setOnClickListener(v -> {
            String messageText = messageInput.getText().toString().trim();
            if (!TextUtils.isEmpty(messageText)) {
                String sender = firebaseUser != null ? firebaseUser.getDisplayName() : "User";
                Message message = new Message(messageText, sender);

                Map<String, Object> messageValues = new HashMap<>();
                messageValues.put("text", message.getText());
                messageValues.put("sender", message.getSender());

                databaseReference.push().setValue(messageValues);

                messageInput.setText("");
            }
        });

        // initialise tts
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.US);
                }
            }
        });

        initializeNavigationBar();

        boolean isAccessibilityEnabled = preferenceManager.isAccessibilityEnabled();

        // add tts and double-click functionality to message input
        if (isAccessibilityEnabled) {
            messageInput.setOnTouchListener(new DoubleClickListener(
                    v -> speak("Type a message"),
                    v -> messageInput.requestFocus()
            ));
        } else {
            messageInput.setOnTouchListener(new DoubleClickListener(
                    v -> messageInput.requestFocus(),
                    v -> {}
            ));
        }

        // add tts and double-click functionality to send button
        if (isAccessibilityEnabled) {
            sendButton.setOnTouchListener(new DoubleClickListener(
                    v -> speak("Send"),
                    v -> sendButton.performClick()
            ));
        } else {
            sendButton.setOnTouchListener(new DoubleClickListener(
                    v -> sendButton.performClick(),
                    v -> {}
            ));
        }
    }

    private void initializeNavigationBar() {
        boolean isAccessibilityEnabled = preferenceManager.isAccessibilityEnabled();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_chat);

        // create listeners for each menu item

        View.OnClickListener homeSingleClickListener;
        View.OnClickListener homeDoubleClickListener = null;

        View.OnClickListener mapSingleClickListener;
        View.OnClickListener mapDoubleClickListener = null;

        View.OnClickListener chatSingleClickListener;
        View.OnClickListener chatDoubleClickListener = null;

        View.OnClickListener cartSingleClickListener;
        View.OnClickListener cartDoubleClickListener = null;

        if (isAccessibilityEnabled) {
            homeSingleClickListener = v -> speak("Open home");
            homeDoubleClickListener = v -> {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            };
        } else {
            homeSingleClickListener = v -> {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            };
        }

        if (isAccessibilityEnabled) {
            mapSingleClickListener = v -> speak("Open map");
            mapDoubleClickListener = v -> {
                startActivity(new Intent(getApplicationContext(), MapActivity.class));
                finish();
            };
        } else {
            mapSingleClickListener = v -> {
                startActivity(new Intent(getApplicationContext(), MapActivity.class));
                finish();
            };
        }

        if (isAccessibilityEnabled) {
            chatSingleClickListener = v -> speak("Already in chat");
            chatDoubleClickListener = v -> Toast.makeText(this, "It is on the page already", Toast.LENGTH_SHORT).show();
        } else {
            chatSingleClickListener = v -> Toast.makeText(this, "It is on the page already", Toast.LENGTH_SHORT).show();
        }

        if (isAccessibilityEnabled) {
            cartSingleClickListener = v -> speak("Open cart");
            cartDoubleClickListener = v -> {
                startActivity(new Intent(getApplicationContext(), ShoppingCart.class));
                finish();
            };
        } else {
            cartSingleClickListener = v -> {
                startActivity(new Intent(getApplicationContext(), ShoppingCart.class));
                finish();
            };
        }

        // set listeners
        bottomNavigationView.findViewById(R.id.bottom_home).setOnTouchListener(new DoubleClickListener(homeSingleClickListener, homeDoubleClickListener));
        bottomNavigationView.findViewById(R.id.bottom_map).setOnTouchListener(new DoubleClickListener(mapSingleClickListener, mapDoubleClickListener));
        bottomNavigationView.findViewById(R.id.bottom_chat).setOnTouchListener(new DoubleClickListener(chatSingleClickListener, chatDoubleClickListener));
        bottomNavigationView.findViewById(R.id.bottom_cart).setOnTouchListener(new DoubleClickListener(cartSingleClickListener, cartDoubleClickListener));

        final View rootView = findViewById(android.R.id.content);

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = rootView.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;


                if (keypadHeight > screenHeight * 0.15) { // assume that the keyboard is up if the height of the remaining part is greater than 15% of the screen height
                    bottomNavigationView.setVisibility(View.GONE);
                } else {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    // method to speak text using tts
    private void speak(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
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