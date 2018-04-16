package com.xuchengpu.customcontrol.activitys;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.xuchengpu.customcontrol.R;
import com.xuchengpu.customcontrol.adapter.BaseAdapter;
import com.xuchengpu.customcontrol.adapter.FruitAdapter;
import com.xuchengpu.customcontrol.bean.Fruit;
import com.xuchengpu.customcontrol.utils.RecycleViewDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BehaviorActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refresh;
    private Fruit[] fruits = {new Fruit("Apple", R.drawable.apple), new Fruit("Banana", R.drawable.banana),
            new Fruit("Orange", R.drawable.orange), new Fruit("Watermelon", R.drawable.watermelon),
            new Fruit("Pear", R.drawable.pear), new Fruit("Grape", R.drawable.grape),
            new Fruit("Pineapple", R.drawable.pineapple), new Fruit("Strawberry", R.drawable.strawberry),
            new Fruit("Cherry", R.drawable.cherry), new Fruit("Mango", R.drawable.mango)};
    private List<Fruit> fruitList = new ArrayList<>();
    //    private FruitRecycleViewAdapter adapter;
    private FruitAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_behavior);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
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
//        adapter = new FruitRecycleViewAdapter(fruitList);
        adapter = new FruitAdapter(this, fruitList, R.layout.item_fruit);
        recyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecycleViewDecoration(this, RecycleViewDecoration.Style.GRIDLAYOUT_DECORATION, R.drawable.item_decoration));
        adapter.setOnItemClickListener(new BaseAdapter.ItemClickListener() {
            @Override
            public void onClick(int positon) {
                Toast.makeText(BehaviorActivity.this, "单击了" + positon, Toast.LENGTH_SHORT).show();
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
        for (int i = 0; i < 20; i++) {
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
                break;
            case R.id.backup:
                recyclerView.addItemDecoration(new RecycleViewDecoration(this, RecycleViewDecoration.Style.GRIDLAYOUT_DECORATION, R.drawable.item_decoration));
                recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                break;
            case R.id.delete:
                recyclerView.addItemDecoration(new RecycleViewDecoration(this, RecycleViewDecoration.Style.LINEARLAYOUT_DECORATION, R.drawable.item_decoration));
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                break;
            case R.id.setting:
                Toast.makeText(BehaviorActivity.this, "点击了设置", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
