package com.example.apple.newsingit_project.manager.datamanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.apple.newsingit_project.manager.MyApplication;

/**
 * Created by apple on 2016. 9. 5..
 */
public class PropertyManager {
    //지정할 키값들을 정의//

    //일반 사용자 프로필 정보//
    private static final String KEY_NAME = "name";
    private static final String KEY_PF_URL = "pf_url";
    private static final String KEY_NT_FS = "nt_fs";
    private static final String KEY_NT_S = "nt_s";
    private static final String KEY_NT_F = "nt_f";

    //SNS, FCM등의 토큰//
    private static final String KEY_FACEBOOK_ID = "facebookid";
    private static final String KEY_FCM_REG_ID = "fcmtoken";
    private static final String ALARM_BADGE_NUMBER = "alarm_badge_number";

    //FCM알람관련 뱃지카운터.//
    private static int badge_number = 0;
    //PropertyManger는 고유의 데이터이기에 싱글톤 디자인 패턴으로 설계//
    private static PropertyManager instance;

    //공유 프래퍼런스 생성//
    SharedPreferences mPrefs;
    SharedPreferences.Editor mEdittor;

    private PropertyManager() {
        Context context = MyApplication.getContext(); //현재 앱의 자원을 얻어온다.//

        //프래퍼런스를 사용하도록 설정//
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        mEdittor = mPrefs.edit();
    }

    public static PropertyManager getInstance() {
        if (instance == null) {
            instance = new PropertyManager();
        }

        return instance;
    }

    //프래퍼런스에 저장된 값들을 불러온다.//
    public String get_name() {
        return mPrefs.getString(KEY_NAME, ""); //만약에 프래퍼런스가 없을 경우 ""로 나온다.//
    }

    public void set_name(String name) {
        mEdittor.putString(KEY_NAME, name);
        mEdittor.commit(); //저장 후 완료한다.//
    }

    public String get_pf_Url() {
        return mPrefs.getString(KEY_PF_URL, "");
    }

    public void set_pf_Url(String pf_Url) {
        mEdittor.putString(KEY_PF_URL, pf_Url);
        mEdittor.commit();
    }

    public String get_nt_fs() {
        return mPrefs.getString(KEY_NT_FS, "");
    }

    public void set_nt_fs(String nt_fs) {
        mEdittor.putString(KEY_NT_FS, nt_fs);
        mEdittor.commit();
    }

    public String get_nt_s() {
        return mPrefs.getString(KEY_NT_S, "");
    }

    public void set_nt_s(String nt_s) {
        mEdittor.putString(KEY_NT_S, nt_s);
        mEdittor.commit();
    }

    public String get_nt_f() {
        return mPrefs.getString(KEY_NT_F, "");
    }

    public void set_nt_f(String nt_f) {
        mEdittor.putString(KEY_NT_F, nt_f);
        mEdittor.commit();
    }

    public String get_facebookid() {
        return mPrefs.getString(KEY_FACEBOOK_ID, "");
    }

    public void set_facebookid(String facebookid) {
        mEdittor.putString(KEY_FACEBOOK_ID, facebookid);
        mEdittor.commit();
    }

    public String get_registerid() {
        return mPrefs.getString(KEY_FCM_REG_ID, "");
    }

    public void set_registerid(String registerid) {
        mEdittor.putString(KEY_FCM_REG_ID, registerid);
        mEdittor.commit();
    }

    public int get_badge_number() {
        return mPrefs.getInt(ALARM_BADGE_NUMBER, badge_number);
    }

    public void setBadge_number(int badge_number) {
        mEdittor.putInt(ALARM_BADGE_NUMBER, badge_number);
        mEdittor.commit();
    }


}
