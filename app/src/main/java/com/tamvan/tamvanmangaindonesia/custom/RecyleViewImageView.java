package com.tamvan.tamvanmangaindonesia.custom;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tamvan.tamvanmangaindonesia.R;
import com.tamvan.tamvanmangaindonesia.holder.ImageManga;

import java.io.File;
import java.util.List;

/**
 * Created by srofi on 5/7/2017.
 */

public class RecyleViewImageView extends RecyclerView.Adapter<RecyleViewImageView.ViewHolder> {

    private Context context;
    private List<String> imageMangas;
    private int iSelect;

    public RecyleViewImageView(Context context, List<String> data){
        this.context = context;
        this.imageMangas = data;
    }

    public int getLastRead(){
        return iSelect;
    }

    public void putNewData(String link){
        imageMangas.add(link);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_listview_view_image, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;
        Uri uri = Uri.fromFile(new File(imageMangas.get(pos)));
        Picasso.with(context)
                .load(uri)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return imageMangas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public PhotoView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (PhotoView) itemView.findViewById(R.id.lvm_imageview_main);
        }
    }
}
