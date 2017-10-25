package com.naver.viewlabs.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.naver.viewlabs.R;

import widget.CustomLayoutManager;
import widget.CustomRecyclerView;
import widget.snap.LinearGravityStartSnapHelper;
import widget.snap.LinearGravityStartSnapHelper2;
import widget.snap.LinearGravityStartSnapHelper3;

import static android.widget.LinearLayout.HORIZONTAL;

/**
 * Created by abyss on 2017. 10. 17..
 */

public class SnapActivity2 extends AppCompatActivity {
    private CustomRecyclerView mRecyclerView;
    private RecyclerView mRecyclerView2;
    private static final int ITEM_SIZE = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap_2);

        mRecyclerView2 = (RecyclerView) findViewById(R.id.recycler_view2);
        mRecyclerView2.setLayoutManager(new CustomLayoutManager(this, HORIZONTAL, false, mRecyclerView2));
        mRecyclerView2.setHasFixedSize(true);
        mRecyclerView2.setAdapter(new SomeAdapter());
        LinearGravityStartSnapHelper3 linearSnapHelper2 = new LinearGravityStartSnapHelper3();
        linearSnapHelper2.attachToRecyclerView(mRecyclerView2);


        mRecyclerView = (CustomRecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new CustomLayoutManager(this, HORIZONTAL, false, mRecyclerView));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(new SomeAdapter());
        LinearGravityStartSnapHelper2 linearSnapHelper = new LinearGravityStartSnapHelper2();
        linearSnapHelper.attachToRecyclerView(mRecyclerView);




    }

    class SomeAdapter extends RecyclerView.Adapter<SnapViewHolder2> {
        public static final int HALF_MAX_VALUE = Integer.MAX_VALUE / 2;
        public final int MIDDLE;
        int drawableList[] = {R.mipmap.th_orange_01, R.mipmap.th_orange_02, R.mipmap.th_orange_03, R.mipmap.th_orange_04, R.mipmap.th_orange_05};

        public SomeAdapter() {
            this.MIDDLE = HALF_MAX_VALUE - HALF_MAX_VALUE;
        }

        @Override
        public SnapViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.snap_item, parent, false);
            return new SnapViewHolder2(view);
        }

        @Override
        public void onBindViewHolder(SnapViewHolder2 holder, int position) {
            holder.snapImageView.setImageResource(drawableList[position % 5]);
        }

        @Override
        public int getItemCount() {
            return ITEM_SIZE;
        }

    }

    class SnapViewHolder2 extends RecyclerView.ViewHolder {

        public ImageView snapImageView;

        public SnapViewHolder2(final View itemView) {
            super(itemView);
            snapImageView = (ImageView) itemView.findViewById(R.id.snap_text);
        }
    }
}
