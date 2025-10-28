package com.example.spotyfeather;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String mAccessToken;
    private RecyclerView mRecyclerView;
    private TrackAdapter mAdapter;
    private ArrayList<String> mTrackNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAccessToken = getIntent().getStringExtra("ACCESS_TOKEN");

        mRecyclerView = findViewById(R.id.tracks_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTrackNames = new ArrayList<>();
        mAdapter = new TrackAdapter(mTrackNames);
        mRecyclerView.setAdapter(mAdapter);

        fetchTopTracks();
    }

    private void fetchTopTracks() {
        new Thread(() -> {
            try {
                URL url = new URL("https://api.spotify.com/v1/me/top/tracks");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", "Bearer " + mAccessToken);

                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

                JSONObject jsonObject = new JSONObject(result.toString());
                JSONArray items = jsonObject.getJSONArray("items");

                mTrackNames.clear();
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    String trackName = item.getString("name");
                    mTrackNames.add(trackName);
                    Log.d("TopTrack", trackName);
                }

                runOnUiThread(() -> mAdapter.notifyDataSetChanged());
            } catch (IOException | JSONException e) {
                Log.e("FetchTopTracks", "Error fetching top tracks", e);
            }
        }).start();
    }
}
