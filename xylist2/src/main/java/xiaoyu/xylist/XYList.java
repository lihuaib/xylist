package xiaoyu.xylist;

import xiaoyu.xylist.interf.ITemplate;

/**
 * Created by lee on 16/10/8.
 */
public class XYList {

    public static TemplateManger load(ITemplate template) {
        TemplateManger manger = new TemplateManger(template);
        return manger;
    }
}
