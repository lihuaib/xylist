package xiaoyu.xylist.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import xiaoyu.xylist.TemplateManger;
import xiaoyu.xylist.interf.IViewBehavior;

/**
 * Created by lee on 16/10/8.
 */

public class XYAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected TemplateManger mManager;
    protected List<IViewBehavior> iViewBehaviors;

    public XYAdapter(TemplateManger manger) {
        mManager = manger;
    }

    public TemplateManger getManager() {
        return mManager;
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
        int viewType = getItemViewType(position);
        IViewBehavior currBehavior = iViewBehaviors.get(viewType);

        if (currBehavior != null) {
            if (!currBehavior.hasData()) {
                currBehavior.setValue(holder.itemView, null);
            } else {
                //TODO 这里只适合有多个type,但是只有一个type 是有数据的情况
                int pos = position - viewType;

                currBehavior.setValue(holder.itemView, mManager.getDatas().get(pos));
            }
        }
    }

    @Override
    public int getItemCount() {
        int total = iViewBehaviors.size() - 1;
        total += mManager.getDatas() != null ? mManager.getDatas().size() : 0;

        return total;
    }

    @Override
    public int getItemViewType(int position) {
        int len = 0, i, size = iViewBehaviors.size();
        for (i = 0; i < size; i++) {
            IViewBehavior behavior = iViewBehaviors.get(i);
            if (!behavior.hasData()) {
                len++;
            } else {
                len += mManager.getDatas().size();
            }
            if (position < len) {
                break;
            }
        }

        return i;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {
        public ItemHolder(View itemView) {
            super(itemView);
        }
    }
}
