package xiaoyu.xylist.templates;

import android.view.View;

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
 * --- 自定义空布局 ---
 */
public class BasicTP extends BaseTP {

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
