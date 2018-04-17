package com.xuchengpu.customcontrol.wiget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.xuchengpu.customcontrol.adapter.WrapRecycleViewAdapter;

/**
 * Created by 许成谱 on 2018/4/17 14:34.
 * qq:1550540124
 * 热爱生活每一天！
 */

public class MyRecycleview extends RecyclerView {
    private WrapRecycleViewAdapter mAdapter;
    private AdapterDataObserver observer;

    public MyRecycleview(Context context) {
        this(context, null);
    }

    public MyRecycleview(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRecycleview(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        observer=new AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                mAdapter.notifyItemRangeChanged(mAdapter.getHeadViewCount()+positionStart,itemCount);
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                super.onItemRangeChanged(positionStart, itemCount, payload);
                mAdapter.notifyItemRangeChanged(mAdapter.getHeadViewCount()+positionStart,itemCount,payload);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                mAdapter.notifyItemRangeInserted(mAdapter.getHeadViewCount()+positionStart,itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                mAdapter.notifyItemRangeRemoved(mAdapter.getHeadViewCount()+positionStart,itemCount);
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                mAdapter.notifyItemRangeRemoved(mAdapter.getHeadViewCount()+fromPosition,toPosition);
            }
        };
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof WrapRecycleViewAdapter) {
            mAdapter = (WrapRecycleViewAdapter) adapter;
        } else {
            mAdapter = new WrapRecycleViewAdapter(adapter);
            adapter.registerAdapterDataObserver(observer);//在主题中把观察者注册进去,这样当我们在adapter中例如点击事件，我们再
            //点击事件中调用 notifyDataSetChanged();则会自动调用observer中的onchange()方法
            //移除item之所以会失效是因为adapter改变了，但mAdapter还是没变所以需要设置观察者模式
        }
        super.setAdapter(mAdapter);
    }

    /**
     * 添加头部
     *
     * @param view
     */
    public void addHeadView(View view) {
        if (mAdapter != null) {
            mAdapter.addHeadView(view);
        }

    }

    /**
     * 移除头部
     *
     * @param view
     */
    public void removeHeadView(View view) {
        if (mAdapter != null) {
            mAdapter.removeHeadView(view);
        }

    }

    /**
     * 添加底部
     *
     * @param view
     */
    public void addFootView(View view) {
        if (mAdapter != null) {
            mAdapter.addFootView(view);
        }


    }

    /**
     * 移除底部
     *
     * @param view
     */
    public void removeFootView(View view) {
        if (mAdapter != null) {
            mAdapter.removeFootView(view);
        }

    }
}
