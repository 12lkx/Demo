package com.example.myapplication.TencentSDK;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.llw.demo.GeofenceEventReceiver;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.map.geolocation.TencentGeofence;
import com.tencent.map.geolocation.TencentGeofenceManager;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.map.geolocation.TencentPoi;

import java.util.List;

public class LocationActivity extends AppCompatActivity implements View.OnClickListener, TencentLocationListener {
    //定位信息显示
    private TextView tvLocationInfo;
    //连续定位按钮
    private Button btnContinuousPositioning;
    //单次定位按钮
    private Button btnSinglePositioning;
    //停止定位按钮
    private Button btnStopPositioning;
    //定位管理
    private TencentLocationManager mLocationManager;
    //定位请求
    private TencentLocationRequest locationRequest;
    //权限
    private RxPermissions rxPermissions;
    //添加围栏
    private Button btnAddFence;
    //移除围栏
    private Button btnRemoveFence;
    //地理围栏管理
    private TencentGeofenceManager mTencentGeofenceManager;
    //围栏别名
    private String geofenceName = "测试范围";
    //围栏
    private TencentGeofence geofence;
    //设置动作
    private static final String ACTION_TRIGGER_GEOFENCE = "com.llw.demo.GeofenceEventReceiver";
    //PendingIntent
    private PendingIntent pi;

