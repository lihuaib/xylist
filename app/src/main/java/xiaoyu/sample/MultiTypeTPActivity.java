package xiaoyu.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
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

public class MultiTypeTPActivity extends AppCompatActivity {

    TemplateManger manger;
    List<Integer> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_types);

        setOnClick();

        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        List<IViewBehavior> types = new ArrayList<>();
        types.add(new IViewBehavior() {
            TextView textView;

            @Override
            public List getData() {
                return null;
            }

            @Override
            public View getView() {
                textView = new TextView(MultiTypeTPActivity.this);

                textView.setBackgroundColor(Color.BLACK);

                RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(params);

                return textView;
            }

            @Override
            public void setValue(Object o) {
                if (textView == null) return;
                textView.setText("");
            }
        });

        types.add(new IViewBehavior() {
            TextView textView;

            @Override
            public List getData() {
                return null;
            }

            @Override
            public View getView() {
                textView = new TextView(MultiTypeTPActivity.this);

                textView.setBackgroundColor(Color.RED);

                RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(params);

                return textView;
            }

            @Override
            public void setValue(Object o) {
                if (textView == null) return;
                textView.setText("");
            }
        });

        types.add(new IViewBehavior<String>() {
            TextView textView;

            @Override
            public List getData() {
                return list;
            }

            @Override
            public View getView() {
                textView = new TextView(MultiTypeTPActivity.this);

                textView.setText("xxx");
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundResource(R.color.colorPrimaryDark);

                RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(params);

                return textView;
            }

            @Override
            public void setValue(String o) {
                if (textView == null) return;
                textView.setText(o);
            }
        });

        IDataLoad iDataLoad = new IDataLoad() {
            @Override
            public void refresh() {
                loadRefresh();
            }

            @Override
            public void loadMore() {
                MultiTypeTPActivity.this.loadMore();
            }
        };

        TextView emptyView = new TextView(this);
        emptyView.setText("没有数据");

        (manger = XYList.load(new BasicTP()))
                .setOptions(XYOptions.canPulltoRefresh | XYOptions.canLoadMore | XYOptions.isMultiType)
                .setDatas(list)
                .setDataLoad(iDataLoad)
                .setTypeList(types)
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
            loadEmpty();
        }
    };

    private void loadEmpty() {
        list.clear();

        manger.setDatas(list);
    }

    private void loadRefresh() {
        int start = 0;

        list.clear();
        for (int i = start; i < start + 10; i++) {
            list.add(i);
        }

        manger.setDatas(list);
    }

    private void loadMore() {
        findViewById(R.id.btn_load).postDelayed(new Runnable() {
            @Override
            public void run() {
                int start = list.size() == 0 ? 0 : list.get(list.size() - 1);

                for (int i = start; i < start + 10; i++) {
                    list.add(i);
                }

                manger.setDatas(list);
            }
        }, 1000);

    }
}
