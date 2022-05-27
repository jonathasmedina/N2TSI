package com.example.n2tsi;

public class Filme {
    String titulo, ano, descricao, urlImg;

    public Filme() {
    }

    public Filme(String titulo, String ano, String descricao, String urlImg) {
        this.titulo = titulo;
        this.ano = ano;
        this.descricao = descricao;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }
}
