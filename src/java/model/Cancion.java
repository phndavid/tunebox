/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author user
 */
public class Cancion {
    private String id;
    private String title;
    private String artist;
    private String album;
    private String poster;
    private String url_letra;
    private String url_video;
    private String genero;
    private String mp3;
    private String ogg;
    public Cancion(String id,String title, String artist, String album, String poster, String url_letra, String url_video,String mp3,String ogg, String genero) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.poster = poster;
        this.url_letra = url_letra;
        this.url_video = url_video;
        this.mp3 = mp3;
        this.ogg = ogg;
        this.genero = genero;
    }

    public String getId(){
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getArtist() {
        return artist;
    }
    public String getAlbum() {
        return album;
    }
    public String getUrl_imagen() {
        return poster;
    }
    public String getUrl_letra() {
        return url_letra;
    }
    public String getUrl_video() {
        return url_video;
    }
    public String getMp3(){
        return mp3;
    }
    public String getOgg(){
        return ogg;
    }
    public String getGenero() {
        return genero;
    }
}
