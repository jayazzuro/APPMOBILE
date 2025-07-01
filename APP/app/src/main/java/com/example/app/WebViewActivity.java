package com.example.app;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WebViewActivity extends AppCompatActivity {
    WebView webView;
    private final String returnUrl = "http://192.168.1.129:3000/api/handleVNPayReturn";
    private final String momoReturnUrl = "http://192.168.1.129:3000/api/momo_return";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        setContentView(webView);

        String url = getIntent().getStringExtra("paymentUrl");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("WebViewURL", url);
                if (url.contains(returnUrl)) {
                    handleVNPayReturn(url);
                    return true;
                }

                // ✅ Xử lý callback từ MoMo
                else if (url.contains(momoReturnUrl)) {
                    handleMoMoReturn(url);
                    return true;
                }

                // ✅ Xử lý scheme momo:// để mở app MoMo
                else if (url.startsWith("momo://")) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(WebViewActivity.this, "Không tìm thấy ứng dụng MoMo", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }

                return false;
            }
        });

        webView.loadUrl(url);
    }
    private void handleVNPayReturn(String url) {
        boolean success = url.contains("vnp_ResponseCode=00");
        returnResult(success);
    }

    private void handleMoMoReturn(String url) {
        boolean success = url.contains("resultCode=0");
        returnResult(success);
    }

    private void returnResult(boolean success) {
        Intent result = new Intent();
        result.putExtra("paymentSuccess", success);
        setResult(success ? RESULT_OK : RESULT_CANCELED, result);
        finish();
    }
}