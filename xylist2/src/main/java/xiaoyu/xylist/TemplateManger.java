package xiaoyu.xylist;

import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import xiaoyu.xylist.adapter.ItemViewBuilder;
import xiaoyu.xylist.interf.ITemplate;

/**
 * Created by lee on 16/10/8.
 */

public class TemplateManger {

    private ITemplate mTemplate;
    private View mContentView;
    private View mEmptyView;
    private int mOptions;
    private List mDatas;
    private int dividePx;

    public TemplateManger(ITemplate template) {
        this.mTemplate = template;
    }

    public TemplateManger setOptions(int value) {
        mOptions = value;
        return this;
    }

    public int getOptions() {
        return mOptions;
    }

    public TemplateManger setDatas(List datas) {
        System.out.println("setDatas TemplateManger");

        if (datas == null) {
            mDatas = null;
        } else {
            if (mDatas == null) {
                mDatas = new ArrayList();
            }
            mDatas.clear();
            mDatas.addAll(datas);
        }

        if (mContentView != null) {
            mTemplate.refreshData();
        }

        return this;
    }

    public List getDatas() {
        return mDatas;
    }

    public TemplateManger setEmptyView(View emptyView) {
        mEmptyView = emptyView;
        return this;
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public TemplateManger into(View contentView, ItemViewBuilder itemViewBuilder) {
        mContentView = contentView;

        if (mContentView instanceof RecyclerView) {
            dpToPx();
            ((RecyclerView) mContentView).addItemDecoration(new SpaceItemDecoration(dividePx));
        }

        mTemplate.setTemplateManager(this, contentView, itemViewBuilder);
        return this;
    }

    public TemplateManger setDivider(int dp) {
        dividePx = dp;
        return this;
    }

    private void dpToPx() {
        DisplayMetrics dm = mContentView.getContext().getApplicationContext().getResources().getDisplayMetrics();
        dividePx = (int)(dividePx * dm.density + 0.5f);
    }
}
