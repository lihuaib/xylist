package xiaoyu.xylist.templates.ViewPagerTP;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import xiaoyu.xylist.adapter.XYAdapter;
import xiaoyu.xylist.templates.BaseTP;

/**
 * Created by lee on 16/10/21.
 *
 * 取代原来的 view pager, 横向滚动
 */
public class ViewPagerTP extends BaseTP {

    protected void init() {
        buildAdapter();
    }

    protected void buildAdapter() {
        if (adapter == null) {
            adapter = new XYAdapter(mManager);
            adapter.setViewBehavior(currentViewBehaviors);

            findListViewPos();

            if(listViewPos == -1) return;

            Context mContext = recyclerView.getContext();
            final RecyclerViewPager recyclerViewPager = new RecyclerViewPager(mContext);
            recyclerViewPager.setLayoutParams(recyclerView.getLayoutParams());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerViewPager.setLayoutManager(linearLayoutManager);
            recyclerViewPager.setAdapter(adapter);

            parentGroup.removeViewAt(listViewPos);
            parentGroup.addView(recyclerViewPager, listViewPos);
            recyclerView = recyclerViewPager;

            recyclerViewPager.addOnPageChangedListener(new RecyclerViewPager.OnPageChangedListener() {
                @Override
                public void OnPageChanged(int oldPosition, int newPosition) {
                    int cp = recyclerViewPager.getCurrentPosition();
                    if (cp < 0) return;

                    Log.d("lee", "oldPosition:" + oldPosition);
                    Log.d("lee", "newPosition:" + newPosition);
                }
            });
        }
    }

    @Override
    public boolean setEmptyView() {
        if (mManager.getDatas() == null || mManager.getDatas().size() == 0) {
            findListViewPos();

            View view = mManager.getEmptyView();
            if(view == null) return true;
            parentGroup.removeViewAt(listViewPos);

            if (ptrFrameLayout == null) {
                view.setLayoutParams(recyclerView.getLayoutParams());
            } else {
                view.setLayoutParams(ptrFrameLayout.getLayoutParams());
            }

            parentGroup.addView(view, listViewPos);

            return true;
        }

        return false;
    }
}