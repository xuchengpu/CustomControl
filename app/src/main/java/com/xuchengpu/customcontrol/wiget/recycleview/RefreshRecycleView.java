package com.xuchengpu.customcontrol.wiget.recycleview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.xuchengpu.customcontrol.R;
import com.xuchengpu.customcontrol.utils.RefreshHelper;

/**
 * Created by 许成谱 on 2018/4/19 11:00.
 * qq:1550540124
 * 热爱生活每一天！
 */

public class RefreshRecycleView extends WrapRecycleview {

    private RefreshHelper helper;//刷新动画帮助类，可借助此类自定义下拉刷新动画，开闭原则
    private View refreshView;//展示刷新的头部view
    private int mRefreshViewHeight;
    private CurrentStatus status;//当前所处的状态
    private float dampingRatio;//滑动的阻尼比

    public RefreshRecycleView(Context context) {
        this(context, null);
    }

    public RefreshRecycleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        status = CurrentStatus.NORMAL;
        dampingRatio = 0.4f;
    }

    public void setRefreshHelper(RefreshHelper helper) {
        this.helper = helper;
        addRefreshView();//添加刷新的头部
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        addDefaultView(adapter);//添加一个默认的下拉刷新动画
    }

    private void addDefaultView(Adapter adapter) {
        if (adapter != null&&getLayoutManager()!=null) {
            View refreshView = LayoutInflater.from(getContext()).inflate(R.layout.layout_refresh, this, false);
            addHeadView(refreshView);//添加到头部
            this.refreshView = refreshView;
        }
    }

    private void addRefreshView() {
        Adapter adapter = getAdapter();
        if (adapter != null && helper != null) {
            View headRefreshView = helper.getRefreshView(getContext(), this);
            if(refreshView!=null&&headRefreshView!=null) {
                removeHeadView(refreshView);
                addHeadView(headRefreshView);//添加到头部
                this.refreshView = headRefreshView;//更新内存中的头部view实例
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            if (refreshView != null) {
                mRefreshViewHeight = refreshView.getMeasuredHeight();//测量后重新获取测量后的view高度
                setTopMargin(-mRefreshViewHeight);
            }
        }
    }

    /**
     * 设置刷新头的marginTop
     *
     * @param marginTop
     */
    private void setTopMargin(int marginTop) {
        //对recycleview设置margintop时会产生整个布局抖动，故只对refreView做操作，节省内存
//        MarginLayoutParams params = (MarginLayoutParams) this.getLayoutParams();
//        if(refreshHeight<-mRefreshViewHeight) {
//            refreshHeight=-mRefreshViewHeight;//屏蔽非法值
//        }
//        params.topMargin=refreshHeight;
//        this.setLayoutParams(params);
        if (refreshView != null) {
            MarginLayoutParams params = (MarginLayoutParams) refreshView.getLayoutParams();
            if (marginTop < -mRefreshViewHeight) {
                marginTop = -mRefreshViewHeight;//屏蔽非法值
            }
            params.topMargin = marginTop;
            refreshView.setLayoutParams(params);
        }

    }

    private float downY;//手指按下时的Y坐标
    private float moveY;//手指移动时的Y坐标
    private float newMarginTop;//refreshView新的marginTop值


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //写在这里是因为有可能事件被子类消费而不经过自身的onTouch方法
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                break;

        }


        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                //屏蔽正在刷新时再次进入
                if (status == CurrentStatus.NORMAL || status == CurrentStatus.PULLING) {
                    moveY = ev.getY();
                    float distanceY = dampingRatio * (moveY - downY);//滑动的距离
                    newMarginTop = distanceY - mRefreshViewHeight;//新的marginTop=滑动的距离-刷新头的高度

                    if(newMarginTop>=-mRefreshViewHeight) {//屏蔽向上滑动
                        setTopMargin((int) newMarginTop);//设置刷新头的margintop
                        status = CurrentStatus.PULLING;//更改此时的状态为正在拖拽
                        if (helper != null) {
                            helper.onPull((int) newMarginTop, mRefreshViewHeight);//更新ui动画
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (status == CurrentStatus.PULLING) {
                    if (newMarginTop > 0) {
                        //下拉的高度超出refreshView的高度后，回弹至refreshView高度，展示动画表明正在刷新
                        reBoundToRefreshing(newMarginTop, 0);
                        status = CurrentStatus.REFRESHING;//更改此时的状态为正在刷新
                        if (listener != null) {
                            listener.onRefreshing();
                        }
                        if (helper != null) {
                            helper.onRefreshing();
                        }
                    } else if (newMarginTop >= -mRefreshViewHeight) {
                        //回弹至原始状态
                        reBoundToRefreshing(0, -mRefreshViewHeight);
                        status = CurrentStatus.NORMAL;//虽然由于动画的原因，回弹还没完毕，但更改此时的状态无关大雅
                    }
                }

                break;
        }


        return super.onTouchEvent(ev);
    }

    /**
     * 回弹至指定状态，带动画效果
     * @param start
     * @param end
     */
    private void reBoundToRefreshing(float start, float end) {
        if (refreshView == null) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(start, end);
        animator.setDuration(200)
                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float animatedValue = (float) animation.getAnimatedValue();
                        setTopMargin((int) animatedValue);
                    }
                });
        animator.start();
    }

    /**
     * 停止刷新动画,影藏刷新动画所在的头部view
     */
    public void StopRefresh() {
        if (helper != null) {
            helper.stopRefreshing();//执行结束动画
        }
        //收起头部
        refreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                setTopMargin(-mRefreshViewHeight);
                status = CurrentStatus.NORMAL;//状态置为正常状态
            }
        },500);


    }


    private enum CurrentStatus {
        NORMAL, PULLING, REFRESHING;//上个状态:正常、正在下拉、正在刷新
    }

    public interface RefreshListener {
        void onRefreshing();
    }

    private RefreshListener listener;

    public void setOnRefreshingListener(RefreshListener listener) {
        this.listener = listener;
    }
}
