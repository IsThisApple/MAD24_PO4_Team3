package sg.edu.np.mad.nearbuy;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Toast;

public class DoubleClickListener implements View.OnTouchListener {
    private final View.OnClickListener singleClickListener;
    private final View.OnClickListener doubleClickListener;
    private long lastClickTime = 0;

    public DoubleClickListener(View.OnClickListener singleClickListener, View.OnClickListener doubleClickListener) {
        this.singleClickListener = singleClickListener;
        this.doubleClickListener = doubleClickListener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            long clickTime = SystemClock.uptimeMillis();
            if (clickTime - lastClickTime < ViewConfiguration.getDoubleTapTimeout()) {
                // double-click detected
                if (doubleClickListener != null) {
                    doubleClickListener.onClick(v);
                }
                lastClickTime = 0; // reset last click time
            } else {
                // single-click detected
                if (singleClickListener != null) {
                    singleClickListener.onClick(v);
                }
            }
            lastClickTime = clickTime;
            return true;
        }
        return false;
    }

}