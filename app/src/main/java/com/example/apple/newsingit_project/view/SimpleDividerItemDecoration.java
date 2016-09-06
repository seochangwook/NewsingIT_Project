package com.example.apple.newsingit_project.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Tacademy on 2016-09-06.
 */
public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {

    private Paint paint = new Paint();
    private int color;
    private int strokeWidth;

    public SimpleDividerItemDecoration(int color, int strokeWidth) {
        this.color = color;
        this.strokeWidth = strokeWidth;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int viewType = parent.getChildViewHolder(child).getItemViewType();

//            // row가 아니면 그리지 않음
//            if (viewType != Const.VIEW_TYPE_ROW) {
//                continue;
//            }
//            // 내가 마지막이 아니고, 내 다음이 row가 아니면 그리지 않음
//            else if (i < childCount - 1) {
//                View next = parent.getChildAt(i + 1);
//                if (parent.getChildViewHolder(next).getItemViewType() != Const.VIEW_TYPE_ROW) {
//                    continue;
//                }
//            }

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = child.getBottom();

            paint.setColor(color);
            paint.setStrokeWidth(strokeWidth);
            c.drawLine(left, top, right, bottom, paint);
        }
    }
}
