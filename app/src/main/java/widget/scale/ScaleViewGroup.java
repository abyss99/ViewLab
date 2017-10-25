package widget.scale;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by abyss on 2017. 8. 14..
 */

public class ScaleViewGroup extends FrameLayout {
    public ScaleViewGroup(@NonNull Context context) {
        super(context);

    }

    public ScaleViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ScaleView scaleView = new ScaleView(getContext(), Color.RED);
        scaleView.setTag("scalefront");
        addView(scaleView, 0);
    }

    public ScaleViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        int count = getChildCount();
        View child;
        for (int i = 0; i < count; i++) {
            child = getChildAt(i);
            if(!(child instanceof ScaleView) || child.getTag() == null) {
                continue;
            }
            ScaleView scaleView = (ScaleView) child;
            if (scaleView.getTag().equals("scalefront")) {
                canvas.save();
                canvas.drawColor(Color.TRANSPARENT);
                Path path = new Path();
                path.moveTo(0, 0);
                path.lineTo(canvas.getWidth() + dy2 + 30, 0);
                path.lineTo(canvas.getWidth() + dy2, canvas.getHeight());
                path.lineTo(0, canvas.getHeight());
                canvas.clipPath(path);
                canvas.drawColor(scaleView.getColor());
                scaleView.draw(canvas);
                canvas.restore();

            } else if(scaleView.getTag().equals("scaleback")) {
                canvas.save();
                canvas.drawColor(scaleView.getColor());
                scaleView.draw(canvas);
                canvas.restore();
            }
        }

    }

    boolean release = true;
    float dX, dY;
    float dy2 = 0.0f;

    int lastcolor = Color.BLUE;

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                release = false;


                ScaleView scaleView = new ScaleView(getContext(), lastcolor);
                scaleView.setTag("scaleback");
                addView(scaleView, 0);
                lastcolor = lastcolor == Color.BLUE ? Color.RED : Color.BLUE;
                Log.d("ScaleView", "getX : " + getX() + ", getY : " + getY() + ", event.getRawX()" + event.getRawX() + ", event.getRawY()" + event.getRawY());
                dX = getX() - event.getRawX();
                dY = getY() - event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                Log.d("ScaleView", "event.getRawX() + dX : " + (event.getRawX() + dX));
                dy2 = event.getRawX() + dX;
                invalidateScaleView(dy2);
                break;
            case MotionEvent.ACTION_UP:
                release = true;
//                removeViewAt(getChildCount());

                for (int i = 0; i < getChildCount(); i++) {
                    View child2 = getChildAt(i);
                    if (child2.getTag() != null && child2.getTag().equals("scalefront")) {
                        removeViewAt(i);
                    }
                }

                View child;
                for (int i = 0; i < getChildCount(); i++) {
                    child = getChildAt(i);
                    if (child.getTag() != null && child.getTag().equals("scaleback")) {
                        child.setTag("scalefront");
                        break;
                    }
                }

                dy2 = 0;
                performClick();
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private void invalidateScaleView(float dy2) {
//        int count = getChildCount();
//        View child;
//        for (int i = 0; i < count; i++) {
//            child = getChildAt(i);
//            if (child instanceof ScaleView && child.getTag() != null && child.getTag().equals("scalefront")) {
//                Log.d("ScaleView", child.getTag().toString());
//                ScaleView scaleView = (ScaleView) child;
//                scaleView.setDy2(dy2);
//                scaleView.invalidate();
//                break;
//            }
//        }
        invalidate();
    }
}
