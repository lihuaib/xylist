package xiaoyu.xylist.interf;

import android.view.View;

import java.util.List;

/**
 * Created by lee on 16/10/8.
 */
public interface IViewBehavior<T> {
    boolean hasData();

    View getView();
    void setValue(View view, T t);
}