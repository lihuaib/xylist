package xiaoyu.xylist.footers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import xiaoyu.xylist.R;

/**
 * Created by lee on 16/10/9.
 */

public class XYFooterView extends RelativeLayout {

    public static final int STATUS_LOAD_MORE = 1;
    public static final int STATUS_LOADING = 2;
    public static final int STATUS_NO_MORE_LOAD = 3;

    private TextView tvLoadMore;
    private ProgressBar progressBar;
    private int currStatus = STATUS_LOAD_MORE;

    public static XYFooterView get(Context context) {
        return (XYFooterView) inflate(context, R.layout.xylist_xyfooter, null);
    }

    public XYFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        setUpViews();
    }

    private void setUpViews() {
        tvLoadMore = (TextView) findViewById(R.id.tv_loadmore);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    public int getStatus() {
        return currStatus;
    }

    public void setStatus(int status) {
        currStatus = status;
        switch (status) {
            case STATUS_LOAD_MORE:
                progressBar.setVisibility(View.GONE);
                tvLoadMore.setVisibility(View.VISIBLE);
                tvLoadMore.setText(R.string.xylist_s1);
                break;
            case STATUS_LOADING:
                progressBar.setVisibility(View.VISIBLE);
                tvLoadMore.setVisibility(View.VISIBLE);
                tvLoadMore.setText(R.string.xylist_s2);
                break;
            case STATUS_NO_MORE_LOAD:
                progressBar.setVisibility(View.GONE);
                tvLoadMore.setVisibility(View.VISIBLE);
                tvLoadMore.setText(R.string.xylist_s3);
                break;
        }
    }
}
