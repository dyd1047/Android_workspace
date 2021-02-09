package com.koreait.mapproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    SupportMapFragment mapFragment;
    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //xml에 명시한 맵 프래그먼트 얻기!!
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    //맵이 로드가 되어, 상요할 준비가 되면..
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        LatLng pos = new LatLng(37.74253187772694, 127.05042836426318);
        MarkerOptions options = new MarkerOptions();
        options.position(pos);
        googleMap.addMarker(options); //앱에 옵션 추가
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(pos)); //카메라를 지정한 위치로 이동
    }
}