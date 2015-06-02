package org.bq.contact;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

/**
 * Created by bq on 2015/1/5.
 */
public class SimpleContactListView extends ContactListView {

    public SimpleContactListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void createScroller() {

        mScroller = new IndexScroller(getContext(), this);


        mScroller.setAutoHide(autoHide);

        // style 1
        //mScroller.setShowIndexContainer(false);
        //mScroller.setIndexPaintColor(Color.argb(255, 49, 64, 91));

        // style 2
        mScroller.setShowIndexContainer(true);
        mScroller.setIndexPaintColor(Color.WHITE);


        if (autoHide)
            mScroller.hide();
        else
            mScroller.show();


    }
}
