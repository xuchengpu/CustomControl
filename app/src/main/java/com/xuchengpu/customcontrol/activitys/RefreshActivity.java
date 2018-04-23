package com.xuchengpu.customcontrol.activitys;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xuchengpu.customcontrol.R;
import com.xuchengpu.customcontrol.adapter.BaseAdapter;
import com.xuchengpu.customcontrol.adapter.FruitAdapter;
import com.xuchengpu.customcontrol.bean.Fruit;
import com.xuchengpu.customcontrol.utils.LoadingHelper;
import com.xuchengpu.customcontrol.utils.RefreshHelper;
import com.xuchengpu.customcontrol.wiget.recycleview.LoadingRecycleView;
import com.xuchengpu.customcontrol.wiget.recycleview.RefreshRecycleView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RefreshActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private FloatingActionButton fab;
    private LoadingRecycleView recyclerView;
    private Fruit[] fruits = {new Fruit("Apple", R.drawable.apple), new Fruit("Banana", R.drawable.banana),
            new Fruit("Orange", R.drawable.orange), new Fruit("Watermelon", R.drawable.watermelon),
            new Fruit("Pear", R.drawable.pear), new Fruit("Grape", R.drawable.grape),
            new Fruit("Pineapple", R.drawable.pineapple), new Fruit("Strawberry", R.drawable.strawberry),
            new Fruit("Cherry", R.drawable.cherry), new Fruit("Mango", R.drawable.mango)};
    private List<Fruit> fruitList = new ArrayList<>();
    private List<Fruit> fruitListMore = new ArrayList<>();

    private FruitAdapter fruitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        recyclerView = (LoadingRecycleView) findViewById(R.id.recycleview);
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
                                Toast.makeText(RefreshActivity.this, "撤销成功", Toast.LENGTH_SHORT).show();
                            }
                        }).show();

            }
        });
        //设置中间recycleview的内容
        initFruits();

//        GridLayoutManager manager = new GridLayoutManager(this, 2);
//        recyclerView.setLayoutManager(manager);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        final int spanCount = manager.getSpanCount();
//        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                if (position == 0) {
//                    return spanCount;
//                }
//                return 1;
//            }
//        });

        fruitAdapter = new FruitAdapter(this, fruitList, R.layout.item_fruit);
        recyclerView.setAdapter(fruitAdapter);

        //自己设置动画效果，也可以不用设置
        recyclerView.setRefreshHelper(new RefreshHelper() {

            private View view;
            private android.widget.TextView textView;

            @Override
            public View getRefreshView(Context context, ViewGroup parent) {
                view = LayoutInflater.from(context).inflate(R.layout.layout_refresh2, parent, false);
                textView = (android.widget.TextView) view.findViewById(R.id.tv_content);
                return view;
            }

            @Override
            public void onPull(int currentHeight, int viewHeight) {
                textView.setText("currentHeight==" + currentHeight);

            }

            @Override
            public void onRefreshing() {
                textView.setText("正在努力加载中……");
            }

            @Override
            public void stopRefreshing() {
                textView.setText("刷新完成");
            }
        });
        recyclerView.setLoadingHelper(new LoadingHelper() {
            private View view;
            private android.widget.TextView textView;

            @Override
            public View getLoadingView(Context context, ViewGroup parent) {
                view = LayoutInflater.from(context).inflate(R.layout.layout_refresh2, parent, false);
                textView = (android.widget.TextView) view.findViewById(R.id.tv_content);
                return view;
            }

            @Override
            public void onPull(int currentHeight, int viewHeight) {
                textView.setText("上拉加载更多");
            }

            @Override
            public void onLoading() {
                textView.setText("正在努力加载中……");
            }

            @Override
            public void stopLoading() {
                Toast.makeText(RefreshActivity.this, "加载更多完成", Toast.LENGTH_SHORT).show();
            }
        });

        fruitAdapter.setOnItemClickListener(new BaseAdapter.ItemClickListener() {
            @Override
            public void onClick(int positon) {
                Toast.makeText(RefreshActivity.this, "点击了" + positon, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setOnRefreshingListener(new RefreshRecycleView.RefreshListener() {
            @Override
            public void onRefreshing() {
                updateData();
            }
        });
        recyclerView.setOnLoadingListener(new LoadingRecycleView.LoadingListener() {

            @Override
            public void onLoading() {
                loadMoreData();
            }
        });


    }

    /**
     * 刷新数据
     */
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
                        fruitAdapter.notifyDataSetChanged();
                        recyclerView.StopRefresh();
                    }
                });
            }
        }.start();
    }

    private void loadMoreData() {
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
                        fruitList.addAll(fruitListMore);
                        recyclerView.StopLoading();
                        fruitAdapter.notifyDataSetChanged();
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
            fruitListMore.add(fruits[index]);
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
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.backup:
                Toast.makeText(RefreshActivity.this, "点击了返回", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(RefreshActivity.this, "点击了删除", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                Toast.makeText(RefreshActivity.this, "点击了设置", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
