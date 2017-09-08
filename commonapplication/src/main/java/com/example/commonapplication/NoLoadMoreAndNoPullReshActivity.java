package com.example.commonapplication;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidlongs.pullrefreshrecyclerylib.common.PullRecyclerViewUtils;
import com.androidlongs.pullrefreshrecyclerylib.inter.PullRecyclerViewLinserner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by androidlongs on 2017/9/8.
 */

public class NoLoadMoreAndNoPullReshActivity extends Activity {

    private PullRecyclerViewUtils<DataModel> pullRecyclerViewUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_no_loadmore_and_no_pull_layout);
        TextView headerTextView = findViewById(R.id.tv_header_title);
        headerTextView.setText("普通模式");


        int pageType = getIntent().getIntExtra("pageType", 1);
        int bgType = getIntent().getIntExtra("bgType", 1);
        int refreshType = getIntent().getIntExtra("refreshType", 1);
        int nodataFirstIn = getIntent().getIntExtra("nodata_first_in", 1);


        //当前页面 要显示列表数据的父布局
        LinearLayout maintContentLinearLayyout = findViewById(R.id.ll_content);


        //初始化
        pullRecyclerViewUtils = PullRecyclerViewUtils.getInstance();

        //1条目布局ID
        int itemLayoutId = R.layout.item_comm_base_layout;
        //2数据集合
        final List<DataModel> list = new ArrayList<>();

        //3初始化Recyclerview
        /**
         * 参数一 this Context实例
         * 参数二 单一布局模式条目布局ID
         * 参数三 数据集合
         * 参数四 回调监听
         */
        RelativeLayout relativeLayout = pullRecyclerViewUtils.setRecyclerViewFunction(this, itemLayoutId, list, mPullRecclerLinserner);

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
        switch (pageType) {
            case 1:
                //下拉刷新和上拉加载更多模式
                pullRecyclerViewUtils.setRecyclerviewStatue(PullRecyclerViewUtils.RECYCLRYVIEW_STATUE.PULL_AND_UP);
                break;

            case 2:
                //无下拉刷新 也无上拉加载更多
                pullRecyclerViewUtils.setRecyclerviewStatue(PullRecyclerViewUtils.RECYCLRYVIEW_STATUE.NORMAL);
                break;
            case 3:
                //无下拉刷新 只有上拉加载更多
                pullRecyclerViewUtils.setRecyclerviewStatue(PullRecyclerViewUtils.RECYCLRYVIEW_STATUE.UP_LOAD_MORE);
                break;
            case 4:
                //下拉刷新 无上拉加载更多
                pullRecyclerViewUtils.setRecyclerviewStatue(PullRecyclerViewUtils.RECYCLRYVIEW_STATUE.PULL_REFRESH);
                break;
            default:
                //下拉刷新和上拉加载更多模式
                pullRecyclerViewUtils.setRecyclerviewStatue(PullRecyclerViewUtils.RECYCLRYVIEW_STATUE.PULL_AND_UP);
                break;
        }

        //背景样式的设置
        switch (bgType) {
            case 1:
                pullRecyclerViewUtils.setMainBackgroundRelativeLayoutColor(Color.GRAY);
                break;
            case 2:
                pullRecyclerViewUtils.addMainBackgroundChildLayout(R.layout.item_refresh_bg_layout);
                //或者
                View view = View.inflate(this, R.layout.item_refresh_bg_layout, null);
                pullRecyclerViewUtils.addMainBackgroundChildLayout(view);
                break;
            default:
                pullRecyclerViewUtils.setMainBackgroundRelativeLayoutColor(Color.WHITE);
                break;
        }

        //下拉刷新部位的背景颜色 设置
        pullRecyclerViewUtils.setPullRefshBackGroundColor(Color.WHITE);
        //下拉刷新部位的显示文字的颜色
        pullRecyclerViewUtils.setPullRefshTextColorFunction(Color.BLUE);
        //下拉刷新样式的设置

        switch (refreshType) {
            case 1:
                pullRecyclerViewUtils.setPullRefshStatue(PullRecyclerViewUtils.PULLREFSH_SHOW_VIEW_STATUE.PB_AND_TV);
                break;
            case 2:
                //设置下拉刷新样式类型
                pullRecyclerViewUtils.setPullRefshStatue(PullRecyclerViewUtils.PULLREFSH_SHOW_VIEW_STATUE.TV);
                break;
            case 3:
                //设置下拉刷新样式类型
                pullRecyclerViewUtils.setPullRefshStatue(PullRecyclerViewUtils.PULLREFSH_SHOW_VIEW_STATUE.PB);
                break;
            case 4:
                //设置下拉刷新显示图片
                pullRecyclerViewUtils.setPullRefshImageFunction(this.getResources().getDrawable(R.drawable.home_table_topic_header));
                //设置下拉刷新样式类型
                pullRecyclerViewUtils.setPullRefshStatue(PullRecyclerViewUtils.PULLREFSH_SHOW_VIEW_STATUE.IV);
                break;
            case 5://设置下拉刷新显示图片
                pullRecyclerViewUtils.setPullRefshImageFunction(this.getResources().getDrawable(R.drawable.dra_pull_anim));
                //设置下拉刷新样式类型
                pullRecyclerViewUtils.setPullRefshStatue(PullRecyclerViewUtils.PULLREFSH_SHOW_VIEW_STATUE.IV);
                break;
            case 6:
                //设置下拉刷新显示图片
                pullRecyclerViewUtils.setPullRefshImageFunction(this.getResources().getDrawable(R.drawable.home_table_topic_header));
                //设置下拉刷新样式类型
                pullRecyclerViewUtils.setPullRefshStatue(PullRecyclerViewUtils.PULLREFSH_SHOW_VIEW_STATUE.IV_AND_TV);
                break;
        }


        //设置首次进入无数据显示样式
        /**
         * @param netLoadingStatue NO_DATA,//无数据
         *                         LOADING,//加载中
         *                         NORMAL//列表数据页面
         * @see PullRecyclerViewUtils.SHOW_DEFAUTLE_PAGE_TYPE
         */
        switch (nodataFirstIn) {
            case 1:
                pullRecyclerViewUtils.setFirstDefaultPageType(PullRecyclerViewUtils.SHOW_DEFAUTLE_PAGE_TYPE.LOADING);
                break;
            case 2:
                pullRecyclerViewUtils.setFirstDefaultPageType(PullRecyclerViewUtils.SHOW_DEFAUTLE_PAGE_TYPE.NO_DATA);
                break;
            case 3:
                pullRecyclerViewUtils.setFirstDefaultPageType(PullRecyclerViewUtils.SHOW_DEFAUTLE_PAGE_TYPE.NORMAL);
                break;
        }

        //----
        //5将recyclerview添加到当前显示的页面中
        maintContentLinearLayyout.addView(relativeLayout);


        //模拟网络
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                List<DataModel> list = new ArrayList<>();
                for (int i = 0; i < 14; i++) {
                    DataModel dataModel = new DataModel();
                    dataModel.name = "小燕子的情怀" + i;
                    list.add(dataModel);
                }
                //更新数据
                //这里需要注意的是 每次设置的数据集合都是一个新的List对象
                pullRecyclerViewUtils.setLoadingDataList(list);
            }
        }, 1800);
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
                            List<DataModel> list = new ArrayList<>();
                            for (int i = 0; i < 16; i++) {
                                DataModel dataModel = new DataModel();
                                dataModel.name = "小燕子的情怀" + i;
                                list.add(dataModel);
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
                            List<DataModel> list = new ArrayList<>();
                            for (int i = 0; i < 16; i++) {
                                DataModel dataModel = new DataModel();
                                dataModel.name = "小燕子的情怀" + i;
                                list.add(dataModel);
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

                    //对就的模型数据
                    DataModel dataModel = (DataModel) object;

                    //设置数据
                    TextView nameTextView = itemView.findViewById(R.id.tv_item_name);
                    nameTextView.setText(dataModel.name);
                }
            };
}
