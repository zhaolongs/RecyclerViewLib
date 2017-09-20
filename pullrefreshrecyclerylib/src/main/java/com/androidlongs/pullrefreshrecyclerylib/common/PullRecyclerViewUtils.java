package com.androidlongs.pullrefreshrecyclerylib.common;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidlongs.pullrefreshrecyclerylib.R;
import com.androidlongs.pullrefreshrecyclerylib.inter.PullRecyclerViewLinserner;
import com.androidlongs.pullrefreshrecyclerylib.inter.PullRecyclerViewOnItemClickLinserner;
import com.androidlongs.pullrefreshrecyclerylib.model.PullRecyclerMoreStatueModel;
import com.androidlongs.pullrefreshrecyclerylib.view.PullCustomRecyclerView;
import com.androidlongs.pullrefreshrecyclerylib.view.QuickIndexBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by androidlongs on 2017/8/21.
 * 站在顶峰，看世界
 * 落在谷底，思人生
 */


/**
 * 异常 ：java.lang.NoClassDefFoundError: Failed resolution of: Landroid/support/v4/animation/AnimatorCompatHelper;
 * 解决 ：可修改  compile 'com.android.support:appcompat-v7:24.2.1'
 *
 * @param <T>
 */

public class PullRecyclerViewUtils<T> {

    private PullCustomRecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private RelativeLayout mMainRefreshView;
    private ProgressBar mPullRefProgressBar;
    private ImageView mPullRefImageView;
    private LinearLayout mMainBackgroundRelativeLayout;
    private LinearLayout mLoadingNoDataLinearLayout;
    private TextView mLoadingNoDataTextView;
    private LinearLayout mLoadingIngLinearLayout;
    private TextView mLoadingIngTextView;
    //索引
    private QuickIndexBar mQuickIndexBar;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private ImageView mLoadingNoDataImageView;
    private ObjectAnimator mClickNoDataHidIngAndShowDataAnimator;
    private ObjectAnimator mClickNoDataHidIngAndShowNoDataAnimator;

    private PullRecyclerViewUtils() {

    }


    public static PullRecyclerViewUtils getInstance() {
        return new PullRecyclerViewUtils();
    }

    //条目点击事件监听回调
    private PullRecyclerViewOnItemClickLinserner mPullRecyclerViewOnItemClickLinserner;

    //刷新布局的高度 px 会根据屏幕密度来进行计算
    private int mPullRefshLayoutHeight;
    //
    private Context mContext;

    private int mItemLayoutId = 0;
    //回调接口
    private PullRecyclerViewLinserner mPullRecyclerViewLinserner;
    //数据
    private List<T> mStringList;

    private boolean mIsOneceRemoveRefreshLayout = false;

    //多布局集合
    /**
     * mMoreItemLayoutIdMap
     * key 对应item中的    position
     * value 对应item中的  layout id
     */
    // private LinkedHashMap<Integer, Integer> mMoreItemLayoutIdMap;


    /**
     * 设置
     *
     * @param context      上下文对象
     * @param itemLayoutId 单布局 layout id
     * @param list         数据集合
     * @param linserner    回调监听
     */
    public RelativeLayout setRecyclerViewFunction(Context context, int itemLayoutId, List<T> list, PullRecyclerViewLinserner linserner) {
        return setRecyclerViewFunction(context, itemLayoutId, list, linserner, null, RECYCLRYVIEW_STATUE.PULL_AND_UP, SHOW_DEFAUTLE_PAGE_TYPE.LOADING, LOADING_NO_DATA_PAGE_TYPE.TEXT);
    }

    /**
     * 设置
     *
     * @param context            上下文对象
     * @param itemLayoutId       单布局 layout id
     * @param list               数据集合
     * @param linserner          回调监听
     * @param itemClickLinserner 条目点击监听
     */
    public RelativeLayout setRecyclerViewFunction(Context context, int itemLayoutId, List<T> list, PullRecyclerViewLinserner linserner, PullRecyclerViewOnItemClickLinserner itemClickLinserner) {
        return setRecyclerViewFunction(context, itemLayoutId, list, linserner, itemClickLinserner, RECYCLRYVIEW_STATUE.PULL_AND_UP, SHOW_DEFAUTLE_PAGE_TYPE.LOADING, LOADING_NO_DATA_PAGE_TYPE.TEXT);
    }

    public RelativeLayout setRecyclerViewFunction(Context context, int itemLayoutId, List<T> list, SHOW_DEFAUTLE_PAGE_TYPE page_type, PullRecyclerViewLinserner linserner, PullRecyclerViewOnItemClickLinserner itemClickLinserner) {
        return setRecyclerViewFunction(context, itemLayoutId, list, linserner, itemClickLinserner, RECYCLRYVIEW_STATUE.PULL_AND_UP, page_type, LOADING_NO_DATA_PAGE_TYPE.TEXT);
    }

    /**
     * @param context          上下文对象
     * @param itemLayoutId     条目布局
     * @param linserner        设置监听
     * @param defautlePageType 默认显示无数据页面
     */
    public RelativeLayout setRecyclerViewFunction(Context context, int itemLayoutId, PullRecyclerViewLinserner linserner, SHOW_DEFAUTLE_PAGE_TYPE defautlePageType) {
        return setRecyclerViewFunction(context, itemLayoutId, null, linserner, null, RECYCLRYVIEW_STATUE.PULL_AND_UP, defautlePageType, LOADING_NO_DATA_PAGE_TYPE.TEXT);
    }

    /**
     * @param context          上下文对象
     * @param itemLayoutId     条目布局
     * @param dataList         数据
     * @param linserner        设置监听
     * @param defautlePageType 默认显示无数据页面
     */
    public RelativeLayout setRecyclerViewFunction(Context context, int itemLayoutId, List<T> dataList, PullRecyclerViewLinserner linserner, SHOW_DEFAUTLE_PAGE_TYPE defautlePageType) {
        return setRecyclerViewFunction(context, itemLayoutId, dataList, linserner, null, RECYCLRYVIEW_STATUE.PULL_AND_UP, defautlePageType, LOADING_NO_DATA_PAGE_TYPE.TEXT);
    }

