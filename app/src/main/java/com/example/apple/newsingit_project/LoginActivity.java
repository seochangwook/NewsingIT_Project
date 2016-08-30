package com.example.apple.newsingit_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    /**
     * Facebook 관련 변수
     **/
    Button facebook_login_button; //로그인 버튼 커스텀//
    Button btnTwitterLogin;
    Button btnGoogleLodin;

    private BackPressCloseHandler backPressCloseHandler; //뒤로가기 처리//

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_layout);

        facebook_login_button = (Button) findViewById(R.id.btn_facebook_login);
        btnGoogleLodin = (Button) findViewById(R.id.btn_google_login);
        btnTwitterLogin = (Button) findViewById(R.id.btn_twitter_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        facebook_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_Login();
            }
        });

        btnGoogleLodin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_Login();
            }
        });

        btnTwitterLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                is_Login();
//                //twitter를 dialog로 띄움
//                Intent intent = new Intent(LoginActivity.this, TwitterLinkActivity.class);
//
//                startActivity(intent);
            }
        });

        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        backPressCloseHandler.onBackPressed();
    }

    public void is_Login() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

        startActivity(intent);

        finish();
    }

    public class BackPressCloseHandler {
        private long backKeyPressedTime = 0;
        private Toast toast;

        private Activity activity;

        public BackPressCloseHandler(Activity context) {
            this.activity = context;
        }

        public void onBackPressed() {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                showGuide();

                return;
            }

            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                activity.finish();
                toast.cancel();
            }
        }

        private void showGuide() {
            toast = Toast.makeText(activity, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);

            toast.show();
        }
    }
}
