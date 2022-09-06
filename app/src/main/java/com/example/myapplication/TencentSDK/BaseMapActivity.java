package com.example.myapplication.TencentSDK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.utils.SensorEventHelper;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.llw.demo.map.MapLifecycle;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.lbssearch.TencentSearch;
import com.tencent.lbssearch.httpresponse.HttpResponseListener;
import com.tencent.lbssearch.object.param.SearchParam;
import com.tencent.lbssearch.object.param.SuggestionParam;
import com.tencent.lbssearch.object.result.SearchResultObject;
import com.tencent.lbssearch.object.result.SuggestionResultObject;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.LocationSource;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMapInitializer;
import com.tencent.tencentmap.mapsdk.maps.UiSettings;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.Circle;
import com.tencent.tencentmap.mapsdk.maps.model.CircleOptions;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.MyLocationStyle;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BaseMapActivity extends AppCompatActivity implements LocationSource, TencentLocationListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener, View.OnFocusChangeListener, TencentMap.OnCameraChangeListener {
    //基础地图
    private MapView mapView;
    //腾讯地图
    private TencentMap tencentMap;
    //地图生命周期
    private MapLifecycle mapLifecycle;
    //定位管理
    private TencentLocationManager locationManager;
    //定位请求
    private TencentLocationRequest locationRequest;
    //定位数据源监听
    private LocationSource.OnLocationChangedListener locationChangedListener;
    private ImageView image,img1,img2,style1,style2,style3,img3;
    //权限
    private RxPermissions rxPermissions;
    private Toolbar toolbar;
    //传感器
    private SensorEventHelper mSensorHelper;
    private Marker marker;
    private SearchView mSearchView;
    /**
     * 是否能进行下一步操作
     */
    private boolean mIsEnableNext = true;
    /**
     * 是否运行使用搜索建议
     */
    private boolean mIsUseSug = true;
    private TencentSearch mTencentSearch;
    private RecyclerView mRecyclerView;
    private SearchPoiAdapter mSearchPoiAdapter;
    private List<PoiInfo> mPoiInfos;
    private Marker mPoiMarker;
    private Marker mMapCenterPointerMarker;
    private TimerTask mTimerTask;
    private Timer mTimer = new Timer();
    private Marker marker1;
    private Circle ac;
    private Circle c;
    private long start;
    private final Interpolator interpolator1 = new LinearInterpolator();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_map);
        initView();
        checkVersion();
        initLocation();
        ToolBar();
        initSensor();
    }

    private void initSensor() {
        mSensorHelper = new SensorEventHelper(this);
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        }
    }

    private void ToolBar() {
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("地图");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.inflateMenu(R.menu.menu_map);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.search:
                        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(BaseMapActivity.this);
                        View view1 = getLayoutInflater().inflate(R.layout.dialog_bottom_new, null);
                        mBottomSheetDialog.setContentView(view1);
                        mBottomSheetDialog.setCanceledOnTouchOutside(true);
                        mBottomSheetDialog.getWindow().setDimAmount(0f);
                        img1=view1.findViewById(R.id.img1);
                        img1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //标准地图
                                tencentMap.setTrafficEnabled(true);
                                tencentMap.setIndoorEnabled(true);
                                tencentMap.setMapType(TencentMap.MAP_TYPE_NORMAL);
                            }
                        });
                        img2=view1.findViewById(R.id.img2);
                        img2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //卫星地图
                                tencentMap.setTrafficEnabled(false);
                                tencentMap.setMapType(TencentMap.MAP_TYPE_SATELLITE);
                            }
                        });
                        img3=view1.findViewById(R.id.img3);
                        img3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //设置最小缩放等级 最大值 20 最小值 1
                                //tencentMap.setMinZoomLevel(18);
                                //启用3D视图
                                tencentMap.setIndoorEnabled(true);
                                tencentMap.setBuilding3dEffectEnable(true);
                                Log.d("3D","显示3d地图");

                            }
                        });
                        style1=view1.findViewById(R.id.style1);
                        style1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tencentMap.setMapStyle(1);
                            }
                        });
                        style2=view1.findViewById(R.id.style2);
                        style2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tencentMap.setMapStyle(2);
                            }
                        });
                        style3=view1.findViewById(R.id.style3);
                        style3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                tencentMap.setMapStyle(3);
                            }
                        });
                        mBottomSheetDialog.show();
                        break;
                    case R.id.menu_find_poi_search:
                        BottomSheetDialog mBottomSheetDialog1 = new BottomSheetDialog(BaseMapActivity.this);
                        View view2 = getLayoutInflater().inflate(R.layout.search_poi, null);
                        mBottomSheetDialog1.setContentView(view2);
                        mSearchView = (SearchView) view2.findViewById(R.id.search1);
                        mSearchView.setOnQueryTextListener(BaseMapActivity.this);
                        mSearchView.setOnCloseListener(BaseMapActivity.this);
                        mSearchView.setOnQueryTextFocusChangeListener(BaseMapActivity.this);
                        mRecyclerView = view2.findViewById(R.id.layout_recycle_container);
                        mRecyclerView.setLayoutManager(new LinearLayoutManager(BaseMapActivity.this));
                        mRecyclerView.setAdapter(mSearchPoiAdapter);
                        mBottomSheetDialog1.setCanceledOnTouchOutside(true);
                        mBottomSheetDialog1.getWindow().setDimAmount(0f);
                        mBottomSheetDialog1.show();
                        break;

                }
                return true;
            }
        });
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /***
     * 创建activity调用一次
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.find_poi, menu);
        return true;
    }

    /***
     * 更新menu
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_find_poi_search).setEnabled(mIsEnableNext);
        return super.onPrepareOptionsMenu(menu);
    }
    @Override

    public boolean onMenuOpened(int featureId, Menu menu) {

        if (menu != null) {

            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {

                try {

                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);

                    method.setAccessible(true);

                    method.invoke(menu, true);

                } catch (Exception e) {

                    e.printStackTrace();

                }

            }

        }

        return super.onMenuOpened(featureId, menu);

    }
    /**
         * 页面初始化
         */
        private void initView() {
            TencentMapInitializer.setAgreePrivacy(true);
            //地图
            mapView = findViewById(R.id.mapView);
            //创建tencentMap地图对象
            tencentMap = mapView.getMap();

            mapLifecycle = new MapLifecycle(mapView);
            //将观察者与被观察者绑定起来
            getLifecycle().addObserver(mapLifecycle);
            //image=findViewById(R.id.image);
            //image.setOnClickListener(this);
//            img1=findViewById(R.id.img1);
//            img2=findViewById(R.id.img2);
//            img1.setOnClickListener(this);
//            img2.setOnClickListener(this);
            //实例化
            /**
             * 腾讯地图提供了UiSettings类以方便开发者对地图手势及SDK提供的控件的控制，以定制自己想要的视图效果。
             * UiSettings类的实例化也是通过TencentMap来获取。
             */
            UiSettings mapUiSettings = tencentMap.getUiSettings();

            /**
             * 可以控制地图的缩放级别，每次点击改变1个级别，此控件默认打开，
             * 可以通过UiSettings.setZoomControlsEnabled(boolean)接口控制此控件的显示和隐藏。
             */
            //mapUiSettings.setZoomControlsEnabled(true);

            /**
             * 此控件可以指示地图的南北方向，默认的视图状态下不显示，只有在地图的偏航角或俯仰角不为0时才会显示，
             *  并且该控件的默认点击事件会将地图视图的俯仰角和偏航角动画到0的位置。
             *  可以通过UiSettings.setCompassEnabled(boolean)接口控制此控件的显示和隐藏。
             */
            //mapUiSettings.setCompassEnabled(true);

            /**
             * 当通过TencentMap.setLocationSource(locationSource)设置好地图的定位源后，
             * 点击此按钮可以在地图上标注一个蓝点指示用户的当前位置。
             * 可以通过UiSettings.setMyLocationButtonEnabled()接口设置此控件的显示和隐藏。
             */
            mapUiSettings.setMyLocationButtonEnabled(true);

            /**
             * 旋转手势
             */
            //mapUiSettings.setRotateGesturesEnabled(true);

            //tencentMap.setMyLocationEnabled(true);
            mSearchPoiAdapter = new SearchPoiAdapter(this);
            mPoiInfos = new ArrayList<>();
            mSearchPoiAdapter.submitList(mPoiInfos);
            mTencentSearch = new TencentSearch(this);
            tencentMap.setOnCameraChangeListener(this);
            rxPermissions = new RxPermissions(this);
        }
    /**
     * 初始化定位
     */
    private void initLocation() {
        //用于访问腾讯定位服务的类, 周期性向客户端提供位置更新
        locationManager = TencentLocationManager.getInstance(this);
        //设置坐标系
        locationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);
        //创建定位请求
        locationRequest = TencentLocationRequest.create();
        //设置定位周期（位置监听器回调周期）为3s
        locationRequest.setInterval(300000);

        //隐私策略
        locationManager.setUserAgreePrivacy(true);
        //位置信息的详细程度 REQUEST_LEVEL_ADMIN_AREA表示获取经纬度，位置所处的中国大陆行政区划
        locationRequest.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA);
        //是否允许使用GPS
        locationRequest.setAllowGPS(true);
        //是否需要获取传感器方向，提高室内定位的精确度
        locationRequest.setAllowDirection(true);
        //是否需要开启室内定位
        locationRequest.setIndoorLocationMode(true);
        MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        //地图样式
        //tencentMap.setMapType(TencentMap.MAP_TYPE_SATELLITE);
        //地图上设置定位数据源
        tencentMap.setMyLocationStyle(myLocationStyle);
        tencentMap.setLocationSource(this);
        //设置当前位置可见
        tencentMap.setMyLocationEnabled(true);
        //tencentMap.setOnTapMapViewInfoWindowHidden(true);
        tencentMap.setTrafficEnabled(true);
    }
    /**
     * 检查Android版本
     */
    private void checkVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //6.0或6.0以上
            //动态权限申请
            permissionsRequest();
        } else {
            showMsg("您不需要动态获得权限，可以直接定位");
        }
    }
    /**
     * 动态权限申请
     */
    private void permissionsRequest() {//使用这个框架使用了Lambda表达式，设置JDK版本为 1.8或者更高
        rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {//申请成功
                        //发起连续定位请求
                        showMsg("您已获得权限，可以定位了");
                    } else {//申请失败
                        showMsg("权限未开启");
                    }
                });
    }



    /**
     * mapview的生命周期管理
     */
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSensorHelper != null) {
            mSensorHelper.registerSensorListener();
        }
        mapView.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSensorHelper != null) {
            mSensorHelper.unRegisterSensorListener();
            mSensorHelper.setCurrentMarker(null);
            mSensorHelper = null;
        }
        mapView.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mapView.onRestart();
    }
    private Bitmap getBitMap(int resourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = 55;
        int newHeight = 55;
        float widthScale = ((float) newWidth) / width;
        float heightScale = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(widthScale, heightScale);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return bitmap;
    }
    /**
     * 接收定位结果
     */
    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int code, String reason) {
        if (tencentLocation != null && locationChangedListener != null) {
            if (mTimerTask != null) {
                mTimerTask.cancel();
                mTimerTask = null;
            }
            if (code == TencentLocation.ERROR_OK && locationChangedListener != null) {

                //重新构建一个定位对象
                Location location = new Location(tencentLocation.getProvider());
                //设置经纬度
                location.setLatitude(tencentLocation.getLatitude());
                location.setLongitude(tencentLocation.getLongitude());
                //设置精度，这个值会被设置为定位点上表示精度的圆形半径
                //location.setAccuracy(tencentLocation.getAccuracy());
                //设置定位标的旋转角度，注意 tencentLocation.getBearing() 只有在 gps 时才有可能获取
                //location.setBearing((float) tencentLocation.getBearing());
                LatLng latLng = new LatLng(tencentLocation.getLatitude(), tencentLocation.getLongitude());
                addMarker(latLng);
                addLocationMarker(tencentLocation);
                tencentMap.animateCamera(CameraUpdateFactory.newLatLng(latLng), new TencentMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                        mMapCenterPointerMarker = tencentMap.addMarker(new MarkerOptions(latLng).icon(
                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                        Point point = tencentMap.getProjection().toScreenLocation(latLng);
                        mMapCenterPointerMarker.setFixingPoint(point.x, point.y);
                        mMapCenterPointerMarker.setFixingPointEnable(true);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                if (mSensorHelper != null) {
                    //定位图标旋转
                    mSensorHelper.setCurrentMarker(marker);

                }
                //将位置信息返回给地图
                locationChangedListener.onLocationChanged(location);
            }

        }
    }
    private void addLocationMarker(TencentLocation tencentLocation) {
        LatLng mylocation = new LatLng(tencentLocation.getLatitude(), tencentLocation.getLongitude());
        float accuracy = tencentLocation.getAccuracy();
        if (marker1 == null) {
            marker1 = addMarker1(mylocation);
            ac = tencentMap.addCircle(new CircleOptions().center(mylocation)
                    .fillColor(Color.argb(100, 151, 203, 227)).radius(accuracy)
                    .strokeColor(Color.argb(255, 151, 213, 227)).strokeWidth(5));
            c = tencentMap.addCircle(new CircleOptions().center(mylocation)
                    .fillColor(Color.argb(70, 151, 203, 227)).radius(accuracy)
                    .strokeColor(Color.argb(255, 151, 213, 227)).strokeWidth(0));
        } else {
            marker1.setPosition(mylocation);
            ac.setCenter(mylocation);
            ac.setRadius(accuracy);
            c.setCenter(mylocation);
            c.setRadius(accuracy);
        }
        Scalecircle(c);
    }

    public void Scalecircle(final Circle circle) {
        // start = SystemClock.uptimeMillis();
        mTimerTask = new circleTask(circle, 1000);
        mTimer.schedule(mTimerTask, 0, 30);
    }

    private Marker addMarker1(LatLng point) {
        Marker marker2 = tencentMap.addMarker(new MarkerOptions().position(point).icon(BitmapDescriptorFactory.fromResource(R.mipmap.navi_map_gps_locked))
                .anchor(0.5f, 0.5f));
        return marker2;
    }
    private void addMarker(LatLng latLng) {
        if (marker != null) return;
        MarkerOptions options = new MarkerOptions();
        options.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(this.getResources(), R.mipmap.navi_map_gps_locked)));
        options.anchor(0.5f, 0.5f);
        //options.icon(BitmapDescriptorFactory.fromBitmap(getBitMap(R.mipmap.location)));
        options.position(latLng);
        marker = tencentMap.addMarker(options);
        Log.d("2222","-------------------------");
    }



    private class circleTask extends TimerTask {
        private double r;
        private Circle circle;
        private long duration = 1000;

        public circleTask(Circle circle, long rate) {
            this.circle = circle;
            this.r = circle.getRadius();
            if (rate > 0) {
                this.duration = rate;
            }
        }

        @Override
        public void run() {
            try {
                long elapsed = SystemClock.uptimeMillis() - start;
                float input = (float) elapsed / duration;
//                外圈放大后消失
                float t = interpolator1.getInterpolation(input);
                double r1 = (t + 1) * r;
                circle.setRadius(r1);
                if (input > 2) {
                    start = SystemClock.uptimeMillis();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 启用
     * @param onLocationChangedListener  数据源更改监听
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        locationChangedListener = onLocationChangedListener;

        int error = locationManager.requestLocationUpdates(locationRequest, this, Looper.myLooper());
        switch (error) {
            case 1:
                showMsg("设备缺少使用腾讯定位服务需要的基本条件");
                break;
            case 2:
                showMsg("AndroidManifest 中配置的 key 不正确");
                break;
            case 3:
                showMsg("自动加载libtencentloc.so失败");
                break;
            default:
                break;
        }
    }


    /**
     * 用于接收GPS、WiFi、Cell状态码
     */
    @Override
    public void onStatusUpdate(String name, int status, String desc) {
        //GPS, WiFi, Radio 等状态发生变化
        Log.v("State changed", name + "===" + desc);
    }

    /**
     * 停用
     */
    @Override
    public void deactivate() {
        locationManager.removeUpdates(this);
        locationManager = null;
        locationRequest = null;
        locationChangedListener = null;
    }

    /**
     * Toast提示
     * @param msg
     */
    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
//
//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        if (TextUtils.isEmpty(query)) {
//            clearList();
//            return false;
//        }
//        mIsUseSug = false;
//        //根据关键字，请求搜索列表
//        TencentLocation location = locationManager.getInstance(this).getLastKnownLocation();
//        SearchParam param = new SearchParam();
//        param.keyword(query).boundary(new SearchParam.Region(location.getCity()));
//        mTencentSearch.search(param, new HttpResponseListener<SearchResultObject>() {
//
//            @Override
//            public void onSuccess(int pI, SearchResultObject pSearchResultObject) {
//                if (pSearchResultObject != null) {
//                    Log.i("TAG", "onScuess()" + "////");
//                    mRecyclerView.setVisibility(View.VISIBLE);
//                    updateSearchPoiList(pSearchResultObject.data);
//                }
//            }
//
//            @Override
//            public void onFailure(int pI, String pS, Throwable pThrowable) {
//                mRecyclerView.setVisibility(View.INVISIBLE);
//                Log.e("tencent-map-samples", pS, pThrowable);
//            }
//        });
//        return true;
//    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (TextUtils.isEmpty(query)) {
            clearList();
            return false;
        }
        mIsUseSug = false;
        //根据关键字，请求搜索列表
        TencentLocation location = locationManager.getInstance(this).getLastKnownLocation();
        SearchParam param = new SearchParam();
        param.keyword(query).boundary(new SearchParam.Region(location.getCity()));
        mTencentSearch.search(param, new HttpResponseListener<SearchResultObject>() {

            @Override
            public void onSuccess(int pI, SearchResultObject pSearchResultObject) {
                if (pSearchResultObject != null) {
                    Log.i("TAG", "onScuess()" + "////");
                    mRecyclerView.setVisibility(View.VISIBLE);
                    updateSearchPoiList(pSearchResultObject.data);
                }
            }

            /**
             * 更新搜索POI结果
             *
             * @param pData
             */
            private void updateSearchPoiList(List<SearchResultObject.SearchResultData> pData) {
                if (!pData.isEmpty()) {
                    mPoiInfos.clear();
                    for (SearchResultObject.SearchResultData data : pData) {
                        PoiInfo poiInfo = new PoiInfo();
                        poiInfo.id = data.id;
                        poiInfo.name = data.title;
                        poiInfo.address = data.address;
                        poiInfo.latLng = data.latLng;
                        poiInfo.source = PoiInfo.SOURCE_SEARCH;
                        mPoiInfos.add(poiInfo);
                    }

                    mSearchPoiAdapter.notifyDataSetChanged();
                } else {
                    clearList();
                }
            }

            @Override
            public void onFailure(int pI, String pS, Throwable pThrowable) {
                mRecyclerView.setVisibility(View.INVISIBLE);
                Log.e("tencent-map-samples", pS, pThrowable);
            }
        });
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            mIsUseSug = true;
            clearList();
            return false;
        }

        if (!mIsUseSug) {
            return false;
        }
        //搜索建议
        TencentLocation location = locationManager.getInstance(this).getLastKnownLocation();
        SuggestionParam param = new SuggestionParam();
        LatLng latLng=new  LatLng(location.getLatitude(), location.getLongitude());
        param.keyword(newText).region(location.getCity()).location(latLng);
        mTencentSearch.suggestion(param, new HttpResponseListener<SuggestionResultObject>() {
            @Override
            public void onSuccess(int pI, SuggestionResultObject pSuggestionResultObject) {
                if (pSuggestionResultObject != null && mIsUseSug) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    updateSuggestionPoiList(pSuggestionResultObject.data);
                }
            }

            @Override
            public void onFailure(int pI, String pS, Throwable pThrowable) {
                mRecyclerView.setVisibility(View.INVISIBLE);
                Log.e("tencent-map-samples", pS, pThrowable);
            }
        });
        return true;
    }

    @Override
    public boolean onClose() {
        return clearList();
    }

    @Override
    public void onFocusChange(View view, boolean b) {

    }
    /**
     * 在地图上显示POI
     *
     * @param pInfo
     */
    private void performShowPoiInMap(PoiInfo pInfo) {

        if (tencentMap == null || tencentMap.isDestroyed()) {
            return;
        }

        if (mPoiMarker != null) {
            mPoiMarker.remove();
        }

        mPoiMarker = tencentMap.addMarker(new MarkerOptions(pInfo.latLng).title(pInfo.name).snippet(pInfo.address));
        tencentMap.animateCamera(CameraUpdateFactory.newLatLng(pInfo.latLng));
        mSearchView.clearFocus();
    }
