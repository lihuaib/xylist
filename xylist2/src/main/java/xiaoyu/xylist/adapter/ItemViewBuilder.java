package xiaoyu.xylist.adapter;

import xiaoyu.xylist.interf.IBuildItem;
import xiaoyu.xylist.interf.IDataLoad;

/**
 * Created by lee on 16/10/8.
 */

public class ItemViewBuilder {

    private IBuildItem iBuildItem;

    private IDataLoad iDataLoad;

    public ItemViewBuilder() {
    }

    /** build item **/

    public void setiBuildItem(IBuildItem iBuildItem) {
        this.iBuildItem = iBuildItem;
    }

    public IBuildItem getiBuildItem() {
        return iBuildItem;
    }

    /** data load **/

    public void setDataLoad(IDataLoad iDataLoad) {
        this.iDataLoad = iDataLoad;
    }

    public IDataLoad getDataLoad() {
        return this.iDataLoad;
    }
}
