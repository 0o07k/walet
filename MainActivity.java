package com.vocabwallet.app;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.json.JSONArray;

import java.util.Locale;

/**
 * Hosts the offline app page (assets/index.html) in a WebView.
 * Android WebView does not provide speechSynthesis, so pronunciation and
 * clipboard are exposed to the page through the "AndroidBridge" object.
 */
public class MainActivity extends Activity {

    private WebView webView;
    private TextToSpeech tts;
    private boolean ttsReady = false;
    private String pendingSpeech = null;
    private JSONArray pendingGroup = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        setContentView(webView);

        WebSettings s = webView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);   // localStorage keeps the vocabulary
        s.setAllowFileAccess(true);
        s.setCacheMode(WebSettings.LOAD_DEFAULT);

        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new Bridge(), "AndroidBridge");

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int r = tts.setLanguage(Locale.US);
                ttsReady = r != TextToSpeech.LANG_MISSING_DATA && r != TextToSpeech.LANG_NOT_SUPPORTED;
                tts.setSpeechRate(0.9f);
                if (ttsReady && pendingSpeech != null) {
                    playWord(pendingSpeech);
                }
                pendingSpeech = null;
            }
        });
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override public void onStart(String id) { }
            @Override public void onDone(String id) {
                if ("last".equals(id)) notifyGroupDone();
            }
            @Override public void onError(String id) {
                if ("last".equals(id)) notifyGroupDone();
            }
        });

        webView.loadUrl("file:///android_asset/index.html");
    }

    private void playWord(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "word");
    }

    private void notifyGroupDone() {
        runOnUiThread(() -> webView.evaluateJavascript("window.__speakDone && window.__speakDone();", null));
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }

    public class Bridge {

        @JavascriptInterface
        public void speak(String text) {
            if (!ttsReady) {
                pendingSpeech = text;
                return;
            }
            tts.stop();
            playWord(text);
        }

        @JavascriptInterface
        public void speakAll(String jsonArray) {
            try {
                JSONArray words = new JSONArray(jsonArray);
                if (!ttsReady || words.length() == 0) {
                    notifyGroupDone();
                    return;
                }
                tts.stop();
                for (int i = 0; i < words.length(); i++) {
                    String id = (i == words.length() - 1) ? "last" : "w" + i;
                    tts.speak(words.getString(i), TextToSpeech.QUEUE_ADD, null, id);
                }
            } catch (Exception e) {
                notifyGroupDone();
            }
        }

        @JavascriptInterface
        public void stop() {
            if (tts != null) tts.stop();
        }

        @JavascriptInterface
        public void copy(String text) {
            runOnUiThread(() -> {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (cm != null) cm.setPrimaryClip(ClipData.newPlainText("vocab", text));
            });
        }
    }
}
