package com.tulasoft.subtitledownloader;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tulasoft.subtitledownloader.Detail.DetailActivity;
import com.tulasoft.subtitledownloader.MovieList.MovieItem;
import com.tulasoft.subtitledownloader.MovieList.MovieListAdapter;
import com.tulasoft.subtitledownloader.Service.API;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    protected String searchQuery;
    List<MovieItem> movieItemList;
    ListView listView;
    MovieListAdapter movieListAdapter;
    Button btnSearch;
    EditText txtSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.movie_list);
        btnSearch = findViewById(R.id.btnSearch);
        txtSearch = findViewById(R.id.txtSearch);

        movieItemList = new ArrayList<>();
        movieListAdapter = new MovieListAdapter(this, R.layout.movie_item, (ArrayList<MovieItem>) movieItemList);
        listView.setAdapter(movieListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MovieItem movieItem = (MovieItem) listView.getItemAtPosition(i);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("movieId", movieItem.getMovieID());
                intent.putExtra("movieTitle", movieItem.getTitle());
                intent.putExtra("movieImage", movieItem.getImage());
                intent.putExtra("movieDescription", movieItem.getDescription());
                MainActivity.this.startActivity(intent);
            }
        });
        txtSearch.setShowSoftInputOnFocus(true);
        txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    CloseKeyboard();
                    searchQuery = txtSearch.getText().toString();
                    if(!searchQuery.equals("")) {
                        SearchMovie(searchQuery);
                        return true;
                    }
                }
                return false;
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CloseKeyboard();
                searchQuery = txtSearch.getText().toString();
                if(!searchQuery.equals("")) {
                    SearchMovie(searchQuery);
                }
            }
        });
    }

    private void SearchMovie(String searchQuery){
        String URL = API.SearchTitle + searchQuery;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    movieItemList.clear();
                    JSONObject responseObj = new JSONObject(response);
                    Log.d("Response", response);
                    JSONArray jsonArray = responseObj.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jsonMovie = jsonArray.getJSONObject(i);
                        String id = jsonMovie.getString("id");
                        String title = jsonMovie.getString("title");
                        String description = jsonMovie.getString("description");
                        String image = jsonMovie.getString("image");
                        MovieItem movieItem = new MovieItem(id, image, title, description);
                        movieItemList.add(movieItem);
                        movieListAdapter.notifyDataSetChanged();
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

    private void CloseKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            listView.setSelection(0);
        }
    }

}