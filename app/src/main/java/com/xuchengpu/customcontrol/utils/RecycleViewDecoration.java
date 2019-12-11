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

    private Style style;
    private Drawable drawable;

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
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                int spanCount = ((GridLayoutManager) layoutManager).getSpanCount();

//                图中每行itemView有3个，有2条divider，每一项itemView的偏移大小就是(2*dividerWidth)/3了，同理，给定每一行itemView的个数为spanCount，也就是RecyclerView的列数，则divider的条数为(spanCount-1),
//                因此定义每一项itemView的偏移宽度为：
//                eachWidth=（spanCount-1）* dividerWidth / spanCount;
//                因为第一项的left（之后用L0表示，以此类推，L1、L2…代表第二、三…项的left
//                ）为0，所以第一项的right（之后用R0表示）为eachWidth-0，也就是说：
//                L0 = 0; R0=eachWidth;
//                第一项的right加上第二项的left等于dividerWidth，因此L1=dividerWidth-R0=dividerWidth-eachWidth，
//                R1=eachWidth-L1=eachWidth-(dividerWidth-eachWidth)=2eachWidth-dividerWidth
//                L1=dividerWidth-eachWidth; R1=2eachWidth-dividerWidth
//                因此L2=dividerWidth-R1=dividerWidth-(2eachWidth-dividerWidth)=2dividerWidth-2eachWidth=2L1，同理可得L3=3L1，L4=4L1……Ln=nL1，因此left可以用以下代码来表示：
//                left = itemPosition % spanCount * (dividerWidth - eachWidth); //itemPositison代表当前item位置，0，1，2

                int mDividerWidth = drawable.getIntrinsicHeight();
                int eachWidth = (spanCount - 1) * mDividerWidth / spanCount;

                outRect.left = position % spanCount * (mDividerWidth - eachWidth);//此公式是一个个罗列出来找规律推导出来的
                outRect.right = eachWidth - outRect.left;
                outRect.bottom = drawable.getIntrinsicHeight();
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int count = parent.getChildCount();
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        int spanCount = 1;
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
        }
        int drawableHeight = drawable.getIntrinsicHeight();
        if (style == Style.LINEARLAYOUT_DECORATION) {
            for (int i = 1; i < count; i++) {//跳过第0个从第一个顶部开始
                View child = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                Rect rect = new Rect();
                rect.bottom = child.getTop() - params.topMargin;
                rect.top = rect.bottom - drawableHeight;
                rect.right = child.getRight() + params.rightMargin;
                rect.left = child.getLeft() - params.leftMargin;
                drawable.setBounds(rect);
                drawable.draw(c);
            }
        } else if (style == Style.GRIDLAYOUT_DECORATION) {
            for (int i = 0; i < count; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                Rect rect = new Rect();
                //画竖线
                int eachWidth = (spanCount - 1) * drawableHeight / spanCount;
                rect.top = child.getTop() - params.topMargin;
                rect.left = i % spanCount * (drawableHeight - eachWidth);//此公式是一个个罗列出来找规律推导出来的
                rect.right = eachWidth - rect.left;
                rect.bottom = drawable.getIntrinsicHeight();
                drawable.setBounds(rect);
                drawable.draw(c);
                //画横线
                rect.top = child.getBottom() + params.topMargin;
                rect.bottom = rect.top + drawableHeight;
                rect.left = child.getLeft() - params.leftMargin;
                rect.right = child.getRight() + params.rightMargin;
                drawable.setBounds(rect);
                drawable.draw(c);
            }
        }

    }

    public enum Style {
        LINEARLAYOUT_DECORATION, GRIDLAYOUT_DECORATION
    }
}
