package com.example.apple.newsingit_project;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import io.fabric.sdk.android.Fabric;

public class TwitterTestActivity extends Activity {
    private static final String TWITTER_KEY = "WKUT4AupkcyiAUHVVAM5xrIvE";
    private static final String TWITTER_SECRET = "yR9fxY13cGOJyG0vPMAu6a8HdDNOcU9FPczPncLm6OwyxdsPkM";

  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterAuthConfig authConfig =  new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);;
        if (!Fabric.isInitialized()) {
            Fabric.with(this, new Twitter(authConfig));
        }

        setContentView(R.layout.activity_twitter_test);

        SearchTimeline searchTimeline = new SearchTimeline.Builder()
                .query("#김수현")
                .languageCode("ko")
                .maxItemsPerRequest(200)
                .build();

        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(searchTimeline)
                .build();

        ListView listview =(ListView)findViewById(R.id.testlist);

        listview.setAdapter(adapter);

    }

}
