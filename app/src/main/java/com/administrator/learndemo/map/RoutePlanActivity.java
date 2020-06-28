package com.administrator.learndemo.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.administrator.learndemo.R;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyTrafficStyle;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
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
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.autonavi.tbt.TrafficFacilityInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class RoutePlanActivity extends AppCompatActivity implements AMapLocationListener, AMapNaviListener, AMap.OnMapClickListener, AMap.OnMapLongClickListener,
        AMap.OnMarkerClickListener, GeocodeSearch.OnGeocodeSearchListener, AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter, AMap.OnCameraChangeListener {

    public static final String KEY_POI_END = "end_search_target";
    public static final String KEY_POI_START = "start_search_target";
    public static final String KEY_EXTRA_SEARCH = "extra_key";
    private SearchPoi mStartSearchPoi;
    private SearchPoi mEndSearchPoi;
    private TextureMapView mMapView;
    private AMap mAMap;

    public AMapLocationClient mlocationClient;
    public AMapLocationClientOption mLocationOption;

    private GeocodeSearch mGeocodeSearch;
    private LatLng mChooseLatLng;
    private Marker mAddPointMaker;
    private ArrayList<String> mWayPointStrings = new ArrayList<>();

    private float currentZoom = 14;
    private float maxZoom = 18;
    private float minZoom = 3;

    private NaviLatLng startPoint = null;
    private NaviLatLng endPoint = null;
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
     * 导航对象(单例)
     */
    private AMapNavi mAMapNavi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_plan);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getBundleExtra(KEY_EXTRA_SEARCH);
        mEndSearchPoi = (SearchPoi) bundle.getSerializable(KEY_POI_END);
        mStartSearchPoi = (SearchPoi) bundle.getSerializable(KEY_POI_START);

        startLocation();

        mMapView = (TextureMapView) this.findViewById(R.id.map);
        initMap(savedInstanceState);
        initPointData();
        initNavi();
    }


    private void initPointData() {
        if (mStartSearchPoi != null) {
            startPoint = new NaviLatLng(Double.parseDouble(mStartSearchPoi.getLatitude()), Double.parseDouble(mStartSearchPoi.getLongitude()));
        }

        if (mStartSearchPoi != null) {
            endPoint = new NaviLatLng(Double.parseDouble(mEndSearchPoi.getLatitude()), Double.parseDouble(mEndSearchPoi.getLongitude()));
        }
    }

    /**
     * 初始化AMap对象
     */
    private void initMap(Bundle savedInstanceState) {
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            mMapView.onCreate(savedInstanceState);
            MyTrafficStyle myTrafficStyle = new MyTrafficStyle();
            myTrafficStyle.setSlowColor(getResources().getColor(R.color.route_slow_color));
            myTrafficStyle.setSmoothColor(getResources().getColor(R.color.route_smooth_color));
            myTrafficStyle.setCongestedColor(getResources().getColor(R.color.route_congested_color));
            myTrafficStyle.setSeriousCongestedColor(getResources().getColor(R.color.route_serious_congested_color));
            mAMap.setMyTrafficStyle(myTrafficStyle);
            mAMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
                @Override
                public void onMapLoaded() {
                    mAMap.setTrafficEnabled(true);
                    mAMap.setMapTextZIndex(3);
                    mAMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
                        @Override
                        public void onTouch(MotionEvent motionEvent) {
                            // removeTimeCount();
                        }
                    });


                }
            });

            mAMap.getUiSettings().setZoomControlsEnabled(false);
            mAMap.setOnMapClickListener(this);
            mAMap.setOnMapLongClickListener(this);
            mAMap.setOnMarkerClickListener(this);
            mGeocodeSearch = new GeocodeSearch(this);
            mGeocodeSearch.setOnGeocodeSearchListener(this);
            mAMap.setOnInfoWindowClickListener(this);
            mAMap.setInfoWindowAdapter(this);


            moveCamera();
        }
    }

    /**
     * 导航初始化
     */
    private void initNavi() {
//        congestion=CacheUtils.getInstance(mContext).getBoolean(Utils.INTENT_NAME_AVOID_CONGESTION,false);
//        cost=CacheUtils.getInstance(mContext).getBoolean(Utils.INTENT_NAME_AVOID_COST,false);
//        avoidhightspeed=CacheUtils.getInstance(mContext).getBoolean(Utils.INTENT_NAME_AVOID_HIGHSPEED,false);
//        hightspeed=CacheUtils.getInstance(mContext).getBoolean(Utils.INTENT_NAME_PRIORITY_HIGHSPEED,false);
        mStrategyBean = new StrategyBean(true, false, true, false);
        startList.add(startPoint);
        endList.add(endPoint);
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);
        calculateDriveRoute();
    }

    /**
     * 驾车路径规划计算
     */
    private void calculateDriveRoute() {
        try {
            strategyFlag = mAMapNavi.strategyConvert(mStrategyBean.isCongestion(), mStrategyBean.isCost(), mStrategyBean.isAvoidhightspeed(), mStrategyBean.isHightspeed(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Thread() {
            @Override
            public void run() {
                mAMapNavi.calculateDriveRoute(startList, endList, wayList, strategyFlag);
            }
        }.start();

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        mCurLocation = aMapLocation;
    }

    private void moveCamera() {
//        double la = EcoApplication.getInstance().getLatitude();
//        double lo = EcoApplication.getInstance().getLongitude();
        currentZoom = 14;
        if (mAMap != null) {
            mAMap.clear();
            mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(mStartSearchPoi.getLatitude()), Double.parseDouble(mStartSearchPoi.getLongitude())), currentZoom));
        }
    }

    private void startLocation() {
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(getApplicationContext());
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setNeedAddress(true);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    @Override
    public View getInfoWindow(final Marker marker) {
        if (marker.getTitle().equals("-1")) {
            View view = LayoutInflater.from(RoutePlanActivity.this).inflate(R.layout.way_point_add, null);
            TextView textView = (TextView) view.findViewById(R.id.point_name);
            ImageView imageView = (ImageView) view.findViewById(R.id.point_add);
            textView.setText(marker.getSnippet());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (wayList.size() >= 3) {
                        return;

                    }
                    LatLng latLng = marker.getPosition();
                    wayList.add(new NaviLatLng(latLng.latitude, latLng.longitude));
                    mAddPointMaker.remove();
                    mAddPointMaker.destroy();
                    mAddPointMaker = null;
                    mWayPointStrings.add(marker.getSnippet());
                  //  mIsNewCalculate = true;
                    calculateDriveRoute();

                }
            });
            return view;
        } else if (marker.getTitle().equals("0") || marker.getTitle().equals("1") || marker.getTitle().equals("2")) {
            View view = LayoutInflater.from(RoutePlanActivity.this).inflate(R.layout.way_point_delete, null);
            TextView textView = (TextView) view.findViewById(R.id.point_name);
            ImageView imageView = (ImageView) view.findViewById(R.id.point_delete);
            textView.setText(marker.getSnippet());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LatLng latLng = marker.getPosition();
                    wayList.remove(new NaviLatLng(latLng.latitude, latLng.longitude));
                    mWayPointStrings.remove(Integer.valueOf(marker.getTitle()));
                 //   mIsNewCalculate = true;
                    calculateDriveRoute();
                }
            });
            return view;
        }

        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
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
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        HashMap<Integer, AMapNaviPath> paths = mAMapNavi.getNaviPaths();
        for (int i = 0; i < ints.length; i++) {
            AMapNaviPath path = paths.get(ints[i]);
            if (path != null) {
                drawRoutes(ints[i], path);
            }
        }
        setRouteLineTag(paths, ints);
        LatLng centerLatLng = mAMap.getCameraPosition().target;
        setMapToCenter();
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
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        //cleanRouteOverlay();
        mMapView.onDestroy();
        mAMapNavi.removeAMapNaviListener(this);
        mAMapNavi = null;
    }

    private LatLng getSourthWestLatlng() {
        if (mSourthWestLatLngs.size() <= 0) {
            return null;
        }

        double lat = mSourthWestLatLngs.get(0).latitude;
        double lng = mSourthWestLatLngs.get(0).longitude;
        for (int i = 0; i < mSourthWestLatLngs.size(); i++) {
            double curlat = mSourthWestLatLngs.get(i).latitude;
            if (curlat < lat) {
                lat = curlat;
            }
            double curLng = mSourthWestLatLngs.get(i).longitude;
            if (curLng < lng) {
                lng = curLng;
            }


        }
        return new LatLng(lat, lng);
    }

    private LatLng getNorthEastLatlng() {
        if (mNorthEastLatLngs.size() <= 0) {
            return null;
        }
        double lat = mNorthEastLatLngs.get(0).latitude;
        double lng = mNorthEastLatLngs.get(0).longitude;
        for (int i = 0; i < mNorthEastLatLngs.size(); i++) {
            double curlat = mNorthEastLatLngs.get(i).latitude;
            if (curlat > lat) {
                lat = curlat;
            }
            double curLng = mNorthEastLatLngs.get(i).longitude;
            if (curLng > lng) {
                lng = curLng;
            }
        }
        return new LatLng(lat, lng);
    }


    /**
     * 绘制路径规划结果
     *
     * @param routeId 路径规划线路ID
     * @param path    AMapNaviPath
     */

    private void drawRoutes(int routeId, AMapNaviPath path) {
        mAMap.moveCamera(CameraUpdateFactory.changeTilt(0));
        mNorthEastLatLngs.add(path.getBoundsForPath().northeast);
        mSourthWestLatLngs.add(path.getBoundsForPath().southwest);
        path.getWayPoint();
//        EcoRouteOverLay transRouteOverLay = new EcoRouteOverLay(mAMap, path, mContext);
//        transRouteOverLay.setWayPointStrings(mWayPointStrings);
//        RouteOverlayOptions transRouteOverlayOptions = new RouteOverlayOptions();
//        transRouteOverlayOptions.setArrowOnTrafficRoute(BitmapDescriptorFactory.fromResource(R.mipmap.navi_custtexture_aolr).getBitmap());
//        transRouteOverlayOptions.setNormalRoute(BitmapDescriptorFactory.fromResource(R.mipmap.navi_custtexture_transparent).getBitmap());
//        transRouteOverlayOptions.setUnknownTraffic(BitmapDescriptorFactory.fromResource(R.mipmap.navi_custtexture_no_transpararent).getBitmap());
//        transRouteOverlayOptions.setSmoothTraffic(BitmapDescriptorFactory.fromResource(R.mipmap.navi_custtexture_green_transparent).getBitmap());
//        transRouteOverlayOptions.setSlowTraffic(BitmapDescriptorFactory.fromResource(R.mipmap.navi_custtexture_slow_transparent).getBitmap());
//        transRouteOverlayOptions.setJamTraffic(BitmapDescriptorFactory.fromResource(R.mipmap.navi_custtexture_bad_transparent).getBitmap());
//        transRouteOverlayOptions.setVeryJamTraffic(BitmapDescriptorFactory.fromResource(R.mipmap.navi_custtexture_grayred_transparent).getBitmap());
//        transRouteOverlayOptions.setLineWidth(60f);
//        transRouteOverLay.setRouteOverlayOptions(transRouteOverlayOptions);
//        transRouteOverLay.setTrafficLine(true);
//        transRouteOverLay.setStartPointBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.startpoint1));
//        transRouteOverLay.setEndPointBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.endpoint1));
//        transRouteOverLay.addToMap();
//
//        transRouteOverlays.put(routeId, transRouteOverLay);
//        EcoRouteOverLay routeOverLay = new EcoRouteOverLay(mAMap, path, mContext);
//        routeOverLay.setWayPointStrings(mWayPointStrings);
//        RouteOverlayOptions routeOverlayOptions = new RouteOverlayOptions();
//        routeOverlayOptions.setArrowOnTrafficRoute(BitmapDescriptorFactory.fromResource(R.mipmap.navi_custtexture_aolr).getBitmap());
//        routeOverlayOptions.setNormalRoute(BitmapDescriptorFactory.fromResource(R.mipmap.navi_custtexture_transparent).getBitmap());
//        routeOverlayOptions.setUnknownTraffic(BitmapDescriptorFactory.fromResource(R.mipmap.navi_custtexture_no).getBitmap());
//        routeOverlayOptions.setSmoothTraffic(BitmapDescriptorFactory.fromResource(R.mipmap.navi_custtexture_green).getBitmap());
//        routeOverlayOptions.setSlowTraffic(BitmapDescriptorFactory.fromResource(R.mipmap.navi_custtexture_slow).getBitmap());
//        routeOverlayOptions.setJamTraffic(BitmapDescriptorFactory.fromResource(R.mipmap.navi_custtexture_bad).getBitmap());
//        routeOverlayOptions.setVeryJamTraffic(BitmapDescriptorFactory.fromResource(R.mipmap.navi_custtexture_grayred).getBitmap());
//        routeOverlayOptions.setLineWidth(60f);
//        routeOverLay.setRouteOverlayOptions(routeOverlayOptions);
//        routeOverLay.setTrafficLine(true);
//        routeOverLay.setStartPointBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.startpoint1));
//        routeOverLay.setEndPointBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.endpoint1));
//        routeOverLay.addToMap();
//        routeOverlays.put(routeId, routeOverLay);
    }


    /**
     * 路线tag选中设置
     *
     * @param lineOne
     * @param lineTwo
     * @param lineThree
     */
    private void focuseRouteLine(boolean lineOne, boolean lineTwo, boolean lineThree) {
        if (lineOne) {
//            setLinelayoutTwo(lineTwo);
//            setLinelayoutThree(lineThree);
//            setLinelayoutOne(lineOne);
        } else if (lineTwo) {
//
//            setLinelayoutOne(lineOne);
//            setLinelayoutThree(lineThree);
//            setLinelayoutTwo(lineTwo);

        } else {
//            setLinelayoutOne(lineOne);
//            setLinelayoutTwo(lineTwo);
//            setLinelayoutThree(lineThree);
        }
    }

    /**
     * @param paths 多路线回调路线
     * @param ints  多路线回调路线ID
     */
    private void setRouteLineTag(HashMap<Integer, AMapNaviPath> paths, int[] ints) {

    }


    private void setMapToCenter() {
        LatLng sourthWest = getSourthWestLatlng();
        LatLng northEast = getNorthEastLatlng();
        if (sourthWest != null && northEast != null) {
            mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(sourthWest, northEast), 150));
        }
    }
}
