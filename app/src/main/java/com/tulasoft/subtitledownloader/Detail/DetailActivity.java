package com.tulasoft.subtitledownloader.Detail;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.tulasoft.subtitledownloader.R;
import com.tulasoft.subtitledownloader.Service.API;
import com.tulasoft.subtitledownloader.SubtitleList.SubtitleItem;
import com.tulasoft.subtitledownloader.SubtitleList.SubtitleListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {
    private String movieId, type, movieTitle, movieImage, movieDescription;
    private int season, episode, currentSeason, currentEpisode;
    TextView title, description;
    ImageView image;

    List<SubtitleItem> subtitleItemList;
    ListView listView;
    SubtitleListAdapter subtitleListAdapter;
    Spinner dropdownSeason, dropdownEpisode;
    Button btnSearchSubtitle;
    DownloadManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        movieId = intent.getStringExtra("movieId");
        movieTitle = intent.getStringExtra("movieTitle");
        movieDescription = intent.getStringExtra("movieDescription");
        movieImage = intent.getStringExtra("movieImage");

        season = 1;
        episode = 1;

        title = findViewById(R.id.detail_title);
        description = findViewById(R.id.detail_description);
        image = findViewById(R.id.detail_image);
        listView = findViewById(R.id.subtitle_list);
        btnSearchSubtitle = findViewById(R.id.btnSearchSubtitle);
        dropdownSeason = findViewById(R.id.detail_season);
        dropdownEpisode = (Spinner) findViewById(R.id.detail_episode);

        title.setText(movieTitle);
        description.setText(movieDescription);
        Picasso.get().load(movieImage).into(image);

        btnSearchSubtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder query = new StringBuilder();
                query.append(Param("imdbid",movieId));
                query.append(Param("sublanguageid","vie"));
                if(!type.equals("Movie")) {
                    String currentSeason = dropdownSeason.getSelectedItem().toString().substring(7);
                    String currentEpisode = dropdownEpisode.getSelectedItem().toString().substring(7);
                    query.append(Param("episode", currentEpisode));
                    query.append(Param("season", currentSeason));
                }
                LoadSubtitle(query.toString());
            }
        });

        subtitleItemList = new ArrayList<>();
        subtitleListAdapter = new SubtitleListAdapter(this, R.layout.subtitle_item, (ArrayList<SubtitleItem>) subtitleItemList);
        listView.setAdapter(subtitleListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Download file
                Toast.makeText(DetailActivity.this, "Downloading", Toast.LENGTH_LONG).show();
                manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                String downloadLink = subtitleItemList.get(i).getSubDownloadLink();
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadLink));
                request.setDescription(subtitleItemList.get(i).getSubDownloadLink());
                request.setTitle("Downloading");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, subtitleItemList.get(i).getSubFileName());
                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
            }
        });

        LoadDetail(movieId);
    }

    private void LoadDetail(String movieId){
        String URL = API.Detail + movieId;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseObj = new JSONObject(response);
                    type = responseObj.getString("type");
                    description.setText(responseObj.getString("plot"));
                    if(type.equals("Movie")){
                        StringBuilder query = new StringBuilder();
                        query.append(Param("imdbid",movieId));
                        query.append(Param("sublanguageid","vie"));
                        LoadSubtitle(query.toString());
                        Log.d("subtitle", query.toString());
                    }
                    else{
                        JSONObject tvSeriesInfo = responseObj.getJSONObject("tvSeriesInfo");
                        JSONArray seasons = tvSeriesInfo.getJSONArray("seasons");
                        season = seasons.length();
                        String[] arraySeason = new String[season];
                        for (int i = 0; i< season; i++){
                            arraySeason[i] = "Season " + Integer.toString(i+1);
                        }

                        ArrayAdapter<String> adapterSeason = new ArrayAdapter<String>(DetailActivity.this,
                                R.layout.dropdown_layout,
                                arraySeason);
                        adapterSeason.setDropDownViewResource(R.layout.dropdown_item);
                        dropdownSeason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                LoadEpisode(movieId, arraySeason[i]);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        dropdownSeason.setAdapter(adapterSeason);

                        //LoadEpisode(movieId, Integer.toString(1));
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();
            }
        });
        requestQueue.add(stringRequest);
    }

    private void LoadSubtitle(String query){
        String URL = API.SearchSubtitle + query;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    subtitleItemList.clear();
                    JSONArray resArray = new JSONArray(response);
                    Log.e("subtitle", response);
                    for (int i = 0; i< resArray.length(); i++){
                        JSONObject jsonSub = resArray.getJSONObject(i);
                        SubtitleItem subtitleItem = new SubtitleItem();
                        subtitleItem.setIDSubtitleFile(jsonSub.getString("IDSubtitleFile"));
                        subtitleItem.setSubFileName(jsonSub.getString("SubFileName"));
                        subtitleItem.setSubDownloadLink(jsonSub.getString("SubDownloadLink"));
                        subtitleItem.setLanguageName(jsonSub.getString("LanguageName"));
                        subtitleItem.setSubLanguageID(jsonSub.getString("SubLanguageID"));

                        subtitleItemList.add(subtitleItem);
                        subtitleListAdapter.notifyDataSetChanged();
                    }

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("X-User-Agent", API.OpenSubtitleUserAgent);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void LoadEpisode(String movieId, String season){
        String seasonNum = season.substring(7);
        String URL = API.SeasonEpisodes + movieId + "/" + seasonNum;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resObject = new JSONObject(response);
                    Log.d("Response episode", response);
                    JSONArray episodes = resObject.getJSONArray("episodes");
                    episode = episodes.length();
                    LoadEpisodeDropdown(episode);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("X-User-Agent", API.OpenSubtitleUserAgent);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    private void LoadEpisodeDropdown(int count){
        String[] arrayEpisode = new String[count];
        for (int i = 0; i< count; i++){
            arrayEpisode[i] = "Episode " + Integer.toString(i+1);
        }

        ArrayAdapter<String> adapterEpisode = new ArrayAdapter<String>(DetailActivity.this,
                R.layout.dropdown_layout,
                arrayEpisode);
        adapterEpisode.setDropDownViewResource(R.layout.dropdown_item);
        dropdownEpisode.setAdapter(adapterEpisode);
    }

    private String Param(String key, String value){
        return "/" + key + "-" + value;
    }
}
