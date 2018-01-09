package com.naver.viewlabs.activities;

import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.naver.viewlabs.R;

/**
 * Created by abyss on 2017. 12. 4..
 */

public class NestedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested);
        final NestedScrollView nestedScrollView = findViewById(R.id.nested_view);
        findViewById(R.id.scroll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nestedScrollView.smoothScrollBy(0, 100);
            }
        });
    }
}
