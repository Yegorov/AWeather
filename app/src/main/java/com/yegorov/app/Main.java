package com.yegorov.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class Main extends Activity {

    private WebView webView;
    private Button btnSignInVk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        webView = (WebView) findViewById(R.id.webView);
        btnSignInVk = (Button) findViewById(R.id.singInVk);

        webView.setVisibility(View.INVISIBLE);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {

                if(url.contains("oauth.vk.com/blank.html#access_token")) {
                    view.setVisibility(View.INVISIBLE);

                    Uri uri = Uri.parse(url);

                    String access = url.split("access_token=")[1];

                    uri = Uri.parse("?" +uri.getFragment());

                    String access_token = uri.getQueryParameter("access_token");
                    String user_id =  uri.getQueryParameter("user_id");

                    Intent intent = new Intent(getApplicationContext(), Weather.class);
                    intent.putExtra(ConstKey.ACCESS_TOKEN, access_token);
                    intent.putExtra(ConstKey.USER_ID, user_id);

                    startActivity(intent);

                    Log.d("ACCESS_TOKEN", access);
                    Log.d("ACCESS_TOKEN", access_token);
                    Log.d("ACCESS_TOKEN", user_id);
                }
                else {
                    webView.setVisibility(View.VISIBLE);
                }

            }
        });


    }

    public void OnClickBtnSignInVk(View v) {
        webView.loadUrl("https://oauth.vk.com/authorize?" +
                "client_id=" + ConstKey.VK_APP_ID +
                "&scope=wall" +
                "&redirect_uri=https://oauth.vk.com/blank.html" +
                "&display=mobile" +
                "&v=5.28" +
                "&response_type=token" +
                "&revoke=0");

    }

}