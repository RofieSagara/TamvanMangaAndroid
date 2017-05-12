package com.tamvan.tamvanmangaindonesia.holder;

import java.io.Serializable;

/**
 * Created by srofi on 2/17/2017.
 */

public class Manga implements Serializable {
    private String link;
    private String namaFolder;
    private String title;
    private String altTitle;
    private String cover;
    private String tahunRilis;
    private String author;
    private String artist;
    private String genre;
    private String sinopsis;

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTahunRilis() {
        return tahunRilis;
    }

    public void setTahunRilis(String tahunRilis) {
        this.tahunRilis = tahunRilis;
    }

    public String getAltTitle() {
        return altTitle;
    }

    public void setAltTitle(String altTitle) {
        this.altTitle = altTitle;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNamaFolder() {
        return namaFolder;
    }

    public void setNamaFolder(String namaFolder) {
        this.namaFolder = namaFolder;
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

}
