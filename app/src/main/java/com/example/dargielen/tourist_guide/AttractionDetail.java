package com.example.dargielen.tourist_guide;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by dargielen on 20.04.2017.
 */

public class AttractionDetail extends AppCompatActivity {

    Attraction attr;
    String longitude, latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = getApplicationContext();
        setContentView(R.layout.attraction_detail);
        Bundle data = getIntent().getExtras();
        attr = data.getParcelable(DBAdapter.Attractions.KEY);
        TextView attr_name = (TextView) findViewById(R.id.attr_name);
        TextView attr_address = (TextView) findViewById(R.id.attr_address);
        TextView attr_dscr = (TextView) findViewById(R.id.attr_dscr_long);
        attr_name.setText(attr.getName());
        attr_address.setText(attr.getAddress());
        attr_dscr.setText(attr.getLong_description());
        int images = ctx.getResources().getIdentifier(attr.getImage(), "drawable", ctx.getPackageName());
        latitude = String.valueOf(attr.getLatitude());
        longitude = String.valueOf(attr.getLongitude());

        ImageView attr_img = (ImageView) findViewById(R.id.imageView);
        attr_img.setImageResource(images);

        Button btn_map = (Button) findViewById(R.id.buttonMap);
        btn_map.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showOnMap();
            }
        });

    }// koniec OnCreate

    private void showOnMap() {

        Log.d("AttractionDetail", "geo:" + latitude + "," + longitude);
        Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

}
