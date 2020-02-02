package com.example.miwokapp;

public class word {

    /*
        Essa classe tem como objetivo guardar todas as inforações
        que serão usadas nos itens da lista
     */
    private String miwokWord;
    private String englishWord;

    public static final int noId = -1;
    private int imageId = noId;

    private int soundId = noId;

    public word(String a, String b){
        miwokWord=a;
        englishWord=b;
    }

    public word(String a, String b,int img){
        miwokWord=a;
        englishWord=b;
        imageId=img;
    }

    public word(String a, String b,int img,int som){
        miwokWord=a;
        englishWord=b;
        imageId=img;
        soundId = som;
    }

    public String getMiwokWord() {
        return miwokWord;
    }

    public void setMiwokWord(String miwokWord) {
        this.miwokWord = miwokWord;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public void setEnglishWord(String englishWord) {
        this.englishWord = englishWord;
    }

    public int getImageId() {return imageId; }

    public boolean hasImage(){ return imageId != noId;}

    public void setImageId(int imageId) { this.imageId = imageId;}

    public boolean hasSound(){ return soundId != noId;}

    public int getSoundId() { return soundId;  }

    public void setSoundId(int soundId) {  this.soundId = soundId;}
}
