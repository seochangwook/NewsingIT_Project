package com.example.apple.newsingit_project;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.apple.newsingit_project.data.json_data.login.LoginRequest;
import com.example.apple.newsingit_project.data.json_data.login.LoginRequestResult;
import com.example.apple.newsingit_project.manager.datamanager.PropertyManager;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.example.apple.newsingit_project.service.fcm.QuickstartPreferences;
import com.example.apple.newsingit_project.service.fcm.RegistrationIntentService;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.DefaultAudience;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "LoginActivity";

    Button facebook_login_button; //로그인 버튼 커스텀//

    /**
     * Facebook 관련 변수
     **/
    LoginManager mLoginManager;
    //AccessTokenTracker tracker;
    String token;
    String id, name;
    /** 공공저장소 관련 **/
    SharedPreferences mPrefs; //공유 프래퍼런스 정의.(서버가 토큰 비교 후 반환해 준 id를 기존에 저장되어 있는 id값과 비교하기 위해)//
    SharedPreferences.Editor mEditor; //프래퍼런스 에디터 정의//
    /** 네트워크 관련 변수 **/
    NetworkManager manager;
    String register_id;
    private CallbackManager callbackManager; //세션연결 콜백관리자.//
    /** FCM 관련 변수 **/
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private BackPressCloseHandler backPressCloseHandler; //뒤로가기 처리//

    private Callback requestloginCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());

            logoutFacebook(); //실패하면 꼬임을 방지하기 위해서 로그아웃을 한다.//
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String responseData = response.body().string();

            Log.d("json data", responseData);

            Gson gson = new Gson();

            LoginRequest loginRequest = gson.fromJson(responseData, LoginRequest.class);

            set_Profile_Data(loginRequest.getResult());
        }
    };

    public void set_Profile_Data(LoginRequestResult loginRequestResult) {
        /** 사용자의 프로필정보를 가져온다. **/
        String name = loginRequestResult.getName();
        String pf_Url = loginRequestResult.getPf_url();
        String id = loginRequestResult.getId();
        boolean nt_fs = loginRequestResult.getNt_fs();
        boolean nt_f = loginRequestResult.getNt_f();
        boolean nt_s = loginRequestResult.getNt_s();

        //파싱된 데이터를 공유 데이터에 저장하고 메인 액티비티로 이동.//
        //프래퍼런스를 셋팅.//
        mPrefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        mEditor = mPrefs.edit();

        PropertyManager.getInstance().set_facebookid(id);
        PropertyManager.getInstance().set_name(name);
        PropertyManager.getInstance().set_pf_Url(pf_Url);
        PropertyManager.getInstance().set_nt_fs("" + nt_fs);
        PropertyManager.getInstance().set_nt_f("" + nt_f);
        PropertyManager.getInstance().set_nt_s("" + nt_s);

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

        startActivity(intent);

        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.login_activity_layout);

        registBroadcastReceiver();

        facebook_login_button = (Button) findViewById(R.id.btn_test);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        printKeyHash(); //해시값 출력//

        callbackManager = CallbackManager.Factory.create(); //onActivityResult설정.//

        facebook_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogin()) {
                    //     logoutFacebook();
                } else {
                    getInstanceIdToken(); //FCM ID값 획득//
                }
            }
        });

        backPressCloseHandler = new BackPressCloseHandler(this);

        mLoginManager = LoginManager.getInstance(); //로그인 매니저 등록//
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_READY));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_GENERATING));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    /**
     * 앱이 화면에서 사라지면 등록된 LocalBoardcast를 모두 삭제한다.
     */
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /**
     * Instance ID를 이용하여 디바이스 토큰을 가져오는 RegistrationIntentService를 실행한다.
     */
    public void getInstanceIdToken() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    public void registBroadcastReceiver() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();


                if (action.equals(QuickstartPreferences.REGISTRATION_READY)) {
                    // 액션이 READY일 경우

                } else if (action.equals(QuickstartPreferences.REGISTRATION_GENERATING)) {
                    // 액션이 GENERATING일 경우

                } else if (action.equals(QuickstartPreferences.REGISTRATION_COMPLETE)) {
                    // 액션이 COMPLETE일 경우
                    String token = intent.getStringExtra("token");
                    register_id = token;

                    Log.d("token fcm id : ", register_id);

                    //토큰을 받은 이 후 로그인을 진행한다.//
                    //토큰을 받지 못하면 로그인 과정을 진행하지 않는다.//
                    loginFacebook();
                }
            }
        };
    }

    /**
     * Google Play Service를 사용할 수 있는 환경이지를 체크한다.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        backPressCloseHandler.onBackPressed();
    }

    private void printKeyHash() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.apple.newsingit_project", PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("KeyHash:", e.toString());
        }
    }

    private boolean isLogin()
    {
        AccessToken token = AccessToken.getCurrentAccessToken();

        return token != null; //로그인 구분.//
    }

    private void logoutFacebook() {
        mLoginManager.logOut(); //로그아웃.//
    }

    private void loginFacebook()
    {
        mLoginManager.setDefaultAudience(DefaultAudience.FRIENDS);
        mLoginManager.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);

        //콜백등록.//
        mLoginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Access Token값을 가져온다.//
                AccessToken accessToken = AccessToken.getCurrentAccessToken();

                token = accessToken.getToken();

                Log.d("token : ", token);

                String user_id = accessToken.getUserId();

                //해당 토큰값을 서버로 전송한다.//
                Facebook_LoginServer(); //페이스북 로그인//

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        //기존 제공해주는 로그인 버튼으로도 가능.//
        mLoginManager.logInWithReadPermissions(LoginActivity.this, Arrays.asList("email")); //이메일 획득 권한//
    }

    public void Facebook_LoginServer() {
        /** 네트워크 설정 **/
        /** Network 자원을 설정 **/
        manager = NetworkManager.getInstance(); //싱글톤 객체를 가져온다.//

        /** Client 설정 **/
        OkHttpClient client = manager.getClient();

        /** POST방식의 프로토콜 Scheme 정의 **/
        //우선적으로 Url을 만든다.//
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("https"); //인증서가 있어야 가능//
        builder.host(getResources().getString(R.string.real_server_domain));
        builder.port(4433); //https인증은 포트가 4433이다.(인증서 파일이 필요)//
        builder.addPathSegment("auth");
        builder.addPathSegment("facebook");
        builder.addPathSegment("token");

        /** RequestBody 설정 **/
        RequestBody body = new FormBody.Builder()
                .add("access_token", token) //페이스북 토큰//
                .add("registration_token", register_id) //FCM토큰//
                .build();

        /** Request 설정 **/
        //최종적으로 Request 구성//
        Request request = new Request.Builder()
                .url(builder.build())
                .post(body)
                .tag(this)
                .build();

        client.newCall(request).enqueue(requestloginCallback);
    }

    //인증에 대한 결과를 받는다.//
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** Facebook 로그인 관련 **/
        //등록이 되어있어야지 정상적으로 onSuccess에서 정보를 받아온다.//
        callbackManager.onActivityResult(requestCode, resultCode, data); //

        Log.d("myLog", "requestCode  " + requestCode);
        Log.d("myLog", "resultCode" + resultCode);
        Log.d("myLog", "data  " + data.toString());

    }

    public class BackPressCloseHandler
    {
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
