package practica3;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Practica3 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String nombre;

        System.out.println("Archivo a leer: ");

    }

    private static ArrayList<String> leer(String nombre) throws FileNotFoundException {
        ArrayList<String> lineas =  new ArrayList<>();
        String ruta = "src/practica3" + nombre + ".txt";
        File file = new File(ruta);
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

        try{
            String line;
            while((line = br.readLine()) != null){
                lineas.add(line);
            }
            return lineas;
        }catch(IOException e){
            System.out.println("Ocurrio un error");
        }
        return lineas;
    }
}
