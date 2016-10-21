package xiaoyu.xylist.templates.HorizonPageTP;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

/**
 * Created by lee on 16/10/20.
 */

public class LeftRightRecyclerViewHelper {

    private static float V_ZOOM_IN = 0.5f;
    private static float V_ZOOM_OUT = 1.0f;
    private static float SCREEN_WIDTH = 0;

    public static class OnRcvScrollListener extends RecyclerView.OnScrollListener {

        /**
         * 最后一个可见的item的位置
         */
        private int lastVisibleItemPosition;

        /**
         * 当前滑动的状态
         */
        private int currentScrollState = 0;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            MyLinearLayoutManager layoutManager = (MyLinearLayoutManager) recyclerView.getLayoutManager();
            lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            changeSize(recyclerView.getContext(), layoutManager);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            currentScrollState = newState;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();

            if (currentScrollState == RecyclerView.SCROLL_STATE_IDLE) {
                if ((visibleItemCount > 0 && (lastVisibleItemPosition) >= totalItemCount - 1)) {

                    View v = layoutManager.getChildAt(visibleItemCount - 1);
                    if (v != null) {
                        v.animate().scaleX(V_ZOOM_OUT).scaleY(V_ZOOM_OUT).setDuration(200).start();
                        v.bringToFront();
                    }

                    onLoadMore();
                } else if ((visibleItemCount > 0 && lastVisibleItemPosition <= 1)) {
                    onLoadNew();
                }
            }
        }

        public void onLoadMore() {
            Log.d("jlee", "onLoadMore");
        }

        public void onLoadNew() {
            Log.d("jlee", "onLoadNew");
        }


    }

    public static class MyLinearLayoutManager extends LinearLayoutManager {
        Context mContext;

        public MyLinearLayoutManager(Context context) {
            super(context, LinearLayoutManager.HORIZONTAL, true);
            mContext = context;
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
                changeSize(mContext, this);
            } catch (IndexOutOfBoundsException e) {
                Log.e("jlee", e.toString());
            }
        }

        @Override
        public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
            int scrolled = super.scrollHorizontallyBy(dx, recycler, state);

            return scrolled;
        }


    }

    public static class ItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int total = parent.getLayoutManager().getItemCount();
            int pos = parent.getChildLayoutPosition(view);

            int d = (int) (-parent.getWidth() / 4f);
            if (pos != total - 1) {
                outRect.left = d;
            }
        }
    }

    private static void changeSize(Context context, MyLinearLayoutManager layoutManager) {
        float midpoint = getScreenWidth(context) / 2;
        float d1 = midpoint;
        float s0 = 1.f;

        float maxScale = -1;
        int maxPos = -1;
        int childCount = layoutManager.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = layoutManager.getChildAt(i);
            float childMidpoint = child.getX() + child.getWidth() / 2;
            float d = Math.min(d1, Math.abs(midpoint - childMidpoint));
            float scale = s0 - V_ZOOM_IN * d / d1;

            child.setScaleX(scale);
            child.setScaleY(scale);

            if (maxScale < scale) {
                maxScale = scale;
                maxPos = i;
            }
        }

        if (maxPos >= 0) {
            layoutManager.getChildAt(maxPos).bringToFront();
        }
    }

    private static float getScreenWidth(Context context) {
        if(SCREEN_WIDTH != 0) return SCREEN_WIDTH;

        if(context == null) return 0;

        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        SCREEN_WIDTH = dm.widthPixels;
        return SCREEN_WIDTH;
    }
}