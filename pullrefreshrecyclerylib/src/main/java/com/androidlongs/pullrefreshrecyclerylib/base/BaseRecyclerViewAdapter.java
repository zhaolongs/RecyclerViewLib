package com.androidlongs.pullrefreshrecyclerylib.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by androidlongs on 17/8/12.
 * 站在顶峰，看世界
 * 落在谷底，思人生
 */

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<T> datas;
    private int layoutId;
    protected OnItemClickListner onItemClickListner;//单击事件
    protected OnItemLongClickListner onItemLongClickListner;//长按单击事件
    private boolean clickFlag = true;//单击事件和长单击事件的屏蔽标识


    /**
     * @param context  上下文对象
     * @param datas    数据
     * @param layoutId 布局ID
     */
    public BaseRecyclerViewAdapter(Context context, List<T> datas, int layoutId) {
        this.context = context;
        this.datas = datas;
        this.layoutId = layoutId;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建ViewHolder
        return new BaseViewHolder(inflater.inflate(layoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        //数据绑定
        bindData(holder, datas.get(position), position);
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    protected abstract void bindData(BaseViewHolder holder, T data, int position);

    //设置Item点击事件
    public void setOnItemClickListner(OnItemClickListner onItemClickListner) {
        this.onItemClickListner = onItemClickListner;
    }

    //设置Item长按点击事件
    public void setOnItemLongClickListner(OnItemLongClickListner onItemLongClickListner) {
        this.onItemLongClickListner = onItemLongClickListner;
    }

    public  void updateDatas(List<T> datas){
        this.datas = datas;
        notifyDataSetChanged();
    };

    public interface OnItemClickListner {
        void onItemClickListner(View v, int position);
    }

    public interface OnItemLongClickListner {
        void onItemLongClickListner(View v, int position);
    }

}
