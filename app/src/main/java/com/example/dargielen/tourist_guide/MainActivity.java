package com.example.dargielen.tourist_guide;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private DBAdapter dbAdapter;
    private SimpleCursorAdapter dataAdapter;
    Context ctxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ctxt = getApplicationContext();

        dbAdapter = new DBAdapter(ctxt);
        dbAdapter.open();
        dbAdapter.deleteAllAttractions();
        dbAdapter.insertSomeAttractions();

        displayListView();
    }

    private void displayListView() {
        Cursor cursor = dbAdapter.fetchAllAttractions();

        String[] columns = new String[] {
                DBAdapter.Attractions.COLUMN_NAME_NAME,
                DBAdapter.Attractions.COLUMN_NAME_ADDRESS,
                DBAdapter.Attractions.COLUMN_NAME_DESCRIPTION_SHORT,
                DBAdapter.Attractions.COLUMN_NAME_IMAGES
        };

        int[] to = new int[] {
                R.id.attr_name,
                R.id.attr_address,
                R.id.attr_dscr_short,
                R.id.imageView
        };

        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.listview_element,
                cursor,
                columns,
                to,
                0);

        dataAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId() == R.id.imageView) {
                    ImageView mainImage = (ImageView) view;
                    int resId = ctxt.getResources().getIdentifier(cursor.getString(columnIndex), "drawable", ctxt.getPackageName());
                    mainImage.setImageResource(resId);
                    return true;
                } else if(view.getId() == R.id.filter) {
                    TextView dystans = (TextView) view;
                    double dyst = cursor.getDouble(columnIndex);
                    String tekst = String.format("%.2f", dyst) + " km";
                    dystans.setText(tekst);
                    return true;
                }
                return false;
            }
        });

        ListView listView = (ListView) findViewById(R.id.attractionsList);
        listView.setAdapter(dataAdapter);
    }
}
