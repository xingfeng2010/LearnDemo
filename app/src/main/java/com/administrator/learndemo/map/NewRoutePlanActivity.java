package com.administrator.learndemo.map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.administrator.learndemo.R;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.DriveWayView;
import com.amap.api.navi.view.NextTurnTipView;
import com.amap.api.navi.view.OverviewButtonView;
import com.amap.api.navi.view.RouteOverLay;
import com.amap.api.navi.view.TrafficProgressBar;
import com.amap.api.navi.view.ZoomInIntersectionView;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.autonavi.tbt.TrafficFacilityInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;

public class NewRoutePlanActivity extends AppCompatActivity implements AMapNaviListener, AMapNaviViewListener, View.OnClickListener, AMap.OnMyLocationChangeListener {

    public static final String KEY_POI_END = "end_search_target";
    public static final String KEY_POI_START = "start_search_target";
    public static final String KEY_EXTRA_SEARCH = "extra_key";
    private SearchPoi mStartSearchPoi;
    private SearchPoi mEndSearchPoi;
    private TextureMapView mMapView;

    public AMapLocationClient mlocationClient;
    public AMapLocationClientOption mLocationOption;

    private GeocodeSearch mGeocodeSearch;
    private LatLng mChooseLatLng;
    private Marker mAddPointMaker;
    private ArrayList<String> mWayPointStrings = new ArrayList<>();

    private float currentZoom = 14;
    private float maxZoom = 18;
    private float minZoom = 3;

    private AMapLocation mCurLocation;

    int strategyFlag = 0;
    private StrategyBean mStrategyBean;
    private List<LatLng> mSourthWestLatLngs = new ArrayList<>();
    private List<LatLng> mNorthEastLatLngs = new ArrayList<>();

    private List<NaviLatLng> startList = new ArrayList<NaviLatLng>();
    /**
     * 途径点坐标集合
     */
    private List<NaviLatLng> wayList = new ArrayList<NaviLatLng>();
    /**
     * 终点坐标集合［建议就一个终点］
     */
    private List<NaviLatLng> endList = new ArrayList<NaviLatLng>();


    /**
     * ======================================
     * ===================================
     * 新的测试
     */
    private static final String TAG = "innernav";
    protected AMapNaviView mAMapNaviView;//导航视图类
    private MapView routeMap;
    private AMap map;
    protected AMapNavi mAMapNavi;//导航对外控制类
    private InnerMapRouteAdapter routeAdapter;
    //private Tip endPosTip;
    private NaviLatLng startPoint;
    private NaviLatLng endPoint;
    private RelativeLayout box;
    private RelativeLayout mNaviSource;
    private ListView routeListView;
    private ImageView backImage;
    private MyLocationStyle myLocationStyle;
    private Location mLocation;
    protected NaviLatLng mEndLatlng = new NaviLatLng(29.751161, 106.641618);
    protected NaviLatLng mStartLatlng = new NaviLatLng(29.71935, 106.654584);
    protected final List<NaviLatLng> sList = new ArrayList<NaviLatLng>();
    protected final List<NaviLatLng> eList = new ArrayList<NaviLatLng>();
    private List<RouteOverLay> overLays;
    private List<Integer> routeIds;
    private List<AMapNaviPath> naviPaths;
    private EditText startText;
    private EditText endText;
    private Button naviStart;
    private TrafficProgressBar mTrafficProgressBar;
    private OverviewButtonView mOverviewButtonView;
    protected List<NaviLatLng> mWayPointList;
    // protected TTSController mTtsManager;
    private ImageView mStopNavi;
    private ImageView mStopNavi1;
    // private SpeechSynthesizer mTts;
    private NextTurnTipView mNextTurnTipView;
    private ZoomInIntersectionView mZoomInIntersectionView;
    private TextView mNextStreet;
    private TextView mRetainDistance;
    private TextView mRetainTime;
    private TextView mRestDistance;
    private NaviInfo mNaviInfo;
    private DriveWayView mDriveWayView;

