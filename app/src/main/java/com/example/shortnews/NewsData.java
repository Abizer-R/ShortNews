package com.example.shortnews;

public class NewsData {
    private String newsSource;
    private String newsTitle;
    private String newsUrl;
    private String newsDateTime;
    private String thumbnailUrl;

    public NewsData(String newsSource, String newsTitle, String newsUrl, String newsDateTime, String thumbnail) {
        this.newsSource = newsSource;
        this.newsTitle = newsTitle;
        this.newsUrl = newsUrl;
        this.newsDateTime = newsDateTime;
        this.thumbnailUrl = thumbnail;
    }

    public String getNewsSource() {
        return newsSource;
    }

    public void setNewsSource(String newsSource) {
        this.newsSource = newsSource;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    public String getNewsDateTime() {
        return newsDateTime;
    }

    public void setNewsDateTime(String newsDateTime) {
        this.newsDateTime = newsDateTime;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnail) {
        this.thumbnailUrl = thumbnail;
    }
}
