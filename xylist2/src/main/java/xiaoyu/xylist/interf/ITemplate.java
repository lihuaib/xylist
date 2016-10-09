package xiaoyu.xylist.interf;

import android.view.View;

import xiaoyu.xylist.TemplateManger;
import xiaoyu.xylist.adapter.ItemViewBuilder;

/**
 * Created by lee on 16/10/8.
 */

public interface ITemplate {
    void setTemplateManager(TemplateManger manager, View contentView, ItemViewBuilder itemView);

    void refreshData();
}