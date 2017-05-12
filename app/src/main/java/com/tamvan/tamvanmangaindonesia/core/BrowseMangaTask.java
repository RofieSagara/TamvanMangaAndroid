package com.tamvan.tamvanmangaindonesia.core;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.tamvan.tamvanmangaindonesia.custom.GridViewBrowseManga;
import com.tamvan.tamvanmangaindonesia.holder.Manga;
import com.tamvan.tamvanmangaindonesia.tools.BasicInputOuput;


import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by srofi on 2/17/2017.
 */

public class BrowseMangaTask extends AsyncTask<Void,Integer,Manga[]> {

    private ArrayList<Manga> dataManga;
    private Context context;
    private Handler handler;

    public BrowseMangaTask(Context context,Handler handler){
        this.context = context;
        this.dataManga = new ArrayList<>();
        this.handler = handler;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Manga[] mangas = BasicInputOuput.ReadAllMangaFromFile(context);
        if(mangas!=null){
            GridViewBrowseManga adapter =
                    new GridViewBrowseManga(context, mangas);
            Bundle bundle = new Bundle();
            bundle.putSerializable("adapter", adapter);
            Message message = new Message();
            message.setData(bundle);
            message.arg1 = 3;
            handler.sendMessage(message);
        }
    }

    @Override
    protected void onPostExecute(Manga[] mangas) {
        super.onPostExecute(mangas);
        if(mangas!=null) {
            GridViewBrowseManga adapter =
                    new GridViewBrowseManga(context, mangas);
            Bundle bundle = new Bundle();
            bundle.putSerializable("adapter", adapter);
            Message message = new Message();
            message.setData(bundle);
            message.arg1 = 1;
            handler.sendMessage(message);
        }else {
            Message message = new Message();
            message.arg1 = 2;
            handler.sendMessage(message);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Manga[] doInBackground(Void... voids) {
        String _LINK_LIST_ALL_MANGA = "http://www.pecintakomik.com/directory/";
        try {
            if(isCancelled())return null;
            String contents = BasicInputOuput.DownloadSite(_LINK_LIST_ALL_MANGA, context);
            if(isCancelled())return null;
            if (contents != null) {
                Pattern pattern = Pattern.compile("<li><a .*'>");
                Matcher matcherRegex = pattern.matcher(contents);
                int index = 1;
                while (matcherRegex.find()) {
                    Manga mangas;
                    Pattern patternLink = Pattern.compile("href='.*' rel");
                    String temp = matcherRegex.group();
                    Matcher matchLink = patternLink.matcher(temp);
                    matchLink.find();
                    temp = matchLink.group();
                    temp = temp.replace("href='", "");
                    temp = temp.replace("' rel", "");
                    String dataNameFolder = temp;
                    dataNameFolder = dataNameFolder.replace("/", "");
                    temp = "http://www.pecintakomik.com" + temp;

                    mangas = new Manga();
                    mangas.setLink(temp);
                    mangas.setNamaFolder(dataNameFolder);

                    Pattern patternTitle = Pattern.compile("title='.*'>");
                    temp = matcherRegex.group();
                    Matcher matcherTitle = patternTitle.matcher(temp);
                    matcherTitle.find();
                    temp = matcherTitle.group();
                    temp = temp.replace("title='", "");
                    temp = temp.replace("'>", "");

                    mangas.setTitle(temp);

                    Pattern patternSmalCover = Pattern.compile("' rel='.*' title");
                    temp = matcherRegex.group();
                    Matcher matcherSmalCover = patternSmalCover.matcher(temp);
                    matcherSmalCover.find();
                    temp = matcherSmalCover.group();
                    temp = temp.replace("' rel='", "");
                    temp = temp.replace("' title", "");
                    temp = "http://www.pecintakomik.com" + temp;

                    mangas.setCover(temp);
                    dataManga.add(mangas);

                    index++;
                }

                Manga[] temp = dataManga.toArray(new Manga[dataManga.size()]);
                BasicInputOuput.WriteAllMangaToFile(temp, context);
                return temp;
            } else {
                throw new Exception("Download Error!");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Log.d("Manga","Task Cancel");
    }
}
