package com.naver.viewlabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.naver.viewlabs.activities.CoordinatorActivity1;

/**
 * Created by abyss on 2017. 8. 16..
 */

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }

    public void onClickStartActivity(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.coordinator_sample1:
                intent = new Intent(this, CoordinatorActivity1.class);
                break;
            default:
                intent = null;
        }


        if (intent != null) {
            startActivity(intent);
        }
    }
}
