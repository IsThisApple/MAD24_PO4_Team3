package sg.edu.np.mad.nearbuy;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

public class TextToSpeechInstance {
    private static TextToSpeech tts;

    private TextToSpeechInstance() {
        // private constructor to prevent instantiation
    }

    public static synchronized TextToSpeech getInstance(Context context) {
        if (tts == null) {
            tts = new TextToSpeech(context.getApplicationContext(), status -> {
                if (status == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.US);
                }
            });
        }
        return tts;
    }

}