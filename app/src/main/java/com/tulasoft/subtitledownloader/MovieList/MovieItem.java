package com.tulasoft.subtitledownloader.MovieList;

public class MovieItem {
    public String MovieID;

    public String getMovieID() {
        return this.MovieID;
    }

    public void setMovieID(String value) {
        this.MovieID = value;
    }

    public String ResultType;

    public String getResultType() {
        return this.ResultType;
    }

    public void setResultType(String value) {
        this.ResultType = value;
    }

    public String Image;

    public String getImage() {
        return this.Image;
    }

    public void setImage(String value) {
        this.Image = value;
    }

    public String Title;

    public String getTitle() {
        return this.Title;
    }

    public void setTitle(String value) {
        this.Title = value;
    }

    public String Description;

    public String getDescription() {
        return this.Description;
    }

    public void setDescription(String value) {
        this.Description = value;
    }

    public MovieItem(){};
    public MovieItem(String id, String image, String title, String description) {
        super();
        this.MovieID = id;
        this.ResultType = "Title";
        this.Image = image;
        this.Title = title;
        this.Description = description;
    }

}
