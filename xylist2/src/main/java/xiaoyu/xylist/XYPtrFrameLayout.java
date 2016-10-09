package xiaoyu.xylist;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by lee on 16/10/9.
 */

public class XYPtrFrameLayout extends PtrFrameLayout {
    public XYPtrFrameLayout(Context context) {
        super(context);
    }

    public XYPtrFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XYPtrFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setContentView(View view) {
        addView(view);
        this.mContent = view;
    }
}
