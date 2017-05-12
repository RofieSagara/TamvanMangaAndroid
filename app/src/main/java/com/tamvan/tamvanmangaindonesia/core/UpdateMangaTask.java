package com.tamvan.tamvanmangaindonesia.core;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.tamvan.tamvanmangaindonesia.custom.GridViewUpdateFragmentAdapter;
import com.tamvan.tamvanmangaindonesia.holder.UpdateManga;
import com.tamvan.tamvanmangaindonesia.tools.BasicInputOuput;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by srofi on 2/16/2017.
 */

public class UpdateMangaTask extends AsyncTask<Void,Integer,UpdateManga[]> {

    private Context _context;
    private ArrayList<UpdateManga> _dataManga;
    private Handler handler;

    public UpdateMangaTask(Context context, Handler mHandler){
        this._context = context;
        this.handler = mHandler;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        UpdateManga[] updateMangas = BasicInputOuput.ReadAllUpdateMangaUpdateFromFile(_context);
        if(updateMangas!=null) {
            GridViewUpdateFragmentAdapter adapter =
                    new GridViewUpdateFragmentAdapter(_context, updateMangas);
            Bundle bundle = new Bundle();
            bundle.putSerializable("adapter", adapter);
            Message message = new Message();
            message.setData(bundle);
            message.arg1 = 3;
            handler.sendMessage(message);
        }
    }

    @Override
    protected void onPostExecute(UpdateManga[] updateMangas) {
        super.onPostExecute(updateMangas);
        if(updateMangas!=null) {
            GridViewUpdateFragmentAdapter adapter =
                    new GridViewUpdateFragmentAdapter(_context, updateMangas);
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
    protected UpdateManga[] doInBackground(Void... voids) {
        String _LINK_LIST_LAST_UPDATE = "http://www.pecintakomik.com/";
        _dataManga = new ArrayList<>();
        try {
            if(isCancelled())return null;
            String contents = BasicInputOuput.DownloadSite(_LINK_LIST_LAST_UPDATE, _context);
            if(isCancelled())return null;
            if (contents != null) {
                Pattern pattern = Pattern.compile("<li><span class=\"date\">.*</a></li>");
                Matcher matcherRegex = pattern.matcher(contents);
                boolean isAds = false;
                int index = 1;
                while (matcherRegex.find()) {
                    UpdateManga data;
                    Pattern patternAdsCheck = Pattern.compile("\\[IKLAN\\]");
                    Matcher matcherAdsCheck = patternAdsCheck.matcher(matcherRegex.group());
                    if (matcherAdsCheck.find()) {
                        isAds = true;
                    } else {
                        isAds = false;
                    }
                    String temp;
                    Pattern patternLink = Pattern.compile("href='.*' rel=");
                    Matcher matcherLink = patternLink.matcher(matcherRegex.group());
                    if (matcherLink.find() && !isAds) {
                        temp = matcherLink.group();
                        temp = temp.replace("href='", "");
                        temp = temp.replace("' rel=", "");
                        String dataNameFolder = temp;
                        dataNameFolder = dataNameFolder.replace("/", "");
                        temp = "http://www.pecintakomik.com" + temp;
                        data = new UpdateManga();
                        data.setLink(temp);
                        data.setNameFolder(dataNameFolder);
                    } else {
                        data = new UpdateManga();
                        data.setLink("-");
                        data.setNameFolder("-");
                    }

                    Pattern patternCover = Pattern.compile("rel='.*' title=");
                    Matcher matcherCover = patternCover.matcher(matcherRegex.group());
                    if (matcherCover.find() && !isAds) {
                        temp = matcherCover.group();
                        temp = temp.replace("rel='", "");
                        temp = temp.replace("' title=", "");
                        temp = "http://www.pecintakomik.com" + temp;
                        data.setCover(temp);
                    } else {
                        data.setCover("-");
                    }

                    Pattern patternTitleUpdate = Pattern.compile("'>.*</a>");
                    Matcher matcherTitleUpdate = patternTitleUpdate.matcher(matcherRegex.group());
                    if (matcherTitleUpdate.find() && !isAds) {
                        temp = matcherTitleUpdate.group();
                        temp = temp.replace("'>", "");
                        temp = temp.replace("<img src=\"/images/hot.gif\"></a>", "");
                        temp = temp.replace("<img src=\"/images/end.gif\"></a>", "");
                        temp = temp.replace("<img src=\"/images/new.gif\"></a>", "");
                        temp = temp.replace("<img src=\"/images/hot.gif\">", "");
                        temp = temp.replace("<img src=\"/images/new.gif\">", "");
                        temp = temp.replace("<img src=\"/images/end.gif\">", "");
                        temp = temp.replace("</a>", "");
                        data.setTitleUpdate(temp);
                        int z = data.getTitleUpdate().lastIndexOf("-");
                        String chapter = data.getTitleUpdate().substring(z+1);
                        String titleName = data.getTitleUpdate().substring(0,z);
                        data.setTitle(titleName);
                        data.setEpisode(chapter);
                    } else {
                        data.setTitleUpdate("-");
                    }

                    if (!isAds) {
                        _dataManga.add(data);
                    }
                    index++;

                }
                UpdateManga[] dataTemp = _dataManga.toArray(new UpdateManga[_dataManga.size()]);
                UpdateManga[] dataFix = new UpdateManga[dataTemp.length];
                int j = 0;
                for (int i = 0; i <= dataTemp.length - 1; i++) {
                    for (int k = 0; k <= dataTemp.length - 1; k++) {
                        if (dataFix[k] == null) {
                            dataFix[k] = new UpdateManga();
                            dataFix[k] = dataTemp[i];
                            break;
                        }

                        int z = dataTemp[i].getTitleUpdate().lastIndexOf("-");
                        String titleName = dataTemp[i].getTitleUpdate().substring(0, z - 1);

                        if (dataFix[k].getTitleUpdate().contains(titleName)) {
                            break;
                        }
                    }
                }

                UpdateManga[] dataUpload = BasicInputOuput.RemoveNullUpdateManga(dataFix);
                BasicInputOuput.WriteAllUpdateMangaUpdateToFile(dataUpload, _context);
                return dataUpload;
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
