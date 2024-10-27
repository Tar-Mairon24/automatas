package lexico;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import utils.Token;

public class practica4 {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        String nombre;
        ArrayList<Linea> lineas;
        ManejoArchivo ma = new ManejoArchivo();
        AnalizadorLexico analizador = new AnalizadorLexico();
        List<Token> lexemas;

        String ruta = "src/codes/";
        System.out.println("Ingrese el nombre del archivo a leer(ejemplo: prueba.txt): ");
        nombre = sc.nextLine();
        ruta = ruta + nombre;

        try {
            lineas = ma.leer(ruta);
            File tablatokens = new File("src/lexico/TablaTokens.txt"); // Nombre del archivo de la tabla de tokens
            FileWriter writerTokens = new FileWriter(tablatokens);

            for (Linea linea : lineas) {
                lexemas = analizador.analizador(linea);
                analizador.analizar(lexemas);
            }

            if(analizador.notErrorLexico()) {
                analizador.imprimirTablaTokens(writerTokens);
                System.out.println("Tabla de tokens guardada en 'TablaTokens.txt'");
            }
            else {
                System.out.println("No se puede compilar el programa por errores lexicos");
                System.out.println("Tabla de errores guardada en 'TablaErrores.txt");
            }
            // Cerrar el escritor
            writerTokens.close();
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo de salida");
        }

        sc.close();
    }
}
