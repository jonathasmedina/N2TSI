package com.example.n2tsi;

public class Filme {
    String titulo, ano, urlImg;

    public Filme() {
    }

    public Filme(String titulo, String ano, String urlImg) {
        this.titulo = titulo;
        this.ano = ano;
        this.urlImg = urlImg;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

}
