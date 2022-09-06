package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.frangment.HomeFragment;
import com.example.myapplication.frangment.MessageFragment;
import com.example.myapplication.frangment.PersonFragment;
import com.example.myapplication.frangment.SearchFragment;
import com.example.myapplication.utils.Location;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.map.geolocation.TencentPoi;

import java.util.ArrayList;
import java.util.List;

public class SchoolHomeActivity extends AppCompatActivity implements View.OnClickListener, TencentLocationListener {
    private Toolbar toolbar;
    Bundle bundle=new Bundle();
    Location location1=new Location();
    //定位信息显示
    private TextView tvLocationInfo;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    //权限
    private RxPermissions rxPermissions;
    //定位管理
    private TencentLocationManager mLocationManager;
    //定位请求
    private TencentLocationRequest locationRequest;
    private Fragment f = null;
    private RadioButton img1,img2,img3,img4;
    private Button location;
    private String address1;
    private TextView img_tv1,img_tv2,img_tv3,img_tv4;
    private Window window;

    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(
            10);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_home);
        //实例化
        rxPermissions = new RxPermissions(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
             window = getWindow();
            //透明状态栏
           // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
          //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

//            window.getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            window.setStatusBarColor(getApplication().getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawableResource(R.drawable.background_gradient);
        }
        checkVersion();

        //toolbar=findViewById(R.id.school_toolbar);
        //initToolbar();
        initLocation();
        initView();

    }

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
        mLocationManager.requestLocationUpdates(locationRequest, this);

    }

    @SuppressLint("ResourceAsColor")
    private void initView() {
        img1=findViewById(R.id.image1);
        img2=findViewById(R.id.image2);
        img3=findViewById(R.id.image3);
        img4=findViewById(R.id.image4);
        img_tv1=findViewById(R.id.image1_text1);
        img_tv2=findViewById(R.id.image1_text2);
        img_tv3=findViewById(R.id.image1_text3);
        img_tv4=findViewById(R.id.image1_text4);
        img_tv1.setTextColor(Color.parseColor("#F57A2D"));
        img1.setChecked(true);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        f = new HomeFragment();
        fragmentTransaction.add(R.id.fragment_school, f);
        fragmentTransaction.commit();
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);
    }

    @SuppressLint("ResourceAsColor")
    private void initToolbar() {
        //toolbar = findViewById(R.id.toolbar_school);
        toolbar.setTitle("校园通");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }



    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.image1:
                img1.setChecked(true);
                img2.setChecked(false);
                img3.setChecked(false);
                img4.setChecked(false);
                img_tv1.setTextColor(Color.parseColor("#F57A2D"));
                img_tv2.setTextColor(Color.parseColor("#878686"));
                img_tv3.setTextColor(Color.parseColor("#878686"));
                img_tv4.setTextColor(Color.parseColor("#878686"));
                window.setStatusBarColor(getApplication().getResources().getColor(android.R.color.transparent));
                window.setBackgroundDrawableResource(R.drawable.background_gradient);
                f=new HomeFragment();
//                tvLocationInfo.setText(location1.getName());
                break;
            case R.id.image2:
                img2.setChecked(true);
                img1.setChecked(false);
                img3.setChecked(false);
                img4.setChecked(false);
                img_tv1.setTextColor(Color.parseColor("#878686"));
                img_tv2.setTextColor(Color.parseColor("#F57A2D"));
                img_tv3.setTextColor(Color.parseColor("#878686"));
                img_tv4.setTextColor(Color.parseColor("#878686"));
                window.setStatusBarColor(Color.parseColor("#F1F1F1"));
                f=new MessageFragment();
                break;
            case R.id.image3:
                img3.setChecked(true);
                img1.setChecked(false);
                img2.setChecked(false);
                img4.setChecked(false);
                img_tv1.setTextColor(Color.parseColor("#878686"));
                img_tv3.setTextColor(Color.parseColor("#F57A2D"));
                img_tv2.setTextColor(Color.parseColor("#878686"));
                img_tv4.setTextColor(Color.parseColor("#878686"));
                window.setStatusBarColor(Color.parseColor("#F1F1F1"));
                f=new SearchFragment();
                break;
            case R.id.image4:
                img4.setChecked(true);
                img1.setChecked(false);
                img2.setChecked(false);
                img3.setChecked(false);
                img_tv1.setTextColor(Color.parseColor("#878686"));
                img_tv4.setTextColor(Color.parseColor("#F57A2D"));
                img_tv3.setTextColor(Color.parseColor("#878686"));
                img_tv2.setTextColor(Color.parseColor("#878686"));
                window.setStatusBarColor(Color.parseColor("#FB953C"));
                //window.setStatusBarColor(Color.parseColor("#FB953C"));
                f=new PersonFragment();
                break;

        }
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_school,f);
        fragmentTransaction.commit();
    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {

        //显示定位信息

        //Log.d("debug","显示定位信息");
        showLocationInfo(tencentLocation);
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {
       // tvLocationInfo.setText(location1.getName());
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

        location1.setName(name);
       // tvLocationInfo.setText(location1.getName());


    }

    @Override
    protected void onResume() {
        super.onResume();
//        location=f.getView().findViewById(R.id.lication);
//        tvLocationInfo=f.getView().findViewById(R.id.location_tv);

    }

    @Override
    protected void onStart() {
        super.onStart();
//        location=f.getView().findViewById(R.id.lication);
//        tvLocationInfo=f.getView().findViewById(R.id.location_tv);


    }

    public interface MyOnTouchListener {
        public boolean onTouch(MotionEvent ev);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            if(listener != null) {
                listener.onTouch(ev);
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }
    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener) ;

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

}