package prueba;

import lexico.AnalizadorLexico;
import lexico.Linea;
import lexico.ManejoArchivo;
import lexico.Token;
import sintactico.AnalizadorSintactico;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Prueba {
    public static void main(String[] args) throws IOException {
        AnalizadorLexico al = new AnalizadorLexico();
        ArrayList<Linea> lineas;
        ManejoArchivo ma = new ManejoArchivo();
        AnalizadorLexico analizador = new AnalizadorLexico();
        List<Token> lexemas;
        try {
            lineas = ma.leer("src/lexico/programa.txt");
            File tablatokens = new File("src/lexico/TablaTokens.txt"); // Nombre del archivo de la tabla de tokens
            FileWriter writerTokens = new FileWriter(tablatokens);

            for (Linea linea : lineas) {
                lexemas = analizador.analizador(linea);
                analizador.analizar(lexemas);
            }

            if(analizador.isErrorLexico()) {
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

        ArrayList<sintactico.Token> tokens = new ArrayList<>();

        // Definimos la ruta al archivo que contiene los tokens
        String ruta = "src/lexico/TablaTokens.txt";

        // Creamos un objeto File con la ruta especificada
        File file = new File(ruta);

        // Usamos una declaración try-with-resources para abrir el archivo y leer su contenido
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            // Declaramos un objeto Token y una cadena para contener cada línea del archivo
            sintactico.Token token;
            String line;

            // Leemos el archivo línea por línea
            while ((line = br.readLine()) != null) {
                // Dividimos cada línea en partes usando un espacio como delimitador
                String[] parts = line.split(" ");

                // Creamos un nuevo objeto Token con las partes y lo añadimos a la lista de tokens
                token = new sintactico.Token(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
                tokens.add(token);
            }
        } catch (IOException e) {
            // Manejamos cualquier IOException que pueda ocurrir durante las operaciones de archivo
            System.out.println("Ocurrió un error");
        }

        // Creamos un nuevo analizador sintáctico con la lista de tokens
        PruebaSintactico analizadorSintactico = new PruebaSintactico(tokens);

        // Analizamos los tokens e imprimimos un mensaje dependiendo del resultado
        if(analizadorSintactico.analizar()) {
            System.out.println("El programa se ha compilado correctamente");
        }
        else {
            System.out.println("No se puede compilar el programa por errores sintacticos");
        }
    }
}
