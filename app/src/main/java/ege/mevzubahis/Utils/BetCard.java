package ege.mevzubahis.Utils;

/**
 * Created by Acer Bilgisayar on 23.5.2017.
 */

public class BetCard {
    private String imgURL;
    private String title;

    public BetCard(String imgURL, String title) {
        this.imgURL = imgURL;
        this.title = title;

    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

