package xiaoyu.xylist;

/**
 * Created by lee on 16/10/8.
 */

public class XYOptions {

    /**
     * b 属性 是否包含 a 属性
     * @param a
     * @param b
     * @return
     */
    public static boolean isContains(int a, int b) {
        return a == (a & b);
    }

    /**
     * 没有任何特性
     */
    public static int none = 0;

    /**
     * 可以下拉刷新
     */
    public static int canPulltoRefresh = 1;

    /**
     * 可以上拉加载更多
     */
    public static int canLoadMore = 2;

    /**
     *  是否是多数据结构
     */
    public static int isMultiType = 4;

    /**
     * 当列表滚动到中间或者底部时,
     * 点击系统的back 可以到达列表顶部
     */
    public static int backToTop = 8;

    /**
     * 当列表被关闭后, 再次加载, 列表会滚动到上次关闭时浏览的位置
     */
    public static int remmberLastPos = 16;
}
