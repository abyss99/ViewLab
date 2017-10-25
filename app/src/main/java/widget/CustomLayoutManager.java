package widget;

import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;

/**
 * Created by abyss on 2017. 10. 17..
 */

public class CustomLayoutManager extends LinearLayoutManager {
    Context context;
    RecyclerView recyclerView;
    int newState;

    public CustomLayoutManager(Context context) {
        super(context);
        this.context = context;
    }

    public CustomLayoutManager(Context context, int orientation, boolean reverseLayout, RecyclerView recyclerView) {
        super(context, orientation, reverseLayout);
        this.context = context;
        this.recyclerView = recyclerView;
    }


    public CustomLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        adjustSize();
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        adjustSize();
        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    private void adjustSize() {
        int widthThreshold = getChildAt(0).getWidth();
        float midpoint = widthThreshold / 2.f;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            float childMidpoint = (getDecoratedRight(child) + getDecoratedLeft(child)) / 2.f;
            float distance = Math.min(midpoint, Math.abs(midpoint - childMidpoint));
            float scale = 1.f - 0.2f * distance / midpoint;
            Log.d("scroll", "no:" + String.valueOf(i) + " childMidpoint:" + String.valueOf(childMidpoint) + " d:" + String.valueOf(distance) + " scale:" + String.valueOf(scale) + " mid point:" + String.valueOf(midpoint));

            child.setScaleX(scale);
            child.setScaleY(scale);

        }
    }
}
