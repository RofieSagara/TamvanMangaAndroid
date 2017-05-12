package com.tamvan.tamvanmangaindonesia.custom;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tamvan.tamvanmangaindonesia.R;
import com.tamvan.tamvanmangaindonesia.holder.Chapter;
import com.tamvan.tamvanmangaindonesia.holder.ImageManga;

/**
 * Created by srofi on 2/17/2017.
 */

public class ListViewImageView extends BaseAdapter {

    private Context context;
    private Chapter chapter;
    private ImageManga dataImage;

    public ListViewImageView(Context context, Chapter chapter){
        this.context = context;
        this.chapter = chapter;
        this.dataImage = chapter.getImageMangas();
    }

    @Override
    public int getCount() {
        return dataImage.getLink().length;
    }

    @Override
    public String getItem(int i) {
        return dataImage.getLink()[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.custom_listview_view_image,viewGroup,false);
        ImageView imageView = (ImageView)view.findViewById(R.id.lvm_imageview_main);
        String dataLink = getItem(i);
        Picasso.with(context).load(dataLink).fit().into(imageView);
        view.setTag(dataLink);
        return view;
    }
}
