package com.tamvan.tamvanmangaindonesia.holder;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by srofi on 2/17/2017.
 */

public class Chapter implements Serializable {

    private String title;
    private String link;
    private String titleChapter;
    private String folderName;
    private ImageManga imageMangas;

    public ImageManga getImageMangas() {
        return imageMangas;
    }

    public void setImageMangas(ImageManga imageMangas) {
        this.imageMangas = imageMangas;
    }

    public String getTitleChapter() {
        return titleChapter;
    }

    public void setTitleChapter(String titleChapter) {
        this.titleChapter = titleChapter;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
