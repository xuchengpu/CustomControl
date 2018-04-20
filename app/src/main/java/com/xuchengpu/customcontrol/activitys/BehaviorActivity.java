package com.xuchengpu.customcontrol.activitys;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.xuchengpu.customcontrol.R;
import com.xuchengpu.customcontrol.adapter.BaseAdapter;
import com.xuchengpu.customcontrol.adapter.FruitAdapter;
import com.xuchengpu.customcontrol.wiget.recycleview.WrapRecycleViewAdapter;
import com.xuchengpu.customcontrol.bean.Fruit;
import com.xuchengpu.customcontrol.utils.RecycleViewDecoration;
import com.xuchengpu.customcontrol.wiget.recycleview.WrapRecycleview;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BehaviorActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private WrapRecycleview recyclerView;
    private SwipeRefreshLayout refresh;
    private Fruit[] fruits = {new Fruit("Apple", R.drawable.apple), new Fruit("Banana", R.drawable.banana),
            new Fruit("Orange", R.drawable.orange), new Fruit("Watermelon", R.drawable.watermelon),
            new Fruit("Pear", R.drawable.pear), new Fruit("Grape", R.drawable.grape),
            new Fruit("Pineapple", R.drawable.pineapple), new Fruit("Strawberry", R.drawable.strawberry),
            new Fruit("Cherry", R.drawable.cherry), new Fruit("Mango", R.drawable.mango)};
    private List<Fruit> fruitList = new ArrayList<>();
    private FruitAdapter adapter;
    private WrapRecycleViewAdapter wrapAdapter;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behavior);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        recyclerView = (WrapRecycleview) findViewById(R.id.recycleview);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MaterialDesignActivity.this, "点击了FloatingActionButton", Toast.LENGTH_SHORT).show();
                Snackbar.make(v, "点击了snackbar", Snackbar.LENGTH_SHORT)
                        .setAction("撤销", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(BehaviorActivity.this, "撤销成功", Toast.LENGTH_SHORT).show();
                            }
                        }).show();

            }
        });
        //设置中间recycleview的内容
        initFruits();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FruitAdapter(this, fruitList, R.layout.item_fruit);
//        wrapAdapter = new WrapRecycleViewAdapter(adapter);
//        recyclerView.setAdapter(wrapAdapter);
        recyclerView.setAdapter(adapter);



        view = LayoutInflater.from(this).inflate(R.layout.headview, recyclerView, false);
//        wrapAdapter.addHeadView(view);
        recyclerView.addHeadView(view);

        recyclerView.addItemDecoration(new RecycleViewDecoration(this, RecycleViewDecoration.Style.LINEARLAYOUT_DECORATION, R.drawable.item_decoration));
        adapter.setOnItemClickListener(new BaseAdapter.ItemClickListener() {
            @Override
            public void onClick(int positon) {
                Toast.makeText(BehaviorActivity.this, "单击了" + positon, Toast.LENGTH_SHORT).show();
                fruitList.remove(positon);
                adapter.notifyItemRemoved(positon);
                adapter.notifyDataSetChanged();
            }
        });
        adapter.setOnItemLongClickListener(new BaseAdapter.ItemLongClickListener() {
            @Override
            public boolean onClick(int positon) {
                Toast.makeText(BehaviorActivity.this, "长按了"+positon, Toast.LENGTH_SHORT).show();
                return true;
            }
        });


        //SwipeRefreshLayout的使用
        refresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
//        refresh.setColorSchemeResources(R.color.colorPrimary);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateData();
            }
        });
    }

    private void updateData() {

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initFruits();
                        adapter.notifyDataSetChanged();
                        refresh.setRefreshing(false);
                    }
                });
            }
        }.start();
    }

    private void initFruits() {
        fruitList.clear();
        for (int i = 0; i < 10; i++) {
            Random random = new Random();
            int index = random.nextInt(fruits.length);
            fruitList.add(fruits[index]);
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
                wrapAdapter.addHeadView(view);
                break;
            case R.id.backup:
                wrapAdapter.addFootView(view);
                break;
            case R.id.delete:
                wrapAdapter.removeHeadView(view);
                break;
            case R.id.setting:
                wrapAdapter.removeFootView(view);
                break;
        }
        return true;
    }
}
