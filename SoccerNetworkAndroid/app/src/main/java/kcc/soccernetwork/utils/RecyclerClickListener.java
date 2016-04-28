package kcc.soccernetwork.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 4/6/2016.
 */

public class RecyclerClickListener implements RecyclerView.OnItemTouchListener {
    public static interface ItemClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    private GestureDetector mGestureDetector;
    private ItemClickListener mItemClickListener;
    public RecyclerClickListener(Context context, final RecyclerView recyclerView, ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && mItemClickListener != null) {
                    mItemClickListener.onLongClick(child, recyclerView.getChildPosition(child));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && mItemClickListener != null && mGestureDetector.onTouchEvent(e)) {
            mItemClickListener.onClick(child, rv.getChildPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }


}
