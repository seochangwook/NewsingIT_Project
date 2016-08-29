package com.example.apple.newsingit_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import io.fabric.sdk.android.Fabric;

public class TwitterLinkActivity extends Activity {
    //공개키와 비밀키//
    private static final String TWITTER_KEY = "WKUT4AupkcyiAUHVVAM5xrIvE";
    private static final String TWITTER_SECRET = "yR9fxY13cGOJyG0vPMAu6a8HdDNOcU9FPczPncLm6OwyxdsPkM";

    //트위터 검색 변수.//
    public String search_value = null;

    SwipeRefreshLayout swipeLayout;
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);

        if (!Fabric.isInitialized()) {
            Fabric.with(this, new Twitter(authConfig));
        }

        setContentView(R.layout.activity_twitter_link);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        listview = (ListView) findViewById(R.id.testlist);

        Intent intent = getIntent();

        search_value = intent.getStringExtra("KEY_KEYWORD");

        final SearchTimeline timeline = new SearchTimeline.Builder()
                .query(search_value)
                .languageCode("ko")
                .build();

        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(timeline)
                .build();

        listview.setAdapter(adapter);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(true);
                adapter.refresh(new Callback<TimelineResult<Tweet>>() {
                    @Override
                    public void success(Result<TimelineResult<Tweet>> result) {
                        swipeLayout.setRefreshing(false);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        // Toast or some other action
                    }
                });
            }
        });
    }
}
