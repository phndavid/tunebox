
package controller;
import model.Usuario;
import com.google.gson.Gson; 
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Controller {

    
    private Usuario usuario;
    private static ConexionBD conexionBD; 
    /**
     * 
     * @param nombreUsuario
     * @param emailUsuario 
     */
    public Controller(String nombreUsuario, String emailUsuario){ 
        usuario = new Usuario(emailUsuario,nombreUsuario);
        if(conexionBD==null)
            conexionBD = new ConexionBD();
        //cargarInfoUsuario(usuario);
        //escribirArchivoJsonTodasLasCanciones();
        
    }
    public static void main(String[] args){
        conexionBD = new ConexionBD();
        escribirArchivoJsonTodasLasCanciones();
    }
    /**
     * 
     */
    public static void escribirArchivoJsonTodasLasCanciones(){
        try {
            FileWriter writer;
            writer = new FileWriter("./web/json/play.json");
            Gson json = new Gson();
            json.toJson(conexionBD.cargarTodasCancionesServidor(),writer);
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            //cargo = "No lo hizo, pero ejecuto el metodo"; 
        }
    }
    
    
}    
