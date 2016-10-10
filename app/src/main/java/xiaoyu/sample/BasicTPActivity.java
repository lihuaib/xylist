package xiaoyu.sample;

import android.content.Intent;
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
import xiaoyu.xylist.adapter.ItemViewBuilder;
import xiaoyu.xylist.interf.IBuildItem;
import xiaoyu.xylist.interf.IDataLoad;
import xiaoyu.xylist.templates.BasicTP;

public class BasicTPActivity extends AppCompatActivity {

    TemplateManger manger;
    List<Integer> list;
    ItemViewBuilder itemViewBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basictp);

        setOnClick();

        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        itemViewBuilder = new ItemViewBuilder();
        itemViewBuilder.setiBuildItem(new IBuildItem() {
            @Override
            public void set(View view, int position, Object object) {
                ((TextView) view).setText(object.toString());
            }

            @Override
            public View get(int viewType) {
                TextView textView = new TextView(BasicTPActivity.this);
                textView.setText("xxx");
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundResource(R.color.colorPrimaryDark);
                RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(params);

                return textView;
            }

            @Override
            public int getItemCount() {
                return list.size();
            }

            @Override
            public int getItemType(int position) {
                return 0;
            }
        });

        itemViewBuilder.setDataLoad(new IDataLoad() {
            @Override
            public void refresh() {
                loadRefresh();
            }

            @Override
            public void loadMore() {
                BasicTPActivity.this.loadMore();
            }
        });

        TextView emptyView = new TextView(this);
        emptyView.setText("没有数据");

        (manger = XYList.load(new BasicTP()))
                .setOptions(XYOptions.canPulltoRefresh | XYOptions.canLoadMore)
                .setDatas(list)
                .setEmptyView(emptyView)
                .into(findViewById(R.id.rc_list), itemViewBuilder);
    }

    private void setOnClick() {
        findViewById(R.id.btn_load).setOnClickListener(btnLoadListener);
        findViewById(R.id.btn_refresh).setOnClickListener(btnRefreshListener);
        findViewById(R.id.btn_empty).setOnClickListener(btnEmptyListener);

        findViewById(R.id.btn_redirect1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BasicTPActivity.this, MultiTypeTPActivity.class));
            }
        });

        findViewById(R.id.btn_redirect2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BasicTPActivity.this, MultiTypeFixedTPActivity.class));
            }
        });
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

        if (list.size() > 0) {
            int start = list.get(list.size() - 1);

            list.clear();
            for (int i = start; i < start + 10; i++) {
                list.add(i);
            }
        }

        manger.setDatas(list);
    }

    private void loadMore() {
        System.out.println("loadMore, size=" + list.size());

        int start = list.size() == 0 ? 0 : list.get(list.size() - 1);

        for (int i = start; i < start + 10; i++) {
            list.add(i);
        }

        findViewById(R.id.btn_load).postDelayed(new Runnable() {
            @Override
            public void run() {
                manger.setDatas(list);
            }
        }, 10000);
    }
}
