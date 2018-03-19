package com.lightgo.schooldaily;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
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
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class NagivationActivity extends AppCompatActivity {
    private Marker mMarker1;
    MapView mMapView = null;
    BaiduMap mBaiduMap = null;
    private LocationClient locationClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    boolean isFirstLoc = true; // 是否首次定位

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_nagivation);

        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        locationClient = new LocationClient(this);
        locationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);//定位请求时间间隔
        locationClient.setLocOption(option);
        locationClient.start();
    }

    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                LatLng pt1 = new LatLng(26.882285, 112.687948);
                LatLng pt2 = new LatLng(26.893587, 112.687589);
                LatLng pt3 = new LatLng(26.893297, 112.679486);
                LatLng pt4 = new LatLng(26.880698, 112.679738);
                LatLng pt5 = new LatLng(26.880456, 112.6799);
                LatLng pt6 = new LatLng(26.880601, 112.680762);
                LatLng pt7 = new LatLng(26.881036, 112.682559);
                LatLng pt8 = new LatLng(26.881536, 112.684301);
                LatLng pt9 = new LatLng(26.881826, 112.686817);
                LatLng pt10 = new LatLng(26.882277, 112.687913);
                List<LatLng> pts = new ArrayList<LatLng>();
                pts.add(pt1);
                pts.add(pt2);
                pts.add(pt3);
                pts.add(pt4);
                pts.add(pt5);
                pts.add(pt6);
                pts.add(pt7);
                pts.add(pt8);
                pts.add(pt9);
                pts.add(pt10);
                //构建用户绘制多边形的Option对象
                OverlayOptions polygonOption = new PolygonOptions()
                        .points(pts)
                        .stroke(new Stroke(10, 0x1971C671))
                        .fillColor(0x6671C671);
                //在地图上添加多边形Option，用于显示
                mBaiduMap.addOverlay(polygonOption);


                BitmapDescriptor biaoe = BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_marka);
                LatLng p5 = new LatLng(26.884528,112.682431);
                OverlayOptions option5 = new MarkerOptions()
                        .position(p5)
                        .icon(biaoe);
                mMarker1 = (Marker) mBaiduMap.addOverlay(option5);
                option5 = new TextOptions()
                        .fontSize(24)
                        .fontColor(0xFFFF00FF)
                        .text("计算机楼")
                        .rotate(0)
                        .position(pt1);
                mBaiduMap.addOverlay(option5);
                mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Intent intentjd=new Intent(NagivationActivity.this, JidiActivity .class);
                        startActivity(intentjd);
                        return true;
                    }
                });

            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        locationClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }
}

