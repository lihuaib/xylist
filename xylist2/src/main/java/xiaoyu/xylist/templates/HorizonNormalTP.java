package xiaoyu.xylist.templates;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import xiaoyu.xylist.adapter.XYAdapter;
import xiaoyu.xylist.templates.HorizonPageTP.LeftRightRecyclerViewHelper;

/**
 * Created by lee on 16/10/20.
 * <p>
 * 最基本的模版, 结构如下, 是 横向 的结构
 * --- HorizonPageTP ----
 * <p>
 * 布局
 * --- page1 --- | --- page2 ---
 * <p>
 * 空布局
 * --- 自定义空布局 ---
 */
public class HorizonNormalTP extends BaseTP {

    protected void init() {
        buildAdapter();
    }

    protected void buildAdapter() {
        if (adapter == null) {
            adapter = new XYAdapter(mManager);
            adapter.setViewBehavior(currentViewBehaviors);

            recyclerView.setLayoutManager(linearLayoutManager = new LinearLayoutManager(recyclerView.getContext()));
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setAdapter(adapter);
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
