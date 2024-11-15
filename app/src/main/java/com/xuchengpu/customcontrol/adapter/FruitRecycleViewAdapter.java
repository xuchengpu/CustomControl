package com.xuchengpu.customcontrol.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xuchengpu.customcontrol.R;
import com.xuchengpu.customcontrol.bean.Fruit;

import java.util.List;

/**
 * Created by 许成谱 on 2018/1/30 14:09.
 * qq:1550540124
 * 热爱生活每一天！
 */

public class FruitRecycleViewAdapter extends RecyclerView.Adapter<FruitRecycleViewAdapter.ViewHolder> {
    public static final String FRUIT_NAME = "fruit_name";

    public static final String FRUIT_IMAGE_ID = "fruit_image_id";

    private final List<Fruit> fruitList;
    private Context mContext;

    public FruitRecycleViewAdapter(List<Fruit> fruitList) {
        this.fruitList = fruitList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_fruit, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);


    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Fruit fruit = fruitList.get(position);
//        holder.imageView.setImageResource(fruit.getImgId());
        Glide.with(mContext).load(fruitList.get(position).getImgId()).into(holder.imageView);
        holder.desc.setText(fruit.getName());
    }

    @Override
    public int getItemCount() {
        return fruitList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView desc;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_fruit);
            desc = (TextView) itemView.findViewById(R.id.tv_desc);
        }
    }
}
