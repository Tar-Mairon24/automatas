package sintactico;

import java.io.*;
import java.util.ArrayList;

public class practica5 {
    public static void main(String[] args) {
        // Inicializamos el analizador sintáctico
        AnalizadorSintactico analizadorSintactico;

        // Creamos un ArrayList para almacenar los tokens
        ArrayList<Token> tokens = new ArrayList<>();

        // Definimos la ruta al archivo que contiene los tokens
        String ruta = "src/lexico/TablaTokens.txt";

        // Creamos un objeto File con la ruta especificada
        File file = new File(ruta);

        // Usamos una declaración try-with-resources para abrir el archivo y leer su contenido
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            // Declaramos un objeto Token y una cadena para contener cada línea del archivo
            Token token;
            String line;

            // Leemos el archivo línea por línea
            while ((line = br.readLine()) != null) {
                // Dividimos cada línea en partes usando un espacio como delimitador
                String[] parts = line.split(" ");

                // Creamos un nuevo objeto Token con las partes y lo añadimos a la lista de tokens
                token = new Token(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
                tokens.add(token);
            }
        } catch (IOException e) {
            // Manejamos cualquier IOException que pueda ocurrir durante las operaciones de archivo
            System.out.println("Ocurrió un error");
        }

        // Creamos un nuevo analizador sintáctico con la lista de tokens
        analizadorSintactico = new AnalizadorSintactico(tokens);

        // Analizamos los tokens e imprimimos un mensaje dependiendo del resultado
        if(analizadorSintactico.analizar()) {
            System.out.println("El programa se ha compilado correctamente");
        }
        else {
            System.out.println("No se puede compilar el programa por errores sintacticos");
        }
    }
}

