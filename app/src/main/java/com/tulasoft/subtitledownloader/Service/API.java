package com.tulasoft.subtitledownloader.Service;

public class API {
    //private static final String IMDB_API_KEY = "k_u9ajpwef";
    private static final String IMDB_API_KEY = "k_fpmzjz67";
    //https://imdb-api.com/en/API/SearchTitle/k_u9ajpwef/the%20boys
    public static final String SearchTitle = "https://imdb-api.com/API/SearchTitle/" + IMDB_API_KEY +"/";
    //https://imdb-api.com/en/API/Title/k_u9ajpwef/tt1190634  --tv series detail
    //https://imdb-api.com/en/API/Title/k_u9ajpwef/tt7775902  --tv episode detail
    public static final String Detail = "https://imdb-api.com/en/API/Title/" + IMDB_API_KEY + "/";
    //https://imdb-api.com/en/API/SeasonEpisodes/k_u9ajpwef/tt1190634/3  --tv season episodes list
    public static final String SeasonEpisodes = "https://imdb-api.com/en/API/SeasonEpisodes/" + IMDB_API_KEY + "/";
    public static final String SearchSubtitle = "https://rest.opensubtitles.org/search";
    public static final String OpenSubtitleUserAgent = "TemporaryUserAgent";

}
