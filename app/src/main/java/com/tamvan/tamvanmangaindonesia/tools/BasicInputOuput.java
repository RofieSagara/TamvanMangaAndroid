package com.tamvan.tamvanmangaindonesia.tools;

import android.content.Context;
import android.util.Log;

import com.tamvan.tamvanmangaindonesia.holder.Chapter;
import com.tamvan.tamvanmangaindonesia.holder.ImageManga;
import com.tamvan.tamvanmangaindonesia.holder.Manga;
import com.tamvan.tamvanmangaindonesia.holder.UpdateManga;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by srofi on 2/16/2017.
 */

public class BasicInputOuput {

    public static String DownloadSite(String urlData, Context context) throws Exception {
        try {
            URL url = new URL(urlData);
            File file = new File(context.getFilesDir().getAbsolutePath()+"/"+"data.bmi");

            Log.d("ImangeManager", "Download Dimulai");
            Log.d("ImangeManager", "Download url: " + url);

            //Membuka koneksi
            URLConnection ucon = url.openConnection();

            InputStream inputStream = ucon.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] data = new byte[50];
            int current = 0;

            while ((current = bufferedInputStream.read(data, 0, data.length)) != -1) {
                byteArrayOutputStream.write(data, 0, current);
            }

            //Disimpan di stream
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
            fileOutputStream.close();

            int length = (int) file.length();
            byte[] bytes = new byte[length];

            FileInputStream in = new FileInputStream(context.getFilesDir().getAbsolutePath()+"/"+"data.bmi");
            in.read(bytes);
            in.close();
            //Disimpan ke String

            Log.d("ImangeManager", "Download finish!");
            return new String(bytes);     //data mentah hmtl situs
        } catch (Exception e) {
            Log.d("ImangeManager", "Error:" + e);
            throw new Exception("Error in Match");
        }
    }

    public static UpdateManga[] RemoveNullUpdateManga(UpdateManga[] data){
        UpdateManga[] mangas = new UpdateManga[CountSizeArrayUpdateManga(data)];
        int index = 0;
        while (true){
            if(data[index]!=null){
                mangas[index] = data[index];
                index++;
            }else {
                break;
            }
        }
        return mangas;
    }

    private static int CountSizeArrayUpdateManga(UpdateManga[] data) {
        int index = 0;
        while (true) {
            if (data[index] != null) {
                index++;
            } else {
                break;
            }
        }
        return index;
    }

    public static void WriteAllUpdateMangaUpdateToFile(UpdateManga[] data,Context context){
        ObjectOutput objectOutput = null;
        try{
            objectOutput = new ObjectOutputStream(new FileOutputStream(new File(context.getFilesDir().getAbsolutePath()+"/"+"dataAllUpdateManga.bmi")));
            objectOutput.writeObject(data);
            objectOutput.close();
        }catch (Exception e){
            Log.d("ServiceAnime", "Error Write: " + e.getMessage());
        }
    }

    public static UpdateManga[] ReadAllUpdateMangaUpdateFromFile(Context context){
        ObjectInputStream objectInputStream;
        UpdateManga[] data = null;
        try{
            objectInputStream = new ObjectInputStream(new FileInputStream(new File(context.getFilesDir().getAbsolutePath()+"/"+"dataAllUpdateManga.bmi")));
            data = (UpdateManga[])objectInputStream.readObject();
            objectInputStream.close();
        }catch (Exception e){
            Log.d("ServiceAnime", "Error Read: " + e.getMessage());
            WriteAllUpdateMangaUpdateToFile(data, context);
            return data;
        }
        return data;
    }

    public static Manga[] RemoveNullManga(Manga[] data){
        Manga[] mangas = new Manga[CountSizeArrayManga(data)];
        int index = 0;
        while (true){
            if(data[index]!=null){
                mangas[index] = data[index];
                index++;
            }else {
                break;
            }
        }
        return mangas;
    }

    private static int CountSizeArrayManga(Manga[] data) {
        int index = 0;
        while (true) {
            if (data[index] != null) {
                index++;
            } else {
                break;
            }
        }
        return index;
    }

    public static void WriteAllMangaToFile(Manga[] data,Context context){
        ObjectOutput objectOutput = null;
        try{
            objectOutput = new ObjectOutputStream(new FileOutputStream(new File(context.getFilesDir().getAbsolutePath()+"/"+"dataAllManga.bmi")));
            objectOutput.writeObject(data);
            objectOutput.close();
        }catch (Exception e){
            Log.d("ServiceAnime", "Error Write: " + e.getMessage());
        }
    }

    public static Manga[] ReadAllMangaFromFile(Context context){
        ObjectInputStream objectInputStream;
        Manga[] data = null;
        try{
            objectInputStream = new ObjectInputStream(new FileInputStream(new File(context.getFilesDir().getAbsolutePath()+"/"+"dataAllManga.bmi")));
            data = (Manga[])objectInputStream.readObject();
            objectInputStream.close();
        }catch (Exception e){
            Log.d("ServiceAnime", "Error Read: " + e.getMessage());
            WriteAllMangaToFile(data, context);
            return data;
        }
        return data;
    }

    public static Manga ReadSingleMangaFromFile(Context context,String title){
        ObjectInputStream objectInputStream;
        Manga data = null;
        try{
            objectInputStream = new ObjectInputStream(new FileInputStream(new File(context.getFilesDir().getAbsolutePath()+"/"+title+"/dataMangaInfo.bmi")));
            data = (Manga)objectInputStream.readObject();
            objectInputStream.close();
        }catch (Exception e){
            Log.d("ServiceAnime", "Error Read: " + e.getMessage());
            return data;
        }
        return data;
    }

    public static void WriteSingleMangaToFile(Manga data,Context context,String title){
        ObjectOutput objectOutput = null;
        File file = new File(context.getFilesDir().getAbsolutePath() + "/" + title + "/");
        file.mkdirs();
        try{
            objectOutput = new ObjectOutputStream(new FileOutputStream(new File(context.getFilesDir().getAbsolutePath()+"/"+title+"/dataMangaInfo.bmi")));
            objectOutput.writeObject(data);
            objectOutput.close();
        }catch (Exception e){
            Log.d("ServiceAnime", "Error Write: " + e.getMessage());
        }
    }

    public static void WriteAllMangaChapterToFile(Chapter[] data, Context context, String title){
        ObjectOutput objectOutput = null;
        try{
            objectOutput = new ObjectOutputStream(new FileOutputStream(new File(context.getFilesDir().getAbsolutePath()+"/"+title+"/dataMangaChapter.bmi")));
            objectOutput.writeObject(data);
            objectOutput.close();
        }catch (Exception e){
            Log.d("ServiceAnime", "Error Write: " + e.getMessage());
        }
    }

    public static Chapter[] ReadAllMangaChapterFromFile(Context context,String title){
        ObjectInputStream objectInputStream;
        Chapter[] data = null;
        try{
            objectInputStream = new ObjectInputStream(new FileInputStream(new File(context.getFilesDir().getAbsolutePath()+"/"+title+"/dataMangaChapter.bmi")));
            data = (Chapter[])objectInputStream.readObject();
            objectInputStream.close();
        }catch (Exception e){
            Log.d("ServiceAnime", "Error Read: " + e.getMessage());
            WriteAllMangaChapterToFile(data, context, title);
            return data;
        }
        return data;
    }

    public static Manga ReadFavoriteMangaFromFile(Context context,Manga dataManga){
        ObjectInputStream objectInputStream;
        Manga data = null;
        try{
            objectInputStream = new ObjectInputStream(new FileInputStream(new File(context.getFilesDir().getAbsolutePath()+"/bookmark/"+dataManga.getNamaFolder()+".chp")));
            data = (Manga)objectInputStream.readObject();
            objectInputStream.close();
        }catch (Exception e){
            Log.d("ServiceAnime", "Error Read: " + e.getMessage());
            return data;
        }
        return data;
    }

    public static boolean WriteChapterAlredyRead(Context context,Chapter dataChapter){
        ObjectOutput objectOutput = null;
        File file = new File(context.getFilesDir().getAbsoluteFile()+"/Already/");
        file.mkdir();
        List<String> data = ReadChapterAlreadyRead(context);
        if(data==null){
            data = new ArrayList<>();
        }
        data.add(dataChapter.getTitleChapter());
        try{
            objectOutput = new ObjectOutputStream(new FileOutputStream(new File(context.getFilesDir().getAbsolutePath()+"/Already/Already.chp")));
            objectOutput.writeObject(data);
            objectOutput.close();
            return true;
        }catch (Exception ex){
            Log.d("ServiceAnime", "Error Write: " + ex.getMessage());
            return false;
        }
    }

    public static List<String> ReadChapterAlreadyRead(Context context){
        ObjectInputStream objectInputStream;
        File file = new File(context.getFilesDir().getAbsoluteFile()+"/Already/");
        file.mkdir();
        List<String> data = null;
        try{
            objectInputStream = new ObjectInputStream(new FileInputStream(new File(context.getFilesDir().getAbsolutePath()+"/Already/Already.chp")));
            data = (List<String>)objectInputStream.readObject();
            objectInputStream.close();
        }catch (Exception e){
            Log.d("ServiceAnime", "Error Read: " + e.getMessage());
            return data;
        }
        return data;
    }

    public static boolean isMangaFavorite(Context context,Manga dataManga){
        Manga data = null;
        data = BasicInputOuput.ReadFavoriteMangaFromFile(context,dataManga);
        if(data!=null){
            return true;
        }else {
            return false;
        }
    }

    public static boolean WriteFavoriteMangaToFile(Context context,Manga chapter){
        ObjectOutput objectOutput = null;
        File file = new File(context.getFilesDir().getAbsolutePath()+"/bookmark/");
        file.mkdirs();
        try{
            objectOutput = new ObjectOutputStream(new FileOutputStream(new File(context.getFilesDir().getAbsolutePath()+"/bookmark/"+chapter.getNamaFolder()+".chp")));
            objectOutput.writeObject(chapter);
            objectOutput.close();
            return true;
        }catch (Exception e){
            Log.d("ServiceAnime", "Error Write: " + e.getMessage());
            return false;
        }
    }

    public static boolean DeleteFavoriteMangaFromFile(Context context,Manga chapter){
        File file = new File(context.getFilesDir().getAbsolutePath() + "/bookmark/" + chapter.getNamaFolder() + ".chp");
        return file.delete();
    }

    public static Manga[] ReadAllFavorite(Context context){
        File file = new File(context.getFilesDir().getAbsolutePath()+"/bookmark/");
        ArrayList<Manga> data = new ArrayList<>();
        if(file.isDirectory()){

            for (File child : file.listFiles()) {
                data.add(BasicInputOuput.ReadFavoriteMangaFromFile(context, child));
            }
        }
        Manga[] datatemp = data.toArray(new Manga[data.size()]);
        return datatemp;
    }

    public static Manga ReadFavoriteMangaFromFile(Context context,File path){
        ObjectInputStream objectInputStream;
        Manga data = null;
        try{
            objectInputStream = new ObjectInputStream(new FileInputStream(path));
            data = (Manga)objectInputStream.readObject();
            objectInputStream.close();
        }catch (Exception e){
            Log.d("ServiceAnime", "Error Read: " + e.getMessage());
            return data;
        }
        return data;
    }

    public static boolean CheckImageIsAlready(Context context,String chapter,String title){
        File file = new File(context.getFilesDir().getAbsolutePath()+"/"+title+"/"+chapter+".chp");
        if(file.exists()){
            return true;
        }else {
            return false;
        }

    }

    public static String DownloadImage(String urlData, Context context,String fileName) throws Exception{
        try {
            URL url = new URL(urlData);
            File file = new File(context.getFilesDir().getAbsolutePath()+"/tempManga/");
            file.mkdirs();

            Log.d("ImangeManager", "Download Dimulai");
            Log.d("ImangeManager", "Download url: " + url);
            Log.d("ImangeManager", "Download Save: " + file.toString());

            //Membuka koneksi
            URLConnection ucon = url.openConnection();

            InputStream inputStream = ucon.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] data = new byte[50];
            int current = 0;

            while ((current = bufferedInputStream.read(data, 0, data.length)) != -1) {
                byteArrayOutputStream.write(data, 0, current);
            }

            //Disimpan di stream
            FileOutputStream fileOutputStream = new FileOutputStream(file+"/"+fileName);
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
            fileOutputStream.close();

            int length = (int) file.length();
            byte[] bytes = new byte[length];

            /*FileInputStream in = new FileInputStream(context.getFilesDir().getAbsolutePath()+"/tempManga/"+fileName);
            in.read(bytes);
            in.close();*/
            //Disimpan ke String
            String fileLocal = context.getFilesDir().getAbsolutePath()+"/tempManga/"+fileName;
            Log.d("ImangeManager", "Download finish!");
            return fileLocal;
        } catch (Exception e) {
            Log.d("ImangeManager", "Error:" + e);
            throw new Exception("Error Link");
        }
    }

    public static ImageManga ReadAllMangaImageFromFile(Context context, String title, String chapter){
        ObjectInputStream objectInputStream;
        ImageManga data = null;
        try{
            objectInputStream = new ObjectInputStream(new FileInputStream(new File(context.getFilesDir().getAbsolutePath()+"/"+title+"/"+chapter+".chp")));
            data = (ImageManga)objectInputStream.readObject();
            objectInputStream.close();
        }catch (Exception e){
            Log.d("ServiceAnime", "Error Read: " + e.getMessage());
            return data;
        }
        return data;
    }
}
