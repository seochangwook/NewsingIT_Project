package com.example.apple.newsingit_project;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.DefaultAudience;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    /**
     * Facebook 관련 변수
     **/
    Button facebook_login_button; //로그인 버튼 커스텀//
    Button btnTwitterLogin;
    Button btnGoogleLodin;

    Button btnTest;
    LoginManager mLoginManager;
    AccessTokenTracker tracker;
    ImageView profile_image;
    String id, name, profile_link, profile_img_url, user_name, user_email;
    ImageTask get_profile_image_task_facebook; //계정 이미지 불러오기 작업//


    private CallbackManager callbackManager; //세션연결 콜백관리자.//

    private BackPressCloseHandler backPressCloseHandler; //뒤로가기 처리//

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.login_activity_layout);

        facebook_login_button = (Button) findViewById(R.id.btn_facebook_login);
        btnGoogleLodin = (Button) findViewById(R.id.btn_google_login);
        btnTwitterLogin = (Button) findViewById(R.id.btn_twitter_login);

        btnTest = (Button) findViewById(R.id.btn_test);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        printKeyHash();
        callbackManager = CallbackManager.Factory.create(); //onActivityResult설정.//

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
            }
        });

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogin()) {
                    //     logoutFacebook();
                } else {
                    loginFacebook();
                }
            }
        });

        backPressCloseHandler = new BackPressCloseHandler(this);

        mLoginManager = LoginManager.getInstance();

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

    public void set_user_image(String user_id) {
        get_profile_image_task_facebook = new ImageTask(user_id);
        get_profile_image_task_facebook.execute(); //스레드 작업 실행//
    }

    private boolean isLogin() {
        AccessToken token = AccessToken.getCurrentAccessToken();

        return token != null; //로그인 구분.//
    }

    private void logoutFacebook() {
        mLoginManager.logOut(); //로그아웃.//

        profile_image.setImageResource(R.drawable.facebook_people_image);
    }

    private void loginFacebook() {
        mLoginManager.setDefaultAudience(DefaultAudience.FRIENDS);
        mLoginManager.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);

        //콜백등록.//
        mLoginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Access Token값을 가져온다.//
                AccessToken accessToken = AccessToken.getCurrentAccessToken();

                String token = accessToken.getToken();

                Log.d("token : ", token);

                String user_id = accessToken.getUserId();

                //해당 토큰값을 서버로 전송한다.//

                Toast.makeText(LoginActivity.this, "Token : " + token + "/ user id : " + user_id, Toast.LENGTH_SHORT).show();

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
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

    class ImageTask extends AsyncTask<Void, Void, Boolean> {
        Bitmap bitmap;
        private String URL_Address = "";
        private boolean isCheck = false; //이미지가 처음엔 다운로드 실패했다는 가정//

        //URL주소를 셋팅하는 생성자.//
        public ImageTask(String user_token_id) {
            URL_Address = "https://graph.facebook.com/";
            URL_Address = URL_Address + user_token_id + "/";
            URL_Address = URL_Address + "picture";
        }

        @Override
        protected void onPreExecute() {
            Log.i("image download file : ", URL_Address);
        }

        @Override
        protected Boolean doInBackground(Void... data) {
            //이미지 다운로드 작업 시작 및 네트워크 작업 진행//
            try {
                URL url = new URL(URL_Address); //연결한 URL주소 셋팅//

                try {
                    //네트워크 객체 선언.//
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect(); //연결//

                    InputStream is = conn.getInputStream(); //스트림 객체 선언//

                    Log.i("image value : ", "" + is);

                    bitmap = BitmapFactory.decodeStream(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            if (bitmap != null) {
                isCheck = true;
            }

            return isCheck;
        }

        @Override
        protected void onPostExecute(Boolean image_check) //메인 UI와 통신가능//
        {
            if (image_check == true) {
                profile_image.setImageBitmap(bitmap); //이미지 초기화//
            } else if (image_check == false) {
                profile_image.setImageResource(R.drawable.not_image);
            }
        }
    }

}
