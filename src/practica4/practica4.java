package practica4;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class practica4 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String nombre;
        ArrayList<String> texto;
        ManejoArchivo ma = new ManejoArchivo();
        ArrayList<Linea> lineas = new ArrayList<>();

        System.out.print("Archivo a leer (ej. Prueba.txt): ");
        nombre = sc.nextLine();

        try {
            lineas = ma.leer(nombre);
            AnalizadorLexico al = new AnalizadorLexico(lineas);
            for (Linea linea : lineas) {
                al.analizar(linea);
            }
            al.imprimirTablaTokens();
        } catch (FileNotFoundException e) {
            System.out.println("No existe el archivo");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
