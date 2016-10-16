package xiaoyu.xylist.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import xiaoyu.xylist.TemplateManger;
import xiaoyu.xylist.XYOptions;
import xiaoyu.xylist.interf.IViewBehavior;

/**
 * Created by lee on 16/10/8.
 */

public class XYAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private TemplateManger mManager;
    private List<IViewBehavior> iViewBehaviors;

    public XYAdapter(TemplateManger manger) {
        mManager = manger;
    }

    public void setViewBehavior(List<IViewBehavior> iViewBehaviorList) {
        this.iViewBehaviors = iViewBehaviorList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        IViewBehavior behavior = iViewBehaviors.get(viewType);

        return new ItemHolder(behavior.getView());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int len = 0, cnt = 0;
        IViewBehavior currBehavior = null;
        for (IViewBehavior behavior : iViewBehaviors) {
            if (behavior.getData() == null) {
                len++;
            } else {
                len += behavior.getData().size();
            }

            if (position < len) {
                currBehavior = behavior;
                break;
            }

            cnt++;
        }

        if (currBehavior != null) {
            if (currBehavior.getData() == null) {
                currBehavior.setValue(null);
            } else {
                int pos = position - cnt;
                currBehavior.setValue(mManager.getDatas().get(pos));
            }
        }
    }

    @Override
    public int getItemCount() {
        int total = iViewBehaviors.size() - 1;
        total += mManager.getDatas() != null ? mManager.getDatas().size() : 0;

        if (isNeedFooter()) {
            total++;
        }

        return total;
    }

    @Override
    public int getItemViewType(int position) {
        int len = 0, i, size = iViewBehaviors.size();
        for (i = 0; i < size; i++) {
            IViewBehavior behavior = iViewBehaviors.get(i);
            if (behavior.getData() == null) {
                len++;
            } else {
                len += behavior.getData().size();
            }
            if (position < len) {
                break;
            }
        }

        if(i >= size) i = size - 1;

        return i;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {
        public ItemHolder(View itemView) {
            super(itemView);
        }
    }

    private boolean isNeedFooter() {
        return XYOptions.isContains(XYOptions.canLoadMore, mManager.getOptions());
    }
}
