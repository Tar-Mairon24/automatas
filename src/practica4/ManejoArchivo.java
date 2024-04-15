package practica4;

import java.io.*;
import java.util.ArrayList;

public class ManejoArchivo {
    private String ruta;
    private ArrayList<Linea> lineas;
    public ManejoArchivo(){
        ruta = "src/practica4/prueba.txt";
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
    public ArrayList<Linea> leer() throws IOException {
        lineas =  new ArrayList<>();
        File file = new File(ruta);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        Linea linea;
        int numeroLinea = 0;
        try{
            String line;
            while((line = br.readLine()) != null){
                linea = new Linea(line, numeroLinea);
                numeroLinea++;
                lineas.add(linea);
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
