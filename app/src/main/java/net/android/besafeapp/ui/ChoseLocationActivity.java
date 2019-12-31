package net.android.besafeapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiDetailInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.utils.DistanceUtil;

import net.android.besafeapp.R;
import net.android.besafeapp.adapter.SearchAddressAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ChoseLocationActivity extends AppCompatActivity implements OnGetPoiSearchResultListener {
    private static String TAG = "MainActivity";
    private Context mContext;
    private TextView tv_city;
    private EditText et_keyword;
    private ListView lv_searchAddress, lv_poiSearch;
    private LinearLayout ll_poiSearch, ll_mapView;

    //地图定位
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private MyLocationListener myListener = new MyLocationListener();
    public LocationClient mLocationClient = null;
    private LocationClientOption option = null;
    private boolean isFirstLocation = true;
    private LatLng currentLatLng;//当前所在位置
    private Marker marker;//地图标注

    //poi检索
    private GeoCoder mGeoCoder;//反向地理解析，获取周边poi
    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;//地点检索输入提示检索（Sug检索）
    private LatLng center;//地图中心点坐标
    private int radius = 300;//poi检索半径
    private int loadIndex = 0;//分页页码（分页功能我就不写了，常用的东西，你们自个加吧）
    private int pageSize = 50;
    private List<PoiInfo> poiInfoListForGeoCoder = new ArrayList<>();//地理反向解析获取的poi
    private List<PoiInfo> poiInfoListForSearch = new ArrayList<>();//检索结果集合
    private SearchAddressAdapter adapter_searchAddress;

    private String cityName = "";
    private String keyword = "";
    private LinearLayout liBack;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_location);
        mContext = ChoseLocationActivity.this;
        liBack = findViewById(R.id.li_back);
        tvTitle = findViewById(R.id.li_title);
        liBack.setOnClickListener(v->finish());
        tvTitle.setText("选择位置");
        tv_city = findViewById(R.id.tv_city);
        et_keyword = findViewById(R.id.et_keyword);
        ll_mapView = findViewById(R.id.ll_mapView);
        ll_poiSearch = findViewById(R.id.ll_poiSearch);
        lv_searchAddress = findViewById(R.id.lv_searchAddress);
        lv_poiSearch = findViewById(R.id.lv_poiSearch);
        //监听输入框
        monitorEditTextChage();
        //初始化地理解析、建议搜索、poi搜索
        initGeoCoder();
        initSuggestionSearch();
        initPoiSearch();
        //初始化地图及定位
        initMap();
        initLocation();
        //监听地图事件（加载完成、拖动等）
        monitorMap();
    }

    private void monitorEditTextChage() {
        et_keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                keyword = editable.toString();
                if (keyword.length() <= 0) {
                    //当清空文本后展示地图，隐藏搜索结果
                    showMapView();
                    return;
                }
                /* 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新 */
                /* 由于我们需要滑动地图展示周边poi，所以就不用建议搜索列表来搜索poi了，搜索时直接利用城市和输入的关键字进行城市内检索poi */
//                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
//                        .keyword(keyword)
//                        .city(cityName));
                searchCityPoiAddress();
            }
        });
    }

    private void initMap() {
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
    }

    /**
     * 初始化定位相关
     */
    private void initLocation() {
        // 声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());

        // 利用LocationClientOption类配置定位SDK参数
        option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // 可选，设置定位模式，默认高精度  LocationMode.Hight_Accuracy：高精度； LocationMode. Battery_Saving：低功耗； LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
        // 可选，设置返回经纬度坐标类型，默认gcj02
        // gcj02：国测局坐标；
        // bd09ll：百度经纬度坐标；
        // bd09：百度墨卡托坐标；
        // 海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标

        option.setOpenGps(true);
        // 可选，设置是否使用gps，默认false
        // 使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
        // 可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(true);
        // 可选，定位SDK内部是一个service，并放到了独立进程。
        // 设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
        // 可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setEnableSimulateGps(false);
        // 可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        option.setIsNeedLocationDescribe(true);
        // 可选，是否需要位置描述信息，默认为不需要，即参数为false

        option.setIsNeedLocationPoiList(true);
        // 可选，是否需要周边POI信息，默认为不需要，即参数为false

        option.setIsNeedAddress(true);// 获取详细信息
        //设置扫描间隔
//        option.setScanSpan(10000);

        mLocationClient.setLocOption(option);
        // 注册监听函数
        mLocationClient.registerLocationListener(myListener);
        mLocationClient.start();
    }

    /**
     * 根据获取到的位置在地图上移动“我”的位置
     *
     * @param location
     */
    private void navigateTo(BDLocation location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        String address = location.getAddrStr();
        if (isFirstLocation) {
            currentLatLng = new LatLng(latitude, longitude);
            MapStatus.Builder builder = new MapStatus.Builder();
            MapStatus mapStatus = builder.target(currentLatLng).zoom(17.0f).build();
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory
                    .newMapStatus(mapStatus));
            isFirstLocation = false;

            //反向地理解析（含有poi列表）
            mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(currentLatLng));
        }
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        mBaiduMap.setMyLocationData(locationData);
    }

    /**
     * 监听当前位置
     */
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            Log.e(TAG, "当前“我”的位置：" + location.getAddrStr());
            if (location.getLocType() == BDLocation.TypeGpsLocation
                    || location.getLocType() == BDLocation.TypeNetWorkLocation) {
                navigateTo(location);
                cityName = location.getCity();
                tv_city.setText(cityName);
                Log.e(TAG, "当前定位城市：" + location.getCity());
            }
        }
    }

    /**
     * 监听地图事件（这里主要监听移动地图）
     */
    private void monitorMap() {
        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {

            @Override
            public void onMapLoaded() {
                // TODO Auto-generated method stub
                //地图加载完成
            }
        });
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {

            /**
             * 手势操作地图，设置地图状态等操作导致地图状态开始改变。
             * @param mapStatus 地图状态改变开始时的地图状态
             */
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
            }

            /** 因某种操作导致地图状态开始改变。
             * @param mapStatus 地图状态改变开始时的地图状态
             * @param i 取值有：
             * 1：用户手势触发导致的地图状态改变,比如双击、拖拽、滑动底图
             * 2：SDK导致的地图状态改变, 比如点击缩放控件、指南针图标
             * 3：开发者调用,导致的地图状态改变
             */
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
                Log.e(TAG, "地图状态改变开始时：" + i + "");
            }

            /**
             * 地图状态变化中
             * @param mapStatus 当前地图状态
             */
            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
                LatLng latlng = mBaiduMap.getMapStatus().target;
                double latitude = latlng.latitude;
                double longitude = latlng.longitude;
