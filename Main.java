import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

import lexico.AnalizadorLexico;
import lexico.ManejoArchivo;
import lexico.Linea;
import sintactico.AnalizadorSintactico;
import utils.Token;
import semantico.AnalizadorSemantico;

public class Main {
    public static void main(String[] args) throws IOException {
        String nombre = "";
        Scanner sc = new Scanner(System.in);
        ManejoArchivo ma = new ManejoArchivo();
        AnalizadorLexico lexico = new AnalizadorLexico();
        AnalizadorSintactico analizadorSintactico;
        AnalizadorSemantico analizadorSemantico;
        ArrayList<Linea> lineas;
        ArrayList<Token> lexemas;

        File buildDir = new File("src/build");
        if (buildDir.exists() && buildDir.isDirectory()) {
            for (File file: buildDir.listFiles()){
                if(file.isFile())
                    file.delete();
            }
        }

        while (true) {
            System.out.println("Archivo a compilar: ");
            nombre = sc.nextLine();
            try {
                // Attempt to read the file
                lineas = ma.leer("src/codes/" + nombre);
                break; // Exit the loop if the file is read successfully
            } catch (IOException e) {
                System.err.println((char) 27 + "[31m" + "El archivo: " + nombre + " no existe o esta mal escrito, los nombres del programa deben empezar en mayuscula" + (char) 27 + "[0m");
            }
        }

        File tablatokens = new File("src/build/TablaTokens.txt"); // Nombre del archivo de la tabla de tokens
        FileWriter writerTokens = new FileWriter(tablatokens);

        for(Linea linea : lineas) {
            lexemas = lexico.analizador(linea);
            lexico.analizar(lexemas);
        }

        if(lexico.notErrorLexico()) {
            lexico.imprimirTablaTokens(writerTokens);
            System.out.println("Tabla de tokens generada en build/TablaTokens.txt");
        }
        else {
            System.err.println("No se puede compilar el programa por errores lexicos");
            System.out.println("Tabla de errores generada en build/TablaErrores.txt");
        }

        // Cerrar el escritor
        writerTokens.close();

        lexemas = lexico.getTokens();
        analizadorSintactico = new AnalizadorSintactico(lexemas);

        if(lexico.notErrorLexico()){
            analizadorSintactico.analizar();
        }

        analizadorSemantico = new AnalizadorSemantico(lexemas);

        if(!analizadorSintactico.getErrotSintactico()){
            analizadorSemantico.analizar();
        }

        sc.close();
    }
}
