package com.tamvan.tamvanmangaindonesia.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.tamvan.tamvanmangaindonesia.R;
import com.tamvan.tamvanmangaindonesia.core.ImageViewTask;
import com.tamvan.tamvanmangaindonesia.custom.RecyleViewImageView;
import com.tamvan.tamvanmangaindonesia.holder.Chapter;
import com.tamvan.tamvanmangaindonesia.holder.ImageManga;

import java.util.ArrayList;
import java.util.List;

public class ViewImageActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMain;
    private Handler handler;
    private ProgressBar progressBarMain;
    private Chapter dataChapter;
    private RecyleViewImageView recyleViewImageView;
    private List<String> dataImage;
    private LinearLayoutManager linearLayoutManager;
    private ImageViewTask imageViewTask;
    private int lastIndexView = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        dataChapter = (Chapter) getIntent().getSerializableExtra("Chapter");
        progressBarMain = (ProgressBar)findViewById(R.id.vm_progressbar_main);
        linearLayoutManager = new LinearLayoutManager(this);

        handler = new Handler(Looper.getMainLooper()){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.arg1==1){//Loading
                    progressBarMain.setVisibility(View.VISIBLE);
                }else if(msg.arg1==2){//Pop Image
                    Bundle bundle = msg.getData();
                    String lnk = bundle.getString("data");
                    if(recyleViewImageView==null){
                        dataImage = new ArrayList<>();
                        dataImage.add(lnk);
                        recyleViewImageView = new
                                RecyleViewImageView(ViewImageActivity.this,dataImage);
                        recyclerViewMain = (RecyclerView)findViewById(R.id.vm_recyclerview_main);
                        recyclerViewMain.setAdapter(recyleViewImageView);
                        recyclerViewMain.setLayoutManager(linearLayoutManager);
                        recyclerViewMain.setItemAnimator(new DefaultItemAnimator());
                        recyclerViewMain.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                                lastIndexView = linearLayoutManager.findLastVisibleItemPosition();
                            }
                        });
                    }else {
                        recyleViewImageView.putNewData(lnk);
                        recyleViewImageView.notifyDataSetChanged();
                    }
                }else if(msg.arg1==3){//Error
                    progressBarMain.setVisibility(View.GONE);
                }else if(msg.arg1==4){//Done
                    progressBarMain.setVisibility(View.GONE);
                }
            }
        };
        setUp();
    }

    private void setUp(){
        imageViewTask = new ImageViewTask(ViewImageActivity.this,handler,dataChapter);
        imageViewTask.execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        imageViewTask.cancel(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(dataImage!=null) {
            recyleViewImageView = new
                    RecyleViewImageView(ViewImageActivity.this, dataImage);
            recyclerViewMain = (RecyclerView) findViewById(R.id.vm_recyclerview_main);
            recyclerViewMain.setAdapter(recyleViewImageView);
            recyclerViewMain.setLayoutManager(linearLayoutManager);
            recyclerViewMain.setItemAnimator(new DefaultItemAnimator());
            recyclerViewMain.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    lastIndexView = linearLayoutManager.findLastVisibleItemPosition();
                }
            });
            linearLayoutManager.scrollToPosition(lastIndexView);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recyleViewImageView = new
                RecyleViewImageView(ViewImageActivity.this,dataImage);
        recyclerViewMain = (RecyclerView)findViewById(R.id.vm_recyclerview_main);
        recyclerViewMain.setAdapter(recyleViewImageView);
        recyclerViewMain.setLayoutManager(linearLayoutManager);
        recyclerViewMain.setItemAnimator(new DefaultItemAnimator());
        recyclerViewMain.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                lastIndexView = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
        linearLayoutManager.scrollToPosition(lastIndexView);
    }
}
