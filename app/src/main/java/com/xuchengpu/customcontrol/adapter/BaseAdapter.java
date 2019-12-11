package com.xuchengpu.customcontrol.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by 许成谱 on 2018/4/16 13:37.
 * qq:1550540124
 * 热爱生活每一天！
 * recycleview的万能适配器
 *
 * @param <T> 传入的数据类型
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter.ViewHolder> {

    private  MuitiTypeSupport<T> muitiTypeSupport;
    public Context mContext;
    private List<T> datas;//数据类型用泛型代替，由使用者自己配置
    private int layoutId;
    private LayoutInflater inflater;


    public BaseAdapter(Context context, List<T> datas, int layoutId) {
        this.datas = datas;
        this.layoutId = layoutId;
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }
    public BaseAdapter(Context context, List<T> datas,MuitiTypeSupport<T> muitiTypeSupport) {
        this(context,datas,-1);
        this.muitiTypeSupport=muitiTypeSupport;
    }

    @Override
    public int getItemViewType(int position) {
        if(muitiTypeSupport!=null) {
            return  muitiTypeSupport.getLayoutId(datas.get(position),position);
        }
        return super.getItemViewType(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(muitiTypeSupport!=null) {
            layoutId=viewType;
        }
        return new ViewHolder(inflater.inflate(layoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        convert(holder, datas.get(position),position);//把数据暴露出去，由实现者具体去实现
        //设置点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onClick(position);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (longClickListener != null) {
                    return longClickListener.onClick(position);
                }
                return false;
            }
        });

    }

    public abstract void convert(ViewHolder holder, T itemData,int position);

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> sparseArray;//省去装箱拆箱，效率更高

        public ViewHolder(View itemView) {
            super(itemView);
            sparseArray = new SparseArray();
        }

        /**
         * 通过id获取view
         *
         * @param viewId
         * @param <V>
         * @return
         */
        public <V extends View> V getView(int viewId) {
            View view = sparseArray.get(viewId);
            if (view == null) {
                view = itemView.findViewById(viewId);
                sparseArray.put(viewId, view);
            }
            return (V) view;
        }

        /**
         * 设置文本
         *
         * @param viewId
         * @param text
         * @return this, 方便使用链式调用
         */
        public ViewHolder setText(int viewId, CharSequence text) {
            android.widget.TextView textView = getView(viewId);
            textView.setText(text);
            return this;
        }

        /**
         * 使用系统的ImageView直接设置图片资源
         *
         * @param viewId
         * @param imgId
         * @return
         */
        public ViewHolder setImageResource(int viewId, int imgId) {
            ImageView imageView = getView(viewId);
            imageView.setImageResource(imgId);
            return this;
        }

        /**
         * 使用第三方工具加载图片
         *
         * @param viewId
         * @param imagePath
         * @param imageLoader
         * @param <S>
         * @return
         */
        public <S> ViewHolder setImageByThirdUtils(int viewId, S imagePath, ImageLoader imageLoader) {
            if (imageLoader == null) {
                throw new NullPointerException("imageLoader不能为空");
            }
            imageLoader.setImage((ImageView) getView(viewId), imagePath);
            return this;
        }

        static abstract class ImageLoader {
            public abstract <T> void setImage(ImageView imageView, T imagePath);
        }
    }

    public interface ItemClickListener {
        void onClick(int positon);
    }

    private ItemClickListener clickListener;

    public void setOnItemClickListener(ItemClickListener listener) {
        this.clickListener = listener;
    }


    public interface ItemLongClickListener {
        boolean onClick(int positon);
    }

    private ItemLongClickListener longClickListener;

    public void setOnItemLongClickListener(ItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    /**
     * 多布局加载,这个本质上还是一种接口回调的思想，只不过是改为直接从构造器中去设置了罢了
     * @param <T>
     */
    public interface MuitiTypeSupport<T>{
        /**
         * 根据数据内容或者位置position来拿布局
         * @param itemData
         * @param positon
         * @return
         */
        int getLayoutId(T itemData ,int positon);
    }
}
