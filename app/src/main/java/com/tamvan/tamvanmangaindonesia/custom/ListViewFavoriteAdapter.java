package com.tamvan.tamvanmangaindonesia.custom;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tamvan.tamvanmangaindonesia.R;
import com.tamvan.tamvanmangaindonesia.activity.InfoMangaActivity;
import com.tamvan.tamvanmangaindonesia.holder.Manga;

/**
 * Created by srofi on 2/17/2017.
 */

public class ListViewFavoriteAdapter extends BaseAdapter {

    public Manga[] dataManga;
    public Context context;

    public ListViewFavoriteAdapter(Context context,Manga[] dataManga){
        this.context = context;
        this.dataManga = dataManga;
    }

    @Override
    public int getCount() {
        return dataManga.length;
    }

    @Override
    public Manga getItem(int i) {
        return dataManga[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.custom_listview_fv,viewGroup,false);
        TextView textView = (TextView)view.findViewById(R.id.cfv_textview_main);
        ImageView imageView = (ImageView)view.findViewById(R.id.cfv_imageview_main);

        final Manga data = getItem(i);
        Picasso.with(context).load(data.getCover()).centerCrop().resize(100,50).into(imageView);
        textView.setText(data.getTitle());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, InfoMangaActivity.class);
                intent.putExtra("Data",data);
                context.startActivity(intent);
            }
        });
        return view;
    }
}
