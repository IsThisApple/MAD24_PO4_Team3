package sg.edu.np.mad.nearbuy;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private static final String PREFERENCES_FILE = "nearbuy_preferences";
    private static final String ACCESSIBILITY_ENABLED = "accessibility_enabled";

    private SharedPreferences sharedPreferences;

    public PreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
    }

    public void setAccessibilityEnabled(boolean enabled) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(ACCESSIBILITY_ENABLED, enabled);
        editor.apply();
    }

    public boolean isAccessibilityEnabled() {
        return sharedPreferences.getBoolean(ACCESSIBILITY_ENABLED, false);
    }

}