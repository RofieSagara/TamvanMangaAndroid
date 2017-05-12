package com.tamvan.tamvanmangaindonesia.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tamvan.tamvanmangaindonesia.R;
import com.tamvan.tamvanmangaindonesia.holder.Manga;

import java.io.Serializable;

/**
 * Created by srofi on 2/17/2017.
 */

public class GridViewBrowseManga extends BaseAdapter implements Serializable {

    private Context context;
    private Manga[] manga;

    public GridViewBrowseManga(Context context,Manga[] manga){
        this.context = context;
        this.manga = manga;
    }
    @Override
    public int getCount() {
        return manga.length;
    }

    @Override
    public Manga getItem(int i) {
        return manga[i];
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
        Manga updateManga = getItem(i);
        textViewTitle.setText(updateManga.getTitle());
        textViewChapter.setVisibility(View.INVISIBLE);
        Picasso.with(context).load(updateManga.getCover()).fit().into(imageViewMain);
        view.setTag(updateManga);
        return view;
    }
}
