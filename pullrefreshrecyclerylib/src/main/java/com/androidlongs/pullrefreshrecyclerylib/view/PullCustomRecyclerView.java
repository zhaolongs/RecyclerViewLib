package com.androidlongs.pullrefreshrecyclerylib.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by androidlongs on 2017/8/22.
 * 站在顶峰，看世界
 * 落在谷底，思人生
 */

public class PullCustomRecyclerView extends RecyclerView {
    public PullCustomRecyclerView(Context context) {
        super(context);
    }

    public PullCustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PullCustomRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent e) {
//        return mIsDispatchBoolean;
//    }

    public boolean mIsDispatchBoolean = true;

    public void setDispatchBoolean(boolean flag) {
        Log.e("dispatch ", "flag is " + flag);
        mIsDispatchBoolean = flag;
    }
}
