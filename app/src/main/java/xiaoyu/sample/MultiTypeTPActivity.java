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
import xiaoyu.xylist.adapter.ItemViewBuilder;
import xiaoyu.xylist.interf.IBuildItem;
import xiaoyu.xylist.interf.IDataLoad;
import xiaoyu.xylist.templates.BasicTP;

public class MultiTypeTPActivity extends AppCompatActivity {

    TemplateManger manger;
    List<Integer> list;
    ItemViewBuilder itemViewBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_types);

        setOnClick();

        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        final List<Integer> types = new ArrayList<>();
        types.add(0); // black textview
        types.add(1); // red textview
        types.add(2); // normal textview

        itemViewBuilder = new ItemViewBuilder();
        itemViewBuilder.setiBuildItem(new IBuildItem() {
            @Override
            public void set(View view, int position, Object value) {
                int type = getItemType(position);
                if(type == 2) {
                    ((TextView) view).setText(value.toString());
                } else {
                    ((TextView) view).setText("");
                }
            }

            @Override
            public View get(int viewType) {
                TextView textView = new TextView(MultiTypeTPActivity.this);

                if(viewType == 0) {
                    textView.setBackgroundColor(Color.BLACK);
                } else if(viewType == 1) {
                    textView.setBackgroundColor(Color.RED);
                } else {
                    textView.setText("xxx");
                    textView.setTextColor(Color.WHITE);
                    textView.setBackgroundResource(R.color.colorPrimaryDark);
                }

                RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                textView.setLayoutParams(params);

                return textView;
            }

            @Override
            public int getItemCount() {
                return types.size() + list.size() - 1;
            }

            @Override
            public int getItemType(int position) {
                if(position == 0)
                    return 0;
                if(position == 1)
                    return 1;
                return 2;
            }
        });

        itemViewBuilder.setDataLoad(new IDataLoad() {
            @Override
            public void refresh() {
                loadRefresh();
            }

            @Override
            public void loadMore() {
                MultiTypeTPActivity.this.loadMore();
            }
        });

        TextView emptyView = new TextView(this);
        emptyView.setText("没有数据");

        (manger = XYList.load(new BasicTP()))
                .setOptions(XYOptions.canPulltoRefresh | XYOptions.canLoadMore | XYOptions.isMultiType)
                .setDatas(list)
                .setEmptyView(emptyView)
                .into(findViewById(R.id.rc_list), itemViewBuilder);
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
