package com.example.apple.newsingit_project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;
import io.fabric.sdk.android.Fabric;

public class TwitterTestActivity extends AppCompatActivity {
    private static final String TWITTER_KEY = "WKUT4AupkcyiAUHVVAM5xrIvE";
    private static final String TWITTER_SECRET = "yR9fxY13cGOJyG0vPMAu6a8HdDNOcU9FPczPncLm6OwyxdsPkM";

    FamiliarRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterAuthConfig authConfig =  new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);;
        if (!Fabric.isInitialized()) {
            Fabric.with(this, new Twitter(authConfig));
        }

        setContentView(R.layout.activity_twitter_test);

        SearchTimeline searchTimeline = new SearchTimeline.Builder()
                .query("#iot")
                .languageCode("ko")
                .build();

        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(searchTimeline)
                .build();

        GridView listview =(GridView) findViewById(R.id.testlist);

        listview.setAdapter(adapter);

    }

}