/**
     * 清空列表
     *
     * @return
     *//**/
    private boolean clearList() {
        if (!mPoiInfos.isEmpty()) {
            mPoiInfos.clear();
            mSearchPoiAdapter.notifyDataSetChanged();
            return true;
        }

        return false;
    }
    /**
     * 更新搜索建议结果
     *
     * @param pData
     */
    private void updateSuggestionPoiList(List<SuggestionResultObject.SuggestionData> pData) {

        if (!pData.isEmpty()) {
            mPoiInfos.clear();
            for (SuggestionResultObject.SuggestionData data : pData) {
                PoiInfo poiInfo = new PoiInfo();
                poiInfo.id = data.id;
                poiInfo.name = data.title;
                poiInfo.latLng = data.latLng;
                poiInfo.source = PoiInfo.SOURCE_SUG;
                mPoiInfos.add(poiInfo);
            }

            mSearchPoiAdapter.notifyDataSetChanged();
        } else {
            clearList();
        }

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if (cameraPosition.tilt < 5) {
            tencentMap.setBuildingEnable(false);
        } else {
            tencentMap.setBuildingEnable(true);
        }
    }

    @Override
    public void onCameraChangeFinished(CameraPosition cameraPosition) {
        LatLng mLatPosition = new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude);
        //获取当前地图中心点，请求搜索接口
        SearchParam.Nearby nearby = new SearchParam.Nearby();
        nearby.point(mLatPosition);
        nearby.r(1000);
        nearby.autoExtend(true);
        SearchParam param = new SearchParam("厦门", nearby);
        if (mTencentSearch != null) {
            mTencentSearch.search(param, new HttpResponseListener<SearchResultObject>() {
                @Override
                public void onSuccess(int i, SearchResultObject baseObject) {
                    if (baseObject != null) {
                       // mRecyclerView.setVisibility(View.VISIBLE);
                        updateSearchPoiList(baseObject.data);
                    }
                }

                @Override
                public void onFailure(int i, String s, Throwable throwable) {
                    mRecyclerView.setVisibility(View.INVISIBLE);
                }
            });
        }
    }
    /**
     * 更新搜索POI结果
     *
     * @param pData
     */
    private void updateSearchPoiList(List<SearchResultObject.SearchResultData> pData) {
        if (!pData.isEmpty()) {
            mPoiInfos.clear();
            for (SearchResultObject.SearchResultData data : pData) {
                PoiInfo poiInfo = new PoiInfo();
                poiInfo.id = data.id;
                poiInfo.name = data.title;
                poiInfo.address = data.address;
                poiInfo.latLng = data.latLng;
                poiInfo.source = PoiInfo.SOURCE_SEARCH;
                mPoiInfos.add(poiInfo);
            }

            mSearchPoiAdapter.notifyDataSetChanged();
        } else {
            clearList();
        }
    }
    private class PoiInfo {
        static final int SOURCE_SUG = 0;
        static final int SOURCE_SEARCH = 1;
        int source;
        String id;
        String name;
        String address;
        LatLng latLng;
    }

    private class SearchPoiAdapter extends ListAdapter<PoiInfo, SearchPoiItemViewHolder> {

        Context mContext;

        SearchPoiAdapter(Context pContext) {
            super(new DiffUtil.ItemCallback<PoiInfo>() {
                @Override
                public boolean areItemsTheSame(@NonNull PoiInfo oldItem, @NonNull PoiInfo newItem) {
                    return oldItem.id.equals(newItem.id);
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(@NonNull PoiInfo oldItem, @NonNull PoiInfo newItem) {
                    return oldItem.equals(newItem);
                }
            });

            mContext = pContext;
        }

        @NonNull
        @Override
        public SearchPoiItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SearchPoiItemViewHolder(this, parent, viewType);
        }


        @Override
        public int getItemViewType(int position) {
            PoiInfo poiInfo = getItem(position);
            return poiInfo.source;
        }

        @Override
        public void onBindViewHolder(@NonNull SearchPoiItemViewHolder holder, int position) {
            holder.bindView(getItem(position));
        }

        public void onItemClick(PoiInfo pItem) {
            if (pItem.source == PoiInfo.SOURCE_SUG) {
                mIsUseSug = false;
                mSearchView.setQuery(pItem.name, true);
            } else if (pItem.source == PoiInfo.SOURCE_SEARCH) {
                performShowPoiInMap(pItem);
            }
        }
    }


    private static class SearchPoiItemViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle;
        private TextView mSubTitle;
        private SearchPoiAdapter mAdapter;

        SearchPoiItemViewHolder(SearchPoiAdapter pAdapter, ViewGroup pParent, int pViewType) {
            super(LayoutInflater.from(pAdapter.mContext).inflate(getItemLayoutId(pViewType), pParent, false));
            mAdapter = pAdapter;
            mTitle = itemView.findViewById(android.R.id.text1);
            mSubTitle = itemView.findViewById(android.R.id.text2);
        }

        private static int getItemLayoutId(int pViewType) {
            if (pViewType == PoiInfo.SOURCE_SUG) {
                return android.R.layout.simple_list_item_1;
            } else if (pViewType == PoiInfo.SOURCE_SEARCH) {
                return android.R.layout.simple_list_item_2;
            }
            return android.R.layout.simple_list_item_2;
        }

        public void bindView(PoiInfo pItem) {
            mTitle.setText(pItem.name);
            if (mSubTitle != null) {
                mSubTitle.setText(pItem.address);
                mSubTitle.setVisibility(TextUtils.isEmpty(pItem.address) ? View.GONE : View.VISIBLE);
            }

            itemView.setOnClickListener(v -> mAdapter.onItemClick(pItem));
        }
    }
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.image:
//                BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
//                View view1 = getLayoutInflater().inflate(R.layout.dialog_bottom_new, null);
//                mBottomSheetDialog.setContentView(view1);
//                mBottomSheetDialog.setCanceledOnTouchOutside(true);
//                mBottomSheetDialog.getWindow().setDimAmount(0f);
//                img1=view1.findViewById(R.id.img1);
//                img1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        //标准地图
//                        tencentMap.setTrafficEnabled(true);
//                        tencentMap.setMapType(TencentMap.MAP_TYPE_NORMAL);
//                    }
//                });
//                img2=view1.findViewById(R.id.img2);
//                img2.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        //卫星地图
//                        tencentMap.setTrafficEnabled(false);
//                        tencentMap.setMapType(TencentMap.MAP_TYPE_SATELLITE);
//                    }
//                });
//                style1=view1.findViewById(R.id.style1);
//                style1.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        tencentMap.setMapStyle(1);
//                    }
//                });
//                style2=view1.findViewById(R.id.style2);
//                style2.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        tencentMap.setMapStyle(2);
//                    }
//                });
//                style3=view1.findViewById(R.id.style3);
//                style3.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        tencentMap.setMapStyle(3);
//                    }
//                });
//                mBottomSheetDialog.show();
//                break;
//
//
//        }
//
//
//    }
}