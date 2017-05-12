package com.tamvan.tamvanmangaindonesia.custom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tamvan.tamvanmangaindonesia.R;
import com.tamvan.tamvanmangaindonesia.activity.ViewImageActivity;
import com.tamvan.tamvanmangaindonesia.holder.Chapter;
import com.tamvan.tamvanmangaindonesia.tools.BasicInputOuput;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by srofi on 2/17/2017.
 */

public class ListViewChapterAdapter extends BaseAdapter implements Serializable {

    private Context context;
    private Chapter[] data;
    private List<String> already;
    private int lastSelect;

    public ListViewChapterAdapter(Context context,Chapter[] data){
        this.context = context;
        this.data = data;
        this.already = new ArrayList<>();
        already = BasicInputOuput.ReadChapterAlreadyRead(context);
    }

    public int getLastSelect(){
       return lastSelect;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        already = BasicInputOuput.ReadChapterAlreadyRead(context);
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Chapter getItem(int i) {
        return data[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.custom_chapter_manga,viewGroup,false);
        TextView textViewTitle = (TextView)view.findViewById(R.id.cm_textview_titleChapter);
        final int last = i;
        final Chapter updateManga = getItem(i);
        if(isAlready(updateManga.getTitleChapter())){
            textViewTitle.setTextColor(Color.parseColor("#FFFFFF"));
        }else {
            textViewTitle.setTextColor(Color.parseColor("#000000"));
        }
        textViewTitle.setText(updateManga.getTitleChapter());
        view.setTag(updateManga);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ViewImageActivity.class);
                intent.putExtra("Chapter",updateManga);
                context.startActivity(intent);
                BasicInputOuput.WriteChapterAlredyRead(context,updateManga);
                lastSelect = last;
            }
        });
        return view;
    }

    private boolean isAlready(String titleChapter){
        if(already!=null) {
            for (int i = 0; i < already.size(); i++) {
                if (already.get(i).equals(titleChapter)) {
                    return true;
                }
            }
        }
        return false;
    }
}
