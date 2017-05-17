package com.example.dargielen.tourist_guide;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

/**
 * Created by dargielen on 20.04.2017.
 */

public class AttractionDetail extends AppCompatActivity {

    Attraction attr;
    String longitude, latitude;
    String[] images = new String[4];

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
        /*int image1 = ctx.getResources().getIdentifier(attr.getImage1(), "drawable", ctx.getPackageName());
        int image2 = ctx.getResources().getIdentifier(attr.getImage2(), "drawable", ctx.getPackageName());
        int image3 = ctx.getResources().getIdentifier(attr.getImage3(), "drawable", ctx.getPackageName());
        int image4 = ctx.getResources().getIdentifier(attr.getImage4(), "drawable", ctx.getPackageName());*/

        String image1 = attr.getImage1();
        String image2 = attr.getImage2();
        String image3 = attr.getImage3();
        String image4 = attr.getImage4();

        images[0] = image1;
        images[1] = image2;
        images[2] = image3;
        images[3] = image4;
        latitude = String.valueOf(attr.getLatitude());
        longitude = String.valueOf(attr.getLongitude());

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this, images));

        Button btn_map = (Button) findViewById(R.id.buttonMap);
        btn_map.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showOnMap();
            }
        });

    }// koniec OnCreate

    private void showOnMap() {

        Uri gmmIntentUri = Uri.parse("http://maps.google.co.in/maps?q=" + attr.getName());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

}
