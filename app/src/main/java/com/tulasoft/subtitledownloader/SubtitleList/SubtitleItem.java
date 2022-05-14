package com.tulasoft.subtitledownloader.SubtitleList;

public class SubtitleItem {
    private String SubFileName;
    private String IDSubtitleFile;
    private String SubLanguageID;
    private String LanguageName;
    private String SubDownloadLink;

    public String getSubFileName() {
        return SubFileName;
    }

    public void setSubFileName(String subFileName) {
        SubFileName = subFileName;
    }

    public String getIDSubtitleFile() {
        return IDSubtitleFile;
    }

    public void setIDSubtitleFile(String IDSubtitleFile) {
        this.IDSubtitleFile = IDSubtitleFile;
    }

    public String getSubLanguageID() {
        return SubLanguageID;
    }

    public void setSubLanguageID(String subLanguageID) {
        SubLanguageID = subLanguageID;
    }

    public String getLanguageName() {
        return LanguageName;
    }

    public void setLanguageName(String languageName) {
        LanguageName = languageName;
    }

    public String getSubDownloadLink() {
        return SubDownloadLink;
    }

    public void setSubDownloadLink(String subDownloadLink) {
        SubDownloadLink = subDownloadLink;
    }

    public SubtitleItem() {
    }

    public SubtitleItem(String subFileName, String IDSubtitleFile, String subLanguageID, String languageName, String subDownloadLink) {
        SubFileName = subFileName;
        this.IDSubtitleFile = IDSubtitleFile;
        SubLanguageID = subLanguageID;
        LanguageName = languageName;
        SubDownloadLink = subDownloadLink;
    }
}
