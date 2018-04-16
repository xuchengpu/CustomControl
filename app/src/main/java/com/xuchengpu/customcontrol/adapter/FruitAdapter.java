package com.xuchengpu.customcontrol.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xuchengpu.customcontrol.R;
import com.xuchengpu.customcontrol.bean.Fruit;

import java.util.List;

/**
 * Created by 许成谱 on 2018/4/16 13:54.
 * qq:1550540124
 * 热爱生活每一天！
 */

public class FruitAdapter extends BaseAdapter<Fruit> {

//    public FruitAdapter(Context context, List<Fruit> datas, int layoutId) {
//        super(context, datas, layoutId);
//    }

    public FruitAdapter(Context context, List<Fruit> datas,int layoutId) {
        super(context, datas, new MuitiTypeSupport<Fruit>() {
            @Override
            public int getLayoutId(Fruit itemData, int positon) {
                //在此处根据position 或者数据来指定layout,相当于分类型的recycleview
                if(positon%2==0) {
                    return R.layout.item_fruit;
                }else{
                    return R.layout.item_fruit2;
                }
            }
        });
    }

    @Override
    public void convert(ViewHolder holder, Fruit itemData,int positon) {

        holder.setText(R.id.tv_desc, itemData.getName());
        holder.setImageByThirdUtils(R.id.iv_fruit, itemData.getImgId(), new MyLoader());
    }
    class MyLoader extends ViewHolder.ImageLoader{

        @Override
        public <T> void setImage(ImageView imageView, T imagePath) {
            //这里使用自己选定的第三方工具
            Glide.with(mContext).load( imagePath).into(imageView);
        }
    }
}
