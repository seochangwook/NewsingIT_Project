package com.example.apple.newsingit_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    Handler mHandler;

    /**
     * Facebook 자동 로그인 인증을 위한 관련 변수
     **/


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splah_activity_layout);

        mHandler = new Handler(Looper.getMainLooper());

        Auto_Login();
    }

    public void Auto_Login() {
        /** 자동로그인 처리 매커니즘 **/
        runOnUiThread(new Runnable() {
            public void run() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));

                        finish();
                    }
                }, 2000);
            }
        });
    }
}
