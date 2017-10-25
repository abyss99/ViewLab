package widget.snap;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by abyss on 2017. 8. 30..
 */

public class LinearGravityStartSnapHelper3 extends LinearSnapHelper {
    private OrientationHelper mHorizontalHelper;

    public LinearGravityStartSnapHelper3() {

    }

    @Override
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView)
            throws IllegalStateException {
        super.attachToRecyclerView(recyclerView);
    }

    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager,
                                              @NonNull View targetView) {
        int[] out = new int[2];

        out[0] = distanceToStart(targetView, getHorizontalHelper(layoutManager));
        out[1] = 0;
        return out;
    }

    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        return getStartView(layoutManager, getHorizontalHelper(layoutManager));
    }

    private int distanceToStart(View targetView, OrientationHelper helper) {
        return helper.getDecoratedStart(targetView) - helper.getStartAfterPadding();
    }

    private View getStartView(RecyclerView.LayoutManager layoutManager,
                              OrientationHelper helper) {

        int firstChild = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();

        boolean isLastItem = ((LinearLayoutManager) layoutManager)
                .findLastCompletelyVisibleItemPosition()
                == layoutManager.getItemCount() - 1;

        if (firstChild == RecyclerView.NO_POSITION || isLastItem) {
            return null;
        }

        View child = layoutManager.findViewByPosition(firstChild);

        if (helper.getDecoratedEnd(child) >= helper.getDecoratedMeasurement(child) / 2
                && helper.getDecoratedEnd(child) > 0) {
            return child;
        } else {
            if (((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition()
                    == layoutManager.getItemCount() - 1) {
                return null;
            } else {
                return layoutManager.findViewByPosition(firstChild + 1);
            }
        }
    }

    private OrientationHelper getHorizontalHelper(RecyclerView.LayoutManager layoutManager) {
        if (mHorizontalHelper == null) {
            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return mHorizontalHelper;
    }
}