/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;


public class Usuario {

    private String email;

    private String nombre;

    private String rol;

    private ArrayList<Lista> listasReproducciones;

    public Usuario(String email, String nombre) {
        this.email = email;
        this.nombre = nombre;
        listasReproducciones = new ArrayList<Lista>(); 
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<Lista> getListasReproducciones() {
        return listasReproducciones;
    }

    public void agregarLista(Lista lista) {
        listasReproducciones.add(lista);
    }
}