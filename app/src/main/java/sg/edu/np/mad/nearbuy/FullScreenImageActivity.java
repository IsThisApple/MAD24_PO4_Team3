package sg.edu.np.mad.nearbuy;


import android.graphics.Matrix;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;


import java.util.List;


public class FullScreenImageActivity extends AppCompatActivity {


    private ImageView fullScreenImageView;
    private List<Integer> images;
    private int currentIndex = 0;
    private boolean isZoomedIn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);


        fullScreenImageView = findViewById(R.id.full_screen_image);
        images = (List<Integer>) getIntent().getSerializableExtra("images");
        currentIndex = getIntent().getIntExtra("index", 0);


        if (images != null && !images.isEmpty()) {
            fullScreenImageView.setImageResource(images.get(currentIndex));
        }


        fullScreenImageView.setOnClickListener(v -> toggleFullScreen());
        fullScreenImageView.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(FullScreenImageActivity.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    toggleZoom(e.getX(), e.getY());
                    return true;
                }
            });


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });


        findViewById(R.id.arrow_left_full).setOnClickListener(v -> showPreviousImage());
        findViewById(R.id.arrow_right_full).setOnClickListener(v -> showNextImage());
        findViewById(R.id.close_button).setOnClickListener(v -> finish());
    }


    private void toggleFullScreen() {
        if (!isZoomedIn) {
            findViewById(R.id.arrow_left_full).setVisibility(View.VISIBLE);
            findViewById(R.id.arrow_right_full).setVisibility(View.VISIBLE);
            findViewById(R.id.close_button).setVisibility(View.VISIBLE);
        }
    }


    private void toggleZoom(float x, float y) {
        if (isZoomedIn) {
            fullScreenImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            isZoomedIn = false;
        } else {
            fullScreenImageView.setScaleType(ImageView.ScaleType.MATRIX);
            Matrix matrix = new Matrix();
            fullScreenImageView.getImageMatrix().invert(matrix);
            matrix.postScale(1.5f, 1.5f, x, y);
            fullScreenImageView.setImageMatrix(matrix);
            isZoomedIn = true;


            findViewById(R.id.arrow_left_full).setVisibility(View.GONE);
            findViewById(R.id.arrow_right_full).setVisibility(View.GONE);
            findViewById(R.id.close_button).setVisibility(View.GONE);
        }
    }


    private void showPreviousImage() {
        if (images != null && currentIndex > 0) {
            currentIndex--;
            fullScreenImageView.setImageResource(images.get(currentIndex));
        }
    }


    private void showNextImage() {
        if (images != null && currentIndex < images.size() - 1) {
            currentIndex++;
            fullScreenImageView.setImageResource(images.get(currentIndex));
        }
    }
}