//                Log.e(TAG, "地图状态变化中：" + latitude + "," + longitude);
                addMarker(latlng);
            }

            /**
             * 地图状态改变结束
             * @param mapStatus 地图状态改变结束后的地图状态
             */
            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                center = mBaiduMap.getMapStatus().target;
//                Log.e(TAG, "地图状态改变结束后：" + center.latitude + "," + center.longitude);
                if (poiInfoListForGeoCoder != null) {
                    poiInfoListForGeoCoder.clear();
                }
                //反向地理解析（含有poi列表）
                mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(center));
            }
        });
    }

    /**
     * 添加marker
     *
     * @param point Marker坐标点
     */
    private void addMarker(LatLng point) {
        if (marker != null) {
//            marker.remove();
            marker.setPosition(point);
        } else {
            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.mipmap.location);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap);
            //在地图上添加Marker，并显示
            marker = (Marker) mBaiduMap.addOverlay(option);
        }
    }

    /**
     * 展示搜索的布局
     */
    private void showSeachView() {
        ll_mapView.setVisibility(View.GONE);
        ll_poiSearch.setVisibility(View.VISIBLE);
    }

    /**
     * 展示地图的布局
     */
    private void showMapView() {
        ll_mapView.setVisibility(View.VISIBLE);
        ll_poiSearch.setVisibility(View.GONE);
    }

    //-----------------------------------------反向地理解析，获取周边poi列表--------------------------------------------------

    /**
     * 反向地理解析,结果中含有poi信息，用于刚进入地图和移动地图时使用
     */
    private void initGeoCoder() {
        mGeoCoder = GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                if (poiInfoListForGeoCoder != null) {
                    poiInfoListForGeoCoder.clear();
                }
                if (reverseGeoCodeResult.error.equals(SearchResult.ERRORNO.NO_ERROR)) {
                    //获取城市
                    ReverseGeoCodeResult.AddressComponent addressComponent = reverseGeoCodeResult.getAddressDetail();
                    cityName = addressComponent.city;
                    tv_city.setText(cityName);
                    //获取poi列表
                    if (reverseGeoCodeResult.getPoiList() != null) {
                        poiInfoListForGeoCoder = reverseGeoCodeResult.getPoiList();
                    }
                } else {
                    Toast.makeText(mContext, "该位置范围内无信息", Toast.LENGTH_SHORT);
                }
                initGeoCoderListView();
            }
        });
    }

    //-----------------------------------------建议搜索（sug检索）------------------------------------------------------------------
    private void initSuggestionSearch() {
        // 初始化建议搜索模块，注册建议搜索事件监听(sug搜索)
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
            /**
             * 获取在线建议搜索结果，得到requestSuggestion返回的搜索结果
             * @param suggestionResult    Sug检索结果
             */
            @Override
            public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
                    Toast.makeText(mContext, "未找到结果", Toast.LENGTH_LONG).show();
                    return;
                }

                List<SuggestionResult.SuggestionInfo> sugList = suggestionResult.getAllSuggestions();
                for (SuggestionResult.SuggestionInfo info : sugList) {
                    if (info.key != null) {
                        Log.e(TAG, "搜索结果：" + info.toString());
                        Log.e(TAG, "key：" + info.key);
                        DecimalFormat df = new DecimalFormat("######0");
                        //用当前所在位置算出距离
                        String distance = df.format(DistanceUtil.getDistance(currentLatLng, info.pt));
                        Log.e(TAG, "距离：" + distance);
                    }
                }

            }
        });
    }

    //--------------------------------------------------poi检索---------------------------------------------------------------------
    private void initPoiSearch() {
        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
    }

    /**
     * 获取POI搜索结果，包括searchInCity，searchNearby，searchInBound返回的搜索结果
     *
     * @param poiResult Poi检索结果，包括城市检索，周边检索，区域检索
     */
    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiInfoListForSearch != null) {
            poiInfoListForSearch.clear();
        }
        if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(mContext, "未找到结果", Toast.LENGTH_LONG).show();
            initPoiSearchListView();
            return;
        }

        if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
            poiInfoListForSearch = poiResult.getAllPoi();
            showSeachView();
            initPoiSearchListView();
            return;
        }

        if (poiResult.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
            // 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
            String strInfo = "在";

            for (CityInfo cityInfo : poiResult.getSuggestCityList()) {
                strInfo += cityInfo.city;
                strInfo += ",";
            }

            strInfo += "找到结果";
            Toast.makeText(mContext, strInfo, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 获取POI详情搜索结果，得到searchPoiDetail返回的搜索结果
     * V5.2.0版本之后，该方法废弃，使用{@link #onGetPoiDetailResult(PoiDetailSearchResult)}代替
     *
     * @param poiDetailResult POI详情检索结果
     */
    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        if (poiDetailResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(mContext, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext,
                    poiDetailResult.getName() + ": " + poiDetailResult.getAddress(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {
        if (poiDetailSearchResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(mContext, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        } else {
            List<PoiDetailInfo> poiDetailInfoList = poiDetailSearchResult.getPoiDetailInfoList();
            if (null == poiDetailInfoList || poiDetailInfoList.isEmpty()) {
                Toast.makeText(mContext, "抱歉，检索结果为空", Toast.LENGTH_SHORT).show();
                return;
            }

            for (int i = 0; i < poiDetailInfoList.size(); i++) {
                PoiDetailInfo poiDetailInfo = poiDetailInfoList.get(i);
                if (null != poiDetailInfo) {
                    Toast.makeText(mContext,
                            poiDetailInfo.getName() + ": " + poiDetailInfo.getAddress(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    private void initGeoCoderListView() {
        adapter_searchAddress = new SearchAddressAdapter(poiInfoListForGeoCoder, mContext, currentLatLng);
        lv_searchAddress.setAdapter(adapter_searchAddress);
        lv_searchAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                i.putExtra("location", poiInfoListForGeoCoder.get(position).address);
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }

    private void initPoiSearchListView() {
        adapter_searchAddress = new SearchAddressAdapter(poiInfoListForSearch, mContext, currentLatLng);
        lv_poiSearch.setAdapter(adapter_searchAddress);
    }

    /**
     * 周边搜索
     */
    private void searchNearbyProcess(LatLng center) {
        //以定位点为中心，搜索半径以内的
        PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption()
                .keyword(keyword)
                .sortType(PoiSortType.distance_from_near_to_far)
                .location(center)
                .radius(radius)
                .pageCapacity(pageSize)
                .pageNum(loadIndex);

        mPoiSearch.searchNearby(nearbySearchOption);
    }


    /**
     * 响应城市内搜索
     */
    public void searchCityPoiAddress() {
        mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city(cityName)
                .keyword(keyword)//必填
                .pageCapacity(pageSize)
                .pageNum(loadIndex));//分页页码
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //重写返回键
            if (keyword.trim().length() > 0) {//如果输入框还有字，就返回到地图界面并清空输入框
                showMapView();
                et_keyword.setText("");
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        // 当不需要定位图层时关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        // 取消监听函数
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(myListener);
        }
        mPoiSearch.destroy();
        mSuggestionSearch.destroy();
        mGeoCoder.destroy();
    }
}
