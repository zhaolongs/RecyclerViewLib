# RecyclerViewApplication
Android RecyclerView的上拉加载更多，下拉刷新组件化封装
**** ****
### pullrefreshrecyclerylib  开发文档
<hr/>
GitHub项目地址 <a href="#">点击查看详情</a>
<hr/>

##### 1 工程添加依赖 

```
compile project(':pullrefreshrecyclerylib')
```


##### 2 初始化基本使用
效果样式 一 无上拉加载更多 也无下拉刷新功能
<img src="http://orqv8r94o.bkt.clouddn.com/android_pull_recycler_2.gif"/>
<br/><br/>
效果样式 二 有上拉加载更多 有下拉刷新功能
<img src="http://orqv8r94o.bkt.clouddn.com/android_pull_recycler_1.gif"/>
<br/><br/>
效果样式 三 无上拉加载更多 有下拉刷新功能
<img src="http://orqv8r94o.bkt.clouddn.com/android_pull_recycler_3.gif"/>
<br/><br/>
效果样式 四 有上拉加载更多 无下拉刷新功能
<img src="http://orqv8r94o.bkt.clouddn.com/android_pull_recycler_4.gif"/>

###### 2.1 创建RecyclerView并设置数据



```

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
        //----
        //5将recyclerview添加到当前显示的页面中
        maintContentLinearLayyout.addView(relativeLayout);


        //模拟网络
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                List<DataModel> list = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    DataModel dataModel = new DataModel();
                    dataModel.name = "小燕子的情怀" + i;
                    list.add(dataModel);
                }
                //更新数据
                //这里需要注意的是 每次设置的数据集合都是一个新的List对象
                pullRecyclerViewUtils.setLoadingDataList(list);
            }
        }, 3000);
```

###### 2.2 回调监听

```java

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
                    }, 3000);
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
                    }, 3000);
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
```


<hr/>
##### 3 设置多布局样式 
效果样式（例如这里设置了三种Item的样式）
<img src="http://orqv8r94o.bkt.clouddn.com/android_pull_recycler_5.gif"/>
###### 3.1 初始化设置 （与2中描述的基本一至）

在这里需要注意的是 ，初始化操作Recyclerview方式与2中描述的基本一至，重要的是在为RecyclerView设置数据的时候，要构造多布局样式数据模型PullRecyclerMoreStatueModel

```
/**
 * Created by androidlongs on 2017/8/21.
 * 站在顶峰，看世界
 * 落在谷底，思人生
 */

public class PullRecyclerMoreStatueModel implements Serializable {
    //数据模型 
    public Object model;
    //布局类型
    public int itemType=-1;
    //布局ID
    public int itemLayoutId=-1;
}

```

```

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
```

对应的回调设置数据中

```java
private PullRecyclerViewLinserner mPullRecclerLinserner = new

            PullRecyclerViewLinserner() {
                //当触发上拉加载更多时，回调此方法
                @Override
                public void loadMoreData() {
                   
                }

                //当触发下拉刷新数据时会回调此方法
                @Override
                public void loadingRefresDataFunction() {
                    //模拟网络请求
                
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
                        //对应就的模型数据
                        DataModel dataModel = (DataModel) pullRecyclerMoreStatueModel.model;
                        //设置数据
                        TextView nameTextView = itemView.findViewById(R.id.tv_item_name);
                        nameTextView.setText(dataModel.name);
                    } else if (itemType == 2) {
                        //对应的模型数据
                        DataModel dataModel = (DataModel) pullRecyclerMoreStatueModel.model;
                        //设置数据
                        TextView nameTextView = itemView.findViewById(R.id.tv_item_name);
                        nameTextView.setText(dataModel.name);
                    } else {
                        //对应的模型数据
                        DataModel dataModel = (DataModel) pullRecyclerMoreStatueModel.model;
                        //设置数据
                        TextView nameTextView = itemView.findViewById(R.id.tv_item_name);
                        nameTextView.setText(dataModel.name);
                    }


                }
            };
```


##### 4 设置多布局样式 
效果样式一（例如这里设置了蓝色的背景）
<img src="http://orqv8r94o.bkt.clouddn.com/android_pull_recycler_6.gif"/>
<br>
效果样式二（例如这里自定义了一个背景）
<img src="http://orqv8r94o.bkt.clouddn.com/android_pull_recycler_7.gif"/>
###### 4.1 初始化设置 （与2中描述的基本一至）
###### 4.2 设置刷新背景（纯色）

```java
pullRecyclerViewUtils.setMainBackgroundRelativeLayoutColor(Color.BLUE);
```

###### 4.3 设置刷新背景 （自定义视图）

在这里提供了两种设置方式 

```java
 //方式一 直接将自定义的背景样式xml 的ID设置
 pullRecyclerViewUtils.addMainBackgroundChildLayout(R.layout.item_refresh_bg_layout);
 
 //方式二 设置一个构造 好的View 
   View view = View.inflate(this,R.layout.item_refresh_bg_layout,null);
   pullRecyclerViewUtils.addMainBackgroundChildLayout(view);
```
###### 4.4 设置刷新部位背景 （纯色）
```java
  
    //下拉刷新部位的背景颜色 设置
    pullRecyclerViewUtils.setPullRefshBackGroundColor(Color.WHITE);
    //下拉刷新部位的显示文字的颜色
    pullRecyclerViewUtils.setPullRefshTextColorFunction(Color.BLUE);
```

