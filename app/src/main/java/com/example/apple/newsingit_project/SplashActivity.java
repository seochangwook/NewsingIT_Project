package com.example.apple.newsingit_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.apple.newsingit_project.manager.datamanager.PropertyManager;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    Handler mHandler;

    /**
     * Network variable
     **/
    NetworkManager manager;
    LoginManager mLoginManager;
    /**
     * SharedPreference(PropertyManager) 정보
     **/
    SharedPreferences mPrefs; //공유 프래퍼런스 정의.(서버가 토큰 비교 후 반환해 준 id를 기존에 저장되어 있는 id값과 비교하기 위해)//
    SharedPreferences.Editor mEditor; //프래퍼런스 에디터 정의//
    /**
     * Callback메소드 등록
     **/
    private Callback requestmainnewslistcallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) //접속 실패의 경우.//
        {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());

            //에러가 나면 우선 문제가 있다는 것이니 로그인 화면으로 이동.//
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);

            startActivity(intent);

            finish();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String response_data = response.body().string();

            if (response.code() == 401) //로그인 안함.//
            {
                Log.d("json data", response_data);

                //로그인으로 이동하기 전 로그아웃을 한다.//
                mLoginManager.logOut();

                //공유 프래퍼런스 초기화.//
                PropertyManager.getInstance().set_facebookid("");
                PropertyManager.getInstance().set_name("");
                PropertyManager.getInstance().set_pf_Url("");
                PropertyManager.getInstance().set_nt_fs("");
                PropertyManager.getInstance().set_nt_f("");
                PropertyManager.getInstance().set_nt_s("");

                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);

                startActivity(intent);

                finish();
            } else if (response.code() == 200) {
                Log.d("json data", response_data);

                //보안 상 한번 더 비교해본다. 즉 로그인은 되었지만 다른 사용자일 수 있기에 기존 정보랑 비교//
                //만약 요기서 실패 시 다시 로그인으로 이동//
                is_profile_check();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.splah_activity_layout);

        mHandler = new Handler(Looper.getMainLooper());

        //프래퍼런스를 셋팅.//
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPrefs.edit();

        mLoginManager = LoginManager.getInstance(); //로그인 매니저 등록//

        Auto_Login();
    }

    public void Auto_Login() {
        /** 자동로그인 처리 매커니즘 **/
        runOnUiThread(new Runnable() {
            public void run() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //최초 앱을 실행 시 로그인이 되어있는지 검사//
                        is_login_check();
                    }
                }, 1500);
            }
        });
    }

    public void is_login_check() {
        /** Network 설정 **/
        /** Network 자원을 설정 **/
        manager = NetworkManager.getInstance(); //싱글톤 객체를 가져온다.//

        /** Client 설정 **/
        OkHttpClient client = manager.getClient();

        /** GET방식의 프로토콜 Scheme 정의 **/
        //우선적으로 Url을 만든다.//
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("http");
        builder.host(getResources().getString(R.string.real_server_domain));
        builder.port(8080);
        builder.addPathSegment("newscontents");

        /** Request 설정 **/
        Request request = new Request.Builder()
                .url(builder.build())
                .tag(this)
                .build();

        /** 비동기 방식(enqueue)으로 Callback 구현 **/
        client.newCall(request).enqueue(requestmainnewslistcallback);
    }

    public void is_profile_check() {
        //이름, 페이스북 id로 비교 (공유 저장소에 있는 정보를 불러온다.)//
        String name = PropertyManager.getInstance().get_name();
        String facebookid = PropertyManager.getInstance().get_facebookid();
        String fcm_id = PropertyManager.getInstance().get_registerid();
        String pf_Url = PropertyManager.getInstance().get_pf_Url();
        String nt_fs = PropertyManager.getInstance().get_nt_fs();
        String nt_s = PropertyManager.getInstance().get_nt_s();
        String nt_f = PropertyManager.getInstance().get_nt_f();

        //이름하고 페이스북 id가 없는 경우//
        if (name.equals("") && facebookid.equals("")) {
            Log.d("login message", "account fail");

            //로그인으로 이동하기 전 로그아웃을 한다.//
            mLoginManager.logOut();

            //공유 프래퍼런스 초기화.//
            PropertyManager.getInstance().set_facebookid("");
            PropertyManager.getInstance().set_name("");
            PropertyManager.getInstance().set_pf_Url("");
            PropertyManager.getInstance().set_nt_fs("");
            PropertyManager.getInstance().set_nt_f("");
            PropertyManager.getInstance().set_nt_s("");

            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);

            startActivity(intent);

            finish();
        } else  //로그인도 되어 있고 공유 프래퍼런스에 저장도 된 경우//
        {
            Log.d("profile", name);
            Log.d("profile", facebookid);
            Log.d("profile", fcm_id);
            Log.d("profile", pf_Url);
            Log.d("profile", nt_fs);
            Log.d("profile", nt_f);
            Log.d("profile", nt_s);

            //메인 화면으로 이동//
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);

            startActivity(intent);

            finish();
        }
    }
}
