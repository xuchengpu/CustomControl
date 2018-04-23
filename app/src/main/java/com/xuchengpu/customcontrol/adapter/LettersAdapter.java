package com.xuchengpu.customcontrol.adapter;

import android.content.Context;

import com.xuchengpu.customcontrol.R;

import java.util.List;

/**
 * Created by 许成谱 on 2018/4/23 13:44.
 * qq:1550540124
 * 热爱生活每一天！
 */

public class LettersAdapter extends BaseAdapter<String>{
    public LettersAdapter(Context context, List<String> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, String itemData, int position) {
        holder.setText(R.id.tv_desc,itemData);
    }
}