<hr/>

##### 5 设置下拉刷新部分的样式 


```java
   /**
     * @param statue PB_AND_TV,//显示 加载进度显示文字
     *               IV_AND_TV,//显示图片 加载进度显示文字
     *               PB,//只显示加载进度圈
     *               TV,//只显示加载文字
     *               IV//只显示加载图片
     * @see PULLREFSH_SHOW_VIEW_STATUE
     */
```

##### 5.1 样式一  显示刷新圆形进度与文字

```java
    //下拉刷新部位的背景颜色 设置
    pullRecyclerViewUtils.setPullRefshBackGroundColor(Color.WHITE);
    //下拉刷新部位的显示文字的颜色
    pullRecyclerViewUtils.setPullRefshTextColorFunction(Color.BLUE);
    //设置下拉刷新样式类型
    pullRecyclerViewUtils.setPullRefshStatue(PullRecyclerViewUtils.PULLREFSH_SHOW_VIEW_STATUE.PB_AND_TV);
```
<img src="http://orqv8r94o.bkt.clouddn.com/android_pull_recycler_8.gif"/>


##### 5.2 样式二 只显示刷新文字

```java
//设置下拉刷新样式类型
pullRecyclerViewUtils.setPullRefshStatue(PullRecyclerViewUtils.PULLREFSH_SHOW_VIEW_STATUE.TV);
```
<img src="http://orqv8r94o.bkt.clouddn.com/android_pull_recycler_9.gif"/>


##### 5.3 样式三 只显示刷新圆形进度

```java
//设置下拉刷新样式类型
pullRecyclerViewUtils.setPullRefshStatue(PullRecyclerViewUtils.PULLREFSH_SHOW_VIEW_STATUE.PB);
```
<img src="http://orqv8r94o.bkt.clouddn.com/android_pull_recycler_10.gif"/>


##### 5.4 样式四 只显示自定义图片

```java
//设置下拉刷新显示图片
pullRecyclerViewUtils.setPullRefshImageFunction(this.getResources().getDrawable(R.drawable.home_table_topic_header));
//设置下拉刷新样式类型
pullRecyclerViewUtils.setPullRefshStatue(PullRecyclerViewUtils.PULLREFSH_SHOW_VIEW_STATUE.IV);
```
<img src="http://orqv8r94o.bkt.clouddn.com/android_pull_recycler_11.gif"/>


##### 5.5 样式五 只显示自定义帧动画

```java
//设置下拉刷新显示 动画文件
pullRecyclerViewUtils.setPullRefshImageFunction(this.getResources().getDrawable(R.drawable.dra_pull_anim));
//设置下拉刷新样式类型
pullRecyclerViewUtils.setPullRefshStatue(PullRecyclerViewUtils.PULLREFSH_SHOW_VIEW_STATUE.IV);
```
<img src="http://orqv8r94o.bkt.clouddn.com/android_pull_recycler_12.gif"/>

##### 5.6 样式六 显示自定义图片和文字

```java
//设置下拉刷新显示 动画文件
pullRecyclerViewUtils.setPullRefshImageFunction(this.getResources().getDrawable(R.drawable.dra_pull_anim));
//设置下拉刷新样式类型
pullRecyclerViewUtils.setPullRefshStatue(PullRecyclerViewUtils.PULLREFSH_SHOW_VIEW_STATUE.IV_AND_TV);
```
<img src="http://orqv8r94o.bkt.clouddn.com/android_pull_recycler_13.gif"/>




<hr/>
##### 6 设置首次进入页面后 无数据显示的样式

```java
public enum SHOW_DEFAUTLE_PAGE_TYPE {
   NO_DATA,//无数据
   LOADING,//加载中
   NORMAL//列表数据页面
}
```

##### 6.1 显示正在加载中的圆形进度与显示文字

```java
pullRecyclerViewUtils.setFirstDefaultPageType(PullRecyclerViewUtils.SHOW_DEFAUTLE_PAGE_TYPE.LOADING);
```
<img src="http://orqv8r94o.bkt.clouddn.com/android_pull_recycler_14.gif"/>

##### 6.2 显示暂无数据显示文字

```java
pullRecyclerViewUtils.setFirstDefaultPageType(PullRecyclerViewUtils.SHOW_DEFAUTLE_PAGE_TYPE.NO_DATA);
```
<img src="http://orqv8r94o.bkt.clouddn.com/android_pull_recycler_15.gif"/>

##### 6.3 显示空的列表页面

```java
pullRecyclerViewUtils.setFirstDefaultPageType(PullRecyclerViewUtils.SHOW_DEFAUTLE_PAGE_TYPE.NORMAL);
```
<img src="http://orqv8r94o.bkt.clouddn.com/android_pull_recycler_16.gif"/>

