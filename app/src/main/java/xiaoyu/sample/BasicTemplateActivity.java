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
import xiaoyu.xylist.templates.BasicTemplate;

public class BasicTemplateActivity extends AppCompatActivity {

    TemplateManger manger;
    List<Integer> list;
    ItemViewBuilder itemViewBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

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
                TextView textView = new TextView(BasicTemplateActivity.this);
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
                BasicTemplateActivity.this.loadMore();
            }
        });

        TextView emptyView = new TextView(this);
        emptyView.setText("没有数据");

        (manger = XYList.load(new BasicTemplate()))
                .setOptions(XYOptions.canLoadMore)
                .setDatas(list)
                .setEmptyView(emptyView)
                .into(findViewById(R.id.rc_list), itemViewBuilder);
    }

    private void setOnClick() {
        findViewById(R.id.btn_load).setOnClickListener(btnLoadListener);
        findViewById(R.id.btn_refresh).setOnClickListener(btnRefreshListener);

        findViewById(R.id.btn_redirect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BasicTemplateActivity.this, BasicTemplateMultiDataActivity.class));
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

        try {

            Thread.sleep(1000);
        } catch (Exception ex) {

        }

        manger.setDatas(list);
    }
}
