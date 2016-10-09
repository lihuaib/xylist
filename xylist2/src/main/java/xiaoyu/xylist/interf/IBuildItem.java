package xiaoyu.xylist.interf;

import android.view.View;

/**
 * Created by lee on 16/10/9.
 */
public interface IBuildItem {
    void set(View view, int position, Object value);

    View get(int viewType);

    int getItemType(int position);

    int getItemCount();
}