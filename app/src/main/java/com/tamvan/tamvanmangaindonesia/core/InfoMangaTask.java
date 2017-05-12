package com.tamvan.tamvanmangaindonesia.core;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.tamvan.tamvanmangaindonesia.holder.Manga;
import com.tamvan.tamvanmangaindonesia.tools.BasicInputOuput;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by srofi on 2/17/2017.
 */

public class InfoMangaTask extends AsyncTask<Void,Integer,Manga> {

    private Context context;
    private Manga data;
    private Handler handler;

    public InfoMangaTask(Context context, Manga data, Handler handler){
        this.context = context;
        this.data = data;
        this.handler = handler;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Manga manga) {
        super.onPostExecute(manga);
        if(manga!=null){
            Bundle bundle = new Bundle();
            bundle.putSerializable("Manga", manga);
            Message message = new Message();
            message.setData(bundle);
            message.arg1 = 5;
            handler.sendMessage(message);
        }else {
            Message message = new Message();
            message.arg1 = 2;
            message.arg2 = 1;
            handler.sendMessage(message);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Manga doInBackground(Void... voids) {
        try {
            if(isCancelled())return null;
            Manga datatemp = BasicInputOuput.ReadSingleMangaFromFile(context,data.getTitle());
            if(datatemp==null) {
                Manga finalData = new Manga();
                if(isCancelled())return null;
                String contents = BasicInputOuput.DownloadSite(data.getLink(), context);
                if(isCancelled())return null;
                String temp = "";

                if (contents != null) {
                    Pattern pattren2 = Pattern.compile("src=\".*\" alt");
                    Matcher match3 = pattren2.matcher(contents);
                    if (match3.find()) {
                        temp = match3.group();
                        temp = temp.replace("src=\"", "");
                        temp = temp.replace("\" alt", "");
                    } else {
                        temp = "-";
                    }

                    String fileSave = temp;
                    finalData.setCover("http://www.pecintakomik.com" + fileSave);

                    pattren2 = Pattern.compile("<h2>.*</h2>");
                    match3 = pattren2.matcher(contents);
                    if (match3.find()) {
                        temp = match3.group();
                        temp = temp.replace("<h2>", "");
                        temp = temp.replace("</h2>", "");
                    } else {
                        temp = "-";
                    }

                    finalData.setTitle(temp);

                    pattren2 = Pattern.compile("<li><strong>Nama Alternatif:</strong>.*</li>");
                    match3 = pattren2.matcher(contents);

                    if (match3.find()) {
                        temp = match3.group();
                        temp = temp.replace("<li><strong>Nama Alternatif:</strong>", "");
                        temp = temp.replace("</li>", "");
                    } else {
                        pattren2 = Pattern.compile("<li>Nama Alternatif:.*</li>");
                        match3 = pattren2.matcher(contents);
                        if (match3.find()) {
                            temp = match3.group();
                            temp = temp.replace("<li>Nama Alternatif:", "");
                            temp = temp.replace("</li>", "");
                        } else {
                            temp = "-";
                        }

                    }

                    finalData.setAltTitle(temp);

                    pattren2 = Pattern.compile("<li><strong>Tahun Rilis:</strong>.*</li>");
                    match3 = pattren2.matcher(contents);
                    if (match3.find()) {
                        temp = match3.group();
                        temp = temp.replace("<li><strong>Tahun Rilis:</strong>", "");
                        temp = temp.replace("</li>", "");
                    } else {
                        pattren2 = Pattern.compile("<li>Tahun Rilis:.*</li>");
                        match3 = pattren2.matcher(contents);
                        if (match3.find()) {
                            temp = match3.group();
                            temp = temp.replace("<li>Tahun Rilis:", "");
                            temp = temp.replace("</li>", "");
                        } else {
                            temp = "-";
                        }

                    }
                    finalData.setTahunRilis(temp);

                    pattren2 = Pattern.compile("<li><strong>Author.*</li>");
                    match3 = pattren2.matcher(contents);
                    if (match3.find()) {
                        temp = match3.group();
                        temp = temp.replace("<li><strong>Author(s):</strong>", "");
                        temp = temp.replace("</li>", "");
                    } else {
                        pattren2 = Pattern.compile("<li>Author.*</li>");
                        match3 = pattren2.matcher(contents);
                        if (match3.find()) {
                            temp = match3.group();
                            temp = temp.replace("<li>Author", "");
                            temp = temp.replace("</li>", "");
                        } else {
                            temp = "-";
                        }

                    }
                    finalData.setAuthor(temp);

                    pattren2 = Pattern.compile("<li><strong>Artist.*</li>");
                    match3 = pattren2.matcher(contents);
                    if (match3.find()) {
                        temp = match3.group();
                        temp = temp.replace("<li><strong>Artist(s):</strong>", "");
                        temp = temp.replace("</li>", "");
                    } else {
                        pattren2 = Pattern.compile("<li>Artist.*</li>");
                        match3 = pattren2.matcher(contents);
                        if (match3.find()) {
                            temp = match3.group();
                            temp = temp.replace("<li>Artist", "");
                            temp = temp.replace("</li>", "");
                        } else {
                            temp = "-";
                        }

                    }
                    finalData.setArtist(temp);

                    pattren2 = Pattern.compile("<li><strong>Genre:</strong>.*</li>");
                    match3 = pattren2.matcher(contents);
                    if (match3.find()) {
                        temp = match3.group();
                        temp = temp.replace("<li><strong>Genre:</strong>", "");
                        temp = temp.replace("</li>", "");
                    } else {
                        pattren2 = Pattern.compile("<li>Genre.*</li>");
                        match3 = pattren2.matcher(contents);
                        if (match3.find()) {
                            temp = match3.group();
                            temp = temp.replace("<li>Genre", "");
                            temp = temp.replace("</li>", "");
                        } else {
                            temp = "-";
                        }

                    }
                    finalData.setGenre(temp);

                    pattren2 = Pattern.compile("<li><strong>Sinopsis:</strong>.*</li>");
                    match3 = pattren2.matcher(contents);
                    if (match3.find()) {
                        temp = match3.group();
                        temp = temp.replace("<li><strong>Sinopsis:</strong>", "");
                        temp = temp.replace("</li>", "");
                    } else {
                        pattren2 = Pattern.compile("<li>Sinopsis:.*</li>");
                        match3 = pattren2.matcher(contents);
                        if (match3.find()) {
                            temp = match3.group();
                            temp = temp.replace("<li>Sinopsis:", "");
                            temp = temp.replace("</li>", "");
                        } else {
                            temp = "-";
                        }

                    }
                    finalData.setSinopsis(temp);
                    finalData.setLink(data.getLink());
                    finalData.setNamaFolder(data.getNamaFolder());
                    finalData.setCover(data.getCover());

                    return finalData;

                } else {
                    return null;
                }
            }else {
                return datatemp;
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
