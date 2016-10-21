package xiaoyu.xylist.templates.swipe;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.ScrollerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;

/**
 * Created by lee on 16/10/16.
 * <p>
 * 用于拇指向左滑动, 然后可以出来一系列菜单
 */
public class SwipeItem extends RelativeLayout {

    // 主要布局
    View vContent;
    // 右边布局
    View vMenus;

    private ScrollerCompat mOpenScroller;
    private GestureDetectorCompat mGestureDetector;
    private OnClickListener mOnClickListener;

    int duration = 200;
    float mDownX = 0;
    boolean isMoved = false;
    boolean isOpen = false;

    SwipeTP mSwipeTP;

    public SwipeItem(Context context, SwipeTP swipeTP) {
        super(context, null);
        mSwipeTP = swipeTP;

        init();
    }

    private void init() {
        //mOpenScroller = ScrollerCompat.create(this.getContext(), new BounceInterpolator());
        mOpenScroller = ScrollerCompat.create(this.getContext(), new AccelerateInterpolator());

        bindEvent();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {

        mSwipeTP.setSwipeItemHeight(getHeight());
        mSwipeTP.setContentWidth(vContent.getWidth());
        mSwipeTP.setMenuWidth(vMenus.getWidth());

        return super.drawChild(canvas, child, drawingTime);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean res = mGestureDetector.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_UP
                || event.getAction() == MotionEvent.ACTION_CANCEL) {
            if (isMoved) {
                int dis = (int) (mDownX - event.getX());

                swipeEffect(dis);
            }

            if (event.getAction() == MotionEvent.ACTION_UP) {
                return false;
            }
        }

        return res;
    }

    public void setViews(View content, View menus) {
        vContent = content;
        vMenus = menus;

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        vContent.setLayoutParams(params);

        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                mSwipeTP.getSwipeItemHeight() == 0 ? LayoutParams.MATCH_PARENT : mSwipeTP.getSwipeItemHeight());
        vMenus.setLayoutParams(params2);

        this.addView(vMenus);
        this.addView(vContent);

        if (mSwipeTP.isAllItemMenuOpen) {
            openMenu();
            invalidate();
        }
    }

    public View getContentView() {
        return vContent;
    }

    public View getMenus() {
        return vMenus;
    }

    public void closeMenu() {
        swipeEffect(0);
        isOpen = false;
    }

    public void openMenu() {
        onSwipe(mSwipeTP.getMenuWidth());
        isOpen = true;
    }

    public boolean isOpen() {
        return isOpen;
    }

    private void bindEvent() {
        mGestureDetector = new GestureDetectorCompat(getContext(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDown(MotionEvent event) {
                mDownX = event.getX();
                isMoved = false;


                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                isMoved = true;
                int dpx = (int) (e1.getX() - e2.getX());
                mDownX = e1.getX();

                onSwipe(dpx);

                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });
    }

    /*
    * 在移动的过程中， 重新布局整个 item
    * */
    private void onSwipe(int dis) {
        if (vMenus.getHeight() < mSwipeTP.getSwipeItemHeight()) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, mSwipeTP.getSwipeItemHeight());
            vMenus.setLayoutParams(params);
        }

        int maxDis = mSwipeTP.getMenuWidth();
        boolean toLeft = dis >= 0;
        dis = Math.abs(dis);
        if (dis > maxDis)
            dis = maxDis;

        int rl_left;
        int rl_rleft;
        if (toLeft) { // 向左移动
            rl_left = -dis;
            rl_rleft = mSwipeTP.getContentWidth() - dis;

            vContent.layout(rl_left, vContent.getTop(), mSwipeTP.getContentWidth() + rl_left, vContent.getBottom());
            vMenus.layout(rl_rleft, vMenus.getTop(), rl_rleft + mSwipeTP.getMenuWidth(), vMenus.getBottom());
        } else { // 向右移动
            rl_left = dis - maxDis;
            rl_rleft = mSwipeTP.getContentWidth() + dis - maxDis;

            if (vContent.getLeft() >= 0) return;
            vContent.layout(rl_left, vContent.getTop(), mSwipeTP.getContentWidth() + rl_left, vContent.getBottom());
            vMenus.layout(rl_rleft, vMenus.getTop(), rl_rleft + mSwipeTP.getMenuWidth(), vMenus.getBottom());
        }
    }

    private void swipeEffect(int dis) {
        boolean toLeft = dis >= 0;
        dis = Math.abs(dis);
        int rwidth = mSwipeTP.getMenuWidth();
        if (dis > rwidth) dis = rwidth;

        int rl_left;
        int rl_rleft;

        if (dis >= rwidth / 2) {
            if (toLeft) { // 向左滑动
                rl_left = -rwidth;
                rl_rleft = mSwipeTP.getContentWidth() - rwidth;
                mOpenScroller.startScroll(-(rwidth - dis), 0, (rwidth - dis), 0, duration);
            } else { // 向右滑动
                rl_left = 0;
                rl_rleft = mSwipeTP.getContentWidth();
                mOpenScroller.startScroll(rwidth - dis, 0, -(rwidth - dis), 0, duration);
            }
            closeOtherMenu();
            isOpen = toLeft;
        } else {
            // 恢复到默认位置
            if (toLeft) { // 向左滑动, 则恢复到右边的原来状态
                rl_left = 0;
                rl_rleft = mSwipeTP.getContentWidth();
                mOpenScroller.startScroll(dis, 0, -dis, 0, duration);
            } else { // 向右滑动 则恢复到左边的原来状态
                rl_left = -rwidth;
                rl_rleft = mSwipeTP.getContentWidth() - rwidth;
                mOpenScroller.startScroll(-dis, 0, dis, 0, duration);
            }
        }
        vContent.layout(rl_left, vContent.getTop(), mSwipeTP.getContentWidth() + rl_left, vContent.getBottom());
        vMenus.layout(rl_rleft, vMenus.getTop(), rl_rleft + mSwipeTP.getMenuWidth(), vMenus.getBottom());

        invalidate();
    }

    @Override
    public void computeScroll() {
        //先判断mScroller滚动是否完成
        if (mOpenScroller.computeScrollOffset()) {

            //这里调用View的scrollTo()完成实际的滚动
            scrollTo(mOpenScroller.getCurrX(), mOpenScroller.getCurrY());

            //必须调用该方法，否则不一定能看到滚动效果
            postInvalidate();
        }
        super.computeScroll();
    }

    private void closeOtherMenu() {
        RecyclerView recyclerView = (RecyclerView) this.getParent();
        chgOtherItemMenuStatus((LinearLayoutManager) recyclerView.getLayoutManager(), false);
    }

    public static void chgOtherItemMenuStatus(LinearLayoutManager manager, boolean open) {
        int count = manager.getChildCount();
        for (int i = 0; i < count; i++) {
            if (manager.getChildAt(i) instanceof SwipeItem) {
                SwipeItem item = (SwipeItem) manager.getChildAt(i);
                if (open) {
                    item.openMenu();
                } else {
                    if (item.isOpen) {
                        item.closeMenu();
                    }
                }
            }
        }
    }
}
