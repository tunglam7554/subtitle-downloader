package com.tulasoft.subtitledownloader.MovieList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tulasoft.subtitledownloader.R;

import java.util.ArrayList;
import java.util.List;

public class MovieListAdapter extends BaseAdapter {
    public MovieListAdapter() {
    }

    public MovieListAdapter(Context context, int layout, ArrayList<MovieItem> movieItemArrayList) {
        this.context = context;
        this.movieItemArrayList = movieItemArrayList;
        this.layout = layout;
    }

    Context context;
    ArrayList<MovieItem> movieItemArrayList;
    int layout;
    @Override
    public int getCount() {
        return movieItemArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return movieItemArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view  = layoutInflater.inflate(layout, viewGroup, false);
        TextView title, description;
        ImageView image;
        title = view.findViewById(R.id.movie_title);
        description = view.findViewById((R.id.movie_description));
        image = view.findViewById(R.id.movie_image);
        MovieItem movieItem = movieItemArrayList.get(i);
        title.setText(movieItem.getTitle());
        description.setText(movieItem.getDescription());
        Picasso.get().load(movieItem.getImage()).fit().into(image);

        return view;
    }
}
