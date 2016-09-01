package com.example.apple.newsingit_project.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.apple.newsingit_project.R;
import com.example.apple.newsingit_project.data.view_data.FolderData;
import com.example.apple.newsingit_project.view.view_list.FolderViewHolder;

/**
 * Created by apple on 2016. 8. 24..
 */
public class FolderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //폴더 데이터 클래스 선언.//
    FolderData folderData;
    Context context;

    //생성자로 데이터 클래스 할당과 프래그먼트 자원 초기화.//
    public FolderListAdapter(Context context) {
        this.context = context;

        folderData = new FolderData();
    }

    //어댑터랑 연동되는 객체를 설정.//
    public void set_FolderDate(FolderData folderDate) {
        if (this.folderData != folderDate) {
            this.folderData = folderDate;

            notifyDataSetChanged();
        }
    }

    //리사이클뷰에 들어갈 뷰 초기화.//
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_folder_list, parent, false);

        //뷰 홀더 객체 설정.//
        FolderViewHolder holder = new FolderViewHolder(view);

        return holder;
    }

    //리사이클뷰에 들어갈 뷰의 아이템값 초기화.//
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position < folderData.folder_list.size()) {
            final FolderViewHolder folderViewHolder = (FolderViewHolder) holder;

            folderViewHolder.set_Folder(folderData.folder_list.get(position), context);

            final boolean folder_private = folderData.folder_list.get(position).get_folder_private();

            if (folder_private == true) { //잠금 해제//
                folderViewHolder.folder_private_button.setVisibility(View.VISIBLE);
            } else { //잠금 설정//
                folderViewHolder.folder_private_button.setVisibility(View.GONE);
            }

            folderViewHolder.folder_private_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (folder_private == true) { //잠금 해제//
                        Toast.makeText(context, "잠금모드 해제", Toast.LENGTH_SHORT).show();

                        folderData.folder_list.get(position).setFolder_private(false);

                        //인증해제 작업을 해준다.//
                    } else if (folder_private == false) { //잠금 설정//
                        Toast.makeText(context, "잠금모드 설정", Toast.LENGTH_SHORT).show();

                        folderData.folder_list.get(position).setFolder_private(true);

                        //인증작업을 해준다.//
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }

    //리사이클뷰에 나타낼 뷰의 개수를 정의.//
    @Override
    public int getItemCount() {
        return folderData.folder_list.size(); //해당 폴더 목록만큼 반환.//
    }
}