    //广播接收器
    private GeofenceEventReceiver geofenceEventReceiver;
    //基础地图
    private Button btnBaseMap;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        initView();
        initLocation();
        checkVersion();
        initGeofence();
    }

    /**
     * 初始化定位信息
     */
    private void initLocation() {
        //获取TencentLocationManager实例
        mLocationManager = TencentLocationManager.getInstance(this);
        //获取定位请求TencentLocationRequest 实例
        locationRequest = TencentLocationRequest.create();
        //设置定位时间间隔，10s
        locationRequest.setInterval(1000);
        //位置信息的详细程度 REQUEST_LEVEL_ADMIN_AREA表示获取经纬度，位置所处的中国大陆行政区划
        locationRequest.setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA);
        //是否允许使用GPS
        locationRequest.setAllowGPS(true);
        //是否需要获取传感器方向，提高室内定位的精确度
        locationRequest.setAllowDirection(true);

        //是否需要开启室内定位
        locationRequest.setIndoorLocationMode(true);
        mLocationManager.setUserAgreePrivacy(true);




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
     * Toast提示
     *
     * @param msg 内容
     */
    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    /**
         * 页面初始化
         */
        private void initView() {
            tvLocationInfo = findViewById(R.id.tv_location_info);
            btnContinuousPositioning = findViewById(R.id.btn_continuous_positioning);
            btnBaseMap=findViewById(R.id.btn_base_map);
            btnStopPositioning = findViewById(R.id.btn_stop_positioning);
             //实例化
            rxPermissions = new RxPermissions(this);
            btnContinuousPositioning.setOnClickListener(this);
            btnStopPositioning.setOnClickListener(this);
            btnSinglePositioning=findViewById(R.id.btn_single_positioning);
            btnSinglePositioning.setOnClickListener(this);
            btnAddFence=findViewById(R.id.btn_add_fence);
            btnRemoveFence=findViewById(R.id.btn_remove_fence);
            btnRemoveFence.setOnClickListener(this);
            btnAddFence.setOnClickListener(this);
            btnBaseMap.setOnClickListener(this);
        }

    /**
     * 初始化地理围栏
     */
    private void initGeofence() {
        //实例化
        mTencentGeofenceManager = new TencentGeofenceManager(this);
        //地理围栏构建
        TencentGeofence.Builder builder = new TencentGeofence.Builder();
        geofence = builder.setTag(geofenceName)
                //设置圆心和半径，v 是 纬度，v1 是经度，v2 是半径 500米
                .setCircularRegion(22.5, 113.9, 500)
                //设置地理围栏有效期
                .setExpirationDuration(3 * 3600 * 1000)
                //完成构建
                .build();

        //构建Action和传递信息
        Intent receiverIntent = new Intent(ACTION_TRIGGER_GEOFENCE);
        receiverIntent.putExtra("tag", geofence.getTag());
        receiverIntent.putExtra("longitude", geofence.getLongitude());
        receiverIntent.putExtra("latitude", geofence.getLatitude());


        // 随机产生的 requestCode, 避免冲突
        int requestCode = (int) (Math.random() * 1E7);
        //构建PendingIntent
        pi = PendingIntent.getBroadcast(this, requestCode,
                receiverIntent, PendingIntent.FLAG_IMMUTABLE);
        //实例化
        geofenceEventReceiver = new GeofenceEventReceiver();
        //添加动作拦截
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_TRIGGER_GEOFENCE);
        //注册动态广播  记得去掉AndroidManifest.xml的静态广播配置
        registerReceiver(geofenceEventReceiver,intentFilter);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_continuous_positioning:
                //连续定位
                tvLocationInfo.setText("定位中");
                mLocationManager.requestLocationUpdates(locationRequest, this);

                break;
            case R.id.btn_single_positioning:
                mLocationManager.requestSingleFreshLocation(locationRequest,this, Looper.getMainLooper());
                break;
            case R.id.btn_stop_positioning:
                //停止定位
                mLocationManager.removeUpdates(this);
                showMsg("定位已停止");
                break;
            case R.id.btn_add_fence:
                  //添加围栏
                mTencentGeofenceManager.addFence(geofence,pi);
                showMsg("地理围栏已添加，请在附近溜达一下");
                break;
            case R.id.btn_remove_fence:
                //移除围栏
                //指定围栏对象移除
                mTencentGeofenceManager.removeFence(geofence);
                //通过tag移除
                //mTencentGeofenceManager.removeFence(geofenceName);
                showMsg("地理围栏已移除，撒有那拉！");
                break;
            case R.id.btn_base_map:
                startActivity(new Intent(this,BaseMapActivity.class));
                break;
            default:
                break;
        }

    }
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        //移除所有围栏
//        mTencentGeofenceManager.removeAllFences();
//        //销毁围栏管理
//        mTencentGeofenceManager.destroy();
//    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
        //显示定位信息
        Log.d("debug","显示定位信息");
        showLocationInfo(tencentLocation);
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {
        Log.d("debug","停止显示定位信息");
    }
    /**
     * 显示定位信息
     *
     * @param location
     */
    private void showLocationInfo(TencentLocation location) {
        //经度
        double longitude = location.getLongitude();
        //纬度
        double latitude = location.getLatitude();
        //准确性
        float accuracy = location.getAccuracy();
        //地址信息
        String address = location.getAddress();
        //海拔高度
        double altitude = location.getAltitude();
        //面积统计
        Integer areaStat = location.getAreaStat();
        //方向
        float bearing = location.getBearing();
        double direction = location.getDirection();
        //城市
        String city = location.getCity();
        //城市代码
        String cityCode = location.getCityCode();
        //城市电话代码
        String cityPhoneCode = location.getCityPhoneCode();
        //坐标类型
        int coordinateType = location.getCoordinateType();
        //区
        String district = location.getDistrict();
        //经过时间
        long elapsedRealtime = location.getElapsedRealtime();
        //Gps信息
        int gpsRssi = location.getGPSRssi();
        //室内建筑
        String indoorBuildingFloor = location.getIndoorBuildingFloor();
        //室内建筑编码
        String indoorBuildingId = location.getIndoorBuildingId();
        //室内位置类型
        int indoorLocationType = location.getIndoorLocationType();
        //名称
        String name = location.getName();
        //国家
        String nation = location.getNation();
        //周边poi信息列表
        List<TencentPoi> poiList = location.getPoiList();
        //提供者
        String provider = location.getProvider();
        //省
        String province = location.getProvince();
        //速度
        float speed = location.getSpeed();
        //街道
        String street = location.getStreet();
        //街道编号
        String streetNo = location.getStreetNo();
        //时间
        long time = location.getTime();
        //镇
        String town = location.getTown();
        //村
        String village = location.getVillage();

        StringBuffer buffer = new StringBuffer();
        buffer.append("经度：" + longitude + "\n");
        buffer.append("纬度：" + latitude + "\n");
        buffer.append("国家：" + nation + "\n");
        buffer.append("省：" + province + "\n");
        buffer.append("市：" + city + "\n");
        buffer.append("县/区：" + district + "\n");
        buffer.append("街道：" + street + "\n");
        buffer.append("名称：" + name + "\n");
        buffer.append("提供者：" + provider + "\n");
        buffer.append("详细地址：" + address + "\n");
        tvLocationInfo.setText(buffer.toString());
        //tvLocationInfo.setText("定位完成");
    }

}