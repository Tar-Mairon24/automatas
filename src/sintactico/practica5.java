package sintactico;

import lexico.AnalizadorLexico;
import lexico.Linea;
import lexico.ManejoArchivo;
import lexico.Token;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class practica5 {
    public static void main(String[] args) throws IOException {
        ArrayList<Linea> lineas;
        ManejoArchivo ma = new ManejoArchivo();
        AnalizadorLexico analizadorLexico = new AnalizadorLexico();
        List<Token> lexemas, tablaTokens;
        AnalizadorSintactico analizadorSintactico;

        try {
            lineas = ma.leer("src/sintactico/programa.txt");
            File tablatokens = new File("src/sintactico/TablaTokens.txt"); // Nombre del archivo de la tabla de tokens
            FileWriter writerTokens = new FileWriter(tablatokens);

            for (Linea linea : lineas) {
                lexemas = analizadorLexico.analizador(linea);
                analizadorLexico.analizar(lexemas);
            }

            if(analizadorLexico.isErrorLexico()) {
                analizadorLexico.imprimirTablaTokens(writerTokens);
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

        analizadorSintactico = new AnalizadorSintactico(analizadorLexico.getTablaTokens());
        System.out.println(analizadorSintactico.analizar());


    }
}

