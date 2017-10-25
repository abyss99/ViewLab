package widget.scale;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by abyss on 2017-08-11.
 */

public class ScaleView extends View {
    private int color;
    public ScaleView(Context context, int color) {
        super(context);
        this.color = color;
    }

    public ScaleView(Context context) {
        super(context);
    }

    public ScaleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScaleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getColor() {
        return color;
    }

    float dX, dY;

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//
//            case MotionEvent.ACTION_DOWN:
//
//                dX = getX() - event.getRawX();
//                dY = getY() - event.getRawY();
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//
//                animate()
//                        .x(event.getRawX() + dX)
//                        .y(event.getRawY() + dY)
//                        .setDuration(0)
//                        .start();
//                break;
//            default:
//                return false;
//        }
//        return true;
//    }

    float weightX = 0.0f;

    public void setDy2(float dy2) {
        this.dy2 = dy2;
    }

    private float dy2 = 0.0f;

    boolean release = true;
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//
//            case MotionEvent.ACTION_DOWN:
//                release = false;
//                Log.d("ScaleView", "getX : " + getX() + ", getY : " + getY() + ", event.getRawX()" + event.getRawX() + ", event.getRawY()" +event.getRawY());
//                dX = getX() - event.getRawX();
//                dY = getY() - event.getRawY();
////                Log.d("ScaleView", "dx :" + dX);
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//
////                animate()
////                        .x(event.getRawX() + dX)
////                        .y(event.getRawY() + dY)
////                        .setDuration(0)
////                        .start();
//                Log.d("ScaleView", "event.getRawX() + dX : " + (event.getRawX() + dX));
//                dy2 = event.getRawX() + dX;
//                invalidate();
//                break;
//            case MotionEvent.ACTION_UP:
//                release = true;
//                break;
//            default:
//                return false;
//        }
//        return true;
//    }


//    @Override
//    protected void onDraw(Canvas canvas) {
//        Log.d("ScaleView2", String.valueOf(dy2));
//        canvas.save();
//        canvas.drawColor(Color.TRANSPARENT);
//        Path path = new Path();
//        path.moveTo(0, 0);
//        path.lineTo(canvas.getWidth() + dy2, 0);
//        path.lineTo(canvas.getWidth() / 1.2f + dy2, canvas.getHeight());
//        path.lineTo(0, canvas.getHeight());
//        canvas.clipPath(path);
//        canvas.drawColor(color);
//        super.onDraw(canvas);
//        canvas.restore();
//    }



}
