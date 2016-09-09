package com.example.apple.newsingit_project.widget.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.DrawerGroup;
import com.example.apple.newsingit_project.manager.datamanager.PropertyManager;
import com.example.apple.newsingit_project.manager.networkmanager.NetworkManager;
import com.kyleduo.switchbutton.SwitchButton;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Tacademy on 2016-08-23.
 */
public class DrawerAdapter extends BaseExpandableListAdapter {
    DrawerGroup[] items;

    SwitchButton Switch_new_sscrap;
    SwitchButton Switch_my_scrap_favorite;
    SwitchButton Switch_my_page_follow;

    Context context;

    /**
     * Network관련 변수
     **/
    NetworkManager networkManager;

    /**
     * 공유 프래퍼런스 관련 변수
     **/
    SharedPreferences mPrefs; //공유 프래퍼런스 정의.(서버가 토큰 비교 후 반환해 준 id를 기존에 저장되어 있는 id값과 비교하기 위해)//
    SharedPreferences.Editor mEditor; //프래퍼런스 에디터 정의//

    private Callback requestnewscrapalarmoncallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String responseData = response.body().string();

            Log.d("json data", responseData);
        }
    };
    private Callback requestnewscrapalarmoffcallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String responseData = response.body().string();

            Log.d("json data", responseData);
        }
    };
    private Callback requestmyscrapalarmoncallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String responseData = response.body().string();

            Log.d("json data", responseData);
        }
    };
    private Callback requestmyscrapalarmoffcallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String responseData = response.body().string();

            Log.d("json data", responseData);
        }
    };
    private Callback requestmypagefollowoncallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String responseData = response.body().string();

            Log.d("json data", responseData);
        }
    };
    private Callback requestmypagefollowoffcallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String responseData = response.body().string();

            Log.d("json data", responseData);
        }
    };

    public DrawerAdapter(DrawerGroup[] items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return items.length;
    }

    @Override
    public int getChildrenCount(int i) {
        return items[i].childViewList.size();
    }

    @Override
    public Object getGroup(int i) {
        return items[i];
    }

    @Override
    public Object getChild(int i, int child) {
        return items[i].childViewList.get(child);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int childposition) {
        return childposition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean expanded, View convertView, ViewGroup parent) {
        View view;
        TextView groupView;
        final ImageView imageView;

        if(convertView == null){
            view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.view_drawer_group, parent, false);
            groupView = (TextView)view.findViewById(R.id.text_group);
            imageView= (ImageView)view.findViewById(R.id.img_indicator);

            //child를 포함하고 있는 group에만 indicator로 쓸 image를 VISIBLE로 설정한다//
            if(items[groupPosition].name.equals("알림 설정"))
            {
                imageView.setImageResource(R.mipmap.ic_arrow_dropdown);
                imageView.setVisibility(View.VISIBLE);
            }else {
                imageView.setVisibility(View.GONE);
            }

        }else{
            view = convertView;
            groupView = (TextView)view.findViewById(R.id.text_group);
            imageView= (ImageView)view.findViewById(R.id.img_indicator);
            if(items[groupPosition].name.equals("알림 설정")){
                imageView.setVisibility(View.VISIBLE);
            }else {
                imageView.setVisibility(View.GONE);
            }

        }
        groupView.setText(items[groupPosition].name);

        //group indicator를 custom한다//
        if(expanded){ //group이 펼쳐졌을 때 indicator 이미지//
            imageView.setImageResource(R.mipmap.ic_arrow);
        }else {  //group이 닫혔을 때 indicator 이미지//
            imageView.setImageResource(R.mipmap.ic_arrow_dropdown);
        }

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean expanded, View convertView, ViewGroup parent) {
        View view;
        TextView childView;
        String groupname = items[groupPosition].name;

        if(convertView == null){
            view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.view_drawer_child, parent, false);

            Switch_new_sscrap = (SwitchButton) view.findViewById(R.id.switch_new_scrap);
            Switch_my_scrap_favorite = (SwitchButton) view.findViewById(R.id.switch_like_my_scrap);
            Switch_my_page_follow = (SwitchButton) view.findViewById(R.id.switch_follow_me);

            Switch_new_sscrap.setThumbColorRes(R.color.switch_thumb_color);
            Switch_my_scrap_favorite.setThumbColorRes(R.color.switch_thumb_color);
            Switch_my_page_follow.setThumbColorRes(R.color.switch_thumb_color);

            Switch_new_sscrap.setBackColorRes(R.color.switch_background_color);
            Switch_my_scrap_favorite.setBackColorRes(R.color.switch_background_color);
            Switch_my_page_follow.setBackColorRes(R.color.switch_background_color);


            childView = (TextView)view.findViewById(R.id.text_child);

            //child를 포함하고 있는 group에만 switch를 VISIBLE로 설정한다//
            if (groupname.equals("알림 설정")) {
                if (childPosition == 0) {
                    Switch_new_sscrap.setVisibility(View.VISIBLE);
                    Switch_my_scrap_favorite.setVisibility(View.GONE);
                    Switch_my_page_follow.setVisibility(View.GONE);

                    //해당 스위치의 설정을 공유 프래퍼런스를 가지고 설정//
                    String locked_state = PropertyManager.getInstance().get_nt_fs();

                    if (locked_state.equals("true")) {
                        Switch_new_sscrap.setChecked(true);
                    } else if (locked_state.equals("false")) {
                        Switch_new_sscrap.setChecked(false);
                    }
                } else if (childPosition == 1) {
                    Switch_new_sscrap.setVisibility(View.GONE);
                    Switch_my_scrap_favorite.setVisibility(View.VISIBLE);
                    Switch_my_page_follow.setVisibility(View.GONE);

                    String locked_state = PropertyManager.getInstance().get_nt_s();

                    if (locked_state.equals("true")) {
                        Switch_my_scrap_favorite.setChecked(true);
                    } else if (locked_state.equals("false")) {
                        Switch_my_scrap_favorite.setChecked(false);
                    }
                } else if (childPosition == 2) {
                    Switch_new_sscrap.setVisibility(View.GONE);
                    Switch_my_scrap_favorite.setVisibility(View.GONE);
                    Switch_my_page_follow.setVisibility(View.VISIBLE);

                    String locked_state = PropertyManager.getInstance().get_nt_f();

                    if (locked_state.equals("true")) {
                        Switch_my_page_follow.setChecked(true);
                    } else if (locked_state.equals("false")) {
                        Switch_my_page_follow.setChecked(false);
                    }
                }

                //switch의 상태가 바뀔 때 이벤트//
                Switch_new_sscrap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                        if(checked){
                            Toast.makeText(context, "새 스크랩 알림 on", Toast.LENGTH_SHORT).show();

                            //공유 프래퍼런스의 값도 변경해준다.//
                            PropertyManager.getInstance().set_nt_fs("true");

                            //해당 네트워크 작업과 데이터베이스 저장작업을 해준다.//
                            set_new_scrap_alarm_on();

                        }else{
                            Toast.makeText(context, "새 스크랩 알림 off", Toast.LENGTH_SHORT).show();

                            //공유 프래퍼런스의 값도 변경해준다.//
                            PropertyManager.getInstance().set_nt_fs("false");

                            set_new_scrap_alarm_off();
                        }
                    }
                });

                Switch_my_scrap_favorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                        if (checked) {
                            Toast.makeText(context, "내 스크랩 알림 on", Toast.LENGTH_SHORT).show();

                            //공유 프래퍼런스의 값도 변경해준다.//
                            PropertyManager.getInstance().set_nt_s("true");

                            set_my_scrap_alarm_on();
                        } else {
                            Toast.makeText(context, "내 스크랩 알림 off", Toast.LENGTH_SHORT).show();

                            //공유 프래퍼런스의 값도 변경해준다.//
                            PropertyManager.getInstance().set_nt_s("false");

                            set_my_scrap_alarm_off();
                        }
                    }
                });

                Switch_my_page_follow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                        if (checked) {
                            Toast.makeText(context, "마이페이지 알림 on", Toast.LENGTH_SHORT).show();

                            //공유 프래퍼런스의 값도 변경해준다.//
                            PropertyManager.getInstance().set_nt_f("true");

                            set_my_page_follow_alarm_on();
                        } else {
                            Toast.makeText(context, "마이페이지 알림 off", Toast.LENGTH_SHORT).show();

                            //공유 프래퍼런스의 값도 변경해준다.//
                            PropertyManager.getInstance().set_nt_f("false");

                            set_my_page_follow_alarm_off();
                        }
                    }
                });
            } else {
                Switch_new_sscrap.setVisibility(View.GONE);
                Switch_my_scrap_favorite.setVisibility(View.GONE);
                Switch_my_page_follow.setVisibility(View.GONE);
            }

        } else {
            view = convertView;

            Switch_new_sscrap = (SwitchButton) view.findViewById(R.id.switch_new_scrap);
            Switch_my_scrap_favorite = (SwitchButton) view.findViewById(R.id.switch_like_my_scrap);
            Switch_my_page_follow = (SwitchButton) view.findViewById(R.id.switch_follow_me);

            Switch_new_sscrap.setThumbColorRes(R.color.switch_thumb_color);
            Switch_my_scrap_favorite.setThumbColorRes(R.color.switch_thumb_color);
            Switch_my_page_follow.setThumbColorRes(R.color.switch_thumb_color);

            Switch_new_sscrap.setBackColorRes(R.color.switch_background_color);
            Switch_my_scrap_favorite.setBackColorRes(R.color.switch_background_color);
            Switch_my_page_follow.setBackColorRes(R.color.switch_background_color);

            childView = (TextView)view.findViewById(R.id.text_child);

            if (groupname.equals("알림 설정")) {
                if (childPosition == 0) {
                    Switch_new_sscrap.setVisibility(View.VISIBLE);
                    Switch_my_scrap_favorite.setVisibility(View.GONE);
                    Switch_my_page_follow.setVisibility(View.GONE);

                    //해당 스위치의 설정을 공유 프래퍼런스를 가지고 설정//
                    String locked_state = PropertyManager.getInstance().get_nt_fs();

                    if (locked_state.equals("true")) {
                        Switch_new_sscrap.setChecked(true);
                    } else if (locked_state.equals("false")) {
                        Switch_new_sscrap.setChecked(false);
                    }
                } else if (childPosition == 1) {
                    Switch_new_sscrap.setVisibility(View.GONE);
                    Switch_my_scrap_favorite.setVisibility(View.VISIBLE);
                    Switch_my_page_follow.setVisibility(View.GONE);

                    String locked_state = PropertyManager.getInstance().get_nt_s();

                    if (locked_state.equals("true")) {
                        Switch_my_scrap_favorite.setChecked(true);
                    } else if (locked_state.equals("false")) {
                        Switch_my_scrap_favorite.setChecked(false);
                    }

                } else if (childPosition == 2) {
                    Switch_new_sscrap.setVisibility(View.GONE);
                    Switch_my_scrap_favorite.setVisibility(View.GONE);
                    Switch_my_page_follow.setVisibility(View.VISIBLE);

                    String locked_state = PropertyManager.getInstance().get_nt_f();

                    if (locked_state.equals("true")) {
                        Switch_my_page_follow.setChecked(true);
                    } else if (locked_state.equals("false")) {
                        Switch_my_page_follow.setChecked(false);
                    }
                }
            }

            else
            {
                Switch_new_sscrap.setVisibility(View.GONE);
                Switch_my_scrap_favorite.setVisibility(View.GONE);
                Switch_my_page_follow.setVisibility(View.GONE);
            }
        }

        childView.setText(items[groupPosition].childViewList.get(childPosition).name);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public void set_new_scrap_alarm_on() {
        /** 네트워크 설정을 해준다. **/
        /** OkHttp 자원 설정 **/
        networkManager = NetworkManager.getInstance();

        /** Client 설정 **/
        OkHttpClient client = networkManager.getClient();

        /** GET방식의 프로토콜 Scheme 정의 **/
        //우선적으로 Url을 만든다.//
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("http");
        builder.host(context.getResources().getString(R.string.real_server_domain));
        builder.port(8080);
        builder.addPathSegment("users");
        builder.addPathSegment("me"); //uid값은 자기 자신이기에 me가 된다.//

        builder.addQueryParameter("action", "fs"); //새 스크랩에 대한 알람 설정//

        /** Delete이기에 RequestBody를 만든다 **/
        RequestBody body = new FormBody.Builder()
                .add("nt_state", "1") //true이기에 1//
                .build(); //데이터가 없으니 그냥 build로 설정.//

        //최종적으로 Request 구성//
        Request request = new Request.Builder()
                .url(builder.build())
                .put(body)
                .tag(this)
                .build();

        /** 비동기 방식(enqueue)으로 Callback 구현 **/
        client.newCall(request).enqueue(requestnewscrapalarmoncallback);
    }

    public void set_new_scrap_alarm_off() {
        /** 네트워크 설정을 해준다. **/
        /** OkHttp 자원 설정 **/
        networkManager = NetworkManager.getInstance();

        /** Client 설정 **/
        OkHttpClient client = networkManager.getClient();

        /** GET방식의 프로토콜 Scheme 정의 **/
        //우선적으로 Url을 만든다.//
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("http");
        builder.host(context.getResources().getString(R.string.real_server_domain));
        builder.port(8080);
        builder.addPathSegment("users");
        builder.addPathSegment("me"); //uid값은 자기 자신이기에 me가 된다.//

        builder.addQueryParameter("action", "fs"); //새 스크랩에 대한 알람 설정//

        /** Delete이기에 RequestBody를 만든다 **/
        RequestBody body = new FormBody.Builder()
                .add("nt_state", "0") //false이기에 0//
                .build(); //데이터가 없으니 그냥 build로 설정.//

        //최종적으로 Request 구성//
        Request request = new Request.Builder()
                .url(builder.build())
                .put(body)
                .tag(this)
                .build();

        /** 비동기 방식(enqueue)으로 Callback 구현 **/
        client.newCall(request).enqueue(requestnewscrapalarmoffcallback);
    }

    public void set_my_scrap_alarm_on() {
        /** 네트워크 설정을 해준다. **/
        /** OkHttp 자원 설정 **/
        networkManager = NetworkManager.getInstance();

        /** Client 설정 **/
        OkHttpClient client = networkManager.getClient();

        /** GET방식의 프로토콜 Scheme 정의 **/
        //우선적으로 Url을 만든다.//
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("http");
        builder.host(context.getResources().getString(R.string.real_server_domain));
        builder.port(8080);
        builder.addPathSegment("users");
        builder.addPathSegment("me"); //uid값은 자기 자신이기에 me가 된다.//

        builder.addQueryParameter("action", "s"); //새 스크랩에 대한 알람 설정//

        /** Delete이기에 RequestBody를 만든다 **/
        RequestBody body = new FormBody.Builder()
                .add("nt_state", "1") //true이기에 1//
                .build(); //데이터가 없으니 그냥 build로 설정.//

        //최종적으로 Request 구성//
        Request request = new Request.Builder()
                .url(builder.build())
                .put(body)
                .tag(this)
                .build();

        /** 비동기 방식(enqueue)으로 Callback 구현 **/
        client.newCall(request).enqueue(requestmyscrapalarmoncallback);
    }

    public void set_my_scrap_alarm_off() {
        /** 네트워크 설정을 해준다. **/
        /** OkHttp 자원 설정 **/
        networkManager = NetworkManager.getInstance();

        /** Client 설정 **/
        OkHttpClient client = networkManager.getClient();

        /** GET방식의 프로토콜 Scheme 정의 **/
        //우선적으로 Url을 만든다.//
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("http");
        builder.host(context.getResources().getString(R.string.real_server_domain));
        builder.port(8080);
        builder.addPathSegment("users");
        builder.addPathSegment("me"); //uid값은 자기 자신이기에 me가 된다.//

        builder.addQueryParameter("action", "s"); //새 스크랩에 대한 알람 설정//

        /** Delete이기에 RequestBody를 만든다 **/
        RequestBody body = new FormBody.Builder()
                .add("nt_state", "0") //false이기에 0//
                .build(); //데이터가 없으니 그냥 build로 설정.//

        //최종적으로 Request 구성//
        Request request = new Request.Builder()
                .url(builder.build())
                .put(body)
                .tag(this)
                .build();

        /** 비동기 방식(enqueue)으로 Callback 구현 **/
        client.newCall(request).enqueue(requestmyscrapalarmoffcallback);
    }

    public void set_my_page_follow_alarm_on() {
        /** 네트워크 설정을 해준다. **/
        /** OkHttp 자원 설정 **/
        networkManager = NetworkManager.getInstance();

        /** Client 설정 **/
        OkHttpClient client = networkManager.getClient();

        /** GET방식의 프로토콜 Scheme 정의 **/
        //우선적으로 Url을 만든다.//
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("http");
        builder.host(context.getResources().getString(R.string.real_server_domain));
        builder.port(8080);
        builder.addPathSegment("users");
        builder.addPathSegment("me"); //uid값은 자기 자신이기에 me가 된다.//

        builder.addQueryParameter("action", "f"); //새 스크랩에 대한 알람 설정//

        /** Delete이기에 RequestBody를 만든다 **/
        RequestBody body = new FormBody.Builder()
                .add("nt_state", "1") //true이기에 1//
                .build(); //데이터가 없으니 그냥 build로 설정.//

        //최종적으로 Request 구성//
        Request request = new Request.Builder()
                .url(builder.build())
                .put(body)
                .tag(this)
                .build();

        /** 비동기 방식(enqueue)으로 Callback 구현 **/
        client.newCall(request).enqueue(requestmypagefollowoncallback);
    }

    public void set_my_page_follow_alarm_off() {
        /** 네트워크 설정을 해준다. **/
        /** OkHttp 자원 설정 **/
        networkManager = NetworkManager.getInstance();

        /** Client 설정 **/
        OkHttpClient client = networkManager.getClient();

        /** GET방식의 프로토콜 Scheme 정의 **/
        //우선적으로 Url을 만든다.//
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("http");
        builder.host(context.getResources().getString(R.string.real_server_domain));
        builder.port(8080);
        builder.addPathSegment("users");
        builder.addPathSegment("me"); //uid값은 자기 자신이기에 me가 된다.//

        builder.addQueryParameter("action", "f"); //새 스크랩에 대한 알람 설정//

        /** Delete이기에 RequestBody를 만든다 **/
        RequestBody body = new FormBody.Builder()
                .add("nt_state", "0") //false이기에 0//
                .build(); //데이터가 없으니 그냥 build로 설정.//

        //최종적으로 Request 구성//
        Request request = new Request.Builder()
                .url(builder.build())
                .put(body)
                .tag(this)
                .build();

        /** 비동기 방식(enqueue)으로 Callback 구현 **/
        client.newCall(request).enqueue(requestmypagefollowoffcallback);
    }
}
