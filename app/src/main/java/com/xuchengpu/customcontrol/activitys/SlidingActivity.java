package com.xuchengpu.customcontrol.activitys;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xuchengpu.customcontrol.R;
import com.xuchengpu.customcontrol.adapter.BaseAdapter;
import com.xuchengpu.customcontrol.adapter.LettersAdapter;
import com.xuchengpu.customcontrol.wiget.recycleview.WrapRecycleview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SlidingActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private WrapRecycleview recyclerView;
    private List datas = new ArrayList();
    private LettersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (WrapRecycleview) findViewById(R.id.recycleview);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        //设置中间recycleview的内容
        initDatas();
//        GridLayoutManager manager = new GridLayoutManager(this, 4);
//        recyclerView.setLayoutManager(manager);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LettersAdapter(this, datas, R.layout.item_fruit2);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseAdapter.ItemClickListener() {
            @Override
            public void onClick(int positon) {
                Toast.makeText(SlidingActivity.this, "单击了" + positon, Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setOnItemLongClickListener(new BaseAdapter.ItemLongClickListener() {
            @Override
            public boolean onClick(int positon) {
                Toast.makeText(SlidingActivity.this, "长按了"+positon, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                // dragFlags  The directions in which the item can be dragged.
                //swipeFlags The directions in which the item can be swiped.
                int dragFlags = ItemTouchHelper.UP|ItemTouchHelper.DOWN;
                int swipFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                if(recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                    dragFlags = ItemTouchHelper.UP|ItemTouchHelper.DOWN|ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;

                }
                return makeMovementFlags(dragFlags, swipFlags);
            }

            /**
             * 拖动的时候不断回调的方法
             * @param recyclerView
             * @param viewHolder
             * @param target
             * @return
             */
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // 获取原来的位置
                int fromPosition = viewHolder.getAdapterPosition();
                // 得到目标的位置
                int targetPosition = target.getAdapterPosition();
                if (targetPosition > fromPosition) {
                    for (int i = fromPosition; i < targetPosition; i++) {
                        Collections.swap(datas, i, i + 1);// 改变实际的数据集
                    }
                } else {
                    for (int i = fromPosition; i > targetPosition; i--) {
                        Collections.swap(datas, i, i - 1);// 改变实际的数据集
                    }
                }
                adapter.notifyItemMoved(fromPosition, targetPosition);
                return true;
            }

            /**
             * 侧滑删除后会回调的方法
             * @param viewHolder
             * @param direction
             */
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // 获取当前删除的位置
                int position = viewHolder.getAdapterPosition();
//                adapter.notifyDataSetChanged();
                adapter.notifyItemRemoved(position);
                datas.remove(position);
            }

            /**
             * 拖动选择的时候状态回调
             * @param viewHolder
             * @param actionState
             */
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
                if(actionState!=ItemTouchHelper.ACTION_STATE_IDLE) {//否则空指针异常
                    viewHolder.itemView.setBackgroundColor(Color.parseColor("#FF4081"));
                }
            }

            /**
             * 回到正常状态的时候回调
             * @param recyclerView
             * @param viewHolder
             */
            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#3390f118"));
//                ViewCompat.setTranslationX(viewHolder.itemView,0);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void initDatas() {
        char l = 'A';
        for (int i = 0; i < 26; i++) {
            datas.add(String.valueOf(l));
            l++;
        }
    }



    /**
     * 创建menu的布局
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }


    /**
     * 点击menu的回调
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                break;
            case R.id.backup:
                break;
            case R.id.delete:
                break;
            case R.id.setting:
                break;
        }
        return true;
    }
}
