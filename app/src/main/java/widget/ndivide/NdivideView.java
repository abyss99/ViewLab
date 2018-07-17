package widget.ndivide;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.naver.viewlabs.log.Ln;

/**
 * Created by abyss on 2018. 3. 15..
 */

public class NdivideView extends FrameLayout {
    public NdivideView(@NonNull Context context) {
        super(context);
    }

    public NdivideView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NdivideView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int childCount = getChildCount();
        int columnWidth = width / childCount;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.measure(MeasureSpec.makeMeasureSpec(columnWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childCount = getChildCount();
        int childLeft = 0;
        int childTop = 0;
        int bottomMax = 0;

        View child;
        for (int i = 0; i < childCount; i++) {
            child = getChildAt(i);
            child.layout(childLeft, childTop, childLeft + child.getMeasuredWidth(), childTop + child.getMeasuredHeight());
            bottomMax = Math.max(child.getBottom(), bottomMax);
            childLeft = childLeft + child.getMeasuredWidth();
        }
    }
}
