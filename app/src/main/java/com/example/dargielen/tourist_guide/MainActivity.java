package com.example.dargielen.tourist_guide;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import de.greenrobot.event.EventBus;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Tourist guide";
    Activity mActivity;
    private DBAdapter dbAdapter;
    private SimpleCursorAdapter dataAdapter;
    private ProgressBar mProgressBar;
    private ListView listView;
    private EditText mFilter;
    Context ctxt;
    LocationManager locationManager;
    Location yourLocation = new Location("");
    public static final double DEFAULT_LATITUDE = 50.11929513769021;
    public static final double DEFAULT_LONGITUDE = 16.645192354917526;
    private static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 1;
    private static final long INTERVAL_TIME = 20;
    private static final float MINIMAL_DISTANCE = 100;
    //private static final String DATA_URL = "https://drive.google.com/uc?id=0B1or6bxN_MUrb0Y5YU1hR0lZeFU&export=download";
    private static final String DATA_URL = "https://drive.google.com/uc?id=0B1or6bxN_MUrVkFPQkotbWV0U28&export=download";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctxt = getApplicationContext();
        mActivity = this;

        if(isNetworkAvailable()) {
            setStandardView();
        } else {
            setNoInternetView();
        }


        /*setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ctxt = getApplicationContext();

        dbAdapter = new DBAdapter(ctxt);
        dbAdapter.open();
        dbAdapter.deleteAllAttractions();
        dbAdapter.insertSomeAttractions();

        displayListView();*/
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo()!= null;
    }

    private void setStandardView() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EventBus.getDefault().register(this);
        yourLocation.setLatitude(DEFAULT_LATITUDE);
        yourLocation.setLongitude(DEFAULT_LONGITUDE);

        listView = (ListView) findViewById(R.id.attractionsList);
        mFilter = (EditText) findViewById(R.id.filter);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        listView.setVisibility(View.INVISIBLE);
        mFilter.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);

        dbAdapter = new DBAdapter(ctxt);
        dbAdapter.open();
        dbAdapter.deleteAllAttractions();
        useOkHttp();
    }

    private void setNoInternetView() {
        setContentView(R.layout.no_connection);
        Snackbar.make(getWindow().getDecorView().getRootView(), ctxt.getString(R.string.no_connection_text), Snackbar.LENGTH_LONG).show();
        Button refresh = (Button) findViewById(R.id.buttonConnection);
        refresh.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intentRefresh = new Intent(ctxt, MainActivity.class);
                intentRefresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentRefresh);
                mActivity.finish();
            }
        });
    }

    private void getViewReady() {
        getLocation();
        calculateDistance();
        displayListView();
    }

    private void displayListView() {
        Cursor cursor = dbAdapter.fetchAllAttractions();

        String[] columns = new String[]{
                DBAdapter.Attractions.COLUMN_NAME_NAME,
                DBAdapter.Attractions.COLUMN_NAME_ADDRESS,
                DBAdapter.Attractions.COLUMN_NAME_DESCRIPTION_SHORT,
                DBAdapter.Attractions.COLUMN_NAME_IMAGE1,
                DBAdapter.Attractions.COLUMN_NAME_DISTANCE
        };

        int[] to = new int[]{
                R.id.attr_name,
                R.id.attr_address,
                R.id.attr_dscr_short,
                R.id.imageView,
                R.id.attr_dst
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
                    mainImage.setBackgroundColor(Color.BLACK);
                    Picasso.with(ctxt).load(cursor.getString(columnIndex)).
                            placeholder(R.drawable.progress_animation).error(R.drawable.nophoto).into(mainImage);
                    //int resId = ctxt.getResources().getIdentifier(cursor.getString(columnIndex), "drawable", ctxt.getPackageName());
                    //mainImage.setImageResource(resId);
                    return true;
                } else if (view.getId() == R.id.attr_dst) {
                    TextView distance = (TextView) view;
                    double dist = cursor.getDouble(columnIndex);
                    String text = String.format("%.2f", dist) + " km";
                    distance.setText(text);
                    return true;
                }
                return false;
            }
        });

        listView = (ListView) findViewById(R.id.attractionsList);
        listView.setAdapter(dataAdapter);
        listView.setVisibility(View.VISIBLE);
        mFilter.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);

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
                double distance = cursor.getDouble(cursor.getColumnIndex(DBAdapter.Attractions.COLUMN_NAME_DISTANCE));

                Attraction dataToSend = new Attraction(name, address, null, longDescription, image1, image2, image3, image4, longitude, latitude, distance);
                Intent i = new Intent(getApplicationContext(), AttractionDetail.class);
                i.putExtra(DBAdapter.Attractions.KEY, dataToSend);
                startActivity(i);
            }
        });

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

    private void getPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_ACCESS_FINE_LOCATION);
        }
    }

    private void getLocation() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(ctxt, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctxt, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                getPermission();
            } else {
                startLocationListener();
            }
        } else {
            startLocationListener();
        }
    }

    public void startLocationListener() {
        if (ContextCompat.checkSelfPermission(ctxt, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctxt, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);

            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                yourLocation.setLatitude(lastKnownLocation.getLatitude());
                yourLocation.setLongitude(lastKnownLocation.getLongitude());
            }

            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    yourLocation.setLatitude(location.getLatitude());
                    yourLocation.setLongitude(location.getLongitude());
                    calculateDistance();
                    Cursor cursor = dbAdapter.fetchAllAttractions();
                    dataAdapter.changeCursor(cursor);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, INTERVAL_TIME, MINIMAL_DISTANCE, locationListener);
        }

    }

    private void calculateDistance() {
        double result;
        Cursor cursor = dbAdapter.fetchAllAttractions();
        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBAdapter.Attractions._ID));
                    double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(DBAdapter.Attractions.COLUMN_NAME_LATITUDE));
                    double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(DBAdapter.Attractions.COLUMN_NAME_LONGITUDE));
                    Location location = new Location("");
                    location.setLatitude(latitude);
                    location.setLongitude(longitude);
                    result = (double) yourLocation.distanceTo(location)/1000;
                    dbAdapter.insertDistance(id, result);
                }
            } finally {
                cursor.close();
            }
        }
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

            case R.id.order_distance_asc:
                dbAdapter.setOrderBy(DBAdapter.Attractions.COLUMN_NAME_DISTANCE + " ASC");
                displayListView();
                return true;

            case R.id.order_distance_dsc:
                dbAdapter.setOrderBy(DBAdapter.Attractions.COLUMN_NAME_DISTANCE + " DSC");
                displayListView();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void useOkHttp() {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(DATA_URL).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "connection failure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String json = response.body().string();
                    Log.d(TAG, "onResponse: " + json);
                    Attraction[] attractions_data = new Gson().fromJson(json, Attraction[].class);
                    EventBus.getDefault().post(new DataLoadedEvent(attractions_data));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @SuppressWarnings("unused")
    public void onEventMainThread(DataLoadedEvent event) {
        Attraction[] attractions = event.getLoadedAttractions();
        for (Attraction attraction:attractions) {
            dbAdapter.createAttraction(attraction);
        }
        EventBus.getDefault().unregister(this);
        getViewReady();
    }
}
