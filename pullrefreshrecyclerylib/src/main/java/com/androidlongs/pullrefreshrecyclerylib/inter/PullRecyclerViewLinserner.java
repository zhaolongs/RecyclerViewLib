package com.androidlongs.pullrefreshrecyclerylib.inter;

import android.view.View;

/**
 * Created by androidlongs on 2017/8/21.
 * 站在顶峰，看世界
 * 落在谷底，思人生
 */

public interface PullRecyclerViewLinserner {
    /**
     * 上拉加载更多 回调
     */
    void loadMoreData();

    /**
     * 下拉加载更多回调
     */
    void loadingRefresDataFunction();

    /**
     * 设置数据回调
     *
     * @param itemType 根布局对象
     * @param object   数据模型
     * @param position 当前条目位置
     * @param itemType 当前的条目布局类型
     */
    void setViewDatas(View itemView, int position, int itemType, Object object);
}