    //    private MyHandler aHandler = new MyHandler(BaseActivity.this);
//    CountTimeThread countTimeThread;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }

    };

    AMapNaviViewOptions viewOptions;
    String array[] = {"直行车道", "左转车道", "左转或直行车道", "右转车道", "右转或这行车道", "左掉头车道", "左转或者右转车道"
            , " 左转或右转或直行车道", "右转掉头车道", "直行或左转掉头车道", "直行或右转掉头车道", "左转或左掉头车道", "右转或右掉头车道"
            , "无", "无", "不可以选择该车道"};
    String actions[] = {"直行", "左转", "左转或直行", "右转", "右转或这行", "左掉头", "左转或者右转", " 左转或右转或直行"
            , "直行或左转掉头"
            , "直行或右转掉头"
            , "左转或左掉头"
            , "右转或右掉头"
            , "无"
            , "无"
            , "不可以选择"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getBundleExtra(KEY_EXTRA_SEARCH);
        mEndSearchPoi = (SearchPoi) bundle.getSerializable(KEY_POI_END);
        mStartSearchPoi = (SearchPoi) bundle.getSerializable(KEY_POI_START);


        //  Intent intent=getIntent();
        //  endPosTip=(Tip) intent.getParcelableExtra("endPos");
        endPoint = new NaviLatLng(Double.parseDouble(mEndSearchPoi.getLatitude()), Double.parseDouble(mEndSearchPoi.getLongitude()));// 由搜索结果初始化终点
        routeMap = (MapView) findViewById(R.id.route_map);
        map = routeMap.getMap();//2D地图展示搜索路径
        map.moveCamera(CameraUpdateFactory.zoomTo(12));// 地图初始化缩放等级
        map.setPointToCenter(900, 150);//设置起点在屏幕位置

        box = (RelativeLayout) findViewById(R.id.innermap_nav_box);
        mNaviSource = (RelativeLayout) findViewById(R.id.navi_resouce);
        routeListView = (ListView) findViewById(R.id.innermap_nav_box_result);
        backImage = (ImageView) findViewById(R.id.innermap_nav_box_back);
        backImage.setOnClickListener(this);
        routeMap.onCreate(savedInstanceState);
        // 实例化语音引擎
//        mTtsManager = TTSController.getInstance(getApplicationContext());
//        mTtsManager.init();

        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        // mAMapNavi.addAMapNaviListener(mTtsManager);
        mAMapNavi.addAMapNaviListener(this);

        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);


        myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(5000);//设置连续定位模式下的定位间隔
        myLocationStyle.showMyLocation(true);
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//蓝点跟随模式
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);

        map.setMyLocationStyle(myLocationStyle);
        map.setOnMyLocationChangeListener(this);
        map.setMyLocationEnabled(true);
        routeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				ToastUtil.showToast(getApplicationContext(), "选择路径："+arg2, Toast.LENGTH_LONG);
                if (arg1 == null) {
                    mHandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            routeListView.performItemClick((View) routeListView.getChildAt(0), 0, 0);
                        }
                    }, 500);
                    return;
                }
                arg1.setSelected(true);
                InnerMapRouteAdapter.ViewHolder holder = (InnerMapRouteAdapter.ViewHolder) arg1.getTag();
                holder.titleTextView.setSelected(true);
                int count = 0;
                for (RouteOverLay overLay : overLays) {
                    if (count != arg2) {
                        overLay.setTransparency(0.3f);
                    } else {
                        overLay.setTransparency(1f);
                        mAMapNavi.selectRouteId(routeIds.get(arg2));
                    }
                    count++;
                }

            }
        });
        startText = (EditText) findViewById(R.id.innermap_nav_box_mypos);
        endText = (EditText) findViewById(R.id.innermap_nav_box_endpos);
//        startText.setImeOptions(EditorInfo.IME_FLAG_NO_FULLSCREEN | EditorInfo.IME_ACTION_SEARCH);
//        endText.setImeOptions(EditorInfo.IME_FLAG_NO_FULLSCREEN | EditorInfo.IME_ACTION_SEARCH);
        if (mEndSearchPoi.getAddrname() != null && !mEndSearchPoi.getAddrname().equals("")) {
            endText.setText(mEndSearchPoi.getAddrname());
        }
        startText.setText(R.string.rc_innermap_my_location);
        naviPaths = new ArrayList<>();
        overLays = new ArrayList<>();
        routeIds = new ArrayList<>();

        startText.setFocusable(false);
        endText.setFocusable(false);

        naviStart = (Button) findViewById(R.id.navi_start);
        naviStart.setOnClickListener(this);

//        mStopNavi=(ImageView)findViewById(R.id.navi_stop);
        mStopNavi1 = (ImageView) findViewById(R.id.navi_stop1);
        mStopNavi1.setOnClickListener(this);
//        mZoomInIntersectionView = (com.amap.api.navi.view.ZoomInIntersectionView) findViewById(R.id.myZoomInIntersectionView);//

