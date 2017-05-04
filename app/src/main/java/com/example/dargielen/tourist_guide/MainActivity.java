package com.example.dargielen.tourist_guide;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
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

        String[] columns = new String[]{
                DBAdapter.Attractions.COLUMN_NAME_NAME,
                DBAdapter.Attractions.COLUMN_NAME_ADDRESS,
                DBAdapter.Attractions.COLUMN_NAME_DESCRIPTION_SHORT,
                DBAdapter.Attractions.COLUMN_NAME_IMAGE1
        };

        int[] to = new int[]{
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
                } else if (view.getId() == R.id.filter) {
                    TextView distance = (TextView) view;
                    double dist = cursor.getDouble(columnIndex);
                    String text = String.format("%.2f", dist) + " km";
                    distance.setText(text);
                    return true;
                }
                return false;
            }
        });

        ListView listView = (ListView) findViewById(R.id.attractionsList);
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {

                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                String name = cursor.getString(cursor.getColumnIndexOrThrow(DBAdapter.Attractions.COLUMN_NAME_NAME));
                String address = cursor.getString(cursor.getColumnIndex(DBAdapter.Attractions.COLUMN_NAME_ADDRESS));
                String longDescription = cursor.getString(cursor.getColumnIndex(DBAdapter.Attractions.COLUMN_NAME_DESCRIPTION_LONG));
                String image1 = cursor.getString(cursor.getColumnIndex(DBAdapter.Attractions.COLUMN_NAME_IMAGE1));
                String image2 = cursor.getString(cursor.getColumnIndex(DBAdapter.Attractions.COLUMN_NAME_IMAGE2));
                String image3 = cursor.getString(cursor.getColumnIndex(DBAdapter.Attractions.COLUMN_NAME_IMAGE3));
                String image4 = cursor.getString(cursor.getColumnIndex(DBAdapter.Attractions.COLUMN_NAME_IMAGE4));
                double longitude = cursor.getDouble(cursor.getColumnIndex(DBAdapter.Attractions.COLUMN_NAME_LONGITUDE));
                double latitude = cursor.getDouble(cursor.getColumnIndex(DBAdapter.Attractions.COLUMN_NAME_LATITUDE));

                Attraction dataToSend = new Attraction(name, address, null, longDescription, image1, image2, image3, image4, longitude, latitude);
                Intent i = new Intent(getApplicationContext(), AttractionDetail.class);
                i.putExtra(DBAdapter.Attractions.KEY, dataToSend);
                startActivity(i);
            }
        });

        EditText mFilter = (EditText) findViewById(R.id.filter);
        mFilter.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                dataAdapter.getFilter().filter(s.toString());
            }
        });

        dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return dbAdapter.fetchAllAttractionsByNameAndDescription(constraint.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.order_name_asc:
                dbAdapter.setOrderBy(DBAdapter.Attractions.COLUMN_NAME_NAME + " ASC");
                displayListView();
                return true;

            case R.id.order_name_dsc:
                dbAdapter.setOrderBy(DBAdapter.Attractions.COLUMN_NAME_NAME + " DESC");
                displayListView();
                return true;

            /*case R.id.order_distance_asc:
                dbAdapter.setOrderBy(DBAdapter.Attractions.COLUMN_NAME_DISTANCE + " ASC");
                displayListView();
                return true;

            case R.id.order_distance_dsc:
                dbAdapter.setOrderBy(DBAdapter.Attractions.COLUMN_NAME_DISTANCE + " DSC");
                displayListView();
                return true;*/

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
