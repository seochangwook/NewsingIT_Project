package com.example.apple.newsingit_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
    /**
     * Facebook 관련 변수
     **/
    Button facebook_login_button; //로그인 버튼 커스텀//

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_layout);

        facebook_login_button = (Button) findViewById(R.id.btn_facebook_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        facebook_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_Login();
            }
        });
    }

    public void is_Login() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

        startActivity(intent);

        finish();
    }
}
