package practica4;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class practica4 {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        String nombre;
        ArrayList<Linea> lineas;
        ManejoArchivo ma = new ManejoArchivo();
        AnalizadorLexico analizador = new AnalizadorLexico();
        List<String> lexemas;
        int nuemroLinea = 0;


        System.out.print("Archivo a leer (ej. Prueba.txt): ");
        nombre = sc.nextLine();

        try{
            lineas = ma.leer(nombre);
            for(Linea linea : lineas) {
                lexemas = analizador.analizador(linea.getLinea());
                for(String lexema : lexemas){
                    System.out.println(lexema);
                }
                lexemas.clear();
            }
        } catch (FileNotFoundException e) {
            System.out.println("No existe el archivo");
        } catch (IOException e) {
            throw new RuntimeException(e);
    }
    }
}
