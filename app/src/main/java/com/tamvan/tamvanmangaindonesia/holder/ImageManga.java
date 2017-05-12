package com.tamvan.tamvanmangaindonesia.holder;

import java.io.Serializable;

/**
 * Created by srofi on 2/17/2017.
 */

public class ImageManga implements Serializable {
    private String[] link;
    private String[] localeImage;

    public String[] getLink() {
        return link;
    }

    public void setLink(String[] link) {
        this.link = link;
    }

    public String[] getLocaleImage() {
        return localeImage;
    }

    public void setLocaleImage(String[] localeImage) {
        this.localeImage = localeImage;
    }
}