//        startCountTimeThread();


        /*
         *  导航UI自定义
         */
        mOverviewButtonView = (OverviewButtonView) findViewById(R.id.myOverviewButtonView);

        viewOptions = mAMapNaviView.getViewOptions();
        setMapCustomStyleFile(this);//
        viewOptions.setLayoutVisible(false);

        mAMapNaviView.setViewOptions(viewOptions);

        //        viewOptions.setTrafficBarEnabled(false);
        mAMapNaviView.setLazyOverviewButtonView(mOverviewButtonView);
        mNextTurnTipView = (NextTurnTipView) findViewById(R.id.myDirectionView);//路口转向
        mAMapNaviView.setLazyNextTurnTipView(mNextTurnTipView);

        viewOptions.setCrossLocation(new Rect(10, 400, 390, 600), new Rect(20, 300, 20, 300));
        viewOptions.setAutoLockCar(true);
//        viewOptions.setModeCrossDisplayShow(false);
        mNextStreet = (TextView) findViewById(R.id.next_street);
        mRetainDistance = (TextView) findViewById(R.id.retain_distance);
        mRetainTime = (TextView) findViewById(R.id.rest_time);
        mRestDistance = (TextView) findViewById(R.id.rest_distance);
        mDriveWayView = (DriveWayView) findViewById(R.id.myDriveWayView);
        mAMapNaviView.setLazyDriveWayView(mDriveWayView);

        locali_do();
    }

    private void locali_do() {
        double la = Double.parseDouble(mStartSearchPoi.getLatitude());
        double lo = Double.parseDouble(mStartSearchPoi.getLongitude());
        currentZoom = 14;
        if (map != null) {
            map.clear();
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(la, lo), currentZoom));
        }
    }

    private void setMapCustomStyleFile(Context context) {

        String styleName = "mystyle_7.data";
        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        String filePath = null;
        try {
            inputStream = context.getAssets().open(styleName);
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);

            filePath = context.getFilesDir().getAbsolutePath();
            File file = new File(filePath + "/" + styleName);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            outputStream = new FileOutputStream(file);
            outputStream.write(b);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();

                if (outputStream != null)
                    outputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        viewOptions.setCustomMapStylePath(filePath + "/" + styleName);
        map.setCustomMapStylePath(filePath + "/" + styleName);
        // aMap.setCustomMapStylePath("/sdcard/mystyle.data");

        map.showMapText(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAMapNaviView.onResume();
        routeMap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAMapNaviView.onPause();
        routeMap.onPause();
        // mTtsManager.stopSpeaking();

//        仅仅是停止你当前在说的这句话，一会到新的路口还是会再说的
        // mTtsManager.stopSpeaking();
//
//        停止导航之后，会触及底层stop，然后就不会再有回调了，但是讯飞当前还是没有说完的半句话还是会说完
//        mAMapNavi.stopNavi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAMapNaviView.onDestroy();
        //since 1.6.0 不再在naviview destroy的时候自动执行AMapNavi.stopNavi();请自行执行
        mAMapNavi.stopNavi();
        mAMapNavi.destroy();
        routeMap.onDestroy();
//        mTts.stopSpeaking();

        //mTtsManager.destroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.navi_start:
                Log.d(TAG,"route selected:"+routeListView.getSelectedItemPosition());
//                mAMapNavi.startNavi(NaviType.GPS);
                box.setVisibility(View.GONE);
                mAMapNaviView.setVisibility(View.VISIBLE);
//                mTrafficProgressBar.setVisibility(View.VISIBLE);
//                mOverviewButtonView.setVisibility(View.VISIBLE);
                routeMap.setVisibility(View.GONE);
                mAMapNavi.startNavi(NaviType.EMULATOR);
                Log.d(TAG,"stop navi");
                mNaviSource.setVisibility(view.VISIBLE);
//                mZoomInIntersectionView.setVisibility(View.VISIBLE);
                break;
            case R.id.innermap_nav_box_back:
                finish();
                break;
            case R.id.navi_stop1:
                box.setVisibility(View.VISIBLE);
                mAMapNaviView.setVisibility(View.GONE);
                routeMap.setVisibility(View.VISIBLE);
                mAMapNavi.stopNavi();
                mNaviSource.setVisibility(view.GONE);
               // mTtsManager.destroy();
                //      mRetainTime.setVisibility(View.GONE);
//                mTts.stopSpeaking();
                break;

        }
    }

    @Override
    public void onMyLocationChange(Location location) {
        mLocation = location;
        if (naviPaths.size() == 0) {// 根据当前位置和搜索结果确定初始化路径
//            Log.d(TAG, "catch my location:" + arg0.getLatitude() + " : " + arg0.getLongitude());
            startPoint = new NaviLatLng(location.getLatitude(), location.getLongitude());
            queryNaviPath(startPoint, endPoint, null);
        }
    }

    private synchronized void queryNaviPath(NaviLatLng startP, NaviLatLng endP, List<NaviLatLng> passBy) {

        List<NaviLatLng> startList = new ArrayList<>();
        List<NaviLatLng> endList = new ArrayList<>();
        if (startP == null || endP == null) {
//            ToastUtil.showToast(getApplicationContext(), "导航路径获取失败", Toast.LENGTH_SHORT);
            return;
        }
        startList.add(startP);
        endList.add(endP);

        /**
         * 方法: int strategy=mAMapNavi.strategyConvert(congestion,
         * avoidhightspeed, cost, hightspeed, multipleroute); 参数:
         *
         * @congestion 躲避拥堵
         * @avoidhightspeed 不走高速
         * @cost 避免收费
         * @hightspeed 高速优先
         * @multipleroute 多路径
         *
         *                说明: 以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，
         *                如果为true则此策略会算出多条路线。 注意: 不走高速与高速优先不能同时为true
         *                高速优先与避免收费不能同时为true
         */
        int strategy = 0;
        try {
            strategy = mAMapNavi.strategyConvert(true, false, false, false, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAMapNavi.calculateDriveRoute(startList, endList, passBy, strategy);
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {
        mNaviInfo = naviInfo;
        mNextStreet.setText(mNaviInfo.getNextRoadName());
        Log.d(TAG, "123" + mNaviInfo.getPathRetainDistance());
//        Log.d(TAG,"1234"+AMapUtil.getFriendlyLength((int)mNaviInfo.getPathRetainDistance()));
//        mRestDistance.setText(AMapUtil.getFriendlyLength((int)mNaviInfo.getPathRetainDistance()));
//        mRetainDistance.setText(AMapUtil.getFriendlyLength((int)mNaviInfo.getCurStepRetainDistance()));
//        mRetainTime.setText(AMapUtil.getFriendlyTime(mNaviInfo.getPathRetainTime()));
    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] laneInfos, byte[] bytes, byte[] bytes1) {
        StringBuffer sb = new StringBuffer();
        sb.append("共" + laneInfos.length + "车道");
        for (int i = 0; i < laneInfos.length; i++) {
            AMapLaneInfo info = laneInfos[i];
            //当前车道可以选择的动作
            char background = info.getLaneTypeIdHexString().charAt(0);
            //当前用户要执行的动作
            char Recommend = info.getLaneTypeIdHexString().charAt(1);
            //根据文档中每个动作对应的枚举类型，显示对应的图片
            try {
                sb.append("，第" + (i + 1) + "车道为" + array[getHex(background)] + "，该车道可执行动作为" + actions[getHex(Recommend)]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static int getHex(char ch) throws Exception {
        if (ch >= '0' && ch <= '9')
            return (int) (ch - '0');
        if (ch >= 'a' && ch <= 'f')
            return (int) (ch - 'a' + 10);
        if (ch >= 'A' && ch <= 'F')
            return (int) (ch - 'A' + 10);
        throw new Exception("error param");
    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        overLays.clear();
        naviPaths.clear();
        routeIds.clear();
        HashMap<Integer, AMapNaviPath> result = mAMapNavi.getNaviPaths();
        Set<Integer> keys = result.keySet();
        map.clear();

        Iterator<Integer> keyIterator = keys.iterator();
        while (keyIterator.hasNext()) {
            int key = keyIterator.next();
            AMapNaviPath path = result.get(key);
            path.getWayPoint();

            RouteOverLay overlay = new RouteOverLay(map, path, getApplicationContext());
            overlay.setAMapNaviPath(path);
            overlay.addToMap();
            overlay.zoomToSpan();

            overLays.add(overlay);
            naviPaths.add(path);
            routeIds.add(key);

        }
        routeAdapter = new InnerMapRouteAdapter(getApplicationContext(), naviPaths);
        routeListView.setAdapter(routeAdapter);
        routeAdapter.notifyDataSetChanged();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                routeListView.performItemClick((View) routeListView.getChildAt(0), 0, 0);
            }
        }, 500);
    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void onNaviSetting() {

    }

    @Override
    public void onNaviCancel() {

    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }

    @Override
    public void onNaviMapMode(int i) {

    }

    @Override
    public void onNaviTurnClick() {

    }

    @Override
    public void onNextRoadClick() {

    }

    @Override
    public void onScanViewButtonClick() {

    }

    @Override
    public void onLockMap(boolean b) {

    }

    @Override
    public void onNaviViewLoaded() {

    }
}
