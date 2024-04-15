package practica4;

import java.io.*;
import java.util.ArrayList;

public class ManejoArchivo {
    private String ruta;
    private ArrayList<Linea> lineas;
    public ManejoArchivo(){
        ruta = "src/practica4/";
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public void setLineas(ArrayList<Linea> lineas) {
        this.lineas = lineas;
    }

    public ArrayList<Linea> getLineas() {
        return lineas;
    }

    public String getRuta() {
        return ruta;
    }

    /*
   Metodo para leer, devuelve un arraylist con un objeto en el cual se guarda la linea y el numero de linea
   Estos parametros pueden ser accesados mediante sus metodos get de la clase
   @param nombre del archivo a leer
    */
    public ArrayList<Linea> leer(String nombre) throws IOException {
        ruta += nombre;
        lineas = new ArrayList<>();
        File file = new File(ruta);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        Linea linea;
        int numeroLinea = 1;
        try {
            String line;
            boolean comentarioAbierto = false; // Variable para ver si estamos dentro de un comentario
            while ((line = br.readLine()) != null) {
                // Verificar si la línea es un comentario
                if (esComentario(line, comentarioAbierto)) {
                	numeroLinea++;
                    continue; // Saltar esta linea y pasar a la siguiente
                }
                linea = new Linea(line, numeroLinea);
                numeroLinea++;
                lineas.add(linea);
            }
            br.close();
            return lineas;
        } catch (IOException e) {
            System.out.println("Ocurrió un error");
        } finally {
            if (br != null) {
                br.close();
            }
        }
        return lineas;
    }

    // Método para verificar si una línea es un comentario no vacío
    private boolean esComentario(String line, boolean comentarioAbierto) {
        line = line.trim();
        if (line.startsWith("//")) {
            if (comentarioAbierto && line.endsWith("//")) {
                return false;
            } else {
                return true;
            }
        } else if (line.endsWith("//")) {
            return true;
        }
        return false;
    }

}