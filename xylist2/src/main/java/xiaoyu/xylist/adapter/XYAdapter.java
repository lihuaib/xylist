package xiaoyu.xylist.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import xiaoyu.xylist.TemplateManger;
import xiaoyu.xylist.XYOptions;

/**
 * Created by lee on 16/10/8.
 */

public class XYAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private TemplateManger mManager;
    private ItemViewBuilder itemViewBuilder;

    public XYAdapter(TemplateManger manger) {
        mManager = manger;
    }

    public void setItemViewBuilder(ItemViewBuilder itemViewBuilder) {
        this.itemViewBuilder = itemViewBuilder;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(itemViewBuilder.getiBuildItem().get(viewType));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int pos = position;
        int total = itemViewBuilder.getiBuildItem().getItemCount();

        if (XYOptions.isContains(XYOptions.canLoadMore, mManager.getOptions())) {
            total = total - 1;
        }

        if (XYOptions.isContains(XYOptions.isMultiType, mManager.getOptions())) {
            pos = position - total + (mManager.getDatas() == null ? 0 : mManager.getDatas().size());
        }

        Object value = null;
        if (pos >= 0 && pos < mManager.getDatas().size()) {
            value = mManager.getDatas().get(pos);
        }

        itemViewBuilder.getiBuildItem().set(holder.itemView, position, value);
    }

    @Override
    public int getItemCount() {
        return itemViewBuilder.getiBuildItem().getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return itemViewBuilder.getiBuildItem().getItemType(position);
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {
        public ItemHolder(View itemView) {
            super(itemView);
        }
    }
}
