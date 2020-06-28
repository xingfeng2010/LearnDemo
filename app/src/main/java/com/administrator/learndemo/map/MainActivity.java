package com.administrator.learndemo.map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import com.administrator.learndemo.R;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.MyTrafficStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements LocationSource, AMap.OnMapClickListener, AMap.OnCameraChangeListener, AMapLocationListener, GeocodeSearch.OnGeocodeSearchListener {

    private TextureMapView mapView;
    private AMap aMap;
    private ListView mListView;
    private SearchAdapter mSearchAdapter;

    private EditText mMapSearchText;
    private LatLonPoint mCenterPoint = null;
    private PoiSearch.Query mPoiQuery;// Poi查询条件类
    private PoiSearch mPoiSearch;// POI搜索
    private List<SearchPoi> mPoiList = new ArrayList<>();

    private float currentZoom = 14;

    public AMapLocationClient mlocationClient;
    public AMapLocationClientOption mLocationOption;

    private double mCurLatitude = 0;
    private double mCurLongitude = 0;
    private AMapLocation mCurMapLocation;
    private Marker mCurPointMarker;
    private String markAddr;//选择的地点信息
    private Marker mChosePointMarker;
    private String mCurAddress, mCurDiscrebe;
    private String mCityName = "北京";

    private boolean isNearBy;

    private SearchPoi mStartPoi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = (TextureMapView) this.findViewById(R.id.map);
        mMapSearchText = (EditText) this.findViewById(R.id.map_search_text);
        mMapSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                doPoiSearch(charSequence.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mListView = (ListView) this.findViewById(R.id.search_list_view);
        mListView.setVisibility(View.INVISIBLE);
        mSearchAdapter = new SearchAdapter(mPoiList, this);
        mListView.setAdapter(mSearchAdapter);
        mSearchAdapter.setClickListener(new SearchAdapter.ClickListener() {

            @Override
            public void itemClick(int position) {
                startNavi(mPoiList.get(position));
            }
        });

        mapView.onCreate(savedInstanceState);

        startLocation();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkCallPhonePermission = this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
            }
        }

        setUpMap();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mlocationClient.stopLocation();//停止定位
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        //  mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //aMap.moveCamera(CameraUpdateFactory.newLatLngBounds());
        //mapView.onResume();
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            // 此方法必须重写
        }

        // 自定义系统定位蓝点
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.getUiSettings().setCompassEnabled(false);
        aMap.getUiSettings().setScaleControlsEnabled(true);
        aMap.getUiSettings().setZoomControlsEnabled(false);
//          aMap.getUiSettings().setLogoBottomMargin(-1000);
        MyTrafficStyle myTrafficStyle = new MyTrafficStyle();
        myTrafficStyle.setSmoothColor(getResources().getColor(R.color.smooth_color));
        myTrafficStyle.setCongestedColor(getResources().getColor(R.color.congested_color));
        myTrafficStyle.setSlowColor(getResources().getColor(R.color.slow_color));
        myTrafficStyle.setSeriousCongestedColor(getResources().getColor(R.color.serious_congested_color));
        aMap.setMyTrafficStyle(myTrafficStyle);

        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                // mNavi = AMapNavi.getInstance(MainActivity.this);
                aMap.setTrafficEnabled(true);

                //setMapCustomStyleFile(MainActivity.this);
            }
        });


        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        //设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.radiusFillColor(getResources().getColor(R.color.transparent));
        myLocationStyle.strokeColor(getResources().getColor(R.color.transparent));
//
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.transparent));
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(currentZoom));


        aMap.setOnMapClickListener(this);//添加map点击
        aMap.setOnCameraChangeListener(this);
        aMap.getUiSettings().setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);

        aMap.setTrafficEnabled(true);
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
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
    public void deactivate() {
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (mChosePointMarker != null) {
            mChosePointMarker.destroy();
        }
        mChosePointMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 1)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.navi))
                .position(latLng));
        mChosePointMarker.showInfoWindow();
        mChosePointMarker.setToTop();
    }

    private void setMapCustomStyleFile(Context context) {
        String styleName = "style_json.json";
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

        aMap.setCustomMapStylePath(filePath + "/" + styleName);
        aMap.setMapCustomEnable(true);
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
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            mCurMapLocation = aMapLocation;
            mCurLongitude = aMapLocation.getLongitude();
            mCurLatitude = aMapLocation.getLatitude();
            mCurAddress = aMapLocation.getAddress();
            mCurDiscrebe = aMapLocation.getProvince() + aMapLocation.getCity() + aMapLocation.getDistrict();
//            myAddr = aMapLocation.getAddress() + "," + aMapLocation.getLatitude() + "," + aMapLocation.getLongitude();
//            city = aMapLocation.getCity();
            mCityName = aMapLocation.getCity();

            Log.i("lishixing", "setLocationOption mCityName:" + mCityName);
            mCenterPoint = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            mStartPoi = new SearchPoi(mCurLatitude + "", mCurLongitude + "", mCurAddress);

            setLocationOption();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    private void setLocationOption() {
        Log.i("lishixing", "setLocationOption 1111:");
        //setMap2SelectMaker();
        currentZoom = 14;
        mapView.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurLatitude, mCurLongitude), currentZoom));
        markAddr = mCurAddress + "," + mCurMapLocation.getLatitude() + "," + mCurMapLocation.getLongitude();
