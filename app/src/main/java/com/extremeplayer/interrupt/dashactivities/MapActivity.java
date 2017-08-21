package com.extremeplayer.interrupt.dashactivities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.extremeplayer.interrupt.R;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        LatLng svce_center = new LatLng(12.987079, 79.972057);
        LatLng svce_ne = new LatLng(12.991045, 79.977014);
        LatLng svce_sw = new LatLng(12.982682, 79.968302);

        LatLngBounds latLngBounds = new LatLngBounds(svce_sw, svce_ne);

        mMap.addMarker(new MarkerOptions().position(svce_center).title("SVCE"));

        Polyline polyline = mMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(12.983270, 79.971102),
                        new LatLng(12.986417, 79.971403)));

        mMap.setMinZoomPreference(15.0f);
        mMap.setLatLngBoundsForCameraTarget(latLngBounds);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(svce_center, 16));
    }
}
