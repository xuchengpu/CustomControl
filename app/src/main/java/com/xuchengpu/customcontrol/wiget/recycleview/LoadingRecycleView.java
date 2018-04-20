package com.xuchengpu.customcontrol.wiget.recycleview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.xuchengpu.customcontrol.R;
import com.xuchengpu.customcontrol.utils.LoadingHelper;

/**
 * Created by 许成谱 on 2018/4/19 11:00.
 * qq:1550540124
 * 热爱生活每一天！
 */

public class LoadingRecycleView extends RefreshRecycleView {

    private LoadingHelper helper;//刷新动画帮助类，可借助此类自定义下拉刷新动画，开闭原则
    private View loadingView;//展示刷新的头部view
    private int mLoadingViewHeight;
    private CurrentStatus status;//当前所处的状态
    private float dampingRatio;//滑动的阻尼比

    public LoadingRecycleView(Context context) {
        this(context, null);
    }

    public LoadingRecycleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        status = CurrentStatus.NORMAL;
        dampingRatio = 0.4f;
    }

    public void setLoadingHelper(LoadingHelper helper) {
        this.helper = helper;
        addLoadingView();//添加刷新的头部
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        addDefaultView(adapter);//添加一个默认的下拉刷新动画
    }

    private void addDefaultView(Adapter adapter) {
        if (adapter != null&&getLayoutManager()!=null) {
            View refreshView = LayoutInflater.from(getContext()).inflate(R.layout.layout_refresh, this, false);
            this.loadingView = refreshView;
        }
    }

    private void addLoadingView() {
        Adapter adapter = getAdapter();
        if (adapter != null && helper != null) {
            View footLoadingView = helper.getLoadingView(getContext(), this);
            if(loadingView !=null&&footLoadingView!=null) {
                this.loadingView = footLoadingView;//更新内存中的头部view实例
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            if (loadingView != null) {
                mLoadingViewHeight = loadingView.getMeasuredHeight();//测量后重新获取测量后的view高度
            }
        }
    }

    /**
     * 设置刷新头的marginTop
     *
     * @param marginBottom
     */
    private void setBottomMargin(int marginBottom) {
        //对recycleview设置margintop时会产生整个布局抖动，故只对refreView做操作，节省内存
        if (loadingView != null) {
            MarginLayoutParams params = (MarginLayoutParams) loadingView.getLayoutParams();
            if (marginBottom < 0) {
                marginBottom = 0;//屏蔽非法值
            }
            params.bottomMargin = marginBottom;
            loadingView.setLayoutParams(params);
            Log.e("TAG", "marginBottom=="+marginBottom);
        }

    }

    private float downY;//手指按下时的Y坐标
    private float moveY;//手指移动时的Y坐标
    private float newMarginBottom;//refreshView新的marginTop值


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //写在这里是因为有可能事件被子类消费而不经过自身的onTouch方法
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                // 如果是在最底部才处理，否则不需要处理
                if (canScrollDown() ) {
                    // 如果没有到达最底端，也就是说还可以向上滚动就什么都不处理
                    removeFootView(loadingView);
                    status = CurrentStatus.NORMAL;
                    return super.dispatchTouchEvent(ev);
                }

                //屏蔽正在刷新时再次进入
                if (status == CurrentStatus.NORMAL || status == CurrentStatus.PULLING) {
                    moveY = ev.getY();

                    float distanceY = -dampingRatio * (moveY - downY);//滑动的距离
                    newMarginBottom = distanceY -mLoadingViewHeight;//新的marginTop=滑动的距离-刷新头的高度
                    if(newMarginBottom >-mLoadingViewHeight) {//屏蔽向下滑动,大于20个像素再添加
                        addFootView(loadingView);
                        scrollToPosition(getAdapter().getItemCount() - 1);//解决上拉过程中自动滚动的问题 可能是动画问题也可能是item复用机制导致的，后期再解决
                        setBottomMargin((int) newMarginBottom);//设置底部view的marginBottom
                        Log.e("TAG", "newMarginBottom=="+newMarginBottom);
                        status = CurrentStatus.PULLING;//更改此时的状态为正在拖拽
                        if (helper != null) {
                            helper.onPull((int) newMarginBottom, mLoadingViewHeight);//更新ui动画
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (status == CurrentStatus.PULLING) {
                    if (newMarginBottom >-mLoadingViewHeight) {
                        //下拉的高度超出refreshView的高度后，回弹至refreshView高度，展示动画表明正在刷新
                        reBoundToLoading(newMarginBottom, 0);
                        status = CurrentStatus.LOADING;//更改此时的状态为正在刷新
                        if (listener != null) {
                            listener.onLoading();
                        }
                        if (helper != null) {
                            helper.onLoading();
                        }
                    }
                }

                break;
        }


        return super.dispatchTouchEvent(ev);
    }

    /**
     * 回弹至指定状态，带动画效果
     * @param start
     * @param end
     */
    private void reBoundToLoading(float start, float end) {
        if (loadingView == null) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(start, end);
        animator.setDuration(200)
                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float animatedValue = (float) animation.getAnimatedValue();
                        setBottomMargin((int) animatedValue);
                    }
                });
        animator.start();
    }

    /**
     * 停止刷新动画,影藏刷新动画所在的头部view
     */
    public void StopLoading() {
        if (helper != null) {
            helper.stopLoading();//执行结束动画
        }
        //收起头部
        status = CurrentStatus.NORMAL;//状态置为正常状态
        removeFootView(loadingView);


    }
    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     * 判断是不是滚动到了最顶部，这个是从SwipeRefreshLayout里面copy过来的源代码
     */
    public boolean canScrollDown() {
        return ViewCompat.canScrollVertically(this, 1);
    }



    private enum CurrentStatus {
        NORMAL, PULLING, LOADING;//上个状态:正常、正在下拉、正在刷新
    }

    public interface LoadingListener {
        void onLoading();
    }

    private LoadingListener listener;

    public void setOnLoadingListener(LoadingListener listener) {
        this.listener = listener;
    }
}
