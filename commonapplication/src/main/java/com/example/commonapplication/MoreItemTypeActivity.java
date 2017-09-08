package com.example.commonapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidlongs.pullrefreshrecyclerylib.common.PullRecyclerViewUtils;
import com.androidlongs.pullrefreshrecyclerylib.inter.PullRecyclerViewLinserner;
import com.androidlongs.pullrefreshrecyclerylib.model.PullRecyclerMoreStatueModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by androidlongs on 2017/9/8.
 */

public class MoreItemTypeActivity extends Activity {

    private PullRecyclerViewUtils<PullRecyclerMoreStatueModel> pullRecyclerViewUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_no_loadmore_and_no_pull_layout);
        TextView headerTextView = findViewById(R.id.tv_header_title);
        headerTextView.setText("普通模式");


        int pageType = getIntent().getIntExtra("pageType", 1);


        //当前页面 要显示列表数据的父布局
        LinearLayout maintContentLinearLayyout = findViewById(R.id.ll_content);


        //1初始化
        pullRecyclerViewUtils = PullRecyclerViewUtils.getInstance();

        //2数据集合
        final List<PullRecyclerMoreStatueModel> list = new ArrayList<>();

        //3初始化Recyclerview
        /**
         * 参数一 this Context实例
         * 参数二 单一布局模式条目布局ID 多布局模式下可以传-1
         * 参数三 数据集合
         * 参数四 回调监听
         */
        RelativeLayout relativeLayout = pullRecyclerViewUtils.setRecyclerViewFunction(this, -1, list, mPullRecclerLinserner);

        //4相关设置
        //----
        //设置RecyclerView的模式
        /**
         *
         * @param statue NORMAL,//正常状态下，没有下拉刷新也没有上拉加载更多
         *               PULL_REFRESH,//只有下拉刷新功能
         *               UP_LOAD_MORE,//只有上拉加载更多功能
         *               PULL_AND_UP//下拉刷新 上拉加载功能
         * @see PullRecyclerViewUtils.RECYCLRYVIEW_STATUE
         */
        //下拉刷新和上拉加载更多模式
        pullRecyclerViewUtils.setRecyclerviewStatue(PullRecyclerViewUtils.RECYCLRYVIEW_STATUE.PULL_AND_UP);


        //----
        //5将recyclerview添加到当前显示的页面中
        maintContentLinearLayyout.addView(relativeLayout);


        //模拟网络
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                /**
                 * 多而布局样式下 需要使用 PullRecyclerMoreStatueModel 数据模型 将数据和其对应的布局样式封装
                 */
                List<PullRecyclerMoreStatueModel> list = new ArrayList<>();

                for (int i = 0; i < 14; i++) {
                    //网络请求的用户数据
                    DataModel dataModel = new DataModel();
                    dataModel.name = "小燕子的情怀" + i;

                    //构造多布局样式与数据模式
                    PullRecyclerMoreStatueModel moreStatueModel = new PullRecyclerMoreStatueModel();

                    moreStatueModel.model = dataModel;

                    if (i % 3 == 0) {
                        //第一种布局样式

                        moreStatueModel.itemLayoutId = R.layout.item_comm_base_layout;
                        moreStatueModel.itemType = 1;
                    } else if (i % 3 == 1) {
                        //第二种布局样式
                        moreStatueModel.itemLayoutId = R.layout.item_comm_base_2_layout;
                        moreStatueModel.itemType = 2;
                    } else {
                        //第三种布局样式
                        moreStatueModel.itemLayoutId = R.layout.item_comm_base_3_layout;
                        moreStatueModel.itemType = 3;
                    }

                    list.add(moreStatueModel);
                }
                //更新数据
                //这里需要注意的是 每次设置的数据集合都是一个新的List对象
                pullRecyclerViewUtils.setLoadingDataList(list);
            }
        }, 2000);
    }


    /**
     * RecyclerView相关操作的回调
     */
    private PullRecyclerViewLinserner mPullRecclerLinserner = new

            PullRecyclerViewLinserner() {
                //当触发上拉加载更多时，回调此方法
                @Override
                public void loadMoreData() {
                    //模拟网络请求
                    new Handler(getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //请求成功的数据集合
                            List<PullRecyclerMoreStatueModel> list = new ArrayList<>();

                            for (int i = 0; i < 14; i++) {
                                //网络请求的用户数据
                                DataModel dataModel = new DataModel();
                                dataModel.name = "小燕子的情怀" + i;

                                //构造多布局样式与数据模式
                                PullRecyclerMoreStatueModel moreStatueModel = new PullRecyclerMoreStatueModel();

                                moreStatueModel.model = dataModel;

                                if (i % 3 == 0) {
                                    //第一种布局样式

                                    moreStatueModel.itemLayoutId = R.layout.item_comm_base_layout;
                                    moreStatueModel.itemType = 1;
                                } else if (i % 3 == 1) {
                                    //第二种布局样式
                                    moreStatueModel.itemLayoutId = R.layout.item_comm_base_2_layout;
                                    moreStatueModel.itemType = 2;
                                } else {
                                    //第三种布局样式
                                    moreStatueModel.itemLayoutId = R.layout.item_comm_base_3_layout;
                                    moreStatueModel.itemType = 3;
                                }

                                list.add(moreStatueModel);
                            }
                            //更新数据
                            //这里的数据集合为新的list对象就可以
                            //内部自动处理了数据的延续添加刷新
                            pullRecyclerViewUtils.setLoadingDataList(list);
                        }
                    }, 2000);
                }

                //当触发下拉刷新数据时会回调此方法
                @Override
                public void loadingRefresDataFunction() {
                    //模拟网络请求
                    new Handler(getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //请求成功的数据集合
                            List<PullRecyclerMoreStatueModel> list = new ArrayList<>();

                            for (int i = 0; i < 14; i++) {
                                //网络请求的用户数据
                                DataModel dataModel = new DataModel();
                                dataModel.name = "小燕子的情怀" + i;

                                //构造多布局样式与数据模式
                                PullRecyclerMoreStatueModel moreStatueModel = new PullRecyclerMoreStatueModel();

                                moreStatueModel.model = dataModel;

                                if (i % 3 == 0) {
                                    //第一种布局样式

                                    moreStatueModel.itemLayoutId = R.layout.item_comm_base_layout;
                                    moreStatueModel.itemType = 1;
                                } else if (i % 3 == 1) {
                                    //第二种布局样式
                                    moreStatueModel.itemLayoutId = R.layout.item_comm_base_2_layout;
                                    moreStatueModel.itemType = 2;
                                } else {
                                    //第三种布局样式
                                    moreStatueModel.itemLayoutId = R.layout.item_comm_base_3_layout;
                                    moreStatueModel.itemType = 3;
                                }

                                list.add(moreStatueModel);
                            }
                            //这里的数据集合为新的list对象就可以
                            //内部自动处理了数据的清空更新
                            pullRecyclerViewUtils.setLoadingDataList(list);
                        }
                    }, 2000);
                }

                //设置数据回调方法

                /**
                 *
                 * @param itemView  布局条目对应的View
                 * @param position 当前条目位置
                 * @param itemType 当前的条目布局类型
                 * @param object   数据模型
                 */
                @Override
                public void setViewDatas(View itemView, int position, int itemType, Object object) {


                    //多布局模式下  object对应的数据类型是 PullRecyclerMoreStatueModel
                    PullRecyclerMoreStatueModel pullRecyclerMoreStatueModel = (PullRecyclerMoreStatueModel) object;
                    //获取对应的数据类型或者说是条目布局样式
                    itemType = pullRecyclerMoreStatueModel.itemType;

                    //根据itemType来获取对应的数据 与 设置对应的数据显示
                    if (itemType == 1) {
                        //对就的模型数据
                        DataModel dataModel = (DataModel) pullRecyclerMoreStatueModel.model;

                        //设置数据
                        TextView nameTextView = itemView.findViewById(R.id.tv_item_name);
                        nameTextView.setText(dataModel.name);
                    } else if (itemType == 2) {
                        //对就的模型数据
                        DataModel dataModel = (DataModel) pullRecyclerMoreStatueModel.model;

                        //设置数据
                        TextView nameTextView = itemView.findViewById(R.id.tv_item_name);
                        nameTextView.setText(dataModel.name);
                    } else {
                        //对就的模型数据
                        DataModel dataModel = (DataModel) pullRecyclerMoreStatueModel.model;

                        //设置数据
                        TextView nameTextView = itemView.findViewById(R.id.tv_item_name);
                        nameTextView.setText(dataModel.name);
                    }


                }
            };
}
