package widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

/**
 * Created by abyss on 2017. 10. 17..
 */

public class CustomRecyclerView extends RecyclerView {
    private static final int POW = 30;
    private Interpolator interpolator;

    public CustomRecyclerView(Context context) {
        super(context);
        createInterpolator();
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        createInterpolator();
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        createInterpolator();
    }

    private void createInterpolator(){
        interpolator = new Interpolator() {
            @Override
            public float getInterpolation(float t) {
                t = Math.abs(t - 1.0f);
                return (float) (1.0f - Math.pow(t, POW));
            }
        };
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {

//        velocityX *= 0.2;
        // velocityX *= 0.7; for Horizontal recycler view. comment velocityY line not require for Horizontal Mode.

        return super.fling(velocityX, velocityY);
    }

    @Override
    public void smoothScrollBy(int dx, int dy) {
        super.smoothScrollBy(dx, dy, interpolator);
    }
}
