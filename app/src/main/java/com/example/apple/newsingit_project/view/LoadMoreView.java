package com.example.apple.newsingit_project.view;

/**
 * Created by apple on 2016. 8. 24..
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.apple.newsingit_project.R;

import cn.iwgang.familiarrecyclerview.IFamiliarLoadMore;

/**
 * Created by apple on 2016. 7. 25..
 */
public class LoadMoreView extends FrameLayout implements IFamiliarLoadMore {
    private ProgressBar mPbLoad;
    private TextView mTvLoadText;

    private boolean isLoading = false; // 是否加载中

    private int flag;

    /**
     * flag
     * 1 - Main News List
     * 2 - Scrap content list
     * 3 - Folder List
     * 4 - User search List
     * 5 - News search List
     * 6 - Tag search List
     *
     * @param context
     * @param flag
     */

    public LoadMoreView(Context context, int flag) {
        this(context, null);

        this.flag = flag;
    }

    public LoadMoreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.view_load_more, this);
        mPbLoad = (ProgressBar) findViewById(R.id.pb_load);
        mTvLoadText = (TextView) findViewById(R.id.tv_loadText);
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void showNormal() {
        isLoading = false;
        mPbLoad.setVisibility(GONE);

        if (flag == 1) {
            mTvLoadText.setText(getResources().getString(R.string.load_more_normal_main));
        } else if (flag == 2) {
            mTvLoadText.setText(getResources().getString(R.string.load_more_normal_scrap));
        } else if (flag == 3) {
            mTvLoadText.setText(getResources().getString(R.string.load_more_normal_folder));
        } else if (flag == 4) {
            mTvLoadText.setText(getResources().getString(R.string.load_more_normal_main));
        } else if (flag == 5) {
            mTvLoadText.setText(getResources().getString(R.string.load_more_normal_main));
        } else if (flag == 6) {
            mTvLoadText.setText(getResources().getString(R.string.load_more_normal_main));
        }
    }

    @Override
    public void showLoading() {
        isLoading = true;
        mPbLoad.setVisibility(VISIBLE);

        if (flag == 1) {
            mTvLoadText.setText(getResources().getString(R.string.load_more_loading_main));
        } else if (flag == 2) {
            mTvLoadText.setText(getResources().getString(R.string.load_more_loading_scrap));
        } else if (flag == 3) {
            mTvLoadText.setText(getResources().getString(R.string.load_more_loading_folder));
        } else if (flag == 4) {
            mTvLoadText.setText(getResources().getString(R.string.load_more_loading_main));
        } else if (flag == 5) {
            mTvLoadText.setText(getResources().getString(R.string.load_more_loading_main));
        } else if (flag == 6) {
            mTvLoadText.setText(getResources().getString(R.string.load_more_loading_main));
        }
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

}
