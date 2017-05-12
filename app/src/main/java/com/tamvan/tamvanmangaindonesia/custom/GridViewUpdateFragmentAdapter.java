package com.tamvan.tamvanmangaindonesia.custom;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tamvan.tamvanmangaindonesia.R;
import com.tamvan.tamvanmangaindonesia.holder.UpdateManga;

import java.io.Serializable;

/**
 * Created by srofi on 2/16/2017.
 */

public class GridViewUpdateFragmentAdapter extends BaseAdapter implements Serializable{

    private Context context;
    private UpdateManga[] data;

    public GridViewUpdateFragmentAdapter(Context context, UpdateManga[] data){
        this.context = context;
        this.data = data;
        this.data = data;
    }
    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public UpdateManga getItem(int i) {
        return data[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.custom_gridview_fu,viewGroup,false);
        ImageView imageViewMain = (ImageView)view.findViewById(R.id.cfu_imageview_main);
        TextView textViewTitle = (TextView)view.findViewById(R.id.cfu_textview_title);
        TextView textViewChapter = (TextView)view.findViewById(R.id.cfu_textview_chapter);
        UpdateManga updateManga = getItem(i);
        textViewTitle.setText(updateManga.getTitle());
        textViewChapter.setText(updateManga.getEpisode());
        Picasso.with(context).load(updateManga.getCover()).fit().into(imageViewMain);
        view.setTag(updateManga);
        return view;
    }
}
