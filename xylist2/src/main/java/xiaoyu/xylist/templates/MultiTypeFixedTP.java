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
import xiaoyu.xylist.adapter.ItemViewBuilder;
import xiaoyu.xylist.adapter.XYAdapter;
import xiaoyu.xylist.footers.XYFooterView;
import xiaoyu.xylist.headers.XYHeaderView;
import xiaoyu.xylist.interf.IBuildItem;
import xiaoyu.xylist.interf.ITemplate;

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
public class MultiTypeFixedTP implements ITemplate {

    private static int TYPE_FOOTER = -1;
    private static int TYPE_EMPTY = -2;

    private TemplateManger mManager;
    private RecyclerView recyclerView;

    private ItemViewBuilder itemViewBuilder;
    private ItemViewBuilder currentUsedViewBuilder;
    private ItemViewBuilder withFootViewBuilder;
    private ItemViewBuilder withEmptyViewBuilder;

    private XYAdapter adapter;

    private ViewGroup parentGroup;
    private XYHeaderView xyHeaderView;
    private XYFooterView xyFooterView;
    private XYPtrFrameLayout ptrFrameLayout;
    private int listViewPos = -1;

    private List tmpList = new ArrayList();

    @Override
    public void setTemplateManager(TemplateManger manager, View contentView, ItemViewBuilder itemViewBuilder) {
        mManager = manager;

        if (!(contentView instanceof RecyclerView)) return;

        this.recyclerView = (RecyclerView) contentView;
        this.itemViewBuilder = itemViewBuilder;
        currentUsedViewBuilder = itemViewBuilder;

        refreshData();
    }

    @Override
    public void refreshData() {
        if (recyclerView == null) return;

        if (ptrFrameLayout != null) {
            ptrFrameLayout.refreshComplete();
        }

        init();
        if (setEmptyView()) return;

        adapter.notifyDataSetChanged();
    }

    private void init() {
        buildAdapter();
        setListView();
    }

    private boolean setEmptyView() {
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

    private void buildAdapter() {
        if (adapter == null) {
            adapter = new XYAdapter(mManager);
            adapter.setItemViewBuilder(itemViewBuilder);

            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
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

    private boolean isOption(int a) {
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

    private void findListViewPos() {
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
                if (itemViewBuilder.getDataLoad() != null) {
                    itemViewBuilder.getDataLoad().refresh();
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

            withFootViewBuilder = new ItemViewBuilder();
            withFootViewBuilder.setDataLoad(itemViewBuilder.getDataLoad());
            withFootViewBuilder.setiBuildItem(new IBuildItem() {
                @Override
                public void set(View view, int position, Object value) {
                    if (getItemType(position) != TYPE_FOOTER) {
                        itemViewBuilder.getiBuildItem().set(view, position, value);
                    }
                }

                @Override
                public View get(int viewType) {
                    if (viewType == TYPE_FOOTER) {
                        return xyFooterView;
                    }

                    return itemViewBuilder.getiBuildItem().get(viewType);
                }

                @Override
                public int getItemType(int position) {
                    if (position == getItemCount() - 1) {
                        return TYPE_FOOTER;
                    }
                    return itemViewBuilder.getiBuildItem().getItemType(position);
                }

                @Override
                public int getItemCount() {
                    return itemViewBuilder.getiBuildItem().getItemCount() + 1;
                }
            });

            adapter.setItemViewBuilder(withFootViewBuilder);
            currentUsedViewBuilder = withFootViewBuilder;
        }

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

            if (itemViewBuilder.getDataLoad() == null) return;
            if (xyFooterView == null) return;
            if (xyFooterView.getStatus() != XYFooterView.STATUS_LOAD_MORE) return;
            if (dy <= 0) return;

            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
            if (lastVisibleItemPosition == itemViewBuilder.getiBuildItem().getItemCount()) {
                xyFooterView.setStatus(XYFooterView.STATUS_LOADING);
                itemViewBuilder.getDataLoad().loadMore();
            }
        }
    };

    private View.OnClickListener mFootClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (xyFooterView == null) return;
            if (xyFooterView.getStatus() != XYFooterView.STATUS_LOAD_MORE) return;

            xyFooterView.setStatus(XYFooterView.STATUS_LOADING);
            itemViewBuilder.getDataLoad().loadMore();
        }
    };
}
