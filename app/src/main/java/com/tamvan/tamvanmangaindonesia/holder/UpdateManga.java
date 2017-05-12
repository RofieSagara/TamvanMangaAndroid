package com.tamvan.tamvanmangaindonesia.holder;

import java.io.Serializable;

/**
 * Created by srofi on 2/16/2017.
 */

public class UpdateManga implements Serializable {
    private String title;
    private String cover;
    private String episode;
    private String link;
    private String nameFolder;
    private String titleUpdate;

    public String getTitleUpdate() {
        return titleUpdate;
    }

    public void setTitleUpdate(String titleUpdate) {
        this.titleUpdate = titleUpdate;
    }

    public String getNameFolder() {
        return nameFolder;
    }

    public void setNameFolder(String nameFolder) {
        this.nameFolder = nameFolder;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
