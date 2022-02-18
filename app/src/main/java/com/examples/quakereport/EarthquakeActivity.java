package com.examples.quakereport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    public static final String LOG_TAG = "dDD";
    private static final String USAGE_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=6&limit=10";
    static TextView mEmptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEmptyStateTextView = findViewById(R.id.empty_view);
//        AsyncEarthquake earthquake = new AsyncEarthquake();
//        earthquake.execute(USAGE_URL);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager.getInstance(this).initLoader(0, null, this).forceLoad();
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            ProgressBar loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet);
        }

    }

    public void updateUi(List<Earthquake> earthquakeArrayList) {
        ListView earthquakeListView = findViewById(R.id.list);
        ;
        listAdapter adapter = new listAdapter(earthquakeArrayList, this);
        earthquakeListView.setEmptyView(mEmptyStateTextView);
        earthquakeListView.setAdapter(adapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Earthquake currentEarthquake = adapter.getItem(i);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        Log.d(LOG_TAG, "onCreateLoader: ");
        return new EarthquakeLoader(this, USAGE_URL);

    }


    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {

        ProgressBar loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_earthquakes);

        if (earthquakes != null && !earthquakes.isEmpty()) {

            updateUi(earthquakes);
            Log.d(LOG_TAG, "onLoadFinished ");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        updateUi(new ArrayList<Earthquake>());
        Log.d(LOG_TAG, "onLoaderReset: ");
    }


    public static class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {
        String mUrl;

        public EarthquakeLoader(@NonNull Context context, String url) {
            super(context);
            this.mUrl = url;
        }

        @Nullable
        @Override
        public List<Earthquake> loadInBackground() {

            List<Earthquake> earthquakes = QueryUtils.extractEarthquakes(mUrl);
            Log.d(LOG_TAG, "loadInBackground: ");
            return earthquakes;
        }

    }


//
//    private class AsyncEarthquake extends AsyncTask<String, Void, List<Earthquake>> {
//
//        @Override
//        protected List<Earthquake> doInBackground(String... urls) {
//            if (urls.length < 1 || urls[0] == null) {
//                return null;
//            }
//            ArrayList<Earthquake> list = QueryUtils.extractEarthquakes(urls[0]);
//            return list;
//        }
//
//        @Override
//        protected void onPostExecute(List<Earthquake> earthquakes) {
//
//            if (earthquakes == null) {
//                return;
//            }
//
//            updateUi(earthquakes);
//
//        }
//
//
//    }


}