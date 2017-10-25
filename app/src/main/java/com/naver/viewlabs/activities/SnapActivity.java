package com.naver.viewlabs.activities;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.naver.viewlabs.R;

import widget.CustomRecyclerView;
import widget.snap.LinearGravityStartSnapHelper;

import static android.widget.LinearLayout.HORIZONTAL;

public class SnapActivity extends AppCompatActivity {
    private CustomRecyclerView mRecyclerView;
    private static final int ITEM_SIZE = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap);

        mRecyclerView = (CustomRecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new CenterZoomLayoutManager(this, HORIZONTAL, false));
        mRecyclerView.setHasFixedSize(true);

        SnapAdapter snapAdapter = new SnapAdapter();
        mRecyclerView.setAdapter(snapAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                Log.d("scroll", "findFirstVisibleItemPosition:" + String.valueOf(((CenterZoomLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition()));
            }
        });
        LinearGravityStartSnapHelper linearSnapHelper = new LinearGravityStartSnapHelper();
        linearSnapHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.getLayoutManager().scrollToPosition(snapAdapter.MIDDLE);
    }

    class SnapAdapter extends RecyclerView.Adapter<SnapViewHolder> {
        public static final int HALF_MAX_VALUE = Integer.MAX_VALUE / 2;
        public final int MIDDLE;

        public SnapAdapter() {
            this.MIDDLE = HALF_MAX_VALUE - HALF_MAX_VALUE;
        }

        @Override
        public SnapViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.snap_item, parent, false);
            return new SnapViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SnapViewHolder holder, int position) {
            holder.snapTextView.setText(String.valueOf(position));
        }

        @Override
        public int getItemCount() {
            return ITEM_SIZE;
        }

    }

    class SnapViewHolder extends RecyclerView.ViewHolder {

        public TextView snapTextView;

        public SnapViewHolder(final View itemView) {
            super(itemView);
            snapTextView = (TextView) itemView.findViewById(R.id.snap_text);
        }
    }

    class CenterZoomLayoutManager extends LinearLayoutManager {
        private final float mShrinkAmount = 0.35f;
//        private final float mShrinkDistance = 0.9f;

        public CenterZoomLayoutManager(Context context) {
            super(context);
        }

        public CenterZoomLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        @Override
        public void onLayoutCompleted(RecyclerView.State state) {
            super.onLayoutCompleted(state);
            Log.d("scroll", "onLayoutCompleted");

            int widthThreshold = getChildAt(0).getWidth();
            float midpoint = widthThreshold / 2.f;
            float d1 = midpoint;
            float s0 = 1.f;
            float s1 = 1.f - mShrinkAmount;

            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                float childMidpoint =
                        (getDecoratedRight(child) + getDecoratedLeft(child)) / 2.f;
                float d = Math.min(d1, Math.abs(midpoint - childMidpoint));
                float scale = 1.f + (s1 - s0) * d / d1;
                Log.d("scroll", "no:" + String.valueOf(i) + " childMidpoint:" + String.valueOf(childMidpoint) + " d:" + String.valueOf(d) + " scale:" + String.valueOf(scale));

                if (Math.abs(midpoint - childMidpoint) < widthThreshold) {
                    child.setScaleX(scale);
                    child.setScaleY(scale);
                } else {
                    child.setScaleX(0.65f);
                    child.setScaleY(0.65f);
                }

            }
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            super.onLayoutChildren(recycler, state);
            Log.d("scroll", "onLayoutChildren");
        }

        @Override
        public int getChildCount() {
            return super.getChildCount();
        }

        @Override
        public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
            int orientation = getOrientation();
            if (orientation == HORIZONTAL) {
                int widthThreshold = getChildAt(0).getWidth();
                int scrolled = super.scrollHorizontallyBy(dx, recycler, state);

                float midpoint = widthThreshold / 2.f;
                float d0 = 0.f;
//                float d1 = mShrinkDistance * midpoint;
                float d1 = midpoint;
                float s0 = 1.f;
                float s1 = 1.f - mShrinkAmount;


                Log.d("scroll", "widthThreshold:" + String.valueOf(widthThreshold));
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    float childMidpoint =
                            (getDecoratedRight(child) + getDecoratedLeft(child)) / 2.f;
                    float d = Math.min(d1, Math.abs(midpoint - childMidpoint));
                    float scale = 1.f + (s1 - s0) * d / d1;
//                    float scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0);
                    Log.d("scroll", "no:" + String.valueOf(i) + " childMidpoint:" + String.valueOf(childMidpoint) + " d:" + String.valueOf(d) + " scale:" + String.valueOf(scale));

                    if (Math.abs(midpoint - childMidpoint) < widthThreshold) {
                        child.setScaleX(scale);
                        child.setScaleY(scale);
                    } else {
                        child.setScaleX(0.65f);
                        child.setScaleY(0.65f);
                    }

                }
                return scrolled;
            } else {
                return 0;
            }

        }

    }

}
