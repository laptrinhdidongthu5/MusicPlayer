package nhomso1.project2.musicplayer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


/**
 * Ta sẽ kiểm tra các sự kiện chạm để tạo hành động cho RecyclerView
 */
public class CustomTouchListener implements RecyclerView.OnItemTouchListener {

    //Dò cử chỉ để chặn các sự kiện chạm
    GestureDetector gestureDetector;

    private onItemClickListener clickListener;


    public CustomTouchListener(Context context,final RecyclerView recyclerView, final onItemClickListener clickListener) {
        this.clickListener = clickListener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.favoriteOnClick(child, recyclerView.getChildPosition(child));
                    }
                }

        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent e) {

        View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
        Log.i("a",gestureDetector.toString());
        if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
            clickListener.playOnClick(child, recyclerView.getChildLayoutPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        Log.i("a",gestureDetector.toString());
        if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
            clickListener.favoriteOnClick(child, rv.getChildLayoutPosition(child));
        }
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
