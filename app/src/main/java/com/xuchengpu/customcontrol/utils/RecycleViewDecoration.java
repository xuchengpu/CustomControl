package com.xuchengpu.customcontrol.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by 许成谱 on 2018/4/13 15:01.
 * qq:1550540124
 * 热爱生活每一天！
 */

public class RecycleViewDecoration extends RecyclerView.ItemDecoration {

    private final Style style;
    private final Drawable drawable;

    /**
     * @param context 上下文
     * @param style   是GridlayoutManager还是LinearLayoutManager
     */
    public RecycleViewDecoration(Context context, Style style, int drawableResID) {
        this.style = style;
        drawable = ContextCompat.getDrawable(context, drawableResID);
    }

    /**
     * @param outRect 类似于padding margin
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view);
        if (style == Style.LINEARLAYOUT_DECORATION) {
            if (position != 0) {//第0个位置顶部不画分隔线
                outRect.top = drawable.getIntrinsicHeight();
            }
        } else if (style == Style.GRIDLAYOUT_DECORATION) {
            int spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
            if ((position+1) % spanCount == 0) {//屏蔽掉最右边的item的竖线
                outRect.bottom = drawable.getIntrinsicHeight();
            } else {
                outRect.right = drawable.getIntrinsicHeight();
                outRect.bottom = drawable.getIntrinsicHeight();
            }

        }

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int count = parent.getChildCount();
        int spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
        if (style == Style.LINEARLAYOUT_DECORATION) {
            for (int i = 1; i < count; i++) {//跳过第0个从第一个顶部开始
                View child = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                Rect rect = new Rect();
                rect.bottom = child.getTop() - params.topMargin;
                rect.top = rect.bottom - drawable.getIntrinsicHeight();
                rect.right = child.getRight() + params.rightMargin;
                rect.left = child.getLeft() - params.leftMargin;
                drawable.setBounds(rect);
                drawable.draw(c);
            }
        } else if (style == Style.GRIDLAYOUT_DECORATION) {
            for (int i = 0; i < count; i++) {//跳过第0个从第一个顶部开始
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                Rect rect = new Rect();
                if ((i+1 )% spanCount == 0) {//屏蔽掉最右边的item的竖线
                    //画横线
                    rect.top = child.getBottom() + params.topMargin;
                    rect.bottom = rect.top + drawable.getIntrinsicHeight();
                    rect.left = child.getLeft() - params.leftMargin;
                    rect.right = child.getRight() +params.rightMargin;
                    drawable.setBounds(rect);
                    drawable.draw(c);
                } else {
                    //画竖线
                    rect.top = child.getTop() - params.topMargin;
                    rect.bottom = child.getBottom() + params.bottomMargin + drawable.getIntrinsicHeight();
                    rect.left = child.getRight() + params.rightMargin;
                    rect.right = rect.left + drawable.getIntrinsicHeight();
                    drawable.setBounds(rect);
                    drawable.draw(c);
                    //画横线
                    rect.top = child.getBottom() + params.topMargin;
                    rect.bottom = rect.top + drawable.getIntrinsicHeight();
                    rect.left = child.getLeft() - params.leftMargin;
                    rect.right = child.getRight() +params.rightMargin;
                    drawable.setBounds(rect);
                    drawable.draw(c);
                }


            }

        }

    }

    public enum Style {
        LINEARLAYOUT_DECORATION, GRIDLAYOUT_DECORATION
    }
}
