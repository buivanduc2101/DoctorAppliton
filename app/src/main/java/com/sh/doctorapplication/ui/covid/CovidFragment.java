package com.sh.doctorapplication.ui.covid;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sh.doctorapplication.R;

public class CovidFragment extends Fragment {

    private View root;
    private WebView webView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_covid, container, false);
        goToWebSite();
        return root;
    }


    @SuppressLint("SetJavaScriptEnabled")
    private void goToWebSite() {
        WebView webView2 = root.findViewById(R.id.wbWebView);
        this.webView = webView2;
        webView2.getSettings().setJavaScriptEnabled(true);
        this.webView.getSettings().setAppCacheEnabled(true);
        this.webView.getSettings().setDatabaseEnabled(true);
        this.webView.getSettings().setDomStorageEnabled(true);
        this.webView.getSettings().setSupportZoom(false);
        this.webView.clearCache(true);
        this.webView.clearHistory();
        this.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        this.webView.getSettings().setBuiltInZoomControls(true);
        this.webView.getSettings().setGeolocationEnabled(true);
        this.webView.setWebViewClient(new WebViewClient() {
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
        this.webView.setWebChromeClient(new WebChromeClient() {
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
        webView.loadUrl("https://ncov.moh.gov.vn/");
    }

}
