package com.example.apple.newsingit_project.view.view_fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.example.apple.newsingit_project.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewFragment extends Fragment {

    WebView webView;

    public WebViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_web_view, container, false);

        Bundle bundle = getArguments();
        String url = bundle.getString("URL");
        webView = (WebView) view.findViewById(R.id.webView);
        webView.loadUrl(url);

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("message", "ondetach");
    }

}
