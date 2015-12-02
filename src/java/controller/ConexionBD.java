/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Cancion;
import model.Lista;
import model.Usuario;

/**
 *
 * @author user
 */
public class ConexionBD {

    private static Connection con = null;

    public ConexionBD() {
        conectarBD();
    }

    public static void conectarBD() {
        //loading driver 
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //creating connection with the database 
            String servidor = "jdbc:mysql://200.3.193.22/P09681_1_1";
            String usuarioDB = "P09681_1_1";
            String passwordDB = "MWrVIjQj";
            con = DriverManager.getConnection(servidor, usuarioDB, passwordDB);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkUser(String email, String pass) {
        boolean st = false;
        try {
            conectarBD();
            //Statement comando = con.createStatement(); 
            PreparedStatement ps = con.prepareStatement("select * from Usuario where email=? and md5_contraseña=?");
            ps.setString(1, email);
            ps.setString(2, pass);
            //ResultSet rs =comando.executeQuery("select * from Usuarios where User="+"'"+email+"'" + " and Password="+"'"+pass+"'");
            //ResultSet elimina = comando.executeQuery("DELETE * FROM Usuarios WHERE User='Nelson'");
            ResultSet rs = ps.executeQuery();
            st = rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return st;
    }

    public static boolean buscarUsuario(String email) {
        boolean existe = false;
        conectarBD();
        try {
            Statement comando = con.createStatement();
            ResultSet rs = comando.executeQuery("SELECT email FROM Usuario WHERE email='" + email + "'");
            if (rs.next()) {
                existe = true;
            }
        } catch (Exception e) {
        }
        return existe;
    }

    public static boolean registerUser(String user, String pass, String email) {
        boolean registered = false;
        try {

            conectarBD();
            String usuario = "'" + user + "'";
            String contra = "'" + pass + "'";
            String emailo = "'" + email + "'";
            Statement comando = con.createStatement();
            comando.executeUpdate("insert into Usuario (email,nombre,md5_contraseña) values (" + emailo + "," + usuario + "," + contra + ")");
            comando.executeUpdate("INSERT INTO RolActual (email_usuario, rol_actual) VALUES ('" + email + "','oyente')");
            registered = true;

        } catch (Exception ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return registered;
    }

    public static boolean buscarLista(String email, String nombreLista) {
        boolean tiene = false;
        if (!buscarUsuario(email)) {
            try {
                Statement comando = con.createStatement();
                ResultSet rs = comando.executeQuery("SELECT nombre FROM Lista_Reproduccion WHERE email_usuario='" + email + "' AND nombre '" + nombreLista + "'");
                if (rs.next()) {
                    tiene = true;
                }
            } catch (Exception e) {
            }
        }
        return tiene;
    }

    public static boolean crearLista(String email, String nombreLista, String genero, String ruta_img) {
        boolean creo = false;
        conectarBD();
        //if (!buscarLista(email, nombreLista)) {
        try {
            String email_usurario = "'" + email + "'";
            String nombre = "'" + nombreLista + "'";
            String elGenero = "'" + genero + "'";
            String ruta = "'" + ruta_img + "'";
            String cantidad_clientes = "'"+0+"'";
            Statement comando = con.createStatement();
            comando.executeUpdate("INSERT INTO Lista_Reproduccion (email_usuario, nombre, genero, ruta_img,cantidad_clientes) VALUES(" + email_usurario + "," + nombre + "," + elGenero + "," + ruta + ","+cantidad_clientes+")");
            creo = true;
        } catch (Exception e) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, e);
        }
        //}
        return creo;
    }

    public static boolean agregarCancionALista(String email,String nombreLista, String idCancion ) {
        boolean agrego = false;

        try {
            Statement comando = con.createStatement();
            comando.executeUpdate("INSERT INTO Lista_Reproduccion_Cancion(usuario_lista,nombre_lista,id_cancion) VALUES('" + email + "','" + nombreLista + "','" + idCancion + "')");
            agrego = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return agrego;
    }

  
    public static ArrayList<Lista> darListasSegunUsuario(String nombreUsuario) {
        ArrayList<Lista> listas = new ArrayList<>();
        conectarBD();
        try {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM Lista_Reproduccion WHERE email_usuario= '" + nombreUsuario + "'");
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String usuario = rs.getString("email_usuario");
                String rutaImg = rs.getString("ruta_img");
                String genero = rs.getString("genero");
                ArrayList<Cancion> canciones = new ArrayList<>();
                Lista lista = new Lista(nombre, usuario, rutaImg, genero,canciones);
                listas.add(lista);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listas;
    }

    public static ArrayList<Lista> cargarListasUsuario(String usuario) {
        conectarBD();
        ArrayList<Lista> listas = new ArrayList<>();
        try {
            Statement s = con.createStatement();
            String nombreUsuario = usuario;
            ResultSet rs = s.executeQuery("SELECT * FROM Lista_Reproduccion WHERE email_usuario= '" + nombreUsuario + "'");
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String usuarioLista = rs.getString("email_usuario");
                String rutaImg = rs.getString("ruta_img");
                String genero = rs.getString("genero");
                ArrayList<Cancion> canciones = new ArrayList<>();
                Statement s1 = con.createStatement();
                ResultSet rs1 = s1.executeQuery("SELECT id_cancion FROM Lista_Reproduccion_Cancion WHERE usuario_lista= '" + nombreUsuario + "' AND nombre_lista= '" + nombre + "'");
                while (rs1.next()) {
                    String idCancion = rs1.getString("id_cancion");
                    Cancion cancion = buscarCancion(idCancion);
                    canciones.add(cancion);
                }
                Lista lista = new Lista(nombre, usuarioLista, rutaImg, genero,canciones);
                listas.add(lista);
            }
        } catch (Exception e) {
        }
        return listas;
    }
    public static ArrayList<Lista> cargarListas() {
        ArrayList<Lista> listas = new ArrayList<>();
        conectarBD();
        try {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM Lista_Reproduccion");
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String usuario = rs.getString("email_usuario");
                String rutaImg = rs.getString("ruta_img");
                String genero = rs.getString("genero");
                 ArrayList<Cancion> canciones = new ArrayList<>();
                Statement s1 = con.createStatement();
                ResultSet rs1 = s1.executeQuery("SELECT id_cancion FROM Lista_Reproduccion_Cancion WHERE usuario_lista= '" + usuario + "' AND nombre_lista= '" + nombre + "'");
                while (rs1.next()) {
                    String idCancion = rs1.getString("id_cancion");
                    Cancion cancion = buscarCancion(idCancion);
                    canciones.add(cancion);
                }
                Lista lista = new Lista(nombre, usuario, rutaImg, genero,canciones);
                listas.add(lista);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listas;
    }

    public static Usuario darUsuario(String email) {
        Usuario usuario = null;
        conectarBD();
        try {
            Statement comando = con.createStatement();
            ResultSet rs = comando.executeQuery("SELECT email, nombre, rol_actual FROM Usuario, RolActual WHERE email='" + email + "' AND email_usuario='" + email + "'");
            if (rs.next()) {
                String emailU = rs.getString("email");
                String nombreU = rs.getString("nombre");
                String rolU = rs.getString("rol_actual");
                usuario = new Usuario(emailU, nombreU);
                usuario.setRol(rolU);
            }
        } catch (Exception e) {
        }
        return usuario;
    }

    public static boolean cambiarRol(String email, String rolActual) {
        boolean cambio = false;
        conectarBD();
        try {
            Statement s = con.createStatement();
            if (rolActual.equals("oyente")) {
                s.executeUpdate("UPDATE RolActual SET rol_actual='dj' WHERE email_usuario='" + email + "'");
                cambio = true;
            }
            if (rolActual.equals("dj")) {
                s.executeUpdate("UPDATE RolActual SET rol_actual='oyente' WHERE email_usuario='" + email + "'");
                cambio = true;
            }

        } catch (Exception e) {
        }
        return cambio;
    }

    public static void incrementarEnUnoClientesLista(String nombreLista) {
        conectarBD();
        try {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT cantidad_clientes FROM Lista_Reproduccion WHERE nombre ='" + nombreLista + "'");
            if (rs.next()) {
                int cantidadClientesLista = rs.getInt("cantidad_clientes");
                int nuevosClientes = cantidadClientesLista + 1;
                s.executeUpdate("UPDATE Lista_Reproduccion SET cantidad_clientes = '" + nuevosClientes + "' WHERE nombre ='" + nombreLista + "'");
            }
        } catch (Exception e) {
        }
    }

    public static ArrayList<String> ordenarListasPorCantidadClientes() {
        ArrayList<String> clientes = new ArrayList<>();
        conectarBD();
        try {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT nombre FROM Lista_Reproduccion ORDER BY cantidad_clientes desc");
            while (rs.next()) {
                String cantidadCliente = rs.getString("nombre");
                clientes.add(cantidadCliente);
            }
        } catch (Exception e) {
        }
        return clientes;
    }

    public static ArrayList<String> ordenarListasPorCantidadClientesAutor() {
        ArrayList<String> clientes = new ArrayList<>();
        conectarBD();
        try {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT email_usuario FROM Lista_Reproduccion ORDER BY cantidad_clientes desc");
            while (rs.next()) {
                String cantidadCliente = rs.getString("email_usuario");
                clientes.add(cantidadCliente);
            }
        } catch (Exception e) {
        }
        return clientes;
    }

    public static ArrayList<String> ordenarListasPorCantidadClientesUrl() {
        ArrayList<String> clientes = new ArrayList<>();
        conectarBD();
        try {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT ruta_img FROM Lista_Reproduccion ORDER BY cantidad_clientes desc");
            while (rs.next()) {
                String cantidadCliente = rs.getString("ruta_img");
                clientes.add(cantidadCliente);
            }
        } catch (Exception e) {
        }
        return clientes;
    }


    public static boolean actualizarNombreListaActual(String nombreLista, String generoLista, String usuarioLista, String urlLista) {
        boolean actualizo = false;
        conectarBD();
        try {
            Statement s = con.createStatement();
            s.executeUpdate("UPDATE ListaActual SET id='1', nombre='" + nombreLista + "' , usuario_lista='" + usuarioLista + "' , genero_lista='" + generoLista + "' , url_img='" + urlLista + "' WHERE id= '1'");
            actualizo = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return actualizo;
    }

    public static Lista darListaActual() {
        Lista listaActual = null;
        conectarBD();
        try {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT nombre, usuario_lista, genero_lista, url_img FROM ListaActual WHERE id='1'");
            if (rs.next()) {
                String nombre = rs.getString("nombre");
                String usuario = rs.getString("usuario_lista");
                String genero = rs.getString("genero_lista");
                String url = rs.getString("url_img");
                ArrayList<Cancion> canciones = new ArrayList<>();
                listaActual = new Lista(nombre, usuario, url, genero,canciones);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaActual;
    }

    public static Cancion buscarCancion(String idCancion) {
        Cancion cancion = null;
        try {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM Cancion WHERE num= '" + idCancion + "'");
            if (rs.next()) {
                String id = rs.getString("num");
                String nombre = rs.getString("nombre");
                String artista = rs.getString("artista");
                String album = rs.getString("album");
                String url_imagen = rs.getString("url_imagen");
                String url_letra = rs.getString("url_letra_cancion");
                String url_video = rs.getString("url_video");
                String mp3 = rs.getString("url_mp3");
                String ogg = rs.getString("url_ogg");
                String genero = rs.getString("genero");
                cancion = new Cancion(id, nombre, artista, album, url_imagen, url_letra, url_video, mp3, ogg, genero);
            }
        } catch (Exception e) {
        }
        return cancion;
    }

    public static ArrayList<Cancion> cargarTodasCancionesServidor() {
        ArrayList<Cancion> canciones = new ArrayList<>();
        try {
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM Cancion");
            while (rs.next()) {
                String id = rs.getString("num");
                String nombre = rs.getString("nombre");
                String artista = rs.getString("artista");
                String album = rs.getString("album");
                String url_imagen = rs.getString("url_imagen");
                String url_letra = rs.getString("url_letra_cancion");
                String url_video = rs.getString("url_video");
                String mp3 = rs.getString("url_mp3");
                String ogg = rs.getString("url_ogg");
                String genero = rs.getString("genero");
                Cancion cancion = new Cancion(id, nombre, artista, album, url_imagen, url_letra, url_video, mp3, ogg, genero);
                canciones.add(cancion);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return canciones;
    }
}
