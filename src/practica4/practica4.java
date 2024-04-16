package practica4;

import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class practica4 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String nombre;
        ArrayList<Linea> lineas;
        ManejoArchivo ma = new ManejoArchivo();
        AnalizadorLexico analizador = new AnalizadorLexico();
        List<Token> lexemas;

        System.out.print("Archivo a leer (ej. Prueba.txt): ");
        nombre = sc.nextLine();

        try {
            lineas = ma.leer(nombre);
            File outputFile = new File("src/practica4/resultado.txt"); // Nombre del archivo de salida
            FileWriter writer = new FileWriter(outputFile);

            for (Linea linea : lineas) {
                lexemas = analizador.analizador(linea);
                analizador.analizar(lexemas);
            }

            // Escribir los resultados en el archivo de salida
            analizador.imprimirTablaTokens(writer);

            // Cerrar el escritor
            writer.close();
            System.out.println("Resultados guardados en 'resultado.txt'");
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo de salida");
        }
    }
}
