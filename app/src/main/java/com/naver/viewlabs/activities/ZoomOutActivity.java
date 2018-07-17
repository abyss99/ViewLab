package com.naver.viewlabs.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Rational;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.naver.viewlabs.R;
import com.naver.viewlabs.log.Ln;

import java.util.ArrayList;
import java.util.List;

public class ZoomOutActivity extends AppCompatActivity {
    private FragmentManager mFragmentManager;
    private FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);
        mFragmentManager = getSupportFragmentManager();
        container = findViewById(R.id.container);
        addScrollEvent();
        addFragment();

    }

    private void addFragment() {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        String tag = "main";
        Fragment selectedFragment = mFragmentManager.findFragmentByTag(tag);

        if (selectedFragment == null) {
            selectedFragment = new ZoomOutFragment();
            fragmentTransaction.add(R.id.container, selectedFragment, tag);
        } else {
            fragmentTransaction.attach(selectedFragment);
        }
        fragmentTransaction.commit();
    }

    private void addScrollEvent() {
        container.setOnTouchListener(new View.OnTouchListener() {
            int downY;
            int maxDistance = 500;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downY = (int) event.getRawY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        int distanceY = Math.min(maxDistance, Math.max(0, (int) event.getRawY() - downY));
                        float ratio = Math.min((float) distanceY / maxDistance, 0.7f);
                        float scaleFactor = 1.0f - ratio;
                        container.setPivotX(container.getMeasuredWidth());
                        container.setPivotY(container.getMeasuredHeight());
                        container.setScaleX(scaleFactor);
                        container.setScaleY(scaleFactor);
                        container.setAlpha(scaleFactor);
                        return true;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                    default:
                        break;
                }

                return false;
            }
        });
    }

}
