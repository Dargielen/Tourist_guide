package com.example.dargielen.tourist_guide;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by dargielen on 20.05.2017.
 */

public class ShowMap extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.show_map_frag);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_map);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        DBAdapter attr_database = new DBAdapter(getApplicationContext());
        attr_database.open();
        Cursor cursor = attr_database.fetchAllAttractionsForMap();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        Intent intent = getIntent();
        double longitude = intent.getDoubleExtra(MainActivity.LONGITUDE, MainActivity.DEFAULT_LONGITUDE);
        double latitude = intent.getDoubleExtra(MainActivity.LATITUDE, MainActivity.DEFAULT_LATITUDE);
        LatLng mLatLng = new LatLng(longitude, latitude);
        googleMap.addMarker(new MarkerOptions().position(mLatLng).title("Znajdujesz się tutaj"));
        builder.include(mLatLng);
        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.Attractions.COLUMN_NAME_NAME));
                    double longitude2 = cursor.getDouble(cursor.getColumnIndexOrThrow(DBAdapter.Attractions.COLUMN_NAME_LONGITUDE));
                    double latitude2 = cursor.getDouble(cursor.getColumnIndexOrThrow(DBAdapter.Attractions.COLUMN_NAME_LATITUDE));
                    LatLng latLng = new LatLng(longitude2, latitude2);
                    googleMap.addMarker(new MarkerOptions().position(latLng).title(name));
                    builder.include(latLng);
                }
            } finally {
                cursor.close();
            }
        }
        LatLngBounds bounds = builder.build();
        int padding = 50;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.animateCamera(cameraUpdate);
        googleMap.moveCamera(cameraUpdate);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTitle().equals("Znajdujesz się tutaj"))
        {
            Intent intent = new Intent(ShowMap.this, AttractionDetail.class);
            startActivity(intent);
        }
        return false;
    }
}
