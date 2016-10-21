package xiaoyu.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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
import xiaoyu.xylist.templates.BasicTP;
import xiaoyu.xylist.templates.ViewPagerTP.RecyclerViewPager;
import xiaoyu.xylist.templates.ViewPagerTP.ViewPagerTP;

public class ViewPagerTPActivity extends AppCompatActivity {

    TemplateManger manger;
    List<Integer> list;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basictp);

        setOnClick();

        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        recyclerView = (RecyclerView) findViewById(R.id.rc_list);

        List<IViewBehavior> iViewBehaviors = new ArrayList<>();
        iViewBehaviors.add(new IViewBehavior<Integer>() {
            @Override
            public boolean hasData() {
                return true;
            }

            @Override
            public View getView() {
                TextView textView = new TextView(ViewPagerTPActivity.this);
                textView.setText("xxx");
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundResource(R.color.colorPrimaryDark);
                RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(650, ViewGroup.LayoutParams.MATCH_PARENT);
                textView.setLayoutParams(params);

                return textView;
            }

            @Override
            public void setValue(View v, Integer integer) {
                TextView tv = (TextView) v;
                tv.setText(integer + "");

                if (integer % 2 == 0) {
                    tv.setBackgroundColor(Color.RED);
                } else {
                    tv.setBackgroundColor(Color.BLACK);
                }
            }
        });

        IDataLoad iDataLoad = new IDataLoad() {
            @Override
            public void refresh() {
                loadRefresh();
            }

            @Override
            public void loadMore() {
                ViewPagerTPActivity.this.loadMore();
            }
        };

        TextView emptyView = new TextView(this);
        emptyView.setText("没有数据");

        (manger = XYList.load(new ViewPagerTP()))
                .setDatas(list)
                .setTypeList(iViewBehaviors)
                .setDataLoad(iDataLoad)
                .setEmptyView(emptyView)
                .setDivider(5)
                .into(recyclerView);
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
