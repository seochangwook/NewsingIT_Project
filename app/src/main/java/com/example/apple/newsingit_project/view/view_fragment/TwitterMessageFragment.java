package com.example.apple.newsingit_project.view.view_fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apple.newsingit_project.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TwitterMessageFragment extends Fragment {


    public TwitterMessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_twitter_message, container, false);

        return view;
    }

}
