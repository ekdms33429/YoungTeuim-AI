package com.example.youngteuim;

public class Sentence {
    private String eng;
    private String kor;

    public Sentence(String eng, String kor) {
        this.eng = eng;
        this.kor = kor;
    }

    public String getEng() {
        return eng;
    }

    public void setEng(String eng) {
        this.eng = eng;
    }

    public String getKor() {
        return kor;
    }

    public void setKor(String kor) {
        this.kor = kor;
    }

    @Override
    public String toString() {
        return "Sentence{" +
                "eng='" + eng + '\'' +
                ", kor='" + kor + '\'' +
                '}';
    }
}
