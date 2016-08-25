package com.example.apple.newsingit_project.view.view_fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.UserInfoActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchUserFragment extends Fragment {
    Button click_button;

    public SearchUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_user_layout, container, false);

        click_button = (Button) view.findViewById(R.id.user_mypage_click);

        click_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserInfoActivity.class);

                startActivity(intent);
            }
        });

        return view;
    }

}
