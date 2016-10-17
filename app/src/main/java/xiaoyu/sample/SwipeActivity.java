package xiaoyu.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import xiaoyu.xylist.TemplateManger;
import xiaoyu.xylist.XYList;
import xiaoyu.xylist.XYOptions;
import xiaoyu.xylist.interf.IDataLoad;
import xiaoyu.xylist.interf.IViewBehavior;
import xiaoyu.xylist.templates.swipe.SwipeItem;
import xiaoyu.xylist.templates.swipe.SwipeTP;

public class SwipeActivity extends AppCompatActivity {

    TemplateManger manger;
    List<Integer> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        setOnClick();

        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        final SwipeTP template = new SwipeTP();
        List<IViewBehavior> iViewBehaviors = new ArrayList<>();
        iViewBehaviors.add(new IViewBehavior() {
            @Override
            public boolean hasData() {
                return true;
            }

            @Override
            public View getView() {
                SwipeItem item = new SwipeItem(SwipeActivity.this, template);

                TextView textView = new TextView(SwipeActivity.this);
                textView.setText("xxx");
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundResource(R.color.colorPrimaryDark);

                TextView textView2 = new TextView(SwipeActivity.this);
                textView2.setText("kkk");
                textView2.setTextColor(Color.BLACK);
                textView2.setBackgroundColor(Color.WHITE);

                item.setViews(textView, textView2);
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("lee", "swipe item click");
                    }
                });

                RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                item.setLayoutParams(params);

                return item;
            }

            @Override
            public void setValue(View item, Object o) {
                TextView tv = (TextView) ((SwipeItem) item).getContentView();
                tv.setText(o.toString());

                TextView tv2 = (TextView) ((SwipeItem) item).getMenus();
                tv2.setText("menu:" + o.toString());
            }
        });

        IDataLoad iDataLoad = new IDataLoad() {
            @Override
            public void refresh() {
                loadRefresh();
            }

            @Override
            public void loadMore() {
                SwipeActivity.this.loadMore();
            }
        };

        TextView emptyView = new TextView(this);
        emptyView.setText("没有数据");

        (manger = XYList.load(template))
                .setOptions(XYOptions.canPulltoRefresh | XYOptions.canLoadMore)
                .setDatas(list)
                .setTypeList(iViewBehaviors)
                .setDataLoad(iDataLoad)
                .setEmptyView(emptyView)
                .setDivider(5)
                .into(findViewById(R.id.rc_list));
    }

    private void setOnClick() {
        findViewById(R.id.btn_load).setOnClickListener(btnLoadListener);
        findViewById(R.id.btn_refresh).setOnClickListener(btnRefreshListener);
        findViewById(R.id.btn_empty).setOnClickListener(btnEmptyListener);
    }

    View.OnClickListener btnLoadListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loadMore();
        }
    };

    View.OnClickListener btnRefreshListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loadRefresh();
        }
    };

    View.OnClickListener btnEmptyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            list.clear();
            manger.setDatas(list);
        }
    };

    private void loadRefresh() {
        System.out.println("loadRefresh");

        int start = 0;

        list.clear();
        for (int i = start; i < start + 10; i++) {
            list.add(i);
        }

        manger.setDatas(list);
    }

    private void loadMore() {
        System.out.println("loadMore, size=" + list.size());

        findViewById(R.id.btn_load).postDelayed(new Runnable() {
            @Override
            public void run() {
                int start = list.size() == 0 ? 0 : list.get(list.size() - 1);

                if (start <= 40) {
                    for (int i = start; i < start + 10; i++) {
                        list.add(i);
                    }
                }

                manger.setDatas(list);
            }
        }, 3000);
    }
}
