package com.example.apple.newsingit_project.manager.networkmanager;

import android.content.Context;

import com.example.apple.newsingit_project.manager.MyApplication;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by apple on 2016. 8. 29..
 */
public class NetworkManager {
    /**
     * 네트워크 자원
     **/
    //객체를 private로 선언하여 SingleTone Pattern적용//
    private static NetworkManager instance;
    OkHttpClient client;

    /**
     * 싱글톤 패턴을 적용해야지 여러 네트워크부분에서 같은 쿠키, client값을 공유할 수 있다
     **/
    private NetworkManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        Context context = MyApplication.getContext();

        //영속적 쿠키 설정//
        ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
        builder.cookieJar(cookieJar);

        //307에러를 해결하기 위한 설정.//
        builder.followRedirects(true);
        builder.addInterceptor(new RedirectInterceptor());

        //캐시파일을 생성//
        File cacheDir = new File(context.getCacheDir(), "network");

        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }

        //쿠키의 세션 만료시간 설정//
        Cache cache = new Cache(cacheDir, 10 * 1024 * 1024);
        builder.cache(cache);

        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);

        client = builder.build(); //클라이언트를 만든다.//
    }

    //싱글톤 적용//
    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }

        return instance;
    }

    public OkHttpClient getClient() {
        return client;
    }
}
