package com.xuchengpu.customcontrol.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 许成谱 on 2018/4/17 10:22.
 * qq:1550540124
 * 热爱生活每一天！
 * for:仿照listview添加头部和底部的适配器
 */

public class WrapRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerView.Adapter mAdapter;//用来装原来的内容数据
    private SparseArray<View> headViews;//用来装头部view
    private SparseArray<View> footViews;

    private int headIndex;
    private int footIndex;
    private final int headIndexMark=2000000;//用一个较大的数来区别头部、底部、内容
    private final int footIndexMark=1000000;

    public WrapRecycleViewAdapter(RecyclerView.Adapter adapter) {
        this.mAdapter = adapter;
        headViews = new SparseArray<>();
        footViews = new SparseArray<>();
        headIndex = headIndexMark;
        footIndex = footIndexMark;
    }


    @Override
    public int getItemViewType(int position) {
        if (position < headViews.size()) {
            return headViews.keyAt(position);//返回头部的key值
        }
        //下面这段是借签listview源码
        int numHeaders = headViews.size();
        if (mAdapter != null && position >= numHeaders) {
            int adjPosition = position - numHeaders;
            int adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return mAdapter.getItemViewType(adjPosition);//debug可知可能是一个很大的数例如2314555
            }
        }
        return footViews.keyAt(position - headViews.size() - mAdapter.getItemCount());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (headViews.get(viewType) !=null ) {//通过key值拿到value
            return createHeadViewHolder(viewType);
        }else  if (footViews.get(viewType) !=null) {
            return createFootViewHolder(viewType);
        }

        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    private RecyclerView.ViewHolder createFootViewHolder(int viewType) {
        return new RecyclerView.ViewHolder(footViews.get(viewType)){};
    }

    private RecyclerView.ViewHolder createHeadViewHolder(int viewType) {

        return new RecyclerView.ViewHolder(headViews.get(viewType)){};
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //下面这段是借签listview源码
        int numHeaders = headViews.size();
        if (mAdapter != null && position >= numHeaders) {
            int adjPosition = position - numHeaders;
            int adapterCount = mAdapter.getItemCount();
            if (adjPosition < adapterCount) {
              mAdapter.onBindViewHolder(holder,adjPosition);
            }
        }

    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + headViews.size() + footViews.size();//头部+内容+尾部的view数量总和
    }

    /**
     * 添加头部
     *
     * @param view
     */
    public void addHeadView(View view) {
        if (headViews.indexOfValue(view) == -1) {
            headViews.put(headIndex++, view);
            notifyDataSetChanged();
        }

    }

    /**
     * 移除头部
     *
     * @param view
     */
    public void removeHeadView(View view) {
        int indexOfValue = headViews.indexOfValue(view);
        if (indexOfValue != -1) {
            headViews.removeAt(indexOfValue);
            notifyDataSetChanged();
        }
    }

    /**
     * 添加底部
     *
     * @param view
     */
    public void addFootView(View view) {
        if (footViews.indexOfValue(view) == -1) {
            footViews.put(footIndex++, view);
            notifyDataSetChanged();
        }

    }

    /**
     * 移除底部
     *
     * @param view
     */
    public void removeFootView(View view) {
        int indexOfValue = headViews.indexOfValue(view);
        if (indexOfValue != -1) {
            footViews.removeAt(indexOfValue);
            notifyDataSetChanged();
        }
    }
    public int getHeadViewCount(){
        return headViews.size();
    }
}