//        mGeoDescribe.setText(aMapLocation.getAddress());
//        mGeoBuildings.setText("我的位置");
//        mDistanceView.setText("");
//        mSearchPoi=null;
    }

    private void setMap2SelectMaker() {
        if (mChosePointMarker != null) {
            mChosePointMarker.destroy();
        }

        if (mCurPointMarker == null) {
            mCurPointMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.navi))
                    .position(new LatLng(mCurMapLocation.getLatitude(), mCurMapLocation.getLongitude())));
        } else {
            mCurPointMarker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.navi));
            mCurPointMarker.setPosition(new LatLng(mCurMapLocation.getLatitude(), mCurMapLocation.getLongitude()));
            mCurPointMarker.setRotateAngle(0);

        }
    }

    private void showSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    private void doPoiSearch(String newText) {
        if (newText.length() > 0) {
            if (mCityName == null || mCenterPoint == null) {
                return;
            }
            mListView.setVisibility(View.VISIBLE);
            SearchPoi searchPoi = new SearchPoi("0", "0", newText);
            searchPoi.setType(SearchPoi.SEARCH);
            if (mCenterPoint != null) {
                mPoiQuery = new PoiSearch.Query(newText, "", mCityName);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
                mPoiQuery.setPageSize(20);
                mPoiQuery.setCityLimit(false);
                mPoiSearch = new PoiSearch(MainActivity.this, mPoiQuery);
                mPoiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
                    /*** 附近的加油站/银行搜索*/
                    @Override
                    public void onPoiSearched(PoiResult result, int rCode) {
//                    search_progress.setVisibility(View.INVISIBLE);
//                    img_delete.setVisibility(View.VISIBLE);
//                    isNearBy = false;

                        if (rCode == 1000) {
                            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                                // 取得搜索到的poiitems有多少页
                                List<PoiItem> poiItems = result.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                                if (poiItems != null && poiItems.size() > 0) {
                                    SearchPoi searchPoi;
                                    mPoiList.clear();
                                    for (int i = 0; i < poiItems.size(); i++) {
                                        PoiItem poiItem = poiItems.get(i);
                                        searchPoi = new SearchPoi();
                                        searchPoi.setAddrname(poiItem.getTitle());
                                        searchPoi.setDistrict(poiItem.getDirection());
                                        searchPoi.setLatitude(poiItem.getLatLonPoint().getLatitude() + "");
                                        searchPoi.setLongitude(poiItem.getLatLonPoint().getLongitude() + "");
                                        searchPoi.setDistrict(poiItem.getSnippet());
                                        searchPoi.setDistance(poiItem.getDistance());
                                        mPoiList.add(searchPoi);

                                    }
                                    mSearchAdapter.notifyDataSetChanged();
                                } else {
                                    mPoiList.clear();
                                    mSearchAdapter.notifyDataSetChanged();
                                }
                            }
                        }

                    }

                    @Override
                    public void onPoiItemSearched(PoiItem poiItem, int i) {
                        String address = poiItem.getSnippet();
                    }
                });
                if (isNearBy) {
                    mPoiSearch.setBound(new PoiSearch.SearchBound(mCenterPoint, 3000, true));
                }
                // 设置搜索区域为以lp点为圆心，其周围2000米范围
                mPoiSearch.searchPOIAsyn();// 异步搜索
            }
        }
    }

    private void startNavi(SearchPoi searchPoi) {
        Intent intent = new Intent(MainActivity.this, NewRoutePlanActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(RoutePlanActivity.KEY_POI_END, searchPoi);
        bundle.putSerializable(RoutePlanActivity.KEY_POI_START, mStartPoi);
        intent.putExtra(RoutePlanActivity.KEY_EXTRA_SEARCH, bundle);
        MainActivity.this.startActivity(intent);
    }
}
