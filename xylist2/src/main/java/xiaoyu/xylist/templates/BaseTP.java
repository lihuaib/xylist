package xiaoyu.xylist.templates;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import xiaoyu.xylist.TemplateManger;
import xiaoyu.xylist.XYOptions;
import xiaoyu.xylist.XYPtrFrameLayout;
import xiaoyu.xylist.adapter.XYAdapter;
import xiaoyu.xylist.footers.XYFooterView;
import xiaoyu.xylist.headers.XYHeaderView;
import xiaoyu.xylist.interf.ITemplate;
import xiaoyu.xylist.interf.IViewBehavior;

/**
 * Created by lee on 16/10/16.
 */

public abstract class BaseTP implements ITemplate {

    protected TemplateManger mManager;

    protected RecyclerView recyclerView;
    protected LinearLayoutManager linearLayoutManager;

    protected XYAdapter adapter;

    protected ViewGroup parentGroup;
    protected XYHeaderView xyHeaderView;
    protected XYFooterView xyFooterView;
    protected XYPtrFrameLayout ptrFrameLayout;

    protected List<IViewBehavior> currentViewBehaviors = new ArrayList<>();
    protected IViewBehavior footBehavior;

    /**
     * 用于底部加载控件的状态显示
     */
    protected List tmpList = new ArrayList();

    protected int listViewPos = -1;

    @Override
    public void setTemplateManager(TemplateManger manager, View contentView) {
        mManager = manager;

        if (!(contentView instanceof RecyclerView)) return;

        this.recyclerView = (RecyclerView) contentView;
        this.currentViewBehaviors.addAll(manager.getTypeList());

        refreshData();
    }

    @Override
    public void refreshData() {
        if (recyclerView == null) return;

        init();

        if (ptrFrameLayout != null) {
            ptrFrameLayout.refreshComplete();
        }

        if (setEmptyView()) return;

        adapter.notifyDataSetChanged();
    }

    private void init() {
        buildAdapter();
        setListView();
    }

    public abstract boolean setEmptyView();

    private void buildAdapter() {
        if (adapter == null) {
            adapter = new XYAdapter(mManager);
            adapter.setViewBehavior(currentViewBehaviors);

            recyclerView.setLayoutManager(linearLayoutManager = new LinearLayoutManager(recyclerView.getContext()));
            recyclerView.setAdapter(adapter);
        }
    }

    private void setListView() {
        if (isOption(XYOptions.canPulltoRefresh | XYOptions.canLoadMore)) {
            buildPtrFrameLayout();
            setHeader();
            setFooter();
        } else if (isOption(XYOptions.canPulltoRefresh)) {
            buildPtrFrameLayout();
            setHeader();
        } else if (isOption(XYOptions.canLoadMore)) {
            buildPtrFrameLayout();
            ptrFrameLayout.setPullToRefresh(false);
            setFooter();
        } else if (isOption(XYOptions.none)) {
            // do nothing
        }
    }

    protected boolean isOption(int a) {
        return XYOptions.isContains(a, mManager.getOptions());
    }

    private void buildPtrFrameLayout() {
        findListViewPos();

        parentGroup.removeViewAt(listViewPos);

        if (ptrFrameLayout == null) {
            ptrFrameLayout = new XYPtrFrameLayout(recyclerView.getContext());

            ptrFrameLayout.setLayoutParams(recyclerView.getLayoutParams());
            ptrFrameLayout.setContentView(recyclerView);
            ptrFrameLayout.disableWhenHorizontalMove(true);
        }

        parentGroup.addView(ptrFrameLayout, listViewPos);
    }

    protected void findListViewPos() {
        if (listViewPos != -1) return;

        if (parentGroup == null) {
            parentGroup = (ViewGroup) recyclerView.getParent();
        }
        int cnt = parentGroup.getChildCount();
        for (int i = 0; i < cnt; i++) {
            if (parentGroup.getChildAt(i) == recyclerView) {
                listViewPos = i;
                break;
            }
        }
    }

    private void setHeader() {
        if (xyHeaderView != null) return;

        xyHeaderView = new XYHeaderView(recyclerView.getContext());
        xyHeaderView.setLayoutParams(new PtrFrameLayout.LayoutParams(PtrFrameLayout.LayoutParams.MATCH_PARENT, PtrFrameLayout.LayoutParams.WRAP_CONTENT));

        setPullToFreshOption();
        ptrFrameLayout.setHeaderView(xyHeaderView);
        ptrFrameLayout.addPtrUIHandler(xyHeaderView);
        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (mManager.getDataLoad() != null) {
                    mManager.getDataLoad().refresh();
                }
            }
        });
    }

    private void setPullToFreshOption() {
        ptrFrameLayout.setDurationToClose(200);
        ptrFrameLayout.setDurationToCloseHeader(500);
        ptrFrameLayout.setKeepHeaderWhenRefresh(true);
        ptrFrameLayout.setPullToRefresh(true);
        ptrFrameLayout.setRatioOfHeaderHeightToRefresh(1.2f);
        ptrFrameLayout.setResistance(1.7f);
    }

    private void setFooter() {
        if (xyFooterView == null) {
            xyFooterView = XYFooterView.get(recyclerView.getContext());
            xyFooterView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            xyFooterView.setStatus(XYFooterView.STATUS_LOAD_MORE);

            footBehavior = new IViewBehavior() {
                @Override
                public List getData() {
                    return null;
                }

                @Override
                public View getView() {
                    return xyFooterView;
                }

                @Override
                public void setValue(View v, Object o) {
                }
            };

            adapter.setViewBehavior(currentViewBehaviors);
        }

        currentViewBehaviors.remove(footBehavior);
        currentViewBehaviors.add(footBehavior);

        recyclerView.removeOnScrollListener(mScrollListener);
        xyFooterView.setOnClickListener(null);
        xyFooterView.setClickable(false);
        if (tmpList.size() == mManager.getDatas().size() && xyFooterView.getStatus() == XYFooterView.STATUS_LOADING) {
            xyFooterView.setStatus(XYFooterView.STATUS_NO_MORE_LOAD);
        } else {
            xyFooterView.setStatus(XYFooterView.STATUS_LOAD_MORE);
            recyclerView.addOnScrollListener(mScrollListener);
            xyFooterView.setClickable(true);
            xyFooterView.setOnClickListener(mFootClickListener);
        }

        tmpList.clear();
        tmpList.addAll(mManager.getDatas());
    }

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (mManager.getDataLoad() == null) return;
            if (xyFooterView == null) return;
            if (xyFooterView.getStatus() != XYFooterView.STATUS_LOAD_MORE) return;
            if (dy <= 0) return;

            int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
            if (lastVisibleItemPosition >= getItemCount() - 1) {
                xyFooterView.setStatus(XYFooterView.STATUS_LOADING);
                mManager.getDataLoad().loadMore();
            }
        }
    };

    private View.OnClickListener mFootClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (xyFooterView == null) return;
            if (xyFooterView.getStatus() != XYFooterView.STATUS_LOAD_MORE) return;

            xyFooterView.setStatus(XYFooterView.STATUS_LOADING);
            mManager.getDataLoad().loadMore();
        }
    };

    protected int getItemCount() {
        int cnt = 0;
        for (IViewBehavior behavior : currentViewBehaviors) {
            if (behavior.getData() == null) {
                cnt++;
            } else {
                cnt += behavior.getData().size();
            }
        }
        return cnt;
    }
}
