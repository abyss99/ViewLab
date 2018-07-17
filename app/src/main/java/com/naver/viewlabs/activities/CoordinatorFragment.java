package com.naver.viewlabs.activities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.naver.viewlabs.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by abyss on 2018. 4. 13..
 */

public class CoordinatorFragment extends Fragment {

    public final static String ITEMS_COUNT_KEY = "PartThreeFragment$ItemsCount";

    public static CoordinatorFragment createInstance(int itemsCount) {
        CoordinatorFragment coordinatorFragment = new CoordinatorFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ITEMS_COUNT_KEY, itemsCount);
        coordinatorFragment.setArguments(bundle);
        return coordinatorFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.fragment_coordinator1, container, false);
        setupRecyclerView(recyclerView);
        return recyclerView;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(createItemList());
        recyclerView.setAdapter(recyclerAdapter);
        StickyHeaderDecoration decoration = new StickyHeaderDecoration(recyclerAdapter);
        recyclerView.addItemDecoration(decoration);
    }

    private List<String> createItemList() {
        List<String> itemList = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            int itemsCount = bundle.getInt(ITEMS_COUNT_KEY);
            for (int i = 0; i < itemsCount; i++) {
                itemList.add("Item " + i);
            }
        }
        return itemList;
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<String> mItemList;

        public RecyclerAdapter(List<String> itemList) {
            mItemList = itemList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
            return RecyclerItemViewHolder.newInstance(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            RecyclerItemViewHolder holder = (RecyclerItemViewHolder) viewHolder;
            String itemText = mItemList.get(position);
            holder.setItemText(itemText);
        }

        @Override
        public int getItemCount() {
            return mItemList == null ? 0 : mItemList.size();
        }

        public long getHeaderId(int position) {
            if (position < 7) {
                return StickyHeaderDecoration.NO_HEADER_ID;
            }

            return 1;
        }

        public HeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
            final View view = getLayoutInflater().inflate(R.layout.recycler_item_header, parent, false);
            return new HeaderHolder(view);
        }

        public void onBindHeaderViewHolder(HeaderHolder viewholder, int position) {
//            viewholder.header.setText("Discover Specials");
        }

        class HeaderHolder extends RecyclerView.ViewHolder {
            public CardView header;

            public HeaderHolder(View itemView) {
                super(itemView);
                header = (CardView) itemView;
            }
        }

    }

    public static class RecyclerItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView mItemTextView;

        public RecyclerItemViewHolder(final View parent, TextView itemTextView) {
            super(parent);
            mItemTextView = itemTextView;
        }

        public static RecyclerItemViewHolder newInstance(View parent) {
            TextView itemTextView = (TextView) parent.findViewById(R.id.itemTextView);
            return new RecyclerItemViewHolder(parent, itemTextView);
        }

        public void setItemText(CharSequence text) {
            mItemTextView.setText(text);
        }


    }


    static class StickyHeaderDecoration extends RecyclerView.ItemDecoration {
        public static final long NO_HEADER_ID = -1L;

        private Map<Long, RecyclerView.ViewHolder> headerCache;
        private RecyclerAdapter adapter;
        private boolean renderInline;

        /**
         * @param adapter the sticky header adapter to use
         */
        public StickyHeaderDecoration(RecyclerAdapter adapter) {
            this(adapter, false);
        }

        /**
         * @param adapter the sticky header adapter to use
         */
        public StickyHeaderDecoration(RecyclerAdapter adapter, boolean renderInline) {
            this.adapter = adapter;
            this.headerCache = new HashMap<>();
            this.renderInline = renderInline;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int headerHeight = 0;

            if (position != RecyclerView.NO_POSITION
                    && hasHeader(position)
                    && showHeaderAboveItem(position)) {

                View header = getHeader(parent, position).itemView;
                headerHeight = getHeaderHeightForLayout(header);
            }

            outRect.set(0, headerHeight, 0, 0);
        }

        private boolean showHeaderAboveItem(int itemAdapterPosition) {
            if (itemAdapterPosition == 0) {
                return true;
            }
            return adapter.getHeaderId(itemAdapterPosition - 1) != adapter.getHeaderId(itemAdapterPosition);
        }

        /**
         * Clears the header view cache. Headers will be recreated and
         * rebound on list scroll after this method has been called.
         */
        public void clearHeaderCache() {
            headerCache.clear();
        }

        public View findHeaderViewUnder(float x, float y) {
            for (RecyclerView.ViewHolder holder : headerCache.values()) {
                final View child = holder.itemView;
                final float translationX = ViewCompat.getTranslationX(child);
                final float translationY = ViewCompat.getTranslationY(child);

                if (x >= child.getLeft() + translationX &&
                        x <= child.getRight() + translationX &&
                        y >= child.getTop() + translationY &&
                        y <= child.getBottom() + translationY) {
                    return child;
                }
            }

            return null;
        }

        private boolean hasHeader(int position) {
            return adapter.getHeaderId(position) != NO_HEADER_ID;
        }

        private RecyclerView.ViewHolder getHeader(RecyclerView parent, int position) {
            final long key = adapter.getHeaderId(position);

            if (headerCache.containsKey(key)) {
                return headerCache.get(key);
            } else {
                final RecyclerAdapter.HeaderHolder holder = adapter.onCreateHeaderViewHolder(parent);
                final View header = holder.itemView;

                //noinspection unchecked
                adapter.onBindHeaderViewHolder(holder, position);

                int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getMeasuredWidth(), View.MeasureSpec.EXACTLY);
                int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getMeasuredHeight(), View.MeasureSpec.UNSPECIFIED);

                int childWidth = ViewGroup.getChildMeasureSpec(widthSpec,
                        parent.getPaddingLeft() + parent.getPaddingRight(), header.getLayoutParams().width);
                int childHeight = ViewGroup.getChildMeasureSpec(heightSpec,
                        parent.getPaddingTop() + parent.getPaddingBottom(), header.getLayoutParams().height);

                header.measure(childWidth, childHeight);
                header.layout(0, 0, header.getMeasuredWidth(), header.getMeasuredHeight());

                headerCache.put(key, holder);

                return holder;
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
            final int count = parent.getChildCount();
            long previousHeaderId = -1;

            for (int layoutPos = 0; layoutPos < count; layoutPos++) {
                final View child = parent.getChildAt(layoutPos);
                final int adapterPos = parent.getChildAdapterPosition(child);

                if (adapterPos != RecyclerView.NO_POSITION && hasHeader(adapterPos)) {
                    long headerId = adapter.getHeaderId(adapterPos);

                    if (headerId != previousHeaderId) {
                        previousHeaderId = headerId;
                        View header = getHeader(parent, adapterPos).itemView;
                        canvas.save();

                        final int left = child.getLeft();
                        final int top = getHeaderTop(parent, child, header, adapterPos, layoutPos);


                        canvas.translate(left, top);

                        header.setTranslationX(left);
                        header.setTranslationY(top);

//                        TextView textView = (TextView) header;
//                        if (top == 0 && textView.getTextSize() > 50) {
//                            textView.setTextSize(COMPLEX_UNIT_PX, textView.getTextSize() - 2);
//                        }
//
//
//                        if (top > 0 && textView.getTextSize() < 75) {
//                            textView.setTextSize(COMPLEX_UNIT_PX, textView.getTextSize() + 2);
//                        }
//
//                        canvas.scale(1.0f, Math.max(textView.getTextSize() / 75f, 0.8f));


                        header.draw(canvas);


                        canvas.restore();


//                    ((TextView)header).setText("" + top);

                    }
                }
            }
        }

        private int getHeaderTop(RecyclerView parent, View child, View header, int adapterPos, int layoutPos) {
            int headerHeight = getHeaderHeightForLayout(header);
            int top = ((int) child.getY()) - headerHeight;
            if (layoutPos == 0) {
                final int count = parent.getChildCount();
                final long currentId = adapter.getHeaderId(adapterPos);
                // find next view with header and compute the offscreen push if needed
                for (int i = 1; i < count; i++) {
                    int adapterPosHere = parent.getChildAdapterPosition(parent.getChildAt(i));
                    if (adapterPosHere != RecyclerView.NO_POSITION) {
                        long nextId = adapter.getHeaderId(adapterPosHere);
                        if (nextId != currentId) {
                            final View next = parent.getChildAt(i);
                            final int offset = ((int) next.getY()) - (headerHeight + getHeader(parent, adapterPosHere).itemView.getHeight());
                            if (offset < 0) {
                                return offset;
                            } else {
                                break;
                            }
                        }
                    }
                }

                top = Math.max(0, top);
            }

            return top;
        }

        private int getHeaderHeightForLayout(View header) {
            return renderInline ? 0 : header.getHeight();
        }
    }

}

