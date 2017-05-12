package com.tamvan.tamvanmangaindonesia.core;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.tamvan.tamvanmangaindonesia.custom.ListViewChapterAdapter;
import com.tamvan.tamvanmangaindonesia.holder.Chapter;
import com.tamvan.tamvanmangaindonesia.holder.Manga;
import com.tamvan.tamvanmangaindonesia.tools.BasicInputOuput;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by srofi on 2/17/2017.
 */

public class ChapterMangaTask extends AsyncTask<Void,Integer,Chapter[]> {

    private Context context;
    private Handler handler;
    private String link;
    private String title;
    private String dataFolderName;

    public ChapterMangaTask(Context context, Handler handler, String link, String title, String dataFolderName) {
        this.context = context;
        this.handler = handler;
        this.link = link;
        this.title = title;
        this.dataFolderName = dataFolderName;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Chapter[] dataTemp = BasicInputOuput.ReadAllMangaChapterFromFile(context,title);
        if(dataTemp!=null){
            ListViewChapterAdapter adapter =
                    new ListViewChapterAdapter(context, dataTemp);
            Bundle bundle = new Bundle();
            bundle.putSerializable("adapter", adapter);
            Message message = new Message();
            message.setData(bundle);
            message.arg1 = 3;
            handler.sendMessage(message);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Chapter[] chapters) {
        super.onPostExecute(chapters);
        if(chapters!=null){
            ListViewChapterAdapter adapter =
                    new ListViewChapterAdapter(context, chapters);
            Bundle bundle = new Bundle();
            bundle.putSerializable("adapter", adapter);
            Message message = new Message();
            message.setData(bundle);
            message.arg1 = 1;
            handler.sendMessage(message);
        }else {
            Message message = new Message();
            message.arg1 = 2;
            message.arg2 = 2;
            handler.sendMessage(message);
        }
    }

    @Override
    protected Chapter[] doInBackground(Void... voids) {
        try {
            ArrayList<Chapter> dataChapter = new ArrayList<>();
            publishProgress(20,100);
            if(isCancelled())return null;
            String contents = BasicInputOuput.DownloadSite(link, context);
            if(isCancelled())return null;
            publishProgress(30,100);
            if (contents != null) {
                publishProgress(40,100);
                Pattern pattern = Pattern.compile("<li><a href=\".*<em>");
                Matcher matcherRegex = pattern.matcher(contents);
                int index = 1;
                String temp;
                while (matcherRegex.find()) {
                    publishProgress(index,matcherRegex.groupCount());
                    Chapter mangaChapters = new Chapter();
                    Pattern pattern1 = Pattern.compile("<li>.*\"> ");
                    Matcher matcher = pattern1.matcher(matcherRegex.group());
                    if (matcher.find()) {
                        temp = matcher.group();
                        temp = temp.replace("<li><a href=\"", "");
                        temp = temp.replace("\">", "");
                        temp = "http://www.pecintakomik.com" + temp;
                        mangaChapters.setLink(temp);
                    } else {
                        mangaChapters.setLink("-");
                    }

                    pattern1 = Pattern.compile("\">.*<em>");
                    matcher = pattern1.matcher(matcherRegex.group());
                    if (matcher.find()) {
                        temp = matcher.group();
                        temp = temp.replace("\">", "");
                        temp = temp.replace("<em>", "");
                        temp = temp.replace(" <img src=\"/images/new.gif", "");
                        temp = temp.replace(" <img src=\"/images/end.gif", "");
                        temp = temp.replace("<img src=\"/images/hot.gif", "");
                        temp = temp.replace("<img src=\"/images/end.gif", "");
                        temp = temp.replace("<img src=\"/images/new.gif", "");
                        mangaChapters.setTitleChapter(temp);
                    } else {
                        mangaChapters.setTitleChapter("-");
                    }
                    mangaChapters.setTitle(title);
                    mangaChapters.setFolderName(dataFolderName);
                    dataChapter.add(mangaChapters);
                    index++;
                }

                Chapter[] tempfinal = dataChapter.toArray(new Chapter[dataChapter.size()]);
                BasicInputOuput.WriteAllMangaChapterToFile(tempfinal, context, dataFolderName);
                return tempfinal;
            } else {
                return null;
            }
        } catch (Exception e) {
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
