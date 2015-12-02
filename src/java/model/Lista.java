/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;

public class Lista {

    private String nombre;
    private String emailUsuario; 
    private String rutaImg; 
    private String genero; 
    private ArrayList<Cancion> canciones;

    public Lista(String nombre, String emailUsuario, String rutaImg, String genero,ArrayList<Cancion> canciones) {
        this.nombre = nombre;
        this.emailUsuario = emailUsuario; 
        this.rutaImg = rutaImg; 
        this.genero = genero;
        this.canciones = canciones;
    }
    public String getRutaImg(){ 
        return rutaImg; 
    }
    public String getGenero(){ 
        return genero; 
    }
    public String getUsuario(){ 
        return emailUsuario; 
    }
    public void setUsuario(String email){ 
        this.emailUsuario = email; 
    }
    public void anadirCancion(Cancion c) {
        canciones.add(c);
    }

    public Cancion buscarCancion(Cancion c) {

        Cancion ret = null;

        if (canciones.size() > 0) {

            for (int i = 0; i < canciones.size(); i++) {

                Cancion laC = canciones.get(i);

                if (laC.getAlbum().equalsIgnoreCase(c.getAlbum())
                        && laC.getArtist().equalsIgnoreCase(c.getAlbum())
                        && laC.getTitle().equalsIgnoreCase(c.getTitle())
                        && laC.getGenero().equalsIgnoreCase(c.getGenero())) {

                    ret = laC;

                }
            }
        }
        return ret;

    }

    public int darPos(Cancion c) {

        int ret = -1;

        if (canciones.size() > 0 && buscarCancion(c) != null) {

            for (int i = 0; i < canciones.size(); i++) {

                Cancion laC = canciones.get(i);

                if (laC.getAlbum().equalsIgnoreCase(c.getAlbum())
                        && laC.getArtist().equalsIgnoreCase(c.getAlbum())
                        && laC.getTitle().equalsIgnoreCase(c.getTitle())
                        && laC.getGenero().equalsIgnoreCase(c.getGenero())) {

                    ret = i;

                }
            }
        }
        return ret;

    }

    public void remove(Cancion c) {

        if (buscarCancion(c) != null) {
            canciones.remove(darPos(c));
        }
    }

    public void setCancion(Cancion c, int pos) {

        if (buscarCancion(c) != null) {
            if (pos > -1 && pos <= canciones.size()) {
                Cancion laTe = canciones.get(pos);
                remove(laTe);
                canciones.add(pos, laTe);
            }
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Cancion> getCanciones() {
        return canciones;
    }
}
