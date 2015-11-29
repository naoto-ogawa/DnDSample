package com.example.dnd03;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import static android.view.DragEvent.*;
import static android.view.DragEvent.ACTION_DRAG_ENDED;

public class MyListView extends ListView implements AdapterView.OnItemLongClickListener, View.OnDragListener{

    private static final int SCROLL_SIZE = 5;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        addListener();
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addListener();
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addListener();
    }

    public MyListView(Context context) {
        super(context);
        addListener();
    }

    private void addListener() {
        setOnItemLongClickListener(this);
        setOnDragListener(this);
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {

        // event location
        float x = event.getX();
        float y = event.getY();

        // current position
        int pos = pointToPosition((int)x, (int)y);

        int posF = this.getFirstVisiblePosition();
        int posL = this.getLastVisiblePosition();

        // scroll
        // top
        if (pos > -1 && posF > -1 && pos == posF) {
            setSelection(pos - SCROLL_SIZE > 0 ?
                         pos - SCROLL_SIZE     :
                         0
            );
        }
        // bottom
        if (posL > -1 && pos > -1 && posL == pos ) {
            int viewItemCnt = posL - posF + 1;
            int itemCnt     = getAdapter().getCount();
            setSelection(pos + SCROLL_SIZE < itemCnt     ?
                         pos + SCROLL_SIZE - viewItemCnt :
                         itemCnt - viewItemCnt
            );
        }

        // current item view
        View currentView = this.getChildAt(pos - posF);

        // drag item view
        View dragView = (View)event.getLocalState();

        clearBackground();

        switch (event.getAction()) {
            case ACTION_DRAG_LOCATION:
                if (dragView != currentView) {
                    if (currentView.getTag(R.string.dragOverItem) == null) {
                        setDragOverItemColor(currentView);
                        currentView.setTag(R.string.dragOverItem, R.string.dragOverItem);
                    }
                }
                break;
            case ACTION_DROP:
                Object tag = dragView.getTag(R.string.dragItem);
                if (tag != null) {
                    setDefaultItemColor(currentView);
                    invalidateViews();
                    getMyAdapter().swapItems((int) tag, pos);
                }
                break;
            case ACTION_DRAG_ENDED:
                setDefaultItemColor(dragView);
                dragView.setTag(R.string.dragItem, null);
            default:
                break;
        }

        return true;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        ClipData data = ClipData.newPlainText("", "");
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
        setDragItemColor(view);
        view.setTag(R.string.dragItem, i);
        view.startDrag(data, shadowBuilder, view, 0);
        return true;
    }

    private MyAdapter getMyAdapter() {
        return (MyAdapter)getAdapter();
    }

    //-----------------------
    // Item color
    //-----------------------
    private void setDragItemColor(View view) {
        view.setBackgroundColor(Color.BLACK);
        ((TextView)view.findViewById(R.id.text)).setTextColor(Color.WHITE);
    }

    private void setDragOverItemColor(View view) {
        view.setBackgroundColor(Color.GREEN);
        ((TextView)view.findViewById(R.id.text)).setTextColor(Color.WHITE);
        changeHeight(view, 78);
    }

    private void setDefaultItemColor(View view) {
        view.setBackgroundColor(Color.WHITE);
        ((TextView)view.findViewById(R.id.text)).setTextColor(Color.BLACK);
        resetHeight(view);
    }

    private void clearBackground() {
        for(int i = getFirstVisiblePosition() ; i <= this.getLastVisiblePosition(); i++ ) {
            View b = this.getChildAt(i - getFirstVisiblePosition());
            if (b.getTag(R.string.dragOverItem) != null) {
                setDefaultItemColor(b);
                b.setTag(R.string.dragOverItem, null);
            }
        }
    }

    //-----------------------
    // Item size
    //-----------------------
    private void changeHeight(View view, int height) {
        ViewGroup.LayoutParams params  = view.getLayoutParams();
        view.setTag(R.string.height, params.height);
        params.height = height;
        view.setLayoutParams(params);
    }

    private void resetHeight(View v) {
        ViewGroup.LayoutParams params  = v.getLayoutParams();
        Object tag = v.getTag(R.string.height);
        if (tag != null) {
            params.height = (Integer)tag;
            v.setLayoutParams(params);
        }
    }

}
