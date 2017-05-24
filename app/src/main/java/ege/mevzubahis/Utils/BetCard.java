package ege.mevzubahis.Utils;

/**
 * Created by Acer Bilgisayar on 23.5.2017.
 */

public class BetCard {
    private String imgURL;
    private String title;
    private String duration;
    private String senderName;
    private String coin;

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public BetCard(String imgURL, String title, String duration,String coin,String senderName) {
        this.imgURL = imgURL;
        this.title = title;
        this.coin= coin;
        this.senderName = senderName;
        this.duration = duration;

    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
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

