package widget.snap;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by abyss on 2017. 8. 30..
 */

public class LinearGravityStartSnapHelper extends LinearSnapHelper {
    private GravityDelegate delegate;

    public LinearGravityStartSnapHelper() {
        delegate = new GravityDelegate();
    }

    @Override
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView)
            throws IllegalStateException {
        delegate.attachToRecyclerView(recyclerView);
        super.attachToRecyclerView(recyclerView);
    }

    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager,
                                              @NonNull View targetView) {
        return delegate.calculateDistanceToFinalSnap(layoutManager, targetView);
    }

    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        return delegate.findSnapView(layoutManager);
    }

//    @Override
//    public boolean onFling(int velocityX, int velocityY) {
//        return super.onFling((int) (velocityX * 0.02f), velocityY);
//    }
//
//    @Override
//    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
//        return super.findTargetSnapPosition(layoutManager, (int) (velocityX * 10), velocityY);
//    }

    public interface SnapListener {
        void onSnap(int position);
    }
}
