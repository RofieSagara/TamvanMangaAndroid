package com.tamvan.tamvanmangaindonesia.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tamvan.tamvanmangaindonesia.R;
import com.tamvan.tamvanmangaindonesia.core.ChapterMangaTask;
import com.tamvan.tamvanmangaindonesia.core.InfoMangaTask;
import com.tamvan.tamvanmangaindonesia.custom.ListViewChapterAdapter;
import com.tamvan.tamvanmangaindonesia.holder.Chapter;
import com.tamvan.tamvanmangaindonesia.holder.Manga;
import com.tamvan.tamvanmangaindonesia.tools.BasicInputOuput;

public class InfoMangaActivity extends AppCompatActivity {

    private Handler handler;
    private ProgressBar progressBarMain;
    private ListView listView;
    private ListViewChapterAdapter dataAdapter;
    private Manga dataManga;
    private Manga newManga;
    private Button buttonFavorite;
    private InfoMangaTask infoMangaTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_manga);

        progressBarMain = (ProgressBar)findViewById(R.id.im_progressbar_main);
        listView = (ListView)findViewById(R.id.im_listview_main);
        buttonFavorite = (Button)findViewById(R.id.im_button_favorite);

        dataManga = (Manga)getIntent().getExtras().getSerializable("Data");

        handler = new Handler(Looper.getMainLooper()){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.arg1==1){ //sukse reader chapter
                    Bundle bundle = msg.getData();
                    ListViewChapterAdapter adapter = (ListViewChapterAdapter)bundle.get("adapter");
                    dataAdapter = adapter;
                    listView.setAdapter(dataAdapter);
                    adapter.notifyDataSetChanged();
                    progressBarMain.setVisibility(View.GONE);
                }else if(msg.arg1==2){ //gagal
                    Toast.makeText(InfoMangaActivity.this,"Gagal mendapatkan data!"
                            ,Toast.LENGTH_SHORT)
                            .show();
                    if(msg.arg2==1){
                        InfoMangaTask infoMangaTask = new InfoMangaTask(InfoMangaActivity.this
                                ,dataManga
                                ,handler);
                        infoMangaTask.execute();
                    }else if(msg.arg2==2){
                        ChapterMangaTask chapterMangaTask =
                                new ChapterMangaTask(
                                        InfoMangaActivity.this,
                                        handler,
                                        dataManga.getLink(),
                                        dataManga.getTitle(),
                                        dataManga.getNamaFolder());
                        chapterMangaTask.execute();
                    }
                }else if(msg.arg1==3){ // offline reader Chapter
                    Bundle bundle = msg.getData();
                    ListViewChapterAdapter adapter =
                            (ListViewChapterAdapter)bundle.get("adapter");
                    dataAdapter = adapter;
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    progressBarMain.setVisibility(View.VISIBLE);
                }else if(msg.arg1==5){ //Info sukses
                    Bundle bundle = msg.getData();
                    Manga dataManga = (Manga)bundle.getSerializable("Manga");
                    newManga = dataManga;
                    ImageView imageView = (ImageView)findViewById(R.id.im_imageview_cover);
                    Picasso.with(InfoMangaActivity.this).load(dataManga.getCover()).fit().into(imageView);
                    TextView textViewTitle = (TextView)findViewById(R.id.im_textview_title);
                    TextView textViewAltTitle = (TextView)findViewById(R.id.im_textview_altTitle);
                    TextView textViewTahun = (TextView)findViewById(R.id.im_textview_tahunRilis);
                    TextView textViewAuthor = (TextView)findViewById(R.id.im_textview_author);
                    TextView textViewArtist = (TextView)findViewById(R.id.im_textview_artist);
                    TextView textViewGenre = (TextView)findViewById(R.id.im_textview_genre);
                    TextView textViewSinop = (TextView)findViewById(R.id.im_textview_sinopsis);
                    textViewTitle.setText(dataManga.getTitle());
                    textViewAltTitle.setText(dataManga.getAltTitle());
                    textViewTahun.setText(dataManga.getTahunRilis());
                    textViewAuthor.setText(dataManga.getAuthor());
                    textViewArtist.setText(dataManga.getArtist());
                    textViewGenre.setText(dataManga.getGenre());
                    textViewSinop.setText(dataManga.getSinopsis());
                    if(!BasicInputOuput.isMangaFavorite(InfoMangaActivity.this,dataManga)){
                        buttonFavorite.setText("Favorite");
                    }else {
                        buttonFavorite.setText("Not a Favorite");
                    }
                }
            }
        };

        infoMangaTask = new InfoMangaTask(InfoMangaActivity.this,dataManga,handler);
        infoMangaTask.execute();
        ChapterMangaTask chapterMangaTask =
                new ChapterMangaTask(
                        InfoMangaActivity.this,
                        handler,
                        dataManga.getLink(),
                        dataManga.getTitle(),
                        dataManga.getNamaFolder());
        chapterMangaTask.execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        buttonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!BasicInputOuput.isMangaFavorite(InfoMangaActivity.this,dataManga)){
                    BasicInputOuput.WriteFavoriteMangaToFile(InfoMangaActivity.this,dataManga);
                    buttonFavorite.setText("Not a Favorite");
                }else {
                    BasicInputOuput.DeleteFavoriteMangaFromFile(InfoMangaActivity.this,dataManga);
                    buttonFavorite.setText("Favorite");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        infoMangaTask.cancel(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(newManga!=null && dataAdapter!=null) {
            Manga dataManga = newManga;
            ImageView imageView = (ImageView) findViewById(R.id.im_imageview_cover);
            Picasso.with(InfoMangaActivity.this).load(dataManga.getCover()).fit().into(imageView);
            TextView textViewTitle = (TextView) findViewById(R.id.im_textview_title);
            TextView textViewAltTitle = (TextView) findViewById(R.id.im_textview_altTitle);
            TextView textViewTahun = (TextView) findViewById(R.id.im_textview_tahunRilis);
            TextView textViewAuthor = (TextView) findViewById(R.id.im_textview_author);
            TextView textViewArtist = (TextView) findViewById(R.id.im_textview_artist);
            TextView textViewGenre = (TextView) findViewById(R.id.im_textview_genre);
            TextView textViewSinop = (TextView) findViewById(R.id.im_textview_sinopsis);
            textViewTitle.setText(dataManga.getTitle());
            textViewAltTitle.setText(dataManga.getAltTitle());
            textViewTahun.setText(dataManga.getTahunRilis());
            textViewAuthor.setText(dataManga.getAuthor());
            textViewArtist.setText(dataManga.getArtist());
            textViewGenre.setText(dataManga.getGenre());
            textViewSinop.setText(dataManga.getSinopsis());
            listView.setAdapter(dataAdapter);
            dataAdapter.notifyDataSetChanged();
            listView.setSelection(dataAdapter.getLastSelect());
            if(!BasicInputOuput.isMangaFavorite(InfoMangaActivity.this,dataManga)){
                buttonFavorite.setText("Favorite");
            }else {
                buttonFavorite.setText("Not a Favorite");
            }
        }
    }
}
