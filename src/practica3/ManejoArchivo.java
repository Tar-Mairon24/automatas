package practica3;

import java.io.*;
import java.util.ArrayList;

public class ManejoArchivo {
    private String ruta;
    private ArrayList<String> lineas;
    public ManejoArchivo(){
        ruta = "src/practica3/";
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public void setLineas(ArrayList<String> lineas) {
        this.lineas = lineas;
    }

    public ArrayList<String> getLineas() {
        return lineas;
    }

    public String getRuta() {
        return ruta;
    }

    /*
   Metodo para leer, devuelve un arraylist con todas las lineas del archivo, para poder usarlas individualmente
   @param nombre del archivo a leer
    */
    public ArrayList<String> leer(String nombre) throws IOException {
        ruta += nombre;
        lineas =  new ArrayList<>();
        File file = new File(ruta);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        try{
            String line;
            while((line = br.readLine()) != null){
                lineas.add(line);
            }
            br.close();
            return lineas;
        }catch(IOException e){
            System.out.println("Ocurrio un error");
        }
        br.close();
        return lineas;
    }
}
