package com.dfl.demo.rsf.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WidgetWeb extends RecyclerView.ViewHolder {

    private final WebView web;

    public WidgetWeb(@NonNull Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.widget_web, null));
        web = itemView.findViewById(R.id.web);
        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient() {});
        web.setWebChromeClient(new WebChromeClient() {});
        web.loadUrl("https://m.jd.com");
    }
}