    /**
     * @param context            上下文对象
     * @param itemLayoutId       布局ID
     *                           多布局状态下 传 -1
     * @param list               数据
     *                           多布局下 list数据为 PullRecyclerMoreStatueModel
     * @param linserner          操作回调
     * @param itemClickLinserner 条目点击事件回调
     * @param statue             功能设置
     *                           RECYCLRYVIEW_STATUE.NORMAL,//正常状态下，没有下拉刷新也没有上拉加载更多
     *                           RECYCLRYVIEW_STATUE.PULL_REFRESH,//只有下拉刷新功能
     *                           RECYCLRYVIEW_STATUE.UP_LOAD_MORE,//只有上拉加载更多功能
     *                           RECYCLRYVIEW_STATUE.PULL_AND_UP//下拉刷新 上拉加载功能
     * @param defautle_page_type 默认显示页面 @see SHOW_DEFAUTLE_PAGE_TYPE
     *                           SHOW_DEFAUTLE_PAGE_TYPE.NO_DATA,//无数据
     *                           SHOW_DEFAUTLE_PAGE_TYPE.LOADING//加载中
     * @param noDataPageType     默认显示页面 无数据状态显示类型
     *                           LOADING_NO_DATA_PAGE_TYPE.IMAGE,//只显示图片
     *                           LOADING_NO_DATA_PAGE_TYPE.IMAGE_AND_TEXT,//显示图片和文字
     *                           LOADING_NO_DATA_PAGE_TYPE.TEXT//只显示文字
     * @see SHOW_DEFAUTLE_PAGE_TYPE  默认显示页面
     * @see RECYCLRYVIEW_STATUE 功能设置
     * @see LOADING_NO_DATA_PAGE_TYPE 无数据页面显示类型
     */
    @SuppressLint("ClickableViewAccessibility")
    public RelativeLayout setRecyclerViewFunction(
            Context context,
            int itemLayoutId,
            List<T> list,
            PullRecyclerViewLinserner linserner,
            PullRecyclerViewOnItemClickLinserner itemClickLinserner,
            RECYCLRYVIEW_STATUE statue,
            SHOW_DEFAUTLE_PAGE_TYPE defautle_page_type,
            LOADING_NO_DATA_PAGE_TYPE noDataPageType) {
        if (mRecyclerView == null) {
            mContext = context;
            //单布局
            mItemLayoutId = itemLayoutId;
            //加载数据回调监听
            mPullRecyclerViewLinserner = linserner;
            //数据集合
            if (list == null) {
                mStringList = new ArrayList<>();
            } else {
                mStringList = new ArrayList<>();
                mStringList.addAll(list);
            }

            //当前RecyclerView 的显示功能模式
            mCurrentStatue = statue;
            //条目点击回调
            mPullRecyclerViewOnItemClickLinserner = itemClickLinserner;

            //当前默认显示的页面
            mCurrentShowDefaultType = defautle_page_type;

            //当前默认页面显示无数据类型
            mCurrentLoadingNoDataType = noDataPageType;

            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float scaledDensity = displayMetrics.scaledDensity;
            mPullRefshLayoutHeight = (int) (62 * scaledDensity);

            mMainRefreshView = (RelativeLayout) View.inflate(context, R.layout.pull_refresh_main, null);
            //加载无数据布局
            mLoadingNoDataLinearLayout = (LinearLayout) mMainRefreshView.findViewById(R.id.ll_pull_refresh_loading_no_data);
            //加载无数数据显示文字
            mLoadingNoDataTextView = (TextView) mMainRefreshView.findViewById(R.id.tv_pull_refresh_loading_no_data);
            mLoadingNoDataImageView = (ImageView) mMainRefreshView.findViewById(R.id.iv_pull_refresh_loading_no_data);
            mLoadingNoDataTextView.setOnClickListener(mNoDataOnClickListener);

            switch (mCurrentLoadingNoDataType) {
                case TEXT:
                    mLoadingNoDataImageView.setVisibility(View.GONE);
                    mLoadingNoDataTextView.setVisibility(View.VISIBLE);
                    break;
                case IMAGE:
                    mLoadingNoDataImageView.setVisibility(View.VISIBLE);
                    mLoadingNoDataTextView.setVisibility(View.GONE);
                    break;
                case IMAGE_AND_TEXT:
                    mLoadingNoDataImageView.setVisibility(View.VISIBLE);
                    mLoadingNoDataTextView.setVisibility(View.VISIBLE);
                    break;
            }

            //索引
            mQuickIndexBar = (QuickIndexBar) mMainRefreshView.findViewById(R.id.pull_refresh_index_bar);
            mQuickIndexBar.setVisibility(View.GONE);

            //加载中布局
            mLoadingIngLinearLayout = (LinearLayout) mMainRefreshView.findViewById(R.id.ll_pull_refresh_loading_ing);
            //加载上显示文字
            mLoadingIngTextView = (TextView) mMainRefreshView.findViewById(R.id.tv_pull_refresh_loading_ing);


            switch (mCurrentShowDefaultType) {
                case LOADING:
                    /**
                     * 默认显示加载中页面
                     * @see mStringList size 为0 显示加载中页页面，否则显示数据页面
                     * @see mCurrentShowDefaultType 不前显示的页面类型
                     * @see mLoadingIngLinearLayout 加载中页面
                     * @see mLoadingNoDataLinearLayout 无数据页面
                     * @see mNetLoadingStatue 当前网络加载状态
                     */
                    if (mStringList == null || mStringList.size() == 0) {
                        //显示加载中
                        mLoadingIngLinearLayout.setVisibility(View.VISIBLE);
                        //隐藏无数据页面
                        mLoadingNoDataLinearLayout.setVisibility(View.GONE);
                        //更新网络加载标识
                        mNetLoadingStatue = NETLOADINGSTATE.DEFAULT_LOADING;
                    } else {
                        //隐藏加载中
                        mLoadingIngLinearLayout.setVisibility(View.GONE);
                        //隐藏无数据
                        mLoadingNoDataLinearLayout.setVisibility(View.GONE);
                        //更新网络无加载标识
                        mNetLoadingStatue = NETLOADINGSTATE.NO_LOADING;
                    }
                    break;
                case NO_DATA:
                    /**
                     * 默认显示无数据页面
                     * @see mStringList size 为0 显示无数据页页面，否则显示数据页面
                     **/
                    if (mStringList == null || mStringList.size() == 0) {
                        mLoadingIngLinearLayout.setVisibility(View.GONE);
                        mLoadingNoDataLinearLayout.setVisibility(View.VISIBLE);
                    } else {
                        mLoadingIngLinearLayout.setVisibility(View.GONE);
                        mLoadingNoDataLinearLayout.setVisibility(View.GONE);
                        mNetLoadingStatue = NETLOADINGSTATE.NO_LOADING;
                    }
                    break;
            }


            //主显示背景
            mMainBackgroundRelativeLayout = (LinearLayout) mMainRefreshView.findViewById(R.id.ll_root_background);
            //刷新的布局
            mPullRefshLayout = (LinearLayout) mMainRefreshView.findViewById(R.id.ll_pull_refresh_main);
            //刷新的提示文字
            mPullRefTextView = (TextView) mMainRefreshView.findViewById(R.id.tv_pull_refesh);

            //刷新的进度
            mPullRefProgressBar = (ProgressBar) mMainRefreshView.findViewById(R.id.pb_pull_refresh_main);
            //imagevIWE
            mPullRefImageView = (ImageView) mMainRefreshView.findViewById(R.id.iv_pull_refresh_main);

            mPullRefshLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Log.e("global", "mPullRefshLayout global layout " + mPullRefshLayoutHeight + "  " + mPullRefshLayout.getTop());

                    mPullRefshLayout.layout(0, -mPullRefshLayoutHeight, mPullRefshLayout.getWidth(), 0);

                    /**
                     * 处理布局文件被重新加载问题
                     */
                    if (mStringList != null&&mStringList.size()>0) {
                        if (mLoadingNoDataLinearLayout != null) {
                            mLoadingNoDataLinearLayout.setVisibility(View.GONE);
                        }
                        if (mLoadingIngLinearLayout != null) {
                            mLoadingIngLinearLayout.setVisibility(View.GONE);
                        }
                    }
//                    if (mStringList==null||mStringList.size()==0) {
//                        mLoadingIngLinearLayout.setVisibility(View.GONE);
//                        mLoadingNoDataLinearLayout.setVisibility(View.VISIBLE);
//                    }else {
//                        mLoadingIngLinearLayout.setVisibility(View.GONE);
//                        mLoadingNoDataLinearLayout.setVisibility(View.GONE);
//                    }

                }
            });
            //初始化RecyclerView
            mRecyclerView = (PullCustomRecyclerView) mMainRefreshView.findViewById(R.id.rv_list);
            //设置adapter
            //设置布局样式
            mLinearLayoutManager = new LinearLayoutManager(context);
            //设置方向
            mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            //关联RecyclerView
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            //设置分割线
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());

            //关联Adapter
            mRecyclerView.setAdapter(mViewHolderAdapter);

            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            //设置滑动监听事件
            mRecyclerView.setOnScrollListener(mOnScrollListener);
            if ((mCurrentStatue == RECYCLRYVIEW_STATUE.PULL_REFRESH) || (mCurrentStatue == RECYCLRYVIEW_STATUE.PULL_AND_UP)) {
                //设置触摸
                mRecyclerView.setOnTouchListener(mOnTouchListener);
            }
        }

        return mMainRefreshView;
    }

    private SHOW_DEFAUTLE_PAGE_TYPE mCurrentShowDefaultType = SHOW_DEFAUTLE_PAGE_TYPE.LOADING;

    public void setNetLoadingType(NETLOADINGSTATE netLoadingStatue) {
        mNetLoadingStatue = netLoadingStatue;
    }

    /**
     * @param netLoadingStatue NO_DATA,//无数据
     *                         LOADING,//加载中
     *                         NORMAL//列表数据页面
     * @see SHOW_DEFAUTLE_PAGE_TYPE
     */
    public void setFirstDefaultPageType(SHOW_DEFAUTLE_PAGE_TYPE netLoadingStatue) {
        mCurrentShowDefaultType = netLoadingStatue;
        switch (mCurrentShowDefaultType) {
            case LOADING:
                /**
                 * 默认显示加载中页面
                 * @see mStringList size 为0 显示加载中页页面，否则显示数据页面
                 * @see mCurrentShowDefaultType 不前显示的页面类型
                 * @see mLoadingIngLinearLayout 加载中页面
                 * @see mLoadingNoDataLinearLayout 无数据页面
                 * @see mNetLoadingStatue 当前网络加载状态
                 */
                if (mStringList == null || mStringList.size() == 0) {
                    //显示加载中
                    mLoadingIngLinearLayout.setVisibility(View.VISIBLE);
                    //隐藏无数据页面
                    mLoadingNoDataLinearLayout.setVisibility(View.GONE);
                    //更新网络加载标识
                    mNetLoadingStatue = NETLOADINGSTATE.DEFAULT_LOADING;
                } else {
                    //隐藏加载中
                    mLoadingIngLinearLayout.setVisibility(View.GONE);
                    //隐藏无数据
                    mLoadingNoDataLinearLayout.setVisibility(View.GONE);
                    //更新网络无加载标识
                    mNetLoadingStatue = NETLOADINGSTATE.NO_LOADING;
                }
                break;
            case NO_DATA:
                /**
                 * 默认显示无数据页面
                 * @see mStringList size 为0 显示无数据页页面，否则显示数据页面
                 **/
                if (mStringList == null || mStringList.size() == 0) {
                    mLoadingIngLinearLayout.setVisibility(View.GONE);
                    mLoadingNoDataLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    mLoadingIngLinearLayout.setVisibility(View.GONE);
                    mLoadingNoDataLinearLayout.setVisibility(View.GONE);
                    mNetLoadingStatue = NETLOADINGSTATE.NO_LOADING;
                }
                break;
            case NORMAL:
                /**
                 * 默认显示数据页面
                 * @see mStringList size 为0 显示加载中页页面，否则显示数据页面
                 * @see mCurrentShowDefaultType 不前显示的页面类型
                 * @see mLoadingIngLinearLayout 加载中页面
                 * @see mLoadingNoDataLinearLayout 无数据页面
                 * @see mNetLoadingStatue 当前网络加载状态
                 */
                if (mStringList == null || mStringList.size() == 0) {
                    //显示加载中
                    mLoadingIngLinearLayout.setVisibility(View.GONE);
                    //隐藏无数据页面
                    mLoadingNoDataLinearLayout.setVisibility(View.GONE);
                    //更新网络加载标识
                    mNetLoadingStatue = NETLOADINGSTATE.DEFAULT_LOADING;
                } else {
                    //隐藏加载中
                    mLoadingIngLinearLayout.setVisibility(View.GONE);
                    //隐藏无数据
                    mLoadingNoDataLinearLayout.setVisibility(View.GONE);
                    //更新网络无加载标识
                    mNetLoadingStatue = NETLOADINGSTATE.NO_LOADING;
                }
                break;
        }
    }


    public enum SHOW_DEFAUTLE_PAGE_TYPE {
        NO_DATA,//无数据
        LOADING,//加载中
        NORMAL//列表数据页面
    }

    private RecyclerView.Adapter<RecyclerView.ViewHolder> mViewHolderAdapter = new
            RecyclerView.Adapter<RecyclerView.ViewHolder>() {
                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                    log(" itemviewType is ---  " + viewType);

                    //单一布局情况
                    if (viewType == 0) {
                        //加载条目布局文件
                        View view = View.inflate(mContext, mItemLayoutId, null);
                        //创建ViewHolder
                        CustomViewHolder customViewHolder = new CustomViewHolder(view, mPullRecyclerViewLinserner, mPullRecyclerViewOnItemClickLinserner);
                        return customViewHolder;
                    } else if (viewType == -11) {
                        //最后一个条目设置刷新布局显示
                        View view = View.inflate(mContext, R.layout.refresh_view_footer, null);
                        //创建ViewHolder
                        UpLoadViewHolder customViewHolder = new UpLoadViewHolder(view, mContext, mOnLastItemClickListerner);
                        return customViewHolder;
                    } else {
                        //其他多布局模式
                        View view = View.inflate(mContext, viewType, null);
                        //创建ViewHolder
                        CustomViewHolder customViewHolder = new CustomViewHolder(view, mPullRecyclerViewLinserner, mPullRecyclerViewOnItemClickLinserner);
                        return customViewHolder;
                    }


                }

                @Override
                public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

                    //根据position来获取ViewHolder的类型
                    int itemViewType = this.getItemViewType(position);
                    log(" itemviewType is " + itemViewType);


                    if (itemViewType == 0) {
                        //获取 显示普通数据 Holder
                        CustomViewHolder viewHolder = (CustomViewHolder) holder;

                        Object s = mStringList.get(position);
                        //设置数据
                        viewHolder.setDatas(position, itemViewType, s);

                    } else if (itemViewType == -11) {
                        //最后一个条目 获取刷新布局对应的Holder
                        UpLoadViewHolder viewHolder = (UpLoadViewHolder) holder;
                        viewHolder.setDatas(position, mCurrentUpLoadingStatue, mLoadingMoreTextColor, mLoadingMoreBackGroundColor);

                    } else {
                        //获取 显示普通数据 Holder
                        CustomViewHolder viewHolder = (CustomViewHolder) holder;

                        Object s = mStringList.get(position);
                        //设置数据
                        viewHolder.setDatas(position, itemViewType, s);
                    }


                }

                @Override
                public int getItemCount() {

                    /**
                     * 只有当显示的条目个数大于10 才启用上拉到底部加载更多数据功能
                     */


                    if (mStringList == null) {
                        return 0;
                    } else if (mStringList.size() > 10 && ((mCurrentStatue == RECYCLRYVIEW_STATUE.UP_LOAD_MORE) || (mCurrentStatue == RECYCLRYVIEW_STATUE.PULL_AND_UP))) {
                        return mStringList.size() + 1;
                    } else {
                        return mStringList.size();
                    }

                }

                @Override
                public int getItemViewType(int position) {

                    if (mStringList != null) {

                        if (mStringList.size() > 10) {
                            if (position == mStringList.size()) {
                                //如果是最后一个条目 那么返回1
                                //用来加载显示刷新布局
                                return -11;
                            } else {
                                Object lO = mStringList.get(position);
                                if (lO instanceof PullRecyclerMoreStatueModel) {
                                    return ((PullRecyclerMoreStatueModel) lO).itemLayoutId;
                                }
                                //用来加载显示普通布局
                                return 0;
                            }
                        } else {
                            //用来加载显示普通布局
                            Object lO = mStringList.get(position);
                            if (lO instanceof PullRecyclerMoreStatueModel) {
                                return ((PullRecyclerMoreStatueModel) lO).itemLayoutId;
                            }
                            return 0;
                        }
                    } else {
                        return 0;
                    }
                }
            };


    //当前屏幕上显示的最后一个条目数据对应的位置
    private int mLastVisibleItemPosition;
    //当前屏幕显示的第一个条目数据对应的位置
    private int mFirstVisibleItemPosition;

    //获取当前RecyclerView完全显示出的第一个条目的位置
    private int mFirstCompletelyVisibleItemPosition;

    //获取当前RecyclerView完全显示出的最后一个条目的位置
    private int mLastCompletelyVisibleItemPosition;

    /**
     * RecyclerView是否滑动到了顶部 只有滑动到了顶部才可以启用下拉刷新功能
     * 这里通过RecyclerView的布局管理者 mLinearLayoutManager来动态的获取当前屏幕上显示的RecyclerView的第一个条目对应的 角标索引
     */
    private boolean mIsToTop = true;


    /**
     * 上拉加载更多数据 是否正在加载
     * 当正在加载更多数据时，此时可能还会滑动RecyclerView
     * 为防止同时发起多次请求数据 所设置的标识
     */
    private boolean mIsLoading = false;

    /**
     * RecyclerView 的滑动监听事件
     * 在这里可以判断RecyclerView是否滑动到了顶部
     * 在这里用来判断RecyclerView是否滑动到了底部
     */
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //根据当前的布局管理都来获取显示的条目位置
            if (mLinearLayoutManager != null) {

                //回调
                if (mPullRecyclerLiserner != null) {
                    mPullRecyclerLiserner.onScrolled(recyclerView, dx, dy, mLinearLayoutManager);
                }
                //所有的条目个数
                int itemCount1 = mLinearLayoutManager.getItemCount();
                //获取当前RecyclerView显示最后一个条目的位置
                mLastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();
                //获取当前RecyclerView显示的第一个条目的位置
                mFirstVisibleItemPosition = mLinearLayoutManager.findFirstVisibleItemPosition();

                //获取当前RecyclerView完全显示出的最后一个条目的位置
                mLastCompletelyVisibleItemPosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();

                //获取当前RecyclerView完全显示出的第一个条目的位置
                mFirstCompletelyVisibleItemPosition = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();

                /**
                 * 当RecyclerView 显示的第一个条目 完全加载出来时
                 *      mFirstVisibleItemPosition 为 0
                 *      mFirstCompletelyVisibleItemPosition 也为0
                 * 当RecyclerView 显示的第一个条目 并没有完全加载出来，也就是显示了一半（显示不完全 ）
                 *      mFirstVisibleItemPosition 为 0
                 *      mFirstCompletelyVisibleItemPosition 为1
                 *
                 * 所以 当mFirstCompletelyVisibleItemPosition 为 0 表示 完全滑动到了顶部
                 */

                if (mFirstCompletelyVisibleItemPosition == 0) {
                    //更新滑动到顶部标识
                    mIsToTop = true;
                    Log.e("scroll ", "滑动到顶部 ");
                } else {
                    //更新滑动到顶部标识 false不在顶部
                    mIsToTop = false;

                    //获取当前屏幕上显示的条目的个数
                    int childCount = mLinearLayoutManager.getChildCount();
                    //获取总共的条目个数
                    int itemCount = mLinearLayoutManager.getItemCount();

                    /**
                     * 有上拉加载更多的状态时 启用
                     */
                    if ((mCurrentStatue == RECYCLRYVIEW_STATUE.UP_LOAD_MORE) || (mCurrentStatue == RECYCLRYVIEW_STATUE.PULL_AND_UP)) {
                        //当显示的条目数据大于10条时 才启用上拉加载更多功能
                        if (itemCount > 10) {
                            //当显示出最后一个条目时
                            if (mLastVisibleItemPosition == itemCount - 1) {
                                log("大于10 可以加载更多 " + itemCount);
                                //加载更多
                                if (!mIsLoading) {
                                    //更新加载标识
                                    mIsLoading = true;
                                    //加载更多数据
                                    //接口回调
                                    //上拉加载更多

                                    int size = 0;

                                    if (mStringList != null && mStringList.size() > 0) {
                                        size = mStringList.size();
                                    }

                                    mCurrentUpLoadingStatue = RECYCLERVIEW_UP_LOADING_STATUE.LIST_NOT_NULL;
                                    mViewHolderAdapter.notifyItemChanged(size);
                                    if (mPullRecyclerViewLinserner != null) {
                                        mPullRecyclerViewLinserner.loadMoreData();
                                    }
                                    //更新网络加载状态
                                    mNetLoadingStatue = NETLOADINGSTATE.UP_LOADING;

                                }
                            }
                        } else {
                            log("小于10 不可以加载更多 " + itemCount + " " + childCount);
                        }
                    }

                }
            }


        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (mViewHolderAdapter != null) {
                //当滑动停止时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                }
            }
        }

    };

    private ValueAnimator mValueAnimator;


    /**
     * 下拉刷新状态标识
     */
    public enum RefresState {
        PULL_DEFAULE,//开始默认
        PULL_SHOW,//显示释放加载
        PULL_LOADING,//显示正在加载中
        PULL_LOADING_FINISH//显示刷新完毕
    }

    /**
     * 网络加载标识
     */
    public enum NETLOADINGSTATE {
        NO_LOADING,//无加载
        DEFAULT_LOADING,//默认加载
        PULL_LOADING,//下拉加载中
        UP_LOADING,//上拉加载中
        CLICK_NO_DATA//无数据下点击加载
    }


    //网络加载标识
    private NETLOADINGSTATE mNetLoadingStatue = NETLOADINGSTATE.DEFAULT_LOADING;
    //滑动自动回弹的时间
    private long mPullDuration = 200;
    //手指按下的位置
    private float mDownY;
    //手指按下时 刷新控件的位置
    private int mPullTop;
    //手指按下时 RecyclerView控件的位置
    private int mRecyTop;
    //手指按下标识
    public boolean mIsDown = false;

    //当前的刷新状态
    public RefresState mCurrentRefresState = RefresState.PULL_DEFAULE;
    //下拉刷新显示的布局
    private LinearLayout mPullRefshLayout;
    //下拉刷新中显示的文字提示
    private TextView mPullRefTextView;


    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(final View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:


                    /**
                     * mValueAnimator 是当手指离开屏幕时，页面显示布局文件滚回原始默认位置或者指定位置的 动画
                     *
                     * 当手指按下去时，如果当前的下拉刷新布局或者 RecyclerView 正在移动，需要停止
                     */
                    if (mValueAnimator != null) {
                        if (mValueAnimator.isRunning() || mValueAnimator.isStarted()) {
                            mValueAnimator.cancel();
                        }
                    }

                    //获取手指按下的纵坐标
                    mDownY = event.getRawY();
                    //获取刷新控件当前的位置
                    mPullTop = mPullRefshLayout.getTop();
                    //获取列表控件当前的位置
                    mRecyTop = mRecyclerView.getTop();
                    //手指按下的标识
                    mIsDown = true;

                    log("down top is mRecyTop" + mRecyTop + "    mPullTop  " + mPullTop);
                    if (mIsToTop) {
                        return true;
                    } else {
                        return false;
                    }

                case MotionEvent.ACTION_MOVE:

                    if (mDownY == 0) {
                        /**
                         * mValueAnimator 是当手指离开屏幕时，页面显示布局文件滚回原始默认位置或者指定位置的 动画
                         *
                         * 当手指按下去时，如果当前的下拉刷新布局或者 RecyclerView 正在移动，需要停止
                         */
                        if (mValueAnimator != null) {
                            if (mValueAnimator.isRunning() || mValueAnimator.isStarted()) {
                                mValueAnimator.cancel();
                            }
                        }

                        //获取手指按下的纵坐标
                        mDownY = event.getRawY();
                        //获取刷新控件当前的位置
                        mPullTop = mPullRefshLayout.getTop();
                        //获取列表控件当前的位置
                        mRecyTop = mRecyclerView.getTop();
                        //手指按下的标识
                        mIsDown = true;

                    }
                    //获取实时手指触摸屏幕的 Y轴位置
                    float moveY = event.getRawY();
                    //计算 手指移动的距离
                    int flagY = (int) (moveY - mDownY);
                    /**
                     * 缩小 要不布局会随着滑动的距离变化太大
                     *  flagY >0 向下滑动
                     *  flagY < 0向上滑动
                     */
                    flagY = flagY / 2;


                    //当RecyclerView滑动到顶部的时候才可以拖动
                    if (mIsToTop) {

                        if (mCurrentRefresState == RefresState.PULL_DEFAULE) {

                            /**
                             * PULL_DEFAULE 状态时 RecyclerView处于屏幕的顶部
                             *  向上滑动时不做处理
                             *  向下滑动时 处理移动
                             */
                            if (flagY >= 0) {
                                /**
                                 * 当下滑到一定距离（显示刷新布局 mPullRefshLayout完全显示出来后）
                                 * 更新状态为 PULL_SHOW
                                 * 更新刷新布局的显示
                                 */
                                if (mPullRefshLayout.getTop() >= 0) {
                                    if (mCurrentRefresState != RefresState.PULL_SHOW) {
                                        mCurrentRefresState = RefresState.PULL_SHOW;
                                        mPullRefTextView.setText("释放刷新");
                                    }
                                }
                                //RecyclerView 位置限定
                                int recyTop = mRecyTop + flagY;

                                if (recyTop <= 0) {
                                    recyTop = 0;
                                }
                                int recyBottom = mRecyclerView.getHeight() + recyTop;

                                //下拉刷新位置限定
                                int pullTop = mPullTop + flagY;
                                if (pullTop <= -mPullRefshLayoutHeight) {
                                    pullTop = -mPullRefshLayoutHeight;
                                }
                                int pullBottom = mPullRefshLayout.getHeight() + pullTop;

                                //重新设置RecyclerView的显示
                                setRecyclerViewLayout(getRecyclerViewRect(recyTop, recyBottom));
                                //重新设置刷新布局文件的显示
                                setPullRefreshLayout(getPullRefreshLayoutRect(pullTop, pullBottom));


                                return true;
                            }
                        } else if (mCurrentRefresState == RefresState.PULL_SHOW) {

                            int recyTop = mRecyTop + flagY;
                            int recyBottom = mRecyclerView.getHeight() + recyTop;


                            //更新列表的

                            int pullTop = mPullTop + flagY;
                            if (pullTop <= -mPullRefshLayoutHeight) {
                                pullTop = -mPullRefshLayoutHeight;
                            }
                            int pullBottom = mPullRefshLayout.getHeight() + pullTop;

                            /**
                             * mPullRefshLayout没完全显示出来
                             * 也就是 mPullRefshLayout.getTop() < 0
                             * 更新为 PULL_DEFAULE 状态
                             */
                            if (mPullRefshLayout.getTop() < 0) {
                                if (mCurrentRefresState != RefresState.PULL_DEFAULE) {
                                    mCurrentRefresState = RefresState.PULL_DEFAULE;
                                    mPullRefTextView.setText("下拉刷新");
                                }
                            }

                            //重新设置RecyclerView的显示
                            setRecyclerViewLayout(getRecyclerViewRect(recyTop, recyBottom));
                            //重新设置刷新布局文件的显示
                            setPullRefreshLayout(getPullRefreshLayoutRect(pullTop, pullBottom));


                            return true;
                        } else if (mCurrentRefresState == RefresState.PULL_LOADING) {

                            /**
                             * 正在加载中 状态
                             *
                             * 在这里设置的是 如果下拉刷新正在进行中
                             * 那么只允许下拉 不可上滑
                             */
                            if (flagY > 0) {

                                int recyTop = mRecyTop + flagY;
                                int recyBottom = mRecyclerView.getHeight() + recyTop;


                                //更新列表的
                                int pullTop = mPullTop + flagY;
                                int pullBottom = mPullRefshLayout.getHeight() + pullTop;


                                //重新设置RecyclerView的显示
                                setRecyclerViewLayout(getRecyclerViewRect(recyTop, recyBottom));
                                //重新设置刷新布局文件的显示
                                setPullRefreshLayout(getPullRefreshLayoutRect(pullTop, pullBottom));


                                return true;
                            } else {
                                return true;
                            }
                        } else if (mCurrentRefresState == RefresState.PULL_LOADING_FINISH) {


                            /**
                             * 加载完成 状态
                             *
                             * 在这里设置的是 如果下拉刷新正在进行中
                             * 那么只允许下拉 不可上滑
                             */
                            int recyTop = mRecyTop + flagY;
                            if (recyTop <= 0) {
                                recyTop = 0;
                            }
                            int recyBottom = mRecyclerView.getHeight() + recyTop;


                            //更新列表的

                            int pullTop = mPullTop + flagY;
                            if (pullTop <= -mPullRefshLayoutHeight) {
                                pullTop = -mPullRefshLayoutHeight;
                            }
                            int pullBottom = mPullRefshLayout.getHeight() + pullTop;


                            //重新设置RecyclerView的显示
                            setRecyclerViewLayout(getRecyclerViewRect(recyTop, recyBottom));
                            //重新设置刷新布局文件的显示
                            setPullRefreshLayout(getPullRefreshLayoutRect(pullTop, pullBottom));

                            mRecyclerView.setDispatchBoolean(false);
                            return true;

                        }


                    } else {
                        return false;
                    }


                case MotionEvent.ACTION_UP:


                    //手指抬起
                    mIsDown = false;
                    mDownY = 0;
                    mRecyclerView.setDispatchBoolean(true);

                    //获取RecyclerView当前的位置
                    final int recyUpTop = mRecyclerView.getTop();

                    /**
                     * PULL_DEFAULE  状态，弹回初始默认隐藏页面
                     */
                    if (mCurrentRefresState == RefresState.PULL_DEFAULE) {
                        //不刷新，隐藏
                        mValueAnimator = ValueAnimator.ofFloat(1f, 0f);
                        mValueAnimator.setDuration(mPullDuration);
                        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                //0 -- 1
                                Float value = (Float) animation.getAnimatedValue();


                                int recyTop = (int) (recyUpTop * value);

                                int recyBottom = mRecyclerView.getHeight() + recyTop;


                                int pullTop = recyTop - mPullRefshLayoutHeight;

                                int pullBottom = mPullRefshLayout.getHeight() + pullTop;

                                //重新设置RecyclerView的显示
                                setRecyclerViewLayout(getRecyclerViewRect(recyTop, recyBottom));
                                //重新设置刷新布局文件的显示
                                setPullRefreshLayout(getPullRefreshLayoutRect(pullTop, pullBottom));


                            }
                        });
                        //开启
                        mValueAnimator.start();
                    } else if (mCurrentRefresState == RefresState.PULL_SHOW || mCurrentRefresState == RefresState.PULL_LOADING) {


                        mRecyclerView.setDispatchBoolean(false);

                        /**
                         * PULL_SHOW        状态
                         * PULL_LOADING     状态
                         * 都将进入显示 加载中数据状态
                         */
                        log("up state is  " + mCurrentRefresState);
                        //设置文字
                        mPullRefTextView.setText("正在加载中");


                        //刷新 显示正在加载中
                        mValueAnimator = ValueAnimator.ofFloat(0f, 1f);
                        mValueAnimator.setDuration(mPullDuration);
                        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                //0 -- 1
                                Float value = (Float) animation.getAnimatedValue();

                                int recyTop = (int) (recyUpTop - ((recyUpTop - mPullRefshLayoutHeight) * value));
                                int recyBottom = mRecyclerView.getHeight() + recyTop;


                                int pullTop = recyTop - mPullRefshLayoutHeight;
                                int pullBottom = mPullRefshLayout.getHeight() + pullTop;

                                //重新设置刷新布局文件的显示
                                setPullRefreshLayout(getPullRefreshLayoutRect(pullTop, pullBottom));

                                //重新设置RecyclerView的显示
                                setRecyclerViewLayout(getRecyclerViewRect(recyTop, recyBottom));

                                int top = mRecyclerView.getTop();
                                int bot = mRecyclerView.getBottom();


                                Log.e("pull ing ", "top " + pullTop + "  " + pullBottom + "  sourTop " + top + "  " + bot);


                            }
                        });
                        mValueAnimator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {

                                //正在加载中状态
                                if (mCurrentRefresState != RefresState.PULL_LOADING) {
                                    mCurrentRefresState = RefresState.PULL_LOADING;
                                    //加载更多数据方法
                                    //下拉加载刷新回调
                                    if (mPullRecyclerViewLinserner != null) {
                                        mPullRecyclerViewLinserner.loadingRefresDataFunction();
                                    }

                                }
                                mCurrentRefresState = RefresState.PULL_LOADING;
                                //更新网络加载状 为下拉刷新状态
                                mNetLoadingStatue = NETLOADINGSTATE.PULL_LOADING;

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        mValueAnimator.start();
                    } else if (mCurrentRefresState == RefresState.PULL_LOADING_FINISH) {
                        log("up state is loading_finish ");
                        //设置文字
                        mPullRefTextView.setText("已更新数据完成");

                        //关闭刷新
                        mValueAnimator = ValueAnimator.ofFloat(1f, 0f);
                        mValueAnimator.setDuration(mPullDuration);
                        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                //0 -- 1
                                Float value = (Float) animation.getAnimatedValue();


                                int recyTop = (int) (recyUpTop * value);

                                int recyBottom = mRecyclerView.getHeight() + recyTop;


                                int pullTop = recyTop - mPullRefshLayoutHeight;

                                int pullBottom = mPullRefshLayout.getHeight() + pullTop;

                                //重新设置RecyclerView的显示
                                setRecyclerViewLayout(getRecyclerViewRect(recyTop, recyBottom));
                                //重新设置刷新布局文件的显示
                                setPullRefreshLayout(getPullRefreshLayoutRect(pullTop, pullBottom));


                            }
                        });
                        mValueAnimator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                //更新为初始状态
                                mCurrentRefresState = RefresState.PULL_DEFAULE;
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        mValueAnimator.start();
                    }

                    break;
            }
            return false;
        }
    };

    /**
     * 重新布局RecyclerView 并进行刷新
     */
    public void setRecyclerViewLayout(Rect rect) {
        mRecyclerView.layout(rect.left, rect.top, rect.right, rect.bottom);
        //mRecyclerView.invalidate();
    }


    /**
     * 将RecyclerView 的left top right bottom 封装到Rect
     */
    public Rect getRecyclerViewRect(int top, int bottom) {
        return new Rect(mRecyclerView.getLeft(), top, mRecyclerView.getWidth(), bottom);
    }


    public Rect getPullRefreshLayoutRect(int top, int bottom) {
        return new Rect(mPullRefshLayout.getLeft(), top, mPullRefshLayout.getWidth(), bottom);
    }

    public void setPullRefreshLayout(Rect rect) {
        mPullRefshLayout.layout(rect.left, rect.top, rect.right, rect.bottom);
        mPullRefshLayout.invalidate();
    }


    private void log(String msg) {
        Log.e("recy", "|------------------------------------------------------------");
        Log.d("recy", "|--------   " + msg);
    }

    public void closePullRefresh() {
        if (mRecyclerView != null) {
            mRecyclerView.setDispatchBoolean(false);
        }

        log("加载完成");
        //加载完成
        mCurrentRefresState = RefresState.PULL_LOADING_FINISH;
        //更新显示
        mPullRefTextView.setText("刷新数据完成");

        if (mIsDown) {
            //正在滑动中不需要结束布局显示
        } else {
            log("结束刷新");
            closePullRefresh(true);
        }
    }

    public void closePullRefresh(boolean flag) {

        if (flag) {
            mValueAnimator = ValueAnimator.ofFloat(1f, 0f);
            mValueAnimator.setDuration(mPullDuration);
            mValueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mCurrentRefresState = RefresState.PULL_DEFAULE;
                    //更新列表
                    mViewHolderAdapter.notifyDataSetChanged();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    //1 --- 0
                    Float value = (Float) animation.getAnimatedValue();


                    int recyTop = (int) (mPullRefshLayoutHeight * value);

                    int recyBottom = mRecyclerView.getHeight() + recyTop;


                    int pullTop = recyTop - mPullRefshLayoutHeight;

                    int pullBottom = mPullRefshLayout.getHeight() + pullTop;

                    //重新设置RecyclerView的显示
                    setRecyclerViewLayout(getRecyclerViewRect(recyTop, recyBottom));
                    //重新设置刷新布局文件的显示
                    setPullRefreshLayout(getPullRefreshLayoutRect(pullTop, pullBottom));


                }

            });

            mValueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    //重新设置RecyclerView的显示
                    setRecyclerViewLayout(getRecyclerViewRect(0, mRecyclerView.getHeight()));
                    //重新设置刷新布局文件的显示
                    setPullRefreshLayout(getPullRefreshLayoutRect(-mPullRefshLayoutHeight, mPullRefshLayout.getHeight()));

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            mValueAnimator.start();


        }

    }


    //普通加载项的ViewHolder
    private static class CustomViewHolder extends RecyclerView.ViewHolder {


        private PullRecyclerViewLinserner mPullRecyclerViewLinserner;
        private PullRecyclerViewOnItemClickLinserner mPullRecyclerViewOnItemClickLinserner;

        public CustomViewHolder(View itemView, PullRecyclerViewLinserner linserner, PullRecyclerViewOnItemClickLinserner pullRecyclerViewOnItemClickLinserner) {
            super(itemView);
            mPullRecyclerViewLinserner = linserner;

            mPullRecyclerViewOnItemClickLinserner = pullRecyclerViewOnItemClickLinserner;
        }

        public void setDatas(final int position, final int itemType, final Object object) {

            if (mPullRecyclerViewLinserner != null) {
                mPullRecyclerViewLinserner.setViewDatas(itemView, position, itemType, object);
            }
            if (itemView != null && itemView instanceof ViewGroup) {
                if (((ViewGroup) itemView).getChildCount() > 0 && ((ViewGroup) itemView).getChildAt(0) != null) {
                    ((ViewGroup) itemView).getChildAt(0).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mPullRecyclerViewOnItemClickLinserner != null) {
                                mPullRecyclerViewOnItemClickLinserner.setonItemClick(position, itemType, object);
                            }
                        }
                    });
                }
            }

        }


    }

    //
    public interface OnLastItemClickListerner {

        void loadMoreData();
    }

    public OnLastItemClickListerner mOnLastItemClickListerner = new OnLastItemClickListerner() {

        @Override
        public void loadMoreData() {
            //点击条目最后一条 加载更多
            if (mPullRecyclerViewLinserner != null) {
                mPullRecyclerViewLinserner.loadMoreData();
            }

            //网络加载标识 加载更多
            mNetLoadingStatue = NETLOADINGSTATE.UP_LOADING;
        }
    };

    //上拉加载更多的 ViewHolder
    private static class UpLoadViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTextView;
        private Context mContext;
        private final String mLoadingMoreNoDataString;
        private final String mLoadingMoreString;
        private final ProgressBar mLoadingMoreProgressBar;
        private final RelativeLayout mRootView;

        private OnLastItemClickListerner mOnLastItemClickListerner;

        public UpLoadViewHolder(View itemView, Context context, OnLastItemClickListerner linserner) {
            super(itemView);
            mContext = context;
            mOnLastItemClickListerner = linserner;
            mRootView = (RelativeLayout) itemView.findViewById(R.id.rl_loadiing_more_rootview);
            mLoadingMoreProgressBar = (ProgressBar) itemView.findViewById(R.id.pb_loading_more);
            mTextView = (TextView) itemView.findViewById(R.id.tv_loading_more);
            mLoadingMoreString = context.getResources().getString(R.string.up_loading_more);
            mLoadingMoreNoDataString = context.getResources().getString(R.string.up_loading_more_no_data);

        }


        public void setDatas(int position, RECYCLERVIEW_UP_LOADING_STATUE currentStatue, int loadingMoreTextColor, int loadingMoreBackGroundColor) {

            Log.d("recy", "设置加载更多布局");
            //设置显示字体颜色
            mTextView.setTextColor(loadingMoreTextColor);
            //设置显示背景颜色
            mRootView.setBackgroundColor(loadingMoreBackGroundColor);
            if (currentStatue == RECYCLERVIEW_UP_LOADING_STATUE.LIST_IS_NULL) {
                //显示暂无更多数据
                mTextView.setText(mLoadingMoreNoDataString);
                //隐藏加载中
                mLoadingMoreProgressBar.setVisibility(View.GONE);
                //设置点击事件
                mTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mOnLastItemClickListerner != null) {
                            mTextView.setOnClickListener(null);
                            mTextView.setText(mLoadingMoreString);
                            mLoadingMoreProgressBar.setVisibility(View.VISIBLE);
                            //回调
                            mOnLastItemClickListerner.loadMoreData();
                        }
                    }
                });
            } else {
                mTextView.setOnClickListener(null);
                //显示加载更多数据
                mTextView.setText(mLoadingMoreString);
                //显示加载中
                mLoadingMoreProgressBar.setVisibility(View.VISIBLE);
            }
        }
    }


    //功能 状态
    private RECYCLRYVIEW_STATUE mCurrentStatue = RECYCLRYVIEW_STATUE.PULL_AND_UP;

    /***
     * RecyclerView 功能设置
     */
    public enum RECYCLRYVIEW_STATUE {
        NORMAL,//正常状态下，没有下拉刷新也没有上拉加载更多
        PULL_REFRESH,//只有下拉刷新功能
        UP_LOAD_MORE,//只有上拉加载更多功能
        PULL_AND_UP//下拉刷新 上拉加载功能
    }

    private LOADING_NO_DATA_PAGE_TYPE mCurrentLoadingNoDataType = LOADING_NO_DATA_PAGE_TYPE.TEXT;

    /**
     * 默认显示无数据页面显示类型
     */
    public enum LOADING_NO_DATA_PAGE_TYPE {
        IMAGE,//只显示图片
        IMAGE_AND_TEXT,//显示图片和文字
        TEXT//只显示文字
    }

    private RECYCLERVIEW_UP_LOADING_STATUE mCurrentUpLoadingStatue = RECYCLERVIEW_UP_LOADING_STATUE.LIST_NOT_NULL;

    //上拉加载更多数据VIEW功能状态
    public enum RECYCLERVIEW_UP_LOADING_STATUE {
        LIST_IS_NULL,//没有数据
        LIST_NOT_NULL//有数据
    }


    //--------------------------------------------------------------------------------------------------
    //更新新数据 更新所有的数据
    //下拉刷新时可以使用

    /**
     * @param isLoadingListNull 加载的数据 是否为null 是：true
     */
    public void updateDataList(List<T> list, final boolean isLoadingListNull) {

        mIsLoading = false;

        //隐藏加载中
        //closePullRefresh();
        if (list == null) {
            list = new ArrayList<>();
        }

        log("recy mNetLoadingStatue " + mNetLoadingStatue);

        switch (mNetLoadingStatue) {
            case PULL_LOADING:
                //下拉
                //结束刷新状态
                closePullRefresh();

                if (mStringList.size() == 0) {
                    mStringList.clear();
                    mViewHolderAdapter.notifyDataSetChanged();
                    //显示无数据页面
                    ObjectAnimator lAlphaAnimation = ObjectAnimator.ofFloat(mLoadingNoDataLinearLayout, "alpha", 0f, 1f);
                    lAlphaAnimation.setDuration(300);
                    lAlphaAnimation.setInterpolator(new LinearInterpolator());
                    lAlphaAnimation.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            mLoadingNoDataLinearLayout.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {

                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    lAlphaAnimation.start();
                } else {
                    //刷新
                    final List<T> finalList = list;
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mStringList.clear();
                            mViewHolderAdapter.notifyDataSetChanged();
                            mStringList.addAll(finalList);
                            mViewHolderAdapter.notifyDataSetChanged();
                        }
                    }, 400);
                }
                break;
            case UP_LOADING:
                //上拉

                final List<T> finalList1 = list;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        mStringList.clear();
                        mStringList.addAll(finalList1);
                        if (isLoadingListNull) {
                            log("上拉 LIST_IS_NULL");
                            mCurrentUpLoadingStatue = RECYCLERVIEW_UP_LOADING_STATUE.LIST_IS_NULL;
                            int lSize = mStringList.size();
                            int lI = lSize - 1;
                            if (lI < 0) {
                                lI = 0;
                            }
                            mViewHolderAdapter.notifyDataSetChanged();
                        } else {
                            log("上拉 LIST_NOT_NULL");
                            mCurrentUpLoadingStatue = RECYCLERVIEW_UP_LOADING_STATUE.LIST_NOT_NULL;
                            mViewHolderAdapter.notifyDataSetChanged();
                        }
                    }
                }, 400);

                break;
            //没有
            case NO_LOADING:
                break;
            //
            case CLICK_NO_DATA:
                //无数据点击

                if (list.size() == 0) {
                    mStringList.clear();
                    mViewHolderAdapter.notifyDataSetChanged();
                    //隐藏加载中 显示无数据页面
                    final ObjectAnimator lAlphaAnimation = ObjectAnimator.ofFloat(mLoadingNoDataLinearLayout, "alpha", 0f, 1f);
                    lAlphaAnimation.setDuration(300);
                    lAlphaAnimation.setInterpolator(new LinearInterpolator());
                    lAlphaAnimation.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            mLoadingNoDataLinearLayout.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {

                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });

                    if (mClickNoDataHidIngAndShowNoDataAnimator != null) {
                        if (mClickNoDataHidIngAndShowNoDataAnimator.isRunning()) {
                            mClickNoDataHidIngAndShowNoDataAnimator.cancel();
                        }
                    }
                    mClickNoDataHidIngAndShowNoDataAnimator = ObjectAnimator.ofFloat(mLoadingIngLinearLayout, "alpha", 1f, 0f);
                    mClickNoDataHidIngAndShowNoDataAnimator.setDuration(300);
                    mClickNoDataHidIngAndShowNoDataAnimator.setInterpolator(new LinearInterpolator());
                    mClickNoDataHidIngAndShowNoDataAnimator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            mLoadingIngLinearLayout.setVisibility(View.GONE);
                            lAlphaAnimation.start();
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    mClickNoDataHidIngAndShowNoDataAnimator.start();

                } else {
                    //隐藏加载中 显示数据页面
                    if (mClickNoDataHidIngAndShowDataAnimator != null) {
                        if (mClickNoDataHidIngAndShowDataAnimator.isRunning()) {
                            mClickNoDataHidIngAndShowDataAnimator.cancel();
                            mClickNoDataHidIngAndShowDataAnimator = null;
                        }
                    }
                    mStringList.clear();
                    mViewHolderAdapter.notifyDataSetChanged();
                    mStringList.addAll(list);

                    mClickNoDataHidIngAndShowDataAnimator = ObjectAnimator.ofFloat(mLoadingIngLinearLayout, "alpha", 1f, 0f);
                    mClickNoDataHidIngAndShowDataAnimator.setDuration(300);
                    mClickNoDataHidIngAndShowDataAnimator.setInterpolator(new LinearInterpolator());
                    mClickNoDataHidIngAndShowDataAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            Float value = (Float) valueAnimator.getAnimatedValue();
                            mLoadingIngLinearLayout.setAlpha(value);
                            if (value == 0) {
                                if (mLoadingIngLinearLayout.getVisibility() == View.VISIBLE) {
                                    mLoadingIngLinearLayout.setVisibility(View.GONE);
                                    //刷新数据
                                    mViewHolderAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });

                    mClickNoDataHidIngAndShowDataAnimator.start();
                }
                break;
            case DEFAULT_LOADING:
                //第一次加载
                mStringList.clear();
                mStringList.addAll(list);

                if (mStringList.size() == 0) {
                    //隐藏加载中 显示无数据页面
                    final ObjectAnimator lAlphaAnimation = ObjectAnimator.ofFloat(mLoadingNoDataLinearLayout, "alpha", 0f, 1f);
                    lAlphaAnimation.setDuration(300);
                    lAlphaAnimation.setInterpolator(new LinearInterpolator());
                    lAlphaAnimation.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            mLoadingNoDataLinearLayout.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {

                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });

                    ObjectAnimator lAlphaAnimation2 = ObjectAnimator.ofFloat(mLoadingIngLinearLayout, "alpha", 1f, 0f);
                    lAlphaAnimation2.setDuration(300);
                    lAlphaAnimation2.setInterpolator(new LinearInterpolator());
                    lAlphaAnimation2.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            mLoadingIngLinearLayout.setVisibility(View.GONE);
                            lAlphaAnimation.start();
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    lAlphaAnimation2.start();
                } else {
                    switch (mCurrentShowDefaultType) {
                        case NO_DATA:
                            //隐藏无数据页面 显示数据
                            ObjectAnimator lAlphaAnimation2 = ObjectAnimator.ofFloat(mLoadingNoDataLinearLayout, "alpha", 1f, 0f);
                            lAlphaAnimation2.setDuration(300);
                            lAlphaAnimation2.setInterpolator(new LinearInterpolator());
                            lAlphaAnimation2.addListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animator) {
                                    mLoadingNoDataLinearLayout.setVisibility(View.GONE);
                                    mViewHolderAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onAnimationCancel(Animator animator) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animator) {

                                }
                            });
                            lAlphaAnimation2.start();
                            break;
                        case LOADING:
                            //隐藏加载中 显示数据
                            ObjectAnimator lAlphaAnimation3 = ObjectAnimator.ofFloat(mLoadingIngLinearLayout, "alpha", 1f, 0f);
                            lAlphaAnimation3.setDuration(300);
                            lAlphaAnimation3.setInterpolator(new LinearInterpolator());
                            lAlphaAnimation3.addListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animator) {
                                    mLoadingIngLinearLayout.setVisibility(View.GONE);
                                    mViewHolderAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onAnimationCancel(Animator animator) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animator) {

                                }
                            });
                            lAlphaAnimation3.start();
                            break;
                    }

                }

                break;
        }

    }

    //更新加载更多的数据

    /**
     * @param list
     * @param flag true 替换  false不替换
     */
    public void updateMoreDataList(final List<T> list, final boolean flag, List<Object> clist) {

        //更新加载标识
        mIsLoading = false;
        int size = 0;

        log("隐藏加载中");
        hidLoadingIngFucntion();

        if (mStringList != null) {
            size = mStringList.size();
        } else {
            mStringList = new ArrayList<>();
        }

        if (flag) {
            mStringList = list;
        } else {
            if (list != null) {
                mStringList.addAll(list);
            }
        }


        if (clist == null || clist.size() == 0) {
            mCurrentUpLoadingStatue = RECYCLERVIEW_UP_LOADING_STATUE.LIST_IS_NULL;
        } else {
            mCurrentUpLoadingStatue = RECYCLERVIEW_UP_LOADING_STATUE.LIST_NOT_NULL;

        }

        if (mStringList == null || mStringList.size() == 0) {
            //显示无数据页面
            log("显示无数据页面");
            showLoadingNoDataFunction();

            return;
        } else {
            log("隐藏无数据页面");
            hidLoadingNoDataFunction();


        }


        final int finalSize1 = size;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                log("刷新页面 " + finalSize1);
                mViewHolderAdapter.notifyItemRemoved(finalSize1);
                mViewHolderAdapter.notifyItemChanged(finalSize1);


            }
        }, 600);


    }

    public <T> void addLoadingMoreDataList(final List<T> list) {
        int size = 0;
        if (mStringList != null) {
            size = mStringList.size();
        }
        //隐藏加载中 无数据页面
        hidLoadingIngFucntion();
        hidLoadingNoDataFunction();
        if (list == null || list.size() == 0) {
            final int finalSize = size;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //无数据标识
                    mCurrentUpLoadingStatue = RECYCLERVIEW_UP_LOADING_STATUE.LIST_IS_NULL;
                    //更新
                    mViewHolderAdapter.notifyItemChanged(finalSize);

                }
            }, 600);
        } else {
            final int finalSize = size;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //无数据标识
                    mCurrentUpLoadingStatue = RECYCLERVIEW_UP_LOADING_STATUE.LIST_NOT_NULL;
                    //更新
                    mViewHolderAdapter.notifyItemChanged(finalSize);

                }
            }, 600);
        }
    }

    public void setRecyclerviewHeight(int flag) {
        if (mRecyclerView != null) {

            int lHeightPixels = mContext.getResources().getDisplayMetrics().heightPixels;
            if (flag < 0 || flag > lHeightPixels) {
                flag = lHeightPixels;
            }

            ViewGroup.LayoutParams lLayoutParams = mRecyclerView.getLayoutParams();
            if (lLayoutParams != null) {
                lLayoutParams.height = flag;
            }
        }
    }

    public void setLoadingDataList(List<T> list) {
        setLoadingDataList(list, mCurrentShowDefaultType);
    }

    public void setLoadingDataList(List<T> list, SHOW_DEFAUTLE_PAGE_TYPE defautle_page_type) {


        mCurrentShowDefaultType = defautle_page_type;

        mIsLoading = false;

        //隐藏加载中
        //closePullRefresh();
        if (list == null) {
            list = new ArrayList<>();
        }

        log("recy mNetLoadingStatue " + mNetLoadingStatue);

        switch (mNetLoadingStatue) {
            case PULL_LOADING:
                //下拉
                //结束刷新状态
                closePullRefresh();

                if (list.size() == 0) {

                    //显示无数据页面
                    ObjectAnimator lAlphaAnimation = ObjectAnimator.ofFloat(mLoadingNoDataLinearLayout, "alpha", 0f, 1f);
                    lAlphaAnimation.setDuration(300);
                    lAlphaAnimation.setInterpolator(new LinearInterpolator());
                    lAlphaAnimation.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            mLoadingNoDataLinearLayout.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            mStringList.clear();
                            mViewHolderAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    lAlphaAnimation.start();
                } else {

                    //刷新
                    final List<T> finalList = list;
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mStringList.clear();
                            mViewHolderAdapter.notifyDataSetChanged();
                            mStringList.addAll(finalList);
                            mViewHolderAdapter.notifyDataSetChanged();
                        }
                    }, 400);
                }
                break;
            case UP_LOADING:
                //上拉
                int lSize = mStringList.size();
                mStringList.addAll(list);
                if (list.size() == 0) {
                    mCurrentUpLoadingStatue = RECYCLERVIEW_UP_LOADING_STATUE.LIST_IS_NULL;
                    if (lSize == 0) {
                        mViewHolderAdapter.notifyDataSetChanged();
                    } else {
                        mViewHolderAdapter.notifyItemChanged(lSize);
                    }
                } else {
                    mCurrentUpLoadingStatue = RECYCLERVIEW_UP_LOADING_STATUE.LIST_NOT_NULL;
                    if (lSize == 0) {
                        mViewHolderAdapter.notifyDataSetChanged();
                    } else {
                        mViewHolderAdapter.notifyItemChanged(lSize);
                    }
                }
                break;
            //没有
            case NO_LOADING:
                break;
            //
            case CLICK_NO_DATA:
                //无数据点击

                if (list.size() == 0) {
                    mStringList.clear();
                    mViewHolderAdapter.notifyDataSetChanged();
                    //隐藏加载中 显示无数据页面
                    final ObjectAnimator lAlphaAnimation = ObjectAnimator.ofFloat(mLoadingNoDataLinearLayout, "alpha", 0f, 1f);
                    lAlphaAnimation.setDuration(300);
                    lAlphaAnimation.setInterpolator(new LinearInterpolator());
                    lAlphaAnimation.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            mLoadingNoDataLinearLayout.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {

                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });

                    if (mClickNoDataHidIngAndShowNoDataAnimator != null) {
                        if (mClickNoDataHidIngAndShowNoDataAnimator.isRunning()) {
                            mClickNoDataHidIngAndShowNoDataAnimator.cancel();
                        }
                    }
                    mClickNoDataHidIngAndShowNoDataAnimator = ObjectAnimator.ofFloat(mLoadingIngLinearLayout, "alpha", 1f, 0f);
                    mClickNoDataHidIngAndShowNoDataAnimator.setDuration(300);
                    mClickNoDataHidIngAndShowNoDataAnimator.setInterpolator(new LinearInterpolator());
                    mClickNoDataHidIngAndShowNoDataAnimator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            mLoadingIngLinearLayout.setVisibility(View.GONE);
                            lAlphaAnimation.start();
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    mClickNoDataHidIngAndShowNoDataAnimator.start();

                } else {
                    //隐藏加载中 显示数据页面
                    if (mClickNoDataHidIngAndShowDataAnimator != null) {
                        if (mClickNoDataHidIngAndShowDataAnimator.isRunning()) {
                            mClickNoDataHidIngAndShowDataAnimator.cancel();
                            mClickNoDataHidIngAndShowDataAnimator = null;
                        }
                    }
                    mStringList.clear();
                    mViewHolderAdapter.notifyDataSetChanged();
                    mStringList.addAll(list);

                    mClickNoDataHidIngAndShowDataAnimator = ObjectAnimator.ofFloat(mLoadingIngLinearLayout, "alpha", 1f, 0f);
                    mClickNoDataHidIngAndShowDataAnimator.setDuration(300);
                    mClickNoDataHidIngAndShowDataAnimator.setInterpolator(new LinearInterpolator());
                    mClickNoDataHidIngAndShowDataAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            Float value = (Float) valueAnimator.getAnimatedValue();
                            mLoadingIngLinearLayout.setAlpha(value);
                            if (value == 0) {
                                if (mLoadingIngLinearLayout.getVisibility() == View.VISIBLE) {
                                    mLoadingIngLinearLayout.setVisibility(View.GONE);
                                    //刷新数据
                                    mViewHolderAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });

                    mClickNoDataHidIngAndShowDataAnimator.start();
                }
                break;
            case DEFAULT_LOADING:
                //第一次加载

                if (list.size() == 0) {
                    //隐藏加载中 显示无数据页面
                    final ObjectAnimator lAlphaAnimation = ObjectAnimator.ofFloat(mLoadingNoDataLinearLayout, "alpha", 0f, 1f);
                    lAlphaAnimation.setDuration(300);
                    lAlphaAnimation.setInterpolator(new LinearInterpolator());
                    lAlphaAnimation.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            mLoadingNoDataLinearLayout.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            mStringList.clear();
                            mViewHolderAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });

                    ObjectAnimator lAlphaAnimation2 = ObjectAnimator.ofFloat(mLoadingIngLinearLayout, "alpha", 1f, 0f);
                    lAlphaAnimation2.setDuration(300);
                    lAlphaAnimation2.setInterpolator(new LinearInterpolator());
                    lAlphaAnimation2.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            mLoadingIngLinearLayout.setVisibility(View.GONE);
                            lAlphaAnimation.start();
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    lAlphaAnimation2.start();
                } else {
                    switch (mCurrentShowDefaultType) {
                        case NO_DATA:
                            if (mLoadingIngLinearLayout.getVisibility() == View.VISIBLE) {
                                mLoadingIngLinearLayout.setVisibility(View.GONE);
                            }
                            //隐藏无数据页面 显示数据
                            ObjectAnimator lAlphaAnimation2 = ObjectAnimator.ofFloat(mLoadingNoDataLinearLayout, "alpha", 1f, 0f);
                            lAlphaAnimation2.setDuration(300);
                            lAlphaAnimation2.setInterpolator(new LinearInterpolator());
                            final List<T> finalList1 = list;
                            lAlphaAnimation2.addListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animator) {
                                    mLoadingNoDataLinearLayout.setVisibility(View.GONE);
                                    mStringList.clear();
                                    mViewHolderAdapter.notifyDataSetChanged();
                                    mStringList.addAll(finalList1);
                                    mViewHolderAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onAnimationCancel(Animator animator) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animator) {

                                }
                            });
                            lAlphaAnimation2.start();
                            break;
                        case LOADING:
                        case NORMAL:
                            if (mLoadingNoDataLinearLayout.getVisibility() == View.VISIBLE) {
                                mLoadingNoDataLinearLayout.setVisibility(View.GONE);
                            }
                            //隐藏加载中 显示数据
                            ObjectAnimator lAlphaAnimation3 = ObjectAnimator.ofFloat(mLoadingIngLinearLayout, "alpha", 1f, 0f);
                            lAlphaAnimation3.setDuration(300);
                            lAlphaAnimation3.setInterpolator(new LinearInterpolator());
                            final List<T> finalList2 = list;
                            lAlphaAnimation3.addListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animator) {
                                    mLoadingIngLinearLayout.setVisibility(View.GONE);
                                    mStringList.clear();
                                    mViewHolderAdapter.notifyDataSetChanged();
                                    mStringList.addAll(finalList2);
                                    mViewHolderAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onAnimationCancel(Animator animator) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animator) {

                                }
                            });
                            lAlphaAnimation3.start();
                            break;
                    }

                }

                break;
        }

    }

    //--------------------------------------------------------------------------------------------------
    //上拉加载更多显示文字颜色
    private int mLoadingMoreTextColor = Color.WHITE;

    public void setUpLoadingMoreTextColorFunction(int textColor) {
        mLoadingMoreTextColor = textColor;
    }

    //上拉加载更多显示背景颜色
    private int mLoadingMoreBackGroundColor = Color.BLACK;

    public void setUpLoadingMoreBackGroundColor(int color) {
        mLoadingMoreBackGroundColor = color;
    }
    //--------------------------------------------------------------------------------------------------


    //--------------------------------------------------------------------------------------------------
    //下拉刷新显示文字颜色
    private int mPullRefshTextColor = Color.WHITE;

    public void setPullRefshTextColorFunction(int textColor) {
        mLoadingMoreTextColor = textColor;
        if (mPullRefTextView != null) {
            mPullRefTextView.setTextColor(textColor);
        }
    }

    //下拉刷新显示背景颜色
    private int mPullRefshBackGroundColor = Color.BLACK;

    public void setPullRefshBackGroundColor(int color) {
        mLoadingMoreBackGroundColor = color;
        if (mPullRefshLayout != null) {
            mPullRefshLayout.setBackgroundColor(color);
        }
    }

    public void setPullRefshBackGroundDrable(Drawable drawable) {
        if (mPullRefshLayout != null) {
            mPullRefshLayout.setBackground(drawable);
        }
    }

    //下拉刷新显示的图片
    private Drawable mPullRefshDrawable = null;

    public void setPullRefshImageFunction(Drawable drawable) {
        mPullRefshDrawable = drawable;
        if (mPullRefImageView != null) {
            mPullRefImageView.setImageDrawable(drawable);
            Drawable drawable1 = mPullRefImageView.getDrawable();
            if (drawable1 != null) {
                if (drawable1 instanceof AnimationDrawable) {
                    ((AnimationDrawable) drawable1).start();
                }
            }
        }

    }

    private PULLREFSH_SHOW_VIEW_STATUE mCurrentPullRefshStatue = PULLREFSH_SHOW_VIEW_STATUE.PB_AND_TV;

    //下拉刷新模式
    public enum PULLREFSH_SHOW_VIEW_STATUE {
        PB_AND_TV,//显示 加载进度显示文字
        IV_AND_TV,//显示图片 加载进度显示文字
        PB,//只显示加载进度圈
        TV,//只显示加载文字
        IV//只显示加载图片
    }

    /**
     * @param statue PB_AND_TV,//显示 加载进度显示文字
     *               IV_AND_TV,//显示图片 加载进度显示文字
     *               PB,//只显示加载进度圈
     *               TV,//只显示加载文字
     *               IV//只显示加载图片
     * @see PULLREFSH_SHOW_VIEW_STATUE
     */
    public void setPullRefshStatue(PULLREFSH_SHOW_VIEW_STATUE statue) {
        switch (statue) {
            case IV:
                if (mPullRefTextView != null) {
                    mPullRefTextView.setVisibility(View.GONE);
                }
                if (mPullRefProgressBar != null) {
                    mPullRefProgressBar.setVisibility(View.GONE);
                }

                if (mPullRefImageView != null) {
                    mPullRefImageView.setVisibility(View.VISIBLE);
                }
                break;
            case PB:
                if (mPullRefTextView != null) {
                    mPullRefTextView.setVisibility(View.GONE);
                }
                if (mPullRefProgressBar != null) {
                    mPullRefProgressBar.setVisibility(View.VISIBLE);
                }
                if (mPullRefImageView != null) {
                    mPullRefImageView.setVisibility(View.GONE);
                }
                break;
            case TV:
                if (mPullRefTextView != null) {
                    mPullRefTextView.setVisibility(View.VISIBLE);
                }
                if (mPullRefProgressBar != null) {
                    mPullRefProgressBar.setVisibility(View.GONE);
                }
                if (mPullRefImageView != null) {
                    mPullRefImageView.setVisibility(View.GONE);
                }
                break;
            case PB_AND_TV:
                if (mPullRefProgressBar != null) {
                    mPullRefProgressBar.setVisibility(View.VISIBLE);
                }
                if (mPullRefTextView != null) {
                    mPullRefTextView.setVisibility(View.VISIBLE);
                }
                if (mPullRefImageView != null) {
                    mPullRefImageView.setVisibility(View.GONE);
                }
                break;
            case IV_AND_TV:
                if (mPullRefProgressBar != null) {
                    mPullRefProgressBar.setVisibility(View.GONE);
                }
                if (mPullRefTextView != null) {
                    mPullRefTextView.setVisibility(View.VISIBLE);
                }
                if (mPullRefImageView != null) {
                    mPullRefImageView.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
    //--------------------------------------------------------------------------------------------------

    //设置主背景的颜色
    public void setMainBackgroundRelativeLayoutColor(int color) {
        if (mMainBackgroundRelativeLayout != null) {
            mMainBackgroundRelativeLayout.setBackgroundColor(color);
        }
    }

    public void addMainBackgroundChildLayout(int layoutId) {
        if (mMainBackgroundRelativeLayout != null) {
            mMainBackgroundRelativeLayout.removeAllViews();
            View childView = View.inflate(mContext, layoutId, null);
            mMainBackgroundRelativeLayout.addView(childView);
        }
    }

    public void addMainBackgroundChildLayout(View childView) {
        if (mMainBackgroundRelativeLayout != null) {
            mMainBackgroundRelativeLayout.removeAllViews();
            mMainBackgroundRelativeLayout.addView(childView);
        }
    }

    public void setMainBackgroundRelativeLayoutDrawble(Drawable drawble) {
        if (mMainBackgroundRelativeLayout != null) {
            mMainBackgroundRelativeLayout.setBackgroundDrawable(drawble);
        }
    }

    public LinearLayout getMainBackgroundRelativeLayoutDrawble() {
        return mMainBackgroundRelativeLayout;
    }
    //--------------------------------------------------------------------------------------------------
    //加载无数据相关设置

    //显示加载无数数据内容
    public void showLoadingNoDataFunction() {

        if (mLoadingNoDataLinearLayout != null) {

            if (mLoadingIngLinearLayout.getVisibility() == View.VISIBLE) {
                //先隐藏加载中
                //再显示无数据

                final ObjectAnimator lAlphaAnimation2 = ObjectAnimator.ofFloat(mLoadingNoDataLinearLayout, "alpha", 0f, 1f);
                lAlphaAnimation2.setDuration(400);
                lAlphaAnimation2.setInterpolator(new LinearInterpolator());
                lAlphaAnimation2.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        mLoadingNoDataLinearLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        mLoadingNoDataLinearLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });

                ObjectAnimator lAlphaAnimation = ObjectAnimator.ofFloat(mLoadingIngLinearLayout, "alpha", 10f, 0);
                lAlphaAnimation.setDuration(400);
                lAlphaAnimation.setInterpolator(new LinearInterpolator());
                lAlphaAnimation.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        mLoadingIngLinearLayout.setVisibility(View.GONE);
                        lAlphaAnimation2.start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                lAlphaAnimation.start();


            } else {

                Log.d("hid", "隐藏加载中 2");
                ObjectAnimator lAlphaAnimation = ObjectAnimator.ofFloat(mLoadingNoDataLinearLayout, "alpha", 0f, 1f);
                lAlphaAnimation.setDuration(400);
                lAlphaAnimation.setInterpolator(new LinearInterpolator());
                lAlphaAnimation.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        mLoadingNoDataLinearLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        mLoadingNoDataLinearLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                lAlphaAnimation.start();
            }
        }
    }

    //隐藏加载无数数据内容
    public void hidLoadingNoDataFunction() {
        if (mLoadingNoDataLinearLayout != null && mLoadingNoDataLinearLayout.getVisibility() == View.VISIBLE && mLoadingNoDataLinearLayout.getAlpha() == 1) {
            Log.d("hid", "隐藏加载中 2");
            ObjectAnimator lAlphaAnimation = ObjectAnimator.ofFloat(mLoadingNoDataLinearLayout, "alpha", 1f, 0f);
            lAlphaAnimation.setDuration(400);
            lAlphaAnimation.setInterpolator(new LinearInterpolator());
            lAlphaAnimation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    mLoadingNoDataLinearLayout.setAlpha(1f);
                    mLoadingNoDataLinearLayout.setVisibility(View.GONE);

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            lAlphaAnimation.start();
        }
    }

    //点击加载数据事件
    public View.OnClickListener mNoDataOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //隐藏加载无数据
            //显示加载中
            final ObjectAnimator lAlphaAnimation1 = ObjectAnimator.ofFloat(mLoadingIngLinearLayout, "alpha", 0f, 1f);
            lAlphaAnimation1.setDuration(400);
            lAlphaAnimation1.setInterpolator(new LinearInterpolator());
            lAlphaAnimation1.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    mLoadingIngLinearLayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    //更新标识
                    mNetLoadingStatue = NETLOADINGSTATE.CLICK_NO_DATA;
                    //回调函数
                    if (mPullRecyclerViewLinserner != null) {
                        mPullRecyclerViewLinserner.loadMoreData();
                    }

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            ObjectAnimator lAlphaAnimation = ObjectAnimator.ofFloat(mLoadingNoDataLinearLayout, "alpha", 1f, 0f);
            lAlphaAnimation.setDuration(200);
            lAlphaAnimation.setInterpolator(new LinearInterpolator());
            lAlphaAnimation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    mLoadingNoDataLinearLayout.setVisibility(View.GONE);
                    lAlphaAnimation1.start();
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            lAlphaAnimation.start();

        }
    };

    //设置加载无数据文字 颜色
    public void setLoadingNoDataTextColor(int color) {
        if (mLoadingNoDataTextView != null) {
            mLoadingNoDataTextView.setTextColor(color);
        }
    }

    //设置加载无数据文字
    public void setLoadingNoDataText(String msg) {
        if (mLoadingNoDataTextView != null) {
            if (!TextUtils.isEmpty(msg)) {
                mLoadingNoDataTextView.setText(msg);
            }

        }
    }


    //--------------------------------------------------------------------------------------------------
    //加载中相关设置


    //显示加载中
    public void showLoadingIngFucntion() {
        Log.d("hid", "显示加载中 1");
        if (mLoadingIngLinearLayout != null && mLoadingIngLinearLayout.getVisibility() == View.GONE) {
            Log.d("hid", "隐藏加载中 2");
            ObjectAnimator lAlphaAnimation = ObjectAnimator.ofFloat(mLoadingIngLinearLayout, "alpha", 0f, 1f);
            lAlphaAnimation.setDuration(400);
            lAlphaAnimation.setInterpolator(new LinearInterpolator());
            lAlphaAnimation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    mLoadingIngLinearLayout.setAlpha(0);
                    mLoadingIngLinearLayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    mLoadingIngLinearLayout.setVisibility(View.VISIBLE);
                    mLoadingIngLinearLayout.setAlpha(1f);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            lAlphaAnimation.start();
        }
    }

    //隐藏加载中
    public void hidLoadingIngFucntion() {
        Log.d("hid", "隐藏加载中 1");
        if (mLoadingIngLinearLayout != null && mLoadingIngLinearLayout.getAlpha() == 1) {
            Log.d("hid", "隐藏加载中 2");
            ObjectAnimator lAlphaAnimation = ObjectAnimator.ofFloat(mLoadingIngLinearLayout, "alpha", 1f, 0f);
            lAlphaAnimation.setDuration(400);
            lAlphaAnimation.setInterpolator(new LinearInterpolator());
            lAlphaAnimation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    mLoadingIngLinearLayout.setVisibility(View.GONE);
                    mLoadingIngLinearLayout.setAlpha(1f);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            lAlphaAnimation.start();
        }
    }

    //设置加载中文字颜色

    //----------------------------------------------------------------------------
    //设置索引相关
    public void setShowIndexBar(QuickIndexBar.OnLetterChangeListener listener) {
        if (mQuickIndexBar != null && mQuickIndexBar.getVisibility() == View.GONE) {
            AlphaAnimation lAlphaAnimation = new AlphaAnimation(0f, 1f);
            lAlphaAnimation.setDuration(600);
            lAlphaAnimation.setInterpolator(new LinearInterpolator());
            lAlphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mQuickIndexBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mQuickIndexBar.startAnimation(lAlphaAnimation);
            mQuickIndexBar.setOnLetterChangeListener(listener);
        }
    }

    public void setHidIndexBar() {
        if (mQuickIndexBar != null && mQuickIndexBar.getVisibility() == View.VISIBLE) {
            AlphaAnimation lAlphaAnimation = new AlphaAnimation(1f, 0f);
            lAlphaAnimation.setDuration(600);
            lAlphaAnimation.setInterpolator(new LinearInterpolator());
            lAlphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mQuickIndexBar.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mQuickIndexBar.startAnimation(lAlphaAnimation);
        }
    }

    //设置索引显示文字颜色
    public void setQuickIndexBarTextColor(int color) {
        if (mQuickIndexBar != null) {
            mQuickIndexBar.settextColor(color);
        }
    }

    //按下进文字显示颜色
    public void setQuickIndexBarPressTextColor(int color) {
        if (mQuickIndexBar != null) {
            mQuickIndexBar.setPressTextColor(color);
        }
    }


    public void setPullRecyclerViewOnItemClickLinserner(PullRecyclerViewOnItemClickLinserner linserner) {
        this.mPullRecyclerViewOnItemClickLinserner = linserner;
    }


    /**
     * 显示
     */
    public void setMainRecyclerviewShowFunction() {

        if (mMainRefreshView != null) {
            AlphaAnimation lAlphaAnimation = new AlphaAnimation(0f, 1f);
            lAlphaAnimation.setDuration(400);
            lAlphaAnimation.setInterpolator(new LinearInterpolator());
            lAlphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mMainRefreshView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mLoadingIngLinearLayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mMainRefreshView.startAnimation(lAlphaAnimation);
        }

    }

    /**
     * 设置索引的颜色
     */
    public void setQuickIndexBarBackGroundColor(int transparent) {
        if (mQuickIndexBar != null) {
            mQuickIndexBar.setBackgroundColor(transparent);
        }
    }


    /**
     * 将recyclerview 滑动到指定的位置
     */
    public void setRecyclerViewScrollTo(int i) {
        if (mRecyclerView != null) {
            mRecyclerView.scrollToPosition(i);
        }
    }


    private OnPullRecyclerviewScrollLiserner mPullRecyclerLiserner;

    public interface OnPullRecyclerviewScrollLiserner {

        void onScrolled(RecyclerView recyclerView, int dx, int dy, LinearLayoutManager mLinearLayoutManager);
    }

    public void setPullScrollRecyclerLiserner(OnPullRecyclerviewScrollLiserner liserner) {
        mPullRecyclerLiserner = liserner;
    }


    /**
     * @param statue NORMAL,//正常状态下，没有下拉刷新也没有上拉加载更多
     *               PULL_REFRESH,//只有下拉刷新功能
     *               UP_LOAD_MORE,//只有上拉加载更多功能
     *               PULL_AND_UP//下拉刷新 上拉加载功能
     * @see RECYCLRYVIEW_STATUE
     */
    public void setRecyclerviewStatue(RECYCLRYVIEW_STATUE statue) {
        mCurrentStatue = statue;
        mRecyclerView.setOnScrollListener(mOnScrollListener);
        if ((mCurrentStatue == RECYCLRYVIEW_STATUE.PULL_REFRESH) || (mCurrentStatue == RECYCLRYVIEW_STATUE.PULL_AND_UP)) {
            //设置触摸
            mRecyclerView.setOnTouchListener(mOnTouchListener);
        } else {
            mRecyclerView.setOnTouchListener(null);
        }
    }
}
