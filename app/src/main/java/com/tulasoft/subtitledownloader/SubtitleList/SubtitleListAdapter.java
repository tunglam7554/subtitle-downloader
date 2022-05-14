package com.tulasoft.subtitledownloader.SubtitleList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.tulasoft.subtitledownloader.R;

import java.util.ArrayList;

public class SubtitleListAdapter extends BaseAdapter {
    Context context;
    ArrayList<SubtitleItem> subtitleItemArrayList;
    int layout;

    public SubtitleListAdapter() {
    }

    public SubtitleListAdapter(Context context, int layout,ArrayList<SubtitleItem> subtitleItemArrayList) {
        this.context = context;
        this.subtitleItemArrayList = subtitleItemArrayList;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return subtitleItemArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return subtitleItemArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view  = layoutInflater.inflate(layout, viewGroup, false);
        TextView filename;
        filename = view.findViewById(R.id.subtitle_filename);
        SubtitleItem subtitleItem = subtitleItemArrayList.get(i);
        filename.setText(subtitleItem.getSubFileName());

        return view;
    }
}
