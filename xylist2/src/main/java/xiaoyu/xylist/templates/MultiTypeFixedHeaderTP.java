package xiaoyu.xylist.templates;

import android.view.View;

import xiaoyu.xylist.adapter.ItemViewBuilder;
import xiaoyu.xylist.interf.IBuildItem;

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

    @Override
    public boolean setEmptyView() {
        if (mManager.getDatas() == null || mManager.getDatas().size() == 0) {
            withEmptyViewBuilder = new ItemViewBuilder();
            withEmptyViewBuilder.setDataLoad(itemViewBuilder.getDataLoad());
            withEmptyViewBuilder.setiBuildItem(new IBuildItem() {
                @Override
                public void set(View view, int position, Object value) {
                    if (position < getItemCount() - 1) {
                        itemViewBuilder.getiBuildItem().set(view, position, value);
                    }
                }

                @Override
                public View get(int viewType) {
                    if (viewType == TYPE_EMPTY) {
                        View emptyView = mManager.getEmptyView();
                        emptyView.setLayoutParams(recyclerView.getLayoutParams());

                        return emptyView;
                    }

                    return itemViewBuilder.getiBuildItem().get(viewType);
                }

                @Override
                public int getItemType(int position) {
                    if (mManager.getEmptyView() != null
                            && position == getItemCount() - 1) {
                        return TYPE_EMPTY;
                    }
                    return itemViewBuilder.getiBuildItem().getItemType(position);
                }

                @Override
                public int getItemCount() {
                    if (mManager.getEmptyView() == null) {
                        return itemViewBuilder.getiBuildItem().getItemCount();
                    }
                    return itemViewBuilder.getiBuildItem().getItemCount() + 1;
                }
            });

            adapter.setItemViewBuilder(withEmptyViewBuilder);
            return true;
        } else {
            adapter.setItemViewBuilder(currentUsedViewBuilder);
        }

        return false;
    }
}
