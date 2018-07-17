package com.naver.viewlabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import widget.ndivide.NdivideView;

/**
 * Created by abyss on 2017. 8. 16..
 */

public class MainActivity extends AppCompatActivity {
    NdivideView ndivideView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ndivideView = findViewById(R.id.ndivide);
        addTextView();
    }

    private void addTextView() {
        for (int i = 0; i < 10; i++) {
            TextView item = (TextView) getLayoutInflater().inflate(R.layout.ndivide_item, null);
            item.setText("NDIVIDE : " + i);
            ndivideView.addView(item);
        }
    }


}
