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
        nombre = sc.nextLine();
        boolean errorSintactico = false;
        

        ManejoArchivo ma = new ManejoArchivo();
        AnalizadorLexico lexico = new AnalizadorLexico();
        AnalizadorSintactico analizadorSintactico;
        AnalizadorSemantico analizadorSemantico;
        ArrayList<Linea> lineas;
        ArrayList<Token> lexemas;

        String ruta = "src/codes/" + nombre;

        try{
            lineas = ma.leer(ruta);
            File tablatokens = new File("src/TablaTokens.txt"); // Nombre del archivo de la tabla de tokens
            FileWriter writerTokens = new FileWriter(tablatokens);

            for(Linea linea : lineas) {
                lexemas = lexico.analizador(linea);
                lexico.analizar(lexemas);
            }

            if(lexico.notErrorLexico()) {
                lexico.imprimirTablaTokens(writerTokens);
                System.out.println("Tabla de tokens guardada en 'TablaTokens.txt'");
            }
            else {
                System.err.println("No se puede compilar el programa por errores lexicos");
                System.out.println("Tabla de errores guardada en 'TablaErrores.txt");
            }

            // Cerrar el escritor
            writerTokens.close();
        } catch (Exception e) {
            System.err.println("Error al escribir en el archivo de salida");
        }

        lexemas = lexico.getTokens();
        analizadorSintactico = new AnalizadorSintactico(lexemas);

        if(lexico.notErrorLexico()){
            analizadorSintactico.analizar();
        }

        analizadorSemantico = new AnalizadorSemantico(lexemas);

        analizadorSemantico.analizar();

        sc.close();
    }
}
