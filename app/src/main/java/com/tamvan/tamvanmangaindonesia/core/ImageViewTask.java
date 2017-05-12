package com.tamvan.tamvanmangaindonesia.core;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tamvan.tamvanmangaindonesia.holder.Chapter;
import com.tamvan.tamvanmangaindonesia.holder.ImageManga;
import com.tamvan.tamvanmangaindonesia.tools.BasicInputOuput;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by srofi on 2/17/2017.
 */

public class ImageViewTask extends AsyncTask<Void,String,Chapter> {

    private Context context;
    private Handler handler;
    private Chapter datachapter;

    public ImageViewTask(Context context, Handler handler,Chapter chapter) {
        this.context = context;
        this.handler = handler;
        this.datachapter = chapter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Message message = new Message();
        message.arg1 =1;
        handler.sendMessage(message);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("data",values[0]);
        message.arg1 =2;
        message.setData(bundle);
        handler.sendMessage(message);
    }

    @Override
    protected void onPostExecute(Chapter chapter) {
        super.onPostExecute(chapter);
        Message message = new Message();
        message.arg1 = 4;
        handler.sendMessage(message);
    }

    @Override
    protected Chapter doInBackground(Void... voids) {
        try {
            ImageManga imageManga = new ImageManga();
            String folder = "";
            String linkNext = "";
            ArrayList<String> data = new ArrayList<>();
            //String[] dataLocal = new String[_INDEX_FOR_IMAGE_MANGA];
            String linkFolder = datachapter.getLink();
            String folWithout = "";
            String chapter = "";
            String linkManga = "";
            linkFolder = linkFolder.replace("http://www.pecintakomik.com", "");
            linkNext = linkFolder;
            Log.d("Image Manga", linkFolder);
            Log.d("Image Manga", linkNext);

            String contents = "";
            String temp = "";
            linkNext = linkNext.replace("/manga", "");
            Log.d("Image Manga", linkNext);

            //GetFolder
            Pattern patternFolder = Pattern.compile("/.*/");
            Matcher matcherFolder = patternFolder.matcher(linkFolder);
            matcherFolder.find();
            folder = matcherFolder.group();
            folder = folder.replace("/manga", "");
            folder = folder.replace("/mangas", "");
            Log.d("Image Manga", folder);

            //Get Chapter, Temp Folder, Temp Fol Without underline
            Pattern patternChapter = Pattern.compile(folder + ".*");
            Matcher matcherChapter = patternChapter.matcher(linkFolder);
            matcherChapter.find();
            chapter = matcherChapter.group();
            chapter = chapter.replace(folder, "");
            chapter = chapter.replace(" ", "");
            Log.d("Image Manga", chapter);
            if (BasicInputOuput.CheckImageIsAlready(context, chapter, datachapter.getFolderName())) {
                imageManga = BasicInputOuput.ReadAllMangaImageFromFile(context, datachapter.getFolderName(), chapter);
                //Download manual if data list link was found.
            /*ArrayList<String> a = new ArrayList<>();
            for (int i = 0; i <= imageManga.getLink().length - 1; i++) {
                int z = imageManga.getLink()[i].lastIndexOf("/");
                String fileName = imageManga.getLink()[i].substring(z + 1);
                a.add(BasicInputOuput.DownloadImage(imageManga.getLink()[i], context, fileName));
            }
            String[] n = a.toArray(new String[a.size()]);
            imageManga.setLocaleImage(n);*/
                datachapter.setImageMangas(imageManga);
            } else {


                folWithout = folder.replace("_", " ");
                Log.d("Image Manga", folWithout);

                int index = 0;
                while (true) {
                    String tempArrayLinkChar = "";
                    try {
                        tempArrayLinkChar = linkNext;
                        char[] arrayLinkChar = linkNext.toCharArray();
                        if (arrayLinkChar[0] == '/') {
                            linkManga = "http://www.pecintakomik.com/manga" + linkNext;
                            if(isCancelled()) return null;
                            contents = BasicInputOuput.DownloadSite(linkManga, context);
                        } else {
                            linkNext = "/" + linkNext;
                            linkManga = "http://www.pecintakomik.com/manga" + linkNext;
                            if(isCancelled()) return null;
                            contents = BasicInputOuput.DownloadSite(linkManga, context);

                        }


                        Pattern patternData = Pattern.compile("<td><a href=\".*");
                        Matcher matcherData = patternData.matcher(contents);

                        Pattern patternDataAlter = Pattern.compile("<td><img src=\".*");
                        Matcher matcherDataAlter = patternDataAlter.matcher(contents);
                        String datain = "";
                        //dataLocal[index] = new String();
                        if (matcherData.find()) {
                            temp = matcherData.group();
                            Pattern patternIn = Pattern.compile("mangas" + folder + chapter + ".*\" alt");
                            Matcher matcherIn = patternIn.matcher(temp);
                            boolean matherBool1 = matcherIn.find();

                            Pattern patternOn = Pattern.compile("mangas" + folWithout + chapter + ".*\" alt");
                            Matcher matcherOn = patternOn.matcher(temp);
                            boolean matherBool2 = matcherOn.find();

                            Pattern patternAn = Pattern.compile("manga" + folder + chapter + ".*\" alt");
                            Matcher matcherAn = patternAn.matcher(temp);
                            boolean matherBool3 = matcherAn.find();

                            Pattern patternUn = Pattern.compile("manga" + folWithout + chapter + ".*\" alt");
                            Matcher matcherUn = patternUn.matcher(temp);
                            boolean matherBool4 = matcherUn.find();

                            if (matherBool1) {
                                datain = matcherIn.group();
                            } else if (matherBool2) {
                                datain = matcherOn.group();
                            } else if (matherBool3) {
                                datain = matcherAn.group();
                            } else if (matherBool4) {
                                datain = matcherUn.group();
                            }
                        } else if (matcherDataAlter.find()) {
                            temp = matcherDataAlter.group();
                            Pattern patternIn = Pattern.compile("mangas" + folder + chapter + ".*\" alt");
                            Matcher matcherIn = patternIn.matcher(temp);
                            boolean matherBool1 = matcherIn.find();

                            Pattern patternOn = Pattern.compile("mangas" + folWithout + chapter + ".*\" alt");
                            Matcher matcherOn = patternOn.matcher(temp);
                            boolean matherBool2 = matcherOn.find();

                            Pattern patternAn = Pattern.compile("manga" + folder + chapter + ".*\" alt");
                            Matcher matcherAn = patternAn.matcher(temp);
                            boolean matherBool3 = matcherAn.find();

                            Pattern patternUn = Pattern.compile("manga" + folWithout + chapter + ".*\" alt");
                            Matcher matcherUn = patternUn.matcher(temp);
                            boolean matherBool4 = matcherUn.find();

                            if (matherBool1) {
                                datain = matcherIn.group();
                            } else if (matherBool2) {
                                datain = matcherOn.group();
                            } else if (matherBool3) {
                                datain = matcherAn.group();
                            } else if (matherBool4) {
                                datain = matcherUn.group();
                            }
                        }

                        datain = datain.replace("\" alt", "");
                        datain = "/" + datain;

                        String linkImageDownload = "http://www.pecintakomik.com/manga" + datain;
                        datain = linkImageDownload; //Sementara
                        int z = datain.lastIndexOf("/");
                        String fileName = datain.substring(z + 1);

                        String extension = "";

                        int i = fileName.lastIndexOf('.');
                        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

                        if (i > p) {
                            extension = fileName.substring(i+1);
                        }

                        linkImageDownload = linkImageDownload.replace(" ", "%20");
                        datain = linkImageDownload;
                        String downloadPath = linkImageDownload;
                        if(isCancelled()) return null;
                        downloadPath = BasicInputOuput.DownloadImage(linkImageDownload, context, String.valueOf(index)+extension); //WARNING DOWNLOAD HERE
                        if(isCancelled())return null;
                        Log.d("Image Manga", datain);
                        Log.d("Image Manga Path",downloadPath);
                        data.add(downloadPath);
                        publishProgress(downloadPath);

                    } catch (Exception ex) {
                        linkNext = tempArrayLinkChar;
                        Log.d("Manga","Gagal download foto "+linkNext);
                        continue;
                    }
                    //GetLinkNext
                    Pattern patternLinkNext = Pattern.compile("href=\".*" + chapter + ".*\">");
                    Matcher matcherLinkNext = patternLinkNext.matcher(temp);
                    if (matcherLinkNext.find()) {
                        linkNext = matcherLinkNext.group();
                        linkNext = linkNext.replace("href=\"", "");
                        linkNext = linkNext.replace("\">", "");
                        Log.d("Image Manga", linkNext);
                        index++;
                    } else {
                        break;
                    }
                    int value = 30 + index;

                }

                String[] tp = data.toArray(new String[data.size()]);
                imageManga.setLink(tp);
                //String[] dl = BasicInputOuput.RemoveNullMangaImage(dataLocal);
                //imageManga.set_localImage(dl);
                datachapter.setImageMangas(imageManga);
                return datachapter;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return datachapter;
        }
        return datachapter;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Log.d("Manga","Task Cancel");
    }
}
