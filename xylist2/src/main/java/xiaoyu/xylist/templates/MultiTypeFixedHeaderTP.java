package xiaoyu.xylist.templates;

import android.view.View;

import xiaoyu.xylist.interf.IViewBehavior;

/**
 * Created by lee on 16/10/8.
 * <p>
 * 最基本的模版, 结构如下, 是 竖向 的结构
 * --- BasicTemplate ----
 * <p>
 * 布局
 * --- 头部 ---
 * --- 1 ----
 * --- 2 ----
 * --- 3 ----
 * --- 4 ----
 * --- 5 ----
 * --- 6 ----
 * --- 7 ----
 * --- 尾部 ---
 * <p>
 * 空布局
 * --- 数据1 ---
 * --- 数据2 ---
 * --- 自定义空布局 ---
 */
public class MultiTypeFixedHeaderTP extends BaseTP {

    protected IViewBehavior emptyBehavior = new IViewBehavior() {
        @Override
        public boolean hasData() {
            return false;
        }

        @Override
        public View getView() {
            View emptyView = mManager.getEmptyView();
            emptyView.setLayoutParams(recyclerView.getLayoutParams());

            return emptyView;
        }

        @Override
        public void setValue(View v, Object o) {
        }
    };

    @Override
    public boolean setEmptyView() {
        currentViewBehaviors.remove(emptyBehavior);

        if (mManager.getDatas() == null || mManager.getDatas().size() == 0) {
            currentViewBehaviors.remove(footBehavior);
            currentViewBehaviors.add(emptyBehavior);

            adapter.setViewBehavior(currentViewBehaviors);
            recyclerView.setAdapter(adapter);

            return true;
        } else {
            adapter.setViewBehavior(currentViewBehaviors);
            recyclerView.setAdapter(adapter);

            return false;
        }
    }
}
