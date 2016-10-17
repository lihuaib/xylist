package xiaoyu.xylist.templates.swipe;

import android.support.v7.widget.RecyclerView;

import xiaoyu.xylist.templates.BasicTP;

/**
 * Created by lee on 16/10/8.
 * <p>
 * 结构如下, 是 竖向 的结构
 * 模仿 IOS 的左滑, item 的右边出现一系列菜单
 * --- BasicTemplate ----
 * <p>
 * 布局
 * --- 头部 ---
 * --- 1 --Delete--
 * --- 2 --menu1, menu2--
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
public class SwipeTP extends BasicTP {

    protected boolean isAllItemMenuOpen = false;

    protected int swipeItemHeight;
    protected int contentWidth;
    protected int menuWidth;

    public SwipeTP() {
    }

    @Override
    public void refreshData() {
        super.refreshData();

        if (this.recyclerView != null) {
            this.recyclerView.removeOnScrollListener(mSwipeScrollListener);
            this.recyclerView.addOnScrollListener(mSwipeScrollListener);
        }
    }

    private RecyclerView.OnScrollListener mSwipeScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                SwipeItem.chgOtherItemMenuStatus(linearLayoutManager, isAllItemMenuOpen);
            }
        }
    };

    public boolean isAllItemMenuOpen() {
        return isAllItemMenuOpen;
    }

    public void setAllItemMenuOpen(boolean allItemMenuOpen) {
        isAllItemMenuOpen = allItemMenuOpen;

        SwipeItem.chgOtherItemMenuStatus(linearLayoutManager, allItemMenuOpen);
    }

    public int getSwipeItemHeight() {
        return swipeItemHeight;
    }

    public void setSwipeItemHeight(int swipeItemHeight) {
        if (this.swipeItemHeight < swipeItemHeight)
            this.swipeItemHeight = swipeItemHeight;
    }

    public int getContentWidth() {
        return contentWidth;
    }

    public void setContentWidth(int contentWidth) {
        if (this.contentWidth < contentWidth)
            this.contentWidth = contentWidth;
    }

    public int getMenuWidth() {
        return menuWidth;
    }

    public void setMenuWidth(int menuWidth) {
        if (this.menuWidth < menuWidth)
            this.menuWidth = menuWidth;
    }
}
