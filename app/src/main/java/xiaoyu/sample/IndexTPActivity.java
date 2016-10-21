package xiaoyu.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import xiaoyu.xylist.templates.ViewPagerTP.ViewPagerTP;

public class IndexTPActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        findViewById(R.id.btn_redirect0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IndexTPActivity.this, BasicTPActivity.class));
            }
        });

        findViewById(R.id.btn_redirect1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IndexTPActivity.this, MultiTypeTPActivity.class));
            }
        });

        findViewById(R.id.btn_redirect2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IndexTPActivity.this, MultiTypeFixedTPActivity.class));
            }
        });

        findViewById(R.id.btn_redirect3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IndexTPActivity.this, SwipeActivity.class));
            }
        });

        findViewById(R.id.btn_redirect4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IndexTPActivity.this, HorizonPageActivity.class));
            }
        });

        findViewById(R.id.btn_redirect5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IndexTPActivity.this, ViewPagerTPActivity.class));
            }
        });
    }
}
