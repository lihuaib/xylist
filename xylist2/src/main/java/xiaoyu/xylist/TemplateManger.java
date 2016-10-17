package xiaoyu.xylist;

import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import xiaoyu.xylist.interf.IDataLoad;
import xiaoyu.xylist.interf.ITemplate;
import xiaoyu.xylist.interf.IViewBehavior;

/**
 * Created by lee on 16/10/8.
 */

public class TemplateManger {

    private ITemplate mTemplate;
    private View mContentView;
    private View mEmptyView;
    private int mOptions;
    private List<IViewBehavior> iViewBehaviors;
    private IDataLoad iDataLoad;
    private List mDatas;
    private int dividePx;

    public TemplateManger(ITemplate template) {
        this.mTemplate = template;
    }

    public TemplateManger setOptions(int value) {
        mOptions = value;
        return this;
    }

    public ITemplate getTemplate() {
        return mTemplate;
    }

    public int getOptions() {
        return mOptions;
    }

    public TemplateManger setDatas(List datas) {
        System.out.println("setDatas TemplateManger");

        if (mDatas == null) {
            mDatas = new ArrayList();
        }
        mDatas.clear();

        if (datas != null) {
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

    public TemplateManger setTypeList(List<IViewBehavior> viewBehaviorList) {
        iViewBehaviors = viewBehaviorList;
        return this;
    }

    public List<IViewBehavior> getTypeList() {
        return iViewBehaviors;
    }

    public TemplateManger setDataLoad(IDataLoad dataLoad) {
        iDataLoad = dataLoad;
        return this;
    }

    public IDataLoad getDataLoad() {
        return iDataLoad;
    }

    public TemplateManger into(View contentView) {
        mContentView = contentView;

        if (mContentView instanceof RecyclerView) {
            dpToPx();
            ((RecyclerView) mContentView).addItemDecoration(new SpaceItemDecoration(dividePx));
        }

        mTemplate.setTemplateManager(this, contentView);
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
