package com.naver.viewlabs.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.naver.viewlabs.R;

/**
 * Created by abyss on 2017. 8. 22..
 */

public class TabActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_layout);

        final HorizontalScrollView horizontalScrollView = (HorizontalScrollView) findViewById(R.id.scroll);
        final LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        for (int i = 0; i < 15; i++) {
            final TextView textView = new TextView(this);

            textView.setTag(i);
            textView.setText(String.valueOf(i));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(150, 100);
            textView.setLayoutParams(layoutParams);

            linearLayout.addView(textView);

            textView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    int clickPosition = (int) v.getTag();
                    int scrollXtoMove = 0;
                    for (int i = 0; i < linearLayout.getChildCount(); i++) {
                        TextView view = (TextView) linearLayout.getChildAt(i);
                        if (clickPosition > i + 1) {
                            scrollXtoMove += view.getWidth();
                        }
                        view.setTextColor(Color.WHITE);

                    }

                    horizontalScrollView.smoothScrollTo(scrollXtoMove, 0);
                    textView.setTextColor(Color.RED);
                    Log.e("Tag", "" + textView.getTag());
                }
            });
        }

        horizontalScrollView.addView(linearLayout);
    }
}
