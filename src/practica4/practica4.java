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
            File tablatokens = new File("src/practica4/TablaTokens.txt"); // Nombre del archivo de la tabla de tokens
            FileWriter writerTokens = new FileWriter(tablatokens);
            File tablaErrores = new File("src/practica4/TablaErrores.txt"); // Nombre del archivo de la tabla de errores
            FileWriter writerErrores = new FileWriter(tablaErrores);

            for (Linea linea : lineas) {
                lexemas = analizador.analizador(linea);
                analizador.analizar(lexemas, writerErrores);
            }

            // Escribir los resultados en el archivo de salida
            analizador.imprimirTablaTokens(writerTokens);

            // Cerrar el escritor
            writerTokens.close();
            writerErrores.close();
            System.out.println("Tabla de tokens guardada en 'TablaTokens.txt'");
            System.out.println("Tabla de errores guardada en 'TablaErrores.txt");
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo de salida");
        }
    }
}
